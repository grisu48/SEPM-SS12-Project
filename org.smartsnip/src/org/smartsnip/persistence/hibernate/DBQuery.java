/**
 * File: DBQuery.java
 * Date: 14.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.Pair;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
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
	static final boolean QUERY_FORCE_NULL_VALUES = true;

	/**
	 * Constant for the methods which provide the boolean parameter "forceNull".
	 * If set to this constant the method skips all null values in the query.
	 */
	static final boolean QUERY_SKIP_NULL_VALUES = false;

	/**
	 * Constant for the methods which provide the {@code int} parameter
	 * {@code flags}. If set to this constant the method may return a null
	 * result. This constant is the default. It is overridden by the constant
	 * {@link #QUERY_NOT_NULL}.
	 * <p>
	 * The values of the flags may change in future revisions, so to insert a
	 * hardcoded integer value instead of using this constant is discouraged.
	 * 
	 * @see #QUERY_NOT_NULL
	 * @see #QUERY_UNIQUE_RESULT
	 */
	static final int QUERY_NULLABLE = 0;

	/**
	 * Constant for the methods which provide the {@code int} parameter
	 * {@code flags}. If set to this constant the method fails if a null result
	 * would return. This constant overrides {@link #QUERY_NULLABLE}.
	 */
	static final int QUERY_NOT_NULL = 512;

	/**
	 * Constant for the methods which provide the {@code int} parameter
	 * {@code flags}. If set to this constant the method fails if a null result
	 * would return.
	 */
	static final int QUERY_UNIQUE_RESULT = 1024;

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
	 * logger to produce some debug messages
	 */
	private Logger log = Logger.getLogger(DBQuery.class);

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
	 * Add a parameter to the HQL query.
	 * 
	 * @param parameter
	 * @param value
	 */
	void addParameter(String parameter, Object value) {
		this.parameters.add(new Pair<String, Object>(parameter, value));
	}

	/**
	 * Add a parameter to the HQL select clause.
	 * 
	 * @param parameter
	 * @param value
	 */
	void addSelectParameter(String parameter, Object value) {
		this.parameters.add(new Pair<String, Object>(parameter, value));
	}

	/**
	 * Add a parameter to the HQL where clause.
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
	 * Concatenate the parameters to a HQL update query.
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
		log.trace(builder.toString());
		return builder.toString();
	}

	/**
	 * Concatenate the where parameters to a HQL where clause.
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
	 * Concatenate the where parameters including the list of parameters to a
	 * HQL where clause.
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
	 * Build a HQL update query.
	 * 
	 * @param targetEntity
	 * @return the update query defined by parameters and where clauses
	 */
	private Query buildUpdateQuery(Object targetEntity) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
		Query result = this.session.createQuery(this
				.buildUpdateQueryString(targetEntity.getClass().getName()));
		for (Pair<String, Object> p : this.parameters) {
			result.setParameter(p.first, p.second);
		}
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		log.debug(result);
		return result;
	}

	/**
	 * Build a HQL from query.
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
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
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
		log.debug(result);
		return result;
	}

	/**
	 * Build a HQL from query string.
	 * 
	 * @param targetEntity
	 * @param allParameters
	 *            If set to {@code true} all parameters including them which
	 *            aren't declared as where parameter are considered. Use
	 *            constant {@link #QUERY_ALL_PARAMETERS} or
	 *            {@link #QUERY_WHERE_PARAMETERS_ONLY}.
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
		log.trace(builder.toString());
		return builder.toString();
	}

	/**
	 * Extract parameters from the entity and add them to the parameter lists.
	 * 
	 * @param targetEntity
	 * @param forceNull
	 *            If {@code true} null values are written into the parameters
	 *            list. Use constants {@link #QUERY_FORCE_NULL_VALUES} or
	 *            {@link #QUERY_SKIP_NULL_VALUES}.
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
		boolean hasNaturalId = false;
		Object value;
		for (Method m : methods) {
			try {
				if ((parameter = getColumnName(targetEntity, m)) != null
						&& ((value = m.invoke(targetEntity, (Object[]) null)) != null || forceNull)) {
					// Id, NatuarlId or EmbeddedId cannot be forced to null
					if (hasAnnotationAndField(Id.class, targetEntity,
							parameter, m)) {
						if (value != null) { // Id not null
							key = this.testAndSetKey(targetEntity, key, value);
							this.addWhereParameter(parameter + " =", parameter,
									"", value);
						}
					} else if (hasAnnotationAndField(NaturalId.class,
							targetEntity, parameter, m)) {
						if (value != null) { // NaturalId not null
							this.addWhereParameter(parameter + " =", parameter,
									"", value);
							hasNaturalId = true;
						}
					} else if (hasAnnotationAndField(EmbeddedId.class,
							targetEntity, parameter, m)) {
						if (value != null) { // EmbeddedId not null
							key = this.testAndSetKey(targetEntity, key, value);
							this.getWhereClauseFromEmbeddedId(parameter, key,
									QUERY_NULLABLE);
						}
					} else { // value is no Id, NatuarlId or EmbeddedId
						this.addParameter(parameter, value);
					}
				}
			} catch (ReflectiveOperationException e) {
				throw new HibernateException(e);

			}
		}
		this.initialized = true;
		if (key == null && hasNaturalId) {
			key = getKey(targetEntity, QUERY_UNIQUE_RESULT);
		}
		log.trace("initialized = true");
		log.trace(this);
		return key;
	}

	/**
	 * Tests the method and the field on the presence of the given annotation.
	 * 
	 * @param annotation
	 *            the annotation to test for
	 * @param object
	 *            the object to test
	 * @param field
	 *            the name of the field
	 * @param method
	 * @return {@code true} if field exists and the method or field has the
	 *         requested annotation.
	 */
	private boolean hasAnnotationAndField(
			Class<? extends Annotation> annotation, Object object,
			String field, Method method) {
		boolean result = false;
		try {
			result = method.isAnnotationPresent(annotation)
					|| object.getClass().getDeclaredField(field)
							.isAnnotationPresent(annotation);
		} catch (NoSuchFieldException e) {
			log.trace("method " + method.getName() + " of entity "
					+ object.getClass().getSimpleName() + " failed", e);
		}
		return result;
	}

	// /** //XXX delete if not needed
	// * Tests the method and the field on the presence of a
	// * {@link javax.persistence.Column} annotation and fetches the {@code
	// name}
	// * parameter.
	// *
	// * @param object
	// * the object to test
	// * @param field
	// * the name of the field
	// * @param method
	// * @return the name of the column if the method or field represents a
	// column
	// */
	// private String annotatedNameOfColumn(Object object, String field,
	// Method method) {
	// String result = field;
	// String name;
	// Column annotation;
	// try {
	// if ((annotation = method.getAnnotation(Column.class)) != null) {
	// name = annotation.name();
	// if (!name.isEmpty()) {
	// result = name;
	// }
	// } else if ((annotation = object.getClass().getDeclaredField(field)
	// .getAnnotation(Column.class)) != null) {
	// name = annotation.name();
	// if (!name.isEmpty()) {
	// result = name;
	// }
	// }
	// } catch (NoSuchFieldException e) {
	// result = null;
	// }
	// return result;
	// }

	/**
	 * Search for a {@code Serializable} primary key of an entity fetched from
	 * the database.
	 * 
	 * @param targetEntity
	 * @param flags
	 *            If set to {@code true} the query fails on a null result. This
	 *            method fails with a {@link NullPointerException} if the key
	 *            results to null and the flag {@link #QUERY_NOT_NULL} is set.
	 *            The constant {@link #QUERY_UNIQUE_RESULT} is the default for
	 *            this method.
	 * @return the primary key (Id).
	 */
	private Serializable getKey(Object targetEntity, int flags) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
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
			if ((parameter = getColumnName(targetEntity, m)) != null
					&& (hasAnnotationAndField(Id.class, targetEntity,
							parameter, m) || hasAnnotationAndField(
							EmbeddedId.class, targetEntity, parameter, m))) {
				Query query = this.session.createQuery("select " + parameter
						+ " from " + targetEntity.getClass().getName() + " "
						+ buildWhereClause(QUERY_NOT_EMPTY));
				for (Pair<Vector<String>, Object> w : this.whereParameters) {
					query.setParameter(w.first.get(1), w.second);
				}
				log.trace(query);
				value = query.uniqueResult();

				if (value != null) {
					key = this.testAndSetKey(targetEntity, key, value);
				}
			}

		}
		if (key == null && hasFlag(flags, QUERY_NOT_NULL)) {
			throw new NullPointerException("The primary key of entity "
					+ targetEntity.getClass().getSimpleName()
					+ " must not be null");
		}
		log.debug(key);
		log.trace(this);
		return key;
	}

	/**
	 * Tests if the method is a getter of a field representing a column of a
	 * DB-table and builds a string representing the name of the according
	 * column.
	 * 
	 * @param entity
	 *            the target entity.
	 * @param method
	 *            the method to test
	 * @return the name of the field or {@code null} if the field is no getter.
	 */
	private String getColumnName(Object entity, Method method) {
		String parameter = null;
		if ((method.getName().startsWith("get") || method.getName().startsWith(
				"is"))
				&& method.getGenericParameterTypes().length == 0) {
			int prefixLength = 3;
			if (method.getName().startsWith("is")) {
				prefixLength = 2;
			}
			parameter = method.getName()
					.substring(prefixLength, prefixLength + 1).toLowerCase()
					+ method.getName().substring(prefixLength + 1);

			if (!hasAnnotationAndField(Column.class, entity, parameter, method)
					&& !hasAnnotationAndField(EmbeddedId.class, entity,
							parameter, method)) {
				parameter = null;
			}
		}
		return parameter;
	}

	// /** // XXX delete if not needed
	// * Tests if the method is a getter of a field representing a column of a
	// * DB-table and builds a string representing the name of the according
	// * column.
	// *
	// * @param entity
	// * the target entity.
	// * @param method
	// * the method to test
	// * @return a pair of Strings: first element is the name of the field and
	// the
	// * second element is the name of the column in the database. This
	// * method returns {@code null} if the method is no getter of a
	// * column.
	// */
	// private String getEmbeddedColumnName(Object entity, Method method) {
	// String column = null;
	// if ((method.getName().startsWith("get") || method.getName().startsWith(
	// "is"))
	// && method.getGenericParameterTypes().length == 0) {
	// int prefixLength = 3;
	// if (method.getName().startsWith("is")) {
	// prefixLength = 2;
	// }
	// column = method.getName().substring(prefixLength, prefixLength + 1)
	// .toLowerCase()
	// + method.getName().substring(prefixLength + 1);
	//
	// column = annotatedNameOfColumn(entity, column, method);
	// }
	// return column;
	// }

	/**
	 * Extract the field values from the embedded id of an entity and add them
	 * to the where clause.
	 * 
	 * @param idString
	 *            the name of the embedded id
	 * @param embeddedId
	 *            the embedded id as {@link Serializable} object
	 * @param flags
	 *            If set to {@code true} the query fails on a null result. Use
	 *            constants {@link #QUERY_NOT_NULL} or {@link #QUERY_NULLABLE}.
	 *            The constant {@link #QUERY_UNIQUE_RESULT} is ignored.
	 * @throws NullPointerException
	 *             if a field contains null.
	 * @see #getParameters(Object, boolean)
	 */
	private void getWhereClauseFromEmbeddedId(String idString,
			Serializable embeddedId, int flags) {
		if (!embeddedId.getClass().isAnnotationPresent(Embeddable.class)) {
			throw new HibernateException("Embedded Id of class "
					+ embeddedId.getClass().getSimpleName()
					+ " is no instance of an @Embeddable class.");
		}
		Method[] methods = embeddedId.getClass().getDeclaredMethods();
		String parameter;
		Object value;
		for (Method m : methods) {
			if ((parameter = getColumnName(embeddedId, m)) != null) {
				try {
					if ((value = m.invoke(embeddedId, (Object[]) null)) != null) {
						this.addWhereParameter(idString + "." + parameter
								+ " =", parameter, "", value);
					} else if (hasFlag(flags, QUERY_NOT_NULL)) {
						throw new NullPointerException(
								"Forbidden null value in embedded id "
										+ idString + " detected.");
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
		log.trace("Key: " + key + "(" + value + ")");
		return key;
	}

	/**
	 * Perform an update query.
	 * 
	 * @param targetEntity
	 *            the entity to update
	 * @param forceNull
	 *            set {@code true} if null values are to write into the
	 *            database. Any attempt to write null into a column declared as
	 *            not null will be rejected. Use constants
	 *            {@link #QUERY_FORCE_NULL_VALUES} or
	 *            {@link #QUERY_SKIP_NULL_VALUES}.
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
	 * Perform an insert query.
	 * 
	 * @param targetEntity
	 *            the entity to update
	 * @return the primary key (Id) of the entity
	 */
	Serializable insert(Object targetEntity) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialized = true;
		return this.session.save(targetEntity);
	}

	/**
	 * Perform an insert query if the given entity is not persisted in the
	 * database, update the existing entity otherwise.
	 * 
	 * @param targetEntity
	 *            the entity to update
	 * @param forceNull
	 *            set {@code true} if null values are to write into the
	 *            database. Any attempt to write null into a column declared as
	 *            not null will be rejected. Use constants
	 *            {@link #QUERY_FORCE_NULL_VALUES} or
	 *            {@link #QUERY_SKIP_NULL_VALUES}.
	 * @return the primary key (Id) of the entity
	 */
	Serializable insertOrUpdate(Object targetEntity, boolean forceNull) {
		Serializable key = getParameters(targetEntity, forceNull);
		if ((key = getKey(targetEntity, QUERY_NULLABLE)) != null) {
			if (!this.parameters.isEmpty()) {
				if (buildUpdateQuery(targetEntity).executeUpdate() < 1) {
					throw new HibernateException(
							"failed to update entity with key" + key);
				}
			}
		} else {
			key = this.session.save(targetEntity);

		}
		return key;
	}

	/**
	 * Write the entity to the DB.
	 * 
	 * @param targetEntity
	 *            the entity to write
	 * @param flags
	 *            The entity is inserted or updated depending on the flags. See
	 *            {@link IPersistence#DB_DEFAULT} for details.
	 * @return the Id of the persisted entity
	 */
	Serializable write(Object targetEntity, int flags) {
		if (hasFlag(flags, IPersistence.DB_NEW_ONLY)) {
			return this.insert(targetEntity);
		} else if (hasFlag(flags, IPersistence.DB_UPDATE_ONLY)) {
			return this.update(targetEntity,
					hasFlag(flags, IPersistence.DB_FORCE_NULL_VALUES));
		} else { // IPersistence.DB_DEFAULT case
			return this.insertOrUpdate(targetEntity,
					hasFlag(flags, IPersistence.DB_FORCE_NULL_VALUES));
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
				hasFlag(flags, IPersistence.DB_FORCE_NULL_VALUES));
		if (key == null) {
			throw new HibernateException("remove query needs a serializable Id");
		}
		log.assertLog(key != null, "The primary key of " + targetEntity
				+ " must not be null.");

		if (!buildFromQuery(targetEntity, QUERY_WHERE_PARAMETERS_ONLY)
				.iterate().hasNext()) {
			throw new HibernateException("key not found in database: "
					+ key.toString());
		}
		// guaranteed: entity exists in database

		if (!hasFlag(flags, IPersistence.DB_FORCE_DELETE)
				|| hasFlag(flags, IPersistence.DB_NO_DELETE)) {
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
			if (hasFlag(flags, IPersistence.DB_NO_DELETE)) {
				if (disable == null || cause != null) {
					throw new HibernateException(
							"DB_NO_DELETE flag detected but no disable strategy available for "
									+ targetEntity.getClass().getSimpleName(),
							cause);
				} else if (buildUpdateQuery(targetEntity).executeUpdate() < 1) {
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
				} else if (buildUpdateQuery(targetEntity).executeUpdate() < 1) {
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
	 * Build a HQL delete query.
	 * 
	 * @param targetEntity
	 * @return the delete query defined by the where clauses
	 */
	private Query buildDeleteQuery(Object targetEntity) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
		Query result = this.session.createQuery(this
				.buildDeleteQueryString(targetEntity.getClass().getName()));
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		log.debug(result);
		return result;
	}

	/**
	 * Concatenate the where-parameters to a HQL delete query.
	 * 
	 * @param targetEntity
	 * @return the delete query
	 */
	private String buildDeleteQueryString(String targetEntity) {
		StringBuilder builder = new StringBuilder();
		builder.append("delete ").append(targetEntity).append(" ");
		builder.append(buildWhereClause(QUERY_NOT_EMPTY));
		log.trace(builder.toString());
		return builder.toString();
	}

	/**
	 * Perform a delete query.
	 * 
	 * @param targetEntity
	 *            the entity to delete
	 */
	void delete(Object targetEntity) {
		Serializable key = getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		if (key == null) {
			throw new HibernateException("delete query needs a serializable Id");
		}
		log.assertLog(key != null, "The primary key of " + targetEntity
				+ " must not be null.");

		if (buildDeleteQuery(targetEntity).executeUpdate() < 1) {
			throw new HibernateException("failed to delete entity with key"
					+ key.toString());
		}
	}

	/**
	 * Perform a from query.
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @return a list of entities of type {@code <T>}
	 */
	<T> List<T> from(T targetEntity) {
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		@SuppressWarnings("unchecked")
		// query.list() returns a list of equal type to targetEntity
		List<T> result = query.list();
		return result;
	}

	/**
	 * Perform a from query.
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
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		if (start != null && start > 0) {
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
	 * Perform a from query.
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param flags
	 *            If set to {@code true} the query fails on a null result. Use
	 *            the constants {@link #QUERY_NOT_NULL}, {@link #QUERY_NULLABLE}
	 *            and {@link #QUERY_UNIQUE_RESULT}. Use a bitwise or to set
	 *            multiple flags.
	 * @return a single entity of type {@code <T>}
	 * @throws NullPointerException
	 *             if the query results to null and the {@link #QUERY_NOT_NULL}
	 *             flag is set.
	 * @throws NonUniqueResultException
	 *             if the underlying SQL-query returns a non unique result and
	 *             the {@link #QUERY_UNIQUE_RESULT} flag is set.
	 */
	@SuppressWarnings("unchecked")
	// unchecked: singleQuery(...) returns an object of equal type to
	// targetEntity
	<T> T fromSingle(T targetEntity, int flags) {
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		return (T) singleQuery(query, flags);
	}

	/**
	 * Perform a query which returns a single object.
	 * 
	 * @param query
	 *            a ready to perform query
	 * @param flags
	 *            If set to {@code true} the query fails on a null result. Use
	 *            the constants {@link #QUERY_NOT_NULL}, {@link #QUERY_NULLABLE}
	 *            and {@link #QUERY_UNIQUE_RESULT}. Use a bitwise or to set
	 *            multiple flags.
	 * @return a single object according to the query result
	 * @throws NullPointerException
	 *             if the query results to null and the {@link #QUERY_NOT_NULL}
	 *             flag is set.
	 * @throws NonUniqueResultException
	 *             if the underlying SQL-query returns a non unique result and
	 *             the {@link #QUERY_UNIQUE_RESULT} flag is set.
	 */
	private Object singleQuery(Query query, int flags) {
		Object result = null;
		if (hasFlag(flags, QUERY_UNIQUE_RESULT)) {
			result = query.uniqueResult();
		} else {
			query.setFetchSize(1);
			List<?> values = query.list();
			if (!values.isEmpty()) {
				result = values.get(0);
			}
		}
		if (result == null && hasFlag(flags, QUERY_NOT_NULL)) {
			throw new NullPointerException("Query result is null.");
		}
		return result;
	}

	/**
	 * Perform a from query.
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @return an iterator of type {@code <T>}
	 */
	<T> Iterator<T> iterate(T targetEntity) {
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		@SuppressWarnings("unchecked")
		// query.iterate() returns an iterator of equal type to targetEntity
		Iterator<T> result = query.iterate();
		return result;
	}

	/**
	 * Perform a from query.
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
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildFromQuery(targetEntity, QUERY_ALL_PARAMETERS);
		if (start != null && start > 0) {
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
	 * Perform a select query.
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param selectString
	 *            the select part of the HQL query (part before the {@code from}
	 *            keyword without {@code select}). All parameters used in the
	 *            string must be added with the method
	 *            {@link #addSelectParameter(String, Object)} with equal
	 *            parameter strings declared.
	 * @return a list of results according to the select string
	 */
	List<?> select(Object targetEntity, String selectString) {
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildSelectQuery(targetEntity, selectString,
				QUERY_ALL_PARAMETERS);
		return query.list();
	}

	/**
	 * Perform a select query.
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param selectString
	 *            the select part of the HQL query (part before the {@code from}
	 *            keyword without {@code select}). All parameters used in the
	 *            string must be added with the method
	 *            {@link #addSelectParameter(String, Object)} with equal
	 *            parameter strings declared.
	 * @param start
	 *            first index to fetch
	 * @param count
	 *            max. number of results to fetch
	 * @return a list of results according to the select string
	 */
	List<?> select(Object targetEntity, String selectString, Integer start,
			Integer count) {
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildSelectQuery(targetEntity, selectString,
				QUERY_ALL_PARAMETERS);
		if (start != null && start > 0) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		return query.list();
	}

	/**
	 * Perform a select query.
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param selectString
	 *            the select part of the HQL query (part before the {@code from}
	 *            keyword without {@code select}). All parameters used in the
	 *            string must be added with the method
	 *            {@link #addSelectParameter(String, Object)} with equal
	 *            parameter strings declared.
	 * @param flags
	 *            If set to {@code true} the query fails on a null result. Use
	 *            the constants {@link #QUERY_NOT_NULL}, {@link #QUERY_NULLABLE}
	 *            and {@link #QUERY_UNIQUE_RESULT}. Use a bitwise or to set
	 *            multiple flags.
	 * @return an object as result according to the select string
	 * @throws NullPointerException
	 *             if the query results to null and the {@link #QUERY_NOT_NULL}
	 *             flag is set.
	 * @throws NonUniqueResultException
	 *             if the underlying SQL-query returns a non unique result and
	 *             the {@link #QUERY_UNIQUE_RESULT} flag is set. if the
	 *             underlying SQL-query returns a non unique result.
	 */
	Object selectSingle(Object targetEntity, String selectString, int flags) {
		getParameters(targetEntity, QUERY_SKIP_NULL_VALUES);
		Query query = buildSelectQuery(targetEntity, selectString,
				QUERY_ALL_PARAMETERS);
		return singleQuery(query, flags);
	}

	/**
	 * Build a HQL select query.
	 * 
	 * @param targetEntity
	 *            The entity to fetch as prototype. Set all fields to null which
	 *            aren't to use as query parameter.
	 * @param selectString
	 *            the select part of the HQL query (part before the {@code from}
	 *            keyword without {@code select}). All parameters used in the
	 *            string must be added with the method
	 *            {@link #addSelectParameter(String, Object)} with equal
	 *            parameter strings declared.
	 * @param allParameters
	 *            If set to {@code true} all parameters including them which
	 *            aren't declared as where parameter are considered. Use
	 *            constant {@link #QUERY_ALL_PARAMETERS} or
	 *            {@link #QUERY_WHERE_PARAMETERS_ONLY}.
	 * @return the select query
	 */
	private Query buildSelectQuery(Object targetEntity, String selectString,
			boolean allParameters) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
		StringBuilder builder = new StringBuilder("select ");
		builder.append(selectString).append(" ")
				.append(buildFromQueryString(targetEntity, allParameters));
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
		log.debug(result);
		return result;
	}

	Long count(Object targetEntity) {
		return (Long) select(targetEntity, "count(*)").get(0);
	}

	/**
	 * Perform a query with a custom HQL query string. The parameters must be
	 * set with {@link #addParameter(String, Object)} before calling this method
	 * . Call {@link #initialize()} after adding the parameters.
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
	List<?> customQueryRead(String queryString, Integer start, Integer count) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialize();
		Query query = buildCustomQuery(queryString);
		if (start != null && start > 0) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		return query.list();
	}

	/**
	 * Perform a query with a custom HQL query string. The parameters must be
	 * set with {@link #addParameter(String, Object)} before calling this method
	 * . Call {@link #initialize()} after adding the parameters.
	 * 
	 * @param queryString
	 *            must be a valid HQL query. The parameters in the
	 *            parameters-list must be equal to the parameters in the query
	 *            string.
	 * @return the number of modified rows.
	 */
	int customQueryWrite(String queryString) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialize();
		Query query = buildCustomQuery(queryString);
		return query.executeUpdate();
	}

	/**
	 * Build a custom query.
	 * <p>
	 * Add all necessary parameters with the method
	 * {@link #addParameter(String, Object)} and than call {@link #initialize()}
	 * .
	 * 
	 * @param queryString
	 *            must be a valid HQL query. The parameters in the
	 *            parameters-list must be equal to the parameters in the query
	 *            string.
	 * @return the query ready to perform
	 */
	Query buildCustomQuery(String queryString) {
		if (!this.initialized) {
			throw new HibernateException(
					"The query of customQuery() is not initialized.");
		}
		Query query = this.session.createQuery(queryString);
		for (Pair<String, Object> p : this.parameters) {
			query.setParameter(p.first, p.second);
		}
		return query;
	}

	/**
	 * Perform a query with a custom HQL query string. The parameters must be
	 * set with {@link #addParameter(String, Object)} before calling this method
	 * . Call {@link #initialize()} after adding the parameters.
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
	Iterator<?> customIterator(String queryString, Integer start, Integer count) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialize();
		Query query = buildCustomQuery(queryString);
		if (start != null && start > 0) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		return query.iterate();
	}

	/**
	 * Reset this query. All parameters are cleared and the initialized state is
	 * set to {@code false}.
	 */
	void reset() {
		this.parameters = new ArrayList<Pair<String, Object>>();
		this.whereParameters = new ArrayList<Pair<Vector<String>, Object>>();
		this.selectParameters = new ArrayList<Pair<String, Object>>();
		this.initialized = false;
		log.trace("initialized = false");
	}

	/**
	 * Initialize this query. This method is invoked by some methods of the
	 * {@code DBQuery} class e. g.
	 * {@link #customQueryRead(String, Integer, Integer)},
	 * {@link #customQueryWrite(String)} or
	 * {@link #customIterator(String, Integer, Integer)}.
	 * <p>
	 * This method provides only minimal plausibility tests, so this
	 * initialization is not a guarantee for a valid query.
	 * 
	 * @throws HibernateException
	 *             if the plausibility tests fail
	 */
	void initialize() {
		if (this.parameters == null || this.parameters.isEmpty()) {
			HibernateException e = new HibernateException(
					"Error initializing query.");
			log.warn("Initialization falied.", e);
			throw e;
		}
		this.initialized = true;
	}

	/**
	 * This helper method tests the {@code flags} parameter the methods in e. g.
	 * {@link IPersistence} on the presence of the given flag.
	 * 
	 * @param flags
	 *            the parameter to test on the flag
	 * @param flag
	 *            the flag to test
	 * @return true if the flag is present in the {@code flags} parameter
	 */
	public static boolean hasFlag(int flags, int flag) {
		return (flags & flag) == flag;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DBQuery [session=");
		builder.append(this.session);
		builder.append(", initialized=");
		builder.append(this.initialized);
		builder.append(", parameters=");
		builder.append(this.parameters);
		builder.append(", whereParameters=");
		builder.append(this.whereParameters);
		builder.append(", selectParameters=");
		builder.append(this.selectParameters);
		builder.append("]");
		return builder.toString();
	}
}
