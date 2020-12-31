package com.packtpub.a3ws.ch4.samples.actions;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.axis.utils.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.model.BookshopModel;
import com.packtpub.a3ws.ch4.samples.vo.BookVO;
import com.packtpub.a3ws.ch4.samples.vo.ReviewVO;

public class ViewBookDetailsAction extends BaseAction {
	
	private static final String BOOK_ID_PARAMETER = "id";
	private static final String BOOK_DETAILS_FORWARD = "bookDetails";
	private static final String BOOK_DETAILS_ATTRIBUTE = "book";
	private static final String IS_IN_CART_ATTRIBUTE = "isInCart";
	private static final String REVIEWS_LIST = "reviews";
	

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Boolean loggedIn = new Boolean(false);
		Boolean isInCart = new Boolean(false);
		Boolean isAdmin = new Boolean(false);
		
		if(hasUserTicket(request)){
			loggedIn = new Boolean(true);
			
			if(ADMIN_USERNAME.equals(getUserDetails(request).getUserName()))
				isAdmin = new Boolean(true);
			
			String uuid = request.getParameter(BOOK_ID_PARAMETER);
			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
			Reference bookReference = new Reference(spacesStore,uuid,null);
			Predicate predicateForThisBook = new Predicate(new Reference[]{bookReference},spacesStore,null);
			
			Node[] bookNode = repositoryService.get(predicateForThisBook);
			NamedValue[] propertiesOfTheBook = bookNode[0].getProperties();
			BookVO book = new BookVO();
			for (NamedValue bookProperty : propertiesOfTheBook) {
				if(Constants.PROP_TITLE.equals(bookProperty.getName()))
					book.setTitle(bookProperty.getValue());
				else if(Constants.PROP_DESCRIPTION.equals(bookProperty.getName()))
					book.setDescription(bookProperty.getValue());
				else if(BookshopModel.PROP_BOOK_UUID.equals(bookProperty.getName()))
					book.setId(bookProperty.getValue());
				else if(BookshopModel.PROP_BOOK_AUTHOR.equals(bookProperty.getName())){
					
					//author is a multivalue property
					String[] authors = bookProperty.getValues();
					String authorsString = "";
					for (String author : authors) {
							authorsString += author;
							if(authors.length>1)
								authorsString+=" | ";
					}
					
					book.setAuthor(authorsString);
				}
				else if(BookshopModel.PROP_BOOK_PUBLISHER.equals(bookProperty.getName()))
					book.setPublisher(bookProperty.getValue());
				else if(BookshopModel.PROP_BOOK_ISBN.equals(bookProperty.getName()))
					book.setIsbn(bookProperty.getValue());
			}
			
			
			//check if the Cart space exists in the User Home of the current user
			String username = AuthenticationUtils.getAuthenticationDetails().getUserName();
			AdministrationServiceSoapBindingStub administrationService = WebServiceFactory.getAdministrationService();
			UserDetails userDetails = administrationService.getUser(username);
			NamedValue[] userProperties = userDetails.getProperties();
			String homeFolderNodeRef = null;
			for (NamedValue namedValue : userProperties) {
				if(Constants.PROP_USER_HOMEFOLDER.equals(namedValue.getName()))
					homeFolderNodeRef = namedValue.getValue();
			}
			
			Reference userHomeRef = new Reference(spacesStore, homeFolderNodeRef.substring(24), null);
			QueryResult userHomeChildren = repositoryService.queryChildren(userHomeRef);
			ResultSet resultSetHomeFolder = userHomeChildren.getResultSet();
			String cartSpaceUuid = null;
			
			if(resultSetHomeFolder.getTotalRowCount()>0 
					&& !ADMIN_USERNAME.equals(username)) {
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
				
				if(!StringUtils.isEmpty(cartSpaceUuid)){
					Reference cartReference = new Reference(spacesStore,cartSpaceUuid, null);
					QueryResult cartChildren = repositoryService.queryChildren(cartReference);
					ResultSet resultSetCart = cartChildren.getResultSet();
					if(resultSetCart.getTotalRowCount()>0){
						ResultSetRow[] booksInCart = resultSetCart.getRows();
						for (ResultSetRow bookInCart : booksInCart) {
							NamedValue[] bookProperties = bookInCart.getColumns();
							for (NamedValue bookProperty : bookProperties) {
								if(Constants.PROP_TITLE.equals(bookProperty.getName())){
									
									if(book.getTitle().equals(bookProperty.getValue())) {
											isInCart = new Boolean(true);
									}
								}
							}
						}
					}
				}
				
			}
			
			List<ReviewVO> reviews = new ArrayList<ReviewVO>();
			ParentReference bookReferenceParent = new ParentReference();
			bookReferenceParent.setAssociationType(BookshopModel.ASS_BOOK_REVIEWS);
			bookReferenceParent.setUuid(bookReference.getUuid());
			bookReferenceParent.setStore(SPACES_STORE);
			
			QueryResult queryResultAssociated = repositoryService.queryChildren(bookReferenceParent);
			ResultSet resultSetAss = queryResultAssociated.getResultSet();
			ResultSetRow[] resultsAss = resultSetAss.getRows();
			ContentServiceSoapBindingStub contentService = WebServiceFactory.getContentService();
			if(resultSetAss.getTotalRowCount()>0){
				for (ResultSetRow reviewAssociated : resultsAss) {
					ReviewVO review = new ReviewVO();
					NamedValue[] reviewProperties = reviewAssociated.getColumns();
					for (NamedValue reviewProperty : reviewProperties) {
						if(BookshopModel.PROP_REVIEW_APPROVED.equals(reviewProperty.getName())){
							review.setApproved(new Boolean(reviewProperty.getValue()));
						} else if (BookshopModel.PROP_REVIEW_REVIEWER_NAME.equals(reviewProperty.getName())){
							review.setReviewerName(reviewProperty.getValue());
						} else if(BookshopModel.PROP_REVIEW_RATING.equals(reviewProperty.getName())){
							review.setRating(reviewProperty.getValue());
						} 
					}
					
					Reference reviewRef = new Reference();
					reviewRef.setStore(SPACES_STORE);
					reviewRef.setUuid(reviewAssociated.getNode().getId());
					
					Predicate reviewPredicate = new Predicate();
					reviewPredicate.setNodes(new Reference[]{reviewRef});
					reviewPredicate.setStore(SPACES_STORE);
					
					Content[] reviewContent = contentService.read(reviewPredicate, Constants.PROP_CONTENT);
					
					String ticketURL = "?ticket=" + getUserDetails(request).getTicket();
					String contentUrl = URLDecoder.decode(reviewContent[0].getUrl(),"UTF-8") + ticketURL;
					
					review.setContentUrl(contentUrl);
					reviews.add(review);
				}
			}
			
			request.setAttribute(REVIEWS_LIST, reviews);
			request.setAttribute(IS_IN_CART_ATTRIBUTE, isInCart);
			request.setAttribute(BOOK_DETAILS_ATTRIBUTE, book);
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(BOOK_DETAILS_FORWARD);
		}else {
			return mapping.findForward(LOGIN_FORM_FORWARD);
		}
			
	}
	
}
