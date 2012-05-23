/**
 * File: DBQuery.java
 * Date: 14.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.Pair;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.NaturalId;

/**
 * This class keeps and handles queries of a session. It can set up and execute
 * some HQL queries.
 * 
 * @author littlelion
 * 
 */
class DBQuery {

	/**
	 * Constant for the methods which provide the boolean parameter "notEmpty".
	 * If set to this constant the method fails if the result is an empty query
	 * string.
	 */
	private static final boolean QUERY_NOT_EMPTY = true;

	/**
	 * Constant for the methods which provide the boolean parameter "notEmpty".
	 * If set to this constant the method returns successful, even though the
	 * result is an empty query string.
	 */
	private static final boolean QUERY_EMPTY_VALID = false;

	/**
	 * Constant for the methods which provide the boolean parameter
	 * "allParameters". If set to this constant the method returns a query which
	 * contains a where clause with all parameters including the parameters
	 * which aren't declared in the list of where parameters.
	 */
	private static final boolean QUERY_ALL_PARAMETERS = true;

	/**
	 * Constant for the methods which provide the boolean parameter
	 * "allParameters". If set to this constant the method returns a query which
	 * contains a where clause only with the parameters declared in the list of
	 * where parameters.
	 */
	private static final boolean QUERY_WHERE_PARAMETERS_ONLY = false;

	/**
	 * Constant for the methods which provide the boolean parameter "forceNull".
	 * If set to this constant the method writes null values into the database.
	 */
	static final boolean QUERY_FORCE_NULL = true;

	/**
	 * Constant for the methods which provide the boolean parameter "forceNull".
	 * If set to this constant the method skips all null values in the query.
	 */
	static final boolean QUERY_SKIP_NULL = false;

	/**
	 * Constant for the methods which provide the boolean parameter "notNull".
	 * If set to this constant the method fails if a null result would return.
	 */
	static final boolean QUERY_NOT_NULL = true;

	/**
	 * Constant for the methods which provide the boolean parameter "notNull".
	 * If set to this constant the method may return a null result.
	 */
	static final boolean QUERY_NULLABLE = false;
	/**
	 * the session which owns this query
	 */
	private final Session session;

	/**
	 * initialized state: calling twice {@link #getParameters(Object, boolean)}
	 * will fail.
	 */
	private boolean initialized = false;

	/**
	 * Contains all parameters as {@code Pair} of {@code String} parameter and
	 * {@code Object} value.
	 */
	private List<Pair<String, Object>> parameters = new ArrayList<Pair<String, Object>>();

	/**
	 * Contains all parameters for the where clause as {@code Pair} of
	 * {@code Vector<String>} parameter and {@code Object} value.
	 * <p>
	 * The {@code Vector<String>} contains strings to define the part before the
	 * parameter, the parameter itself and the part after the parameter.
	 */
	private List<Pair<Vector<String>, Object>> whereParameters = new ArrayList<Pair<Vector<String>, Object>>();

	/**
	 * Contains all parameters for the select clause as {@code Pair} of
	 * {@code String} parameter and {@code Object} value. If and only of a
	 * parameter is defined in the select clause it must be added to this list.
	 */
	private List<Pair<String, Object>> selectParameters = new ArrayList<Pair<String, Object>>();

	/**
	 * Query object to build and execute some generic HQL queries.
	 * 
	 * @param session
	 *            the session this query belongs to
	 */
	DBQuery(Session session) {
		super();
		this.session = session;
	}

	/**
	 * @return the session
	 */
	Session getSession() {
		return this.session;
	}

	/**
	 * add a parameter to the HQL query
	 * 
	 * @param parameter
	 * @param value
	 */
	void addParameter(String parameter, Object value) {
		this.parameters.add(new Pair<String, Object>(parameter, value));
	}

	/**
	 * add a parameter to the HQL select clause
	 * 
	 * @param parameter
	 * @param value
	 */
	void addSelectParameter(String parameter, Object value) {
		this.parameters.add(new Pair<String, Object>(parameter, value));
	}

	/**
	 * add a parameter to the HQL where clause
	 * 
	 * @param before
	 *            part before the parameter
	 * @param parameter
	 *            the place-holder for the parameter
	 * @param after
	 *            part after the parameter
	 * @param value
	 *            the value to insert as parameter
	 */
	private void addWhereParameter(String before, String parameter,
			String after, Object value) {
		Vector<String> args = new Vector<String>(3);
		args.add(before);
		args.add(parameter);
		args.add(after);
		whereParameters.add(new Pair<Vector<String>, Object>(args, value));
	}

