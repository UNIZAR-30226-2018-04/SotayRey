<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.TorneoVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html lang="en" >
<head>
    <title>Torneos</title>
    <meta charset="utf-8">
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap -->
    <%@ include file="navbar.jsp" %>
    <%@ include file="../html/imports.html"%>

</head>
<body>

<style type="text/css">
    .jumbotron{
        color: black;
        background-size: cover;
    }
</style>

<div class="jumbotron">
    <div class="container text-center">
        <h1>Torneos</h1>
        <p>Ap&uacutentate y demuestra ser el mejor</p>
    </div>
</div>

<% ArrayList<TorneoVO> lista = (ArrayList<TorneoVO>) session.getAttribute("torneos");
   if(lista == null){ %>

     <div class="container text-center">
        <h2>No hay torneos a los que puedas apuntarte. Vuelve en otro momento</h2>
     </div>

  <% } else {%>

    <div class="container">
        <div class="row">
            <div class="col-md-12">
                    <table class="table table-hover" id="rank-table">
                        <thead>
                        <tr>
                            <th>Nombre del torneo</th>
                            <th>Comienzo del torneo</th>
                            <th>N&uacutemero de fases</th>
                            <th>Premio en monedas al primer puesto</th>
                            <th>Premio en puntos al primer puesto</th>
                            <th>Participar</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for(Integer i = 0; i < lista.size(); i++){
                                TorneoVO torneo = lista.get(i);       %>
                            <tr>
                                <td><%=torneo.getNombre()%></td>
                                <td><%=torneo.getTimeInicio()%></td>
                                <td><%=torneo.getNumFases()%> </td>
                                <td><%=torneo.getPremioDivisaPrimera()%><td>
                                <td><%=torneo.getPremioPuntuacionPrimera()%><td>
                                <td>
                                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#unirseTorneo">Unirse</button>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
            </div>
        </div>
    </div>
  <% } %>


<div class="container">
    <!-- Modal -->
    <div class="modal fade" id="unirseTorneo" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title text-center">Esperando a que empiece...</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>

                <div class="modal-body text-center">
                    <p>Espere a la hora de inicio del torneo</p>
                    <img class="img-fluid" src="../img/esperandoTorneo.gif" alt="animacion cargando">
                </div>
                <div class="modal-footer text-center">
                    <button type="button" onclick="cerrarSocket()" class="btn btn-default" data-dismiss="modal">Cancelar búsqueda</button>
                </div>
            </div>

        </div>
    </div>

</div>


</body>
</html>
