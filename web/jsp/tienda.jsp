<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.ArticuloUsuarioVO" %>
<%@ page import="basedatos.modelo.StatsUsuarioVO" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="basedatos.modelo.LigaVO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="iso-8859-1">
    <title>Tienda</title>
    <%@ include file="navbar.jsp" %>
    <!-- Bootstrap -->
    <%@ include file="../html/imports.html"%>
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

    <!-- Es admin -->
    <div class="card">
        <h2>Opciones de administrador</h2>

        <div class="btn-toolbar">
            <button class="btn btn-warning btn-md disabled" role="button"
                    data-toggle="modal" data-target="#anyadirArticulo">
                <i aria-hidden="true"></i>Añadir artículo
            </button>
            <a href="/RefrescarTiendaAdmin.do" class="btn btn-warning btn-md">
                <i aria-hidden="true"></i>Comprar Articulos
            </a>
            <!-- <a href="#" class="btn btn-warning btn-md">Añadir Artículo</a> -->
            <% if (articulos == null || articulos.size() == 0){ %>
            <a class="btn btn-warning btn-md disabled">Modificar Artículo</a>
            <% } else { %>
            <form action="/RefrescarTiendaAdmin.do" method="post">
                <input type="hidden" name="optionAdmin" value="modify">
                <input type="submit" class="btn btn-warning btn-md"
                       value="Modificar Articulo">
            </form>
            <% } %>
        </div>
        </form>
    </div>


    <% }
        if (articulos == null || articulos.size() == 0){ %>
            <%-- Hay articulos que mostrar--%>

            <div class="container-fluid bg-3 text-center">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Tienda sin articulos</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div><br>

    <% } else { %>

        <%-- Hay articulos que mostrar--%>
        <div id="accordion" role="tablist" aria-multiselectable="true">
          <%for (String tipo: new ArrayList<>(Arrays.asList("Barajas", "Tapetes", "Avatares"))) {  %>
                <div class="card mt-2">
                    <div class="card-header" role="tab" id="heading<%=tipo%>">
                        <h5 class="mb-0">
                            <a class="text-center" data-toggle="collapse" data-parent="#accordion" href="#collapse<%=tipo%>" aria-expanded="true" aria-controls="collapse<%=tipo%>">
                                <h4 class="text-center"> <%=tipo%> </h4>
                            </a>
                        </h5>
                    </div>
                    <div id="collapse<%=tipo%>" class="collapse show" role="tabpanel" aria-labelledby="heading<%=tipo%>">
                        <form class="form" action="/ComprarObjeto.do" method="post">
                            <div class="row text-center">
                            <%  char t = tipo.charAt(0);

                                int i = 0;
                                for (ArticuloUsuarioVO art: articulos) {

                                    if (art.getTipo() == t){
                                        if (usuarioVO.getAdmin() && accionAdmin.equals("modificar")){ %>
                                            <div class="card col-sm-4">
                                                <div class="card-footer text-center bg-primary text-white">
                                                    <%=art.getNombre()%>
                                                </div>
                                                <img class="card-img-top" src="<%=art.getRutaImagen()%>" alt="Card image cap">
                                                <%--<form action="/ModificarObjetoTienda.do" method="post">--%>
                                                    <%--<input id="price" class="text-center" value="a" type="text">--%>
                                                    <%--<input type="submit" id="btnEnviar">--%>
                                                <%--</form>--%>

                                                <!-- Button trigger modal Modificar-->
                                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modificarArticulo">
                                                    <i class="fa fa-pencil mr-2" aria-hidden="true"></i>Modifcar
                                                </button>

                                                <!-- Modal Modificar articulo -->
                                                <div class="modal" id="modificarArticulo" tabindex="-1" role="dialog" aria-labelledby="modificarArticuloLabel" aria-hidden="true">
                                                        <div class="modal-dialog" role="document">
                                                                        <form action="/ModificarObjetoTienda.do" method="post">
                                                            <div class="modal-content">
                                                                <div class="modal-header text-center">
                                                                    <h4 class="modal-title w-100 font-weight-bold">Modificar artículo</h4>
                                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                        <span aria-hidden="true">&times;</span>
                                                                    </button>
                                                                </div>
                                                                <div class="modal-body  container">
                                                                    <div class="col-sm">
                                                                            <div class="form-group">
                                                                                <label for="precio">Precio</label>
                                                                                <input type="number"  class="form-control" name="precio" id="precio"
                                                                                       placeholder="Introduzca el nuevo valor" >
                                                                                <input hidden id="objeto" name="posObjeto" value="<%=i%>">
                                                                            </div>
                                                                                <%--<input type="submit" class="btn btn-primary" value="Guardar Cambios"/>--%>
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <input type="submit" class="btn btn-primary" value="Guardar Cambios"/>
                                                                        <input type="reset" class="btn btn-secondary" data-dismiss="modal" value="Cerrar"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                                        </form>
                                                        </div>
                                                </div>
                                             </div>
                                      <%} else {

                                            //Usuario o admin accionando como usuario

                                        LigaVO liga = art.getRequiere(); %>
                                            <div class="card col-sm-4">
                                                <div class="card-footer text-center bg-primary text-white">
                                                    <%=art.getNombre()%>
                                                </div>
                                                <img class="card-img-top" src="<%=art.getRutaImagen()%>" alt="Card image cap">
                                                <div class="card-footer" >
                                                    <% if (liga != null){ %>
                                                    Se desbloquea al alcanzar la liga <%=liga.getNombre()%>
                                                    <% } else { %>
                                                    Desbloqueado
                                                    <% } %>
                                                </div>
                                                <% if (art.isComprado()) { %>
                                                    <div class="text-center">
                                                    <button  type="button" class="btn btn-success disabled">Ya adquirido</button>
                                                    <input type="radio" aria-label="fav" disabled="disabled"
                                                        <% if (art.isFavorito()){ %>
                                                           checked="checked" >
                                                            <button type="submit" class="btn btn-primary disabled"> Favorito </button>
                                                        <%} else { %>
                                                            >
                                                            <button type="submit" class="btn btn-primary" name="id_objeto" value="<%=i%>"> Favorito </button>
                                                        <%}%>
                                                    </div>
                                                    <% } else if (art.isDisponible()) { %>
                                                        <!-- Comprar objeto -->
                                                        <form action="/ComprarObjeto.do" method="post">
                                                            <input type="hidden" value="<%=i%>" name="posObjeto"/>
                                                            <input type="submit" class="btn btn-primary" value="Comprar: <%= art.getPrecio()%> monedas"/>
                                                        </form>
                                                  <%} else { %>
                                                        <button  type="button"  class="btn btn-blue-grey">Artículo bloqueado</button>
                                                  <%} %>
                                            </div>
                                      <%}
                                    }
                                    ++i;
                                }%>
                            </div>
                        </form>
                    </div>
                </div>
          <%} %>
        </div><br> <!-- fin acordion -->

    <%  } // else hay articulos
    } //else user != null %>

</body>
</html>