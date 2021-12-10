package com.a6raywa1cher.db_rgr.dblib;

public interface DataType<S, D> {
	S serialize(D d);

	D deserialize(S s);

	Class<S> getSerializedType();

	Class<D> getDeserializedType();
}
