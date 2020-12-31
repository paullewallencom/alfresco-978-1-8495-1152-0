using System;
using Alfresco;
using Alfresco.RepositoryWebService;
using NUnit.Framework;
using Samples.Vo;

namespace Samples.Crud
{
	[TestFixture]
	public class DeleteNode
	{
		[TestCase]
		public void CanDeleteNodes()
		{
			AuthenticationUtils.startSession("admin", "admin");
			Store spacesStore = new Store(StoreEnum.workspace, "SpacesStore");
			String name = "AWS Book " + DateTime.Now.Ticks;
			String description = "This is a content created with a sample of the book";
			
			//custom value object
			CreateSampleVO createSampleVo = Builder.BuildCreateSampleVO(name, name, description);
			
			try {
	
				ParentReference parent = new ParentReference(
						spacesStore, 
						null,
						"/app:company_home",
						Constants.ASSOC_CONTAINS,
						"{" + Constants.NAMESPACE_CONTENT_MODEL + "}" + name
				);
				
				//build properties
				NamedValue[] properties = Builder.BuildCustomProperties(createSampleVo);
				
				//create operation
				CMLCreate create = new CMLCreate();
				create.id = "1";
				create.parent = parent;
				create.type = Constants.TYPE_CONTENT;
				create.property = properties;
				
				//build the CML object
				CML cmlAdd = new CML();
				cmlAdd.create = new CMLCreate[]{create};
		        
		        //perform a CML update to create the node
		        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cmlAdd);
		        
				String expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_";
				Assert.IsTrue(result[0].destination.path.StartsWith(expectedPath));
		        
		        //create a predicate
		        Reference reference = result[0].destination;
		        Predicate predicate = new Predicate(new Reference[]{ reference }, spacesStore, null);
		        
		        //delete content
		        CMLDelete delete = new CMLDelete();
		        delete.where = predicate;
		        
		        CML cmlRemove = new CML();
		        cmlRemove.delete = new CMLDelete[]{delete};
		        
		        //perform a CML update to remove the node
		        WebServiceFactory.getRepositoryService().update(cmlRemove);
		        
		        expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_";
		        Assert.IsTrue(reference.path.StartsWith(expectedPath));
	
			} finally {
				AuthenticationUtils.endSession();
			}
	
		}
	}
}
