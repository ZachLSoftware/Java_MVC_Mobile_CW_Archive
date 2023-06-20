<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Add a Product</title>
</head>
<body>
<style>
	textarea {vertical-align:middle}
</style>

<h1>Add a Product</h2>
<form:form action="/addProduct" modelAttribute="product">

	<input type="hidden" name="shop" value="${shopId}" />
	
	<form:label path="name">Product Name: </form:label>
	<form:input type="text" path="name"/>
	<form:errors path="name"/>
	
<br/><br/>	
	
	<form:label path="description">Description: </form:label>
	<form:textarea path="description"/>
	<form:errors path="description"/>
	
<br/><br/>

	<form:label path="shelf">Shelf (Food, Decoration, or Tools): </form:label>
	<form:input path="shelf"/>
	<form:errors path="shelf"/>
	
<br/><br/>
	
	<form:label path="stock">Stock Count: </form:label>
	<form:input type="number" path="stock"/>
	<form:errors path="stock"/>
	
<br/><br/>
	
	<input type="submit"/>
</form:form>
<a href="/">Home</a>
</body>
</html>