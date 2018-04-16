<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.*" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html lang="en" >
<head>
    <title>Tienda</title>
    <%@ include file="navbar.jsp" %>
    <!-- Bootstrap -->
    <%@ include file="../html/imports.html"%>
    <%--
    //TODO: todo esto hay que meterlo en el war
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    --%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<%
    int monedas = 0;
    String error;
    ArrayList<ArticuloUsuarioVO> articulos;

    UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
    StatsUsuarioVO statsVO = (StatsUsuarioVO) session.getAttribute("userMainStats");

    if (usuarioVO == null || statsVO == null) {
        error = "userNotFound";
        session.setAttribute("error", error);
        response.sendRedirect("/jsp/login.jsp");
    } else {
        try {
            monedas = statsVO.getDivisa();
        } catch (NullPointerException e){
            e.printStackTrace();
        } %>
<body>

    <style type="text/css">
        .jumbotron{
            background-image: url('../img/fondoCabeceraTienda.jpg');
            color: white;
            background-size: cover;
        }
    </style>

    <div class="jumbotron" >
        <div class="container text-center">
            <h1>Tienda</h1>
            <p>Aquí podrás encontrar todo tipo de cosas para ser el más chulo en el guiñote. ¡No seas rata y compra
                algo para marcar estilo!</p>
            <p> Mis monedas: <%= monedas %></p>
        </div>
    </div>

    <%
        articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
        if (articulos == null){ %>
            <div class="container-fluid bg-3 text-center">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Tienda sin articulos</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div><br>
        <% } else { %>


<%--TODO: HACER FAVORITOS,  --%>
    <div class="container-fluid bg-3 text-center">
        <% for (String tipo: new ArrayList<>(Arrays.asList("Barajas", "Tapetes", "Avatares"))) {

        %>

        <h3><%=tipo%></h3><br>
        <div class="row equal">

            <%  char t = tipo.charAt(0);
                for (ArticuloUsuarioVO art: articulos) {
                if (art.getTipo() == t){
                LigaVO liga = art.getRequiere(); %>
                <div class="col-sm-4">
                    <div class="panel panel-primary">
                        <div class="panel-heading"> <%= art.getNombre() %></div>
                        <div class="panel-body"><img src= "<%=art.getRutaImagen()%>" class="img-responsive" style="width:100%" alt="Imagen baraja <%= art.getNombre()%>"></div>
                        <% if (liga != null){ %>
                            <div class="panel-footer">Se desbloquea al alcanzar la liga <%=liga.getNombre()%></div>
                        <% } else { %>
                            <div class="panel-footer">Desbloqueado desde el inicio </div>
                        <% } if (art.isComprado()) { %>
                            <button class="btn btn-success">Ya adquirido</button>
                        <% } else if (art.isDisponible()) { %>
                        <!--
                        <a action="/BorrarUsuario.do" method="post" class="btn btn-danger mt-2" href="/ComprarObjetoServlet.do" role="button">
                            <i class="fa fa-trash mr-2" aria-hidden="true"></i>Borrar cuenta
                        -->
                            <button class="btn btn-primary">Comprar: <%= art.getPrecio()%> monedas</button>
                        <% } else { %>
                            <button class="btn btn-danger">Artículo bloqueado</button>
                        <% } %>
                    </div>
                </div>
            <%  }
            } %>
        </div>
        <% } %>
    </div><br>
    <%  }
    } %>

</body>
</html>
