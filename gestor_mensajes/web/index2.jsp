<%--
  Created by IntelliJ IDEA.
  User: Javier
  Date: 01/04/2018
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script>
        var socket = new WebSocket("ws://localhost:8080/endpoint");
        var string = JSON.stringify({
            "tipo_mensaje": "listo_jugador",
            "nombre_participante": "jugador1",
            "total_jugadores": 2,
            "tipo_participante": "jugador",
            "remitente": {
                "id_partida": 0,
                "id_jugador": 1
            }
        });
        socket.onmessage = function (msg) {
            console.log(msg.data);
        };
        function send() {
          socket.send(string);
        }
    </script>
  </head>
  <body>
  <button onclick="send()">Hola1</button>
  </body>
</html>
