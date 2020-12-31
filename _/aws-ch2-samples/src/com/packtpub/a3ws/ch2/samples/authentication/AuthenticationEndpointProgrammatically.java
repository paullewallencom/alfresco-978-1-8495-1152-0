package com.packtpub.a3ws.ch2.samples.authentication;

import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class AuthenticationEndpointProgrammatically {

	/**
	 * @param args
	 * @throws AuthenticationFault
	 */
	public static void main(String[] args) throws AuthenticationFault {
		
		//overriding default endpoint programmatically
		String endPointAddress = "http://localhost:8080/alfresco/api";
		WebServiceFactory.setEndpointAddress(endPointAddress);
		AuthenticationUtils.startSession("admin", "admin");
		try {

			// now you are authenticated in the repository

		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
