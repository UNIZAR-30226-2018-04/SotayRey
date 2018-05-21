<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.TorneoVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="basedatos.InterfazDatos" %>

<html lang="en" >
<head>
    <title>Torneos</title>
    <meta charset="utf-8">
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap -->
    <%@ include file="navbar.jsp" %>
    <%@ include file="../html/imports.html"%>

</head>
<%
    int monedas = 0;
    boolean admin = false;
    String error;
    String accionAdmin = "";
    ArrayList<TorneoVO> torneos = new ArrayList<>();

    UsuarioVO usuarioVO;

    if (session.getAttribute("userId") == null) {
        error = "userNotFound";
        request.setAttribute("error", error);
        RequestDispatcher dispatcher = request.getRequestDispatcher
                ("/jsp/login.jsp");
        dispatcher.forward(request, response);
    } else {
        usuarioVO = (UsuarioVO) session.getAttribute("userId");
        admin = usuarioVO.getAdmin();
        torneos = (ArrayList<TorneoVO>) session.getAttribute("torneos");
    }
%>

<body>

<style type="text/css">
    .jumbotron{
        color: black;
        background-image: url('https://www.shareicon.net/download/2016/05/31/773539_trophy_512x512.png');
        background-repeat: no-repeat;

    }
</style>

<div class="jumbotron">
    <div class="container text-center">
        <h1>Torneos</h1>
        <p>Ap&uacutentate y demuestra ser el mejor</p>
    </div>
</div>

