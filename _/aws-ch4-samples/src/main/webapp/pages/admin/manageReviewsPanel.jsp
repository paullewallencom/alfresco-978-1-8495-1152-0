<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:html>
<head>
	<title>Alfresco 3 Web Services - Bookshop - Manage Reviews</title>
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
	
	<h1>Manage Reviews</h1>
	
	<h3>Reviews to approve:</h3>
	<logic:empty name="reviews">
		<p>There is no reviews to approve.</p>
	</logic:empty>
	
	<logic:notEmpty name="reviews">
		<hr/>
		<logic:iterate id="review" name="reviews">
			<logic:equal value="false" name="review" property="approved">
				<table>
					<tr>
						<td>Reviewer: <bean:write name="review" property="reviewerName"/></td>
						<td> | </td>
						<td>Email: <bean:write name="review" property="reviewerEmail"/></td>
						<td> | </td>
						<td>Rating: <bean:write name="review" property="rating"/></td>
					</tr>
					<tr>
						<td colspan="5">
						<!-- <a target="_blank" href="<bean:write name="review" property="contentUrl"/>">Read this review</a> -->
							<iframe width="600px" src="<bean:write name="review" property="contentUrl"/>">
								Your browser doesn't support iframe
							</iframe>
						</td>
					</tr>
				</table>
				<p></p>
				<table>
					<tr>
						<td><a href="/aws-ch4-samples/admin/acceptReview.do?idReview=<bean:write name="review" property="id"/>">Approve</a></td>
						<td><a href="/aws-ch4-samples/admin/rejectReview.do?idReview=<bean:write name="review" property="id"/>">Reject</a></td>
					</tr>
				</table>
			</logic:equal>
			
			<hr/>
		</logic:iterate>
	</logic:notEmpty>

</body>
</html:html>
