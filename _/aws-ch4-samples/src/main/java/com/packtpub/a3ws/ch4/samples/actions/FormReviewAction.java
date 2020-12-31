package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.vo.ReviewFormBean;

public class FormReviewAction extends BaseAction {
	
	private static final String BOOK_ID_PARAMETER = "id";
	private static final String REVIEW_FORM_FORWARD = "reviewForm";

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
			ReviewFormBean reviewForm = new ReviewFormBean();
			reviewForm.setBookId(bookUuid);
			String username = getUserDetails(request).getUserName();
			
			AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
			
			UserDetails userDetails = administrationService.getUser(username);
			NamedValue[] userProperties = userDetails.getProperties();
			String firstName = "";
			String lastName = "";
			String email = "";
			
			for (NamedValue userProperty : userProperties) {
				if(Constants.PROP_USER_FIRSTNAME.equals(userProperty.getName())){
					firstName = userProperty.getValue();
				} else if(Constants.PROP_USER_LASTNAME.equals(userProperty.getName())){
					lastName = userProperty.getValue();
				} else if(Constants.PROP_USER_EMAIL.equals(userProperty.getName())){
					email = userProperty.getValue();
				}
			}
			
			reviewForm.setReviewerName(firstName+" "+lastName);
			reviewForm.setReviewerEmail(email);
			reviewForm.setRating("0");
			reviewForm.setContent("");
			
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			request.setAttribute(ReviewFormBean.ATTRIBUTE_NAME, reviewForm);
			return mapping.findForward(REVIEW_FORM_FORWARD);
		
		} else
			
			return mapping.findForward(LOGIN_FORM_FORWARD);
	}
	
}
