<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" %>
<html>
<head>
	<link rel="stylesheet" href="css/blog.css"/>
	<title>Blog</title>
</head>

<body> 
<div id="header">
	<h1>
		vmForce Blog
	</h1>
</div>

<div id="main">
	<div id="nav">
		Admin
		<ul>
			<li>		
				<sec:authorize access="hasRole('ROLE_USER')">
					<a href="logout">logout</a>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
					<a href="login">login</a>
				</sec:authorize>
			</li>
		</ul>
	</div> 
	<div id="content">
		<sec:authorize access="hasRole('ROLE_USER')">
			<h3>Create a post:</h3>
			<form action="createPost.htm" method="POST">
				title: <input type="text" name="title"/><br/>
				body: <textarea name="body"></textarea><br/>
				<input type="submit" name="New Post" value="New Post"/>
			</form>
		</sec:authorize>
		<c:forEach items="${posts}" var="post">
			<h2>${post.title}</h2> 
			${post.body}<br/><br/>
			<sec:authorize access="hasRole('ROLE_USER')">	
				<form action="deletePost.htm" method="POST">
					<input type="hidden" name="postId" value="${post.id}"/>
					<input type="submit" name="Delete Post" value="Delete Post"/>
				</form>
			</sec:authorize>	
			<h3>Comments:</h3>
			<c:forEach items="${post.comments}" var="comment">
				Comment by: ${comment.author} <br/>
				${comment.body}<br/><br/>
				<sec:authorize access="hasRole('ROLE_USER')">
					<form action="deleteComment.htm" method="POST">
						<input type="hidden" name="commentId" value="${comment.id}"/>
						<input type="submit" name="Delete Comment" value="Delete Comment"/>
					</form>
				</sec:authorize>
			</c:forEach>
				
			<form action="createComment.htm" method="POST">
				Name: <input type="text" name="name"/><br/>
				Comment: <textarea name="comment"></textarea><br/>
				<input type="hidden" name="postId" value="${post.id}"/>
				<input type="submit" name="New Comment" value="New Comment"/>
			</form>
		</c:forEach>
	</div>
</div>
</body>
</html>
