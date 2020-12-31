package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.vo.UserProfileFormBean;

public class ViewUserAccountAction extends BaseAction {
	
	private static final String USER_PROFILE_FORWARD = "userProfile";

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Boolean loggedIn = new Boolean(false);
		Boolean isAdmin = new Boolean(false);
		
		if(hasUserTicket(request)){
			
			loggedIn = new Boolean(true);
			
			if(ADMIN_USERNAME.equals(getUserDetails(request).getUserName()))
				isAdmin = new Boolean(true);
			
			AuthenticationDetails authenticationDetails = getUserDetails(request);
			AuthenticationUtils.setAuthenticationDetails(authenticationDetails);
			
			//get the Administration Service
			AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
			
			//get information about this current user
			UserDetails userDetails = null;
			try {
				userDetails = administrationService.getUser(authenticationDetails.getUserName());
			} catch (Exception e) {
				return mapping.findForward(ERROR_FORWARD);
			}
			
			NamedValue[] properties = userDetails.getProperties();
			
			UserProfileFormBean userProfile = new UserProfileFormBean();
			if(properties!=null){
				for (NamedValue userProperty : properties) {
					if(userProperty.getName().endsWith("firstName"))
						userProfile.setFirstName(userProperty.getValue());
					else if(userProperty.getName().endsWith("lastName"))
						userProfile.setLastName(userProperty.getValue());
					else if(userProperty.getName().endsWith("email"))
						userProfile.setEmail(userProperty.getValue());
					else if(userProperty.getName().endsWith("location"))
						userProfile.setLocation(userProperty.getValue());
					else if(userProperty.getName().endsWith("userName"))
						userProfile.setUsername(userProperty.getValue());
				}
			}

			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			request.setAttribute(UserProfileFormBean.BEAN_NAME, userProfile);			
			return mapping.findForward(USER_PROFILE_FORWARD);
		} else {
			return mapping.findForward(LOGIN_FORM_FORWARD);
		}
		
	}
	
}
