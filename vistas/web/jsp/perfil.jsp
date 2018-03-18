<%@ page import="seeker.modelo.datos.UsuarioVO" %><%--
  Created by IntelliJ IDEA.
  User: cmarius9
  Date: 18/10/17
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Perfil</title>
    <%@ include file="navbar.jsp" %>
    <!-- Bootstrap -->
    <%@ include file="../html/bootstrap4.html"%>
</head>
<% String nick = null;
    String aps = null;
    String nombre = null;
    String email = null;
    String foto = null;
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("../jsp/login.jsp");
    } else {
        UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("usuario");
        nick = usuarioVO.getNick();
        nombre = usuarioVO.getNombre();
        aps = usuarioVO.getApellidos();
        email = usuarioVO.getEmail();
        foto = usuarioVO.getImagen();
    }%>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-4 mt-4">
                <img class="img-thumbnail" style="width: 400px" src="../img/<%=foto%>" alt="imagen usuario">
            </div>
            <div class="col-md-6 mt-4">
                <h1>@<%=nick%>
                </h1>
                <h3><%=nombre%> <%=aps%>
                </h3>
                <h3>España</h3>
                <h3><%=email%>
                </h3>
                <a class="btn btn-primary mt-2" href="/MostrarPedidos.do" role="button">
                    <i class="fa fa-archive mr-2" aria-hidden="true"></i>Ver pedidos
                </a>
                <a class="btn btn-primary mt-2" href="/MostrarCesta.do" role="button">
                    <i class="fa fa-shopping-basket mr-2" aria-hidden="true"></i>Ver cesta
                </a>
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
