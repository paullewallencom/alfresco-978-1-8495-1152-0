package com.packtpub.a3ws.ch2.samples.crud;

import java.rmi.RemoteException;

import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddAspect;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLUpdate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.types.Version;
import org.alfresco.webservice.types.VersionHistory;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;

public class NodeVersioning {

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
			NamedValue[] propertiesForSpace = buildCustomProperties(spaceNameForMove,spaceNameForMove, description);
			
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
	        
	        name = "AWS Book - Changed by CMLUpdate "+System.currentTimeMillis();
	        
	        //add versionable aspect to the node
	        CMLAddAspect aspect = new CMLAddAspect();
	        aspect.setAspect(Constants.ASPECT_VERSIONABLE);
	        aspect.setWhere(sourcePredicate);
	        
	        //update node
	        CMLUpdate update = new CMLUpdate();
	        update.setProperty(buildCustomProperties(name,name,"Changed by CMLUpdate "+description));
	        update.setWhere(sourcePredicate);
	        
	        CML cmlUpdate = new CML();
	        cmlUpdate.setAddAspect(new CMLAddAspect[]{aspect});
	        cmlUpdate.setUpdate(new CMLUpdate[]{update});
	        
	        //perform a CML update
	        WebServiceFactory.getRepositoryService().update(cmlUpdate);
	        
	        //update node version 1.1
	        CMLUpdate update11 = new CMLUpdate();
	        update11.setProperty(buildCustomProperties("1.1 "+name,"1.1 "+name,"Changed by CMLUpdate "+description));
	        update11.setWhere(sourcePredicate);
	        
	        CML cmlUpdate11 = new CML();
	        cmlUpdate11.setUpdate(new CMLUpdate[]{update11});
	        
	        //perform a CML update
	        WebServiceFactory.getRepositoryService().update(cmlUpdate11);
	        
	        System.out.println("Node: "+sourcePredicate.getNodes(0).getPath()+" updated to version 1.1");
	        
	        //node history: the 1.0 version
	        VersionHistory nodeHistory = WebServiceFactory.getAuthoringService().getVersionHistory(sourcePredicate.getNodes(0));
	        for (int i = 0; i < nodeHistory.getVersions().length; i++) {
				Version version = nodeHistory.getVersions(i);
				 System.out.println("version id:" +version.getId().getUuid());
				 System.out.println("version label:" +version.getLabel());
				 System.out.println("version created: "+version.getCreated().getTime());
				 System.out.println("version creator: "+version.getCreator());
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
