package com.packtpub.a3ws.ch4.samples.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.packtpub.a3ws.ch4.samples.vo.BookVO;

public class HomepageAction extends BaseAction {
	
	private static final String HOMEPAGE_FORWARD = "homepage";
	private static final String LOGGED_IN_ATTRIBUTE = "loggedIn";
	private static final String BOOKS_ATTRIBUTE = "booksList";

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
			
			List<BookVO> booksList = new ArrayList<BookVO>();
			
			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			Store spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
			String luceneQuery = "PATH:\"/app:company_home/cm:Books/*\"";
			Query query = new Query(Constants.QUERY_LANG_LUCENE, luceneQuery);
			QueryResult queryResult = repositoryService.query(spacesStore, query, false);
			
			ResultSet resultSet = queryResult.getResultSet();
			if(resultSet.getTotalRowCount()>0){
				ResultSetRow[] results = resultSet.getRows();
				//retrieve results from the resultSet
				if(results!=null){
					for (ResultSetRow resultRow : results) {
						ResultSetRowNode nodeResult = resultRow.getNode();
						
						//create the current book bean
						BookVO book = new BookVO();
						book.setId(nodeResult.getId());
						
						//retrieve properties from the current node
						for (NamedValue namedValue : resultRow.getColumns()) {
							if (Constants.PROP_TITLE.equals(namedValue.getName())) {
								book.setTitle(namedValue.getValue());
							} else if (Constants.PROP_DESCRIPTION.equals(namedValue.getName())) {
								book.setDescription(namedValue.getValue());
							} 
						}
						
						//add the current book to the list
						booksList.add(book);
						
					}
				}
			
			}
			
			request.setAttribute(BOOKS_ATTRIBUTE, booksList);
			request.setAttribute(IS_ADMIN_ATTRIBUTE, isAdmin);
			request.setAttribute(LOGGED_IN_ATTRIBUTE, loggedIn);
			return mapping.findForward(HOMEPAGE_FORWARD);
		
		} else {
			return mapping.findForward(LOGIN_FORM_FORWARD);
		}
		
	}
	
}
