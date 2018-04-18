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


    <meta charset="utf-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>

<%
    int monedas = 0;
    boolean admin = false;
    String error;
    String accionAdmin = "";
    ArrayList<ArticuloUsuarioVO> articulos;

    UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
    StatsUsuarioVO statsVO = (StatsUsuarioVO) session.getAttribute("userMainStats");

    if (usuarioVO == null || statsVO == null) {
        error = "userNotFound";
        session.setAttribute("error", error);
        response.sendRedirect("/jsp/login.jsp");
    } else {
        admin = usuarioVO.getAdmin();
        if (admin){
            accionAdmin = (String) session.getAttribute("accionAdmin");
            if (accionAdmin == null){
                accionAdmin = "nada";
            }
        }
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

    <div class="jumbotron">
        <div class="container text-center">
            <h1>Tienda</h1>
            <p>Aqu&iacute podr&aacutes encontrar todo tipo de cosas para ser el m&aacutes chulo en el gui&ntildeote. ¡No seas rata y compra
            algo para marcar estilo!</p>
            <p> Mis monedas: <%= monedas%> </p>
        </div>
    </div>

    <%
        articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
        if (admin){ %>
        <div class="container">
            <h2>Opciones de administrador</h2>

                <div class="btn-toolbar">
                    <button class="btn btn-warning btn-md disabled" role="button"
                            data-toggle="modal" data-target="#anyadirArticulo">
                        <i aria-hidden="true"></i>Añadir artículo
                    </button>
                   <!-- <a href="#" class="btn btn-warning btn-md">Añadir Artículo</a> -->
                    <% if (articulos == null || articulos.size() == 0){ %>
                    <a class="btn btn-warning btn-md disabled">Modificar Articulo</a>
                    <a class="btn btn-warning btn-md disabled">Eliminar Artículo</a>
                    <% } else { %>
                    <form action="RefrescarTiendaAdmin.do" method="post">
                        <input type="hidden" name="optionAdmin" value="modify">
                        <input type="submit" class="btn btn-warning btn-md"
                               value="Modificar Articulo">
                    </form>
                    <form action="/RefrescarTiendaAdmin.do" method="post">
                        <input type="hidden" name="optionAdmin" value="delete">
                        <input type="submit" class="btn btn-warning btn-md"
                               value="Eliminar Articulo">
                    </form>
                    <% } %>
                </div>
            </form>
        </div>


    <% }
        if (articulos == null || articulos.size() == 0){ %>
            <div class="container-fluid bg-3 text-center">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Tienda sin articulos</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div><br>
        <% } else { %>

                <!-- hay articulos -->
                <div class="container-fluid bg-3 text-center">
                    <div id="accordion" role="tablist"> <!-- acordion -->
                    <%  for (String tipo: new ArrayList<>(Arrays.asList("Barajas", "Tapetes", "Avatares"))) {  %>
                            <div class="card">
                                <div class="card-header" role="tab" id="head<%=tipo%>">
                                    <h5 class="mb-0">
                                        <a data-toggle="collapse" href="#collapse<%=tipo%>" aria-expanded="true" aria-controls="collapse<%=tipo%>">
                                            <%=tipo%>
                                        </a>
                                    </h5>
                                </div>
                                <div id="collapse<%=tipo%>" class="collapse show" role="tabpanel" aria-labelledby="head<%=tipo%>">
                                    <div class="card-body"> <!-- contenido de cada tipo -->
                                        <form action="/ComprarObjeto.do" method="post">
                                            <div class="row equal">

                                                <%  char t = tipo.charAt(0);
                                                    int i = 0;
                                                    for (ArticuloUsuarioVO art: articulos) {

                                                        if (art.getTipo() == t){
                                                            if (usuarioVO.getAdmin() && !accionAdmin.equals("nada")){ %>
                                                                <div class="col-sm-4">
                                                                    <div class="panel panel-primary">
                                                                        <div class="panel-heading"> <%= art.getNombre() %></div>
                                                                        <div class="panel-body"><img src= "<%=art.getRutaImagen()%>" class="img-responsive" style="width:100%" alt="Imagen baraja <%= art.getNombre()%>"></div>
                                                                        <% if (accionAdmin.equals("eliminar")){ %>
                                                                        <!-- Eliminar objeto -->
                                                                        <form action="/EliminarObjetoTienda.do" method="post">
                                                                            <input type="hidden" value="<%=i%>" name="id_objeto"/>
                                                                            <input type="submit" class="btn btn-primary" value="Eliminar"/>
                                                                        </form>
                                                                        <% } else if (accionAdmin.equals("modificar")){ %>
                                                                        <!-- Eliminar objeto -->
                                                                        <form action="/ModificarObjetoTienda.do" method="post">
                                                                            <input type="hidden" value="<%=i%>" name="id_objeto"/>
                                                                            <input type="submit" class="btn btn-primary" value="Modificar"/>
                                                                        </form>
                                                                        <% } %>
                                                                    </div>
                                                                </div>
                                                        <%  } else { // No es admin

                                                                LigaVO liga = art.getRequiere(); %>

                                                                <div class="col-sm-4">
                                                                    <div class="panel panel-primary">
                                                                        <div class="panel-heading"> <%= art.getNombre() %></div>
                                                                        <div class="panel-body"><img src= "<%=art.getRutaImagen()%>" class="img-responsive card-img-top mt-4" style="width:100%" alt="Imagen baraja <%= art.getNombre()%>"></div>
                                                                        <% if (liga != null){ %>
                                                                        <div class="panel-footer">Se desbloquea al alcanzar la liga <%=liga.getNombre()%></div>
                                                                        <% } else { %>
                                                                        <div class="panel-footer">Desbloqueado desde el inicio </div>
                                                                        <% } if (art.isComprado()) { %>
                                                                        <button  type="button" class="btn btn-success">Ya adquirido</button>
                                                                        <input type="radio" name="id_objeto" value="<%=i%>"
                                                                            <% if (art.isFavorito()){ %>
                                                                               checked
                                                                            <% } %>
                                                                        >
                                                                    <%  } else if (art.isDisponible()) { %>

                                                                        <!-- Comprar objeto -->
                                                                        <form action="/ComprarObjeto.do" method="post">
                                                                            <input type="hidden" value="<%=i%>" name="id_objeto"/>
                                                                            <input type="submit" class="btn btn-primary" value="Comprar: <%= art.getPrecio()%> monedas"/>
                                                                        </form>
                                                                        <% } else { %>
                                                                        <button  type="button"  class="btn btn-blue-grey">Artículo bloqueado</button>
                                                                        <% } %>
                                                                    </div>
                                                                </div>
                                                        <%  }
                                                     }
                                                    ++i;
                                                } %>
                                            </div>
                                        </form> <!-- final form favorito -->
                                        <!-- fin contenido de cada tipo -->
                                    </div>
                                </div>
                            </div>
                    <%  } %>
                    </div>
                </div><br>
        <%  } // else hay articulos
    } //else user != null %>

</body>
</html>
