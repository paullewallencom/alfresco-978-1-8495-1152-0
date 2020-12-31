using System;

namespace Samples.Vo
{
	public class CustomResultVO
	{
		private String name;
		private String description;
		private String title;
		private String type;
		private String id;
		
		public String Id
		{
			get { return id; }
			set { this.id = value; }
		}
		
		public String Type
		{
			get { return type; }
			set { this.type= value; }
		}
		
		public String Name
		{
			get { return name; }
			set { this.name = value; }
		}
		
		public String Title
		{
			get { return title; }
			set { this.title = value; }
		}

		public String Description
		{
			get { return description; }
			set { this.description = value; }
		}
		
		public override String ToString()
		{
			return "CustomResultVO | NAME: " + name + " | TYPE: " + type + " | ID: " + id;
		}
	}
}
