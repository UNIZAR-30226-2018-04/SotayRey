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
        var nombre_jugador = "acastellanos";
        var total_parts = 2;
        var socket = new WebSocket("ws://localhost:8080/matchmaking");
        var listo = JSON.stringify({
            "tipo_mensaje": "busco_partida",
            "nombre_participante": nombre_jugador,
            "tipo_partida": "publica",
            "total_jugadores": total_parts,
            "con_ia": false
        });
        socket.onopen = function() {
            socket.send(listo);
        };
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
