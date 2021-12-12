package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
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
				INNER JOIN employee e ON e.full_name = m.holder_name AND e.department_title = m.department_title
				WHERE e.department_title = ? AND e.full_name = ?
				""",
			Machinery.class,
			e.getDepartmentTitle(), e.getFullName());
	}
}
