package com.packtpub.a3ws.ch2.samples.crud;

import java.rmi.RemoteException;

import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;

public class DeleteNode {

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
		
		try {

			ParentReference parent = new ParentReference(
					spacesStore, 
					null,
					"/app:company_home",
					Constants.ASSOC_CONTAINS,
					"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+name);
			
			//build properties
			NamedValue[] properties = buildCustomProperties(name,name,description);
			
			//create operation
			CMLCreate create = new CMLCreate();
			create.setId("1");
			create.setParent(parent);
			create.setType(Constants.TYPE_CONTENT);
			create.setProperty(properties);
			
			//build the CML object
			CML cmlAdd = new CML();
			cmlAdd.setCreate(new CMLCreate[]{create});
	        
	        //perform a CML update to create the node
	        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cmlAdd);
	        

	        System.out.println("Node created: "+result[0].getDestination().getPath());
	        
	        //create a predicate
	        Reference reference = result[0].getDestination();
	        Predicate predicate = new Predicate(new Reference[]{reference}, spacesStore, null);
	        
	        //delete content
	        CMLDelete delete = new CMLDelete();
	        delete.setWhere(predicate);
	        
	        CML cmlRemove = new CML();
	        cmlRemove.setDelete(new CMLDelete[]{delete});
	        
	        //perform a CML update to create the node
	        WebServiceFactory.getRepositoryService().update(cmlRemove);
	        
	        System.out.println("Node removed: "+reference.getPath());

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
