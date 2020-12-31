package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLRemoveChild;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.vo.ConfirmRemoveBookFormBean;

public class RemoveBookFromCartAction extends BaseAction {
	
	private static final String BOOK_REMOVED_OK_FORWARD = "bookRemoved";

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
			
			//get input fields from the HTML form
			ConfirmRemoveBookFormBean userPasswordForm = (ConfirmRemoveBookFormBean)form;
			AuthenticationDetails authenticationDetails = getUserDetails(request);
			String username = authenticationDetails.getUserName();
			
			//check if the Cart space exists in the User Home of the current user
			UserDetails userDetails = WebServiceFactory.getAdministrationService().getUser(username);
			
			NamedValue[] userProperties = userDetails.getProperties();
			String homeFolderNodeRef = null;
			for (NamedValue namedValue : userProperties) {
				if(Constants.PROP_USER_HOMEFOLDER.equals(namedValue.getName()))
					homeFolderNodeRef = namedValue.getValue();
			}
			
			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			Reference reference = new Reference(SPACES_STORE, homeFolderNodeRef.substring(24), null);
			QueryResult userHomeChildren = repositoryService.queryChildren(reference);
			ResultSet resultSetHomeFolder = userHomeChildren.getResultSet();
			ResultSetRow[] resultsUserHome = resultSetHomeFolder.getRows();
			String cartSpaceUuid = "";
			if(resultSetHomeFolder.getTotalRowCount()>0) {
				for (ResultSetRow resultSetRow : resultsUserHome) {
					//children of the user home folder
					if(Constants.TYPE_FOLDER.equals(resultSetRow.getNode().getType())){
						NamedValue[] properties = resultSetRow.getColumns();
						for (NamedValue namedValue : properties) {
							if(Constants.PROP_NAME.equals(namedValue.getName())){
								
								if("Cart".equals(namedValue.getValue())){
									cartSpaceUuid = resultSetRow.getNode().getId();
									break;
								}
								
							}
						}
					}
				}
			}
			
			//reference for the target space
	        ParentReference targetSpace = new ParentReference();
	        targetSpace.setStore(SPACES_STORE);
	        targetSpace.setUuid(cartSpaceUuid);
	        targetSpace.setAssociationType(Constants.ASSOC_CONTAINS);
	        
	        Reference bookReference = new Reference(SPACES_STORE, userPasswordForm.getBookId(), null);
	        Predicate nodeToRemove = new Predicate(new Reference[]{bookReference}, SPACES_STORE, null);
	        
	        //remove child
	        CMLRemoveChild removeBook = new CMLRemoveChild();
	        removeBook.setFrom(targetSpace);
	        removeBook.setWhere(nodeToRemove);
	        
	        CML cmlRemoveBook = new CML();
	        cmlRemoveBook.setRemoveChild(new CMLRemoveChild[]{removeBook});
	        
	        //update the current user
			try {
				
				//close the current user session
				AuthenticationUtils.endSession();
				
				//start the Admin session
				AuthenticationUtils.startSession(ADMIN_USERNAME, ADMIN_PASSWORD);
				
				//remove the book from the cart
		        WebServiceFactory.getRepositoryService().update(cmlRemoveBook);
			} catch (RuntimeException e) {
				return mapping.findForward(ERROR_FORWARD);
			} finally{
				AuthenticationUtils.endSession();
			}
			
			//restore the user session
			AuthenticationUtils.startSession(userDetails.getUserName(), userPasswordForm.getPassword());
			request.getSession().setAttribute(USER_TICKET, AuthenticationUtils.getAuthenticationDetails());
			
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(BOOK_REMOVED_OK_FORWARD);
		
		} else
			
			return mapping.findForward(LOGIN_FORM_FORWARD);
	}
	
}
