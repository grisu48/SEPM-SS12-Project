/**
 * File: DBCode.java
 * Date: 04.05.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Code")
public class DBCode {

	@Id
	@GeneratedValue
	@Column(name = "code_id")
	private long codeId;

	@NotNull
	@ManyToOne(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "DBSnippet.snippetId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Column(name = "snippet_id")
	private long snippetId;

	@NotNull
	@Column(name = "language", length = 50)
	private String language;

	@Lob
	@Column(name = "file")
	private String file;

	@NotNull
	@Column(name = "version")
	private int version;

	/**
	 * 
	 */
	DBCode() {
		super();
	}

	/**
	 * @param codeId
	 * @param snippetId
	 * @param language
	 * @param file
	 */
	DBCode(long codeId, long snippetId, String language, String file) {
		super();
		this.codeId = codeId;
		this.snippetId = snippetId;
		this.language = language;
		this.file = file;
	}

	/**
	 * @return the codeId
	 */
	public long getCodeId() {
		return this.codeId;
	}

	/**
	 * @param codeId
	 *            the codeId to set
	 */
	public void setCodeId(long codeId) {
		this.codeId = codeId;
	}

	/**
	 * @return the snippetId
	 */
	public long getSnippetId() {
		return this.snippetId;
	}

	/**
	 * @param snippetId
	 *            the snippetId to set
	 */
	public void setSnippetId(long snippetId) {
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
