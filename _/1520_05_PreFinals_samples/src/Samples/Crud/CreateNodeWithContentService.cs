using System;
using System.Text;
using Alfresco;
using Alfresco.RepositoryWebService;
using NUnit.Framework;
using Samples.Vo;

namespace Samples.Crud
{
	[TestFixture]
	public class CreateNodeWithContentService
	{
		[TestCase]
		public void CanCreateNodesWithContent()
		{
			AuthenticationUtils.startSession("admin", "admin");
			Store spacesStore = new Store(StoreEnum.workspace, "SpacesStore");
			String name = "AWS Book - Chapter 2 - " + DateTime.Now.Ticks;
			String description = "This is a content created with a sample of the book";
			String mimeType = "text/plain";
			String encoding = "UTF-8";
			
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
				
				//create the node reference
				Reference reference = new Reference();
				reference.store = spacesStore;
				reference.path = "/app:company_home/cm:" + ISO9075.Encode(name);
				
				//create the predicate
				Predicate predicate = new Predicate();
				predicate.Items = new Reference[]{ reference };
				
				//build the CML object
				CML cml = new CML();
		        cml.create = new CMLCreate[]{ create };
		        
		        //perform a CML update for the node
		        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cml);
		        
		        //get the new node reference
		        Alfresco.ContentWebService.Reference referenceForContent = Alfresco.ContentWebService.Reference.From(result[0].destination);
		        
		        //create content with ContentService
		        Alfresco.ContentWebService.ContentFormat format = new Alfresco.ContentWebService.ContentFormat(mimeType, encoding);
		        Alfresco.ContentWebService.Content content = WebServiceFactory.getContentService().write(
		        		referenceForContent,
		        		Constants.PROP_CONTENT,  
		        		new ASCIIEncoding().GetBytes("This is the content for the new node"),
		        		format
		        );

		        String expectedPath = "/app:company_home/cm:AWS_x0020_Book_x0020_-_x0020_Chapter_x0020_2_x0020_-_x0020_";
		        Assert.IsTrue(content.node.path.StartsWith(expectedPath));
		        
			} finally {
				AuthenticationUtils.endSession();
			}
		}
	}
}
