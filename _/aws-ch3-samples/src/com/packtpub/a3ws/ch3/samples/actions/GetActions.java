package com.packtpub.a3ws.ch3.samples.actions;

import java.rmi.RemoteException;

import org.alfresco.webservice.action.Action;
import org.alfresco.webservice.action.ActionFilter;
import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.action.Condition;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.ISO9075;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetActions {


	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "secret");
		Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
		String name = "AWS Book Folder "+System.currentTimeMillis();
		String description = "This is a content created with a sample of the book";
		
		try {

			ParentReference parent1 = new ParentReference(
					spacesStore, 
					null,
					"/app:company_home",
					Constants.ASSOC_CONTAINS,
					"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+name);
			
			//create operation
			CMLCreate create = new CMLCreate();
			create.setId("1");
			create.setParent(parent1);
			create.setType(Constants.TYPE_FOLDER);
			create.setProperty(buildCustomProperties(name, name, description));
			
			//create the node reference
			Reference reference = new Reference();
			reference.setStore(spacesStore);
			reference.setPath("/app:company_home/cm:"+ISO9075.encode(name));
			
			//create the predicate
			Predicate predicate = new Predicate();
			predicate.setNodes(new Reference[]{reference});
			
			//build the CML object
			CML cml = new CML();
	        cml.setCreate(new CMLCreate[]{create});
	        
	        //perform a CML update to create the node
	        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cml);
	        
	        System.out.println("Space created: "+result[0].getDestination().getPath());
	        
			ActionServiceSoapBindingStub actionService = WebServiceFactory.getActionService();
			
			//create action for the rule
			Action action = new Action();
			action.setActionName("add-features");
			
			//create conditions
			Condition[] conditions = new Condition[1];
			Condition condition = new Condition();
			
			//All items condition
			condition.setConditionName("no-condition");
			conditions[0] = condition;
			action.setConditions(conditions);
			
			//create the input parameter for this action
			NamedValue parameter = new NamedValue();
			parameter.setName("aspect-name");
			parameter.setValue(Constants.ASPECT_VERSIONABLE);
			NamedValue[] parameters = new NamedValue[1];
			parameters[0] = parameter;
			action.setParameters(parameters);
			
			//save the new action against the node
			Action[] actionToSave = new Action[]{action};
			actionService.saveActions(reference, actionToSave);
			
			//get the previous action saved against the node
			Action[] savedActions = actionService.getActions(reference, new ActionFilter());
			for (Action savedAction : savedActions) {
				System.out.println("ActionName:" +savedAction.getActionName());
				System.out.println("Description: "+savedAction.getDescription());
				System.out.println("Id: "+savedAction.getId());
				System.out.println("Title: "+savedAction.getTitle());
				Condition[] savedActionConditions =  savedAction.getConditions();
				for (Condition savedCondition : savedActionConditions) {
					System.out.println("-- Conditions --");
					System.out.println("Condition Name: "+savedCondition.getConditionName());
					NamedValue[] savedConditionParameters = savedCondition.getParameters();
					if(savedConditionParameters!=null){
						for (NamedValue savedConditionParameter : savedConditionParameters) {
							System.out.println("Parameter Name: "+savedConditionParameter.getName() + " | Value: "+savedConditionParameter.getValue());
						}
					}
					System.out.println("-- /Conditions --");
				}
				
				NamedValue[] savedParameters = savedAction.getParameters();
				if(savedParameters!=null){
					System.out.println("-- Parameters --");
					for (NamedValue savedParameter : savedParameters) {
						System.out.println("Parameter Name: "+savedParameter.getName() + " | Value: "+savedParameter.getValue());
					}
					System.out.println("-- /Parameters --");
				}
			}
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}
	
	private static NamedValue[] buildCustomProperties(String name, String title, String description) {
		NamedValue[] properties = new NamedValue[3];
		properties[0] = Utils.createNamedValue(Constants.PROP_NAME, name);
		properties[1] = Utils.createNamedValue(Constants.PROP_TITLE, title);
		properties[2] = Utils.createNamedValue(Constants.PROP_DESCRIPTION, description);
		return properties;
	}
	
}
