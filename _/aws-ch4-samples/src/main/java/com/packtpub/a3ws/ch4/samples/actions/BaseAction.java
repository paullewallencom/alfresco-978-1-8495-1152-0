package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;

import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.Constants;
import org.apache.struts.action.Action;

public class BaseAction extends Action {

	protected static final String IS_ADMIN_ATTRIBUTE = "isAdmin";
	protected static final String ADMIN_USERNAME = "admin";
	protected static final String ADMIN_PASSWORD = "admin";
	protected static final String ERROR_FORWARD = "errorPage";
	protected static final String USER_TICKET = "userTicket";
	protected static final String LOGIN_FORM_FORWARD = "loginForm";
	protected static final String LOGGED_IN_ATTRIBUTE = "loggedIn";
	protected static final Store SPACES_STORE = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
	
	protected AuthenticationDetails getUserDetails(HttpServletRequest request){
		return (AuthenticationDetails)request.getSession().getAttribute(USER_TICKET);
	}
	
	protected boolean hasUserTicket(HttpServletRequest request){
		if(request.getSession().getAttribute(USER_TICKET)!=null)
			return true;
		else
			return false;
	}
	
}
