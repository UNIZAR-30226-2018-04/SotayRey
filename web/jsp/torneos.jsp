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
        <p>Apúntate y demuestra ser el mejor</p>
    </div>
</div>


<div class="container">
    <div class="row">
        <div class="col-md-12">
                <table class="table table-hover" id="rank-table">
                    <thead>
                    <tr>
                        <th>Comienzo del torneo</th>
                        <th>Tiempo restante</th>
                        <th>Participantes</th>
                        <th>Participar</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>05/05/2018 20:00</td>
                        <td>
                            Nada todavía
                        </td>
                        <td>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-striped active" role="progressbar"
                                         aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:40%">
                                        40/100
                                    </div>
                                </div>
                        </td>
                        <td>
                            <button type="button" class="btn btn-success" data-toggle="modal" data-target="#unirseTorneo">Unirse</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Bob</td>
                        <td>100</td>
                        <td>200</td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Holden</td>
                        <td>50</td>
                        <td>20</td>
                    </tr>
                    </tbody>
                </table>
        </div>
    </div>
</div>


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
