<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:html>
<head>
	<title>Alfresco 3 Web Services - Bookshop - Login OK</title>
	<html:base />
</head>
<body bgcolor="white">

	<div align="left">
			<table cellspacing="20">
			<tr>
				<td><a href="/aws-ch4-samples/homepage.do">Home</a></td>
				<logic:equal name="loggedIn" value="true">
					<td>
						<a href="/aws-ch4-samples/userAccount.do">Account</a>
					</td>
					<td>
						<a href="/aws-ch4-samples/userCart.do">Cart</a>
					</td>
				</logic:equal>
				<logic:equal name="loggedIn" value="false">
					<td>
						<a href="/aws-ch4-samples/loginForm.do">Sign in</a>
					</td>
				</logic:equal>
				<logic:equal name="isAdmin" value="true">
					<td>
						<a href="/aws-ch4-samples/admin/manageReviews.do"><strong>Manage Reviews</strong></a>
					</td>
				</logic:equal>
					<td>
						<a href="/aws-ch4-samples/logout.do">Sign out</a>
					</td>
			</tr>
		</table>
	</div>

	<p><bean:write name="username" />, you are logged in.</p>

</body>
</html:html>
