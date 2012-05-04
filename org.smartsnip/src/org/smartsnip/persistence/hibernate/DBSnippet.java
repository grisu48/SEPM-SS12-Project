/**
 * File: DBSnippet.java
 * Date: 03.05.2012
 */
package org.smartsnip.persistence.hibernate;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

/**
 * Database OR mapping class for table Snippet
 * @author littlelion
 *
 */
@Entity
@Table(name="Snippet")
public class DBSnippet {

	@Id
	@GeneratedValue
	@Column(name="snippet_id", insertable=false, updatable=false)
	private long snippetId;
	
	@Column(name="headline", length = 200)
	private String headline;
	
	@Lob
	@Column(name="description")
	private String description;
	
	@GeneratedValue
	@Column(name="viewcount", insertable=false, updatable=false)
	private int viewcount;
	
	@GeneratedValue
	@Column(name="rating_average", insertable=false, updatable=false)
	private float ratingAverage;
	
	@GeneratedValue
	@Column(name="last_edited", insertable=false, updatable=false)
	private Date lastEdited;
	
	@Column(name="user_name", length = 20)
	@ManyToOne(targetEntity=DBUser.class, fetch=FetchType.EAGER)
	@ForeignKey(name="DBUser.userName")
	private String owner;
	
	@Column(name="category_id")
	@ManyToMany(targetEntity=DBCategory.class, mappedBy="RelTagSnippet", fetch=FetchType.EAGER)
	@ForeignKey(name="DBCategory.categoryId")
	private long categoryId;
	
	@Column(name="license_id")
	@ManyToOne(targetEntity=DBLicense.class, fetch=FetchType.EAGER)
	@ForeignKey(name="DBLicense.licenseId")
	private long licenseId;

	/**
	 * 
	 */
	DBSnippet() {
		super();
	}

	/**
	 * @param snippetId
	 * @param headline
	 * @param description
	 * @param viewcount
	 * @param ratingAverage
	 * @param lastEdited
	 * @param owner
	 * @param categoryId
	 * @param licenseId
	 */
	DBSnippet(long snippetId, String headline, String description,
			int viewcount, float ratingAverage, Date lastEdited, String owner,
			long categoryId, long licenseId) {
		super();
		this.snippetId = snippetId;
		this.headline = headline;
		this.description = description;
		this.viewcount = viewcount;
		this.ratingAverage = ratingAverage;
		this.lastEdited = lastEdited;
		this.owner = owner;
		this.categoryId = categoryId;
		this.licenseId = licenseId;
	}

	/**
	 * @return the snippetId
	 */
	public long getSnippetId() {
		return this.snippetId;
	}

	/**
	 * @param snippetId the snippetId to set
	 */
	public void setSnippetId(long snippetId) {
		this.snippetId = snippetId;
	}

	/**
	 * @return the headline
	 */
	public String getHeadline() {
		return this.headline;
	}

	/**
	 * @param headline the headline to set
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the viewcount
	 */
	public int getViewcount() {
		return this.viewcount;
	}

	/**
	 * @param viewcount the viewcount to set
	 */
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}

	/**
	 * @return the ratingAverage
	 */
	public float getRatingAverage() {
		return this.ratingAverage;
	}

	/**
	 * @param ratingAverage the ratingAverage to set
	 */
	public void setRatingAverage(float ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

	/**
	 * @return the lastEdited
	 */
	public Date getLastEdited() {
		return this.lastEdited;
	}

	/**
	 * @param lastEdited the lastEdited to set
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
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the licenseId
	 */
	public long getLicenseId() {
		return this.licenseId;
	}

	/**
	 * @param licenseId the licenseId to set
	 */
	public void setLicenseId(long licenseId) {
		this.licenseId = licenseId;
	}
}
