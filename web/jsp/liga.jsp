<%@ page import="basedatos.modelo.LigaVO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.StatsUsuarioVO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Clasificaci&oacuten</title>
    <meta charset="iso-8859-1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- Bootstrap -->
    <%@ include file="../html/imports.html"%>
    <%@ include file="navbar.jsp" %>

    <style>
        a:hover,a:focus{
            outline: none;
            text-decoration: none;
        }
        .tab .nav-tabs{
            border: none;
            margin-bottom: 10px;
        }
        .tab .nav-tabs li a{
            padding: 10px 20px;
            margin-right: 15px;
            background: #f8333c;
            font-size: 17px;
            font-weight: 600;
            color: #fff;
            text-transform: uppercase;
            border: none;
            border-top: 3px solid #f8333c;
            border-bottom: 3px solid #f8333c;
            border-radius: 0;
            overflow: hidden;
            position: relative;
            transition: all 0.3s ease 0s;
        }
        .tab .nav-tabs li.active a,
        .tab .nav-tabs li a:hover{
            border: none;
            border-top: 3px solid #f8333c;
            border-bottom: 3px solid #f8333c;
            background: #fff;
            color: #f8333c;
        }
        .tab .nav-tabs li a:before{
            content: "";
            border-top: 15px solid #f8333c;
            border-right: 15px solid transparent;
            border-bottom: 15px solid transparent;
            position: absolute;
            top: 0;
            left: -50%;
            transition: all 0.3s ease 0s;
        }
        .tab .nav-tabs li a:hover:before,
        .tab .nav-tabs li.active a:before{ left: 0; }
        .tab .nav-tabs li a:after{
            content: "";
            border-bottom: 15px solid #f8333c;
            border-left: 15px solid transparent;
            border-top: 15px solid transparent;
            position: absolute;
            bottom: 0;
            right: -50%;
            transition: all 0.3s ease 0s;
        }
        .tab .nav-tabs li a:hover:after,
        .tab .nav-tabs li.active a:after{ right: 0; }
        .tab .tab-content{
            padding: 20px 30px;
            border-top: 3px solid #384d48;
            border-bottom: 3px solid #384d48;
            font-size: 17px;
            color: #384d48;
            letter-spacing: 1px;
            line-height: 30px;
            position: relative;
        }
        .tab .tab-content:before{
            content: "";
            border-top: 25px solid #384d48;
            border-right: 25px solid transparent;
            border-bottom: 25px solid transparent;
            position: absolute;
            top: 0;
            left: 0;
        }
        .tab .tab-content:after{
            content: "";
            border-bottom: 25px solid #384d48;
            border-left: 25px solid transparent;
            border-top: 25px solid transparent;
            position: absolute;
            bottom: 0;
            right: 0;
        }
        .tab .tab-content h3{
            font-size: 24px;
            margin-top: 0;
            text-align: center;
        }
        @media only screen and (max-width: 479px){
            .tab .nav-tabs li{
                width: 100%;
                text-align: center;
                margin-bottom: 15px;
            }
        }
    </style>


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
            <h1>Clasificacion</h1>
            <p>Comprueba el puesto en el que vas. ¡Lucha por ser el campeon!</p>
        </div>
    </div>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="tab" role="tabpanel">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#Section1" aria-controls="home" role="tab" data-toggle="tab"><%= ligas.get(0).getNombre()%></a></li>
                    <% for(Integer i = 1; i < ligas.size(); i++){ %>
                     <li role="presentation"><a href= "<%= "#Section" + i.toString() %>" aria-controls="profile" role="tab" data-toggle="tab"><%= ligas.get(i).getNombre()%></a></li>
                    <% } %>
                </ul>
                <!-- Tab panes -->
                <div class="tab-content tabs">
                    <div role="tabpanel" class="tab-pane fade in active" id="Section0">
                        <h3><%=ligas.get(0).getNombre()%></h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce semper, magna a ultricies volutpat, mi eros viverra massa, vitae consequat nisi justo in tortor. Proin accumsan felis ac felis dapibus, non iaculis mi varius.</p>
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
                                        for(StatsUsuarioVO user : clasificacion){
                                    %>
                                    <tr>
                                        <td><%= user.getPuesto() %></td>
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
                        <h3><%=ligas.get(i).getNombre()%></h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce semper, magna a ultricies volutpat, mi eros viverra massa, vitae consequat nisi justo in tortor. Proin accumsan felis ac felis dapibus, non iaculis mi varius.</p>
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
                                            <%  clasificacion = (ArrayList<StatsUsuarioVO>) session.getAttribute(ligas.get(0).getNombre());
                                                for(StatsUsuarioVO user : clasificacion){
                                            %>
                                                <tr>
                                                    <td><%= user.getPuesto() %></td>
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
