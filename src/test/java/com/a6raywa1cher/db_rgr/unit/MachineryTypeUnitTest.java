package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.model.MachineryType;
import com.a6raywa1cher.db_rgr.model.repository.MachineryTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MachineryTypeUnitTest extends DatabaseInitializedTest {
	static MachineryTypeRepository repository;

	@BeforeAll
	static void createMachineryTypeRepository() {
		repository = new MachineryTypeRepository(connector);
	}

	@Test
	void testInsertAndGetAll() {
		MachineryType machineryType = new MachineryType("a", "b", 5);
		repository.insert(machineryType);

		MachineryType result = repository.getAll().get(0);

		assertEquals(machineryType, result);
	}

	@Test
	void testGetById() {
		MachineryType machineryType = new MachineryType("a", "b", 5);
		repository.insert(machineryType);

		MachineryType result = repository.getById("a");

		assertEquals(machineryType, result);
	}

	@Test
	void testCount() {
		long count = new Random().nextLong(300);
		for (int i = 0; i < count; i++) {
			MachineryType machineryType = new MachineryType("a#" + i, "b", 5);
			repository.insert(machineryType);
		}

		long result = repository.count();

		assertEquals(count, result);
	}

	@Test
	void testUpdate() {
		MachineryType machineryType = new MachineryType("a", "b", 5);
		repository.insert(machineryType);

		machineryType.setMachineryTitle("e");
		machineryType.setType("f");
		machineryType.setLifeTimeYears(10);

		repository.update(machineryType);

		MachineryType result = repository.getAll().get(0);

		assertEquals(machineryType, result);
	}

	@Test
	void testUpdate__doesnt_update_others() {
		MachineryType machineryType1 = new MachineryType("a", "b", 4);
		repository.insert(machineryType1);
		MachineryType machineryType2 = new MachineryType("x", "y", 6);
		repository.insert(machineryType2);

		machineryType1.setMachineryTitle("e");
		machineryType1.setType("f");
		machineryType1.setLifeTimeYears(6);

		repository.update(machineryType1);

		List<MachineryType> resultList = repository.getAll();
		resultList.sort(Comparator.comparing(MachineryType::getMachineryTitle));
		MachineryType result1 = resultList.get(0);
		MachineryType result2 = resultList.get(1);

		assertEquals(machineryType1, result1);
		assertEquals(machineryType2, result2);
	}

	@Test
	void testDelete() {
		MachineryType machineryType = new MachineryType("a", "b", 5);
		repository.insert(machineryType);

		repository.delete(machineryType);

		assertEquals(0, repository.count());
	}
}
