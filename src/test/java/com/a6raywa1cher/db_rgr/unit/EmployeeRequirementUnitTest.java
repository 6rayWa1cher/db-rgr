package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.EntityFactory;
import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.EmployeeRequirement;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeRequirementRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeRequirementUnitTest extends DatabaseInitializedTest {
	static EmployeeRequirementRepository repository;
	static EntityFactory entityFactory;
//	Department sampleDepartment = new Department("1", "2", "3");
//	EmployeeType sampleEmployeeType = new EmployeeType("j", "k", 5);

	@BeforeAll
	static void init() {
		entityFactory = new EntityFactory(connector);
		repository = new EmployeeRequirementRepository(connector);
	}

	@Test
	void testInsertAndGetAll() {
		EmployeeRequirement requirement = entityFactory.createEmployeeRequirement();

		EmployeeRequirement result = repository.getAll().get(0);

		assertEquals(requirement, result);
	}

	@Test
	void testGetById() {
		EmployeeRequirement requirement = entityFactory.createEmployeeRequirement();

		EmployeeRequirement result = repository.getById(
			requirement.getDepartmentTitle(), requirement.getEmployeeRank(), requirement.getPosition()
		);

		assertEquals(requirement, result);
	}

	@Test
	void testCount() {
		long count = new Random().nextLong(300);
		for (int i = 0; i < count; i++) {
			entityFactory.createEmployeeRequirement();
		}

		long result = repository.count();

		assertEquals(count, result);
	}

	@Test
	void testUpdate() {
		EmployeeRequirement requirement = entityFactory.createEmployeeRequirement();

		requirement.setCount(Math.max(requirement.getCount() + 1, 1));

		repository.update(requirement);

		EmployeeRequirement result = repository.getAll().get(0);

		assertEquals(requirement, result);
	}

	@Test
	void testUpdate__doesnt_update_others() {
		EmployeeRequirement requirement1 = entityFactory.createEmployeeRequirement();
		EmployeeRequirement requirement2 = entityFactory.createEmployeeRequirement();

		requirement1.setEmployeeRank(requirement2.getEmployeeRank());
		requirement1.setPosition(requirement2.getPosition());
		requirement1.setDepartmentTitle(entityFactory.createDepartment(
			Department.builder()
				.title("a0")
				.build()
		).getTitle());

		repository.update(requirement1);

		List<EmployeeRequirement> resultList = repository.getAll();
		resultList.sort(Comparator.comparing(EmployeeRequirement::getDepartmentTitle));
		EmployeeRequirement result1 = resultList.get(0);
		EmployeeRequirement result2 = resultList.get(1);

		assertEquals(requirement1, result1);
		assertEquals(requirement2, result2);
	}

	@Test
	void testDelete() {
		EmployeeRequirement requirement = entityFactory.createEmployeeRequirement();

		repository.delete(requirement);

		assertEquals(0, repository.count());
	}

	@Test
	void testDelete__doesnt_delete_others() {
		EmployeeRequirement requirement1 = entityFactory.createEmployeeRequirement();
		EmployeeRequirement requirement2 = entityFactory.createEmployeeRequirement();

		repository.delete(requirement1);

		assertEquals(1, repository.count());
		assertEquals(requirement2, repository.getById(
			requirement2.getDepartmentTitle(), requirement2.getEmployeeRank(), requirement2.getPosition()
		));
	}
}
