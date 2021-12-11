package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.EntityFactory;
import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.Employee;
import com.a6raywa1cher.db_rgr.model.EmployeeType;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeRepository;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeRepositoryUnitTest extends DatabaseInitializedTest {
	static EmployeeRepository repository;
	static EmployeeTypeRepository employeeTypeRepository;
	static DepartmentRepository departmentRepository;
	static EntityFactory entityFactory;

	@BeforeAll
	static void init() {
		entityFactory = new EntityFactory(em);
		repository = new EmployeeRepository(em);
		departmentRepository = new DepartmentRepository(em);
		employeeTypeRepository = new EmployeeTypeRepository(em);
	}

	@Test
	void testInsertAndGetAll() {
		Employee employee = entityFactory.createEmployee();

		Employee result = repository.getAll().get(0);

		assertEquals(employee, result);
	}

	@Test
	void testGetById() {
		Employee employee = entityFactory.createEmployee();

		Employee result = repository.getById(
			employee.getFullName(), employee.getDepartmentTitle()
		);

		assertEquals(employee, result);
	}

	@Test
	void testCount() {
		long count = new Random().nextLong(300);
		for (int i = 0; i < count; i++) {
			entityFactory.createEmployee();
		}

		long result = repository.count();

		assertEquals(count, result);
	}

	@Test
	void testUpdate() {
		Employee employee = entityFactory.createEmployee();

		employee.setFullName("a");
		employee.setDepartmentTitle(entityFactory.createDepartment().getTitle());
		EmployeeType newEmployeeType = entityFactory.createEmployeeType();
		employee.setPosition(newEmployeeType.getPosition());
		employee.setEmployeeRank(newEmployeeType.getEmployeeRank());
		employee.setBirthDate(LocalDate.now().minusYears(20));
		employee.setEmploymentDate(LocalDate.now().minusYears(1));
		employee.setLastPromotionDate(LocalDate.now().minusMonths(6));

		repository.update(employee);

		Employee result = repository.getAll().get(0);

		assertEquals(employee, result);
	}

	@Test
	void testUpdate__doesntUpdateOthers() {
		Employee employee1 = entityFactory.createEmployee();
		Employee employee2 = entityFactory.createEmployee();

		employee1.setFullName("Anton");

		repository.update(employee1);

		List<Employee> resultList = repository.getAll();
		resultList.sort(Comparator.comparing(Employee::getFullName));
		Employee result1 = resultList.get(0);
		Employee result2 = resultList.get(1);

		assertEquals(employee1, result1);
		assertEquals(employee2, result2);
	}

	@Test
	void testDelete() {
		Employee employee = entityFactory.createEmployee();

		repository.delete(employee);

		assertEquals(0, repository.count());
	}

	@Test
	void testDelete__doesntDeleteOthers() {
		Employee employee1 = entityFactory.createEmployee();
		Employee employee2 = entityFactory.createEmployee();

		repository.delete(employee1);

		assertEquals(1, repository.count());
		assertEquals(employee2, repository.getById(
			employee2.getFullName(), employee2.getDepartmentTitle()
		));
	}

	@Test
	void testDelete__deleteDepartmentThenCascade() {
		Employee employee = entityFactory.createEmployee();

		Department department = departmentRepository.getById(employee.getDepartmentTitle());

		departmentRepository.delete(department);

		assertEquals(0, repository.count());
	}

	@Test
	void testDelete__deleteEmployeeTypeThenRestrict() {
		Employee employee = entityFactory.createEmployee();

		EmployeeType employeeType = employeeTypeRepository.getById(employee.getEmployeeRank(), employee.getPosition());

		assertThrows(SQLException.class, () -> employeeTypeRepository.delete(employeeType));
	}
}
