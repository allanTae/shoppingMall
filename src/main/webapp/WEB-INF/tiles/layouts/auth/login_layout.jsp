<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>


<!DOCTYPE html> <html lang="kr">
<head>
<meta charset="utf-8">

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
</script>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<!-- Custom styles for this template -->
<link rel="stylesheet" type="text/css"
	  href="<c:url value='/resources/common/css/login.css'/>" >

<!-- Ajax jquery -->
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<!-- SWAL -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<style>
    #tile_body { width:100%;
                 margin: 0 auto;}

	html, body { height: 100%; }

    body { display: -ms-flexbox; /* 인터넷 익스플로어 11 지원을 위함. */
    	   display: flex;
    	   -ms-flex-align: center; /* 인터넷 익스플로어 11 지원을 위함. */
    	   align-items: center;
    }

    .form-signin { width: 100%;
    			   max-width: 330px;
    			   padding: 15px;
    			   margin: auto;
    }

    .form-signin .checkbox { font-weight: 400; }

    .form-signin .form-control { position: relative;
    							 box-sizing: border-box;
    							 height: auto;
    							 padding: 10px;
    							 font-size: 16px;
    }

    .form-signin .form-control:focus { z-index: 2; }

    .form-signin input[id="uid"] { margin-bottom: -1px;
    							   border-bottom-right-radius: 0;
    							   border-bottom-left-radius: 0;
    }

    .form-signin input[id="pwd"] { margin-bottom: 10px;
    							   border-top-left-radius: 0;
    							   border-top-right-radius: 0;
    }

</style>

</head>
<body class="text-center">
    <div id="tile_common">
        <tiles:insertAttribute name="tile_common" />
    </div>
	<div id="tile_body">
		<tiles:insertAttribute name="tile_modal_find-pwd" />
		<tiles:insertAttribute name="tile_body" />
	</div>
</body>
</html>