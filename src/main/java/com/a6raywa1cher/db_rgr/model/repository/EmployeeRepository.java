package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.Employee;
import lombok.SneakyThrows;

import java.util.List;

public class EmployeeRepository extends CrudRepository<Employee> {
	public EmployeeRepository(EntityManager em) {
		super(Employee.class, em);
	}

	@Override
	@SneakyThrows
	public void delete(Employee employee) {
		entityManager.openTransaction();
		try {
			entityManager.executeUpdate("""
				update machinery
				set holder_name = null
				where holder_name = ? and department_title = ?
				""", employee.getFullName(), employee.getDepartmentTitle()
			);
			super.delete(employee);
			entityManager.commit();
		} catch (Exception e) {
			entityManager.rollback();
			throw e;
		}
	}

	@SneakyThrows
	public List<Employee> getAllByDepartment(Department department) {
		return entityManager.executeSelect("""
				select *
				from employee
				where department_title = ?
				""",
			Employee.class,
			department.getTitle());
	}
}
