using System;

namespace Alfresco.RepositoryWebService
{
 	public partial class Store
 	{
 		public Store() { }

 		public Store(StoreEnum scheme, String address)
 		{
 			this.scheme = scheme;
			this.address = address;
 		}
	}
 	
 	public partial class ParentReference
 	{
 		public ParentReference() { }
 		
 		public ParentReference(Store store, String uuid, String path, String associationType, String childName)
 		{
 			this.store = store;
 			this.uuid = uuid;
 			this.path = path;
 			this.associationType = associationType;
 			this.childName = childName;
 		}
 	}
 	
 	public partial class Predicate
 	{
 		public Predicate() { }
 		
 		public Predicate(Reference[] nodes, Store store, Query query) 
 		{
 			this.Items = nodes;
 			// Ignore other parameters
 		}
 	}

	public partial class ContentFormat
	{
		public ContentFormat() { }
		
		public ContentFormat(String mimetype, String encoding)
		{
			this.mimetype = mimetype;
			this.encoding = encoding;
		}
	}
	
	public partial class Query
    {
		public Query() { }
		
		public Query(String language, String statement)
		{
			this.language = language;
			this.statement = statement;
		}
	}
}

namespace Alfresco.AuthoringWebService
{
	public partial class Reference
	{
		public static Reference From(Alfresco.RepositoryWebService.Reference contentReference)
		{
			Reference reference = new Reference();
			reference.path = contentReference.path;
			reference.store = StoreFrom(contentReference.store);
			reference.uuid = contentReference.uuid;
			return reference;
		}
			private static Store StoreFrom(Alfresco.RepositoryWebService.Store contentStore)
		{
			StoreEnum scheme = (StoreEnum) StoreEnum.Parse(typeof(StoreEnum), contentStore.scheme.ToString());
			return new Store(scheme, contentStore.address);
		}
	}
	
 	public partial class Store
 	{
 		public Store() { }

 		public Store(StoreEnum scheme, String address)
 		{
 			this.scheme = scheme;
			this.address = address;
 		}
	}
}

namespace Alfresco.ContentWebService
{
	public partial class Reference
	{
		public static Reference From(Alfresco.RepositoryWebService.Reference contentReference)
		{
			Reference reference = new Reference();
			reference.path = contentReference.path;
			reference.store = StoreFrom(contentReference.store);
			reference.uuid = contentReference.uuid;
			return reference;
		}
		
		private static Store StoreFrom(Alfresco.RepositoryWebService.Store contentStore)
		{
			StoreEnum scheme = (StoreEnum) StoreEnum.Parse(typeof(StoreEnum), contentStore.scheme.ToString());
			return new Store(scheme, contentStore.address);
		}
	}
	
 	public partial class Store
 	{
 		public Store() { }

 		public Store(StoreEnum scheme, String address)
 		{
 			this.scheme = scheme;
			this.address = address;
 		}
	}
		
	public partial class ContentFormat
	{
		public ContentFormat() { }
		
		public ContentFormat(String mimetype, String encoding)
		{
			this.mimetype = mimetype;
			this.encoding = encoding;
		}
	}
}