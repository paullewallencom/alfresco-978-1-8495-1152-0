package com.packtpub.a3ws.ch3.samples.authorization;

import java.rmi.RemoteException;

import org.alfresco.webservice.accesscontrol.ACE;
import org.alfresco.webservice.accesscontrol.ACL;
import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.accesscontrol.AccessStatus;
import org.alfresco.webservice.accesscontrol.GetPermissionsResult;
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

public class GetPermissions {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws AccessControlFault 
	 */
	public static void main(String[] args) throws AccessControlFault, RemoteException {
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
			create.setType(Constants.TYPE_CONTENT);
			create.setProperty(buildCustomProperties(name, name, description));
			
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
	        
	        
	        AccessControlServiceSoapBindingStub accessControlService = WebServiceFactory.getAccessControlService();
	        //add ACE
			
			ACE ace = new ACE();
			ace.setAuthority("admin");
			ace.setPermission(Constants.READ);
			ace.setAccessStatus(AccessStatus.acepted);

			ACL[] acl = accessControlService.addACEs(predicate, new ACE[]{ace});
			
			for (ACL aclElement : acl) {
				ACE[] aces = aclElement.getAces();
				for (ACE aceElement : aces) {
					System.out.println(
						"addACEs | Authority: "+aceElement.getAuthority() +
						" | Permission: " +aceElement.getPermission());
				}	
			}
			
			//getPermissions
			GetPermissionsResult[] permissionsList = accessControlService.getPermissions(predicate);
			GetPermissionsResult permissions = permissionsList[0];
			String[] permissionsCurrent =  permissions.getPermissions();
			for (String permission : permissionsCurrent) {
				System.out.println("Permission: "+permission);
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
