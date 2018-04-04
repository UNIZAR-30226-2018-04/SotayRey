//var ejeX = 800;
//var ejeY = 600;
//var ejeX = window.innerWidth * window.devicePixelRatio;
//var ejeY = window.innerHeigth * window.devicePixelRatio;
//var ejeY = window.innerHeight * window.devicePixelRatio;

/* PORCENTAJES RESPONSIVE */
var numBotones = 3;
var zonaJugableY = 0.9; // Porcentaje
var ejeXBotones = window.innerWidth;
var ejeYBotones = window.innerHeight * zonaJugableY + ( window.innerHeight *(1-zonaJugableY)*0.20);

/* VARIABLES PANTALLA */


var ejeX = window.innerWidth;
var ejeY = window.innerHeight * zonaJugableY;


//var ejeX = window.innerWidth; // Zona jugable
//var ejeY = window.innerHeight * zonaJugableY;
var escalaCartas = 0.5;
var scaleRatio = 0.5;
var ejeXCartaOriginal = 80; // esto en una apartado de game options
var ejeYCartaOriginal = 123;
var ejeXCarta = (ejeX * 0.8) / 6;
var ejeYCarta = ejeXCarta*(ejeYCartaOriginal/ejeXCartaOriginal);

var escalaCarta = 1;
//var escalaCarta = ejeX / 5/ ejeXCarta;
console.log(ejeX);
console.log(window.innerHeight);
console.log(window.devicePixelRatio);
console.log(ejeY);
//var scaleRatio = window.devicePixelRatio / 3;

// para que sea responsive:  https://www.joshmorony.com/how-to-scale-a-game-for-all-device-sizes-in-phaser/
// en vez de Phaser.AUTO -> Phaser.CANVAS
var game = new Phaser.Game(ejeX, ejeY / zonaJugableY, Phaser.CANVAS, '', { preload: preload, create: create, update: update, render: render });

/**
 * Precarga las imagenes y sonidos que se utilizaran en el juego
 */
function preload() {
    game.load.spritesheet("cartas", "assets/naipes.png", ejeXCartaOriginal, ejeYCartaOriginal);
    game.load.image('naipe', 'assets/naipe.png');
    game.load.image('cuadroCarta', 'assets/dragHere.png');
    game.load.image('turno', 'assets/turno.png');
    game.load.image('tapete', 'assets/tapete.png');
    game.load.audio('musica', ['assets/musica.mp3'])
}

/* VARIABLES GLOBALES */

//var numJugadores = 4; // Define el tipo de partida en función del número de jugadores

var misCartas;

// Variables de los jugadores
//var miID = 0;
//var idPartida = 0;


/* se inicializan con la funcion inicializarDispositivo */
var jRef;
var jIzq;
var jArriba;
var jDer;

var turno; // punto rojo que indica el turno

var triunfo;
triunfo = {};
triunfo.carta = {}
triunfo.x = ejeX / 2;
triunfo.y = (ejeY / 2) * 0.85;

var arrayJugadoresDefecto = [];
var arrayJugadores = [];

/**
 *  Inicializa las variables que dependen de la resolucion del dispositivo
 */
