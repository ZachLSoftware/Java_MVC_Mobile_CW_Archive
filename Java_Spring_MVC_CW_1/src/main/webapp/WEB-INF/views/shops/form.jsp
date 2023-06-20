<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Add a Shop</title>
</head>
<body>
<h1>Add a Shop</h2>
<form:form action="/addShop" modelAttribute="shop">

	<form:label path="id">Shop ID: </form:label>
	<form:input type="number" required="required" path="id"/>
	<form:errors path="id"/>
<br/><br/>
	<form:label path="chain">Shop Chain: </form:label>
	<form:input path="chain"/>
	<form:errors path="chain"/>
<br/><br/>
	<form:label path="address">Shop Address: </form:label>
	<form:input path="address"/>
	<form:errors path="address"/>
<br/><br/>
	<input type="submit"/>
</form:form>
<a href="/">Home</a>
</body>
</html>