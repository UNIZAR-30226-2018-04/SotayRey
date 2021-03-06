<%@ page import="basedatos.modelo.UsuarioVO" %>

<script>
    var nombreUsuario;
</script>

<nav class="navbar navbar-expand-lg sticky-top navbar-light" style="background-color: white">
    <% if (session.getAttribute("userId") == null) { %>
        <a class="navbar-brand" href="../index.jsp">
    <% } else { %>
        <a class="navbar-brand" href="../jsp/matchmaking.jsp">
    <% } %>
        <img src="../img/logo.png" width="30" class="mr-2">SotaYRey</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ml-auto mr-2">
            <%
               if (session.getAttribute("userId") == null) { %>
                <li class="nav-item">
                    <a class="btn btn-outline" href="../jsp/login.jsp" role="button">
                        <i class="fa fa-sign-in mr-2 "></i>Entrar/Registrarse</span>
                    </a>
                </li>
            <% } else {
                    UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
                    String nick = usuarioVO.getUsername(); %>
                <li class="nav-item">
                    <a class="btn btn-outline" href="../jsp/matchmaking.jsp" role="button">
                        <i class="fa fa-play mr-2"></i>Jugar</a>
                </li>
            <li class="nav-item">
                <a class="btn btn-outline" href="/GestionarTorneo.do" role="button">
                    <i class="fa fa-star mr-2"></i>Torneos</a>
            </li>
                <li class="nav-item">
                    <a class="btn btn-outline" href="/MostrarLiga.do" role="button">
                        <i class="fa fa-trophy mr-2"></i>Ligas</a>
                </li>
                <li class="nav-item">
                    <a class="btn btn-outline" href="/MostrarObjetosTienda.do" role="button">
                        <i class="fa fa-shopping-basket mr-2"></i>Tienda</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-toggle="dropdown" id="Preview" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                        <script>
                            nombreUsuario = "<%=nick%>";
                        </script>
                        <i class="fa fa-user mr-2"></i><%=nick%>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="Preview">
                        <a class="dropdown-item" href="/PerfilServlet.do">
                            <i class="fa fa-user-circle-o mr-2"></i>Perfil</a>
                        <a class="dropdown-item" href="/LogoutServlet.do">
                            <i class="fa fa-power-off mr-2"></i>Cerrar sesi&oacuten</a>
                    </div>
                </li>
            <% }%>
        </ul>
    </div>
</nav>