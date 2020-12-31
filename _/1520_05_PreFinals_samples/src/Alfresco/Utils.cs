using System;
using Alfresco.RepositoryWebService;

namespace Alfresco
{
	public static class Utils
	{
		public static NamedValue createNamedValue(String name, String value)
		{
			NamedValue namedValue = new NamedValue();
			namedValue.name = name;
			namedValue.value = value;
			return namedValue;
		}
	}
}
