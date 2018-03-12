<!doctype html>
<html lang="en"> 
<head> 
	<meta charset="UTF-8" />
    <title>Phaser - Making your first game, part 9</title>
    <script src="//cdn.jsdelivr.net/phaser/2.2.2/phaser.min.js"></script>
    <style type="text/css">
        body {
            margin: 0;
        }
    </style>
</head>
<body>
<script type="text/javascript" src='socket.js'></script>

<script type="text/javascript">
var client = new WebSocketClient('ws', '127.0.0.1', 8080, '/pruebaSocket/endpoint');
client.connect();
client.send("hola holita");
</script>

<script type="text/javascript" src='guinote.js'></script>

</body>
</html>