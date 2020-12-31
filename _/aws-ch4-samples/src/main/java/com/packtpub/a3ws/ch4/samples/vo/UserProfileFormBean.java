package com.packtpub.a3ws.ch4.samples.vo;


public class UserProfileFormBean extends LoginFormBean {
	
	public static final String BEAN_NAME = "userProfileFormBean";
	
	private String firstName;
	private String lastName;
	private String email;
	private String location;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
