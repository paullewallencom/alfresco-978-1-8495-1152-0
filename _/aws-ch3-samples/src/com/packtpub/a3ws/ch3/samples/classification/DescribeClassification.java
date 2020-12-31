package com.packtpub.a3ws.ch3.samples.classification;

import java.rmi.RemoteException;

import org.alfresco.webservice.classification.ClassificationFault;
import org.alfresco.webservice.classification.ClassificationServiceSoapBindingStub;
import org.alfresco.webservice.types.AssociationDefinition;
import org.alfresco.webservice.types.ClassDefinition;
import org.alfresco.webservice.types.PropertyDefinition;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;

public class DescribeClassification {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws ClassificationFault 
	 */
	public static void main(String[] args) throws ClassificationFault, RemoteException {
		AuthenticationUtils.startSession("admin", "admin");
		try {
			
			ClassificationServiceSoapBindingStub classificationService = WebServiceFactory.getClassificationService();
			
			String classificationQName = "{http://www.alfresco.org/model/content/1.0}generalclassifiable";
			ClassDefinition classDefinition = classificationService.describeClassification(classificationQName);
			
			System.out.println("Title: "+ classDefinition.getTitle());
			System.out.println("Name: "+ classDefinition.getName());
			System.out.println("Description: "+ classDefinition.getDescription());
			System.out.println("SuperClass: "+ classDefinition.getSuperClass());
			
			//Properties
			PropertyDefinition[] properties = classDefinition.getProperties();
			System.out.println("--Properties--");
			for (PropertyDefinition propDefinition : properties) {
				System.out.println("Title: "+ propDefinition.getTitle());
				System.out.println("Name: "+ propDefinition.getName());
				System.out.println("Description: "+ propDefinition.getDescription());
				System.out.println("DataType: "+ propDefinition.getDataType());
				System.out.println("DefaultValue: "+ propDefinition.getDefaultValue());
			}
			System.out.println("--/Properties--");
			
			//Associations
			AssociationDefinition[] associations = classDefinition.getAssociations();
			if(associations!=null){
			System.out.println("--Associations--");
			for (AssociationDefinition assDefinition : associations) {
				System.out.println("Name: "+assDefinition.getName());
				System.out.println("Title: "+assDefinition.getTitle());
				System.out.println("Description: "+assDefinition.getDescription());
				System.out.println("SourceRole: "+assDefinition.getSourceRole());
				System.out.println("TargetRole: "+assDefinition.getTargetRole());
				System.out.println("TargetClass: "+assDefinition.getTargetClass());
			}
			System.out.println("--/Associations--");
			}
			
		} finally {
			AuthenticationUtils.endSession();
		}
	}

}
