package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.model.EmployeeType;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTypeRepositoryUnitTest extends DatabaseInitializedTest {
	static EmployeeTypeRepository repository;

	@BeforeAll
	static void createEmployeeTypeRepository() {
		repository = new EmployeeTypeRepository(connector);
	}

	@Test
	void testInsertAndGetAll() {
		EmployeeType employeeType = new EmployeeType("a", "b", 5);
		repository.insert(employeeType);

		EmployeeType result = repository.getAll().get(0);

		assertEquals(employeeType, result);
	}

	@Test
	void testGetById() {
		EmployeeType employeeType = new EmployeeType("a", "b", 5);
		repository.insert(employeeType);

		EmployeeType result = repository.getById("a", "b");

		assertEquals(employeeType, result);
	}

	@Test
	void testCount() {
		long count = new Random().nextLong(300);
		for (int i = 0; i < count; i++) {
			EmployeeType employeeType = new EmployeeType("a#" + i, "b", 5);
			repository.insert(employeeType);
		}

		long result = repository.count();

		assertEquals(count, result);
	}

	@Test
	void testUpdate() {
		EmployeeType employeeType = new EmployeeType("a", "b", 5);
		repository.insert(employeeType);

		employeeType.setEmployeeRank("e");
		employeeType.setPosition("f");
		employeeType.setRetirementAfterYears(10);

		repository.update(employeeType);

		EmployeeType result = repository.getAll().get(0);

		assertEquals(employeeType, result);
	}

	@Test
	void testUpdate__doesnt_update_others() {
		EmployeeType employeeType1 = new EmployeeType("a", "b", 4);
		repository.insert(employeeType1);
		EmployeeType employeeType2 = new EmployeeType("x", "y", 6);
		repository.insert(employeeType2);

		employeeType1.setEmployeeRank("e");
		employeeType1.setPosition("f");
		employeeType1.setRetirementAfterYears(6);

		repository.update(employeeType1);

		List<EmployeeType> resultList = repository.getAll();
		resultList.sort(Comparator.comparing(EmployeeType::getEmployeeRank));
		EmployeeType result1 = resultList.get(0);
		EmployeeType result2 = resultList.get(1);

		assertEquals(employeeType1, result1);
		assertEquals(employeeType2, result2);
	}

	@Test
	void testDelete() {
		EmployeeType employeeType = new EmployeeType("a", "b", 5);
		repository.insert(employeeType);

		repository.delete(employeeType);

		assertEquals(0, repository.count());
	}
}