	/**
	 * concatenate the parameters to a HQL update query
	 * 
	 * @param targetEntity
	 * @return the update query string
	 */
	private String buildUpdateQueryString(String targetEntity) {
		StringBuilder builder = new StringBuilder();
		boolean comma = false;
		builder.append("update ").append(targetEntity).append(" set ");
		for (Pair<String, Object> p : parameters) {
			if (comma) {
				builder.append(", ");
			}
			builder.append(p.first).append(" = :").append(p.first);
			comma = true;
		}
		if (!comma)
			throw new IllegalArgumentException(
					"Malformed query: No parametes for upate defined.");
		builder.append(" ");
		builder.append(buildWhereClause(QUERY_NOT_EMPTY));
		return builder.toString();
	}

	/**
	 * concatenate the where parameters to a HQL where clause
	 * 
	 * @param notEmpty
	 *            If set to {@code true} an empty where clause is rejected. Use
	 *            the constants {@link #QUERY_NOT_EMPTY} or
	 *            {@link #QUERY_EMPTY_VALID}.
	 * @return the where clause or an empty string if no where parameter is set
	 *         and the notEmpty flag is set to {@code false}
	 */
	private String buildWhereClause(boolean notEmpty) {
		boolean valid = false;
		StringBuilder builder = new StringBuilder("where ");
		for (Pair<Vector<String>, Object> p : this.whereParameters) {
			if (valid) {
				builder.append("and ");
			}
			builder.append(p.first.get(0)).append(" :").append(p.first.get(1))
					.append(" ").append(p.first.get(2)).append(" ");
			valid = true;
		}
		if (!valid) {
			if (notEmpty) {
				throw new IllegalArgumentException(
						"Malformed query: no where clause defined.");
			} else {
				return "";
			}
		}
		return builder.toString();
	}

	/**
	 * concatenate the where parameters including the list of parameters to a
	 * HQL where clause
	 * 
	 * @param notEmpty
	 *            If set to {@code true} an empty where clause is rejected. Use
	 *            the constants {@link #QUERY_NOT_EMPTY} or
	 *            {@link #QUERY_EMPTY_VALID}.
	 * @return the where clause or an empty string if no where parameter is set
	 *         and the notEmpty flag is set to {@code false}
	 */
	private String buildWhereFromAllParameters(boolean notEmpty) {
		boolean valid = false;
		StringBuilder builder = new StringBuilder(
				buildWhereClause(QUERY_EMPTY_VALID));
		if (builder.length() > 0) {
			valid = true;
		}
		for (Pair<String, Object> p : this.parameters) {
			if (valid) {
				builder.append("and ");
			} else {
				builder.append("where ");
			}
			builder.append(p.first).append(" = :").append(p.first).append(" ");
			valid = true;
			if (!valid) {
				if (notEmpty) {
					throw new IllegalArgumentException(
							"Malformed query: no where clause defined.");
				} else {
					return "";
				}
			}
		}
		return builder.toString();
	}

	/**
	 * build a HQL update query
	 * 
	 * @param targetEntity
	 * @return the update query defined by parameters and where clauses
	 */
	private Query buildUpdateQuery(Object targetEntity) {
		Query result = this.session.createQuery(this
				.buildUpdateQueryString(targetEntity.getClass().getName()));
		for (Pair<String, Object> p : this.parameters) {
			result.setParameter(p.first, p.second);
		}
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		System.out.println(result);// XXX
		return result;
	}

	/**
	 * build a HQL from query
	 * 
	 * @param targetEntity
	 * @param allParameters
	 *            If set to {@code true} all parameters including them which
	 *            aren't declared as where parameter are considered. Use
	 *            constant {@link #QUERY_ALL_PARAMETERS} or
	 *            {@link #QUERY_WHERE_PARAMETERS_ONLY}.
	 * @return the update query defined by parameters and where clauses
	 */
	private Query buildFromQuery(Object targetEntity, boolean allParameters) {
		Query result = this.session.createQuery(buildFromQueryString(
				targetEntity, allParameters));
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		if (allParameters) {
			for (Pair<String, Object> p : this.parameters) {
				result.setParameter(p.first, p.second);
			}
		}
		return result;
	}

