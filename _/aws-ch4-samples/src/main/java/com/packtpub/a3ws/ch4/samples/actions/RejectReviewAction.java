package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.CMLRemoveChild;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RejectReviewAction extends BaseAction {
	
	private static final String REVIEW_MANAGED_OK_FORWARD = "managedOk";
	private static final String REVIEW_ID_PARAMETER = "idReview";

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Boolean loggedIn = new Boolean(false);
		Boolean isAdmin = new Boolean(false);
		
		if(hasUserTicket(request)
				&& ADMIN_USERNAME.equals(getUserDetails(request).getUserName())){
			
			loggedIn = new Boolean(true);
			isAdmin = new Boolean(true);
			
			String idReview = request.getParameter(REVIEW_ID_PARAMETER);
			
			Reference reviewRef = new Reference();
	        reviewRef.setStore(SPACES_STORE);
	        reviewRef.setUuid(idReview);
			
	        RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
	        
			QueryResult queryResult = repositoryService.queryParents(reviewRef);
			ResultSet resultSet = queryResult.getResultSet();
			Reference book = new Reference();
			book.setStore(SPACES_STORE);
			if(resultSet.getTotalRowCount()>0) {
				ResultSetRow[] parents = resultSet.getRows();
				for (ResultSetRow parent : parents) {
					NamedValue[] parentProperties = parent.getColumns();
					for (NamedValue parentProperty : parentProperties) {
						if(Constants.PROP_NAME.equals(parentProperty.getName())){
							if(parentProperty.getValue().endsWith(".pdf")){
								book.setUuid(parent.getNode().getId());
								break;
							}
						}
					}
				}
			}
			
			Predicate review = new Predicate();
	        review.setNodes(new Reference[]{reviewRef});
	        review.setStore(SPACES_STORE);
			
			//remove child to address this review from the book
	        CMLRemoveChild removeReviewFromBook = new CMLRemoveChild();
	        removeReviewFromBook.setFrom(book);
	        removeReviewFromBook.setWhere(review);
	        
	        //remove an instance of this association from the reviews association
	        CML cmlRemoveChild = new CML();
	        cmlRemoveChild.setRemoveChild(new CMLRemoveChild[]{removeReviewFromBook});
	        repositoryService.update(cmlRemoveChild);
	        
	        //delete the rejected review from the Reviews space of the user
	        CMLDelete deleteReview = new CMLDelete();
	        deleteReview.setWhere(review);
	        
	        CML cmlDelete = new CML();
	        cmlDelete.setDelete(new CMLDelete[]{deleteReview});
	        repositoryService.update(cmlDelete);
			
	        request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(REVIEW_MANAGED_OK_FORWARD);
		}else {
			return mapping.findForward(LOGIN_FORM_FORWARD);
		}
			
	}
	
}
