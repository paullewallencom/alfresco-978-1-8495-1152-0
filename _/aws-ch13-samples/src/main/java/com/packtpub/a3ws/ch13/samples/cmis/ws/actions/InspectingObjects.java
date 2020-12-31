package com.packtpub.a3ws.ch13.samples.cmis.ws.actions;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.ns.cmis.messaging._200908.CmisObjectInFolderListType;
import org.oasis_open.docs.ns.cmis.messaging._200908.CmisObjectInFolderType;
import org.oasis_open.docs.ns.cmis.messaging._200908.CmisRepositoryEntryType;
import org.oasis_open.docs.ns.cmis.ws._200908.CmisException;
import org.oasis_open.docs.ns.cmis.ws._200908.NavigationServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.RepositoryServicePort;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.packtpub.a3ws.ch13.samples.cmis.ws.client.CmisClient;
import com.packtpub.a3ws.ch13.samples.cmis.ws.client.NavigationServiceUtils;

public class InspectingObjects {

	private static Log log = LogFactory.getLog(InspectingObjects.class);
	
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
		String rootFolderId = repositoryService.getRepositoryInfo(repositoryId, null).getRootFolderId();
		
		NavigationServicePort navigationService = cmisClient.getNavigationService(CmisClient.USERNAME, CmisClient.USER_PASSWORD);
		CmisObjectInFolderListType childrenList = navigationService.getChildren(repositoryId,
				rootFolderId , null, null, false, null, null, false, null, null, null);
		List<CmisObjectInFolderType> children = childrenList.getObjects();
		
		log.info("--- Inspecting Company Home ---");
		NavigationServiceUtils.logChildrenProperties(children);
		log.info("--- /Inspecting Company Home ---");
		
		//inspecting Data Dictionary
		String dataDictionaryFolderId = NavigationServiceUtils.getObjectIdFromProperties(children, "[Data Dictionary]");
		CmisObjectInFolderListType dataDictionaryChildrenList = navigationService.getChildren(repositoryId,
				dataDictionaryFolderId, null, null, false, null, null, false, null, null, null);
		List<CmisObjectInFolderType> dataDictionaryChildren = dataDictionaryChildrenList.getObjects();
		
		log.info("--- Inspecting Data Dictionary ---");
		NavigationServiceUtils.logChildrenProperties(dataDictionaryChildren);
		log.info("--- /Inspecting Data Dictionary ---");
	}
	
	

}
