<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>insert</title>
</head>
<body>
	<h2>商品登録</h2>
	<a href="http://localhost:8080/webExam2/manageTop.html">登録をやめる</a>
	<form action="SignUpServlet" method="post">
		<table border="1">
			<tr>
				<th>USER NAME</th><td><input type="text" name="userName"></td>
			</tr>
			<tr>
				<th>PASSWORD</th><td><input type="text" name="passWord"></td>
			</tr>
		</table>
		<button>登録</button>
		
		<c:if test="${message != null }">
			<p>${message}</p>
		</c:if>
	</form>
</body>
</html>