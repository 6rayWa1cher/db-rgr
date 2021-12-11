package com.a6raywa1cher.db_rgr.model;

import com.a6raywa1cher.db_rgr.dblib.entity.Column;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineryType extends Entity {
	@Column(pk = true)
	private String machineryTitle;

	@Column
	private String type;

	@Column
	private Integer lifeTimeYears;
}
