package com.packtpub.a3ws.ch13.samples.cmis.ws.actions;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.ns.cmis.core._200908.CmisObjectType;
import org.oasis_open.docs.ns.cmis.core._200908.CmisProperty;
import org.oasis_open.docs.ns.cmis.core._200908.CmisPropertyString;
import org.oasis_open.docs.ns.cmis.messaging._200908.CmisRepositoryEntryType;
import org.oasis_open.docs.ns.cmis.messaging._200908.Query;
import org.oasis_open.docs.ns.cmis.messaging._200908.QueryResponse;
import org.oasis_open.docs.ns.cmis.ws._200908.CmisException;
import org.oasis_open.docs.ns.cmis.ws._200908.DiscoveryServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.RepositoryServicePort;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.packtpub.a3ws.ch13.samples.cmis.ws.client.CmisClient;

public class SearchingRepository {

	private static Log log = LogFactory.getLog(SearchingRepository.class);
	
	/**
	 * @param args
	 * @throws CmisException
	 */
	public static void main(String[] args) throws CmisException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {CmisClient.APPLICATION_CONTEXT});
		CmisClient cmisClient = (CmisClient)context.getBean(CmisClient.CMIS_CLIENT_BEAN_NAME);
		RepositoryServicePort repositoryService = cmisClient.getRepositoryService(CmisClient.USERNAME, CmisClient.USER_PASSWORD);
		List<CmisRepositoryEntryType> repositories = repositoryService.getRepositories(null);
		String repositoryId = repositories.get(0).getRepositoryId();
		
		DiscoveryServicePort discoveryService = cmisClient.getDiscoveryService(CmisClient.USERNAME, CmisClient.USER_PASSWORD);
		Query query = new Query();
		query.setRepositoryId(repositoryId);
		query.setStatement("SELECT * FROM cmis:document WHERE CONTAINS('alfresc*')");
		QueryResponse results = discoveryService.query(query);
		
		List<CmisObjectType> resultList = results.getObjects().getObjects();
		for (CmisObjectType result : resultList) {
			List<CmisProperty> properties = result.getProperties().getProperty();
			for (CmisProperty property : properties) {
				if("cmis:name".equals(property.getPropertyDefinitionId()))
					log.info(((CmisPropertyString)property).getValue());
			}
		}

	}

}
