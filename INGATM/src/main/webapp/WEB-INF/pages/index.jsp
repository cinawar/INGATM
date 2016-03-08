<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
	
	<title>ING ATM's</title>

</head>

<body>


	<c:url value="${logoutUrl}"  var="logoutUrl" />

	<!-- csrt for log out-->
	<form action="/INGATM/login" method="post" id="logoutForm">
	  <input type="hidden" 
		name="${_csrf.parameterName}"
		value="${_csrf.token}" />
		<input type="hidden" name="msg" 
		value="msg" />
	</form>
	
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>


	<!-- navbar -->
	<nav class="navbar navbar-default alert-success">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="/INGATM">ING ATMs</a>
			</div>
			<c:if test="${pageContext.request.userPrincipal.name != null}">
		<div class="navbar-header">
			Welcome : ${pageContext.request.userPrincipal.name} | <a
				href="javascript:formSubmit()"> Logout</a>
		</div>
	</c:if>
			<form class="navbar-form navbar-right" role="search" method="post" action="/INGATM/search" enctype="application/x-www-form-urlencoded">
				<div class="form-group">
					<input type="text" class="form-control" placeholder="Search" name="cityName">
				</div>
				<button type="submit" class="btn btn-default">Search</button>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			</form>
		</div>
	</nav>

	<div class="container">
		<c:if test="${not empty atms}">
			<table class="table table-hover">
				<thead>			
					<tr>
						<th>Type</th>
						<th>City</th>
						<th>Street</th>
						<th>House Number</th>
					</tr>
					
				</thead>
				<tbody>
					<c:forEach var="atm" items="${atms}">
						<tr >
							<td>${atm.type}</td>
							<td>${atm.address.city}</td>
							<td>${atm.address.street}</td>
							<td>${atm.address.housenumber}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</c:if>
		<c:if test="${empty atms}">
		<thead>
					<tr>
						<th>No data found!</th>
					 
					</tr>
				</thead>
		</c:if>
	</div>
</body>
</html>