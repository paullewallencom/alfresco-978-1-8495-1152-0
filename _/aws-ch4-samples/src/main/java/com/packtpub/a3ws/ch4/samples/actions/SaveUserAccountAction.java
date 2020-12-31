package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.vo.UserProfileFormBean;

public class SaveUserAccountAction extends BaseAction {

	private static final String PROFILE_SAVED_OK_FORWARD = "profileSavedOk";
	
	private static final String PROP_LOCATION = Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, "location");
	
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
			
			//get input fields from the HTML form
			UserProfileFormBean userProfile = (UserProfileFormBean)form;
			
			AuthenticationDetails authenticationDetails = getUserDetails(request);
			String username = authenticationDetails.getUserName();
			
			//authenticate as Admin
			try {
				//close the current user session
				AuthenticationUtils.endSession();
				
				//check the user credentials
				AuthenticationUtils.startSession(ADMIN_USERNAME, ADMIN_PASSWORD);
				
			} catch (RuntimeException e) {
				return mapping.findForward(ERROR_FORWARD);
			}
			
			//we want to update the existing user
			UserDetails[] updateUserList = new UserDetails[1];
			UserDetails updateUser = new UserDetails();
			updateUser.setUserName(username);
			
			//update properties
			NamedValue[] updateProperties = new NamedValue[4];
			
			updateProperties[0] = new NamedValue();
			updateProperties[0].setName(Constants.PROP_USER_FIRSTNAME);
			updateProperties[0].setValue(userProfile.getFirstName());
			
			updateProperties[1] = new NamedValue();
			updateProperties[1].setName(Constants.PROP_USER_LASTNAME);
			updateProperties[1].setValue(userProfile.getLastName());
			
			updateProperties[2] = new NamedValue();
			updateProperties[2].setName(Constants.PROP_USER_EMAIL);
			updateProperties[2].setValue(userProfile.getEmail());
			
			updateProperties[3] = new NamedValue();
			updateProperties[3].setName(PROP_LOCATION);
			updateProperties[3].setValue(userProfile.getLocation());
			
			updateUser.setProperties(updateProperties);
			updateUserList[0] = updateUser;
			
			//restore the current user session
			try {
				//get Administration Service
				AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
				
				//perform the user update
				administrationService.updateUsers(updateUserList);
				
			} catch (RuntimeException e) {
				return mapping.findForward(ERROR_FORWARD);
			} finally{
				AuthenticationUtils.endSession();
			}
			
			//setup user session
			AuthenticationUtils.startSession(username, userProfile.getPassword());
			request.getSession().setAttribute(USER_TICKET, AuthenticationUtils.getAuthenticationDetails());
			request.setAttribute(UserProfileFormBean.BEAN_NAME, userProfile);			
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(PROFILE_SAVED_OK_FORWARD);
			
		} else {
			return mapping.findForward(LOGIN_FORM_FORWARD);
		}
		
	}
	
}
