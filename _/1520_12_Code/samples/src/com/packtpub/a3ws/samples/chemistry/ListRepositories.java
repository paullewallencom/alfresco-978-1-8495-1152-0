package com.packtpub.a3ws.samples.chemistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class ListRepositories {

	public static void main(String args[]) {
		
		String serverUrl = args[0];
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		Map<String, String> params = new HashMap<String, String>();
        params.put(SessionParameter.USER, args[1]);
        params.put(SessionParameter.PASSWORD, args[2]);
        params.put(SessionParameter.ATOMPUB_URL, serverUrl);
        params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        List<Repository> repos = sessionFactory.getRepositories(params);
        for (Repository repo : repos) {
        	System.out.println(repo.getId());
        }
	}
}