function inicializarDispositivo(){
    if (ejeX > 800){
        console.log("dispositivo grande");
        console.log(ejeX);
        ejeXCarta = ejeXCartaOriginal;
        ejeYCarta = ejeYCartaOriginal;

    }
    else if (ejeX > 420){
        console.log("dispositivo grande");
        console.log(ejeX);
        ejeXCarta = (ejeX * 0.65) / 6;
        ejeYCarta = ejeXCarta*(ejeYCartaOriginal/ejeXCartaOriginal);

    }
    else{
        // El valor que esta por defecto
    }
    jRef = {};
    jRef.nombre = "ref";
    jRef.XPosMedia = ejeX / 2;
    jRef.YPosMedia = ejeY * 0.80;
    jRef.sumX = ejeXCarta;
    jRef.sumY = 0;
    jRef.XLanzar = jRef.XPosMedia;
    jRef.YLanzar = jRef.YPosMedia - ejeYCarta * 1.20;
//jRef.YLanzar = 300;
    jRef.cartaLanzada;
    jRef.numCartas;
    jRef.cartasEnMano = game.add.group();
    jRef.dorso;
    jRef.rotacion = 0;

    jIzq = {};
    jIzq.XPosMedia = ejeX * 0.15;
    jIzq.YPosMedia = ejeY / 2;
    jIzq.sumX = 0;
    jIzq.sumY = ejeXCarta; // es ejeX porque esta tumbada
    jIzq.XLanzar = jIzq.XPosMedia + ejeYCarta * 1.10;
    jIzq.YLanzar = jIzq.YPosMedia;
    jIzq.cartaLanzada;
    jIzq.numCartas;
    jIzq.cartasEnMano = game.add.group();
    jIzq.dorso;
    jIzq.rotacion = 90;

    jArriba = {};
    jArriba.XPosMedia = ejeX / 2;
    jArriba.YPosMedia = ejeY * 0.02;
    jArriba.sumX = ejeXCarta;
    jArriba.sumY = 0;
    jArriba.XLanzar = jArriba.XPosMedia;
    jArriba.YLanzar = jArriba.YPosMedia + ejeYCarta * 1.10;
    jArriba.cartaLanzada;
    jArriba.numCartas;
    jArriba.cartasEnMano = game.add.group();
    jArriba.dorso;
    jArriba.rotacion = 0;

    jDer = {};
    jDer.XPosMedia = ejeX * 0.85;
    jDer.YPosMedia = (ejeY / 2) * 1.2;
    jDer.sumX = 0;
    jDer.sumY = ejeXCarta;
    jDer.XLanzar = jDer.XPosMedia - ejeYCarta * 1.10;
    jDer.YLanzar = jDer.YPosMedia;
    jDer.cartaLanzada;
    jDer.numCartas;
    jDer.cartasEnMano = game.add.group();
    jDer.dorso;
    jDer.rotacion = 270;

    turno = game.add.sprite(0, 0, 'turno');
    turno.height = ejeYCarta * 0.30;
    turno.width = ejeXCarta * 0.30;


    arrayJugadoresDefecto = [jRef, jArriba, jIzq, jDer];


}

/**
 *  Mapeo de los jugadores para saber la posicion que ocupan
 *  en funcion  del tipo de partida y mi identificador
 */
function mapearJugadores(){
    for (i = 0; i < numJugadores; i++) {
        arrayJugadores[i] = arrayJugadoresDefecto[(miID + i)%numJugadores];
    }
}

/**
 * Cambia y dibuja el triunfo sobre la mesa
 * @param numero Numero de la carta de triunfo
 * @param palo Palo de la carta de triunfo
 */
function modificarTriunfo(numero, palo){
    triunfo.carta = crearCarta(numero, palo);
    triunfo.carta.x = triunfo.x;
    triunfo.carta.y = triunfo.y;
    triunfo.carta.height = ejeYCarta;
    triunfo.carta.width = ejeXCarta;

}


function inicializarRef(){ // TODO esto al final creo que no debería estar
    for (i = 0; i < 6; i++){
        var carta = jRef.cartasEnMano.create(0, 0, 'cartas');
        carta.frame = i;
        carta.numero = i+1;
        carta.palo = "oros";
    }
    dibujarJugador(jRef);
    inicializarCuadroCarta(jRef);
    //dibujarCuadroCarta(jRef);
}

/* Inicializa los jugadores dependiendo del numero que haya, excepto el de referencia */
function inicializarJugadores(){
    crearCartas(jArriba);
    //dibujarCuadroCarta(jArriba);
    inicializarCuadroCarta(jArriba);
    if (numJugadores == 4){
        crearCartas(jIzq);
        //dibujarCuadroCarta(jIzq);
        inicializarCuadroCarta(jIzq);
        crearCartas(jDer);
        inicializarCuadroCarta(jDer);
   }
}

/**
 * Dibuja el cuadro para lanzar la carta
 * @param jugador Jugador a dibujar el cuadro
 */
function inicializarCuadroCarta(jugador){
    crearCuadroCarta(jugador);
    dibujarCuadroCarta(jugador);
}

/**
 * Crea el cuadro punteado donde el jugador tiene que lanzar la carta
 * @param jugador Jugador a dibujar el cuadro
 */
function crearCuadroCarta(jugador){
    jugador.cartaLanzada = game.add.sprite(jugador.XLanzar, jugador.YLanzar, 'cuadroCarta');
}

