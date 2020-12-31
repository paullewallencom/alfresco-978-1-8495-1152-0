package com.packtpub.a3ws.ch4.samples.actions;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddChild;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLWriteContent;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Query;
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
import com.packtpub.a3ws.ch4.samples.vo.ReviewFormBean;

public class SaveReviewAction extends BaseAction {
	
	private static final String ADD_REVIEW_OK_FORWARD = "reviewAdded";

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
			
			ReviewFormBean reviewForm = (ReviewFormBean)form;
			String bookUuid = reviewForm.getBookId();
			
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
			
			String luceneQuery = "PARENT:\""+homeFolderNodeRef+"\" AND @cm\\:name:\"Reviews\"";
			Query query = new Query(Constants.QUERY_LANG_LUCENE, luceneQuery);
			
			QueryResult result = repositoryService.query(SPACES_STORE, query, false);
			String pathSpaceReviews = null;
			String idSpaceReviews = null;
			
			if(result.getResultSet().getTotalRowCount()==0){
				
				//you need to create the Reviews space
				ParentReference parent = new ParentReference(
						SPACES_STORE, 
						homeFolderNodeRef.substring(24),
						null,
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}Reviews");
				
				//build properties
				NamedValue[] properties = new NamedValue[1];
				properties[0] = new NamedValue();
				properties[0].setName(Constants.PROP_NAME);
				properties[0].setValue("Reviews");
				
				//create operation
				CMLCreate create = new CMLCreate();
				create.setId("1");
				create.setParent(parent);
				create.setType(Constants.TYPE_FOLDER);
				create.setProperty(properties);
				
				//build the CML object
				CML cml = new CML();
		        cml.setCreate(new CMLCreate[]{create});
		        
		        //perform a CML update to create the Cart space
		        UpdateResult[] resultForCart = repositoryService.update(cml);
		        
		        pathSpaceReviews = resultForCart[0].getDestination().getPath();
	
			} else {
				
				//the Reviews space exists, we only need to add the review
				ResultSet resultSet = result.getResultSet();
				ResultSetRow[] results = resultSet.getRows();
				idSpaceReviews = results[0].getNode().getId();
	
			}
			
			//add a new review in the Reviews space
			
			//create the Reviews reference
			ParentReference reviewsSpace = null;
			
			if(pathSpaceReviews!=null){
				
				//if you want to use the path
				reviewsSpace = new ParentReference(
						SPACES_STORE, 
						null,
						pathSpaceReviews,
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+"Review_"+bookUuid+"_user_"+username);
			
			}else {
			
				//if you want to use the UUID
				reviewsSpace = new ParentReference(
						SPACES_STORE, 
						idSpaceReviews,
						null,
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+"Review_"+bookUuid+"_user_"+username);
			}
			
			Random random = new Random();
			
			//build properties
			NamedValue[] propertiesReview = new NamedValue[5];
			
			propertiesReview[0] = new NamedValue();
			propertiesReview[0].setName(BookshopModel.PROP_REVIEW_REVIEWER_NAME);
			propertiesReview[0].setValue(reviewForm.getReviewerName());
			
			propertiesReview[1] = new NamedValue();
			propertiesReview[1].setName(BookshopModel.PROP_REVIEW_REVIEWER_EMAIL);
			propertiesReview[1].setValue(reviewForm.getReviewerEmail());
			
			propertiesReview[2] = new NamedValue();
			propertiesReview[2].setName(BookshopModel.PROP_REVIEW_RATING);
			propertiesReview[2].setValue(reviewForm.getRating());
			
			propertiesReview[3] = new NamedValue();
			propertiesReview[3].setName(BookshopModel.PROP_REVIEW_APPROVED);
			propertiesReview[3].setValue("false");
			
			propertiesReview[4] = new NamedValue();
			propertiesReview[4].setName(Constants.PROP_NAME);
			propertiesReview[4].setValue(bookUuid+random.nextLong());
			
			//create a node for the review
			CMLCreate createReview = new CMLCreate();
			createReview.setId("1");
			createReview.setParent(reviewsSpace);
			createReview.setType(BookshopModel.REVIEW_TYPE);
			createReview.setProperty(propertiesReview);

			//build the CML object
			CML cmlAddReview = new CML();
			cmlAddReview.setCreate(new CMLCreate[]{createReview});
			
	        //perform a CML update to create the Reviews space
	        UpdateResult[] resultForReviews = repositoryService.update(cmlAddReview);

			//write the content for this review
			Reference reviewRef = resultForReviews[0].getDestination();
			Predicate reviewPredicate = new Predicate();
			reviewPredicate.setStore(SPACES_STORE);
			reviewPredicate.setNodes(new Reference[]{reviewRef});
			
			//create content for the review
			ContentFormat format = new ContentFormat();
			format.setMimetype("text/plain");
			format.setEncoding("UTF-8");
			
			CMLWriteContent contentReview = new CMLWriteContent();
			contentReview.setFormat(format);
			contentReview.setProperty(Constants.PROP_CONTENT);
			contentReview.setContent(reviewForm.getContent().getBytes());
			contentReview.setWhere(reviewPredicate);
	        
	        CML cmlWriteReview = new CML();
			cmlWriteReview.setWriteContent(new CMLWriteContent[]{contentReview});
			
	        //perform a CML update to write the content the node
			repositoryService.update(cmlWriteReview);
	        
	        //authenticate as Admin
			try {
				//close the current user session
				AuthenticationUtils.endSession();
				
				//check the user credentials
				AuthenticationUtils.startSession(ADMIN_USERNAME, ADMIN_PASSWORD);
				
			} catch (RuntimeException e) {
				return mapping.findForward(ERROR_FORWARD);
			}
	        
			//associate this review to the book
			ParentReference bookAssociation = new ParentReference();
			bookAssociation.setAssociationType(BookshopModel.ASS_BOOK_REVIEWS);
			bookAssociation.setStore(SPACES_STORE);
			
			bookAssociation.setChildName("user_"+username+"_"+random.nextLong());
			bookAssociation.setUuid(bookUuid);
			
			CMLAddChild associateReview = new CMLAddChild();
	        associateReview.setWhere(reviewPredicate);
	        associateReview.setTo(bookAssociation);
	        
			CML cmlAssociateReview = new CML();
			cmlAssociateReview.setAddChild(new CMLAddChild[]{associateReview});
			
			//perform a CML update to associate the review to the book
			repositoryService.update(cmlAssociateReview);
	        
	        //setup user session
			AuthenticationUtils.startSession(userDetails.getUserName(), reviewForm.getPassword());
			request.getSession().setAttribute(USER_TICKET, AuthenticationUtils.getAuthenticationDetails());
			
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(ADD_REVIEW_OK_FORWARD);
		
		} else
			
			return mapping.findForward(LOGIN_FORM_FORWARD);
	}
	
}
