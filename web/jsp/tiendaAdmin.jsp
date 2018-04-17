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
    --%>
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

    <style type="text/css">
        .jumbotron{
            background-image: url('../img/fondoCabeceraTienda.jpg');
            color: white;
            background-size: cover;
        }
    </style>

    <div class="jumbotron">
        <div class="container text-center">
            <h1>Tienda</h1>
            <p>Aqui puedes crear objetos y editar los ya existentes</p>
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
                int i = 0;
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
                                <button  type="button" class="btn btn-success">Ya adquirido</button>
                            <% } else if (art.isDisponible()) { %>

                                <!-- Comprar objeto -->
                                <form action="/ComprarObjetoServlet.do" method="post">
                                    <input type="hidden" value="<%=i%>" name="id_objeto"/>
                                    <input type="submit" class="btn btn-primary" value="Comprar: <%= art.getPrecio()%> monedas"/>
                                </form>
                            <% } else { %>
                                <button  type="button"  class="btn btn-blue-grey">Artículo bloqueado</button>
                            <% } %>
                        </div>
                    </div>
            <%  }
                ++i;
            } %>
        </div>
        <% } %>
    </div><br>
    <%  }
    } %>

</body>
</html>