	/**
	 * Build a HQL from query string.
	 * 
	 * @param targetEntity
	 * @param allParameters
	 * @return the from query string
	 */
	private String buildFromQueryString(Object targetEntity,
			boolean allParameters) {
		StringBuilder builder = new StringBuilder();
		builder.append("from ").append(targetEntity.getClass().getName())
				.append(" ");
		if (allParameters) {
			builder.append(buildWhereFromAllParameters(QUERY_EMPTY_VALID));
		} else {
			builder.append(buildWhereClause(QUERY_EMPTY_VALID));
		}
		return builder.toString();
	}

	/**
	 * extract parameters from the entity
	 * 
	 * @param targetEntity
	 * @param forceNull
	 *            If {@code true} null values are written into the parameters
	 *            list. Use constants {@link #QUERY_FORCE_NULL} or
	 *            {@link #QUERY_SKIP_NULL}.
	 * @return the primary key (Id). If the Id is no instance of
	 *         {@code Serializable} or the Id coldn't be fetched {@code null} is
	 *         returned.
	 */
	private Serializable getParameters(Object targetEntity, boolean forceNull) {
		if (!targetEntity.getClass().isAnnotationPresent(Entity.class)) {
			throw new HibernateException("Class "
					+ targetEntity.getClass().getSimpleName()
					+ " is no @Entity.");
		}
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		Method[] methods = targetEntity.getClass().getDeclaredMethods();
		String parameter;
		Serializable key = null;
		Object value;
		for (Method m : methods) {
			if ((m.getName().startsWith("get") || m.getName().startsWith("is"))
					&& m.getGenericParameterTypes().length == 0) {
				int prefixLength = 3;
				if (m.getName().startsWith("is")) {
					prefixLength = 2;
				}
				parameter = m.getName()
						.substring(prefixLength, prefixLength + 1)
						.toLowerCase()
						+ m.getName().substring(prefixLength + 1);
				try {
					if ((value = m.invoke(targetEntity, (Object[]) null)) != null
							|| forceNull) {
						if (value != null
								&& (m.isAnnotationPresent(Id.class) || targetEntity
										.getClass().getDeclaredField(parameter)
										.isAnnotationPresent(Id.class))) {
							key = this.testAndSetKey(targetEntity, key, value);
							this.addWhereParameter(parameter + " =", parameter,
									"", value);
						} else if (value != null
								&& (m.isAnnotationPresent(NaturalId.class) || targetEntity
										.getClass().getDeclaredField(parameter)
										.isAnnotationPresent(NaturalId.class))) {
							this.addWhereParameter(parameter + " =", parameter,
									"", value);
						} else if (value != null
								&& (m.isAnnotationPresent(EmbeddedId.class) || targetEntity
										.getClass().getDeclaredField(parameter)
										.isAnnotationPresent(EmbeddedId.class))) {
							key = this.testAndSetKey(targetEntity, key, value);
							this.getWhereClauseFromEmbeddedId(key);

						} else {
							this.addParameter(parameter, value);
						}
					}
				} catch (ReflectiveOperationException e) {
					throw new HibernateException(e);
				}
			}
		}
		return key;
	}

	/**
	 * Search for a {@code Serializable} primary key of an entity fetched from
	 * the database. This method fails with a {@link NullPointerException} if
	 * the key results to null.
	 * 
	 * @param targetEntity
	 * @param notNull
	 *            If set to {@code true} the query fails on a null result. Use
	 *            constants {@link #QUERY_NOT_NULL} or {@link #QUERY_NULLABLE}.
	 * @return the primary key (Id).
	 */
	private Serializable getKey(Object targetEntity, boolean notNull) {
		if (!targetEntity.getClass().isAnnotationPresent(Entity.class)) {
			throw new HibernateException("Class "
					+ targetEntity.getClass().getSimpleName()
					+ " is no @Entity.");
		}
		Method[] methods = targetEntity.getClass().getDeclaredMethods();
		String parameter;
		Serializable key = null;
		Object value;
		for (Method m : methods) {
			if ((m.getName().startsWith("get") || m.getName().startsWith("is"))
					&& m.getGenericParameterTypes().length == 0) {
				int prefixLength = 3;
				if (m.getName().startsWith("is")) {
					prefixLength = 2;
				}
				parameter = m.getName()
						.substring(prefixLength, prefixLength + 1)
						.toLowerCase()
						+ m.getName().substring(prefixLength + 1);
				try {
					if (m.isAnnotationPresent(Id.class)
							|| targetEntity.getClass()
									.getDeclaredField(parameter)
									.isAnnotationPresent(Id.class)
							|| m.isAnnotationPresent(EmbeddedId.class)
							|| targetEntity.getClass()
									.getDeclaredField(parameter)
									.isAnnotationPresent(EmbeddedId.class)) {
						Query query = this.session.createQuery("select "
								+ parameter + " from "
								+ targetEntity.getClass().getName() + " "
								+ buildWhereClause(QUERY_NOT_EMPTY));
						for (Pair<Vector<String>, Object> w : this.whereParameters) {
							query.setParameter(w.first.get(1), w.second);
						}
						value = query.uniqueResult();

						if (value != null) {
							key = this.testAndSetKey(targetEntity, key, value);
						}
					}
				} catch (ReflectiveOperationException e) {
					throw new HibernateException(e);
				}
			}
		}
		if (key == null && notNull) {
			throw new NullPointerException("The primary key of entity "
					+ targetEntity.getClass().getSimpleName()
					+ " must not be null");
		}
		return key;
	}

