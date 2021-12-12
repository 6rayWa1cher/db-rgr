package com.a6raywa1cher.db_rgr.dblib;

import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassAnalyzer;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import lombok.SneakyThrows;
import org.intellij.lang.annotations.Language;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.a6raywa1cher.db_rgr.lib.ReflectionUtils.wrapSneaky;

public class EntityManager implements AutoCloseable {
	private final ClassAnalyzer classAnalyzer;

	private final EntityParser entityProcessor;

	private final DatabaseConnector databaseConnector;

	private final Map<Class<? extends Entity>, Map<UUID, Object[]>> prevPrimaryKeys = new HashMap<>();

	private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private final Map<UUID, EntityPhantomReference<?>> references = new HashMap<>();

	public EntityManager(ClassAnalyzer classAnalyzer, EntityParser entityProcessor, DatabaseConnector databaseConnector) {
		this.classAnalyzer = classAnalyzer;
		this.entityProcessor = entityProcessor;
		this.databaseConnector = databaseConnector;
	}

	private Map<UUID, Object[]> getPrevPrimaryKeysForClass(Class<? extends Entity> c) {
		return prevPrimaryKeys.computeIfAbsent(c, cls -> new HashMap<>());
	}

	private void checkReferenceQueue() {
		Reference<?> reference;
		while ((reference = referenceQueue.poll()) != null) {
			EntityPhantomReference<?> entityPhantomReference = (EntityPhantomReference<?>) reference;
			entityPhantomReference.cleanup();
		}
	}

	public <T extends Entity> void updateEntityInfo(T t) {
		getPrevPrimaryKeysForClass(t.getClass())
			.put(t.getUuid(), classAnalyzer.getClassData(t.getClass()).getPrimaryKey()
				.stream()
				.map(wrapSneaky(fd -> fd.getter().invoke(t)))
				.toArray()
			);
		references.put(t.getUuid(), new EntityPhantomReference<>(t));
		checkReferenceQueue();
	}

	public <T extends Entity> void deregisterObject(T t) {
		getPrevPrimaryKeysForClass(t.getClass()).remove(t.getUuid());
		references.remove(t.getUuid());
		checkReferenceQueue();
	}

	public <T extends Entity> Object[] getRecordedPrimaryKey(T t) {
		return getPrevPrimaryKeysForClass(t.getClass())
			.get(t.getUuid());
	}

	public ExecuteResult executeSelect(@Language("SQL") String sql, Object... params) throws SQLException {
		return databaseConnector.executeSelect(sql, params);
	}

	public <T> List<T> executeSelect(@Language("SQL") String sql, Class<T> targetClass, Object... params) throws SQLException {
		ExecuteResult executeResult = databaseConnector.executeSelect(sql, params);
		List<T> out = entityProcessor.parseResult(targetClass, executeResult);
		if (Entity.class.isAssignableFrom(targetClass)) out.forEach(t -> updateEntityInfo((Entity) t));
		return out;
	}

	public <T> T executeSelectSingle(@Language("SQL") String sql, Class<T> targetClass, Object... params) throws SQLException {
		List<T> result = executeSelect(sql, targetClass, params);
		if (result.size() > 1) {
			throw new RuntimeException("Database returned more than one result; expected exactly one");
		}
		return result.size() == 1 ? result.get(0) : null;
	}

	public void execute(@Language("SQL") String sql, Object... params) throws SQLException {
		databaseConnector.execute(sql, params);
	}

	public int executeUpdate(@Language("SQL") String sql, Object... params) throws SQLException {
		return databaseConnector.executeUpdate(sql, params);
	}

	public ClassAnalyzer getClassAnalyzer() {
		return classAnalyzer;
	}

	public EntityParser getEntityProcessor() {
		return entityProcessor;
	}

	public void openTransaction() {
		openTransaction(Connection.TRANSACTION_READ_COMMITTED);
	}

	@SneakyThrows
	public void openTransaction(int level) {
		databaseConnector.beginTransaction(level);
	}

	@SneakyThrows
	public void commit() {
		databaseConnector.commit();
	}

	@SneakyThrows
	public void rollback() {
		databaseConnector.rollback();
	}

	@Override
	public void close() throws Exception {
		databaseConnector.close();
	}

	private class EntityPhantomReference<T extends Entity> extends PhantomReference<T> {
		private final UUID entityUuid;
		private final Class<T> tClass;

		public EntityPhantomReference(T referent) {
			super(referent, referenceQueue);
			this.entityUuid = referent.getUuid();
			this.tClass = (Class<T>) referent.getClass();
		}

		public void cleanup() {
			getPrevPrimaryKeysForClass(tClass).remove(entityUuid);
			references.remove(entityUuid);
		}
	}
}
