using System;
using System.Xml;

namespace Alfresco
{
	public static class ISO9075
	{
		public static String Encode(String toEncode)
		{
			return XmlConvert.EncodeName(toEncode);
		}
	}
}