<div class="container">

    <!-- Gestión de errores -->
    <%  error = (String) request.getAttribute("error"); %>
    <%
        if (error != null) { // Hay un error %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">

        <%
            switch (error){
                case "date": %>
        <strong>Fecha introducida incorrecta o demasiado antigua.</strong> Inténtalo de nuevo.
        <%
                break;
            case "interno": %>
        <strong> Sistema en mantenimiento.</strong> Vuelva a intentarlo en unos minutos.
        <%
                break;
            case "done": %>
        <strong>Tarea realizada correctamente.</strong>
        <%
                break;
            default:%>
        <strong><%=error%></strong> Ha ocurrido un error. Vuelva a iniciar sesión e intentelo de nuevo.
        <%
            }%>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>

    <% } %>
    <div class="card">
    <% if (admin) {%>
            <div class="card-header">
                <h2>Gestión de torneos</h2>
            </div>
            <div class="card-footer">
                <div class="btn-toolbar">
                    <button type="button" class="btn btn-warning my-1 mx-1" data-toggle="modal" data-target="#crearTorneo">Crear Torneo</button>
                </div>
            </div>

    <%  } if(torneos == null){ %>
        <div class="card-body text-center">
                <h2>No hay torneos a los que puedas apuntarte. Vuelve en otro momento</h2>
        </div>
    <% } else {%>
            <div id="accordion" role="tablist">
                <div class="card mb-1">
                    <div class="card-header" role="tab" id="headingOne">
                        <h5 class="mb-0">
                            <a data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                Torneos Puntuales
                            </a>
                        </h5>
                    </div>

                    <div id="collapseOne" class="collapse show" role="tabpanel" aria-labelledby="headingOne">
                        <%--<div class="card-body">--%>
                                <table class="table">
                                    <thead>
                                    <tr class="bg-info">
                                        <th>Nombre del torneo</th>
                                        <th>Comienzo del torneo</th>
                                        <th>N&uacutemero de fases</th>
                                        <th>Premio en monedas al primer puesto</th>
                                        <th>Premio en puntos al primer puesto</th>
                                        <th>Participar</th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <% for(Integer i = 0; i < torneos.size(); i++){
                                        TorneoVO torneo = torneos.get(i);       %>
                                    <tr class="active">
                                        <td><%=torneo.getNombre()%></td>
                                        <td><%=torneo.getTimeInicio()%></td>
                                        <td><%=torneo.getNumFases()%> </td>
                                        <td><%=torneo.getPremioDivisaPrimera()%></td>
                                        <td><%=torneo.getPremioPuntuacionPrimera()%></td>
                                        <td>
                                            <div class="btn-toolbar">
                                                <button type="button" onclick="buscarPartida()" class="btn btn-success mx-1 my-1" data-toggle="modal" data-target="#unirseTorneo">Unirse</button>
                                                <button type="button" class="btn btn-warning mx-1 my-1" data-toggle="modal" data-target="#modificarTorneo<%=i%>">Modificar</button>

                                                <form action="/GestionarTorneo.do" method="post">
                                                    <input type="hidden" id="<%=i%>" name="btnEliminar" value="<%=i%>">
                                                    <input type="hidden" id="action<%=i%>" name="action_torneo" value="eliminar">
                                                    <input type="submit" class="btn btn-danger mx-1 my-1"
                                                           value="Eliminar">
                                                </form>
                                            </div>
                                        </td>
                                    </tr>

                                    <!-- Modal Modificar Torneo -->
                                    <div class="modal fade" id="modificarTorneo<%=i%>" role="dialog">
                                        <div class="modal-dialog" role="document">

                                            <!-- Modal content-->
                                            <div class="modal-content text-center">
                                                <div class="modal-header">
                                                    <h4 class="modal-title text-center">Modificar Torneo</h4>
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                </div>

                                                <div class="modal-body text-center container">
                                                    <p>Introduzca los siguientes datos para crear un nuevo torneo</p>
                                                    <form action="/GestionarTorneo.do" method="post">
                                                        <div class="col-sm">
                                                            <div class="row">
                                                                <label for="punt_mod">Puntos 1ª ronda:</label>
                                                                <input type="number" class="form-control my-1" name="punt_mod" id="punt_mod"
                                                                       placeholder="Puntos al ganar la priemra ronda" value="<%=torneo.getPremioPuntuacionPrimera()%>">
                                                            </div>
                                                            <div class="row">
                                                                <label for="divisas_mod">Divisas 1ª ronda:</label>
                                                                <input type="number" class="form-control my-1" name="divisas_mod" id="divisas_mod"
                                                                       placeholder="Divisas al ganar la priemra ronda" value="<%=torneo.getPremioDivisaPrimera()%>">
                                                            </div>
                                                            <div class="row">
                                                                <label for="date_ini<%=i%>" class="text-sm-left">Fecha inicio:</label>
                                                                <div class="col-sm">

                                                                    <input type="date" class="form-control my-1" name="date_ini" id="date_ini<%=i%>"
                                                                           placeholder="N.º de fases" value="<%=torneo.getTimeInicio().toString().split(" ")[0]%>">
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <label for="time_ini<%=i%>" class="text-sm-left">Tiempo inicio:</label>
                                                                <div class="col-sm">
                                                                    <input type="time" class="form-control my-1" name="time_ini" id="time_ini<%=i%>"
                                                                           placeholder="N.º de fases" value="<%=torneo.getTimeInicio().toString().split(" ")[1]%>">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <input type="hidden" name="pos_torneo_mod" value="<%=i%>">
                                                            <button type="submit" class="btn btn-primary" name="action_torneo" value="modificar">Modifcar Torneo</button>
                                                            <button type="reset" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <% } %>
                                    </tbody>
                                </table>
                        <%--</div>--%>
                    </div>
                </div>
                <div class="card mb-1">
                    <div class="card-header" role="tab" id="headingTwo">
                        <h5 class="mb-0">
                            <a class="collapsed" data-toggle="collapse" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                Torneos Peri&oacute;dicos
                            </a>
                        </h5>
                    </div>
                    <div id="collapseTwo" class="collapse" role="tabpanel" aria-labelledby="headingTwo">
                        <div class="card-body">

                        </div>
                    </div>
                </div>
            </div>

        <%--</div>--%>
    <% } %>
    </div>

        <!-- Modal Unirse-->
        <div class="modal fade" id="unirseTorneo" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title text-center">Esperando a que empiece...</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>

                    <div class="modal-body text-center">
                        <p>Espere a la hora de inicio del torneo</p>
                        <img class="img-fluid" src="../img/esperandoTorneo.gif" alt="animacion cargando">
                    </div>
                    <div class="modal-footer text-center">
                        <button type="button" onclick="cerrarSocket()" class="btn btn-default" data-dismiss="modal">Cancelar búsqueda</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Crear Torneo -->
        <div class="modal fade" id="crearTorneo" role="dialog">
            <div class="modal-dialog" role="document">

                <!-- Modal content-->
                <div class="modal-content text-center">
                    <div class="modal-header">
                        <h4 class="modal-title text-center">Crear Torneo</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>

                    <div class="modal-body text-center container">
                        <p>Introduzca los siguientes datos para crear un nuevo torneo</p>
                        <form action="/GestionarTorneo.do" method="post">
                            <div class="col-sm">

                                <input type="text" class="form-control my-1" name="nombre" id="nombre"
                                placeholder="Nombre">
                                <input type="text" class="form-control my-1" name="desc" id="desc"
                                placeholder="Descripcion">
                                <input type="number" class="form-control my-1" name="fases" id="fases"
                                placeholder="N.º de fases">
                                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                                    <label class="btn btn-secondary active">
                                        <input type="radio" name="puntual" value="1" autocomplete="off" checked> Puntual
                                    </label>
                                    <label class="btn btn-secondary">
                                        <input type="radio" name="puntual" value="0" autocomplete="off" > Peri&oacute;dico
                                    </label>
                                </div>
                                <div class="row" >
                                    <label id="lb_dias" for="dias" class="text-sm-left" style="display: none">Peri&oacute;do:</label>
                                    <div class="col-sm">
                                        <input type="number" class="form-control my-1" name="dias" id="dias"
                                               placeholder="N.º de d&iacute;as" style="display: none">
                                    </div>
                                </div>

                                <script>

                                    $(function(){
                                        // cache these!
                                        var radioButtons = $("[name=puntual]"), // get both radiobuttons
                                            input_dias  = $("#dias");
                                            lab_dias = $("#lb_dias");

                                        radioButtons.change(function () {
                                            if( $(this).val() === "0" ) { // this time, we do need to check the value
                                                input_dias.show();
                                                lab_dias.show();
                                            } else {
                                                input_dias.hide();
                                                lab_dias.hide();
                                            }
                                        });
                                    });
                                </script>

                                <div class="row">
                                    <label for="date_ini" class="text-sm-left">Fecha inicio:</label>
                                    <div class="col-sm">
                                        <input type="date" class="form-control my-1" name="date_ini" id="date_ini"
                                               placeholder="N.º de fases">
                                    </div>
                                </div>
                                <div class="row">
                                    <label for="time_ini" class="text-sm-left">Tiempo inicio:</label>
                                    <div class="col-sm">
                                        <input type="time" class="form-control my-1" name="time_ini" id="time_ini"
                                               placeholder="N.º de fases">
                                    </div>
                                </div>
                                <input type="number" class="form-control my-1" name="punt" id="punt"
                                       placeholder="Puntos al ganar la priemra ronda">
                                <input type="number" class="form-control my-1" name="divisas" id="divisas"
                                       placeholder="Divisas al ganar la priemra ronda">
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary" value="crear" id="action_torneo" name="action_torneo">Crear Torneo</button>
                                <button type="reset" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>

