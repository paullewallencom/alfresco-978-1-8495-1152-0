package com.packtpub.a3ws.ch2.samples.search;

import java.rmi.RemoteException;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

public class SearchQueryParents {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	public static void main(String[] args) throws RepositoryFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		try {

			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
			Reference reference = new Reference(spacesStore, null, "/app:company_home/app:dictionary");
			QueryResult queryResult = repositoryService.queryParents(reference);
			ResultSet resultSet = queryResult.getResultSet();
			ResultSetRow[] results = resultSet.getRows();
			
			//retrieve results from the resultSet
			for (ResultSetRow resultRow : results) {
				ResultSetRowNode nodeResult = resultRow.getNode();
				
				System.out.println("---- Result ----");
				System.out.println("Node ID: "+nodeResult.getId());
				System.out.println("Node Type: "+nodeResult.getType());
				
				//retrieve properties from the current node
				for (NamedValue namedValue : resultRow.getColumns()) {
					if (Constants.PROP_NAME.equals(namedValue.getName())) {
						System.out.println("Name: "+namedValue.getValue());
					} else if (Constants.PROP_DESCRIPTION.equals(namedValue.getName())) {
						System.out.println("Description: "+namedValue.getValue());
					} 
				}
				System.out.println("---- /Result ----");
			}

		} finally {
			AuthenticationUtils.endSession();
		}

	}

}
