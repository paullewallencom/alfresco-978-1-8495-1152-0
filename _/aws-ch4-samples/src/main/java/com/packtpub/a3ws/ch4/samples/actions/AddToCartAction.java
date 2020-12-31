package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddChild;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AddToCartAction extends BaseAction {
	
	private static final String BOOK_ID_PARAMETER = "id";
	private static final String ADD_BOOK_OK_FORWARD = "bookAdded";

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Boolean loggedIn = new Boolean(false);
		Boolean isAdmin = new Boolean(false);
		
		if(hasUserTicket(request)) {
		
			loggedIn = new Boolean(true);
			
			if(ADMIN_USERNAME.equals(getUserDetails(request).getUserName()))
				isAdmin = new Boolean(true);
			
			String bookUuid = request.getParameter(BOOK_ID_PARAMETER);
			
			//check if the Cart space exists in the User Home of the current user
			String username = AuthenticationUtils.getAuthenticationDetails().getUserName();
			AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
			UserDetails userDetails = administrationService.getUser(username);
			
			NamedValue[] userProperties = userDetails.getProperties();
			String homeFolderNodeRef = null;
			for (NamedValue namedValue : userProperties) {
				if(Constants.PROP_USER_HOMEFOLDER.equals(namedValue.getName()))
					homeFolderNodeRef = namedValue.getValue();
			}
			
			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			
			String luceneQuery = "PARENT:\""+homeFolderNodeRef+"\" AND @cm\\:name:\"Cart\"";
			Query query = new Query(Constants.QUERY_LANG_LUCENE, luceneQuery);
			
			QueryResult result = repositoryService.query(SPACES_STORE, query, false);
			String pathCart = null;
			String idCart = null;
			
			if(result.getResultSet().getTotalRowCount()==0){
				
				//you need to create the Cart space
				ParentReference parent = new ParentReference(
						SPACES_STORE, 
						homeFolderNodeRef.substring(24),
						null,
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}Cart");
				
				//build properties
				NamedValue[] properties = new NamedValue[1];
				properties[0] = new NamedValue();
				properties[0].setName(Constants.PROP_NAME);
				properties[0].setValue("Cart");
				
				//create operation
				CMLCreate create = new CMLCreate();
				create.setId("1");
				create.setParent(parent);
				create.setType(Constants.TYPE_FOLDER);
				create.setProperty(properties);
				
				//build the CML object
				CML cml = new CML();
		        cml.setCreate(new CMLCreate[]{create});
		        
		        //perform a CML update to create the Cart space
		        UpdateResult[] resultForCart = repositoryService.update(cml);
		        
		        pathCart = resultForCart[0].getDestination().getPath();
	
			} else {
				
				//the Cart space exists, we only need to add books
				ResultSet resultSet = result.getResultSet();
				ResultSetRow[] results = resultSet.getRows();
				idCart = results[0].getNode().getId();
	
			}
			
			//add a new book in the Cart space
			
			//create the Cart space
			ParentReference cartSpace = null;
			
			if(pathCart!=null){
				
				//if you want to use the path
				cartSpace = new ParentReference(
						SPACES_STORE, 
						null,
						pathCart,
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+bookUuid);
			
			}else {
			
				//if you want to use the UUID
				cartSpace = new ParentReference(
						SPACES_STORE, 
						idCart,
						null,
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+bookUuid);
			}
			
			Reference bookToAddRef = new Reference();
			bookToAddRef.setUuid(bookUuid);
			bookToAddRef.setStore(SPACES_STORE);
			
			Predicate bookToAdd = new Predicate();
			bookToAdd.setNodes(new Reference[]{bookToAddRef});
			
			CMLAddChild addBookToCart = new CMLAddChild();
			addBookToCart.setWhere(bookToAdd);
			addBookToCart.setTo(cartSpace);
			
			CML cmlAddChild = new CML();
	        cmlAddChild.setAddChild(new CMLAddChild[]{addBookToCart});
	        
	        //perform a CML update to add the node
	        repositoryService.update(cmlAddChild);
			
	        //inherit permission from the userHome space
			AccessControlServiceSoapBindingStub accessControlService = WebServiceFactory.getAccessControlService();
			Predicate cartPredicate = new Predicate(new Reference[]{cartSpace},SPACES_STORE ,null);
	        accessControlService.setInheritPermission(cartPredicate, true);
	        
	        request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(ADD_BOOK_OK_FORWARD);
		
		} else
			
			return mapping.findForward(LOGIN_FORM_FORWARD);
	}
	
}
