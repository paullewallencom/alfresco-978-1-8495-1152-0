package com.packtpub.a3ws.ch2.samples.authentication;

import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.util.AuthenticationUtils;

public class Authentication {

	/**
	 * @param args
	 * @throws AuthenticationFault 
	 */
	public static void main(String[] args) throws AuthenticationFault {
		AuthenticationUtils.startSession("admin", "admin");
		try{
			
			// now you are authenticated in the repository
		
		} finally {
			AuthenticationUtils.endSession();
		}
		
	}

}
