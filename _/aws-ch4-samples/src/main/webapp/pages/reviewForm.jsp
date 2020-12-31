<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:html>
<head>
	<title>Alfresco 3 Web Services - Bookshop - Add a Review</title>
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
	
	<h1>Add a Review</h1>

	<html:form action="/saveReview.do" method="post">
		<html:hidden property="bookId" />
		<table>
			<tr>
				<td>Name:</td>
				<td>
					<html:text property="reviewerName" disabled="true" size="30"/>
					<html:hidden property="reviewerName"/>
				</td>
			</tr>
			<tr>
				<td>Email:</td>
				<td>
					<html:text property="reviewerEmail" disabled="true" size="30"/>
					<html:hidden property="reviewerEmail"/>
				</td>
			</tr>
			<tr>
				<td>Rating:</td>
				<td>
					<html:select property="rating">
						<html:option value="0">0</html:option>
						<html:option value="1">1</html:option>
						<html:option value="2">2</html:option>
						<html:option value="3">3</html:option>
						<html:option value="4">4</html:option>
						<html:option value="5">5</html:option>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>Review:</td>
				<td>
					<html:textarea rows="10" cols="50" property="content"/>
				</td>
			</tr>
			<tr>
				<td><strong>Confirm your Password:</strong></td>
				<td>
					<html:password property="password" value=""/>
				</td>
			</tr>
		</table>
		
		<html:submit>Submit for approval</html:submit>
	
	</html:form>
	
	
</body>
</html:html>
