//var ejeX = 800;
//var ejeY = 600;
var ejeX = window.innerWidth * window.devicePixelRatio;
//var ejeY = window.innerHeigth * window.devicePixelRatio;
var ejeY = window.innerHeight * window.devicePixelRatio;
var escalaCartas = 0.5;
var scaleRatio = 0.5;
var ejeXCarta = 80; // esto en una apartado de game options
var ejeYCarta = 123;
var escalaCarta = 1;
console.log(ejeX);
console.log(window.innerHeight);
console.log(window.devicePixelRatio);
console.log(ejeY);
//var scaleRatio = window.devicePixelRatio / 3;

// para que sea responsive:  https://www.joshmorony.com/how-to-scale-a-game-for-all-device-sizes-in-phaser/
// en vez de Phaser.AUTO -> Phaser.CANVAS
var game = new Phaser.Game(ejeX, ejeY, Phaser.CANVAS, '', { preload: preload, create: create, update: update, render: render });


function preload() {
    game.load.spritesheet("cartas", "assets/naipes.png", ejeXCarta, ejeYCarta);
    game.load.image('naipe', 'assets/naipe.png');
    game.load.image('cuadroCarta', 'assets/dragHere.png');
}

/* VARIABLES GLOBALES */

var numJugadores = 4; // Define el tipo de partida en función del número de jugadores

var misCartas;

// Variables de los jugadores
var miID = 0;


var jRef;
jRef = {};
jRef.nombre = "ref";
jRef.XPosMedia = ejeX / 2;
jRef.YPosMedia = ejeY * 0.80;
jRef.sumX = ejeXCarta;
jRef.sumY = 0;
jRef.XLanzar = jRef.XPosMedia;
jRef.YLanzar = jRef.YPosMedia - ejeYCarta * 1.5;
//jRef.YLanzar = 300;
jRef.cartaLanzada;
//jRef.numCartas;
jRef.dorso;
jRef.rotacion = 0;

var jIzq;
jIzq = {};
jIzq.XPosMedia = ejeX * 0.2;
jIzq.YPosMedia = ejeY / 2;
jIzq.sumX = 0;
jIzq.sumY = ejeXCarta; // es ejeX porque esta tumbada
jIzq.XLanzar = jIzq.XPosMedia + ejeYCarta * 1.10;
jIzq.YLanzar = jIzq.YPosMedia;
jIzq.cartaLanzada;
//jIzq.numCartas;
jIzq.dorso;
jIzq.rotacion = 90;


var jArriba;
jArriba = {};
jArriba.XPosMedia = ejeX / 2;
jArriba.YPosMedia = ejeY * 0.02;
jArriba.sumX = ejeXCarta;
jArriba.sumY = 0;
jArriba.XLanzar = jArriba.XPosMedia;
jArriba.YLanzar = jArriba.YPosMedia + ejeYCarta * 1.10;
jArriba.cartaLanzada;
//jArriba.numCartas;
jArriba.dorso;
jArriba.rotacion = 0;

var jDer;

var arrayJugadoresDefecto = [jRef, jArriba, jIzq, jDer];
var arrayJugadores = [];

/* Mapeo en funcion  del tipo de partida y mi identificador */
function mapearJugadores(){
    for (i = 0; i < numJugadores; i++) {
        arrayJugadores[i] = arrayJugadoresDefecto[(miID + i)%numJugadores];
    }
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
   }
}

function inicializarCuadroCarta(jugador){
    crearCuadroCarta(jugador);
    dibujarCuadroCarta(jugador);
}

function crearCuadroCarta(jugador){
    jugador.cartaLanzada = game.add.sprite(jugador.XLanzar, jugador.YLanzar, 'cuadroCarta');
}


function dibujarCuadroCarta(jugador){
    //var cuadro = game.add.sprite(jugador.XLanzar, jugador.YLanzar, 'cuadroCarta');
    jugador.cartaLanzada.loadTexture('cuadroCarta');
    jugador.cartaLanzada.width = ejeXCarta;
    jugador.cartaLanzada.height = ejeYCarta;
    jugador.cartaLanzada.angle = jugador.rotacion;
    //cuadro.scale.setTo(escalaCarta, escalaCarta);
}

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

