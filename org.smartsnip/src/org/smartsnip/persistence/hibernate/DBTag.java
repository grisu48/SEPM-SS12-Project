/**
 * File: DBTag.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Database OR mapping class for table Tag
 * 
 * @author littlelion
 * 
 */
@Entity
@Table(name = "Tag")
class DBTag {
	@Id
	@Column(name = "name", length = 50)
	@ManyToMany(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private String name;

	@NotNull
	@GeneratedValue
	@Column(name = "usage_freq", insertable = false, updatable = false)
	private int usageFrequence;

	@Column(name = "snippet_id")
	@ManyToMany(targetEntity = DBSnippet.class, fetch = FetchType.EAGER)
	@ForeignKey(inverseName = "DBSnippet.snippetId", name = "DBTag.snippetId")
	private List<Long> snippets;

	DBTag() {
	}

	DBTag(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the usageFrequence
	 */
	public int getUsageFrequence() {
		return this.usageFrequence;
	}

	/**
	 * @param usageFrequence
	 *            the usageFrequence to set
	 */
	public void setUsageFrequence(int usageFrequence) {
		this.usageFrequence = usageFrequence;
	}

	/**
	 * @return the snippets
	 */
	public List<Long> getSnippets() {
		return snippets;
	}

	/**
	 * @param snippets
	 *            the snippets to set
	 */
	public void setSnippets(List<Long> snippets) {
		this.snippets = snippets;
	}

}