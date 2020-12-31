using System;

namespace Samples.Vo
{

	public class CreateSampleVO
	{
		private String name;
		private String title;
		private String description;
		
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
		
	}
}