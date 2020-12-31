package com.packtpub.a3ws.ch4.samples.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.model.BookshopModel;
import com.packtpub.a3ws.ch4.samples.vo.BookVO;

public class ViewUserCartAction extends BaseAction {
	
	private static final String CART_FORWARD = "cartPage";
	private static final String CART_ATTRIBUTE = "cart";

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
			
			//check if the Cart space exists in the User Home of the current user
			String username = AuthenticationUtils.getAuthenticationDetails().getUserName();
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
			String cartSpaceUuid = "";
			//list of value objects for the view
			List<BookVO> booksAddedToCartList = new ArrayList<BookVO>();
			if(resultSetHomeFolder.getTotalRowCount()>0) {
				ResultSetRow[] resultsUserHome = resultSetHomeFolder.getRows();
				for (ResultSetRow resultSetRow : resultsUserHome) {
					//children of the user home folder
					if(Constants.TYPE_FOLDER.equals(resultSetRow.getNode().getType())){
						NamedValue[] properties = resultSetRow.getColumns();
						for (NamedValue namedValue : properties) {
							if(Constants.PROP_NAME.equals(namedValue.getName())){
								
								if("Cart".equals(namedValue.getValue())){
									cartSpaceUuid = resultSetRow.getNode().getId();
								}
								
							}
						}
					}
				}
				
				if(cartSpaceUuid!="") {
					Reference cartReference = new Reference(SPACES_STORE,cartSpaceUuid, null);
					QueryResult cartChildren = repositoryService.queryChildren(cartReference);
					ResultSet resultSetCart = cartChildren.getResultSet();
					ResultSetRow[] booksAddedToCart = resultSetCart.getRows();

					if(booksAddedToCart!=null) {
						
						for (ResultSetRow bookAdded : booksAddedToCart) {
							BookVO book = new BookVO();
							book.setId(bookAdded.getNode().getId());
							NamedValue[] bookProperties = bookAdded.getColumns();
							
							//book properties
							for (NamedValue bookProperty : bookProperties) {
								if(Constants.PROP_TITLE.equals(bookProperty.getName()))
									book.setTitle(bookProperty.getValue());
								else if(Constants.PROP_DESCRIPTION.equals(bookProperty.getName()))
									book.setDescription(bookProperty.getValue());
								else if(BookshopModel.PROP_BOOK_AUTHOR.equals(bookProperty.getName())){
									
									//author is a multivalue property
									String[] authors = bookProperty.getValues();
									String authorsString = "";
									for (String author : authors) {
										authorsString += " | " + author;
									}
									
									book.setAuthor(authorsString);
								}
								else if(BookshopModel.PROP_BOOK_PUBLISHER.equals(bookProperty.getName()))
									book.setPublisher(bookProperty.getValue());
								else if(BookshopModel.PROP_BOOK_ISBN.equals(bookProperty.getName()))
									book.setIsbn(bookProperty.getValue());
			
							}
							
							booksAddedToCartList.add(book);
							
						}
						
					}
				
				}
			
			}
			
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
	        request.setAttribute(CART_ATTRIBUTE, booksAddedToCartList);
			return mapping.findForward(CART_FORWARD);
		
		}else
			
			return mapping.findForward(LOGIN_FORM_FORWARD);
	}
	
}
