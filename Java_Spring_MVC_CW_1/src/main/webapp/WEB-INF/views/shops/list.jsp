<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Shop List</title>
</head>
<body>
<h2>Shop List</h2>
<p>
	<c:forEach items="${shops}" var="shop">
		<p>
		<h3><c:out value="${shop.getChain()}, located at ${shop.getAddress()}"/></h3>
		<a href="/products?shop=${shop.getId()}">View Products  for ${shop.getChain()}</a>
		</p>
	</c:forEach>
</p>
<a href="/">Home</a>
</body>
</html>