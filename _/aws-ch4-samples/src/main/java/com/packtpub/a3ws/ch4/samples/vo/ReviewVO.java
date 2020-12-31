package com.packtpub.a3ws.ch4.samples.vo;

public class ReviewVO {

	private String id;
	private String reviewerName;
	private String rating;
	private boolean approved;
	private String contentUrl;
	private String reviewerEmail;
	
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReviewerEmail() {
		return reviewerEmail;
	}
	public void setReviewerEmail(String reviewerEmail) {
		this.reviewerEmail = reviewerEmail;
	}
	
}
