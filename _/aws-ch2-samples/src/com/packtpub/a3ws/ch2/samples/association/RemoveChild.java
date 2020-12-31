package com.packtpub.a3ws.ch2.samples.association;

import java.rmi.RemoteException;

import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddChild;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLRemoveChild;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;

public class RemoveChild {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
		String name = "AWS Book - Remove Child "+System.currentTimeMillis();
		String spaceNameForMove = "AWS Book - Remove Child Space Sample "+System.currentTimeMillis();
		String description = "This is a content created with a sample of the book";
		
		try {

			//parent for the new node
			ParentReference parentForNode = new ParentReference(
					spacesStore, 
					null,
					"/app:company_home",
					Constants.ASSOC_CONTAINS,
					"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+name);
			
			//parent for the new space
			ParentReference parentForSpace = new ParentReference(
					spacesStore, 
					null,
					"/app:company_home",
					Constants.ASSOC_CONTAINS,
					"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+spaceNameForMove);
			
			
			//build properties
			NamedValue[] properties = buildCustomProperties(name,name,description);
			NamedValue[] propertiesForSpace = buildCustomProperties(spaceNameForMove,spaceNameForMove,description);
			
			//create a node
			CMLCreate create = new CMLCreate();
			create.setId("1");
			create.setParent(parentForNode);
			create.setType(Constants.TYPE_CONTENT);
			create.setProperty(properties);
			
			//create a space
			CMLCreate createSpace = new CMLCreate();
			createSpace.setId("2");
			createSpace.setParent(parentForSpace);
			createSpace.setType(Constants.TYPE_FOLDER);
			createSpace.setProperty(propertiesForSpace);
			
			//build the CML object
			CML cmlAdd = new CML();
			cmlAdd.setCreate(new CMLCreate[]{create, createSpace});
	        
	        //perform a CML update to create nodes
	        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cmlAdd);
	        

	        System.out.println("Node created: "+result[0].getDestination().getPath());
	        System.out.println("Space created: "+result[1].getDestination().getPath());
	        
	        //create a predicate with the first CMLCreate result
	        Reference referenceForNode = result[0].getDestination();
	        Predicate sourcePredicate = new Predicate(new Reference[]{referenceForNode}, spacesStore, null);
	        
	        //create a reference from the second CMLCreate performed for space
	        Reference referenceForTargetSpace = result[1].getDestination();
	        
	        //reference for the target space
	        ParentReference targetSpace = new ParentReference();
	        targetSpace.setStore(spacesStore);
	        targetSpace.setPath(referenceForTargetSpace.getPath());
	        targetSpace.setAssociationType(Constants.ASSOC_CONTAINS);
	        targetSpace.setChildName(name);
	        
	        //add child
	        CMLAddChild addChild = new CMLAddChild();
	        addChild.setWhere(sourcePredicate);
	        addChild.setTo(targetSpace);
	        
	        CML cmlAddChild = new CML();
	        cmlAddChild.setAddChild(new CMLAddChild[]{addChild});
	        
	        //perform a CML update to add a child node
	        UpdateResult[] resultAddChild = WebServiceFactory.getRepositoryService().update(cmlAddChild);
	        
	        System.out.println("Node: "+referenceForNode.getPath()+" added to space: "+targetSpace.getPath());
	        
	        Reference refUpdate = resultAddChild[0].getDestination();
	        Predicate nodeToRemove = new Predicate(new Reference[]{refUpdate}, spacesStore, null);
	        
	        //remove child
	        CMLRemoveChild removeChild = new CMLRemoveChild();
	        removeChild.setFrom(targetSpace);
	        removeChild.setWhere(nodeToRemove);
	        
	        CML cmlRemoveChild = new CML();
	        cmlRemoveChild.setRemoveChild(new CMLRemoveChild[]{removeChild});
	        
	        //perform a CML update to remove the node
	        WebServiceFactory.getRepositoryService().update(cmlRemoveChild);
	        
	        System.out.println("Node: "+nodeToRemove.getNodes(0).getPath()+" removed from space: "+referenceForTargetSpace.getPath());

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
