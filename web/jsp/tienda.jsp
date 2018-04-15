<%@ page import="basedatos.modelo.StatsUsuarioVO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Tienda</title>
    <meta charset="iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <%@ include file="navbar.jsp" %>
    <!-- Bootstrap -->
    <%@ include file="../html/imports.html"%>
</head>
<body>

<%
    int monedas = 0;

    if (session.getAttribute("userId") == null) {
        response.sendRedirect("/jsp/login.jsp");
    } else {
        StatsUsuarioVO statsVO = (StatsUsuarioVO) session.getAttribute("userStats");
        monedas = statsVO.getDivisa();
    }%>

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

    <div class="container-fluid bg-3 text-center">
        <h3>Barajas</h3><br>
        <div class="row equal">
            <div class="col-sm-4">
                <div class="panel panel-primary">
                    <div class="panel-heading">BARAJA DE ORO</div>
                    <div class="panel-body"><img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image"></div>
                    <div class="panel-footer">Se desbloquea al alcanzar la liga oro</div>
                    <button class="btn btn-primary">Comprar: 100 monedas</button>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="panel panel-primary">
                    <div class="panel-heading">BARAJA DE BRONCE</div>
                    <div class="panel-body"><img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image"></div>
                    <div class="panel-footer">Se desbloquea al alcanzar la liga bronce</div>
                    <button class="btn btn-success">Ya adquirido</button>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="panel panel-primary">
                    <div class="panel-heading">BARAJA DE DIAMANETE</div>
                    <div class="panel-body"><img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image"></div>
                    <div class="panel-footer">Se desbloquea al alcanzar la liga diamante</div>
                    <button class="btn btn-danger">Artículo bloqueado</button>
                </div>
            </div>
        </div>
    </div><br>

    <div class="container-fluid bg-3 text-center">
        <div class="row">
            <div class="col-sm-4">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
            <div class="col-sm-4">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
            <div class="col-sm-4">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
        </div>
    </div><br><br>


    <div class="container-fluid bg-3 text-center">
        <h3>Dorsos</h3><br>
        <div class="row">
            <div class="col-sm-3">
                <div class="panel panel-primary">
                    <div class="panel-heading">DORSO LEGENDARIO</div>
                    <div class="panel-body"><img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image"></div>
                    <div class="panel-footer">Se desbloquea al alcanzar la liga oro</div>
                    <button class="btn btn-primary">Comprar: 100 monedas</button>
                </div>
            </div>
            <div class="col-sm-3">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
            <div class="col-sm-3">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
            <div class="col-sm-3">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
        </div>
    </div><br>

    <div class="container-fluid bg-3 text-center">
        <h3>Avatares</h3><br>
        <div class="row">
            <div class="col-sm-3">
                <div class="panel panel-primary">
                    <div class="panel-heading">AVATAR LEGENDARIO</div>
                    <div class="panel-body"><img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image"></div>
                    <div class="panel-footer">Se desbloquea al alcanzar la liga oro</div>
                    <button class="btn btn-primary">Comprar: 100 monedas</button>
                </div>
            </div>
            <div class="col-sm-3">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
            <div class="col-sm-3">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
            <div class="col-sm-3">
                <p>Some text..</p>
                <img src="https://placehold.it/150x80?text=IMAGE" class="img-responsive" style="width:100%" alt="Image">
            </div>
        </div>
    </div><br>

</body>
</html>
