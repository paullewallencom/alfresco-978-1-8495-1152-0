package com.packtpub.a3ws.ch3.samples.rules;

import java.rmi.RemoteException;

import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.action.RuleType;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetRuleTypes {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		try {

			ActionServiceSoapBindingStub actionService = WebServiceFactory.getActionService();
			RuleType[] ruleTypes = actionService.getRuleTypes();
			for (RuleType ruleType : ruleTypes) {
				System.out.println(
						"Name: " + ruleType.getName() + 
						" | Label: " + ruleType.getDisplayLabel()
				);
			}
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
