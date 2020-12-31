<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:html>
<head>
	<title>Alfresco 3 Web Services - Bookshop - Book Details</title>
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
	
	<h1>Book Details</h1>
	<h2><bean:write name="book" property="title"/></h2>
	<hr/>
	<logic:equal name="isInCart" value="false">
		<h3><a href="/aws-ch4-samples/addToCart.do?id=<bean:write name="book" property="id"/>">add to cart</a></h3>
	</logic:equal>
	<logic:equal name="isInCart" value="true">
		<h3>This book is added in your Cart</h3>
	</logic:equal>
	
	<p><strong>ISBN:</strong> <bean:write name="book" property="isbn"/></p>
	<p><strong>Author:</strong> <bean:write name="book" property="author"/></p>
	<p><strong>Publisher:</strong> <bean:write name="book" property="publisher"/></p>

	<p><strong>Description</strong></p>
	<p><bean:write name="book" property="description"/></p>
	
	<h3>Reviews:</h3>
	<logic:empty name="reviews">
		<p>There is no reviews for this book.</p>
	</logic:empty>
	
	<p><a href="/aws-ch4-samples/formReview.do?id=<bean:write name="book" property="id"/>">Add a Review</a></p>
	
	<logic:notEmpty name="reviews">
		<hr/>
		<logic:iterate id="review" name="reviews">
			<logic:equal value="true" name="review" property="approved">
				<table>
					<tr>
						<td>Reviewer: <bean:write name="review" property="reviewerName"/></td>
						<td> | </td>
						<td>Rating: <bean:write name="review" property="rating"/></td>
					</tr>
					<tr>
						<td><a target="_blank" href="<bean:write name="review" property="contentUrl"/>">Read this review</a></td>
						<td colspan="5">
							<iframe width="400px" src="<bean:write name="review" property="contentUrl"/>">
								Your browser doesn't support iframe
							</iframe>
						</td>
					</tr>
				</table>
				<p></p>
			</logic:equal>
			<hr/>
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:equal name="isInCart" value="false">
		<h3><a href="/aws-ch4-samples/addToCart.do?id=<bean:write name="book" property="id"/>">add to cart</a></h3>
	</logic:equal>
	<logic:equal name="isInCart" value="true">
		<h3>This book is added in your Cart</h3>
	</logic:equal>
	
</body>
</html:html>
