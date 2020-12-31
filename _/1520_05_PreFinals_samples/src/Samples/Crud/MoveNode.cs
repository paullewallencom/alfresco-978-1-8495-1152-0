﻿using System;
using Alfresco;
using Alfresco.RepositoryWebService;
using NUnit.Framework;
using Samples.Vo;

namespace Samples.Crud
{
	[TestFixture]
	public class MoveNode {
	
		[TestCase]
		public void CanModeNodes()
		{
			AuthenticationUtils.startSession("admin", "admin");
			Store spacesStore = new Store(StoreEnum.workspace, "SpacesStore");
			String name = "AWS Book - Content Moved " + DateTime.Now.Ticks;
			String spaceNameForMove = "AWS Book - Move Space Sample " + DateTime.Now.Ticks;
			String description = "This is a content created with a sample of the book";
			
			//custom value object
			CreateSampleVO createSampleVo = Builder.BuildCreateSampleVO(name, name, description);
			CreateSampleVO createFolderSampleVo = Builder.BuildCreateSampleVO(spaceNameForMove, spaceNameForMove, description);
			
			try {
	
				//parent for the new node
				ParentReference parentForNode = new ParentReference(
						spacesStore, 
						null,
						"/app:company_home",
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}" + name
				);
				
				//parent for the new space
				ParentReference parentForSpace = new ParentReference(
						spacesStore, 
						null,
						"/app:company_home",
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}"+ spaceNameForMove
				);
				
				
				//build properties
				NamedValue[] properties = Builder.BuildCustomProperties(createSampleVo);
				NamedValue[] propertiesForSpace = Builder.BuildCustomPropertiesForSpace(createFolderSampleVo);
				
				//create a node
				CMLCreate create = new CMLCreate();
				create.id = "1";
				create.parent = parentForNode;
				create.type = Constants.TYPE_CONTENT;
				create.property = properties;
				
				//create a space
				CMLCreate createSpace = new CMLCreate();
				createSpace.id = "2";
				createSpace.parent = parentForSpace;
				createSpace.type = Constants.TYPE_FOLDER;
				createSpace.property = propertiesForSpace;
				
				//build the CML object
				CML cmlAdd = new CML();
				cmlAdd.create = new CMLCreate[]{create, createSpace};
		        
		        //perform a CML update to create nodes
		        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cmlAdd);
		        
				String expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Content_x0020_Moved_x0020_";
				Assert.IsTrue(result[0].destination.path.StartsWith(expectedPath));
				
				expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Move_x0020_Space_x0020_Sample_x0020_";
				Assert.IsTrue(result[1].destination.path.StartsWith(expectedPath));
		        
		        //create a predicate with the first CMLCreate result
		        Reference referenceForNode = result[0].destination;
		        Predicate sourcePredicate = new Predicate(new Reference[]{ referenceForNode }, spacesStore, null);
		        
		        //create a reference from the second CMLCreate performed for space
		        Reference referenceForTargetSpace = result[1].destination;
		        
		        //reference for the target space
		        ParentReference targetSpace = new ParentReference();
		        targetSpace.store = spacesStore;
		        targetSpace.path = referenceForTargetSpace.path;
		        targetSpace.associationType = Constants.ASSOC_CONTAINS;
		        targetSpace.childName = name;
		        
		        //move content
		        CMLMove move = new CMLMove();
		        move.where = sourcePredicate;
		        move.to = targetSpace;
		        
		        CML cmlMove = new CML();
		        cmlMove.move = new CMLMove[]{move};
		        
		        //perform a CML update to move the node
		        WebServiceFactory.getRepositoryService().update(cmlMove);
		        
		        expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Content_x0020_Moved_x0020_";
		        Assert.IsTrue(referenceForNode.path.StartsWith(expectedPath));
		        
		        expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Move_x0020_Space_x0020_Sample_x0020_";
		        Assert.IsTrue(targetSpace.path.StartsWith(expectedPath));
	
			} finally {
				AuthenticationUtils.endSession();
			}
		}
	
	}
}
