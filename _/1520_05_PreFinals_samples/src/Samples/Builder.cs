using System;
using Alfresco;
using Alfresco.RepositoryWebService;
using Samples.Vo;

namespace Samples
{
	public static class Builder
	{
		public static NamedValue[] BuildCustomProperties(CreateSampleVO createSampleVo)
		{
			NamedValue[] properties = new NamedValue[3];
			properties[0] = Utils.createNamedValue(Constants.PROP_NAME, createSampleVo.Name);
			properties[1] = Utils.createNamedValue(Constants.PROP_TITLE, createSampleVo.Title);
			properties[2] = Utils.createNamedValue(Constants.PROP_DESCRIPTION, createSampleVo.Description);
			return properties;
		}
	
		public static CreateSampleVO BuildCreateSampleVO(String name, String title, String description)
		{
			CreateSampleVO createSample = new CreateSampleVO();
			createSample.Name = name;
			createSample.Title = title;
			createSample.Description = description;
			return createSample;
		}
		
		public static NamedValue[] BuildCustomPropertiesForSpace(CreateSampleVO createSampleVo)
		{
			NamedValue[] properties = new NamedValue[3];
			properties[0] = Utils.createNamedValue(Constants.PROP_NAME, createSampleVo.Name);
			properties[1] = Utils.createNamedValue(Constants.PROP_TITLE, createSampleVo.Title);
			properties[2] = Utils.createNamedValue(Constants.PROP_DESCRIPTION, createSampleVo.Description);
			return properties;
		}	
	}
}
