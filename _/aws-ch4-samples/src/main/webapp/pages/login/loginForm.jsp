<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<html:html>
<head>
	<title>Alfresco 3 Web Services - Bookshop - Login</title>
	<html:base />
</head>
<body bgcolor="white">

	<h1>Bookshop</h1>
	<h2>Login</h2>
	
	<html:form action="/login" method="post">
		<table>
			<tr>
				<td>Username:</td><td><html:text property="username" value=""/></td>
			</tr>
			<tr>
				<td>Password:</td><td><html:password property="password" value=""/></td>
				<td><html:submit>Sign in</html:submit></td>
			</tr>
		</table>
	</html:form>

</body>
</html:html>
