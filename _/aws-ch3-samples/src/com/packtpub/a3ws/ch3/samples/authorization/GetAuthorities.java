package com.packtpub.a3ws.ch3.samples.authorization;

import java.rmi.RemoteException;

import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.accesscontrol.SiblingAuthorityFilter;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetAuthorities {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws AccessControlFault 
	 */
	public static void main(String[] args) throws AccessControlFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");	
		try {
			
	        AccessControlServiceSoapBindingStub accessControlService = WebServiceFactory.getAccessControlService();
			
	        String[] authorities = accessControlService.getAuthorities();
			for (String authority : authorities) {
				System.out.println("Authority: "+authority);
			}
			
			String[] childAuths = accessControlService.getChildAuthorities("GROUP_ALFRESCO_ADMINISTRATORS", new SiblingAuthorityFilter("USER", true));
			for (String string : childAuths) {
				System.out.println("Child Authorities: "+string);
			}
			
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}
	
}
