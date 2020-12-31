package com.packtpub.a3ws.samples.chemistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.api.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.api.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class ListServerCapabilities {

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
        RepositoryCapabilities capabilities = info.getCapabilities();
        System.out.println("CAPABILITIES of " + info.getName());
        System.out.println("ACL:                           " + capabilities.getAclCapability());
        System.out.println("Change:                        " + capabilities.getChangesCapability());
        System.out.println("Join:                          " + capabilities.getJoinCapability());
        System.out.println("Query:                         " + capabilities.getQueryCapability());
        System.out.println("Rendition:                     " + capabilities.getRenditionsCapability());
        System.out.println("GetDescendants:                " + capabilities.isGetDescendantsSupported());
        System.out.println("GetFolderTree:                 " + capabilities.isGetFolderTreeSupported());
        System.out.println("Multifiling:                   " + capabilities.isMultifilingSupported());
        System.out.println("Unfiling:                      " + capabilities.isUnfilingSupported());
        System.out.println("VersionSpecificFiling:         " + capabilities.isVersionSpecificFilingSupported());
        System.out.println("AllVersionsSearchable:         " + capabilities.isAllVersionsSearchableSupported());
        System.out.println("ContentStreamUpdatableAnytime: " + capabilities.getContentStreamUpdatesCapability());
        System.out.println("PWCSearchable:                 " + capabilities.isPwcSearchableSupported());
        System.out.println("PWCUpdatable:                  " + capabilities.isPwcUpdatableSupported());
	}
}
