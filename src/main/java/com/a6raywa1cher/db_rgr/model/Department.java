package com.a6raywa1cher.db_rgr.model;


import com.a6raywa1cher.db_rgr.dblib.entity.Column;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department extends Entity {
	@Column(value = "department_title", pk = true)
	private String title;

	@Column(value = "tel_number")
	private String telephoneNumber;

	@Column
	private String address;
}
