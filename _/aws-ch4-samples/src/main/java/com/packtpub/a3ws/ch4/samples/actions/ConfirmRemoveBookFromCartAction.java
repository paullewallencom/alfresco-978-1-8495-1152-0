package com.packtpub.a3ws.ch4.samples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.vo.ConfirmRemoveBookFormBean;

public class ConfirmRemoveBookFromCartAction extends BaseAction {
	
	private static final String BOOK_ID_PARAMETER = "id";
	private static final String PASSWORD_FORM_FORWARD = "passwordForm";

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
			
			String bookId = request.getParameter(BOOK_ID_PARAMETER);
			ConfirmRemoveBookFormBean passwordForm = new ConfirmRemoveBookFormBean();
			passwordForm.setBookId(bookId);
			request.setAttribute(ConfirmRemoveBookFormBean.ATTRIBUTE_NAME, passwordForm);
			
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(PASSWORD_FORM_FORWARD);
		
		} else
			
			return mapping.findForward(LOGIN_FORM_FORWARD);
	}
	
}
