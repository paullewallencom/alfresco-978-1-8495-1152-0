package com.packtpub.a3ws.samples.cmis.wiki.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>WikiService</code>.
 */
public interface WikiServiceAsync {

	void loadPage(String name, AsyncCallback<CMISPage> callback)	throws IllegalArgumentException;
	
	void savePage(String name, String text, AsyncCallback<CMISPage> callback)	throws IllegalArgumentException;
	
	void search(String query, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
}
