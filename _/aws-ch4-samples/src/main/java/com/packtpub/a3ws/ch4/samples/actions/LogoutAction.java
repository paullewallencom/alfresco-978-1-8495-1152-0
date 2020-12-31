package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.util.AuthenticationDetails;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutAction extends BaseAction {
	
	private static final String LOGOUT_FORWARD = "home";
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		AuthenticationDetails userTicket = getUserDetails(request);
		AuthenticationUtils.setAuthenticationDetails(userTicket);
		
		AuthenticationUtils.endSession();
		
		request.getSession().removeAttribute(USER_TICKET);
		return mapping.findForward(LOGOUT_FORWARD);
		
	}
	
}
