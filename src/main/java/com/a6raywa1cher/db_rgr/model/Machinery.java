package com.a6raywa1cher.db_rgr.model;

import com.a6raywa1cher.db_rgr.dblib.entity.Column;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machinery extends Entity {
	@Column(pk = true)
	private String departmentTitle;

	@Column(pk = true)
	private Integer id;

	@Column(pk = true)
	private String machineryTitle;

	@Column
	private String holderName;

	@Column
	private LocalDate dateOfPurchase;
}
