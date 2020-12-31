package com.packtpub.a3ws.ch3.samples.actions;

import java.rmi.RemoteException;

import org.alfresco.webservice.action.ActionItemDefinition;
import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.action.ParameterDefinition;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetConditionDefinitions {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		
		try {

			ActionServiceSoapBindingStub actionService = WebServiceFactory.getActionService();
			ActionItemDefinition[] conditionDefinitions = actionService.getConditionDefinitions();
			for (ActionItemDefinition actionItemDefinition : conditionDefinitions) {
				
				System.out.println(
						"-- Condition: " + actionItemDefinition.getName() +
						" | Title: " + actionItemDefinition.getTitle() +
						" | Type: " + actionItemDefinition.getType()
				);
				ParameterDefinition[] parameters = actionItemDefinition.getParameterDefinition();
				if(parameters!=null) {
					System.out.println("---- Condition Parameters ----");
					for (ParameterDefinition parameter : parameters) {
						System.out.println(
								"Name: " + parameter.getName() + 
								" | Type: " + parameter.getType() + 
								" | Label: " + parameter.getDisplayLabel()
						);
					}
					System.out.println("---- /Parameters ----\n");
				}
			}
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
