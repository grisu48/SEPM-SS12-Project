/**
 * File: DBCode.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * @author Gerhard Aigner
 * 
 */
@Entity
@Indexed
@DynamicInsert
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="EntityCache")
@Table(name = "Code")
public class DBCode {

	@Id
	@DocumentId
	@GeneratedValue
	@Column(name = "code_id", insertable = false, updatable = false)
	private Long codeId;

	@Column(name = "snippet_id")
	private Long snippetId;

	@Column(name = "language", length = 50)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String language;

	@Lob
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "file", columnDefinition = "TEXT")
	private String file;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "version")
	private Integer version;

	/**
	 * Entity, POJO class
	 */
	DBCode() {
		super();
	}

	/**
	 * @return the codeId
	 */
	public Long getCodeId() {
		return this.codeId;
	}

	/**
	 * @param codeId
	 *            the codeId to set
	 */
	public void setCodeId(Long codeId) {
		this.codeId = codeId;
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
	 * @return the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return this.file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.codeId == null) ? 0 : this.codeId.hashCode());
		result = prime * result
				+ ((this.file == null) ? 0 : this.file.hashCode());
		result = prime * result
				+ ((this.fileName == null) ? 0 : this.fileName.hashCode());
		result = prime * result
				+ ((this.language == null) ? 0 : this.language.hashCode());
		result = prime * result
				+ ((this.snippetId == null) ? 0 : this.snippetId.hashCode());
		result = prime * result
				+ ((this.version == null) ? 0 : this.version.hashCode());
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
		DBCode other = (DBCode) obj;
		if (this.codeId == null) {
			if (other.codeId != null)
				return false;
		} else if (!this.codeId.equals(other.codeId))
			return false;
		if (this.file == null) {
			if (other.file != null)
				return false;
		} else if (!this.file.equals(other.file))
			return false;
		if (this.fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!this.fileName.equals(other.fileName))
			return false;
		if (this.language == null) {
			if (other.language != null)
				return false;
		} else if (!this.language.equals(other.language))
			return false;
		if (this.snippetId == null) {
			if (other.snippetId != null)
				return false;
		} else if (!this.snippetId.equals(other.snippetId))
			return false;
		if (this.version == null) {
			if (other.version != null)
				return false;
		} else if (!this.version.equals(other.version))
			return false;
		return true;
	}
}
