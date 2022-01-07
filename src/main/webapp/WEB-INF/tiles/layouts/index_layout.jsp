<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html lang="kr">
    <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>TaeTae Mall</title>

    <!-- Boot bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
    <!-- Font Awesome icons (free version)-->
    <script src="https://use.fontawesome.com/releases/v5.15.4/js/all.js" crossorigin="anonymous"></script>
    <!-- Google fonts-->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css" />
    <link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700" rel="stylesheet" type="text/css" />
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="${pageContext.request.contextPath}/resources/common/css/index/indexStyle.css" rel="stylesheet" />

    <!-- Bootstrap core JS-->
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
    <!-- Core theme JS-->
    <script src="${pageContext.request.contextPath}/resources/common/js/index/indexScript.js"></script>

         <!-- carousel style -->
        <style>
            @media (max-width: 767px) {
                .carousel-inner .carousel-item > div {
                    display: none;
                }
                .carousel-inner .carousel-item > div:first-child {
                    display: block;
                }
            }
            .carousel-inner .carousel-item.active,
            .carousel-inner .carousel-item-next,
            .carousel-inner .carousel-item-prev {
                display: flex;
            }
            /* medium and up screens */
            @media (min-width: 768px) {
                .carousel-inner .carousel-item-end.active,
                .carousel-inner .carousel-item-next {
                  transform: translateX(25%);
                }
                .carousel-inner .carousel-item-start.active,
                .carousel-inner .carousel-item-prev {
                  transform: translateX(-25%);
                }
            }
            .carousel-inner .carousel-item-end,
            .carousel-inner .carousel-item-start {
              transform: translateX(0);
            }
        </style>
</head>
<body class="text-center" id="page-top">
    <div id="tile_nav">
        <tiles:insertAttribute name="tile_nav" />
    </div>
	<div id="tile_body">
		<tiles:insertAttribute name="tile_body" />
	</div>
</body>
</html>