package com.packtpub.a3ws.ch4.samples.model;

import org.alfresco.webservice.util.Constants;

public interface BookshopModel {

	public static final String NAMESPACE_URI = "http://www.packtpub.com/a3ws/samples/bookshop";
	public static final String NAMESPACE_PREFIX = "bs";
	public static final String MODEL_NAME = "bookshopModel";
	
	public static final String UUID_LOCAL_ID = "node-uuid";
	public static final String ISBN_LOCAL_ID = "isbn";
	public static final String AUTHOR_LOCAL_ID = "author";
	public static final String PUBLISHER_LOCAL_ID = "publisher";
	
	public static final String REVIEW_TYPE_LOCAL_ID = "review";
	public static final String REVIEW_REVIEWER_NAME_LOCAL_ID = "reviewer_name";
	public static final String REVIEW_REVIEWER_EMAIL_LOCAL_ID = "reviewer_email";
	public static final String REVIEW_RATING_LOCAL_ID = "review_rating";
	public static final String REVIEW_APPROVED_LOCAL_ID = "approved";
	
	public static final String ASS_REVIEWS_LOCAL_ID = "reviews";
	
	public static final String PROP_BOOK_UUID = Constants.createQNameString(Constants.NAMESPACE_SYSTEM_MODEL, UUID_LOCAL_ID);
	public static final String PROP_BOOK_ISBN = Constants.createQNameString(NAMESPACE_URI, ISBN_LOCAL_ID);
	public static final String PROP_BOOK_AUTHOR = Constants.createQNameString(NAMESPACE_URI, AUTHOR_LOCAL_ID);
	public static final String PROP_BOOK_PUBLISHER = Constants.createQNameString(NAMESPACE_URI, PUBLISHER_LOCAL_ID);
	public static final String ASS_BOOK_REVIEWS = Constants.createQNameString(NAMESPACE_URI, ASS_REVIEWS_LOCAL_ID);
	
	public static final String REVIEW_TYPE = Constants.createQNameString(NAMESPACE_URI, REVIEW_TYPE_LOCAL_ID);
	public static final String PROP_REVIEW_REVIEWER_NAME = Constants.createQNameString(NAMESPACE_URI, REVIEW_REVIEWER_NAME_LOCAL_ID);
	public static final String PROP_REVIEW_REVIEWER_EMAIL = Constants.createQNameString(NAMESPACE_URI, REVIEW_REVIEWER_EMAIL_LOCAL_ID);
	public static final String PROP_REVIEW_RATING = Constants.createQNameString(NAMESPACE_URI, REVIEW_RATING_LOCAL_ID);
	public static final String PROP_REVIEW_APPROVED = Constants.createQNameString(NAMESPACE_URI, REVIEW_APPROVED_LOCAL_ID);
	
	
	
}