</body>
</html>

<%
    // Se necesita para acceder al tapete del usuario y luego enviarselo al juego
    InterfazDatos facade = null;
    try {
        facade = InterfazDatos.instancia();
    } catch (Exception e) {
        System.err.println("ERROR: creando interfaz");
    }
    UsuarioVO usuarioVO2 = (UsuarioVO) session.getAttribute("userId");
    String nick = usuarioVO2.getUsername();
    String miTapete = facade.obtenerTapeteFavorito(nick).getRutaImagen();
%>

<script>
    var socket;
    function buscarPartida() {
        //socket = new WebSocket("ws://localhost:8080/mm/matchmaking");
        var nombre_jugador = nombreUsuario;
        socket = new WebSocket("ws://localhost:8080/mm/matchmaking");

        var listo = JSON.stringify({
            "tipo_mensaje": "busco_torneo",
            "nombre_participante": nombre_jugador
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
    function recibirMensaje(msg) {
        console.log("HE RECIBIDO UN MENSAJE");

        switch(mensaje.tipo_mensaje){
            case "partida_lista":
                var mensaje = JSON.parse(msg);
                var total_jugadores = mensaje.total_jugadores;
                var id_partida = mensaje.id_partida;
                var nombre_jugador = nombreUsuario;
                var id_jugador;
                var con_ia = mensaje.con_ia;
                var tapete = "<%=miTapete%>";
                var espectador = false;
                console.log("EL MENSAJE ES: " + mensaje.tipo_mensaje);
                id_jugador = mensaje.id_jugador;
                parametros = "miID=" + id_jugador + "&idPartida=" + id_partida + "&nombre=" + nombre_jugador + "&numJugadores=" + total_jugadores + "&tapete=" + tapete + "&espectador=" + espectador
                    + "&torneo=" + mensaje.torneo;
                window.location.replace("../juego.html?" + parametros);
                break;
            case "restante_torneo":
                setTimeout(function(){
                    // TODO tipo de mensaje para avisar del comienzo del toreno

                    }, parseInt(mensaje.tiempo)*1000); // Porque esta en segundos
                break;
        }

    }

    $("#botIA").click(function() {
        console.log("Click bot IA");
        $("#botParejas").addClass("disabled");
    });
    $("#botMult").on("click", function() {
        console.log("Click bot mult");
        $("#botParejas").removeClass("disabled");
    });
    $("#buscarPartida").on("hidden.bs.modal", function () {
        cerrarSocket();
    });

    function findGetParameter(parameterName) {
        var result = null,
            tmp = [];
        location.search
            .substr(1)
            .split("&")
            .forEach(function (item) {
                tmp = item.split("=");
                if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
            });
        return result;
    }

    var sigueTorneo = findGetParameter("sigueTorneo") == "true";
    if (sigueTorneo) {
        buscarPartida();
        $('#unirseTorneo').modal('show');
    }

</script>




