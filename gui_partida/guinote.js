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

}

function create() {

    //  We're going to be using physics, so enable the Arcade Physics system
    game.physics.startSystem(Phaser.Physics.ARCADE);
    game.stage.backgroundColor = "#009933";

    
    // Juego de cartas
    
 // Add some items to left side, and set a onDragStop listener
    // to limit its location when dropped.

    var cartas = game.add.group();
    cartas.inputEnabled = true;
    //cartas.input.enableDrag(true);

    for (var i = 0; i < 6; i++)
    {
    	//stars.create(i * 70, 0, 'star');
        // Directly create sprites on the left group.
    	
    	//var carta = game.add.sprite(ejeXCarta * escalaCarta / -2, ejeY / 2, "cartas");
    	var carta = cartas.create(ejeXCarta * escalaCarta * i, ejeY -  200, 'cartas', i);
    	//carta.anchor.set(0.5);
        carta.scale.setTo(escalaCarta, escalaCarta);
        carta.frame = i + 10;
        //carta.width = ejeXCarta * escalaCarta;
        //carta.height = ejeYCarta * escalaCarta;
        //carta.smoothed = false;
    	
    	//var carta = cartas.create(95 * i, ejeY -  200, 'naipe', i); el bueno
        //var carta = game.add.sprite(15 * i, 400 , 'naipe', i);
        carta.nombre = "carta bonita";
        carta.events.onInputDown.add(listener, this);
        carta.inputEnabled = true;
        // Enable input detection, then it's possible be dragged.
        

        // Make this item draggable.
        //carta.input.enableDrag(true);
        //carta.scale.setTo(scaleRatio, scaleRatio);
        
        // Then we make it snap to left and right side,
        // also we make it only snap when released.
        //item.input.enableSnap(90, 90, false, true);

        // Limit drop location to only the 2 columns.
        //item.events.onDragStop.add(fixLocation);
    }
    console.log("probando la consola");
    cartas.forEach(function(item) {
        console.log("consolita");
        console.log(item.nombre);
    }, this);
    
    
    
    // Botones de acción
    cantarVeinte = game.add.text(ejeX - 300, ejeY - 200, '', { fill: '#ffffff' });
    cantarCuarenta = game.add.text(ejeX - 300, ejeY - 150, '', { fill: '#ffffff' });
    cambiarTriunfo = game.add.text(ejeX - 300, ejeY - 100, '', { fill: '#ffffff' });
    
    cantarVeinte.text = "CANTAR 20";
    cantarCuarenta.text = "CANTAR 40";
    cambiarTriunfo.text = "CAMBIAR TRIUNFO";
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