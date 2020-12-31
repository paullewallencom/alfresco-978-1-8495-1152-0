package com.packtpub.a3ws.samples.chemistry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class ReadContentStream {

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
	
	public static void main(String args[]) {
		String serverUrl = args[0];
		String username = args[1];
		String password = args[2];
		Session session = getSession(serverUrl, username, password);
        Folder root = session.getRootFolder();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_DOCUMENT.value());
        String name = "New Document (" + System.currentTimeMillis() + ").txt";
		properties.put(PropertyIds.NAME, name);
		List<Ace> addAces = new LinkedList<Ace>();
		List<Ace> removeAces = new LinkedList<Ace>();
		List<Policy> policies = new LinkedList<Policy>();
        String content = "The quick brown fox jumps over the lazy dog.";
		ContentStream contentStream = new ContentStreamImpl("text.txt", BigInteger.valueOf(content.length()),
				"text/plain", new ByteArrayInputStream(content.getBytes()));
		Document newDocument = root.createDocument(properties, contentStream, VersioningState.MAJOR, policies, addAces, removeAces, session.getDefaultContext());
		try {
			ContentStream cs = newDocument.getContentStream();
			System.out.println("File type = " + cs.getMimeType());
			System.out.println("File size = " + cs.getLength());
			System.out.println("File contents:");
			byte[] buf = new byte[1024];
			InputStream in = cs.getStream();
			in.read(buf);
			System.out.write(buf);
			System.out.println();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
