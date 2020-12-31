Prerequisites:
* Microsoft .NET Framework, at least version 2.0
* Alfresco, running at http://localhost:8080
* Web Services Enhancements (WSE) 3.0 for Microsoft .NET, available here:
	http://www.microsoft.com/downloads/details.aspx?FamilyID=018a09fd-3a74-43c5-8ec1-8d789091255d

How to build:
Check msbuild is available from PATH, otherwise type (for .NET 3.5):

	set PATH=%PATH%;%WinDir%\Microsoft.NET\Framework\v3.5

or (for .NET 2.0)

	set PATH=%PATH%;%WinDir%\Microsoft.NET\Framework\v2.0.50727

To build and run integration tests, type:

	build.bat

Enjoy!