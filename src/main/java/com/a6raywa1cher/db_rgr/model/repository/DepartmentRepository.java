package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.dblib.entity.Column;
import com.a6raywa1cher.db_rgr.dblib.entity.SubEntity;
import com.a6raywa1cher.db_rgr.model.Department;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.List;

public class DepartmentRepository extends CrudRepository<Department> {
	public DepartmentRepository(EntityManager em) {
		super(Department.class, em);
	}

	@SneakyThrows
	public List<EmployeeTypeStats> getEmployeeTypeStats(Department department) {
		return entityManager.executeSelect("""
			with A as (
				select e.employee_rank, e.position, count(*) as count
				from public.employee_type t
					left join public.employee e on e.position = t.position and e.employee_rank = t.employee_rank
				where e.department_title = ?
				group by e.employee_rank, e.position
			)
			select a.employee_rank, a.position, coalesce(r.count::bigint, 0) as expected_count, a.count as real_count
			from public.employee_requirement r
				right join A a on a.employee_rank = r.employee_rank and a.position = r.position
			""", EmployeeTypeStats.class, department.getTitle());
	}

	@SneakyThrows
	public List<MachineryTypeStats> getMachineryTypeStats(Department department) {
		return entityManager.executeSelect("""
			with A as (
				select m.machinery_title, count(*) as count
				from public.machinery_type t
					left join public.machinery m on t.machinery_title = m.machinery_title
				where m.department_title = ?
				group by m.machinery_title
			)
			select a.machinery_title, coalesce(r.count::bigint, 0) as expected_count, a.count as real_count
			from public.machinery_requirement r
				right join A a on a.machinery_title = r.machinery_title
			""", MachineryTypeStats.class, department.getTitle());
	}

	@Data
	@SubEntity
	public static class EmployeeTypeStats {
		@Column
		private String employeeRank;

		@Column
		private String position;

		@Column
		private Long expectedCount;

		@Column
		private Long realCount;
	}

	@Data
	@SubEntity
	public static class MachineryTypeStats {
		@Column
		private String machineryTitle;

		@Column
		private Long expectedCount;

		@Column
		private Long realCount;
	}
}
