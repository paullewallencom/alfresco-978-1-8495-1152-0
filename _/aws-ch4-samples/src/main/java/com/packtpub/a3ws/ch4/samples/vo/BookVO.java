package com.packtpub.a3ws.ch4.samples.vo;

import java.util.ArrayList;
import java.util.List;

public class BookVO {

	private String id;
	private String title;
	private String description;
	private String isbn;
	private String author;
	private String publisher;
	private List<ReviewFormBean> reviews = new ArrayList<ReviewFormBean>();
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public List<ReviewFormBean> getReviews() {
		return reviews;
	}
	public void setReviews(List<ReviewFormBean> reviews) {
		this.reviews = reviews;
	} 
	
}