	/**
	 * Extract the field values from the embedded id of an entity and add them
	 * to the where clause.
	 * 
	 * @param embeddedId
	 * @throws NullPointerException
	 *             if a field contains null.
	 * @see #getParameters(Object, boolean)
	 */
	private void getWhereClauseFromEmbeddedId(Serializable embeddedId) {
		if (!embeddedId.getClass().isAnnotationPresent(Embeddable.class)) {
			throw new HibernateException("Embedded Id of class "
					+ embeddedId.getClass().getSimpleName()
					+ " is no instance of an @Embeddable class.");
		}
		Method[] methods = embeddedId.getClass().getDeclaredMethods();
		String parameter;
		Object value;
		for (Method m : methods) {
			if ((m.getName().startsWith("get") || m.getName().startsWith("is"))
					&& m.getGenericParameterTypes().length == 0) {
				int prefixLength = 3;
				if (m.getName().startsWith("is")) {
					prefixLength = 2;
				}
				parameter = m.getName()
						.substring(prefixLength, prefixLength + 1)
						.toLowerCase()
						+ m.getName().substring(prefixLength + 1);
				try {
					if ((value = m.invoke(embeddedId, (Object[]) null)) != null) {
						this.addWhereParameter(parameter + " =", parameter, "",
								value);
					} else {
						throw new NullPointerException(
								"Forbidden null value in embedded id detected.");
					}
				} catch (ReflectiveOperationException e) {
					throw new HibernateException(e);
				}
			}
		}
	}

	/**
	 * Tests and sets the primary key of an entity. Multible calls fail with a
	 * {@link HibernateException}.
	 * 
	 * @param targetEntity
	 * @param key
	 *            the key to set. It must be initialized with null, otherwise
	 *            multiple calls are detected.
	 * @param value
	 * @return the updated key which is to write to the same variable which is
	 *         given by the {@code key} parameter. If the Id is no instance of
	 *         {@code Serializable} a {@link HibernateException} is thrown.
	 */
	private Serializable testAndSetKey(Object targetEntity, Serializable key,
			Object value) {
		if (key != null) {
			throw new HibernateException("Forbidden multible Id's in "
					+ targetEntity.getClass().getSimpleName() + " declared.");
		} else if (value instanceof Serializable) {
			key = (Serializable) value;
		} else {
			throw new HibernateException("Id of "
					+ targetEntity.getClass().getSimpleName()
					+ " must be serializable");
		}
		return key;
	}

	/**
	 * perform an update query
	 * 
	 * @param targetEntity
	 *            the entity to update
	 * @param forceNull
	 *            set {@code true} if null values are to write into the
	 *            database. Any attempt to write null into a column declared as
	 *            not null will be rejected. Use constants
	 *            {@link #QUERY_FORCE_NULL} or {@link #QUERY_SKIP_NULL}.
	 * @return the primary key (Id) of the entity
	 */
	Serializable update(Object targetEntity, boolean forceNull) {
		Serializable key = getParameters(targetEntity, forceNull);
		if (!this.parameters.isEmpty()) {
			key = getKey(targetEntity, QUERY_NOT_NULL);
			if (buildUpdateQuery(targetEntity).executeUpdate() < 1) {
				throw new HibernateException("failed to update entity with key"
						+ key);
			}
		}
		return key;
	}

