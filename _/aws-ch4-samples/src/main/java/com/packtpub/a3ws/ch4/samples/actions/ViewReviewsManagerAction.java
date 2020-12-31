package com.packtpub.a3ws.ch4.samples.actions;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.model.BookshopModel;
import com.packtpub.a3ws.ch4.samples.vo.ReviewVO;

public class ViewReviewsManagerAction extends BaseAction {
	
	private static final String REVIEW_MANAGED_OK_FORWARD = "manageReviewsPanel";
	private static final String IS_IN_CART_ATTRIBUTE = "isInCart";
	private static final String REVIEWS_LIST = "reviews";

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Boolean loggedIn = new Boolean(false);
		Boolean isInCart = new Boolean(false);
		Boolean isAdmin = new Boolean(false);
		
		if(hasUserTicket(request)
				&& ADMIN_USERNAME.equals(getUserDetails(request).getUserName())){
			
			loggedIn = new Boolean(true);
			isAdmin = new Boolean(true);
			
			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			ContentServiceSoapBindingStub contentService = WebServiceFactory.getContentService();
			
			String luceneQuery = "@bs\\:approved:\"false\"";
			Query query = new Query(Constants.QUERY_LANG_LUCENE, luceneQuery);
			QueryResult result = repositoryService.query(SPACES_STORE, query, false);
			
			ResultSet resultSet = result.getResultSet();
			ResultSetRow[] reviewsResult = resultSet.getRows();
			
			List<ReviewVO> reviews = new ArrayList<ReviewVO>();
			
			if(resultSet.getTotalRowCount()>0){
				for (ResultSetRow reviewResult : reviewsResult) {
					ReviewVO review = new ReviewVO();
					review.setId(reviewResult.getNode().getId());
					
					NamedValue[] reviewProperties = reviewResult.getColumns();
					for (NamedValue property : reviewProperties) {
						if(BookshopModel.PROP_REVIEW_REVIEWER_NAME.equals(property.getName())){
							review.setReviewerName(property.getValue());
						} else if(BookshopModel.PROP_REVIEW_REVIEWER_EMAIL.equals(property.getName())){
							review.setReviewerEmail(property.getValue());
						} else if(BookshopModel.PROP_REVIEW_RATING.equals(property.getName())){
							review.setRating(property.getValue());
						}
					}
					
					Reference reference = new Reference();
					reference.setStore(SPACES_STORE);
					reference.setUuid(reviewResult.getNode().getId());
					
					Predicate predicate = new Predicate();
					predicate.setNodes(new Reference[]{reference});
					predicate.setStore(SPACES_STORE);
					
					Content[] content = contentService.read(predicate, Constants.PROP_CONTENT);
					
					String ticketURL = "?ticket=" + getUserDetails(request).getTicket();
					String contentUrl = URLDecoder.decode(content[0].getUrl(),"UTF-8") + ticketURL;
					
					review.setContentUrl(contentUrl);
					reviews.add(review);
				}
			}
			
			request.setAttribute(REVIEWS_LIST, reviews);
			request.setAttribute(IS_IN_CART_ATTRIBUTE, isInCart);
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(REVIEW_MANAGED_OK_FORWARD);
			
		}else {
			return mapping.findForward(LOGIN_FORM_FORWARD);
		}
			
	}
	
}
