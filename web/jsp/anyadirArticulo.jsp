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

<%  UsuarioVO usuarioVO = null;
    StatsUsuarioVO statsVO = null;
    String nick = null;
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
    usuarioVO = (UsuarioVO) session.getAttribute("userId");

    if ( session.getAttribute("userId") == null) {
        String error = "userNotFound";
        request.setAttribute("error", error);
        response.sendRedirect("/jsp/login.jsp");
    } else {
        usuarioVO = (UsuarioVO) session.getAttribute("userId");
        if (!usuarioVO.getAdmin()){
            String error = "adminNotFound";
            request.setAttribute("error", error);
            response.sendRedirect("/jsp/login.jsp");
        }
        //TODO: get ligas
    }%>


<body>
<div class="container">
    <div class="row">
        <div class="col-md-6 mt-4">
            <h1 class="text-xl-left"> Añadir artículo a la tienda  </h1>
            <form action="/AnyadirArticuloTienda.do" method="post" >
                <div class="form-group">
                    <label for="modNombre">Nombre</label>
                    <input type="text" class="form-control" id="modNombre" name="modNombre" placeholder="Escriba el nombre del artículo">
                </div>
                <div class="form-group">
                    <label for="modPrecio">Precio</label>
                    <input type="number" class="form-control" id="modPrecio" name="modPrecio" placeholder="Introduzca el precio del artículo">
                </div>
                <div class="form-group">
                    <label for="modDesc">Descripción</label>
                    <input type="text" class="form-control" id="modDesc" name="modDesc">
                </div>
                <div class="form-group">
                    <label for="tipo">Tipo del artículo</label>
                    <select id="tipo" name="tipo" class="form-control">
                        <option value="T">Tapete</option>
                        <option value="B">Baraja</option>
                        <option value="A">Avatar</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="imagen">Imagen</label>
                    <input type="file" class="form-control-file" id="imagen" name="imagen">
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="fa fa-save mr-2" aria-hidden="true"></i>Guardar</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
