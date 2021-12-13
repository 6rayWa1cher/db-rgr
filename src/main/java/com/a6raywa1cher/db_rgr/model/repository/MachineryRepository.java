package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.Employee;
import com.a6raywa1cher.db_rgr.model.Machinery;
import lombok.SneakyThrows;

import java.util.List;

public class MachineryRepository extends CrudRepository<Machinery> {
	public MachineryRepository(EntityManager em) {
		super(Machinery.class, em);
	}

	@SneakyThrows
	public List<Machinery> getAllByHolder(Employee e) {
		return entityManager.executeSelect("""
				SELECT m.*
				FROM public.machinery m
				WHERE m.department_title = ? AND m.holder_name = ?
				""",
			Machinery.class,
			e.getDepartmentTitle(), e.getFullName());
	}

	@SneakyThrows
	public List<Machinery> getAllByDepartment(Department department) {
		return entityManager.executeSelect("""
				select *
				from machinery
				where department_title = ?
				""",
			Machinery.class,
			department.getTitle());
	}
}
