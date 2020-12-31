package com.packtpub.a3ws.ch4.samples.vo;


public class ReviewFormBean extends PasswordFormBean {
	
	public static final String ATTRIBUTE_NAME = "reviewFormBean";
	private String reviewerName = "";
	private String reviewerEmail = "";
	private String rating = "";
	private String content = "";
	private String bookId = "";
	
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public String getReviewerEmail() {
		return reviewerEmail;
	}
	public void setReviewerEmail(String reviewerEmail) {
		this.reviewerEmail = reviewerEmail;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

}
