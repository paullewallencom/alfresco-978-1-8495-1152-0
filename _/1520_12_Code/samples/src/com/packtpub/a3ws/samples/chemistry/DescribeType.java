package com.packtpub.a3ws.samples.chemistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.api.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;


public class DescribeType {

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
		ObjectType type = session.getTypeDefinition("D:bs:book");
        System.out.println("Type " + type.getId() + ":");
        System.out.println("Display name: " + type.getDisplayName());
        System.out.println("Description: " + type.getDescription());
        System.out.println("Query name: " + type.getQueryName());
        System.out.println("Properties:");
        Map<String, PropertyDefinition<?>> properties = type.getPropertyDefinitions();
        for (String propName : properties.keySet()) {
        	PropertyDefinition<?> property = properties.get(propName);
        	System.out.println("  " + property.getId() + 
        			" (" + property.getDisplayName() + "): " +
        			property.getPropertyType().name());
        }
	}
}
