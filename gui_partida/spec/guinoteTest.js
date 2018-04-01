describe("PROBANDO UNAS RONDAS DE 4 JUGADORES", function() {

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



    it("el estado inicial debe estar representado", function() {
        recibirMensaje(JSON.stringify(estado_inicial));
        expect(jArriba.numCartas).toBe(6);
    });


    it("jugador con id 0 lanza una carta que tiene", function() {
        jugadorLanzaCarta(0, 4, "bastos");
        expect(arrayJugadores[0].cartaLanzada.numero).toBe(4);
    });

    it("jugador con id 1 lanza una carta", function() {
        jugadorLanzaCarta(1, 1, "bastos");
        expect(arrayJugadores[1].cartaLanzada.numero).toBe(1);
    });
    it("jugador con id 2 lanza una carta", function() {
        jugadorLanzaCarta(2, 2, "bastos");
        expect(arrayJugadores[2].cartaLanzada.numero).toBe(2);
    });

    it("jugador con id 3 lanza una carta", function() {
        jugadorLanzaCarta(3, 3, "bastos");
        expect(arrayJugadores[3].cartaLanzada.numero).toBe(3);
    });


    it("RONDA ACABADA", function() {
        rondaAcabada();
        expect(arrayJugadores[0].cartaLanzada.numero).toBe(0);
    });


});