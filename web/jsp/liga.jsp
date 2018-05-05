<%@ page import="basedatos.modelo.LigaVO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.StatsUsuarioVO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Clasificaci&oacuten</title>
    <meta charset="iso-8859-1">
    <!-- Bootstrap -->
    <%@ include file="../html/imports.html"%>
    <%@ include file="navbar.jsp" %>


</head>
<body>

    <%
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("/jsp/login.jsp");
        }

        //TODO: Capturar nombre de usuario para mostrarlo en verde

        ArrayList<LigaVO> ligas = (ArrayList<LigaVO>) session.getAttribute("ligas");
        if(ligas == null){
            //TODO: Error?
            response.sendRedirect("/jsp/home.jsp");
        }

        if(ligas.size() == 0){
            //TODO: Error?
            response.sendRedirect("/jsp/home.jsp");
        }

    %>

    <style type="text/css">
        .jumbotron{
            color: black;
            background-size: cover;
        }
    </style>

    <div class="jumbotron">
        <div class="container text-center">
            <h1>Clasificaci&oacuten</h1>
            <p>Comprueba el puesto en el que vas.</p>
        </div>
    </div>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="tab" role="tabpanel">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" href="#Section0" role="tab" data-toggle="tab"><%= ligas.get(0).getNombre()%></a>
                    </li>
                    <% for(Integer i = 1; i < ligas.size(); i++){ %>
                    <li class="nav-item">
                        <a class="nav-link" href="#Section<%= i.toString() %>" role="tab" data-toggle="tab"><%= ligas.get(i).getNombre()%></a>
                    </li>
                    <% } %>
                </ul>
                <!-- Tab panes -->
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane fade in active" id="Section0">
                        <br>
                        <h3><%=ligas.get(0).getNombre()%></h3>
                        <br>
                        <p><%= ligas.get(0).getDescripcion()%></p>
                        <br>
                        <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="panel panel-primary">
                                <table class="table table-hover" id="rank-table0">
                                    <thead>
                                    <tr>
                                        <th>Posicion</th>
                                        <th>Usuario</th>
                                        <th>Puntos</th>
                                        <th>Monedas</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <%  ArrayList<StatsUsuarioVO> clasificacion = (ArrayList<StatsUsuarioVO>) session.getAttribute(ligas.get(0).getNombre());
                                        for(Integer j = 1; j < clasificacion.size(); j++){
                                            StatsUsuarioVO user = clasificacion.get(j);
                                    %>
                                    <tr>
                                        <td><%= j.toString() %></td>
                                        <td><%= user.getUsername() %></td>
                                        <td><%= user.getPuntuacion()%></td>
                                        <td><%= user.getDivisa()%></td>
                                    </tr>
                                    <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
                    <% for(Integer i = 1; i < ligas.size(); i++) { %>
                    <div role="tabpanel" class="tab-pane fade" id="Section<%=i.toString()%>">
                        <br>
                        <h3><%=ligas.get(i).getNombre()%></h3>
                        <br>
                        <p><%=ligas.get(i).getDescripcion()%></p>
                        <br>
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="panel panel-primary">
                                        <table class="table table-hover" id="rank-table<%=i.toString()%>">
                                            <thead>
                                            <tr>
                                                <th>Posicion</th>
                                                <th>Usuario</th>
                                                <th>Puntos</th>
                                                <th>Monedas</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <%  clasificacion = (ArrayList<StatsUsuarioVO>) session.getAttribute(ligas.get(i).getNombre());
                                                for(Integer j = 1; j < clasificacion.size(); j++){
                                                    StatsUsuarioVO user = clasificacion.get(j);
                                            %>
                                                <tr>
                                                    <td><%= j.toString() %></td>
                                                    <td><%= user.getUsername() %></td>
                                                    <td><%= user.getPuntuacion()%></td>
                                                    <td><%= user.getDivisa()%></td>
                                                </tr>
                                            <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% }%>
                </div>
            </div>
        </div>
    </div>
</div>



</body>
</html>
