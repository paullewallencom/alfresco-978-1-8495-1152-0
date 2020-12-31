<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>

<html:html>
<head>
	<title>Alfresco 3 Web Services - Bookshop - Welcome</title>
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

	<h1>Bookshop</h1>

	<logic:notEmpty name="booksList">
		<p>Books available now:</p>
		
		<table>
			<logic:iterate name="booksList" id="book">
				<tr>
					<td><strong><bean:write name="book" property="title"/></strong></td>
					<td>| <bean:write name="book" property="description"/> |</td>
					<td><a href="/aws-ch4-samples/viewBookDetails.do?id=<bean:write name="book" property="id"/>">View Details</a></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<logic:empty name="booksList">
		<p>There is no book in the bookshop.</p>
	</logic:empty>
	
</body>
</html:html>
