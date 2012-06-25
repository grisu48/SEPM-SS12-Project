/**
 * File: SqlPersistence.java
 * Date: 29.04.2012
 */
package org.smartsnip.persistence;

import org.smartsnip.persistence.hibernate.SqlPersistenceImpl;

/**
 * @author Gerhard Aigner
 *
 */
public class SqlPersistence extends SqlPersistenceImpl {

	/**
	 * @see SqlPersistenceImpl#SqlPersistenceImpl()
	 * @throws IllegalAccessException 
	 * 
	 */
	protected SqlPersistence() throws IllegalAccessException {
		super();
	}

}
