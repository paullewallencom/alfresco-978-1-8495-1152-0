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

public class UpdateUsers {

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
			userDetails.setUserName("jdoe");
			userDetails.setPassword("yourpassword");

			//properties for the user
			NamedValue[] properties = new NamedValue[3];
			properties[0] = new NamedValue(Constants.PROP_USER_FIRSTNAME, false, "John", null);
			properties[1] = new NamedValue(Constants.PROP_USER_LASTNAME, false, "Doe", null);
			properties[2] = new NamedValue(Constants.PROP_USER_EMAIL, false, "john.doe@example.com", null);
			
			userDetails.setProperties(properties);
			newUsers[0] = userDetails;
			
			//create the user
			UserDetails[] responseUserDetails = administrationService.createUsers(newUsers);
			
			for (UserDetails userDetailsResponse : responseUserDetails) {
				System.out.println("User created: " + userDetailsResponse.getUserName());
				NamedValue[] userProperties = userDetailsResponse.getProperties();
				System.out.println("-- Properties --");
				for (NamedValue namedValue : userProperties) {
					System.out.println("Name: " + namedValue.getName() + " | Value: " + namedValue.getValue());
				}
				System.out.println("-- /Properties --\n");
			}
			
			//we want to update the existing user
			UserDetails[] updateUserList = new UserDetails[1];
			UserDetails updateUser = new UserDetails();
			updateUser.setUserName("jdoe");
			
			//update properties
			NamedValue[] updateProperties = new NamedValue[3];
			updateProperties[0] = new NamedValue(Constants.PROP_USER_FIRSTNAME, false, "Johann", null);
			updateProperties[1] = new NamedValue(Constants.PROP_USER_LASTNAME, false, "Doh", null);
			updateProperties[2] = new NamedValue(Constants.PROP_USER_EMAIL, false, "j.doh@example.com", null);
			
			updateUser.setProperties(updateProperties);
			updateUserList[0] = updateUser;
			
			//update the user
			UserDetails[] updateResponse = administrationService.updateUsers(updateUserList);
			
			for (UserDetails userDetailsResponse : updateResponse) {
				System.out.println("User updated: " + userDetailsResponse.getUserName());
				NamedValue[] userProperties = userDetailsResponse.getProperties();
				System.out.println("-- Properties --");
				for (NamedValue namedValue : userProperties) {
					System.out.println("Name: " + namedValue.getName() + " | " + "Value: " + namedValue.getValue());
				}
				System.out.println("-- /Properties --\n");
			}
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
