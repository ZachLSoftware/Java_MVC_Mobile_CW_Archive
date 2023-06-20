<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>List</title>
</head>
<body style="text-align: center">
<p>
	<h1>Movies</h1>
	<c:forEach items="${movies}" var="movie">
		<p>
		<h3><pre><c:out value="${movie.toString()}"/></pre></h3>
		<a href="/deleteMovie?id=${movie.getId()}">Delete ${movie.getTitle()}</a>
		</p>
		<hr style="border-top: solid; width: 50%;"/>
	</c:forEach>
		<h1>People</h1>
	<c:forEach items="${people}" var="person">
		<p>
		<h3><pre><c:out value="${person.toString()}"/></pre></h3>
		<c:if test="${person.getJobTitle() eq 'Director'}">
			<c:forEach items="${movies}" var="movie">
				<c:if test="${movie.getDirector().getId()==person.getId()}">
					<pre><c:out value="Director of: ${movie.getTitle()}"/></pre>
				</c:if>
			</c:forEach>
		</c:if>
		<a href="/deletePerson?id=${person.getId()}">Delete ${person.getFirstName()} ${person.getLastName()}</a>
		</p>
		<hr style="border-top: solid; width: 50%;"/>
	</c:forEach>
		<h1>Soundtracks</h1>
	<c:forEach items="${soundtracks}" var="soundtrack">
		<p>
		<h3><pre><c:out value="${soundtrack.toString()}"/></pre></h3>
		<c:set var = "prev"/>
	<ul>
		<c:forEach items="${soundtrack.getSongs()}" var="song">

			<c:if test="${song.getId() != prev}">
				<li>
				<c:out value="${song.toString()}"/>
				<a href="/deleteSong?id=${song.getId()}"/>Delete ${song.getTrackNumber()}: ${song.getTitle()}</a>
				</li>
			</c:if>
			<c:set var = "prev" value = "${song.getId()}"/>
		</c:forEach>
	</ul>
		<a href="/deleteSoundtrack?id=${soundtrack.getId()}">Delete ${soundtrack.getTitle()}</a>
		</p>
		<hr style="border-top: solid; width: 50%;"/>
	</c:forEach>

	
</p>
</body>
</html>