package com.packtpub.a3ws.ch3.samples.actions;

import java.rmi.RemoteException;

import org.alfresco.webservice.action.ActionItemDefinition;
import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.action.ParameterDefinition;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetActionDefinitions {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		
		try {

			ActionServiceSoapBindingStub actionService = WebServiceFactory.getActionService();
			ActionItemDefinition[] actionDefinitions = actionService.getActionDefinitions();
			for (ActionItemDefinition actionItemDefinition : actionDefinitions) {
				
				System.out.println(
						"-- Action: " + actionItemDefinition.getName() +
						" | Title: " + actionItemDefinition.getTitle() +
						" | Type: " + actionItemDefinition.getType()
				);
				ParameterDefinition[] parameters = actionItemDefinition.getParameterDefinition();
				if(parameters!=null) {
					System.out.println("---- Parameters ----");
					for (ParameterDefinition parameter : parameters) {
						System.out.println(
								"Name: " + parameter.getName() + 
								" | Type: " + parameter.getType()
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
