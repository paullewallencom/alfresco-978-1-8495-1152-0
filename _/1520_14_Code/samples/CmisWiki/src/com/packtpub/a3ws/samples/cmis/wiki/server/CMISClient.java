package com.packtpub.a3ws.samples.cmis.wiki.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.api.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

public class CMISClient {

	private static String serverUrl;
	private static String username;
	private static String password;
	
	private static CMISClient INSTANCE;
	
	private CMISClient() {
		serverUrl = "http://localhost:8080/alfresco/service/cmis";
		username = "admin";
		password = "admin";
	}
	
	public static synchronized CMISClient getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CMISClient();
		}
		return INSTANCE;
		
	}
	private static Session getSession() {
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		Map<String, String> params = new HashMap<String, String>();
        params.put(SessionParameter.USER, username);
        params.put(SessionParameter.PASSWORD, password);
        params.put(SessionParameter.ATOMPUB_URL, serverUrl);
        params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        List<Repository> repos = sessionFactory.getRepositories(params);
        if (repos.isEmpty()) {
        	throw new RuntimeException("Server has no repositories!");
        }
        return repos.get(0).createSession();
	}

	public Folder getWikiFolder() {
		try {
			return (Folder) getSession().getObjectByPath("/Wiki");
		} catch (CmisObjectNotFoundException e) {
			return null;
		}
	}

	public Folder getWikiSubFolder(String path) {
		try {
	        return (Folder) getSession().getObjectByPath("/Wiki" + path);
		} catch (CmisObjectNotFoundException e) {
			return null;
		}
	}

	public Folder getImagesFolder() {
		try {
			return (Folder) getSession().getObjectByPath("/Wiki/images");
		} catch (CmisObjectNotFoundException e) {
			return null;
		}
}
	
	public Document getChild(Folder parent, String name) {
		try {
			return (Document) getSession().
				getObjectByPath(parent.getPath() + "/" + name);
		} catch (CmisObjectNotFoundException e) {
			return null;
		}
	}

	public void writeCMISDocument(String name, String content) {
		Session session = getSession();
		Folder wikiFolder = getWikiFolder();
		if (wikiFolder == null) {
			throw new RuntimeException
			("Wiki folder missing. Create it under Company Home.");
		}
		Document page = getChild(wikiFolder, name);
		ContentStream contentStream = new ContentStreamImpl("content.txt", 
				BigInteger.valueOf(content.length()),
				"text/plain", new ByteArrayInputStream(content.getBytes()));
		if (page == null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "D:wiki:page");
			properties.put(PropertyIds.NAME, name);
			page = wikiFolder.createDocument(properties, contentStream,
					VersioningState.MAJOR, 
					null, null, null, session.getDefaultContext());
		} else {
			try {
				page.setContentStream(contentStream, true);
			} catch (Exception e) {
				throw new RuntimeException("While updating page: " + 
						e.getLocalizedMessage());
			}
		}
	}

	public String loadCMISDocument(String name) {
		Folder wikiFolder = getWikiFolder();
		if (wikiFolder == null) {
			throw new RuntimeException
			("Wiki folder missing. Create it under Company Home.");
		}
		Document page = getChild(wikiFolder, name);
		if (page == null) {
			return null;
		}
		try {
			ContentStream cs = page.getContentStream();
			InputStream in = cs.getStream();
			InputStreamReader reader = new InputStreamReader(in);
			StringWriter out = new StringWriter((int) cs.getLength());
			char[] buf = new char[1024];
			int n = 0;
			while ((n = reader.read(buf)) > 0) {
				out.write(buf, 0, n);
			}
			return new String(buf);
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
	
	public List<String> searchCMISDocuments(String query) {
		Session session = getSession();
		Folder wikiFolder = getWikiFolder();
		if (wikiFolder == null) {
			throw new RuntimeException
			("Wiki folder missing. Create it under Company Home.");
		}
		String sql = "SELECT * FROM wiki:page WHERE IN_TREE('" +
			wikiFolder.getId() + 
			"') AND CONTAINS('" + query + "')";
		ItemIterable<QueryResult> results = session.query(sql, false);
        List<String> pages = new ArrayList<String>((int) results.getTotalNumItems());
        for (QueryResult result : results) {
        	pages.add((String) result.getPropertyValueById(PropertyIds.NAME));
        }
		return pages;
	}

	public void writeImage(byte[] bytes, String contentType, String name) {
		Session session = getSession();
		Folder wikiFolder = getImagesFolder();
		if (wikiFolder == null) {
			throw new RuntimeException
			("Images folder missing. Create /Wiki/images under Company Home.");
		}
		ContentStream contentStream = new ContentStreamImpl(
				"content.jpg",
				BigInteger.valueOf(bytes.length),
				"image/jpeg",
				new ByteArrayInputStream(bytes));
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID,
				BaseTypeId.CMIS_DOCUMENT.value());
		properties.put(PropertyIds.NAME, name);
		wikiFolder.createDocument(properties, contentStream,
				VersioningState.MAJOR, 
				null, null, null, session.getDefaultContext());
	}

}
