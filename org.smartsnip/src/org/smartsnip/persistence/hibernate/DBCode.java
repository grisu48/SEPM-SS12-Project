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

/**
 * @author littlelion
 * 
 */
@Entity
//TODO update hibernate see issue HHH-7074
//"the replacement annotations of @Entity are not working"
@SuppressWarnings("deprecation")
@org.hibernate.annotations.Entity(dynamicInsert = true)
//@DynamicInsert
@Table(name = "Code")
public class DBCode {

	@Id
	@GeneratedValue
	@Column(name = "code_id")
	private Long codeId;

//	@ManyToOne(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
//	@ForeignKey(name = "DBSnippet.snippetId")
//	@OnDelete(action = OnDeleteAction.CASCADE)
	@Column(name = "snippet_id")
	private Long snippetId;

	@Column(name = "language", length = 50)
	private String language;

	@Lob
	@Column(name = "file")
	private String file;

	@Column(name = "version")
	private Integer version;

	/**
	 * 
	 */
	DBCode() {
		super();
	}

	// XXX remove constructor
	// /**
	// * @param codeId
	// * @param snippetId
	// * @param language
	// * @param file
	// */
	// DBCode(Long codeId, Long snippetId, String language, String file) {
	// super();
	// this.codeId = codeId;
	// this.snippetId = snippetId;
	// this.language = language;
	// this.file = file;
	// }

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
}
