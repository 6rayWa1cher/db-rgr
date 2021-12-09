package com.a6raywa1cher.db_rgr.unit;

import com.a6raywa1cher.db_rgr.DatabaseInitializedTest;
import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartmentRepositoryUnitTest extends DatabaseInitializedTest {
	static DepartmentRepository repository;

	@BeforeAll
	static void createDepartmentRepository() {
		repository = new DepartmentRepository(connector);
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
}