	/**
	 * perform an insert query
	 * 
	 * @param targetEntity
	 *            the entity to update
	 * @return the primary key (Id) of the entity
	 */
	Serializable insert(Object targetEntity) {
		return this.session.save(targetEntity);
	}

	/**
	 * perform an insert query if the given entity is not persisted in the
	 * database, update the existing entity otherwise.
	 * 
	 * @param targetEntity
	 *            the entity to update
	 * @param forceNull
	 *            set {@code true} if null values are to write into the
	 *            database. Any attempt to write null into a column declared as
	 *            not null will be rejected. Use constants
	 *            {@link #QUERY_FORCE_NULL} or {@link #QUERY_SKIP_NULL}.
	 * @return the primary key (Id) of the entity
	 */
	Serializable insertOrUpdate(Object targetEntity, boolean forceNull) {
		Serializable key = getParameters(targetEntity, forceNull);
		if (!this.parameters.isEmpty()) {
			if ((key = getKey(targetEntity, QUERY_NULLABLE)) != null) {
				if (buildUpdateQuery(targetEntity).executeUpdate() < 1) {
					throw new HibernateException(
							"failed to update entity with key" + key);
				}

			} else {
				key = insert(targetEntity);
			}
		}
		return key;
	}

	/**
	 * write the entity to the DB.
	 * 
	 * @param targetEntity
	 *            the entity to write
	 * @param flags
	 *            The entity is inserted or updated depending on the flags. See
	 *            {@link IPersistence#DB_DEFAULT} for details.
	 * @return the Id of the persisted entity
	 */
	Serializable write(Object targetEntity, int flags) {
		if (SqlPersistenceHelper.hasFlag(flags, IPersistence.DB_NEW_ONLY)) {
			return this.insert(targetEntity);
		} else if (SqlPersistenceHelper.hasFlag(flags,
				IPersistence.DB_UPDATE_ONLY)) {
			return this.update(targetEntity, SqlPersistenceHelper.hasFlag(
					flags, IPersistence.DB_FORCE_NULL_VALUES));
		} else { // IPersistence.DB_DEFAULT case
			return this.insertOrUpdate(targetEntity, SqlPersistenceHelper
					.hasFlag(flags, IPersistence.DB_FORCE_NULL_VALUES));
		}
	}

	/**
	 * Remove an existing entity from the database or disable it.
	 * 
	 * @param targetEntity
	 *            the entity to remove or disable
	 * @param flags
	 *            If the {@link IPersistence#DB_NO_DELETE} flag is set this
	 *            method calls the {@code disable()} method of the entity. Set
	 *            the {@link IPersistence#DB_NO_DELETE} only if the
	 *            {@code disable()} method is present, otherwise a
	 *            {@code HibernateException} is thrown. The
	 *            {@link IPersistence#DB_DEFAULT} tries to use the
	 *            {@code disable()} method but falls back to the
	 *            {@link IPersistence#DB_FORCE_DELETE} behavior if not present.
	 *            If the {@link IPersistence#DB_FORCE_DELETE} flag is present
	 *            the entry of the database is deleted.
	 * @return the key of an updated entity or null if it has been deleted.
	 */
	Serializable remove(Object targetEntity, int flags) {
		Serializable key = getParameters(targetEntity,
				SqlPersistenceHelper.hasFlag(flags,
						IPersistence.DB_FORCE_NULL_VALUES));
		if (key == null) {
			throw new HibernateException("remove query needs a serializable Id");
		}
		// guaranteed: key not null

		if (!buildFromQuery(targetEntity, QUERY_WHERE_PARAMETERS_ONLY)
				.iterate().hasNext()) {
			throw new HibernateException("key not found in database: "
					+ key.toString());
		}
		// guaranteed: entity exists in database

		if (!SqlPersistenceHelper.hasFlag(flags, IPersistence.DB_FORCE_DELETE)
				|| SqlPersistenceHelper.hasFlag(flags,
						IPersistence.DB_NO_DELETE)) {
			// all cases except DB_FORCE_DELETE

			Method disable = null;
			Exception cause = null;
			try {
				disable = targetEntity.getClass().getMethod("disable",
						(Class[]) null);
			} catch (Exception e) {
				cause = e; // keep exception to pass on in DB_NO_DELETE case
				// no delete() method available
			}
			if (SqlPersistenceHelper.hasFlag(flags, IPersistence.DB_NO_DELETE)) {
				if (disable == null || cause != null) {
					throw new HibernateException(
							"DB_NO_DELETE flag detected but no disable strategy available for "
									+ targetEntity.getClass().getSimpleName(),
							cause);
				}
				if (buildUpdateQuery(targetEntity).executeUpdate() < 1) {
					throw new HibernateException(
							"failed to update entity with key" + key.toString());
				}
			} else { // IPersistence.DB_DEFAULT case
				if (disable == null || cause != null) {
					if (buildDeleteQuery(targetEntity).executeUpdate() < 1) {
						throw new HibernateException(
								"failed to delete entity with key: "
										+ key.toString());
					}
					key = null;
				}
				if (buildUpdateQuery(targetEntity).executeUpdate() < 1) {
					throw new HibernateException(
							"failed to update entity with key" + key.toString());
				}
			}

		} else { // DB_FORCE_DELETE case
			if (buildDeleteQuery(targetEntity).executeUpdate() < 1) {
				throw new HibernateException(
						"failed to delete entity with key: " + key.toString());
			}
			key = null;
		}
		return key;
	}