/**
 * Dibuja el cuadro punteado en la posición donde el jugador lanza la carta
 * @param jugador Jugador a dibujar el cuadro
 */
function dibujarCuadroCarta(jugador){
    //var cuadro = game.add.sprite(jugador.XLanzar, jugador.YLanzar, 'cuadroCarta');
    jugador.cartaLanzada.loadTexture('cuadroCarta');
    jugador.cartaLanzada.numero = 0;
    jugador.cartaLanzada.palo = 0;
    jugador.cartaLanzada.width = ejeXCarta;
    jugador.cartaLanzada.height = ejeYCarta;
    jugador.cartaLanzada.angle = jugador.rotacion;
    //cuadro.scale.setTo(escalaCarta, escalaCarta);
}

/**
 * Dibuja la carta lanzada
 * @param jugador Jugador a dibujar la carta lanzada
 */
function dibujarCartaLanzada(jugador){
    //var cuadro = game.add.sprite(jugador.XLanzar, jugador.YLanzar, 'cuadroCarta');
    console.log("dibujo la carta en el medio");
    jugador.cartaLanzada.width = ejeXCarta;
    jugador.cartaLanzada.height = ejeYCarta;
    jugador.cartaLanzada.x = jugador.XLanzar;
    jugador.cartaLanzada.y = jugador.YLanzar;
    jugador.cartaLanzada.angle = jugador.rotacion;
    //cuadro.scale.setTo(escalaCarta, escalaCarta);
}

/**
 * Crea las cartas para un jugador. Se utiliza para los jugadores
 * que no son el de referencia ya que no se puede saber qué cartas son,
 * por lo que se dibuja el dorso correspondiente
 * @param jugador Jugador a crear las cartas
 */
function crearCartas(jugador){
    for (i = 0; i < jugador.numCartas; i++){
        console.log("CRANDO CARTAS PARA " + jugador.XPosMedia);
        jugador.cartasEnMano.create(0, 0, 'cartas');
    }

}

/**
 * Dibuja las cartas que tiene en la mano un jugador
 * @param identificador identificador del jugador a dibujar las cartas
 */
function dibujarJugador(identificador){
    var jugador = identificador;
    console.log("DIBUJANDO PARA " + jugador.XPosMedia);
    var i = - jugador.cartasEnMano.length/2; // Para que al final la posición sea centrada


    jugador.cartasEnMano.forEach(function(item) {
        console.log("NUMERO:  " + item.numero + "  PALO: " + item.palo)
        item.x = jugador.XPosMedia + i*jugador.sumX;
        item.y = jugador.YPosMedia + i*jugador.sumY;
        //item.loadTexture(jugador.dorso, 0);
        if (jugador.nombre != "ref") {
            item.frame = 12; // TODO modificar esto para que sea el dorso
        }
        item.angle = jugador.rotacion;
        item.height = ejeYCarta;
        item.width = ejeXCarta;
        //item.scale.setTo(escalaCarta, escalaCarta);
        console.log("pongo carta en mesa");
        console.log(item.height);
        console.log(item.x);
        console.log(item.y);
        i = i + 1;
    }, this);

}

/* Dibuja las cartas del jugador de referencia */
/*
function dibujarJugadorRef(){
    var jugador = jRef;
    jugador.cartasEnMano.forEach(function(item) {
        item.x = jugador.XPosMedia + i*jugador.sumX;
        item.y = jugador.YPosMedia + i*jugador.sumY;
        item.angle = jugador.rotacion;
        //item.scale.setTo(escalaCarta, escalaCarta);
        console.log("pongo carta en mesa");
        console.log(item.x);
        console.log(item.y);
        i = i + 1;
    }, this);

}

*/
/**
 * Inicializa el juego
 */
