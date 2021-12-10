package com.a6raywa1cher.db_rgr.dblib.datatypes;

import com.a6raywa1cher.db_rgr.dblib.DataType;

import java.sql.Date;
import java.time.LocalDate;

public class LocalDateDataType implements DataType<Date, LocalDate> {
	@Override
	public Date serialize(LocalDate localDate) {
		return Date.valueOf(localDate);
	}

	@Override
	public LocalDate deserialize(Date date) {
		return date.toLocalDate();
	}

	@Override
	public Class<Date> getSerializedType() {
		return Date.class;
	}

	@Override
	public Class<LocalDate> getDeserializedType() {
		return LocalDate.class;
	}
}
