package com.packtpub.a3ws.samples.chemistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.api.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class ListTypes {

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
        RepositoryInfo info = session.getRepositoryInfo();
        System.out.println("Types of " + info.getName());
        List<Tree<ObjectType>> trees = session.getTypeDescendants(null, -1, false);
        listTypes(trees, 1);
	}

	private static void listTypes(List<Tree<ObjectType>> trees, int level) {
        for (Tree<ObjectType> tree : trees) {
        	for (int i = 0 ; i < level ; ++i) {
        		System.out.print("\t");
        	}
        	System.out.println(tree.getItem().getId());
        	listTypes(tree.getChildren(), level + 1);
        }
	}
}
