<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.TorneoVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="basedatos.InterfazDatos" %>
<%@ page import="basedatos.modelo.TorneoPeriodicoVO" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.math.BigInteger" %>
<%@ page import="java.sql.Time" %>

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
    ArrayList<TorneoPeriodicoVO> torneos_period  = new ArrayList<>();
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
        torneos_period = (ArrayList<TorneoPeriodicoVO>) session.getAttribute("torneos_period");
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

    <%  }%>
            <div id="accordion" role="tablist">
                <% int j=0; for (String n: new String[]{"Torneos Programados", "Torneos Peri&oacute;dicos"}) { j++;
                    if(j==1 || (admin && j == 2)){ // Muestra torneos programados y torneos periódicos para el admin
                %>

                <div class="card mb-1">

                    <div class="card-header" role="tab" id="heading<%=j%>">
                        <h5 class="mb-0">
                            <a class="text-center" data-toggle="collapse" data-parent="#accordion" href="#collapse<%=j%>" aria-expanded="true" aria-controls="collapse<%=j%>">
                                <h4 class="text-center"> <%=n%> </h4>
                            </a>
                        </h5>
                    </div>


                    <div id="collapse<%=j%>" class="collapse show" role="tabpanel" aria-labelledby="heading<%=n%>">
                       <% if(torneos_period == null){ %>
                        <div class="card-body text-center">
                            <h2>No hay torneos a los que puedas apuntarte. Vuelve en otro momento</h2>
                        </div>
                        <% } else {%>
                        <table class="table">
                            <thead>
                            <tr class="bg-info">
                                <th>Nombre del torneo</th>
                                <th>Comienzo del torneo</th>
                                <th>N&uacutemero de fases</th>
                                <th>Premio en monedas al primer puesto</th>
                                <th>Premio en puntos al primer puesto</th>
                                <% if (admin){
                                    if (j==2){%>
                                <th>D&iacute;as</th>
                                    <% } %>
                                <th>Acciones</th>
                                <% } else { %>
                                <th>Participar</th>
                                <% } %>
                            </tr>
                            </thead>

                            <tbody>
                            <%int tam;
                            if (j==1 ){
                                tam = torneos.size();
                            }else {
                                tam = torneos_period.size();
                            }
                            for (int i = 0; i < tam; i++) {
                                int puntPrimera=0, divPrimera=0, fases = 0, dias = 0;
                                String nombre;
                                BigInteger idTorneo = BigInteger.valueOf(-1);
                                Timestamp inicio;
                                Timestamp actual = new Timestamp(System.currentTimeMillis());
                                if (j==1){
                                    TorneoVO torneo = torneos.get(i);
                                    idTorneo = torneo.getId();
                                    nombre = torneo.getNombre();
                                    fases = torneo.getNumFases();
                                    puntPrimera = torneo.getPremioPuntuacionPrimera();
                                    divPrimera = torneo.getPremioDivisaPrimera();
                                    inicio = torneo.getTimeInicio();
                            %>
                                    <% } else {
                                        TorneoPeriodicoVO torneo = torneos_period.get(i);
                                        nombre = torneo.getNombre();
                                        fases = torneo.getNumFases();
                                        puntPrimera = torneo.getPremioPuntuacionPrimera();
                                        divPrimera = torneo.getPremioDivisaPrimera();
                                        dias = torneo.getDias();
                                        if (fases == -1){
                                            fases = 0;
                                        }
                                        if (puntPrimera == -1){
                                            puntPrimera = 0;
                                        }
                                        if (divPrimera == -1){
                                            divPrimera = 0;
                                        }
                                        inicio = torneo.getTimePrimero();
                                    }%>

                            <tr class="active">
                                <td><%=nombre%></td>
                                <td><%=inicio%></td>
                                <td><%=fases%> </td>
                                <td><%=(int)(divPrimera*Math.pow(2,fases-1))%></td>
                                <td><%=(int)(puntPrimera*Math.pow(2,fases-1))%></td>
                                <td>
                                    <% if (j==1){ // Torneo puntual %>
                                    <div class="btn-toolbar">
                                        <% if (inicio.before(actual)){ // Ya ha comenzado %>
                                        <button type="button" onclick="buscarPartida(<%=idTorneo%>)" class="btn btn-success mx-1 my-1" data-toggle="modal" data-target="#unirseTorneo">Unirse</button>
                                        <% } if(admin && inicio.after(new Timestamp(System.currentTimeMillis()))){ // Se puede modificar solo si no ha empezado %>
                                        <button type="button" class="btn btn-warning mx-1 my-1" data-toggle="modal" data-target="#modificarTorneo<%=i%>">Modificar</button>
                                        <form action="/GestionarTorneo.do" method="post">
                                            <input type="hidden" id="<%=i%>" name="btnEliminar" value="<%=i%>">
                                            <input type="hidden" id="action<%=i%>" name="action_torneo" value="eliminar">
                                            <input type="submit" class="btn btn-danger mx-1 my-1"
                                                   value="Eliminar">
                                        </form>
                                        <% } %>

                                    </div>
                                    <% } else { %>
                                    <%=dias%></td>
                                    <td>
                                        <div class="btn-toolbar">
                                            <form action="/GestionarTorneo.do" method="post">
                                                <input type="hidden" id="<%=i%>" name="btnEliminar" value="<%=i%>">
                                                <input type="hidden" id="action<%=i%>" name="action_torneo" value="eliminar">
                                                <input type="hidden" id="period<%=i%>" name="period" value="period">
                                                <input type="submit" class="btn btn-danger mx-1 my-1"
                                                       value="Eliminar">
                                            </form>
                                        </div>
                                    <% } %>
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
                                                        placeholder="Puntos al ganar la priemra ronda" value="<%=puntPrimera%>">
                                                    </div>
                                                    <div class="row">
                                                        <label for="divisas_mod">Divisas 1ª ronda:</label>
                                                        <input type="number" class="form-control my-1" name="divisas_mod" id="divisas_mod"
                                                        placeholder="Divisas al ganar la priemra ronda" value="<%=divPrimera%>">
                                                    </div>
                                                    <div class="row">
                                                        <label for="date_ini<%=i%>" class="text-sm-left">Fecha inicio:</label>
                                                        <div class="col-sm">

                                                        <input type="date" class="form-control my-1" name="date_ini" id="date_ini<%=i%>"
                                                        placeholder="N.º de fases" value="<%=inicio.toString().split(" ")[0]%>">
                                                    </div>
                                                    </div>
                                                    <div class="row">
                                                        <label for="time_ini<%=i%>" class="text-sm-left">Tiempo inicio:</label>
                                                        <div class="col-sm">
                                                            <input type="time" class="form-control my-1" name="time_ini" id="time_ini<%=i%>"
                                                            placeholder="N.º de fases" value="<%=inicio.toString().split(" ")[1]%>">
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
                            <% }%>
                            </tbody>
                        </table>
                        <% } /* fin else, hay torneos */%>
                    </div>
                </div>
                <% } // Fin muestra torneos
                } /* fin for tipo torneo */ %>

            </div>
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
    var id_torneo = -1;
    var socket;
    function buscarPartida(idTorneo) {
        //socket = new WebSocket("ws://localhost:8080/mm/matchmaking");
        id_torneo = idTorneo;
        var nombre_jugador = nombreUsuario;
        socket = new WebSocket("ws://localhost:8080/mm/matchmaking");

        var listo = JSON.stringify({
            "tipo_mensaje": "busco_torneo",
            "id_torneo": idTorneo,
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
        var mensaje = JSON.parse(msg);
        switch(mensaje.tipo_mensaje){
            case "partida_lista":
                var total_jugadores = mensaje.total_jugadores;
                var id_partida = mensaje.id_partida;
                var nombre_jugador = nombreUsuario;
                var id_jugador;
                var con_ia = mensaje.con_ia == "true";
                var tapete = "<%=miTapete%>";
                var espectador = false;
                console.log("EL MENSAJE ES: " + mensaje.tipo_mensaje);
                id_jugador = mensaje.id_jugador;
                parametros = "miID=" + id_jugador + "&idPartida=" + id_partida + "&nombre=" + nombre_jugador + "&numJugadores=" + total_jugadores + "&tapete=" + tapete + "&espectador=" + espectador + "&con_ia=" + con_ia
                    + "&torneo=" + mensaje.torneo + "&id_torneo=" + id_torneo;
                window.location.replace("../juego.html?" + parametros);
                break;
            case "restante_torneo":
                setTimeout(function(){
                    var listo = JSON.stringify({
                        "tipo_mensaje": "empezar_torneo",
                        "id_torneo": id_torneo
                    });
                    socket.send(listo);
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
    $("#unirseTorneo").on("hidden.bs.modal", function () {
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
        id_torneo = parseInt(findGetParameter("id_torneo"));
        buscarPartida(id_torneo);
        $('#unirseTorneo').modal('show');
    }

</script>




