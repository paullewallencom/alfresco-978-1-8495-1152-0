package com.packtpub.a3ws.ch3.samples.authorization;

import java.rmi.RemoteException;

import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.accesscontrol.GetClassPermissionsResult;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetClassPermissions {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws AccessControlFault 
	 */
	public static void main(String[] args) throws AccessControlFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");	
		try {
			
	        AccessControlServiceSoapBindingStub accessControlService = WebServiceFactory.getAccessControlService();
			
	        String[] consumerName = {"{http://www.alfresco.org/model/user/1.0}user"};
	        GetClassPermissionsResult[] classPermissions = accessControlService.getClassPermissions(consumerName);
			
	        for (GetClassPermissionsResult getClassPermissionsResult : classPermissions) {
				System.out.println("ClassName: "+getClassPermissionsResult.getClassName());
				String[] permissions = getClassPermissionsResult.getPermissions();
				for (String permission : permissions) {
					System.out.println("Permission: "+permission);
				}
			}
	        
		} finally {
			AuthenticationUtils.endSession();
		}

	}
	
}
