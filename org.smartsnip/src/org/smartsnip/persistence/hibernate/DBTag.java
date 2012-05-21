/**
 * File: DBTag.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.HashSet;
import java.util.Set;

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
	@Column(name = "tag_name", length = 50)
	private String name;

	@GeneratedValue
	@Column(name = "usage_freq", insertable = false, updatable = false)
	private Integer usageFrequence;

	// XXX remove snippets of tag if not necessary
//	@Column(name = "snippet_id")
//	@ManyToMany(targetEntity = DBSnippet.class, mappedBy="tags",fetch = FetchType.EAGER, cascade={CascadeType.MERGE, CascadeType.PERSIST})
//	@ForeignKey(name= "DBTag.name", inverseName = "DBSnippet.snippetId")
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	private Set<Long> snippets = new HashSet<Long>();

	DBTag() {
	}

	// XXX remove constructor
//	DBTag(String name) {
//		this.name = name;
//	}

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
	public Integer getUsageFrequence() {
		return this.usageFrequence;
	}

	/**
	 * @param usageFrequence
	 *            the usageFrequence to set
	 */
	public void setUsageFrequence(Integer usageFrequence) {
		this.usageFrequence = usageFrequence;
	}

//	/**
//	 * @return the snippets
//	 */
//	public Set<Long> getSnippets() {
//		return snippets;
//	}
//
//	/**
//	 * @param snippets
//	 *            the snippets to set
//	 */
//	public void setSnippets(Set<Long> snippets) {
//		this.snippets = snippets;
//	}

}