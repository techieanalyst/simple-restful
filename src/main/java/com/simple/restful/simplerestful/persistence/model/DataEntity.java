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
	
	public DataEntity(Integer id, String user, Date loginTime, String attributeOne, String attributeTwo,
			String attributeThree, String attributeFour) {
		super();
		this.id = id;
		this.user = user;
		this.loginTime = loginTime;
		this.attributeOne = attributeOne;
		this.attributeTwo = attributeTwo;
		this.attributeThree = attributeThree;
		this.attributeFour = attributeFour;
	}
	
	public DataEntity() {}

	public String getUser() {
		return user;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public String getAttributeOne() {
		return attributeOne;
	}

	public String getAttributeTwo() {
		return attributeTwo;
	}

	public String getAttributeThree() {
		return attributeThree;
	}

	public String getAttributeFour() {
		return attributeFour;
	}

}
