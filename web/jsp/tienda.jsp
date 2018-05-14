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

    UsuarioVO usuarioVO;
    StatsUsuarioVO statsVO;

    if (session.getAttribute("userId") == null || (StatsUsuarioVO) session.getAttribute("userMainStats") == null) {
        error = "userNotFound";
        session.setAttribute("error", error);
        response.sendRedirect("/jsp/login.jsp");
    } else {
        usuarioVO = (UsuarioVO) session.getAttribute("userId");
        statsVO = (StatsUsuarioVO) session.getAttribute("userMainStats");
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
    <% error = (String) request.getAttribute("error");
    if (error != null){%>

        <div class="alert alert-danger alert-dismissible fade show" role="alert">
         <% if (error.equals("objectNotFound")){ %>
                <strong>Objeto incorrecto </strong>Int&eacutentalo de nuevo.
          <% }  else if (error.equals("noMoney")){ %>
                <strong>No dispones del dinero suficiente </strong>
            <% } else{ %>
                <strong>No hay ninguna liga </strong> A&ntilde;ade una liga o inicia sesi&oacuten.
          <% }  %>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <% } %>
    <%
        articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
        if (admin){ %>

    <!-- Es admin -->
    <div class="card">
        <h2>Opciones de administrador</h2>

        <div class="btn-toolbar">
            <a href="/jsp/anyadirArticulo.jsp" class="btn btn-warning btn-md my-1 mx-1">
                <i aria-hidden="true"></i>A&ntilde;adir Articulos
            </a>
            <a href="/RefrescarTiendaAdmin.do" class="btn btn-warning btn-md my-1 mx-1">
                <i aria-hidden="true"></i>Comprar Articulos
            </a>
            <!-- <a href="#" class="btn btn-warning btn-md">Añadir Artículo</a> -->
            <% if (articulos == null || articulos.size() == 0){ %>
            <a class="btn btn-warning btn-md disabled my-1 mx-1">Modificar Art&iacuteculo</a>
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
                <div class="row list-group my-4">
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
                                                <style>
                                                    .image-holder {
                                                    background-size: cover;
                                                    background-position: center center;
                                                    height: 400px;
                                                    width: 100%;
                                                }
                                                .box {
                                                    border: 1px solid #ddd;
                                                    display: inline-flex;
                                                    align-items: center;
                                                    justify-content: center;
                                                    flex: 1 50%;
                                                    min-width: 200px;
                                                }
                                                </style>

                                                <div class="col-lg-6 box">
                                                    <img class="responsive" src="<%=art.getRutaImagen()%>" alt="Card image cap" width="600" height="400">
                                                </div>
                                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modificarArticulo<%=i%>" >
                                                    <i class="fa fa-pencil mr-2" aria-hidden="true"></i>Modifcar
                                                </button>

                                                <!-- Modal Modificar articulo -->
                                                <div class="modal" id="modificarArticulo<%=i%>" tabindex="-1" role="dialog" aria-labelledby="modificarArticuloLabel" aria-hidden="true">
                                                    <div class="modal-dialog" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header text-center">
                                                                <h4 class="modal-title w-100 font-weight-bold">Modificar art&iacuteculo</h4>
                                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                    <span aria-hidden="true">&times;</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body  container">
                                                                <div class="col-sm">
                                                                                <label for="precio">Precio</label>
                                                                                <input type="number" class="form-control" name="precio<%=i%>" id="precio"
                                                                                       placeholder="<%=art.getPrecio()%>" value="<%=art.getPrecio()%>">
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="submit" class="btn btn-primary" value="<%=i%>" id="indiceObjeto<%=i%>" name="indiceObjeto" formaction="/ModificarObjetoTienda.do" >Guardar Cambios</button>
                                                                    <button type="reset" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                                                                </div>
                                                            </div>
                                                        </div>
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
                                                            <button type="submit" class="btn btn-primary" name="id_objeto" id="id_objeto" value="<%=i%>"> Favorito </button>
                                                        <%}%>
                                                    </div>
                                                    <% } else if (art.isDisponible()) { %>
                                                        <!-- Comprar objeto -->
                                                        <button type="submit" class="btn btn-primary" value="<%=i%>" name ="id_objeto" id="id_objeto"> Comprar: <%= art.getPrecio()%> monedas </button>
                                                  <%} else { %>
                                                        <button  type="button"  class="btn btn-blue-grey">Art&iacuteculo bloqueado</button>
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