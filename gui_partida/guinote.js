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


function preload() {
    game.load.spritesheet("cartas", "assets/naipes.png", ejeXCartaOriginal, ejeYCartaOriginal);
    game.load.image('naipe', 'assets/naipe.png');
    game.load.image('cuadroCarta', 'assets/dragHere.png');
}

/* VARIABLES GLOBALES */

var numJugadores = 4; // Define el tipo de partida en función del número de jugadores

var misCartas;

// Variables de los jugadores
var miID = 0;


/* se inicializan con la funcion inicializarDispositivo */
var jRef;
var jIzq;
var jArriba;
var jDer;


var triunfo;
triunfo = {};
triunfo.carta = {}
triunfo.x = ejeX / 2;
triunfo.y = (ejeY / 2) * 0.85;

var arrayJugadoresDefecto = [jRef, jArriba, jIzq, jDer];
var arrayJugadores = [];

/* Inicializa las variables que dependen de la resolucion del dispositivo */
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
    jRef.YLanzar = jRef.YPosMedia - ejeYCarta * 1.5;
//jRef.YLanzar = 300;
    jRef.cartaLanzada;
//jRef.numCartas;
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
//jIzq.numCartas;
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
//jArriba.numCartas;
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
//jArriba.numCartas;
    jDer.dorso;
    jDer.rotacion = 270;
}

/* Mapeo en funcion  del tipo de partida y mi identificador */
function mapearJugadores(){
    for (i = 0; i < numJugadores; i++) {
        arrayJugadores[i] = arrayJugadoresDefecto[(miID + i)%numJugadores];
    }
}

function modificarTriunfo(numero, palo){
    triunfo.carta = crearCarta(numero, palo);
    triunfo.carta.x = triunfo.x;
    triunfo.carta.y = triunfo.y;

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
function create() {

    //  We're going to be using physics, so enable the Arcade Physics system
    game.physics.startSystem(Phaser.Physics.ARCADE);
    game.stage.backgroundColor = "#009933";
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

    mapearJugadores();

    jArriba.cartasEnMano = game.add.group();
    jIzq.cartasEnMano = game.add.group();
    jRef.cartasEnMano = game.add.group();
    jDer.cartasEnMano = game.add.group();
    inicializarJugadores();
    dibujarJugador(jArriba);
    dibujarJugador(jIzq);
    dibujarJugador(jDer);
    inicializarRef();

    modificarTriunfo(12, "copas");


    // Simulacion partida


    //jugadorLanzaCarta(0, 3, "oros");

    //jugadorLanzaCarta(1, 3, "bastos");


    //jugadorLanzaCarta(2, 6, "espadas");

    //jugadorLanzaCarta(3, 11, "copas");

}

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
    carta.numero = numero;
    carta.palo = palo;
    console.log("BUSCO CARTA" + buscarCarta(numero, palo));
    carta.frame = buscarCarta(numero, palo);
    return carta;
}

function crearDorso(numero, palo){
    var carta = game.add.sprite(0, 0, 'cartas');
    carta.frame = 13;
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
           // console.log("entro a buscar en el referencia" + item.numero + " " + item.palo);
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

/* Un jugador roba una carta, si no es el referencia da igual el numero y palo */
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
        jugador.cartasEnMano.add(carta);
        dibujarJugador(jugador);
        dibujarCartaLanzada(jugador);
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

/* HUD */

function dibujarBotones(){
    var espacioBoton = ejeX / numBotones;
    //var style = {font: "20px", fill: "#000000", align:"center"};

    var cantarVeinte = game.add.text(0, ejeYBotones, '', { fill: '#000000'});
    //var cantarVeinte = game.add.text(0, ejeYBotones, '', style);
    var cantarCuarenta = game.add.text(espacioBoton, ejeYBotones, '', { fill: '#000000' });
    var cambiarTriunfo = game.add.text(espacioBoton*2, ejeYBotones, '', {fill: '#000000'});

    cantarVeinte.text = "CANTAR 20";
    cantarCuarenta.text = "CANTAR 40";
    cambiarTriunfo.text = "CAMBIAR TRIUNFO";
    cambiarTriunfo.text.fontSize = "20";
}

/* ANIMACIONES */

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