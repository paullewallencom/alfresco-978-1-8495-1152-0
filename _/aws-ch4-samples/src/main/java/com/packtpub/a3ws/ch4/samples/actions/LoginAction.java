package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.vo.LoginFormBean;

public class LoginAction extends BaseAction {
	
	private static final String HOMEPAGE_FORWARD = "home";
	private static final String LOGIN_KO_FORWARD = "loginKo";
	private static final String LOGGED_IN_FORWARD = "loggedIn";
	
	private static final String USERNAME_ATTRIBUTE = "username";
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		if(hasUserTicket(request)){
			//if a user session exists we want to reuse it
			AuthenticationDetails authenticationDetails = getUserDetails(request);
			AuthenticationUtils.setAuthenticationDetails(authenticationDetails);
			
			request.setAttribute(USERNAME_ATTRIBUTE, authenticationDetails.getUserName());
			return mapping.findForward(LOGGED_IN_FORWARD);
			
		} else {
			
			//we create a new user session
			LoginFormBean loginForm = (LoginFormBean)form;
			try{
				AuthenticationUtils.startSession(loginForm.getUsername(), loginForm.getPassword());
				AuthenticationDetails userDetails = AuthenticationUtils.getAuthenticationDetails();
				
				//we want to save the user session
				request.getSession().setAttribute(USER_TICKET, userDetails);
				return mapping.findForward(HOMEPAGE_FORWARD);
				
			}catch(AuthenticationFault exception){
				return mapping.findForward(LOGIN_KO_FORWARD);
			}catch(RuntimeException exception){
				return mapping.findForward(LOGIN_KO_FORWARD);
			}
		}
		
	}
	
}
