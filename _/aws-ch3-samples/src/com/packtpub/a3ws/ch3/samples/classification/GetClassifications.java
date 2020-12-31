package com.packtpub.a3ws.ch3.samples.classification;

import java.rmi.RemoteException;

import org.alfresco.webservice.classification.ClassificationFault;
import org.alfresco.webservice.classification.ClassificationServiceSoapBindingStub;
import org.alfresco.webservice.types.Classification;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

public class GetClassifications {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws ClassificationFault 
	 */
	public static void main(String[] args) throws ClassificationFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
		try {
			
			ClassificationServiceSoapBindingStub classificationService =
				WebServiceFactory.getClassificationService();
			
			Classification[] classifications = classificationService.getClassifications(spacesStore);
			
			System.out.println("--Classifications--");
			for (Classification classification : classifications) {
				System.out.println("Title: "+classification.getTitle());
				System.out.println("Description: "+classification.getDescription());
				System.out.println("Classification: "+classification.getClassification());
				System.out.println("Root category: "+classification.getRootCategory().getTitle());
			}
			System.out.println("--/Classifications--");
			
		} finally {
			AuthenticationUtils.endSession();
		}
	}

}
