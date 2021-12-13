package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.EntityFactory;
import com.a6raywa1cher.db_rgr.model.*;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartmentRepositoryUnitTest extends DatabaseInitializedTest {
	static DepartmentRepository repository;
	static EntityFactory entityFactory;

	@BeforeAll
	static void createDepartmentRepository() {
		repository = new DepartmentRepository(em);
		entityFactory = new EntityFactory(em);
	}

	@Test
	void testInsertAndGetAll() {
		Department department = new Department("a", "b", "c");
		repository.insert(department);

		Department result = repository.getAll().get(0);

		assertEquals(department, result);
	}

	@Test
	void testGetById() {
		Department department = new Department("a", "b", "c");
		repository.insert(department);

		Department result = repository.getById("a");

		assertEquals(department, result);
	}

	@Test
	void testCount() {
		long count = new Random().nextLong(300);
		for (int i = 0; i < count; i++) {
			Department department = new Department("a#" + i, "b", "c");
			repository.insert(department);
		}

		long result = repository.count();

		assertEquals(count, result);
	}

	@Test
	void testUpdate() {
		Department department = new Department("a", "b", "c");
		repository.insert(department);

		department.setTitle("e");
		department.setTelephoneNumber("f");
		department.setAddress("g");

		repository.update(department);

		Department result = repository.getAll().get(0);

		assertEquals(department, result);
	}

	@Test
	void testUpdate__doesnt_update_others() {
		Department department1 = new Department("a", "b", "c");
		repository.insert(department1);
		Department department2 = new Department("x", "y", "z");
		repository.insert(department2);

		department1.setTitle("e");
		department1.setTelephoneNumber("f");
		department1.setAddress("g");

		repository.update(department1);

		List<Department> resultList = repository.getAll();
		resultList.sort(Comparator.comparing(Department::getTitle));
		Department result1 = resultList.get(0);
		Department result2 = resultList.get(1);

		assertEquals(department1, result1);
		assertEquals(department2, result2);
	}

	@Test
	void testDelete() {
		Department department = new Department("a", "b", "c");
		repository.insert(department);

		repository.delete(department);

		assertEquals(0, repository.count());
	}

	@Test
	void testGetEmployeeTypeStats() {
		Department department = entityFactory.createDepartment();

		EmployeeRequirement requirement1 = entityFactory.createEmployeeRequirement(
			EmployeeRequirement.builder()
				.departmentTitle(department.getTitle())
				.count(2)
				.build()
		);

		EmployeeRequirement requirement2 = entityFactory.createEmployeeRequirement(
			EmployeeRequirement.builder()
				.departmentTitle(department.getTitle())
				.count(1)
				.build()
		);
		entityFactory.createEmployee(
			Employee.builder()
				.departmentTitle(department.getTitle())
				.employeeRank(requirement2.getEmployeeRank())
				.position(requirement2.getPosition())
				.build()
		);

		List<DepartmentRepository.EmployeeTypeStats> employeeTypeStats = repository.getEmployeeTypeStats(department);
		employeeTypeStats.sort(Comparator.comparing(DepartmentRepository.EmployeeTypeStats::getExpectedCount));
		var stat1 = employeeTypeStats.get(1);
		var stat2 = employeeTypeStats.get(0);

		assertEquals(requirement1.getEmployeeRank(), stat1.getEmployeeRank());
		assertEquals(requirement1.getPosition(), stat1.getPosition());
		assertEquals(Long.valueOf(requirement1.getCount()), stat1.getExpectedCount());
		assertEquals(0, stat1.getRealCount());

		assertEquals(requirement2.getEmployeeRank(), stat2.getEmployeeRank());
		assertEquals(requirement2.getPosition(), stat2.getPosition());
		assertEquals(Long.valueOf(requirement2.getCount()), stat2.getExpectedCount());
		assertEquals(1, stat2.getRealCount());
	}

	@Test
	void testGetMachineryTypeStats() {
		Department department = entityFactory.createDepartment();

		MachineryRequirement requirement1 = entityFactory.createMachineryRequirement(
			MachineryRequirement.builder()
				.departmentTitle(department.getTitle())
				.count(2)
				.build()
		);

		MachineryRequirement requirement2 = entityFactory.createMachineryRequirement(
			MachineryRequirement.builder()
				.departmentTitle(department.getTitle())
				.count(1)
				.build()
		);
		entityFactory.createMachinery(
			Machinery.builder()
				.departmentTitle(department.getTitle())
				.machineryTitle(requirement2.getMachineryTitle())
				.build()
		);

		List<DepartmentRepository.MachineryTypeStats> stats = repository.getMachineryTypeStats(department);
		stats.sort(Comparator.comparing(DepartmentRepository.MachineryTypeStats::getExpectedCount));
		var stat1 = stats.get(1);
		var stat2 = stats.get(0);

		assertEquals(requirement1.getMachineryTitle(), stat1.getMachineryTitle());
		assertEquals(Long.valueOf(requirement1.getCount()), stat1.getExpectedCount());
		assertEquals(0, stat1.getRealCount());

		assertEquals(requirement2.getMachineryTitle(), stat2.getMachineryTitle());
		assertEquals(Long.valueOf(requirement2.getCount()), stat2.getExpectedCount());
		assertEquals(1, stat2.getRealCount());
	}
}
