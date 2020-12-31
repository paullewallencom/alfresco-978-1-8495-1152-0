package com.packtpub.a3ws.ch3.samples.users;

import java.rmi.RemoteException;

import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.NewUserDetails;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

public class DeleteUsers {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		try {

			AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
			NewUserDetails[] newUsers = new NewUserDetails[1];
			NewUserDetails userDetails = new NewUserDetails();
			userDetails.setUserName("plucidi");
			userDetails.setPassword("awsbook");

			NamedValue[] properties = new NamedValue[3];
			properties[0] = new NamedValue(Constants.PROP_USER_FIRSTNAME, false, "Piergiorgio", null);
			properties[1] = new NamedValue(Constants.PROP_USER_LASTNAME, false, "Lucidi", null);
			properties[2] = new NamedValue(Constants.PROP_USER_EMAIL, false, "p.lucidi@sourcesense.com", null);
			
			userDetails.setProperties(properties);
			newUsers[0] = userDetails;
			
			UserDetails[] responseUserDetails = administrationService.createUsers(newUsers);
			
			for (UserDetails userDetailsResponse : responseUserDetails) {
				System.out.println("User created: " + userDetailsResponse.getUserName());
				NamedValue[] userProperties = userDetailsResponse.getProperties();
				System.out.println("-- Properties --");
				for (NamedValue namedValue : userProperties) {
					System.out.println("Name: " + namedValue.getName() + " | " + "Value: " + namedValue.getValue());
				}
				System.out.println("-- /Properties --");
			}
			
			String[] usersToDelete = {"plucidi"};
			administrationService.deleteUsers(usersToDelete);
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