	/**
	 * build a HQL delete query
	 * 
	 * @param targetEntity
	 * @return the delete query defined by the where clauses
	 */
	private Query buildDeleteQuery(Object targetEntity) {
		Query result = this.session.createQuery(this
				.buildDeleteQueryString(targetEntity.getClass().getName()));
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		return result;
	}

	/**
	 * concatenate the where-parameters to a HQL delete query
	 * 
	 * @param targetEntity
	 * @return the delete query
	 */
	private String buildDeleteQueryString(String targetEntity) {
		StringBuilder builder = new StringBuilder();
		builder.append("delete ").append(targetEntity).append(" ");
		builder.append(buildWhereClause(QUERY_NOT_EMPTY));
		return builder.toString();
	}

	/**
	 * perform a delete query
	 * 
	 * @param targetEntity
	 *            the entity to delete
	 */
	void delete(Object targetEntity) {
		Serializable key = getParameters(targetEntity, QUERY_SKIP_NULL);
		if (key == null) {
			throw new HibernateException("delete query needs a serializable Id");
		}
		// guaranteed: key not null

		if (buildDeleteQuery(targetEntity).executeUpdate() < 1) {
			throw new HibernateException("failed to delete entity with key"
					+ key.toString());
		}
	}

	/**
	 * perform a from query
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @return a list of entities of type {@code <T>}
	 */
	<T> List<T> from(T targetEntity) {
		getParameters(targetEntity, QUERY_SKIP_NULL);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		@SuppressWarnings("unchecked")
		// query.list() returns a list of equal type to targetEntity
		List<T> result = query.list();
		return result;
	}

	/**
	 * perform a from query
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param start
	 *            first index to fetch
	 * @param count
	 *            max. number of results to fetch
	 * @return a list of entities of type {@code <T>}
	 */
	<T> List<T> from(T targetEntity, Integer start, Integer count) {
		getParameters(targetEntity, QUERY_SKIP_NULL);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		if (start != null) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		@SuppressWarnings("unchecked")
		// query.list() returns a list of equal type to targetEntity
		List<T> result = query.list();
		return result;
	}

	/**
	 * perform a from query
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param notNull
	 *            If set to {@code true} the query fails on a null result. Use
	 *            constants {@link #QUERY_NOT_NULL} or {@link #QUERY_NULLABLE}.
	 * @return a single entity of type {@code <T>}
	 */
	<T> T fromSingle(T targetEntity, boolean notNull) {
		getParameters(targetEntity, QUERY_SKIP_NULL);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		query.setFetchSize(1);
		@SuppressWarnings("unchecked")
		// query.list() returns a list of equal type to targetEntity
		List<T> values = query.list();
		T result = null;
		if (!values.isEmpty() || notNull) {
			result = values.get(0);
		}
		return result;
	}

	/**
	 * perform a from query
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @return an iterator of type {@code <T>}
	 */
	<T> Iterator<T> iterate(T targetEntity) {
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		@SuppressWarnings("unchecked")
		// query.iterate() returns an iterator of equal type to targetEntity
		Iterator<T> result = query.iterate();
		return result;
	}

