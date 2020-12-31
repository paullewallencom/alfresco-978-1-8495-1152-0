package com.packtpub.a3ws.ch13.samples.cmis.ws.actions;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.ns.cmis.core._200908.CmisRepositoryInfoType;
import org.oasis_open.docs.ns.cmis.messaging._200908.CmisRepositoryEntryType;
import org.oasis_open.docs.ns.cmis.ws._200908.CmisException;
import org.oasis_open.docs.ns.cmis.ws._200908.RepositoryServicePort;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.packtpub.a3ws.ch13.samples.cmis.ws.client.CmisClient;

public class GetRepositories {

	private static Log log = LogFactory.getLog(GetRepositories.class);
	
	/**
	 * @param args
	 * @throws CmisException 
	 */
	public static void main(String[] args) throws CmisException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {CmisClient.APPLICATION_CONTEXT});
		CmisClient cmisClient = (CmisClient)context.getBean(CmisClient.CMIS_CLIENT_BEAN_NAME);
		RepositoryServicePort repositoryService = cmisClient.getRepositoryService(CmisClient.USERNAME, CmisClient.USER_PASSWORD);
		List<CmisRepositoryEntryType> repositories = repositoryService.getRepositories(null);
		for (CmisRepositoryEntryType repository : repositories) {
			CmisRepositoryInfoType repositoryInfo = repositoryService.getRepositoryInfo(repository.getRepositoryId(), null);
			log.info("Repository Id: " + repository.getRepositoryId());
			log.info("Repository Name: " + repository.getRepositoryName());
			log.info("RootFolder Id: " + repositoryInfo.getRootFolderId());
			log.info("Cmis version supported: " + repositoryInfo.getCmisVersionSupported());
			log.info("Vendor: " + repositoryInfo.getVendorName());
			log.info("Product version: " + repositoryInfo.getProductVersion());
		}
	}

}
