package com.packtpub.a3ws.ch3.samples.authorization;

import java.rmi.RemoteException;

import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.accesscontrol.NewAuthority;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class AddChildAuthority {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws AccessControlFault 
	 */
	public static void main(String[] args) throws AccessControlFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");	
		try {
			
	        AccessControlServiceSoapBindingStub accessControlService = WebServiceFactory.getAccessControlService();
	        
	        NewAuthority newAuthority = new NewAuthority();
	        newAuthority.setAuthorityType("GROUP");
	        newAuthority.setName("AWSBOOK");
	        NewAuthority[] authorities = new NewAuthority[]{newAuthority};
			accessControlService.createAuthorities(null, authorities);
			
			String[] childAuthorities = {"admin"};
			accessControlService.addChildAuthorities("GROUP_AWSBOOK", childAuthorities);
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}
	
}
