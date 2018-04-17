<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<html>
<head>
    <title>Login/Registro</title>
    <%@ include file="../html/imports.html"%>
</head>
<body>
    <div class="container">
        <div class="row">
            <h1 class="text-center col-md-12 mb-4 mt-4">
                <img src="../img/logo.png" style="max-width: 60px" class="mr-3"/>SotaYRey</h1>
        </div>
        <% String error = (String) request.getAttribute("error"); %>
        <%
            if (error != null) { // Hay un error %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">

        <%
                switch (error){
                    case "userNotFound": %>
            <strong>Usuario no encontrado o contraseña incorrecta.</strong> Inténtalo de nuevo.
            <%
                    break;
                case "adminNotFound":%>
            <strong>No eres administrador del sistema</strong>
            <%
                        break;
                    case "emptyUser":%>
            <strong>Introduzca un usuario</strong>
            <%
                        break;
                    case "emptyPass":%>
            <strong>Introduzca la contraseña</strong>
            <%
                        break;
                    case "emptyLastName":%>
            <strong>Introduzca sus apellidos</strong>
            <%
                        break;
                    case "emptyName":%>
            <strong>Introduce tu nombre</strong>
            <%
                        break;
                    case "emptyEmail":%>
            <strong>Introduce tu correo electrónico</strong>
            <%
                        break;
                    case "emptyRePass":%>
            <strong>Debes repetir la contraseña</strong>
            <%
                        break;
                    case "wrongRePass":%>
            <strong>La contraseña no coincide</strong>
            <%
                        break;
                    case "existentUser":%>
            <strong>Usuario ya existente.</strong> Inténtalo con otro nombre de usuario.
            <%
                        break;
                default:%>
            <strong><%=error%></strong> Inténtalo con otro nombre de usuario.
            <%
                }%>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <% } %>
        <div class="row mb-4">
            <div class="card col-md-6">
                <h1 class="card-header text-center">Entra a tu cuenta</h1>
                <div class="card-body">
                    <form action="/LoginServlet.do" method="post">
                        <div class="form-group">
                            <label for="loginUser">Usuario</label>
                            <input type="text" class="form-control" name="loginUser" id="loginUser"
                                   aria-describedby="emailHelp" placeholder="Introduce tu usuario">
                        </div>
                        <div class="form-group">
                            <label for="loginPass">Contraseña</label>
                            <input type="password" class="form-control" name="loginPass" id="loginPass"
                                   placeholder="Contraseña">
                        </div>
                        <button type="submit" class="btn btn-primary">Entrar</button>
                    </form>
                </div>
            </div>
            <div class="card col-md-6">
                <h1 class="card-header text-center">Date de alta</h1>
                <div class="card-body">
                    <form action="/RegistroServlet.do" method="post">
                        <div class="form-group">
                            <label for="nick">Usuario</label>
                            <input type="text" class="form-control" name="nick" id="nick" aria-describedby="emailHelp"
                                   placeholder="Introduce tu usuario">
                        </div>
                        <div class="form-group">
                            <label for="name">Nombre</label>
                            <input type="text" class="form-control" name="name" id="name" aria-describedby="emailHelp"
                                   placeholder="Introduce tu nombre">
                        </div>
                        <div class="form-group">
                            <label for="lastName">Apellidos</label>
                            <input type="text" class="form-control" name="lastName" id="lastName"
                                   aria-describedby="emailHelp" placeholder="Introduce tus apellidos">
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" class="form-control" name="email" id="email"
                                   aria-describedby="emailHelp" placeholder="Introduce tu email">
                        </div>
                        <div class="form-group">
                            <label for="passwd">Contraseña</label>
                            <input type="password" class="form-control" name="passwd" id="passwd"
                                   placeholder="Contraseña">
                        </div>
                        <div class="form-group">
                            <label for="passwdRep">Repite contraseña</label>
                            <input type="password" class="form-control" name="passwdRep" id="passwdRep"
                                   placeholder="Contraseña">
                        </div>
                        <button type="submit" class="btn btn-primary">Registrarme</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
