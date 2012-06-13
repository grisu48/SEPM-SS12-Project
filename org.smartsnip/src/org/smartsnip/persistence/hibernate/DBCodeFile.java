/**
 * File: DBCodeFile.java
 * Date: 12.06.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.search.annotations.DocumentId;
import org.smartsnip.persistence.IPersistence;

/**
 * @author littlelion
 *
 */
@Entity
@DynamicInsert
@Table(name="Code")
public class DBCodeFile {

	@Id
	@DocumentId
	@GeneratedValue
	@Column(name = "code_id", insertable = false, updatable = false)
	private Long codeId;

	@Column(name="file_name")
	private String fileName;

	@Lob
	@Column(name="file_content", columnDefinition="MEDIUMBLOB")
	private Byte[] fileContent;

	/**
	 * 
	 */
	DBCodeFile() {
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
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileContent
	 */
	public Byte[] getFileContent() {
		return this.fileContent;
	}

	/**
	 * @param fileContent the fileContent to set
	 */
	public void setFileContent(Byte[] fileContent) {
		this.fileContent = fileContent;
	}

	/**
	 * This method is called by the remove methods of IPersistence in
	 * {@code DB_NO_DELETE} mode. If this method is present the remove method
	 * defaults to {@link IPersistence#DB_NO_DELETE}.
	 */
	void disable() {
		this.fileName = "";
		this.fileContent = new Byte[0];
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
		result = prime * result + Arrays.hashCode(this.fileContent);
		result = prime * result
				+ ((this.fileName == null) ? 0 : this.fileName.hashCode());
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
		DBCodeFile other = (DBCodeFile) obj;
		if (this.codeId == null) {
			if (other.codeId != null)
				return false;
		} else if (!this.codeId.equals(other.codeId))
			return false;
		if (!Arrays.equals(this.fileContent, other.fileContent))
			return false;
		if (this.fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!this.fileName.equals(other.fileName))
			return false;
		return true;
	}
}
