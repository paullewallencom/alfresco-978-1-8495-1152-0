using System;
using System.Text;
using Alfresco;
using Alfresco.RepositoryWebService;
using NUnit.Framework;
using Samples.Vo;

namespace Samples.Crud
{
	[TestFixture]
	public class CreateNodeWithContent
	{
		[TestCase]
		public void CanCreateNodesWithContent()
		{
			AuthenticationUtils.startSession("admin", "admin");
			Store spacesStore = new Store(StoreEnum.workspace, "SpacesStore");
			String name = "AWSBook " + DateTime.Now.Ticks;
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
				
				//set mime type and encoding for indexing
				ContentFormat format = new ContentFormat(mimeType, encoding);
				
				//write operation
				CMLWriteContent writeContent = new CMLWriteContent();
				writeContent.format = format;
				writeContent.where = predicate;
				writeContent.property = Constants.PROP_CONTENT;
				writeContent.content = new ASCIIEncoding().GetBytes("This is the content for the new node");
				
				//build the CML object
				CML cml = new CML();
		        cml.create = new CMLCreate[]{ create };
		        cml.writeContent = new CMLWriteContent[]{ writeContent };
		        
		        //perform a complete CML update for the node and the related file
		        UpdateResult[] result = WebServiceFactory.getRepositoryService().update(cml);
		        
		        String expectedPath = "/app:company_home/cm:AWSBook_x0020_";
		        Assert.IsTrue(result[0].destination.path.StartsWith(expectedPath));
	
			} finally {
				AuthenticationUtils.endSession();
			}
		}
	}
}
