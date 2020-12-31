package com.packtpub.a3ws.ch13.samples.cmis.ws.client;

import org.oasis_open.docs.ns.cmis.ws._200908.DiscoveryServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.MultiFilingServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.NavigationServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.ObjectServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.PolicyServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.RelationshipServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.RepositoryServicePort;
import org.oasis_open.docs.ns.cmis.ws._200908.VersioningServicePort;

public class CmisClient {

	public static final String APPLICATION_CONTEXT = "com/packtpub/a3ws/ch12/samples/context/applicationContext.xml";
	public static final String CMIS_CLIENT_BEAN_NAME = "cmisClient";
	public static final String USERNAME = "admin";
	public static final String USER_PASSWORD = "admin";
	private RepositoryServicePort repositoryService;
	private DiscoveryServicePort discoveryService;
	private MultiFilingServicePort multiFilingService;
	private NavigationServicePort navigationService;
	private ObjectServicePort objectService;
	private PolicyServicePort policyService;
	private RelationshipServicePort relationshipService;
	private VersioningServicePort versioningService;
	
	public void setRepositoryService(RepositoryServicePort repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setDiscoveryService(DiscoveryServicePort discoveryService) {
		this.discoveryService = discoveryService;
	}
	public void setMultiFilingService(MultiFilingServicePort multiFilingService) {
		this.multiFilingService = multiFilingService;
	}
	public void setNavigationService(NavigationServicePort navigationService) {
		this.navigationService = navigationService;
	}
	public void setObjectService(ObjectServicePort objectService) {
		this.objectService = objectService;
	}
	public void setPolicyService(PolicyServicePort policyService) {
		this.policyService = policyService;
	}
	public void setRelationshipService(RelationshipServicePort relationshipService) {
		this.relationshipService = relationshipService;
	}
	public void setVersioningService(VersioningServicePort versioningService) {
		this.versioningService = versioningService;
	}
	
	public RepositoryServicePort getRepositoryService(String username, String password){
		return CmisUtils.configureWss4jClient(repositoryService, username, password);
	}
	
	public DiscoveryServicePort getDiscoveryService(String username, String password){
		return CmisUtils.configureWss4jClient(discoveryService, username, password);
	}
	
	public MultiFilingServicePort getMultiFilingService(String username, String password){
		return CmisUtils.configureWss4jClient(multiFilingService, username, password);
	}
	
	public ObjectServicePort getObjectService(String username, String password){
		return CmisUtils.configureWss4jClient(objectService, username, password);
	}
	
	public NavigationServicePort getNavigationService(String username, String password){
		return CmisUtils.configureWss4jClient(navigationService, username, password);
	}
	
	public PolicyServicePort getPolicyService(String username, String password){
		return CmisUtils.configureWss4jClient(policyService, username, password);
	}
	
	public RelationshipServicePort getRelationshipService(String username, String password){
		return CmisUtils.configureWss4jClient(relationshipService, username, password);
	}
	
	public VersioningServicePort getVersioningService(String username, String password){
		return CmisUtils.configureWss4jClient(versioningService, username, password);
	}
	
	
}