function create() {

    //  We're going to be using physics, so enable the Arcade Physics system
    game.physics.startSystem(Phaser.Physics.ARCADE);
    //game.stage.sprite(0,0, "tapete");

    game.add.tileSprite(0, 0, ejeX, ejeY, 'tapete');
    //game.stage.backgroundColor = "#009933";
    //misCartas = game.add.group();
    //anyadirCarta(misCartas,0);
    //anyadirCarta(misCartas,1);
    //actualizarCartas(misCartas);


    inicializarDispositivo();

    // Botones de acción
    dibujarBotones();

    // Los huecos para tirar las cartas de los jugadores
    //yo = game.add.sprite(game.world.centerX-50, game.world.centerY-50, 'cuadroCarta');
    //yo.scale.setTo(0.1, 0.1);


    // Pruebas


    // Estado inicial de prueba

    var estado_inicial = {
        "tipo_mensaje": "estado_inicial",
        "remitente": {
            "id_partida": 0,
            "id_jugador": 0
        },
        "partida": {
            "ronda": 0,
            "tipo_ronda": "idas",
            "restantes_mazo": 0
        },
        "jugadores": [
            {
                "id": 1,
                "nombre": "hello",
                "avatar": "ruta",
                "tipo": "espectador",
                "puntos": 0,
                "num_cartas": 6,
                "carta_mesa": {
                    "numero": 0,
                    "palo": 0,
                }
            },
            {
                "id": 2,
                "nombre": "hello",
                "avatar": "ruta",
                "tipo": "espectador",
                "puntos": 0,
                "num_cartas": 6,
                "carta_mesa": {
                    "numero": 0,
                    "palo": 0,
                }
            },
            {
                "id": 3,
                "nombre": "hello",
                "avatar": "ruta",
                "tipo": "espectador",
                "puntos": 0,
                "num_cartas": 6,
                "carta_mesa": {
                    "numero": 0,
                    "palo": 0,
                }
            }
        ],
        "triunfo": {
            "numero": 7,
            "palo": "espadas"
        },
        "mano": [
            {
                "numero": 2,
                "palo": "espadas"
            },
            {
                "numero": 4,
                "palo": "bastos"
            },
            {
                "numero": 6,
                "palo": "oros"
            },
            {
                "numero": 10,
                "palo": "copas"
            }
        ]
    }

    console.log("HEEEEELL YEAH " +estado_inicial.remitente.id_partida);
    //representarEstado(estado_inicial);

    //mapearJugadores();

    //jRef.cartasEnMano.inputEnabled = true;
    //jRef.cartasEnMano.onInputDown.add(pulsarCarta, this);

    //jArriba.numCartas = 6;
    //jIzq.numCartas = 6;
    //jDer.numCartas = 6;

    //inicializarJugadores();
    //dibujarJugador(jArriba);
    //dibujarJugador(jIzq);
    //dibujarJugador(jDer);
    //inicializarRef();

    //modificarTriunfo(12, "copas");


    // Simulacion partida


    //jugadorLanzaCarta(0, 3, "oros");

    //jugadorLanzaCarta(1, 3, "bastos");


    //jugadorLanzaCarta(2, 6, "espadas");

    //jugadorLanzaCarta(3, 11, "copas");

    controlMusica();
    listo_jugador(); // Confirma que el jugador ya esta listo para jugar

}

/**
 * No se utiliza
 * @param milliseconds
 */
function sleep(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds){
            break;
        }
    }
}

function onDragStop(sprite) {

    sprite.x = sprite.origenX;
    sprite.y = sprite.origenY;


}

function listener (item) {
    console.log("pulsaron la cartita");
    console.log(item.nombre);
}

/**
 * Para depurar el juego
 */
function render() {

    // Input debug info
    game.debug.inputInfo(32, 32);
    //game.debug.spriteInputInfo(sprite, 32, 130);
    game.debug.pointer( game.input.activePointer );

}

function update() {

    // Nada por el momento
}

/* Funciones de Cartas */




function anyadirCarta(conjuntoCartas, numCarta){
    var carta = conjuntoCartas.create(0, 0, 'cartas');
    carta.frame = numCarta; // TODO indexar la correspondiente

    /* OPCIONES */
    carta.nombre = "carta bonita";
    carta.events.onInputDown.add(lanzarCarta, this);
    carta.inputEnabled = true;
}

function actualizarCartas(grupoCartas){
    //cartas.inputEnabled = true;
    //cartas.input.enableDrag();
    //cartas.input.enableDrag();
    //cartas.input.enableDrag(false, true);
    var i = 0;

    grupoCartas.forEach(function(item) {
        item.x = ejeXCarta * escalaCarta * i;
        item.y = ejeY - 200;
        item.scale.setTo(escalaCarta, escalaCarta);
        i = i + 1;
    }, this);

}

