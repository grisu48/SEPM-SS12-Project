/**
 * File: DBSnippet.java
 * Date: 03.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;

/**
 * Database OR mapping class for table Snippet
 * 
 * @author littlelion
 * 
 */
@Entity
//TODO update hibernate see issue HHH-7074
//"the replacement annotations of @Entity are not working"
@SuppressWarnings("deprecation")
@org.hibernate.annotations.Entity(dynamicInsert = true)
//@DynamicInsert
@Table(name = "Snippet")
public class DBSnippet {

	@Id
	@GeneratedValue
	@Column(name = "snippet_id", insertable = false, updatable = false)
	private Long snippetId;

	@Column(name = "headline", length = 255)
	private String headline;

	@Lob
	@Column(name = "description")
	private String description;

	@GeneratedValue
	@Column(name = "viewcount", insertable = false, updatable = false)
	private Integer viewcount;

	@GeneratedValue
	@Column(name = "rating_average", insertable = false, updatable = false)
	private Float ratingAverage;

	@GeneratedValue
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_edited", insertable = false, updatable = false)
	private Date lastEdited;

	@Column(name = "user_name", length = 20)
	// @ManyToOne(targetEntity = DBUser.class, fetch = FetchType.EAGER)
	// @ForeignKey(name = "DBUser.userName")
	private String owner;

	@Column(name = "category_id")
	// @ManyToOne(targetEntity = DBCategory.class, fetch = FetchType.EAGER)
	// @ForeignKey(name = "DBCategory.categoryId")
	private Long categoryId;

	@Column(name = "license_id")
	// @ManyToOne(targetEntity = DBLicense.class, fetch = FetchType.EAGER)
	// @ForeignKey(name = "DBLicense.licenseId")
	private Long licenseId;

	@Column(name = "tag_name", length = 50)
	@ManyToMany(targetEntity = DBTag.class, fetch = FetchType.EAGER, cascade = {
			CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "RelTagSnippet", joinColumns = @JoinColumn(name = "snippet_id"), inverseJoinColumns = @JoinColumn(name = "tag_name"))
	@ForeignKey(name = "DBTag.tagName")
	private List<String> tags; //FIXME

	@OneToMany(targetEntity = DBComment.class, fetch = FetchType.LAZY, mappedBy = "commentId")
	@ForeignKey(name = "DBComent.commentId")
	private List<Long> comments; //FIXME

	/**
	 * 
	 */
	DBSnippet() {
		super();
	}

	// XXX remove constructor
	// /**
	// * @param snippetId
	// * @param headline
	// * @param description
	// * @param viewcount
	// * @param ratingAverage
	// * @param lastEdited
	// * @param owner
	// * @param categoryId
	// * @param licenseId
	// */
	// DBSnippet(Long snippetId, String headline, String description,
	// Integer viewcount, Float ratingAverage, Date lastEdited, String owner,
	// Long categoryId, Long licenseId) {
	// super();
	// this.snippetId = snippetId;
	// this.headline = headline;
	// this.description = description;
	// this.viewcount = viewcount;
	// this.ratingAverage = ratingAverage;
	// this.lastEdited = lastEdited;
	// this.owner = owner;
	// this.categoryId = categoryId;
	// this.licenseId = licenseId;
	// }

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
	 * @return the headline
	 */
	public String getHeadline() {
		return this.headline;
	}

	/**
	 * @param headline
	 *            the headline to set
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the viewcount
	 */
	public Integer getViewcount() {
		return this.viewcount;
	}

	/**
	 * @param viewcount
	 *            the viewcount to set
	 */
	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}

	/**
	 * @return the ratingAverage
	 */
	public Float getRatingAverage() {
		return this.ratingAverage;
	}

	/**
	 * @param ratingAverage
	 *            the ratingAverage to set
	 */
	public void setRatingAverage(Float ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

	/**
	 * @return the lastEdited
	 */
	public Date getLastEdited() {
		return this.lastEdited;
	}

	/**
	 * @param lastEdited
	 *            the lastEdited to set
	 */
	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return this.owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the licenseId
	 */
	public Long getLicenseId() {
		return this.licenseId;
	}

	/**
	 * @param licenseId
	 *            the licenseId to set
	 */
	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the comments
	 */
	List<Long> getComments() {
		return this.comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	void setComments(List<Long> comments) {
		this.comments = comments;
	}
}
