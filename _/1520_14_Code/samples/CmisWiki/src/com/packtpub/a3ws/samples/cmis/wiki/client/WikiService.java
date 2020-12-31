package com.packtpub.a3ws.samples.cmis.wiki.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("wiki")
public interface WikiService extends RemoteService {

	CMISPage loadPage(String name) throws IllegalArgumentException;

	CMISPage savePage(String name, String text) throws IllegalArgumentException;
	
	List<String> search(String query) throws IllegalArgumentException;
}