function lanzarCartaConfirmar(numCarta){
    misCartas.forEach(function(item) {
        if (item.frame == numCarta){
            item.x = game.world.centerX-50;
            item.y = game.world.centerY-50;
            //item.scale.setTo(escalaCarta, escalaCarta);
            game.world.bringToTop(item);
        }
    }, this);
}

function lanzarCarta (item) {
    console.log("pulsaron la carta " + item.frame +", enviando mensaje");
    console.log(item.nombre);
    enviarMensaje("-----");
    lanzarCartaConfirmar(item.frame);
}

/**
 * Envía el mensaje de confirmación de que el jugador está listo para comenzar la partida
 */
function listo_jugador(){
    var obj = {
        "tipo_mensaje" : "listo_jugador",
        "nombre_participante": nombre,
        "total_jugadores": 2,
        "tipo_participante": "jugador",
        "remitente" : {
            "id_partida" : idPartida,
            "id_jugador" : miID
        }
    }

    //var myJSON = JSON.stringify(obj);
    enviarMensaje(obj);
}

function accion(tipo, numero, palo, cantar){
    var queAccion;
    switch(tipo) {
        case "lanzar_carta":
            queAccion = "lanzar_carta";
            break;
        case  "robar_carta":
            queAccion = "robar_carta";
            break;
        case "cantar":
            queAccion = "cantar";
            break;
        case "cambiar_triunfo":
            queAccion = "cambiar_triunfo";
            break;
    }

    var obj = {
        "tipo_mensaje": "accion",
        "remitente": {
            "id_partida": 0,
            "id_jugador": miID
        },
        "tipo_accion": queAccion,
        "carta": {
            "numero": 0,
            "palo": 0
        },
        "cantidad" : cantar
    }
}


function enviarMensaje(obj){
    // TODO se envia el mensaje al backend
    var msg = JSON.stringify(obj);
    console.log(msg);
    socket.send(msg);
}

/**
 * Inicializa las variables y dibuja el estado
 * @param estado Estado que se desea representar
 */
function representarEstado(estado){
    // Se dibuja el triunfo
    modificarTriunfo(estado.triunfo.numero, estado.triunfo.palo);
    // Se dibujan las cartas de los jugadores
    // TODO hay que mapear antes
    mapearJugadores();
    estado.jugadores.forEach(function(item) {
        if ([item.id] != miID){
            var jugador = arrayJugadores[item.id];
            console.log("JUGADORSITO " + jugador.XPosMedia + " " + item.id);
            jugador.numCartas = item.num_cartas;
            crearCartas(jugador);
            dibujarJugador(jugador);
            jugador.cartaLanzada = crearCarta(item.carta_mesa.numero, item.carta_mesa.palo);
            dibujarCartaLanzada(jugador);
            arrayJugadores[item.id] = jugador;
        }
    }, this);

    // Se pone la mano del jugador
    var jugador = arrayJugadores[miID];
    var carta = {};
    estado.mano.forEach(function(item) {
        carta = crearCarta(item.numero, item.palo);
        console.log(carta.numero + carta.palo);
        //carta.numero = item.numero;
        carta.inputEnabled = true;
        carta.atributo = "HELLOW DA";
        carta.events.onInputDown.add(pulsaCarta, this);
        jugador.cartasEnMano.add(carta);
    }, this);
    dibujarJugador(jugador);
}

function recibirMensaje(msg){
    console.log("RECIBO UN MENSAJEEEEEE");
    console.log(msg);
    var mensaje = JSON.parse(msg);
    switch (mensaje.tipo_mensaje){
        case "broadcast_accion":
            switch (mensaje.tipo_accion){
                case "lanzar_carta":
                    jugadorLanzaCarta(mensaje.id_jugador, mensaje.carta.numero, mensaje.carta.palo);
                    break;
                case "robar_carta":
                    if (mensaje.id_jugador != miID){
                        jugadorRobaCarta(mensaje.id_jugador, "nada", "nada");
                    }
                    else {
                        jugadorRobaCarta(mensaje.id_jugador, mensaje.carta.numero, mensaje.carta.palo);
                    }
                    break;
                case "cantar":
                    break;
                case "cambiar_triunfo":
                    jugadorCambiaTriunfo(mensaje.id_jugador, mensaje.carta.numero, mensaje.carta.palo);
                    break;
                case "turno" :
                    dibujarTurno(mensaje.id_jugador);
                    break;
            }
            break;
        case "gana_ronda":
            //sleep(60000);
            //rondaAcabada();
            setTimeout(function(){ rondaAcabada(); actualizarHUD(mensaje);}, 3000);
            break;
        case "estado_inicial" :
            representarEstado(mensaje);
            break;
    }
}


