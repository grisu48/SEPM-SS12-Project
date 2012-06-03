/**
 * File: DBRelTagSnippet.java
 * Date: 24.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

/**
 * Database OR mapping class for table RelTagSnippet This class is a
 * relationship, no entity. So it is annotated to remove in an increment.
 * 
 * @author littlelion
 * 
 */
@Entity
@DynamicInsert
@Table(name = "RelTagSnippet")
public class DBRelTagSnippet {

	@EmbeddedId
	private RelTagSnippetId tagSnippetId;

	/**
	 * Entity, POJO class
	 */
	DBRelTagSnippet() {
		super();
	}

	// XXX remove constructor
	// /**
	// * @param tagSnippetId
	// */
	// DBRelTagSnippet(Long snippetId, String userName) {
	// super();
	// this.tagSnippetId = new RelTagSnippetId(snippetId, userName);
	// }

	/**
	 * @return the tagSnippetId
	 */
	public RelTagSnippetId getTagSnippetId() {
		return this.tagSnippetId;
	}

	/**
	 * @param tagSnippetId
	 *            the tagSnippetId to set
	 */
	public void setTagSnippetId(RelTagSnippetId tagSnippetId) {
		this.tagSnippetId = tagSnippetId;
	}

	/**
	 * @param snippetId
	 *            the snippetId to set
	 * @param tagName
	 *            the tagName to set
	 */
	public void setTagSnippetId(Long snippetId, String tagName) {
		this.tagSnippetId = new RelTagSnippetId(snippetId, tagName);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.tagSnippetId == null) ? 0 : this.tagSnippetId
						.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBRelTagSnippet other = (DBRelTagSnippet) obj;
		if (this.tagSnippetId == null) {
			if (other.tagSnippetId != null)
				return false;
		} else if (!this.tagSnippetId.equals(other.tagSnippetId))
			return false;
		return true;
	}
}
