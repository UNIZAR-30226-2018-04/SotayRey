<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.*" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="basedatos.InterfazDatos" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html lang="en" >
<head>
    <title>Buscar partida</title>
    <meta charset="utf-8">
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap -->
    <%@ include file="navbar.jsp" %>
    <%@ include file="../html/imports.html"%>

</head>
<body>

<style type="text/css">
    .jumbotron{
        color: #040404;
        background-size: auto;
        background-position: 0% 3%;
        background-image: url('http://1.bp.blogspot.com/-XzkALf18FQM/UiPLGhELPBI/AAAAAAAAFYo/ux53uYsgc44/s1600/ESPA%C3%91OLA+2.jpg');
        background-repeat: repeat;
    }

    .titulo{
        font-weight: bold;
        font-size: 120%;
    }

</style>

<%

%>

<div class="jumbotron">
</div>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="card text-white bg-success">
                <div class="card-header text-center titulo">Buscar partida</div>
                <div class="card-body text-center">
                    <form action="/......" method="post">
                        <div class="form-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                <label class="btn btn-secondary active">
                                    <input type="radio" name="tipoRival" autocomplete="off" value="false" checked> Multijugador
                                </label>
                                <label class="btn btn-secondary">
                                    <input type="radio" name="tipoRival" value = "true" autocomplete="off"> IA
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                <label class="btn btn-secondary active">
                                    <input type="radio" name="tipoPartidaPublica" autocomplete="off" value="4" checked> Parejas
                                </label>
                                <label class="btn btn-secondary">
                                    <input type="radio" name="tipoPartidaPublica" value = "2" autocomplete="off"> Uno contra uno
                                </label>
                            </div>
                        </div>
                        <button type="button" onclick="buscarPartida()" class="btn btn-default submit" data-toggle="modal" data-target="#buscarPartida"><i class="fa fa-paper-plane" aria-hidden="true"></i> Buscar partida</button>
                    </form>
                </div>
            </div>

        </div>

    </div>


</div>

<br/>

<div class="container">
    <div class="row">
        <div class="col-md-4">
            <div class="card text-white bg-info mb-3">
                <div class="card-header text-center titulo">Crear partida privada</div>
                <div class="card-body text-center">

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
                        <button type="button" class="btn btn-default submit"><i class="fa fa-paper-plane" aria-hidden="true"></i> Crear partida</button>
                    </form>
                </div>
            </div>

        </div>




        <div class="col-md-4">
            <div class="card bg-light mb-3">
                <div class="card-header text-center titulo">Unirse a una partida privada</div>
                <div class="card-body text-center">

                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#unirsePartida">Unirse</button>

                </div>
            </div>

        </div>


        <div class="col-md-4">
            <div class="card text-white bg-dark mb-3">
                <div class="card-header text-center titulo">Espectar una partida</div>
                <div class="card-body text-center">

                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#espectarPartida">Espectar</button>

                </div>
            </div>

        </div>


    </div>
</div>



<!-- MENSAJE PRUEBA


{
    "tipo_mensaje": "partida_lista",
    "total_jugadores": 2,
    ?id_partida?: 0,
    ?nombre_jugador?: ?jugador0?,
    ?id_jugador?: 0,
    ?avatar?: ?ruta_avatar?,
    ?con_ia?: true
}

recibirMensaje("{
    "tipo_mensaje": "partida_lista",
    "total_jugadores": 2,
    ?id_partida?: 0,
    ?nombre_jugador?: ?jugador0?,
    ?id_jugador?: 0,
    ?avatar?: ?ruta_avatar?,
    ?con_ia?: true
}");

 -->

<%
    // Se necesita para acceder al tapete del usuario y luego enviarselo al juego
    InterfazDatos facade = null;
    try {
        facade = InterfazDatos.instancia();
    } catch (Exception e) {
        System.err.println("ERROR: creando interfaz");
    }
    UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
    String nick = usuarioVO.getUsername();
    String miTapete = facade.obtenerTapeteFavorito(nick).getRutaImagen();
%>