function crearCartas(jugador){
    for (i = 0; i < 6; i++){
        jugador.cartasEnMano.create(0, 0, 'cartas');
    }

}

function dibujarJugador(identificador){
    var jugador = identificador;

    var i = - jugador.cartasEnMano.length/2; // Para que al final la posición sea centrada


    jugador.cartasEnMano.forEach(function(item) {
        item.x = jugador.XPosMedia + i*jugador.sumX;
        item.y = jugador.YPosMedia + i*jugador.sumY;
        //item.loadTexture(jugador.dorso, 0);
        if (jugador.nombre != "ref") {
            item.frame = 12; // TODO modificar esto para que sea el dorso
        }
        item.angle = jugador.rotacion;
        //item.scale.setTo(escalaCarta, escalaCarta);
        console.log("pongo carta en mesa");
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
function create() {

    //  We're going to be using physics, so enable the Arcade Physics system
    game.physics.startSystem(Phaser.Physics.ARCADE);
    game.stage.backgroundColor = "#009933";
    //misCartas = game.add.group();
    //anyadirCarta(misCartas,0);
    //anyadirCarta(misCartas,1);
    //actualizarCartas(misCartas);

    // Botones de acción
    cantarVeinte = game.add.text(ejeX - 300, ejeY - 200, '', { fill: '#ffffff' });
    cantarCuarenta = game.add.text(ejeX - 300, ejeY - 150, '', { fill: '#ffffff' });
    cambiarTriunfo = game.add.text(ejeX - 300, ejeY - 100, '', { fill: '#ffffff' });

    cantarVeinte.text = "CANTAR 20";
    cantarCuarenta.text = "CANTAR 40";
    cambiarTriunfo.text = "CAMBIAR TRIUNFO";

    // Los huecos para tirar las cartas de los jugadores
    //yo = game.add.sprite(game.world.centerX-50, game.world.centerY-50, 'cuadroCarta');
    //yo.scale.setTo(0.1, 0.1);


    // Pruebas

    mapearJugadores();

    jArriba.cartasEnMano = game.add.group();
    jIzq.cartasEnMano = game.add.group();
    jRef.cartasEnMano = game.add.group();
    inicializarJugadores();
    dibujarJugador(jArriba);
    dibujarJugador(jIzq);
    inicializarRef();


}

function onDragStop(sprite) {

    sprite.x = sprite.origenX;
    sprite.y = sprite.origenY;


}

function listener (item) {
    console.log("pulsaron la cartita");
    console.log(item.nombre);
}


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


/* PROXY */

function enviarMensaje(mensaje){
    // TODO se envia el mensaje al backend
}

function recibirMensaje(msg){
    // TODO hay que parsear el mensaje
}


function crearCarta(numero, palo){
    var carta = game.add.sprite(0, 0, 'cartas');
    console.log("BUSCO CARTA" + buscarCarta(numero, palo));
    carta.frame = buscarCarta(numero, palo);
    return carta;
}

/* Elimina la carta del mazo (cualquiera si no es referencia) y la pone en el centro en la variable cartaLanzada */
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
            if (salir == false && item.numero == numero && item.palo == palo){ // Porque se elimina la carta entonces la funcion no encuentra la carta, por eso el salir
                console.log("EJECUTO ESTO");
                jugador.cartasEnMano.removeChild(item);
                dibujarJugador(jugador);
                jugador.cartaLanzada = crearCarta(numero, palo);
                dibujarCartaLanzada(jugador);
                salir = true;
            }
        }, this);
    }
}

function rondaAcabada(){
    // cambiar todos los cuadros de lanzar por el sprite del cuadro de lanzar
    for (i = 0; i < numJugadores; i++){
        console.log(i);
        var jugador = arrayJugadores[i];
        dibujarCuadroCarta(jugador);
    }
}

/* Devuelve el numero a indexar en el sprite de cartas */
function buscarCarta(numero, palo){
    var indice = numero - 1;
    console.log("EL indice es: " + numero);
    var numCartas = 13;
    if (palo == "oros"){

    }
    else if (palo == "copas"){
        indice = indice + numCartas;
    }
    else if (palo == "espadas"){
        indice = indice + numCartas*2;
    }
    else { // palo == "bastos"
        indice = indice + numCartas*3;
    }
    console.log("EL indice es: " + indice);
    return indice;
}