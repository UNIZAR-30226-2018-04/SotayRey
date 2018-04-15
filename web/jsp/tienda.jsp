<%@ page import="basedatos.modelo.UsuarioVO" %>
<%@ page import="basedatos.modelo.StatsUsuarioVO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="basedatos.modelo.ArticuloUsuarioVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html lang="en" >
<head>
    <title>Tienda</title>
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

<%  String nick = null;
    int monedas = 0;
    UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userID");
    ArrayList<ArticuloUsuarioVO> articulos = new ArrayList<>();
    if (usuarioVO == null) {
        response.sendRedirect("/jsp/login.jsp");
    } else {
        nick = usuarioVO.getUsername();
        StatsUsuarioVO statsVO = (StatsUsuarioVO) session.getAttribute("userStats");
        if (statsVO == null){
            response.sendRedirect("/jsp/login.jsp");
        }
        try {
            monedas = statsVO.getDivisa();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
        if (articulos == null){
            //TODO: no hay articulos
        }
    }
%>

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

    <div class="container-fluid bg-3 text-center">
        <h3>Barajas</h3><br>
        <div class="row equal">
            <% for (ArticuloUsuarioVO art: articulos) {
                if (art.getTipo() == 'B'){%>
            <div class="col-sm-4">
                <div class="panel panel-primary">
                    <div class="panel-heading"> <%= art.getNombre() %></div>
                    <div class="panel-body"><img src= "<%=art.getRutaImagen()%>" class="img-responsive" style="width:100%" alt="Imagen baraja <%= art.getNombre()%>"></div>
                    <div class="panel-footer">Se desbloquea al alcanzar la liga <%= art.getRequiere()%></div>
                    <button class="btn btn-primary">Comprar: <%= art.getPrecio()%> monedas</button>
                </div>
            </div>
            <% }
            } %>
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
    </div><br><br>


    <div class="container-fluid bg-3 text-center">
        <h3>Dorsos</h3><br>
        <div class="row">
            <div class="col-sm-3">
                <div class="panel panel-primary">
                    <div class="panel-heading">BARAJA DE ORO</div>
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
                    <div class="panel-heading">BARAJA DE ORO</div>
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
