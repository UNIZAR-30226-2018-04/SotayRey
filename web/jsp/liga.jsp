<!DOCTYPE html>
<html lang="es">
<head>
    <title>Clasificaci&oacuten</title>
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <%@ include file="navbar.jsp" %>
    <!-- Bootstrap -->
    <%@ include file="../html/imports.html"%>
</head>
<body>

<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("/jsp/login.jsp");
    } else {
        //StatsUsuarioVO statsVO = (StatsUsuarioVO) session.getAttribute("userStats");
        //monedas = statsVO.getDivisa();
    }%>

    <style type="text/css">
        .jumbotron{
            color: black;
            background-size: cover;
        }
    </style>

    <div class="jumbotron">
        <div class="container text-center">
            <h1>Clasificacion</h1>
            <p>Comprueba el puesto en el que vas. ¡Lucha por ser el campeon!</p>
        </div>
    </div>

    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">Ligas</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#">Page 1</a></li>
                <li><a href="#">Page 2</a></li>
                <li><a href="#">Page 3</a></li>
            </ul>
        </div>
    </nav>

</body>
</html>
