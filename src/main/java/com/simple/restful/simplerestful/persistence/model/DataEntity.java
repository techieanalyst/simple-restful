package com.simple.restful.simplerestful.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "data")
public class DataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String user;

	@Column(name = "login_time")
	private Date loginTime;

	@Column(name = "attribute1")
	private String attributeOne;

	@Column(name = "attribute2")
	private String attributeTwo;

	@Column(name = "attribute3")
	private String attributeThree;

	@Column(name = "attribute4")
	private String attributeFour;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getAttributeOne() {
		return attributeOne;
	}

	public void setAttributeOne(String attributeOne) {
		this.attributeOne = attributeOne;
	}

	public String getAttributeTwo() {
		return attributeTwo;
	}

	public void setAttributeTwo(String attributeTwo) {
		this.attributeTwo = attributeTwo;
	}

	public String getAttributeThree() {
		return attributeThree;
	}

	public void setAttributeThree(String attributeThree) {
		this.attributeThree = attributeThree;
	}

	public String getAttributeFour() {
		return attributeFour;
	}

	public void setAttributeFour(String attributeFour) {
		this.attributeFour = attributeFour;
	}

}
