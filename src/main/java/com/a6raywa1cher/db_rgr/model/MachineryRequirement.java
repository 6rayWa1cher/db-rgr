package com.a6raywa1cher.db_rgr.model;

import com.a6raywa1cher.db_rgr.dblib.entity.Column;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineryRequirement extends Entity {
	@Column(pk = true)
	private String departmentTitle;

	@Column(pk = true)
	private String machineryTitle;

	@Column(pk = true)
	private Integer count;
}