<script>

    function getRadioValue(campo)
    {
        for (var i = 0; i < document.getElementsByName(campo).length; i++)
        {
            if (document.getElementsByName(campo)[i].checked)
            {
                return document.getElementsByName(campo)[i].value;
            }
        }
    }


    var socket;
    function buscarPartida(){
        //socket = new WebSocket("ws://localhost:8080/mm/matchmaking");

        var nombre_jugador = nombreUsuario;
        var socket = new WebSocket("ws://localhost:8080/mm/matchmaking");
        var listo = JSON.stringify({
            "tipo_mensaje": "busco_partida",
            "nombre_participante": nombre_jugador,
            "tipo_partida": "publica",
            "total_jugadores": parseInt(getRadioValue("tipoPartidaPublica")),
            "con_ia" : false
            //"con_ia": document.getElementsByName("tipoRival")[0].value
        });

        //console.log(msg);
        //socket.send(msg)
        socket.onopen = function() {
            console.log(listo);
            socket.send(listo);
        };
        socket.onmessage = function (msg) {
            recibirMensaje(msg.data);
            console.log(msg.data);
        };
    }

    function espectarPartida(){
        //socket = new WebSocket("ws://localhost:8080/mm/matchmaking");

        var nombre_jugador = nombreUsuario;
        var socket = new WebSocket("ws://localhost:8080/gm/endpoint");
        var listo = JSON.stringify({
            "tipo_mensaje": "espectar_partida",
            "id_partida": 387
        });

        socket.onopen = function() {
            console.log(listo);
            socket.send(listo);
        };
        socket.onmessage = function (msg) {
            recibirMensaje(msg.data);
            console.log(msg.data);
        };
    }

    function cerrarSocket(){
        socket.close();
        console.log("socket cerrado");
    }

    function recibirMensaje(msg){
        console.log("HE RECIBIDO UN MENSAJE");

         var mensaje = JSON.parse(msg);

        var total_jugadores = mensaje.total_jugadores;
        var id_partida = mensaje.id_partida;
        var nombre_jugador = nombreUsuario;
        var id_jugador;
        var con_ia = mensaje.con_ia;

        var tapete = "<%=miTapete%>";
        var espectador = false;
        console.log("EL MENSAJE ES: " + mensaje.tipo_mensaje);
         switch(mensaje.tipo_mensaje){
             case "partida_lista":
                 id_jugador = mensaje.id_jugador;
                 parametros = "miID="+id_jugador+"&idPartida="+id_partida+"&nombre="+nombre_jugador+"&numJugadores="+total_jugadores+"&tapete="+tapete+"&espectador="+espectador;
                 window.location.replace("../juego.html?"+parametros);
                 break;
             case "espectar_partida":
                 id_jugador = mensaje.id_espectador;
                 var espectador = true;
                 parametros = "miID="+id_jugador+"&idPartida="+id_partida+"&nombre="+nombre_jugador+"&numJugadores="+total_jugadores+"&tapete="+tapete+"&espectador="+espectador;
                 window.location.replace("../juego.html?"+parametros);
                 break;
         }


    }

    $("#myModal").on("hidden.bs.modal", function () {
        cerrarSocket();
    });

</script>

<div class="container">
    <!-- Modal -->
    <div class="modal fade" id="buscarPartida" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title text-center">Buscando partida...</h4>
                </div>
                <div class="modal-body text-center">
                    <p>Espere mientras se encuentran contrincantes</p>
                    <img src="../img/loading.gif" alt="animacion cargando">
                </div>
                <div class="modal-footer text-center">
                    <button type="button" onclick="cerrarSocket()" class="btn btn-default" data-dismiss="modal">Cancelar búsqueda</button>
                </div>
            </div>

        </div>
    </div>

</div>


<div class="container">
    <!-- Modal -->
    <div class="modal fade" id="unirsePartida" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title text-center">Buscando partida...</h4>
                </div>
                <div class="modal-body text-center">
                    <p>Espere mientras se encuentran contrincantes</p>
                    <img src="../img/loading.gif" alt="animacion cargando">
                </div>
                <div class="modal-footer text-center">
                    <button type="button" onclick="cerrarSocket()" class="btn btn-default" data-dismiss="modal">Cancelar búsqueda</button>
                </div>
            </div>

        </div>
    </div>

</div>


<div class="container">
    <!-- Modal -->
    <div class="modal fade" id="espectarPartida" role="dialog">
        <div class="modal-dialog modal-lg">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title text-center">Espectar partida</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body text-center">


                        <table class="table table-striped custab">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th class="text-primary">Equipo A</th>
                                <th class="text-primary">Puntos A</th>
                                <th class="text-danger">Equipo B</th>
                                <th class="text-danger">Puntos B</th>
                                <th class="text-center">Espectar</th>
                            </tr>
                            </thead>
                            <tr>
                                <td>1</td>
                                <td>Carlos, Marius</td>
                                <td>60</td>
                                <td>Víctor, Javier</td>
                                <td>30</td>
                                <td class="text-center"><a class='btn btn-info btn-xs' href="#"><span class="glyphicon glyphicon-edit"></span>Espectar</a></td>
                            </tr>
                        </table>


                </div>
            </div>

        </div>
    </div>

</div>

<button type="button" onclick="espectarPartida()" class="btn btn-default submit"><i class="fa fa-paper-plane" aria-hidden="true"></i> DEBUG ESPECTAR</button>
<a href="../juego.html?miID=3&idPartida=..&nombre=pepito&numJugadores=2&tapete="> ESPECTADOR PARA DEPURAR </a>

</body>
</html>