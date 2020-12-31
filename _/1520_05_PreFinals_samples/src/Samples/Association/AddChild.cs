using System;
using Alfresco;
using Alfresco.RepositoryWebService;
using NUnit.Framework;
using Samples.Vo;

namespace Samples.Association
{
	[TestFixture]
	public class AddChild
	{
		[TestCase]
		public void CanAddChildNode()
		{
			AuthenticationUtils.startSession("admin", "admin");
			Store spacesStore = new Store(StoreEnum.workspace, "SpacesStore");
			String name = "AWS Book - Content Add Child " + DateTime.Now.Ticks;
			String spaceNameForMove = "AWS Book - Add Child Space Sample " + DateTime.Now.Ticks;
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
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}" + spaceNameForMove
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
				cmlAdd.create = new CMLCreate[]{ create, createSpace };
		        
		        //perform a CML update to create nodes
		        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cmlAdd);
		        
				String expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Content_x0020_Add_x0020_Child_x0020_";
				Assert.IsTrue(result[0].destination.path.StartsWith(expectedPath));
		        
		        expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Add_x0020_Child_x0020_Space_x0020_Sample_x0020_";
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
		        
		        //add child
		        CMLAddChild addChild = new CMLAddChild();
		        addChild.where = sourcePredicate;
		        addChild.to = targetSpace;
		        
		        CML cmlAddChild = new CML();
		        cmlAddChild.addChild = new CMLAddChild[]{ addChild };
		        
		        //perform a CML update to add the node
		        WebServiceFactory.getRepositoryService().update(cmlAddChild);
		        
		        expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Content_x0020_Add_x0020_Child_x0020_";
		        Assert.IsTrue(referenceForNode.path.StartsWith(expectedPath));
		        
		        expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Add_x0020_Child_x0020_Space_x0020_Sample_x0020_";
		        Assert.IsTrue(targetSpace.path.StartsWith(expectedPath));
	
			} finally {
				AuthenticationUtils.endSession();
			}
		}
	}
}
