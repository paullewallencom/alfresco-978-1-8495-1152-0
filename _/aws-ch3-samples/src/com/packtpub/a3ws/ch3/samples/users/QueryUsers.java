package com.packtpub.a3ws.ch3.samples.users;

import java.rmi.RemoteException;

import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.administration.UserFilter;
import org.alfresco.webservice.administration.UserQueryResults;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class QueryUsers {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		try {

			AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
			UserFilter filter = new UserFilter();
			filter.setUserName("ad.*");
			
			UserQueryResults userResults = administrationService.queryUsers(filter);
			UserDetails[] userDetailsList = userResults.getUserDetails();
			
			for (UserDetails userDetails : userDetailsList) {
				System.out.println("Username: " + userDetails.getUserName());
				NamedValue[] properties = userDetails.getProperties();
				if(properties!=null){
					System.out.println("---- Properties ----");
					for (NamedValue namedValue : properties) {
						System.out.println(
								"Name: " + namedValue.getName() +
								" | Value: " + namedValue.getValue());
					}
					System.out.println("---- Properties ----\n");
				}
			}
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
