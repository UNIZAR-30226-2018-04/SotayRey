<%--
  Created by IntelliJ IDEA.
  User: Javier
  Date: 17/10/2017
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>SotaYRey</title>
    <%@ include file="html/imports.html"%>
  </head>
  <body>
  <%@include file="jsp/navbar.jsp" %>
  <div class="container">
      <div class="col-md-12 mb-4">
      </div>

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
                  case "userNotExist": %>
        <strong>Usuario no conectado.</strong>
        <%
                  break;
              case "sessionNotExist":%>
        <strong>No hay ninguna sesión en curso</strong>
        <%
                break;
              }
        %>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <% } %>
    </div>

  </body>
</html>
