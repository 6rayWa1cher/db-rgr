package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.EntityFactory;
import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.Employee;
import com.a6raywa1cher.db_rgr.model.Machinery;
import com.a6raywa1cher.db_rgr.model.MachineryType;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeRepository;
import com.a6raywa1cher.db_rgr.model.repository.MachineryRepository;
import com.a6raywa1cher.db_rgr.model.repository.MachineryTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MachineryRepositoryUnitTest extends DatabaseInitializedTest {
	static MachineryRepository repository;
	static MachineryTypeRepository machineryTypeRepository;
	static EmployeeRepository employeeRepository;
	static DepartmentRepository departmentRepository;
	static EntityFactory entityFactory;

	@BeforeAll
	static void init() {
		entityFactory = new EntityFactory(em);
		repository = new MachineryRepository(em);
		departmentRepository = new DepartmentRepository(em);
		machineryTypeRepository = new MachineryTypeRepository(em);
		employeeRepository = new EmployeeRepository(em);
	}

	@Test
	void testInsertAndGetAll() {
		Machinery machinery = entityFactory.createMachinery();

		Machinery result = repository.getAll().get(0);

		assertEquals(machinery, result);
	}

	@Test
	void testGetById() {
		Machinery machinery = entityFactory.createMachinery();

		Machinery result = repository.getById(
			machinery.getDepartmentTitle(), machinery.getId(), machinery.getMachineryTitle()
		);

		assertEquals(machinery, result);
	}

	@Test
	void testCount() {
		long count = new Random().nextLong(300);
		for (int i = 0; i < count; i++) {
			entityFactory.createMachinery();
		}

		long result = repository.count();

		assertEquals(count, result);
	}

	@Test
	void testUpdate() {
		Machinery machinery = entityFactory.createMachinery();

		machinery.setDepartmentTitle(entityFactory.createDepartment().getTitle());
		machinery.setId(machinery.getId() + 1);
		MachineryType newEmployeeType = entityFactory.createMachineryType();
		machinery.setMachineryTitle(newEmployeeType.getMachineryTitle());
		Employee newHolder = entityFactory.createEmployee(
			Employee.builder()
				.departmentTitle(machinery.getDepartmentTitle())
				.build()
		);
		machinery.setHolderName(newHolder.getFullName());
		machinery.setDateOfPurchase(LocalDate.now().minusDays(2));

		repository.update(machinery);

		Machinery result = repository.getAll().get(0);

		assertEquals(machinery, result);
	}

	@Test
	void testUpdate__doesntUpdateOthers() {
		Machinery machinery1 = entityFactory.createMachinery();
		Machinery machinery2 = entityFactory.createMachinery();

		machinery1.setHolderName(null);
		machinery1.setDateOfPurchase(LocalDate.now().minusYears(500));

		repository.update(machinery1);

		List<Machinery> resultList = repository.getAll();
		resultList.sort(Comparator.comparing(Machinery::getDateOfPurchase));
		Machinery result1 = resultList.get(0);
		Machinery result2 = resultList.get(1);

		assertEquals(machinery1, result1);
		assertEquals(machinery2, result2);
	}

	@Test
	void testDelete() {
		Machinery machinery = entityFactory.createMachinery();

		repository.delete(machinery);

		assertEquals(0, repository.count());
	}

	@Test
	void testDelete__doesntDeleteOthers() {
		Machinery machinery1 = entityFactory.createMachinery();
		Machinery machinery2 = entityFactory.createMachinery();

		repository.delete(machinery1);

		assertEquals(1, repository.count());
		assertEquals(machinery2, repository.getById(
			machinery2.getDepartmentTitle(), machinery2.getId(), machinery2.getMachineryTitle()
		));
	}

	@Test
	void testDelete__deleteDepartmentThenCascade() {
		Machinery machinery = entityFactory.createMachinery();

		Department department = departmentRepository.getById(machinery.getDepartmentTitle());

		departmentRepository.delete(department);

		assertEquals(0, repository.count());
	}

	@Test
	void testDelete__deleteMachineryTypeThenRestrict() {
		Machinery machinery = entityFactory.createMachinery();

		MachineryType machineryType = machineryTypeRepository.getById(machinery.getMachineryTitle());

		assertThrows(SQLException.class, () -> machineryTypeRepository.delete(machineryType));
	}

//	@Test
//	void testDelete__deleteEmployeeThenSetNull() {
//		Machinery machinery = entityFactory.createMachinery();
//
//		Employee employee = employeeRepository.getById(machinery.getHolderName(), machinery.getDepartmentTitle());
//
//		assertThrows(SQLException.class, () -> employeeRepository.delete(employee));
//	}

	@Test
	void testDelete__deleteEmployeeThenSetNull() {
		Machinery machinery = entityFactory.createMachinery();

		Employee employee = employeeRepository.getById(machinery.getHolderName(), machinery.getDepartmentTitle());

		employeeRepository.delete(employee);

		repository.updateFromDb(machinery);

		assertNull(machinery.getHolderName());
	}

	@Test
	void testGetAllByHolder() {
		Employee employee = entityFactory.createEmployee();

		Machinery machinery1 = entityFactory.createMachinery(
			Machinery.builder()
				.departmentTitle(employee.getDepartmentTitle())
				.holderName(employee.getFullName())
				.build()
		);

		Machinery machinery2 = entityFactory.createMachinery(
			Machinery.builder()
				.departmentTitle(employee.getDepartmentTitle())
				.holderName(employee.getFullName())
				.build()
		);

		entityFactory.createMachinery(Machinery.builder()
			.departmentTitle(employee.getDepartmentTitle())
			.build());

		List<Machinery> machinery = repository.getAllByHolder(employee);
		machinery.sort(Comparator.comparing(Machinery::getId));
		assertEquals(2, machinery.size());
		assertEquals(machinery1, machinery.get(0));
		assertEquals(machinery2, machinery.get(1));
	}

	@Test
	void testGetAllByDepartment() {
		Department department = entityFactory.createDepartment();

		Machinery machinery1 = entityFactory.createMachinery(
			Machinery.builder()
				.departmentTitle(department.getTitle())
				.build()
		);

		Machinery machinery2 = entityFactory.createMachinery(
			Machinery.builder()
				.departmentTitle(department.getTitle())
				.build()
		);

		entityFactory.createMachinery();

		List<Machinery> machinery = repository.getAllByDepartment(department);
		machinery.sort(Comparator.comparing(Machinery::getId));
		assertEquals(2, machinery.size());
		assertEquals(machinery1, machinery.get(0));
		assertEquals(machinery2, machinery.get(1));
	}
}