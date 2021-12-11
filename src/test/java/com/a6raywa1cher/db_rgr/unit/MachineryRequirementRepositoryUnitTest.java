package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.EntityFactory;
import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.EmployeeRequirement;
import com.a6raywa1cher.db_rgr.model.MachineryRequirement;
import com.a6raywa1cher.db_rgr.model.MachineryType;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import com.a6raywa1cher.db_rgr.model.repository.MachineryRequirementRepository;
import com.a6raywa1cher.db_rgr.model.repository.MachineryTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MachineryRequirementRepositoryUnitTest extends DatabaseInitializedTest {
	static MachineryRequirementRepository repository;
	static MachineryTypeRepository machineryTypeRepository;
	static DepartmentRepository departmentRepository;
	static EntityFactory entityFactory;

	@BeforeAll
	static void init() {
		entityFactory = new EntityFactory(em);
		repository = new MachineryRequirementRepository(em);
		departmentRepository = new DepartmentRepository(em);
		machineryTypeRepository = new MachineryTypeRepository(em);
	}

	@Test
	void testInsertAndGetAll() {
		MachineryRequirement requirement = entityFactory.createMachineryRequirement();

		MachineryRequirement result = repository.getAll().get(0);

		assertEquals(requirement, result);
	}

	@Test
	void testGetById() {
		MachineryRequirement requirement = entityFactory.createMachineryRequirement();

		MachineryRequirement result = repository.getById(
				requirement.getDepartmentTitle(), requirement.getMachineryTitle()
		);

		assertEquals(requirement, result);
	}

	@Test
	void testCount() {
		long count = new Random().nextLong(300);
		for (int i = 0; i < count; i++) {
			entityFactory.createMachineryRequirement();
		}

		long result = repository.count();

		assertEquals(count, result);
	}

	@Test
	void testUpdate() {
		MachineryRequirement requirement = entityFactory.createMachineryRequirement();

		requirement.setCount(Math.max(requirement.getCount() + 1, 1));

		repository.update(requirement);

		MachineryRequirement result = repository.getAll().get(0);

		assertEquals(requirement, result);
	}

	@Test
	void testUpdate__doesntUpdateOthers() {
		MachineryRequirement requirement1 = entityFactory.createMachineryRequirement();
		MachineryRequirement requirement2 = entityFactory.createMachineryRequirement();

		requirement1.setMachineryTitle(requirement2.getMachineryTitle());
		requirement1.setDepartmentTitle(entityFactory.createDepartment(
				Department.builder()
						.title("a0")
						.build()
		).getTitle());

		repository.update(requirement1);

		List<MachineryRequirement> resultList = repository.getAll();
		resultList.sort(Comparator.comparing(MachineryRequirement::getDepartmentTitle));
		MachineryRequirement result1 = resultList.get(0);
		MachineryRequirement result2 = resultList.get(1);

		assertEquals(requirement1, result1);
		assertEquals(requirement2, result2);
	}

	@Test
	void testDelete() {
		MachineryRequirement requirement = entityFactory.createMachineryRequirement();

		repository.delete(requirement);

		assertEquals(0, repository.count());
	}

	@Test
	void testDelete__doesntDeleteOthers() {
		MachineryRequirement requirement1 = entityFactory.createMachineryRequirement();
		MachineryRequirement requirement2 = entityFactory.createMachineryRequirement();

		repository.delete(requirement1);

		assertEquals(1, repository.count());
		assertEquals(requirement2, repository.getById(
				requirement2.getDepartmentTitle(), requirement2.getMachineryTitle()
		));
	}

	@Test
	void testDelete__deleteDepartmentThenCascade() {
		EmployeeRequirement requirement = entityFactory.createEmployeeRequirement();

		Department department = departmentRepository.getById(requirement.getDepartmentTitle());

		departmentRepository.delete(department);

		assertEquals(0, repository.count());
	}

	@Test
	void testDelete__deleteEmployeeTypeThenCascade() {
		MachineryRequirement requirement = entityFactory.createMachineryRequirement();

		MachineryType machineryType = machineryTypeRepository.getById(requirement.getMachineryTitle());

		machineryTypeRepository.delete(machineryType);

		assertEquals(0, repository.count());
	}
}
