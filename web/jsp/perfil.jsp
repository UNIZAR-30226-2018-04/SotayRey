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

        //TODO: resto de cosas
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
                <a class="btn btn-primary mt-2" href="../jsp/modificar_datos.jsp" role="button">
                    <i class="fa fa-pencil mr-2" aria-hidden="true"></i>Editar perfil
                </a>
                <a action="/BorrarUsuario.do" method="post" class="btn btn-danger mt-2" href="/BorrarUsuario.do" role="button">
                    <i class="fa fa-trash mr-2" aria-hidden="true"></i>Borrar cuenta
                </a>
            </div>
        </div>
    </div>
</body>
</html>
