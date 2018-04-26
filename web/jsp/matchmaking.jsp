<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.*" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html lang="en" >
<head>
    <title>Buscar partida</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- Bootstrap -->
    <%@ include file="navbar.jsp" %>
    <%@ include file="../html/imports.html"%>

</head>
<body>

<style type="text/css">
    .jumbotron{
        color: black;
        background-size: cover;
    }
</style>

<div class="jumbotron">
    <div class="container text-center">
        <h1>Buscar partida</h1>
        <p>¡Es hora de jugar!</p>
    </div>
</div>


<div class="container">
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-success">
                <div class="panel-heading text-center">
                    <h3 class="panel-title">Buscar partida</h3>
                </div>
                <div class="panel-body text-center">
                    <form action="/......" method="post">
                        <div class="form-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                <label class="btn btn-secondary active">
                                    <input type="radio" name="tipoRival" id="option1" autocomplete="off" value="multijugador" checked> Multijugador
                                </label>
                                <label class="btn btn-secondary">
                                    <input type="radio" name="tipoRival" value = "ia" autocomplete="off"> IA
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                <label class="btn btn-secondary active">
                                    <input type="radio" name="tipoPartida" id="option1" autocomplete="off" value="parejas" checked> Parejas
                                </label>
                                <label class="btn btn-secondary">
                                    <input type="radio" name="tipoPartida" value = "unocontrauno" autocomplete="off"> Uno contra uno
                                </label>
                            </div>
                        </div>
                        <button type="button" class="btn btn-default submit"><i class="fa fa-paper-plane" aria-hidden="true"></i>Buscar partida</button>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="row">
                <div class="panel panel-primary">
                    <div class="panel-heading text-center">
                        <h3 class="panel-title">Crear partida privada</h3>
                    </div>
                    <div class="panel-body text-center">
                        <form action="/......" method="post">
                            <div class="form-group">
                                <input type="text" name="nombre" class="form-control" placeholder="Nombre partida">
                            </div>
                            <div class="form-group">
                                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                    <label class="btn btn-secondary active">
                                        <input type="radio" name="tipoPartida" id="option1" autocomplete="off" value="parejas" checked> Parejas
                                    </label>
                                    <label class="btn btn-secondary">
                                        <input type="radio" name="tipoPartida" value = "unocontrauno" autocomplete="off"> Uno contra uno
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <input type="password" name="password" class="form-control" placeholder="Contraseña partida">
                            </div>
                            <button type="button" class="btn btn-default submit"><i class="fa fa-paper-plane" aria-hidden="true"></i>Crear partida</button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="panel panel-primary">
                    <div class="panel-heading text-center">
                        <h3 class="panel-title">Unirse a una partida privada</h3>
                    </div>
                    <div class="panel-body text-center">
                        <a href="http://example.com/" class="btn btn-success">
                            Unirse
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</body>
</html>
