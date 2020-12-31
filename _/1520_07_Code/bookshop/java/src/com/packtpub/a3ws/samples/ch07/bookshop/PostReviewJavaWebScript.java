package com.packtpub.a3ws.samples.ch07.bookshop;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.alfresco.web.scripts.Cache;
import org.alfresco.web.scripts.DeclarativeWebScript;
import org.alfresco.web.scripts.Status;
import org.alfresco.web.scripts.WebScriptRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class PostReviewJavaWebScript extends DeclarativeWebScript {

	/** The Bookshop model's namespace URI */
	public static final String BOOKSHOP_NS_URI = "http://www.packtpub.com/a3ws/samples/bookshop";
	
	// Qualified names of types and properties used in the code
	public static final QName ASSOC_REVIEWS = QName.createQName(BOOKSHOP_NS_URI, "reviews");
	
	public static final QName TYPE_REVIEW = QName.createQName(BOOKSHOP_NS_URI, "review");
	
	public static final QName PROP_REVIEWER_NAME = QName.createQName(BOOKSHOP_NS_URI, "reviewer_name");
	
	public static final QName PROP_REVIEWER_EMAIL = QName.createQName(BOOKSHOP_NS_URI, "reviewer_email");
	
	public static final QName PROP_RATING = QName.createQName(BOOKSHOP_NS_URI, "rating");

	/** NodeService is used to create reviews */
	private NodeService nodeService;
	
	/** SearchService is used to search for books */
	private SearchService searchService;
	
	/** ContentService is used to set a review's content */
	private ContentService contentService;
	
	private static final StoreRef store = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
	
	@Override
protected Map<String, Object> executeImpl(WebScriptRequest req,
		Status status, Cache cache) {
	Map<String, Object> model = new HashMap<String, Object>();
	
	// Search for the book given the ISBN contained in the request URI
	String isbn = req.getServiceMatch().getTemplateVars().get("isbn");
	String query = "@bs\\:isbn:" + isbn;
	ResultSet results = searchService.query(store, SearchService.LANGUAGE_LUCENE, query);
	if (results.length() <= 0) {
		status.setCode(HttpServletResponse.SC_NOT_FOUND);
		status.setRedirect(true);
		return model;
	}
	// Check that the request body is a JSON document
	Object content = req.parseContent();
	if (!(content instanceof JSONObject)) {
		status.setCode(HttpServletResponse.SC_BAD_REQUEST);
		status.setRedirect(true);
		return model;
	}
	JSONObject json = (JSONObject) content;
	try {
		// Set a name for the association based on the review's title
		String title = json.getString("title");
		NodeRef book = results.getNodeRef(0);
		// Read the review's properties from the JSON input
		Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
		String id = String.valueOf(System.currentTimeMillis());
		QName assocQName = QName.createQName(BOOKSHOP_NS_URI, id);
		properties.put(ContentModel.PROP_NAME, id);
		properties.put(ContentModel.PROP_TITLE, title);
		properties.put(PROP_REVIEWER_NAME, json.getJSONObject("author").getString("name"));
		properties.put(PROP_REVIEWER_EMAIL, json.getJSONObject("author").getString("email"));
		properties.put(PROP_RATING, json.getInt("rating"));
		// Create the review as a child of the book
		ChildAssociationRef caref = nodeService.createNode(book, ASSOC_REVIEWS, assocQName, TYPE_REVIEW, properties);
		NodeRef child = caref.getChildRef();
		// Write the review's content
		ContentWriter writer = contentService.getWriter(child, ContentModel.PROP_CONTENT, true);
		writer.setEncoding("UTF-8");
		writer.setMimetype("text/plain");
		writer.putContent(json.getString("content"));
		// Redirect to the review's URI
		status.setCode(HttpServletResponse.SC_CREATED);
		status.setLocation(req.getServerPath() + req.getServiceContextPath() + "/books/" + isbn + "/reviews/" + id + ".json");
		status.setRedirect(true);
		return model;
	} catch (JSONException e) {
		status.setCode(HttpServletResponse.SC_BAD_REQUEST);
		status.setRedirect(true);
		status.setException(e);
		return model;
	}
}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}
}
