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
    game.load.image('cuadroCarta', 'assets/cuadroCarta.png');

}

/* VARIABLES GLOBALES */

var misCartas;


function create() {

    //  We're going to be using physics, so enable the Arcade Physics system
    game.physics.startSystem(Phaser.Physics.ARCADE);
    game.stage.backgroundColor = "#009933";
    misCartas = game.add.group();
    anyadirCarta(misCartas,0);
    anyadirCarta(misCartas,1);
    actualizarCartas(misCartas);

    // Botones de acción
    cantarVeinte = game.add.text(ejeX - 300, ejeY - 200, '', { fill: '#ffffff' });
    cantarCuarenta = game.add.text(ejeX - 300, ejeY - 150, '', { fill: '#ffffff' });
    cambiarTriunfo = game.add.text(ejeX - 300, ejeY - 100, '', { fill: '#ffffff' });

    cantarVeinte.text = "CANTAR 20";
    cantarCuarenta.text = "CANTAR 40";
    cambiarTriunfo.text = "CAMBIAR TRIUNFO";

    // Los huecos para tirar las cartas de los jugadores
    yo = game.add.sprite(game.world.centerX-50, game.world.centerY-50, 'cuadroCarta');
    yo.scale.setTo(0.1, 0.1);
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

var tipoPartida = 0; // 0 = 1vs1, 1 = 2vs2
function enviarMensaje(mensaje){
    // TODO se envia el mensaje al backend
}

function recibirMensaje(){

}