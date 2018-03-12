//var ejeX = 800;
//var ejeY = 600;
var ejeX = window.innerWidth * window.devicePixelRatio;
//var ejeY = window.innerHeigth * window.devicePixelRatio;
var ejeY = window.innerHeight * window.devicePixelRatio;
var escalaCartas = 0.5;
var scaleRatio = 0.5;
console.log(ejeX);
console.log(window.innerHeight);
console.log(window.devicePixelRatio);
console.log(ejeY);
//var scaleRatio = window.devicePixelRatio / 3;

// para que sea responsive:  https://www.joshmorony.com/how-to-scale-a-game-for-all-device-sizes-in-phaser/
// en vez de Phaser.AUTO -> Phaser.CANVAS
var game = new Phaser.Game(ejeX, ejeY, Phaser.CANVAS, '', { preload: preload, create: create, update: update, render: render });


function preload() {
	game.load.image('naipe', 'assets/naipe.png');
	game.load.image('diamond', 'assets/diamond.png');
    game.load.image('sky', 'assets/sky.png');
    game.load.image('ground', 'assets/platform.png');
    game.load.image('star', 'assets/star.png');
    game.load.spritesheet('dude', 'assets/dude.png', 32, 48);

}

var player;
var platforms;
var cursors;

var stars;
var score = 0;
var scoreText;

function create() {

    //  We're going to be using physics, so enable the Arcade Physics system
    game.physics.startSystem(Phaser.Physics.ARCADE);

    //  A simple background for our game
    game.add.sprite(0, 0, 'sky');

    //  The platforms group contains the ground and the 2 ledges we can jump on
    platforms = game.add.group();

    //  We will enable physics for any object that is created in this group
    platforms.enableBody = true;

    // Here we create the ground.
    var ground = platforms.create(0, game.world.height - 64, 'ground');

    //  Scale it to fit the width of the game (the original sprite is 400x32 in size)
    ground.scale.setTo(2, 2);

    //  This stops it from falling away when you jump on it
    ground.body.immovable = true;

    //  Now let's create two ledges
    var ledge = platforms.create(400, 400, 'ground');
    ledge.body.immovable = true;

    ledge = platforms.create(-150, 250, 'ground');
    ledge.body.immovable = true;

    // The player and its settings
    player = game.add.sprite(32, game.world.height - 150, 'dude');

    //  We need to enable physics on the player
    game.physics.arcade.enable(player);

    //  Player physics properties. Give the little guy a slight bounce.
    player.body.bounce.y = 0.2;
    player.body.gravity.y = 300;
    player.body.collideWorldBounds = true;

    //  Our two animations, walking left and right.
    player.animations.add('left', [0, 1, 2, 3], 10, true);
    player.animations.add('right', [5, 6, 7, 8], 10, true);
    
    player.inputEnabled = true;
    player.input.enableDrag();

    //  Finally some stars to collect
    stars = game.add.group();

    //  We will enable physics for any star that is created in this group
    stars.enableBody = true;

    //  Here we'll create 12 of them evenly spaced apart
    for (var i = 0; i < 12; i++)
    {
        //  Create a star inside of the 'stars' group
        var star = stars.create(i * 70, 0, 'star');

        //  Let gravity do its thing
        star.body.gravity.y = 300;

        //  This just gives each star a slightly random bounce value
        star.body.bounce.y = 0.7 + Math.random() * 0.2;
    }

    //  The score
    scoreText = game.add.text(16, 16, 'score: 0', { fontSize: '32px', fill: '#000' });

    //  Our controls.
    cursors = game.input.keyboard.createCursorKeys();
    
    
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
    	var carta = cartas.create(15 * i, ejeY -  200, 'naipe', i);
        //var carta = game.add.sprite(15 * i, 400 , 'naipe', i);
        carta.nombre = "carta bonita";

        // Enable input detection, then it's possible be dragged.
        

        // Make this item draggable.
        //carta.input.enableDrag(true);
        carta.scale.setTo(scaleRatio, scaleRatio);
        
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
    
    
    
}

function render() {

    // Input debug info
    game.debug.inputInfo(32, 32);
    //game.debug.spriteInputInfo(sprite, 32, 130);
    game.debug.pointer( game.input.activePointer );

}

function update() {

    //  Collide the player and the stars with the platforms
    var hitPlatform = game.physics.arcade.collide(player, platforms);
    game.physics.arcade.collide(stars, platforms);

    //  Checks to see if the player overlaps with any of the stars, if he does call the collectStar function
    game.physics.arcade.overlap(player, stars, collectStar, null, this);

    //  Reset the players velocity (movement)
    player.body.velocity.x = 0;

    if (cursors.left.isDown)
    {
    	client.send("hola holita");
    	//document.write(websocket);
    	//websocket.send("mensajito wapo");
        //  Move to the left
        player.body.velocity.x = -150;

        player.animations.play('left');
    }
    else if (cursors.right.isDown)
    {
        //  Move to the right
        player.body.velocity.x = 150;

        player.animations.play('right');
    }
    else
    {
        //  Stand still
        player.animations.stop();

        player.frame = 4;
    }
    
    //  Allow the player to jump if they are touching the ground.
    if (cursors.up.isDown && player.body.touching.down && hitPlatform)
    {
        player.body.velocity.y = -350;
    }

}

function collectStar (player, star) {
    
    // Removes the star from the screen
    star.kill();

    //  Add and update the score
    score += 10;
    scoreText.text = 'Score: ' + score;

}