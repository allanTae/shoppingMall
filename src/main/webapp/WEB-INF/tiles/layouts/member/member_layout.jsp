<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>


<!DOCTYPE html> <html lang="kr">
<head>
<meta charset="utf-8">
<title>TaeTae Mall</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	  href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
	  integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS"
	  crossorigin="anonymous">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<!-- Ajax jquery -->
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body class="text-center">
    <div id="tile_common">
        <tiles:insertAttribute name="tile_common" />
    </div>
	<div id="tile_body">
		<tiles:insertAttribute name="tile_body" />
	</div>
</body>
</html>