package com.a6raywa1cher.db_rgr.model;

import com.a6raywa1cher.db_rgr.dblib.Column;
import com.a6raywa1cher.db_rgr.dblib.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MachineryType {
	@Column(pk = true)
	private String machineryTitle;

	@Column
	private String type;

	@Column
	private Integer lifeTimeYears;
}
