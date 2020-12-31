package com.packtpub.a3ws.ch2.samples.crud;

import java.rmi.RemoteException;

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

public class CreateNodeWithContent {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {	
		AuthenticationUtils.startSession("admin", "admin");
		Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
		String name = "AWSBook "+System.currentTimeMillis();
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
			
			//build properties
			NamedValue[] properties = buildCustomProperties(name,name,description);
			
			//create operation
			CMLCreate create = new CMLCreate();
			create.setId("1");
			create.setParent(parent);
			create.setType(Constants.TYPE_CONTENT);
			create.setProperty(properties);
			
			//create the node reference
			Reference reference = new Reference();
			reference.setStore(spacesStore);
			reference.setPath("/app:company_home/cm:"+ISO9075.encode(name));
			
			//create the predicate
			Predicate predicate = new Predicate();
			predicate.setNodes(new Reference[]{reference});
			
			//set mime type and encoding for indexing
			ContentFormat format = new ContentFormat(mimeType, encoding);
			
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
	        
	        //perform a complete CML update for the node and the related file
	        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cml);
	        
	        System.out.println("Node created: "+result[0].getDestination().getPath());

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
