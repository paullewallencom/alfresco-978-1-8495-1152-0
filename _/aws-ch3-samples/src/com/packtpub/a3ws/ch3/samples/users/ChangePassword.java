package com.packtpub.a3ws.ch3.samples.users;

import java.rmi.RemoteException;

import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class ChangePassword {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		try {

			AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
			
			//change the default password for admin
			String username = "admin";
			String oldPassword = "admin";
			String newPassword = "secret";
			administrationService.changePassword(username, oldPassword, newPassword);
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
