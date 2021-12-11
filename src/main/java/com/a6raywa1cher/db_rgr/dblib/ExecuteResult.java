package com.a6raywa1cher.db_rgr.dblib;

import java.sql.ResultSetMetaData;
import java.util.List;

public record ExecuteResult(
	ResultSetMetaData metaData,
	List<Object[]> result
) {

}
