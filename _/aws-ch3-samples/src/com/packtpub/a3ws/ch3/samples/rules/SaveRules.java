package com.packtpub.a3ws.ch3.samples.rules;

import java.rmi.RemoteException;

import org.alfresco.webservice.action.Action;
import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.action.Condition;
import org.alfresco.webservice.action.Rule;
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

public class SaveRules {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
		String name = "AWS Book Folder "+System.currentTimeMillis();
		String description = "This is a content created with a sample of the book";
		
		try {

			ParentReference parent = new ParentReference(
					spacesStore, 
					null,
					"/app:company_home",
					Constants.ASSOC_CONTAINS,
					"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+name);
			
			
			//create operation
			CMLCreate create = new CMLCreate();
			create.setId("1");
			create.setParent(parent);
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
			
			//create the versionable rule
			Rule[] rules = new Rule[1];
			Rule rule = new Rule();
			rule.setAction(action);
			rule.setTitle("Add versionable aspect");
			rule.setDescription("Add the versionable aspect to all the items in the space");
			rule.setExecuteAsynchronously(true);
			rule.setRuleTypes(new String[]{"inbound"});
			
			//add this rule to the new space
			rules[0] = rule;
			Rule[] ruleResultList = actionService.saveRules(reference, rules);
			
			//read result
			for (Rule ruleResult : ruleResultList) {
				System.out.println(
						"Rule created: " + ruleResult.getTitle() + 
						" | Action: " + ruleResult.getAction().getActionName() +
						" | Type: " + ruleResult.getRuleTypes()[0] + 
						" | Space: " + ruleResult.getOwningReference().getPath()
				);
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
