package com.packtpub.a3ws.ch4.samples.vo;


public class ConfirmRemoveBookFormBean extends PasswordFormBean {

	public static final String ATTRIBUTE_NAME = "confirmRemoveBookFormBean";
	private String bookId;

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
	
}
