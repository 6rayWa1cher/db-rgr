package com.a6raywa1cher.db_rgr.model;


import com.a6raywa1cher.db_rgr.dblib.Column;
import com.a6raywa1cher.db_rgr.dblib.Entity;
import lombok.Data;

@Data
@Entity("department")
public class Department {
	@Column(value = "department_title", pk = true)
	private String title;

	@Column(value = "tel_number")
	private String telephoneNumber;

	@Column(value = "address")
	private String address;
}