function crearCarta(numero, palo){
    console.log("ME PIDEN CARTA CON NUMERO: " +numero+" PALO: "+palo);
    var carta;
    var indice = buscarCarta(numero, palo);
    if (palo == 0){
        carta = game.add.sprite(0, 0, 'cuadroCarta');
    }
    else{
        carta = game.add.sprite(0, 0, 'cartas');
        carta.frame = indice;
    }
    carta.numero = numero; // Si no se pone despues el game.add lo machaca
    carta.palo = palo;
    console.log("DEVUELVO CARTA CON NUMERO: " +carta.numero+" PALO: "+carta.palo);
    return carta;
}

function crearDorso(numero, palo){
    var carta = game.add.sprite(0, 0, 'cartas');
    carta.frame = 13;
    return carta;
}

/**
 * Elimina la carta del mazo (cualquiera si no es referencia) y la pone en el centro en la variable cartaLanzada
 * @param idJugador Id del jugador que lanza la carta
 * @param numero Numero de la carta que lanza
 * @param palo Palo de la carta que lanza
 */
function jugadorLanzaCarta(idJugador, numero, palo){
    // indexar jugador
    // coger la primera carta de la mano y eliminarla
    // cambiar el sprite al de la carta correspondiente en el cuadro de lanzar
    var jugador = arrayJugadores[idJugador];
    console.log("ELll parametros es: " + numero);
    if (idJugador!= miID){
        var cartita = jugador.cartasEnMano.getFirstAlive();
        jugador.cartasEnMano.removeChild(cartita);
        dibujarJugador(jugador);
        jugador.cartaLanzada = crearCarta(numero, palo);
        dibujarCartaLanzada(jugador);

    }
    else {
        var salir = false;
        jugador.cartasEnMano.forEach(function(item) {
           // console.log("entro a buscar en el referencia" + item.numero + " " + item.palo);
            if (salir == false && item.numero == numero && item.palo == palo){ // Porque se elimina la carta entonces la funcion no encuentra la carta, por eso el salir
                console.log("EJECUTO ESTO");
                jugador.cartasEnMano.removeChild(item);
                dibujarJugador(jugador);
                jugador.cartaLanzada = crearCarta(numero, palo);
                console.log("DIBUJO MI CARTA DE REFEREEENCIA");
                dibujarCartaLanzada(jugador);
                salir = true;
            }
        }, this);
    }
}

/**
 * Permuta la carta indicada con la de triunfo
 * @param id Jugador que cambia
 * @param numero numero de la carta a cambiar
 * @param palo palo de la carta a cambiar
 */
function jugadorCambiaTriunfo(id, numero, palo){
    console.log("SE CAMBIA EL TRIUNFO");
    var jugador = arrayJugadores[id];
    if (id == miID){ // Soy el de referencia
        var salir = false;
        jugador.cartasEnMano.forEach(function(item) {
            // console.log("entro a buscar en el referencia" + item.numero + " " + item.palo);
            if (salir == false && item.numero == numero && item.palo == palo){ // Porque se elimina la carta entonces la funcion no encuentra la carta, por eso el salir
                var numeroTriunfo = triunfo.carta.numero;
                var paloTriunfo = triunfo.carta.palo;
                modificarTriunfo(item.numero, item.palo);
                jugador.cartasEnMano.removeChild(item);
                var carta = crearCarta(numeroTriunfo, paloTriunfo);
                jugador.cartasEnMano.add(carta);
                dibujarJugador(jugador);
                salir = true;
            }
        }, this);
    }
    else{
        modificarTriunfo(numero, palo);
    }
}

/**
 * Accion del jugador cuando se pulsa una carta. Asociado a la acción de lanzar_carta
 * @param item Carta que se ha pulsado
 */
