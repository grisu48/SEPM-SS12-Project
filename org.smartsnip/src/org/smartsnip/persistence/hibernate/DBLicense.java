/**
 * File: DBLicense.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

import org.hibernate.annotations.NaturalId;

/**
 * Database OR mapping class for table Tag
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
@Table(name = "License")
class DBLicense {
	@Id
	@GeneratedValue
	@Column(name = "license_id", insertable = false, updatable = false)
	private Long licenseId;

	@NaturalId
	@Column(name = "short_descr", length = 255)
	private String shortDescr;

	@Lob
	@Column(name = "license_text")
	private String licenseText;

	/**
	 * 
	 */
	DBLicense() {
	}

	// XXX remove constructor
//	/**
//	 * @param licenseId
//	 * @param shortDescr
//	 * @param licenseText
//	 */
//	DBLicense(Long licenseId, String shortDescr, String licenseText) {
//		super();
//		this.licenseId = licenseId;
//		this.shortDescr = shortDescr;
//		this.LicenseText = licenseText;
//	}

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
	 * @return the shortDescr
	 */
	public String getShortDescr() {
		return this.shortDescr;
	}

	/**
	 * @param shortDescr
	 *            the shortDescr to set
	 */
	public void setShortDescr(String shortDescr) {
		this.shortDescr = shortDescr;
	}

	/**
	 * @return the licenseText
	 */
	public String getLicenseText() {
		return this.licenseText;
	}

	/**
	 * @param licenseText
	 *            the licenseText to set
	 */
	public void setLicenseText(String licenseText) {
		this.licenseText = licenseText;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.licenseId == null) ? 0 : this.licenseId.hashCode());
		result = prime * result
				+ ((this.shortDescr == null) ? 0 : this.shortDescr.hashCode());
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
		DBLicense other = (DBLicense) obj;
		if (this.licenseId == null) {
			if (other.licenseId != null)
				return false;
		} else if (!this.licenseId.equals(other.licenseId))
			return false;
		if (this.shortDescr == null) {
			if (other.shortDescr != null)
				return false;
		} else if (!this.shortDescr.equals(other.shortDescr))
			return false;
		return true;
	}

}