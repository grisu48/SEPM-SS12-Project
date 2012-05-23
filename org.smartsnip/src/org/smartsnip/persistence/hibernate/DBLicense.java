/**
 * File: DBLicense.java
 * Date: 28.04.2012
 */
package org.smartsnip.persistence.hibernate;

import javax.persistence.*;

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

	@Column(name = "short_descr", length = 255)
	private String shortDescr;

	@Lob
	@Column(name = "license_text")
	private String LicenseText;

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
		return this.LicenseText;
	}

	/**
	 * @param licenseText
	 *            the licenseText to set
	 */
	public void setLicenseText(String licenseText) {
		this.LicenseText = licenseText;
	}

}