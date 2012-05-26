/**
 * File: RelTagSnippetId.java
 * Date: 25.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Id class for the entity {@link DBRelTagSnippet}
 * @author littlelion
 *
 */
@Embeddable
class RelTagSnippetId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1652441768090524802L;

	@NotNull
	@Column(name = "snippet_id")
	private Long snippetId;

	@NotNull
	@Column(name = "tag_name", length = 20)
	private String tagName;

	/**
	 * 
	 */
	RelTagSnippetId() {
		super();
	}

	/**
	 * @param snippetId
	 * @param tagName
	 */
	RelTagSnippetId(Long snippetId, String tagName) {
		super();
		this.snippetId = snippetId;
		this.tagName = tagName;
	}

	/**
	 * @return the snippetId
	 */
	public Long getSnippetId() {
		return this.snippetId;
	}

	/**
	 * @param snippetId
	 *            the snippetId to set
	 */
	public void setSnippetId(Long snippetId) {
		this.snippetId = snippetId;
	}

	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return this.tagName;
	}

	/**
	 * @param tagName
	 *            the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.snippetId == null) ? 0 : this.snippetId.hashCode());
		result = prime * result
				+ ((this.tagName == null) ? 0 : this.tagName.hashCode());
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
		RelTagSnippetId other = (RelTagSnippetId) obj;
		if (this.snippetId == null) {
			if (other.snippetId != null)
				return false;
		} else if (!this.snippetId.equals(other.snippetId))
			return false;
		if (this.tagName == null) {
			if (other.tagName != null)
				return false;
		} else if (!this.tagName.equals(other.tagName))
			return false;
		return true;
	}
}
