<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Product List</title>
</head>
<body>
<h2>Product List for ${shop.getChain()}</h2>
<p>
	<c:forEach items="${shop.getProducts()}" var="product">
		<p>
		<h3><c:out value="${product.getName()}, \"${product.getDescription()}\", located on the ${product.getShelf()} shelf"/></h3>
		</p>
	</c:forEach>
	<a href="/newProduct?shop=${shop.getId()}">Add a product for ${shop.getChain()}</a>
	
</p>
<a href="/">Home</a>
</body>
</html>