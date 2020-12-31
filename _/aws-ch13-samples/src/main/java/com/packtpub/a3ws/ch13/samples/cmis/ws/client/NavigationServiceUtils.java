package com.packtpub.a3ws.ch13.samples.cmis.ws.client;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.ns.cmis.core._200908.CmisObjectType;
import org.oasis_open.docs.ns.cmis.core._200908.CmisProperty;
import org.oasis_open.docs.ns.cmis.core._200908.CmisPropertyDateTime;
import org.oasis_open.docs.ns.cmis.core._200908.CmisPropertyId;
import org.oasis_open.docs.ns.cmis.core._200908.CmisPropertyString;
import org.oasis_open.docs.ns.cmis.messaging._200908.CmisObjectInFolderType;

public class NavigationServiceUtils {

	private static Log log = LogFactory.getLog(NavigationServiceUtils.class);
	
	public static void logChildrenProperties(
			List<CmisObjectInFolderType> children) {
		for (CmisObjectInFolderType child : children) {
			CmisObjectType object = child.getObject();
			List<CmisProperty> properties = object.getProperties().getProperty();
			String name = getName(properties);
			log.info("--- "+name+" ---");
			log.info("--- Properties ---");
			for (CmisProperty property : properties) {
				//inspecting properties for each node
				if("cmis:path".equals(property.getPropertyDefinitionId()))
					log.info("PATH: "+ ((CmisPropertyString)property).getValue());
				else if("cmis:name".equals(property.getPropertyDefinitionId()))
					log.info("Name: "+ ((CmisPropertyString)property).getValue());
				else if("cmis:objectId".equals(property.getPropertyDefinitionId()))
					log.info("Object Id: "+ ((CmisPropertyId)property).getValue());
				else if("cmis:objectTypeId".equals(property.getPropertyDefinitionId()))
					log.info("Object Type: "+ ((CmisPropertyId)property).getValue());
				else if("cmis:baseTypeId".equals(property.getPropertyDefinitionId()))
					log.info("Base Type: "+ ((CmisPropertyId)property).getValue());
				else if("cmis:lastModifiedBy".equals(property.getPropertyDefinitionId()))
					log.info("Last Modified By: "+ ((CmisPropertyString)property).getValue());
				else if("cmis:createdBy".equals(property.getPropertyDefinitionId()))
					log.info("Created By: "+ ((CmisPropertyString)property).getValue());
				else if("cmis:creationDate".equals(property.getPropertyDefinitionId()))
					log.info("Creation Date: "+ ((CmisPropertyDateTime)property).getValue());
				else if("cmis:lastModificationDate".equals(property.getPropertyDefinitionId()))
					log.info("Last Modification Date: "+ ((CmisPropertyDateTime)property).getValue());
				else if("cmis:parentId".equals(property.getPropertyDefinitionId()))
					log.info("Parent Id: "+ ((CmisPropertyId)property).getValue());
			}
			log.info("--- /Properties ---");
			
			log.info("--- /"+name+" ---");
			log.info(StringUtils.EMPTY);
			
		}
	}
	
	public static String getName(List<CmisProperty> properties){
		String name = StringUtils.EMPTY;
		for (CmisProperty cmisProperty : properties) {
			if("cmis:name".equals(cmisProperty.getPropertyDefinitionId()))
				name = ((CmisPropertyString)cmisProperty).getValue().toString();
		}
		return name;
	}
	
	public static String getObjectId(List<CmisProperty> properties){
		String objectId = StringUtils.EMPTY;
		for (CmisProperty cmisProperty : properties) {
			if("cmis:objectId".equals(cmisProperty.getPropertyDefinitionId()))
				objectId = ((CmisPropertyId)cmisProperty).getValue().toString();
		}
		return objectId;
	}
	
	public static String getObjectIdFromProperties(List<CmisObjectInFolderType> children, String name){
		String objectId = StringUtils.EMPTY;
		String objectName = StringUtils.EMPTY;
		for (CmisObjectInFolderType child : children) {
			CmisObjectType object = child.getObject();
			List<CmisProperty> properties = object.getProperties().getProperty();
			for (CmisProperty cmisProperty : properties) {
				if("cmis:name".equals(cmisProperty.getPropertyDefinitionId())){
					objectName = ((CmisPropertyString)cmisProperty).getValue().toString();
					if(objectName.equals(name)){
						objectId = getObjectId(properties);
						break;
					}
				}
			}
		}
		objectId = StringUtils.removeEnd(objectId, "]");
		objectId = StringUtils.removeStart(objectId, "[");
		return objectId;
	}
	
}
