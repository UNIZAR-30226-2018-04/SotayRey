<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.*" %>
<%@ page import="java.util.Arrays" %>
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
        background-size: cover;
        background-image: url('https://upload.wikimedia.org/wikipedia/commons/2/2f/Spanish_deck_printed_in_Valencia%2C_in_1778.jpg');
        background-repeat: repeat;
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
        <div class="col-md-12">
            <div class="card text-white bg-success">
                <div class="card-header text-center">Buscar partida</div>
                <div class="card-body text-center">
                    <form action="/......" method="post">
                        <div class="form-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                <label class="btn btn-secondary active">
                                    <input type="radio" name="tipoRival" id="option1" autocomplete="off" value="false" checked> Multijugador
                                </label>
                                <label class="btn btn-secondary">
                                    <input type="radio" name="tipoRival" value = "true" autocomplete="off"> IA
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                <label class="btn btn-secondary active">
                                    <input type="radio" name="tipoPartidaPublica" id="option1" autocomplete="off" value="4" checked> Parejas
                                </label>
                                <label class="btn btn-secondary">
                                    <input type="radio" name="tipoPartidaPublica" value = "2" autocomplete="off"> Uno contra uno
                                </label>
                            </div>
                        </div>
                        <button type="button" onclick="buscarPartida()" class="btn btn-default submit" data-toggle="modal" data-target="#myModal"><i class="fa fa-paper-plane" aria-hidden="true"></i>Buscar partida</button>
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
                <div class="card-header text-center">Crear partida privada</div>
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
                        <button type="button" class="btn btn-default submit"><i class="fa fa-paper-plane" aria-hidden="true"></i>Crear partida</button>
                    </form>



                </div>
            </div>

        </div>




        <div class="col-md-4">
            <div class="card bg-light mb-3">
                <div class="card-header text-center">Unirse a una partida privada</div>
                <div class="card-body text-center">

                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#unirsePartida">Unirse</button>

                </div>
            </div>

        </div>


        <div class="col-md-4">
            <div class="card text-white bg-dark mb-3">
                <div class="card-header text-center">Espectar una partida</div>
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

<script>
    var socket;
    function buscarPartida(){
        /*   socket = new WebSocket("ws://localhost:8080/matchmaking");
         socket.onmessage = function (msg) {
         console.log(msg.data);
         recibirMensaje(msg.data);
         };*/

        var listo = {
            "tipo_mensaje" : "busco_partida",
            "nombre_participante" : "miUsuario",
            "total_jugadores" : document.getElementsByName("tipoPartidaPublica")[0].value,
            "con_ia" : document.getElementsByName("tipoRival")[0].value,
        }
        var msg = JSON.stringify(listo);
        console.log(msg);
        //  socket.send(msg);
    }

    function cerrarSocket(){
        //webSocket.close();
        console.log("socket cerrado");
    }

    function recibirMensaje(msg){
        console.log("HE RECIBIDO UN MENSAJE");
        /*
         var mensaje = JSON.parse(msg);
         var total_jugadores = mensaje.total_jugadores;
         var id_partida = mensaje.id_partida;
         var nombre_jugador = mensaje.nombre_jugador;
         var id_jugador = mensaje.id_jugador;
         var con_ia = mensaje.con_ia;
         */
        var id_partida = 2392383;
        var id_jugador = 1213;
        var nombre_jugador = "juegadorPrueba";
        var parametros = "miID="+id_jugador+"&idPartida="+id_partida+"&nombre="+nombre_jugador;
        window.location.replace("../../gui_partida/juego.html?"+parametros);

    }

    $("#myModal").on("hidden.bs.modal", function () {
        cerrarSocket();
    });

</script>

<div class="container">
    <!-- Modal -->
    <div class="modal fade" id="myModal" role="dialog">
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
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title text-center">Seleccione una partida para unirse</h4>
                </div>
                <div class="modal-body text-center">
                    <table class="table table-hover" id="rank-table">
                        <thead>
                        <tr>
                            <th>Hora de inicio</th>
                            <th>Jugadores</th>
                            <th>Puntos</th>
                            <th>Espectar</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                20:00
                            </td>
                            <td>
                                Pepito, Marco, Alicia, Bob
                            </td>
                            <td>
                                <span style="color: red">25 </span> / <span style="color: blue">40 </span>
                            </td>

                            <td>
                                <button type="button" class="btn btn-success">Espectar</button>
                            </td>
                        </tr>
                        <tr>
                            <td>05/05/2018 20:00</td>
                            <td>
                                Pepito, Marco, Alicia, Bob
                            </td>
                            <td>
                                <span style="color: red">25 </span> / <span style="color: blue">40 </span>
                            </td>

                            <td>
                                <button type="button" class="btn btn-success">Espectar</button>
                            </td>
                        </tr>
                        <tr>
                            <td>05/05/2018 20:00</td>
                            <td>
                                Pepito, Marco, Alicia, Bob
                            </td>
                            <td>
                                <span style="color: red">25 </span> / <span style="color: blue">40 </span>
                            </td>

                            <td>
                                <button type="button" class="btn btn-success">Espectar</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>

</div>

</body>
</html>