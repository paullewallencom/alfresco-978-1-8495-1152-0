package com.packtpub.a3ws.ch3.samples.authorization;

import java.rmi.RemoteException;

import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.accesscontrol.AuthorityFilter;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetAllAuthorities {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws AccessControlFault 
	 */
	public static void main(String[] args) throws AccessControlFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");	
		try {
			
	        AccessControlServiceSoapBindingStub accessControlService = WebServiceFactory.getAccessControlService();
			AuthorityFilter filter = new AuthorityFilter();
			filter.setAuthorityType("USER");
			
	        String[] authorities = accessControlService.getAllAuthorities(filter);
			for (String authority : authorities) {
				System.out.println("Authority: "+authority);
			}
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}
	
}
