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
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.NaturalId;
import org.smartsnip.persistence.IPersistence;
import org.smartsnip.shared.Pair;

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
	private static final int QUERY_NOT_EMPTY = 2048;

	/**
	 * Constant for the methods which provide the boolean parameter "notEmpty".
	 * If set to this constant the method returns successful, even though the
	 * result is an empty query string.
	 */
	private static final int QUERY_EMPTY_VALID = 0;

	/**
	 * Constant for the methods which provide the boolean parameter
	 * "allParameters". If set to this constant the method returns a query which
	 * contains a where clause with all parameters including the parameters
	 * which aren't declared in the list of where parameters.
	 */
	private static final int QUERY_ALL_PARAMETERS = 4096;

	/**
	 * Constant for the methods which provide the boolean parameter
	 * "allParameters". If set to this constant the method returns a query which
	 * contains a where clause only with the parameters declared in the list of
	 * where parameters.
	 */
	private static final int QUERY_WHERE_PARAMETERS_ONLY = 0;

	/**
	 * Constant for the methods which provide the {@code int} parameter
	 * {@code flags}. If set to this constant the method may return a null
	 * result. This constant is the default. It is overridden by the constant
	 * {@link #QUERY_NOT_NULL}. It doesn't interfere with constants of the
	 * {@link IPersistence} interface, so it is not necessary to sort out them.
	 * If combined with them it evaluates to {@link IPersistence#DB_DEFAULT}.
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
	 * would return. This constant overrides {@link #QUERY_NULLABLE}. It doesn't
	 * interfere with constants of the {@link IPersistence} interface, so it is
	 * not necessary to sort out them.
	 */
	static final int QUERY_NOT_NULL = 512;

	/**
	 * Constant for the methods which provide the {@code int} parameter
	 * {@code flags}. If set to this constant the method fails if a null result
	 * would return. It doesn't interfere with constants of the
	 * {@link IPersistence} interface, so it is not necessary to sort out them.
	 */
	static final int QUERY_UNIQUE_RESULT = 1024;

	/**
	 * Set this flag to add the {@link Query#setCacheable(boolean)} property to
	 * the query.
	 */
	static final int QUERY_CACHEABLE = 8192;

	/**
	 * Set this flag to use the transformation string of the
	 * {@link org.hibernate.annotations.ColumnTransformer} annotation in this
	 * query.
	 */
	private static final int COLUMN_TRANSFORMER = 16384;

	/**
	 * Set this flag to use the write transformation string of the
	 * {@link org.hibernate.annotations.ColumnTransformer} annotation in this
	 * query. This flag contains the {@link #COLUMN_TRANSFORMER} flag, so it is
	 * not necessary to add both flags.
	 */
	// don't use the value 32768 for a flag
	private static final int COLUMN_TRANSFORMER_WRITE = 49152;

	/**
	 * Order clause used with the {@link #addOrder(String, boolean)} method.
	 */
	public static final Boolean ORDER_ASCENDING = false;

	/**
	 * Order clause used with the {@link #addOrder(String, boolean)} method.
	 */
	public static final Boolean ORDER_DESCENDING = true;

	/**
	 * the session which owns this query
	 */
	private final Session session;

	/**
	 * initialized state: calling twice {@link #getParameters(Object, int)} will
	 * fail.
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
	 * Contains all {@link ColumnTransformer} attached to a parameter.
	 */
	private Map<String, ColumnTransformer> transformers = new TreeMap<String, ColumnTransformer>();

	/**
	 * Contains all order clauses. Use {@link #addOrder(String, boolean)} to
	 * define an order clause of a query.
	 */
	private List<Pair<String, Boolean>> orderClauses = new ArrayList<Pair<String, Boolean>>();

	/**
	 * Contains the region of the query cache in which the query is to store. If
	 * the region is {@code null} the default region is targeted. Use the
	 * {@link #QUERY_CACHEABLE} flag to activate the caching for this query.
	 */
	private String cacheRegion = null;

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
	void addWhereParameter(String before, String parameter, String after,
			Object value) {
		Vector<String> args = new Vector<String>(3);
		args.add(before);
		args.add(parameter);
		args.add(after);
		whereParameters.add(new Pair<Vector<String>, Object>(args, value));
	}

	/**
	 * add a order clause to the query.
	 * 
	 * @param column
	 *            the name of the column.
	 * @param order
	 *            Use the constant {@link #ORDER_ASCENDING} for ascending order
	 *            or {@link #ORDER_DESCENDING} for descending order.
	 */
	void addOrder(String column, boolean order) {
		this.orderClauses.add(new Pair<String, Boolean>(column, order));
	}

	/**
	 * Concatenate the parameters to a HQL update query.
	 * 
	 * @param targetEntity
	 * @param flags
	 *            Set the {@link #COLUMN_TRANSFORMER} if and only if the
	 *            {@link Query#executeUpdate()} method is used.
	 * @return the update query string
	 */
	private String buildUpdateQueryString(String targetEntity, int flags) {
		int modFlags = flags;
		if (hasFlag(flags, COLUMN_TRANSFORMER)) {
			// use write transformer
			modFlags = flags | COLUMN_TRANSFORMER_WRITE;
		}
		StringBuilder builder = new StringBuilder();
		boolean comma = false;
		builder.append("update ").append(targetEntity).append(" set ");
		for (Pair<String, Object> p : parameters) {
			if (comma) {
				builder.append(", ");
			}

			builder.append(p.first).append(" = ")
					.append(buildTransformedParameter(p.first, modFlags));
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
	 * Build the parameter where possible transformer strings are considered.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param flags
	 *            Set the {@link #COLUMN_TRANSFORMER} flag if the transformer is
	 *            to consider. Use the {@link # COLUMN_TRANSFORMER_WRITE} flag
	 *            for parameters which are to write.
	 * @return the transformer String
	 */
	private String buildTransformedParameter(String parameter, int flags) {
		String result = parameter;
		if (hasFlag(flags, COLUMN_TRANSFORMER)
				&& this.transformers.containsKey(parameter)) {
			if (hasFlag(flags, COLUMN_TRANSFORMER_WRITE)
					&& !this.transformers.get(parameter).write().isEmpty()) {
				result = this.transformers.get(parameter).write();
				result = result.replace("?", " :" + parameter);
			} else if (!this.transformers.get(parameter).read().isEmpty()) {
				result = this.transformers.get(parameter).read();
			}
		} else {
			result = ":" + parameter;
		}
		return result;
	}

	/**
	 * Build the order clause using the {@link #orderClauses} list.
	 * 
	 * @return the order clause as string or an empty string if no order clause
	 *         is defined.
	 */
	private String buildOrderClause() {
		StringBuilder builder = new StringBuilder(" order by ");
		boolean hasOrderClauses = false;
		for (Pair<String, Boolean> clause : this.orderClauses) {
			if (hasOrderClauses) {
				builder.append(", ");
			}
			builder.append(clause.first);
			if (clause.second == ORDER_DESCENDING) {
				builder.append(" desc");
			} else {
				builder.append(" asc");
			}
			hasOrderClauses = true;
		}
		if (hasOrderClauses) {
			return builder.toString();
		}
		return "";
	}

	/**
	 * Concatenate the where parameters to a HQL where clause.
	 * 
	 * @param flags
	 *            If the flag {@code #QUERY_NOT_EMPTY} an empty where clause is
	 *            rejected. Use the constants {@link #QUERY_NOT_EMPTY} or
	 *            {@link #QUERY_EMPTY_VALID}. Set the
	 *            {@link #COLUMN_TRANSFORMER} if and only if the
	 *            {@link Query#executeUpdate()} method is used.
	 * @return the where clause or an empty string if no where parameter is set
	 *         and the notEmpty flag is set to {@link #QUERY_EMPTY_VALID}
	 */
	private String buildWhereClause(int flags) {
		boolean valid = false;
		int modFlags = flags;
		if (hasFlag(flags, COLUMN_TRANSFORMER)) {
			// use read transformer
			modFlags = flags & ~COLUMN_TRANSFORMER_WRITE;
		}
		StringBuilder builder = new StringBuilder("where ");
		for (Pair<Vector<String>, Object> p : this.whereParameters) {
			if (valid) {
				builder.append("and ");
			}
			builder.append(p.first.get(0))
					.append(" ")
					.append(buildTransformedParameter(p.first.get(1), modFlags))
					.append(" ").append(p.first.get(2)).append(" ");
			valid = true;
		}
		if (!valid) {
			if (hasFlag(flags, QUERY_NOT_EMPTY)) {
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
	 * @param flags
	 *            If the flag {@code #QUERY_NOT_EMPTY} is present an empty where
	 *            clause is rejected. Use the constants {@link #QUERY_NOT_EMPTY}
	 *            or {@link #QUERY_EMPTY_VALID}. Set the
	 *            {@link #COLUMN_TRANSFORMER} if and only if the
	 *            {@link Query#executeUpdate()} method is used.
	 * @return the where clause or an empty string if no where parameter is set
	 *         and the notEmpty flag is set to {@link #QUERY_EMPTY_VALID}
	 */
	private String buildWhereFromAllParameters(int flags) {
		boolean valid = false;
		int modFlags = flags;
		if (hasFlag(flags, COLUMN_TRANSFORMER)) {
			modFlags = flags | COLUMN_TRANSFORMER_WRITE;
		}
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
			builder.append(p.first).append(" = ")
					.append(buildTransformedParameter(p.first, modFlags))
					.append(" ");
			valid = true;
			if (!valid) {
				if (hasFlag(flags, QUERY_NOT_EMPTY)) {
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return the update query defined by parameters and where clauses
	 */
	private Query buildUpdateQuery(Object targetEntity, int flags) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
		Query result = this.session.createQuery(this.buildUpdateQueryString(
				targetEntity.getClass().getName(), flags));
		for (Pair<String, Object> p : this.parameters) {
			result.setParameter(p.first, p.second);
		}
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		if (hasFlag(flags, QUERY_CACHEABLE)) {
			result.setCacheable(true);
			if (this.cacheRegion != null) {
				result.setCacheRegion(this.cacheRegion);
			}
		}
		log.debug(result);
		return result;
	}

	/**
	 * Build a HQL from query.
	 * 
	 * @param targetEntity
	 * @param flags
	 *            If the flag {@link #QUERY_ALL_PARAMETERS} is present all
	 *            parameters including them which aren't declared as where
	 *            parameter are considered. Use constant
	 *            {@link #QUERY_ALL_PARAMETERS} or
	 *            {@link #QUERY_WHERE_PARAMETERS_ONLY}. Add the
	 *            {@link #QUERY_CACHEABLE} flag to store this query in the
	 *            assigned cache.
	 * @return the update query defined by parameters and where clauses
	 */
	private Query buildFromQuery(Object targetEntity, int flags) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
		Query result = this.session.createQuery(buildFromQueryString(
				targetEntity, flags));
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		if (hasFlag(flags, QUERY_ALL_PARAMETERS)) {
			for (Pair<String, Object> p : this.parameters) {
				result.setParameter(p.first, p.second);
			}
		}
		if (hasFlag(flags, QUERY_CACHEABLE)) {
			result.setCacheable(true);
			if (this.cacheRegion != null) {
				result.setCacheRegion(this.cacheRegion);
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
	 *            If set to {@link #QUERY_ALL_PARAMETERS} all parameters
	 *            including them which aren't declared as where parameter are
	 *            considered. Use constant {@link #QUERY_ALL_PARAMETERS} or
	 *            {@link #QUERY_WHERE_PARAMETERS_ONLY}.
	 * @return the from query string
	 */
	private String buildFromQueryString(Object targetEntity, int allParameters) {
		StringBuilder builder = new StringBuilder();
		builder.append("from ").append(targetEntity.getClass().getName())
				.append(" ");
		if (hasFlag(allParameters, QUERY_ALL_PARAMETERS)) {
			builder.append(buildWhereFromAllParameters(QUERY_EMPTY_VALID));
		} else {
			builder.append(buildWhereClause(QUERY_EMPTY_VALID));
		}
		builder.append(buildOrderClause());
		log.trace(builder.toString());
		return builder.toString();
	}

	/**
	 * Extract parameters from the entity and add them to the parameter lists.
	 * 
	 * @param targetEntity
	 * @param flags
	 *            set {@link IPersistence#DB_FORCE_NULL_VALUES} if null values
	 *            are to write into the database. Any attempt to write null into
	 *            a column declared as not null will be rejected. Use constant
	 *            {@link IPersistence#DB_DEFAULT} to skip null values.
	 * @return the primary key (Id). If the Id is no instance of
	 *         {@code Serializable} or the Id coldn't be fetched {@code null} is
	 *         returned.
	 */
	private Serializable getParameters(Object targetEntity, int flags) {
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
						&& ((value = m.invoke(targetEntity, (Object[]) null)) != null || hasFlag(
								flags, IPersistence.DB_FORCE_NULL_VALUES))) {
					if (hasFlag(flags, COLUMN_TRANSFORMER)) {
						addColumnTransformer(targetEntity, parameter, m);
					}
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

	/**
	 * Tests the method and the field on the presence of a
	 * {@link org.hibernate.annotations.ColumnTransformer} annotation and adds
	 * it to the {@link #transformers} map.
	 * 
	 * @param object
	 *            the object to test
	 * @param field
	 *            the name of the field
	 * @param method
	 *            the name of the targeted getter method.
	 */
	private void addColumnTransformer(Object object, String field, Method method) {
		ColumnTransformer annotation;
		try {
			if ((annotation = method.getAnnotation(ColumnTransformer.class)) != null) {
				transformers.put(field, annotation);
			} else if ((annotation = object.getClass().getDeclaredField(field)
					.getAnnotation(ColumnTransformer.class)) != null) {
				transformers.put(field, annotation);
			}
		} catch (NoSuchFieldException ignored) {
		}
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
	 *            If the flag {@code #QUERY_NOT_NULL} is present the query fails
	 *            on a null result. This method fails with a
	 *            {@link NullPointerException} if the key results to null and
	 *            the flag {@link #QUERY_NOT_NULL} is set. The constant
	 *            {@link #QUERY_UNIQUE_RESULT} is the default for this method.
	 *            Add the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
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
				if (hasFlag(flags, QUERY_CACHEABLE)) {
					query.setCacheable(true);
					if (this.cacheRegion != null) {
						query.setCacheRegion(this.cacheRegion);
					}
				}
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
	 *            If sthe flag {@code #QUERY_NOT_NULL} is present the query
	 *            fails on a null result. Use constants {@link #QUERY_NOT_NULL}
	 *            or {@link #QUERY_NULLABLE}. The constant
	 *            {@link #QUERY_UNIQUE_RESULT} is ignored.
	 * @throws NullPointerException
	 *             if a field contains null.
	 * @see #getParameters(Object, int)
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
	 * @param flags
	 *            set {@link IPersistence#DB_FORCE_NULL_VALUES} if null values
	 *            are to write into the database. Any attempt to write null into
	 *            a column declared as not null will be rejected. Use constant
	 *            {@link IPersistence#DB_DEFAULT} to skip null values.
	 * @return the primary key (Id) of the entity
	 */
	Serializable update(Object targetEntity, int flags) {
		Serializable key = getParameters(targetEntity, flags
				| COLUMN_TRANSFORMER);
		if (!this.parameters.isEmpty()) {
			key = getKey(targetEntity, QUERY_NOT_NULL);
			if (buildUpdateQuery(targetEntity, flags | COLUMN_TRANSFORMER)
					.executeUpdate() < 1) {
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
	 * @param flags
	 *            set {@link IPersistence#DB_FORCE_NULL_VALUES} if null values
	 *            are to write into the database. Any attempt to write null into
	 *            a column declared as not null will be rejected. Use constant
	 *            {@link IPersistence#DB_DEFAULT} to skip null values.
	 * @return the primary key (Id) of the entity
	 */
	Serializable insertOrUpdate(Object targetEntity, int flags) {
		Serializable key = getParameters(targetEntity, flags
				| COLUMN_TRANSFORMER);
		if ((key = getKey(targetEntity, QUERY_NULLABLE)) != null) {
			if (!this.parameters.isEmpty()) {
				if (buildUpdateQuery(targetEntity, flags | COLUMN_TRANSFORMER)
						.executeUpdate() < 1) {
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
			return this.update(targetEntity, flags);
		} else { // IPersistence.DB_DEFAULT case
			return this.insertOrUpdate(targetEntity, flags);
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
				IPersistence.DB_FORCE_NULL_VALUES | COLUMN_TRANSFORMER);
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
				} else if (buildUpdateQuery(targetEntity,
						flags | COLUMN_TRANSFORMER).executeUpdate() < 1) {
					throw new HibernateException(
							"failed to update entity with key" + key.toString());
				}
			} else { // IPersistence.DB_DEFAULT case
				if (disable == null || cause != null) {
					if (buildDeleteQuery(targetEntity,
							flags | COLUMN_TRANSFORMER).executeUpdate() < 1) {
						throw new HibernateException(
								"failed to delete entity with key: "
										+ key.toString());
					}
					key = null;
				} else if (buildUpdateQuery(targetEntity,
						flags | COLUMN_TRANSFORMER).executeUpdate() < 1) {
					throw new HibernateException(
							"failed to update entity with key" + key.toString());
				}
			}

		} else { // DB_FORCE_DELETE case
			if (buildDeleteQuery(targetEntity, flags | COLUMN_TRANSFORMER)
					.executeUpdate() < 1) {
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return the delete query defined by the where clauses
	 */
	private Query buildDeleteQuery(Object targetEntity, int flags) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
		Query result = this.session.createQuery(this.buildDeleteQueryString(
				targetEntity.getClass().getName(), flags));
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		if (hasFlag(flags, QUERY_CACHEABLE)) {
			result.setCacheable(true);
			if (this.cacheRegion != null) {
				result.setCacheRegion(this.cacheRegion);
			}
		}
		log.debug(result);
		return result;
	}

	/**
	 * Concatenate the where-parameters to a HQL delete query.
	 * 
	 * @param targetEntity
	 * @param flags
	 *            Set the {@link #COLUMN_TRANSFORMER} if and only if the
	 *            {@link Query#executeUpdate()} method is used.
	 * @return the delete query
	 */
	private String buildDeleteQueryString(String targetEntity, int flags) {
		StringBuilder builder = new StringBuilder();
		builder.append("delete ").append(targetEntity).append(" ");
		builder.append(buildWhereClause(flags | QUERY_NOT_EMPTY));
		log.trace(builder.toString());
		return builder.toString();
	}

	/**
	 * Perform a delete query.
	 * 
	 * @param targetEntity
	 *            the entity to delete
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 */
	void delete(Object targetEntity, int flags) {
		Serializable key = getParameters(targetEntity, IPersistence.DB_DEFAULT
				| COLUMN_TRANSFORMER);
		if (key == null) {
			throw new HibernateException("delete query needs a serializable Id");
		}
		log.assertLog(key != null, "The primary key of " + targetEntity
				+ " must not be null.");

		if (buildDeleteQuery(targetEntity, flags | COLUMN_TRANSFORMER)
				.executeUpdate() < 1) {
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return a list of entities of type {@code <T>}
	 */
	<T> List<T> from(T targetEntity, int flags) {
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildFromQuery(targetEntity, flags | QUERY_ALL_PARAMETERS);
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return a list of entities of type {@code <T>}
	 */
	<T> List<T> from(T targetEntity, Integer start, Integer count, int flags) {
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildFromQuery(targetEntity, flags | QUERY_ALL_PARAMETERS);
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
	 *            If the flag {@code #QUERY_NOT_NULL} is present the query fails
	 *            on a null result. Use the constants {@link #QUERY_NOT_NULL},
	 *            {@link #QUERY_NULLABLE} and {@link #QUERY_UNIQUE_RESULT}. Use
	 *            a bitwise or to set multiple flags. Add the
	 *            {@link #QUERY_CACHEABLE} flag to store this query in the
	 *            assigned cache.
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
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildFromQuery(targetEntity, flags | QUERY_ALL_PARAMETERS);
		return (T) singleQuery(query, flags);
	}

	/**
	 * Perform a query which returns a single object.
	 * 
	 * @param query
	 *            a ready to perform query
	 * @param flags
	 *            If the flag {@code #QUERY_NOT_NULL} is present the query fails
	 *            on a null result. Use the constants {@link #QUERY_NOT_NULL},
	 *            {@link #QUERY_NULLABLE} and {@link #QUERY_UNIQUE_RESULT}. Use
	 *            a bitwise or to set multiple flags.
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return an iterator of type {@code <T>}
	 */
	<T> Iterator<T> iterate(T targetEntity, int flags) {
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildFromQuery(targetEntity, flags | QUERY_ALL_PARAMETERS);
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return an iterator of type {@code <T>}
	 */
	<T> Iterator<T> iterate(T targetEntity, Integer start, Integer count,
			int flags) {
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildFromQuery(targetEntity, flags | QUERY_ALL_PARAMETERS);
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return a list of results according to the select string
	 */
	List<?> select(Object targetEntity, String selectString, int flags) {
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildSelectQuery(targetEntity, selectString, flags
				| QUERY_ALL_PARAMETERS);
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return a list of results according to the select string
	 */
	List<?> select(Object targetEntity, String selectString, Integer start,
			Integer count, int flags) {
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildSelectQuery(targetEntity, selectString, flags
				| QUERY_ALL_PARAMETERS);
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
	 *            If the flag {@code #QUERY_NOT_NULL} is present the query fails
	 *            on a null result. Use the constants {@link #QUERY_NOT_NULL},
	 *            {@link #QUERY_NULLABLE} and {@link #QUERY_UNIQUE_RESULT}. Use
	 *            a bitwise or to set multiple flags. Add the
	 *            {@link #QUERY_CACHEABLE} flag to store this query in the
	 *            assigned cache.
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
		getParameters(targetEntity, IPersistence.DB_DEFAULT);
		Query query = buildSelectQuery(targetEntity, selectString, flags
				| QUERY_ALL_PARAMETERS);
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
	 * @param flags
	 *            If set to {@link #QUERY_ALL_PARAMETERS} all parameters
	 *            including them which aren't declared as where parameter are
	 *            considered. Use constant {@link #QUERY_ALL_PARAMETERS} or
	 *            {@link #QUERY_WHERE_PARAMETERS_ONLY}. Add the
	 *            {@link #QUERY_CACHEABLE} flag to store this query in the
	 *            assigned cache. Add the {@link #QUERY_CACHEABLE} flag to store
	 *            this query in the assigned cache.
	 * @return the select query
	 */
	private Query buildSelectQuery(Object targetEntity, String selectString,
			int flags) {
		if (!this.initialized) {
			throw new HibernateException("The query of target "
					+ targetEntity.getClass().getSimpleName()
					+ " is not initialized.");
		}
		StringBuilder builder = new StringBuilder("select ");
		builder.append(selectString).append(" ")
				.append(buildFromQueryString(targetEntity, flags));
		builder.append(buildOrderClause());
		Query result = this.session.createQuery(builder.toString());
		for (Pair<String, Object> s : this.selectParameters) {
			result.setParameter(s.first, s.second);
		}
		for (Pair<Vector<String>, Object> w : this.whereParameters) {
			result.setParameter(w.first.get(1), w.second);
		}
		if (hasFlag(flags, QUERY_ALL_PARAMETERS)) {
			for (Pair<String, Object> p : this.parameters) {
				result.setParameter(p.first, p.second);
			}
		}
		if (hasFlag(flags, QUERY_CACHEABLE)) {
			result.setCacheable(true);
			if (this.cacheRegion != null) {
				result.setCacheRegion(this.cacheRegion);
			}
		}
		log.debug(result);
		return result;
	}

	/**
	 * Perform a query to count all entries of a table.
	 * 
	 * @param targetEntity
	 *            the entity to count
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return the number of entries
	 */
	Long count(Object targetEntity, int flags) {
		return (Long) select(targetEntity, "count(*)", flags).get(0);
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return a list of entities
	 */
	List<?> customQueryRead(String queryString, Integer start, Integer count,
			int flags) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialize();
		Query query = buildCustomQuery(queryString, flags);
		if (start != null && start > 0) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		return query.list();
	}

	/**
	 * Perform a query with a custom HQL query string. This query returns a
	 * single result. The parameters must be set with
	 * {@link #addParameter(String, Object)} before calling this method . Call
	 * {@link #initialize()} after adding the parameters.
	 * 
	 * @param queryString
	 *            must be a valid HQL query. The parameters in the
	 *            parameters-list must be equal to the parameters in the query
	 *            string.
	 * @param flags
	 *            If the flag {@code #QUERY_NOT_NULL} is present the query fails
	 *            on a null result. Use the constants {@link #QUERY_NOT_NULL},
	 *            {@link #QUERY_NULLABLE} and {@link #QUERY_UNIQUE_RESULT}. Use
	 *            a bitwise or to set multiple flags. Add the
	 *            {@link #QUERY_CACHEABLE} flag to store this query in the
	 *            assigned cache.
	 * @return a single entity
	 * @throws NullPointerException
	 *             if the query results to null and the {@link #QUERY_NOT_NULL}
	 *             flag is set.
	 * @throws NonUniqueResultException
	 *             if the underlying SQL-query returns a non unique result and
	 *             the {@link #QUERY_UNIQUE_RESULT} flag is set.
	 */
	Object customSingleQueryRead(String queryString, int flags) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialize();
		Query query = buildCustomQuery(queryString, flags);
		return singleQuery(query, flags);
	}

	/**
	 * Perform a query with a custom HQL query string. The parameters must be
	 * set with {@link #addParameter(String, Object)} before calling this method
	 * . Call {@link #initialize()} after adding the parameters. The column
	 * transformer doesn't work for this query, so it is necessary to insert the
	 * transformation string directly into the query string.
	 * 
	 * @param queryString
	 *            must be a valid HQL query. The parameters in the
	 *            parameters-list must be equal to the parameters in the query
	 *            string.
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return the number of modified rows.
	 */
	int customQueryWrite(String queryString, int flags) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialize();
		Query query = buildCustomQuery(queryString, flags);
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return the query ready to perform
	 */
	Query buildCustomQuery(String queryString, int flags) {
		if (!this.initialized) {
			throw new HibernateException(
					"The query of customQuery() is not initialized.");
		}
		Query query = this.session.createQuery(queryString);
		for (Pair<String, Object> p : this.parameters) {
			query.setParameter(p.first, p.second);
		}
		if (hasFlag(flags, QUERY_CACHEABLE)) {
			query.setCacheable(true);
			if (this.cacheRegion != null) {
				query.setCacheRegion(this.cacheRegion);
			}
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
	 * @param flags
	 *            Set the {@link #QUERY_CACHEABLE} flag to store this query in
	 *            the assigned cache.
	 * @return a list of entities of type {@code <T>}
	 */
	Iterator<?> customIterator(String queryString, Integer start,
			Integer count, int flags) {
		if (this.initialized) {
			throw new IllegalStateException(
					"Malformed query caused by multible initialization.");
		}
		this.initialize();
		Query query = buildCustomQuery(queryString, flags);
		if (start != null && start > 0) {
			query.setFirstResult(start);
		}
		if (count != null && count > 0) {
			query.setFetchSize(count);
		}
		return query.iterate();
	}

	/**
	 * @param cacheRegion
	 *            the cacheRegion to set
	 */
	void setCacheRegion(String cacheRegion) {
		this.cacheRegion = cacheRegion;
	}

	/**
	 * Reset this query. All parameters are cleared and the initialized state is
	 * set to {@code false}.
	 */
	void reset() {
		this.parameters = new ArrayList<Pair<String, Object>>();
		this.whereParameters = new ArrayList<Pair<Vector<String>, Object>>();
		this.selectParameters = new ArrayList<Pair<String, Object>>();
		this.orderClauses = new ArrayList<Pair<String,Boolean>>();
		this.cacheRegion = null;
		this.initialized = false;
		log.trace("initialized = false");
	}

	/**
	 * Initialize this query. This method is invoked by some methods of the
	 * {@code DBQuery} class e. g.
	 * {@link #customQueryRead(String, Integer, Integer, int)},
	 * {@link #customQueryWrite(String, int)} or
	 * {@link #customIterator(String, Integer, Integer, int)}.
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
