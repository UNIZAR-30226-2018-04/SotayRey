<%@ page import="basedatos.modelo.UsuarioVO" %>
<%@ page import="basedatos.modelo.StatsUsuarioVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--
  Created by IntelliJ IDEA.
  Creado por Marius y Víctor
  Date: 30/3/18
  Time: 11:04
--%>

<html>
<head>
    <meta charset="UTF-8">
    <title>Perfil</title>
    <%@ include file="navbar.jsp" %>
    <!-- Bootstrap -->
    <%@ include file="../html/imports.html"%>
</head>

<%  String nick = null;
    String liga = null;
    String email = null;
    String nombre = null;
    String apellidos = null;
    int puesto = 0;
    int puntos = 0;
    int monedas = 0;
    int jugadas = 0;
    int ganadas = 0;
    int perdidas = 0;
    int ratio = 0;

    if (session.getAttribute("userId") == null) {
        response.sendRedirect("/jsp/login.jsp");
    } else {
        UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
        nick = usuarioVO.getUsername();
        email = usuarioVO.getCorreo();
        nombre = usuarioVO.getNombre();
        apellidos = usuarioVO.getApellidos();

        StatsUsuarioVO statsVO = (StatsUsuarioVO) session.getAttribute("userStats");
        liga = statsVO.getLigaActual();
        puesto = statsVO.getPuesto();
        puntos = statsVO.getPuntuacion();
        monedas = statsVO.getDivisa();
        ganadas = statsVO.getGanadas();
        perdidas = statsVO.getPerdidas();
        jugadas = ganadas + perdidas;
        if(perdidas != 0){
            ratio = ganadas/perdidas;
        }

    }%>

<body>
    <div class="container">
        <div class="row">
            <div class="col-md-4 mt-4">
                <img class="img-thumbnail" style="width: 400px" src="#" alt="imagen usuario">
            </div>
            <div class="col-md-6 mt-4">
                <h1> <%= nick %> </h1>
                <h3>Liga <%= liga %> (<%= puesto %>º)
                </h3>
                <h3><%= puntos %> ptos. / <%= monedas %> monedas</h3>
                <h3><%= jugadas %> partidas jugadas
                </h3>
                <h3><%= ganadas %> ganadas / <%= perdidas %> perdidas (ratio G/P <%= ratio %>)</h3>
                <button class="btn btn-primary mt-2" role="button"
                    data-toggle="modal" data-target="#myModal">
                    <i class="fa fa-pencil mr-2" aria-hidden="true"></i>Editar perfil
                </button>

                <!-- Modal modificar usuario -->
                <div id="myModal" class="modal fade" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content -->
                        <div class="modal-content">
                            <div class="modal-header text-center">
                                <h4 class="modal-title w-100 font-weight-bold">Editar perfil</h4>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body container">
                                <div class="row">
                                    <div class="col-sm">
                                        <form action="/LoginServlet.do" method="post" > <!-- TODO: Asegurar que funciona el form -->
                                            <div class="form-group">
                                                <label for="modEmail">E-Mail</label>
                                                <input type="email" class="form-control" id="modEmail" placeholder="<%= email %>">
                                            </div>
                                            <div class="form-group">
                                                <label for="modNombre">Nombre</label>
                                                <input type="text" class="form-control" id="modNombre" placeholder="<%= nombre %>">
                                            </div>
                                            <div class="form-group">
                                                <label for="modApellidos">Apellidos</label>
                                                <input type="text" class="form-control" id="modApellidos" placeholder="<%= apellidos %>">
                                            </div>
                                            <div class="form-group">
                                                <label for="modPass">Nueva contraseña</label>
                                                <input type="password" class="form-control" id="modPass" >
                                            </div>
                                            <div class="form-group">
                                                <label for="modOldPass">Contraseña actual</label>
                                                <input type="password" class="form-control" id="modOldPass"
                                                       placeholder="Introduce tu contraseña actual" required>
                                            </div>
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fa fa-save mr-2" aria-hidden="true"></i>Guardar</button>
                                        </form>
                                    </div>
                                    <div class="col-sm">
                                        <a>Columna</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <a class="btn btn-danger mt-2" href="/BorrarUsuario.do" role="button">
                    <i class="fa fa-trash mr-2" aria-hidden="true"></i>Borrar cuenta
                </a>
                <% if(session.getAttribute("isAdmin") != null){ %>
                <a class="btn btn-danger mt-2" href="/MostrarObjetosTienda.do" role="button">
                    <i class="fa fa-shopping-cart mr-2" aria-hidden="true"></i>Gestionar Tienda
                </a>
                <!-- TODO: añadir aqui resto de botones del admin -->
                <% } %>
            </div>
        </div>
    </div>
</body>
</html>
