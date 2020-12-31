package com.packtpub.a3ws.samples.chemistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.api.Ace;
import org.apache.chemistry.opencmis.commons.api.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

public class CreateBinaryDocument {

	private static Session getSession(String serverUrl, String username, String password) {
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

	public static void main(String args[]) throws FileNotFoundException {
		String serverUrl = args[0];
		String username = args[1];
		String password = args[2];
		Session session = getSession(serverUrl, username, password);
        Folder root = session.getRootFolder();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_DOCUMENT.value());
        String name = "New Image (" + System.currentTimeMillis() + ").jpg";
		properties.put(PropertyIds.NAME, name);
		List<Ace> addAces = new LinkedList<Ace>();
		List<Ace> removeAces = new LinkedList<Ace>();
		List<Policy> policies = new LinkedList<Policy>();
		File image = new File("image.jpg");
		ContentStream contentStream = new ContentStreamImpl("content.jpg", BigInteger.valueOf(image.length()),
				"image/jpeg", new FileInputStream(image));
		Document newDocument = root.createDocument(properties, contentStream, VersioningState.MAJOR, policies, addAces, removeAces, session.getDefaultContext());
		System.out.println(newDocument.getId());
	}
}
