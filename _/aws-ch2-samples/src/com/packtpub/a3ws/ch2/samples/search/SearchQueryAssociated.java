package com.packtpub.a3ws.ch2.samples.search;

import java.rmi.RemoteException;

import org.alfresco.webservice.repository.Association;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

public class SearchQueryAssociated {

	/**
	 * To execute this sample you have to add an image in the repository.
	 * Then you have to set an avatar image for the default user of Alfresco.
	 * @param args
	 * @throws RemoteException
	 * @throws RepositoryFault
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		try {

			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
			
			String associationType = "{http://www.alfresco.org/model/content/1.0}avatar";
			String direction = "target";
			Association association = new Association(associationType, direction);

			Reference reference = new Reference();
			reference.setStore(spacesStore);
			reference.setPath("/sys:system/sys:people/cm:admin");
			
			QueryResult queryResult = 
			repositoryService.queryAssociated(reference, association);

			ResultSetRow[] targetNodes = queryResult.getResultSet().getRows();
			
			//retrieve results of all the target nodes
			for (ResultSetRow targetNode : targetNodes) {
				ResultSetRowNode targetNodeResult = targetNode.getNode();
				
				System.out.println("---- Result ----");
				
				//getting the target node
				System.out.println("-- Target --");
				
				System.out.println("Node ID: "+targetNodeResult.getId());
				System.out.println("Node Type: "+targetNodeResult.getType());
				
				//retrieve properties of the target node
				for (NamedValue property : targetNode.getColumns()) {
					if (Constants.PROP_NAME.equals(property.getName())) {
						System.out.println("Name: "+property.getValue());
					} else if (Constants.PROP_DESCRIPTION.equals(property.getName())) {
						System.out.println("Description: "+property.getValue());
					} 
				}
				
				System.out.println("-- /Target --");
				
				//getting the source node
				System.out.println("-- Source --");
				
				String directionSource = "source";
				Association associationSource = new Association(associationType, directionSource);

				Reference referenceSource = new Reference();
				referenceSource.setStore(spacesStore);
				referenceSource.setUuid(targetNode.getNode().getId());
				
				QueryResult queryResultSource = 
				repositoryService.queryAssociated(referenceSource, associationSource);
				
				//retrieve results of all the source nodes
				ResultSetRow[] sourceNodes = queryResultSource.getResultSet().getRows();
				for (ResultSetRow sourceNode : sourceNodes) {
					ResultSetRowNode sourceNodeResult = sourceNode.getNode();
					
					System.out.println("Node ID: "+sourceNodeResult.getId());
					System.out.println("Node Type: "+sourceNodeResult.getType());
					
					//retrieve properties of the source node
					for (NamedValue property : sourceNode.getColumns()) {
						if (Constants.PROP_NAME.equals(property.getName())) {
							System.out.println("Name: "+property.getValue());
						} else if (Constants.PROP_DESCRIPTION.equals(property.getName())) {
							System.out.println("Description: "+property.getValue());
						} 
					}
				}
				
				
				System.out.println("-- /Source --");
				System.out.println("---- /Result ----");
				
			}

		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
