package com.packtpub.a3ws.ch3.samples.actions;

import java.rmi.RemoteException;

import org.alfresco.webservice.action.Action;
import org.alfresco.webservice.action.ActionExecutionResult;
import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLWriteContent;
import org.alfresco.webservice.types.ContentFormat;
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

public class ExecuteActions {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
		String name = "AWS Book "+System.currentTimeMillis();
		String description = "This is a content created with a sample of the book";
		String mimeType = "text/plain";
		String encoding = "UTF-8";
		
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
			create.setType(Constants.TYPE_CMOBJECT);
			create.setProperty(buildCMObjectProperties(name, name, description));
			
			//create the node reference
			Reference reference = new Reference();
			reference.setStore(spacesStore);
			reference.setPath("/app:company_home/cm:"+ISO9075.encode(name));
			
			//set mime type and encoding for indexing
			ContentFormat format = new ContentFormat(mimeType, encoding);
			
			//create the predicate
			Predicate predicate = new Predicate();
			predicate.setNodes(new Reference[]{reference});
			
			//write operation
			CMLWriteContent writeContent = new CMLWriteContent();
			writeContent.setFormat(format);
			writeContent.setWhere(predicate);
			writeContent.setProperty(Constants.PROP_CONTENT);
			writeContent.setContent("This is the content for the new node".getBytes());
			
			//build the CML object
			CML cml = new CML();
	        cml.setCreate(new CMLCreate[]{create});
	        cml.setWriteContent(new CMLWriteContent[]{writeContent});
	        
	        //perform a CML update to create the node
	        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cml);
	        System.out.println("Node created: "+result[0].getDestination().getPath());
			
			ActionServiceSoapBindingStub actionService = WebServiceFactory.getActionService();
			Action action = new Action();
			action.setActionName("specialise-type");
			NamedValue[] actionParameters = new NamedValue[]{new NamedValue("type-name", false, Constants.TYPE_CONTENT, null)};
			action.setParameters(actionParameters);
			
			ActionExecutionResult[] actionResults = actionService.executeActions(predicate, new Action[]{action});
			
			for (ActionExecutionResult actionExecutionResult : actionResults) {
				Action[] actionResult = actionExecutionResult.getActions();
				Action actionPerformed = actionResult[0];
				System.out.println("ActionName: " + actionPerformed.getActionName());
			}
			
			
		} finally {
			AuthenticationUtils.endSession();
		}

	}
	
	private static NamedValue[] buildCMObjectProperties(String name, String title, String description) {
		NamedValue[] properties = new NamedValue[1];
		properties[0] = Utils.createNamedValue(Constants.PROP_NAME, name);
		return properties;
	}

}
