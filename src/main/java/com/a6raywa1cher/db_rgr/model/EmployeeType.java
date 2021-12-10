package com.a6raywa1cher.db_rgr.model;

import com.a6raywa1cher.db_rgr.dblib.Column;
import com.a6raywa1cher.db_rgr.dblib.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeType extends Entity {
	@Column(pk = true)
	private String employeeRank;

	@Column(pk = true)
	private String position;

	@Column
	private Integer retirementAfterYears;
}
