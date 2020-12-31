package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLUpdate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.model.BookshopModel;

public class AcceptReviewAction extends BaseAction {
	
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
			
			Reference reference = new Reference();
	        reference.setStore(SPACES_STORE);
	        reference.setUuid(idReview);
	        
	        Predicate predicate = new Predicate();
	        predicate.setNodes(new Reference[]{reference});
	        predicate.setStore(SPACES_STORE);
			
			//review accepted
	        NamedValue[] reviewProperties = new NamedValue[1];
	        reviewProperties[0] = new NamedValue();
	        reviewProperties[0].setName(BookshopModel.PROP_REVIEW_APPROVED);
	        reviewProperties[0].setValue("true");

	        CMLUpdate update = new CMLUpdate();
	        update.setProperty(reviewProperties);
	        update.setWhere(predicate);
	        
	        CML cml = new CML();
	        cml.setUpdate(new CMLUpdate[]{update});
	        
			//perform a CML update for the review
	        WebServiceFactory.getRepositoryService().update(cml);
	        
	        request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(REVIEW_MANAGED_OK_FORWARD);
		}else {
			return mapping.findForward(LOGIN_FORM_FORWARD);
		}
			
	}
	
}
