package com.a6raywa1cher.db_rgr.model;

import com.a6raywa1cher.db_rgr.dblib.Column;
import com.a6raywa1cher.db_rgr.dblib.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Entity {
	@Column(pk = true)
	private String fullName;

	@Column(pk = true)
	private String departmentTitle;

	@Column
	private String position;

	@Column
	private String employeeRank;

	@Column
	private LocalDate birthDate;

	@Column
	private LocalDate employmentDate;

	@Column
	private LocalDate lastPromotionDate;
}
