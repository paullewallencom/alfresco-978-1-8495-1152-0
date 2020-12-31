using System;
using System.Collections.Generic;
using Alfresco;
using Alfresco.RepositoryWebService;
using NUnit.Framework;
using Samples.Vo;

namespace Samples.Search
{
	[TestFixture]
	public class SearchQuery
	{
		[TestCase]
		public void CanPerformSearches()
		{
			AuthenticationUtils.startSession("admin", "admin");
			try {
	
				RepositoryService repositoryService = WebServiceFactory.getRepositoryService();
				Store spacesStore = new Store(StoreEnum.workspace, "SpacesStore");
				String luceneQuery = "PATH:\"/app:company_home\"";
				Query query = new Query(Constants.QUERY_LANG_LUCENE, luceneQuery);
				QueryResult queryResult = repositoryService.query(spacesStore, query, false);
				ResultSet resultSet = queryResult.resultSet;
				ResultSetRow[] results = resultSet.rows;
				
				//your custom list
				IList<CustomResultVO> customResultList = new List<CustomResultVO>();
				
				//retrieve results from the resultSet
				foreach(ResultSetRow resultRow in results)
				{
					ResultSetRowNode nodeResult = resultRow.node;
					
					//create your custom value object
					CustomResultVO customResultVo = new CustomResultVO();
					customResultVo.Id = nodeResult.id;
					customResultVo.Type = nodeResult.type;
					
					//retrieve properties from the current node
					foreach(NamedValue namedValue in resultRow.columns)
					{
						if (Constants.PROP_NAME.Equals(namedValue.name))
						{
							customResultVo.Name = namedValue.value;
						} else if (Constants.PROP_DESCRIPTION.Equals(namedValue.name))
						{
							customResultVo.Description = namedValue.value;
						} 
					}					
					
					//add the current result to your custom list
					customResultList.Add(customResultVo);
				}
				
				Assert.AreEqual(1, customResultList.Count);
				
				CustomResultVO firstResult = customResultList[0];
				Assert.IsNotNull(firstResult.Id);
				Assert.AreEqual("{http://www.alfresco.org/model/content/1.0}folder", firstResult.Type);
	
			} finally {
				AuthenticationUtils.endSession();
			}
		}
	}
}
