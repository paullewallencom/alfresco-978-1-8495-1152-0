package com.packtpub.a3ws.ch13.samples.cmis.ws.actions;

import java.util.List;

import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.ns.cmis.core._200908.CmisPropertiesType;
import org.oasis_open.docs.ns.cmis.core._200908.CmisPropertyId;
import org.oasis_open.docs.ns.cmis.core._200908.CmisPropertyString;
import org.oasis_open.docs.ns.cmis.core._200908.ObjectFactory;
import org.oasis_open.docs.ns.cmis.messaging._200908.CmisRepositoryEntryType;
import org.oasis_open.docs.ns.cmis.ws._200908.CmisException;
import org.oasis_open.docs.ns.cmis.ws._200908.ObjectServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.RepositoryServicePort;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.packtpub.a3ws.ch13.samples.cmis.ws.client.CmisClient;

public class CreateFolder {

	private static Log log = LogFactory.getLog(CreateFolder.class);
	
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
		
		ObjectServicePort objectService = cmisClient.getObjectService(CmisClient.USERNAME, CmisClient.USER_PASSWORD);
		CmisPropertiesType newFolderProperties = new CmisPropertiesType();
		ObjectFactory objectFactory = new ObjectFactory();
		
		//creating the name property
		CmisPropertyString folderName = objectFactory.createCmisPropertyString();
		folderName.setPropertyDefinitionId("cmis:name");
		folderName.getValue().add("CMIS WS API Sample Folder "+System.currentTimeMillis());
		newFolderProperties.getProperty().add(folderName);

		//creating the type property
		CmisPropertyId folderType = objectFactory.createCmisPropertyId();
		folderType.setPropertyDefinitionId("cmis:objectTypeId");
		folderType.getValue().add("cmis:folder");
		newFolderProperties.getProperty().add(folderType);
		
		Holder<String> idHolder = new Holder<String>();
		objectService.createFolder(repositoryId, newFolderProperties, rootFolderId, null, null, null, null, idHolder);
		log.info("Folder created. Id: "+idHolder.value);
	}
}