function pulsaCarta(item){
    console.log("HA PUSLADO CARTA" + "   " + item.numero + "  " + item.atributo + "  " + item.palo);
    var obj = {
        "tipo_mensaje": "accion",
        "remitente": {
            "id_partida": 0,
            "id_jugador": miID
        },
        "tipo_accion": "lanzar_carta",
        "carta": {
            "numero": item.numero,
            "palo": item.palo
        }
    }
    enviarMensaje(obj);
}

/**
 * Un jugador roba una carta, si no es el referencia da igual el numero y palo
 * @param idJugador Id del jugador que roba
 * @param numero
 * @param palo
 */
function jugadorRobaCarta(idJugador, numero, palo){
    var jugador = arrayJugadores[idJugador];
    if (idJugador!= miID){
        var carta = crearDorso(numero, palo);
        jugador.cartasEnMano.add(carta);
        dibujarJugador(jugador);
        dibujarCartaLanzada(jugador);

    }
    else {
        var carta = crearCarta(numero, palo);
        carta.inputEnabled = true;
        carta.events.onInputDown.add(pulsaCarta, this);
        jugador.cartasEnMano.add(carta);
        dibujarJugador(jugador);
        dibujarCartaLanzada(jugador);
    }
}

/**
 * Limpia la mesa de las cartas que han lanzado los jugadores
 */
function rondaAcabada(){
    // cambiar todos los cuadros de lanzar por el sprite del cuadro de lanzar
    for (i = 0; i < numJugadores; i++){
        console.log(i);
        var jugador = arrayJugadores[i];
        dibujarCuadroCarta(jugador);
    }
}

/**
 * Devuelve el numero a indexar en el sprite de cartas
 * @param numero Numero de la carta
 * @param palo Palo de la carta
 * @returns {number}
 */
function buscarCarta(numero, palo){
    var indice = numero - 1;
    console.log("EL indice es: " + numero);
    var numCartas = 13;
    if (palo == 0){
        indice = 0; // el marco
    }
    else if (palo == "O"){

    }
    else if (palo == "C"){
        indice = indice + numCartas;
    }
    else if (palo == "E"){
        indice = indice + numCartas*2;
    }
    else { // palo == "bastos"
        indice = indice + numCartas*3;
    }
    console.log("EL indice es: " + indice);
    return indice;
}

/* HUD */

/**
 * Dibuja los botones de accion y asocia la funcion para enviar el mensaje: cantar20, cantar40 y cambiarTriunfo
 */
function dibujarBotones(){
    var espacioBoton = ejeX / numBotones;
    //var style = {font: "20px", fill: "#000000", align:"center"};

    var cantarVeinte = game.add.text(0, ejeYBotones, '', { fill: '#ffffff'});
    //var cantarVeinte = game.add.text(0, ejeYBotones, '', style);
    var cantarCuarenta = game.add.text(espacioBoton, ejeYBotones, '', { fill: '#ffffff' });
    var cambiarTriunfo = game.add.text(espacioBoton*2, ejeYBotones, '', {fill: '#ffffff'});

    cantarVeinte.text = "CANTAR 20";
    cantarCuarenta.text = "CANTAR 40";
    cambiarTriunfo.text = "CAMBIAR TRIUNFO";
    cambiarTriunfo.text.fontSize = "20";

    cantarVeinte.inputEnabled = true;
    cantarVeinte.events.onInputDown.add(pulsaBoton, this);
    cantarCuarenta.inputEnabled = true;
    cantarCuarenta.events.onInputDown.add(pulsaBoton, this);
    cambiarTriunfo.inputEnabled = true;
    cambiarTriunfo.events.onInputDown.add(pulsaBoton, this);
}


/**
 * Accion del jugador cuando se pulsa una carta. Asociado a la acción de lanzar_carta
 * @param item Carta que se ha pulsado
 */
function pulsaBoton(item){
    var accion = "";
    var cantidad = 0;
    switch (item.text){
        case "CANTAR 40":
            accion = "cantar";
            cantidad = 40;
            break;
        case "CANTAR 20":
            accion = "cantar";
            cantidad = 20;
            break;
        case "CAMBIAR TRIUNFO":
            accion = "cambiar_triunfo";
            break;
    }
    console.log("HA PUSLADO BOTON" + "   " + item.text);
    var obj = {
        "tipo_mensaje": "accion",
        "remitente": {
            "id_partida": 0,
            "id_jugador": miID
        },
        "tipo_accion": accion,
        "cantidad": cantidad
    }
    enviarMensaje(obj);
}