	/**
	 * perform a from query
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param start
	 *            first index to fetch
	 * @param count
	 *            max. number of results to fetch
	 * @return an iterator of type {@code <T>}
	 */
	<T> Iterator<T> iterate(T targetEntity, Integer start, Integer count) {
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		if (start != null) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		@SuppressWarnings("unchecked")
		// query.iterate() returns an iterator of equal type to targetEntity
		Iterator<T> result = query.iterate();
		return result;
	}

	/**
	 * perform a select query
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param selectString
	 *            the select part of the HQL query (part before the "from"
	 *            keyword).
	 * @return a list of results according to the select string
	 */
	List<Object> select(Object targetEntity, String selectString) {
		Query query = buildSelectQuery(targetEntity, selectString,
				QUERY_ALL_PARAMETERS);
		@SuppressWarnings("unchecked")
		// query.list() returns a list of a subtype of Object
		List<Object> result = query.list();
		return result;
	}

	/**
	 * perform a select query
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param selectString
	 *            the select part of the HQL query (part before the "from"
	 *            keyword).
	 * @param start
	 *            first index to fetch
	 * @param count
	 *            max. number of results to fetch
	 * @return a list of results according to the select string
	 */
	List<Object> select(Object targetEntity, String selectString,
			Integer start, Integer count) {
		Query query = buildSelectQuery(targetEntity, selectString,
				QUERY_ALL_PARAMETERS);
		if (start != null) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		@SuppressWarnings("unchecked")
		// query.list() returns a list of a subtype of Object
		List<Object> result = query.list();
		return result;
	}

	/**
	 * Build a HQL select query
	 * 
	 * @param targetEntity
	 * @param selectString
	 * @param allParameters
	 * @return the select query
	 */
	private Query buildSelectQuery(Object targetEntity, String selectString,
			boolean allParameters) {
		StringBuilder builder = new StringBuilder(selectString);
		builder.append(" ").append(
				buildFromQueryString(targetEntity, allParameters));
		Query result = this.session.createQuery(builder.toString());
		for (Pair<String, Object> s : this.selectParameters) {
			result.setParameter(s.first, s.second);
		}
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		if (allParameters) {
			for (Pair<String, Object> p : this.parameters) {
				result.setParameter(p.first, p.second);
			}
		}
		return result;
	}

	Long count(Object targetEntity) {
		return (Long) select(targetEntity, "select count(*) ").get(0);
	}

	/**
	 * Perform a query with a custom HQL query string. The parameters must be
	 * set with {@link #addParameter(String, Object) before calling this method}
	 * .
	 * 
	 * @param queryString
	 *            must be a valid HQL query. The parameters in the
	 *            parameters-list must be equal to the parameters in the query
	 *            string.
	 * 
	 * @param start
	 *            first index to fetch
	 * @param count
	 *            max. number of results to fetch
	 * @return a list of entities
	 */
	List<Object> customQuery(String queryString, Integer start, Integer count) {
		Query query = this.session.createQuery(queryString);
		if (start != null) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		for (Pair<String, Object> p : this.parameters) {
			query.setParameter(p.first, p.second);
		}
		@SuppressWarnings("unchecked")
		// query.list() returns a list according to the given query string
		List<Object> result = query.list();
		return result;
	}

	/**
	 * Perform a query with a custom HQL query string. The parameters must be
	 * set with {@link #addParameter(String, Object) before calling this method}
	 * .
	 * 
	 * @param queryString
	 *            must be a valid HQL query. The parameters in the
	 *            parameters-list must be equal to the parameters in the query
	 *            string.
	 * 
	 * @param start
	 *            first index to fetch
	 * @param count
	 *            max. number of results to fetch
	 * @return a list of entities of type {@code <T>}
	 */
	Iterator<Object> customIterator(String queryString, Integer start,
			Integer count) {
		Query query = this.session.createQuery(queryString);
		if (start != null) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		for (Pair<String, Object> p : this.parameters) {
			query.setParameter(p.first, p.second);
		}
		@SuppressWarnings("unchecked")
		// query.iterator() returns an iterator according to the given query
		// string
		Iterator<Object> result = query.iterate();
		return result;
	}

	/**
	 * reset this query. All parameters are cleared and the initialized state is
	 * set to {@code false}.
	 */
	void reset() {
		this.parameters = new ArrayList<Pair<String, Object>>();
		this.whereParameters = new ArrayList<Pair<Vector<String>, Object>>();
		this.selectParameters = new ArrayList<Pair<String, Object>>();
		this.initialized = false;
	}
}
