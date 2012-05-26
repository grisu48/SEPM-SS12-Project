/**
 * File: DBRelTagSnippet.java
 * Date: 24.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Database OR mapping class for table RelTagSnippet This class is a
 * relationship, no entity. So it is annotated to remove in an increment.
 * 
 * @author littlelion
 * 
 */
@Entity
// TODO update hibernate see issue HHH-7074
// TODO remove entity and infer relationship to DBSnippet
// "the replacement annotations of @Entity are not working"
@SuppressWarnings("deprecation")
@org.hibernate.annotations.Entity(dynamicInsert = true)
// @DynamicInsert
@Table(name = "RelTagSnippet")
public class DBRelTagSnippet {

	@EmbeddedId
	private RelTagSnippetId tagSnippetId;

	/**
	 * 
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

//	@Embeddable
//	class RelTagSnippetId implements Serializable {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -7191053313390779837L;
//
//		@NotNull
//		@Column(name = "snippet_id")
//		private Long snippetId;
//
//		@NotNull
//		@Column(name = "tag_name", length = 20)
//		private String tagName;
//
//		/**
//		 * 
//		 */
//		RelTagSnippetId() {
//			super();
//		}
//
//		/**
//		 * @param snippetId
//		 * @param tagName
//		 */
//		RelTagSnippetId(Long snippetId, String tagName) {
//			super();
//			this.snippetId = snippetId;
//			this.tagName = tagName;
//		}
//
//		/**
//		 * @return the snippetId
//		 */
//		public Long getSnippetId() {
//			return this.snippetId;
//		}
//
//		/**
//		 * @param snippetId
//		 *            the snippetId to set
//		 */
//		public void setSnippetId(Long snippetId) {
//			this.snippetId = snippetId;
//		}
//
//		/**
//		 * @return the tagName
//		 */
//		public String getTagName() {
//			return this.tagName;
//		}
//
//		/**
//		 * @param tagName
//		 *            the tagName to set
//		 */
//		public void setTagName(String tagName) {
//			this.tagName = tagName;
//		}
//
//		/**
//		 * @see java.lang.Object#hashCode()
//		 */
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + getOuterType().hashCode();
//			result = prime
//					* result
//					+ ((this.snippetId == null) ? 0 : this.snippetId.hashCode());
//			result = prime * result
//					+ ((this.tagName == null) ? 0 : this.tagName.hashCode());
//			return result;
//		}
//
//		/**
//		 * @see java.lang.Object#equals(java.lang.Object)
//		 */
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//			if (obj == null)
//				return false;
//			if (getClass() != obj.getClass())
//				return false;
//			RelTagSnippetId other = (RelTagSnippetId) obj;
//			if (!getOuterType().equals(other.getOuterType()))
//				return false;
//			if (this.snippetId == null) {
//				if (other.snippetId != null)
//					return false;
//			} else if (!this.snippetId.equals(other.snippetId))
//				return false;
//			if (this.tagName == null) {
//				if (other.tagName != null)
//					return false;
//			} else if (!this.tagName.equals(other.tagName))
//				return false;
//			return true;
//		}
//
//		private DBRelTagSnippet getOuterType() {
//			return DBRelTagSnippet.this;
//		}
//
//	}
}