/**
 * Dibuja el punto rojo que indica el turno del jugador
 * @param id_jugador Id del jugador al que hay que poner el turno
 */
function dibujarTurno(id_jugador){
    var jugador = arrayJugadores[id_jugador];
    turno.x = jugador.XLanzar;
    turno.y = jugador.YLanzar;
    turno.width = ejeXCarta;
    turno.height = ejeYCarta;
    turno.angle = jugador.rotacion;
    turno.alfa = 0.5;

}

/* ANIMACIONES */

var HUDInicializado = false;
var puntuacionMia = 0;
var puntuacionRival = 0;
var numRonda = 0;
var tipo_ronda = "IDAS";
var color = "#fffff5";
var fuente =  "15pt impact";

/**
 * Actualiza el HUD del jugador
 * @param datos Datos nuevos a actualizar
 */
function actualizarHUD(datos){
    console.log("ACTUALIZO EL HUD");
    tipo_ronda.tipo = datos.tipo_nueva_ronda;
    numRonda.numero = datos.nueva_ronda;
    puntuacionMia.puntuacion = datos.puntuaciones[miID].puntuacion; // TODO, siempre ids en orden?
    puntuacionRival.puntuacion = datos.puntuaciones[(miID+1)%numJugadores].puntuacion;


    if (HUDInicializado == false){
        console.log("NO ESTABA ININICIALIZADO");
        tipo_ronda = game.add.text(0, 0, '', { font: fuente, fill: color});
        tipo_ronda.tipo = "IDAS";
        tipo_ronda.text = "TIPO RONDA : " + tipo_ronda.tipo;

        numRonda = game.add.text(0, 20, '', { font: fuente, fill: color});
        numRonda.numero = 0;
        numRonda.text = "NUMERO RONDA : " + numRonda.numero;

        puntuacionRival = game.add.text(0, 40, '', { font: fuente, fill: color});
        puntuacionRival.puntuacion = 0;
        puntuacionRival.text = "PUNTUACION RIVAL: " + puntuacionRival.puntuacion;

        puntuacionMia = game.add.text(0, 60, '', { font: fuente, fill: color});
        puntuacionMia.puntuacion = 0;
        puntuacionMia.text = "MI PUNTAUCION : " + puntuacionMia.puntuacion;
        HUDInicializado = true;
    }
    else{
        console.log("ESTABA INICIALIZADO");
        tipo_ronda.text = "TIPO RONDA : " + tipo_ronda.tipo;
        numRonda.text = "NUMERO RONDA : " + numRonda.numero;
        puntuacionRival.text = "PUNTUACION RIVAL: " + puntuacionRival.puntuacion;
        puntuacionMia.text = "MI PUNTAUCION : " + puntuacionMia.puntuacion;
    }
}

var arrastre;

function animacionArrastre(){ /* TODO solo se puede mover con imagenes */
    arrastre = game.add.text(ejeX - 500, ejeY - 300, '', { fill: '#ff000e' });
    //arrastre.body.velocity.x=150;
    arrastre.text = "VAMOS DE ARRASTRE :D";
    game.time.events.add(Phaser.Timer.SECOND*4, borrarArrastre, this);

}

function borrarArrastre(){
    arrastre.destroy();
}

function sleep(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds){
            break;
        }
    }
}

/* MÚSICA */

var music;
var playPause;
function controlMusica(){
    music = game.add.audio('musica');
    playPause = game.add.text(0, ejeY/2, '', { fill: '#ffffff'});
    playPause.text = "MUSIKOTE";
    playPause.inputEnabled = true;
    playPause.events.onInputDown.add(botonMusica, this);
    music.play();
    playPause.estado = "pause";
}

function botonMusica(item){
    console.log("SE PULSA EL BOTON " + item.estado);
    if (item.estado == "playing"){
        console.log("LA PAUSO");
        music.pause();
        playPause.estado = "pause";
    }
    else{
        playPause.estado = "playing";
        music.resume();
    }
}