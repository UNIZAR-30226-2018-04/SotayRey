-- Create tables de la base de datos sotayrey_db

USE DATABASE sotayrey_db;

CREATE TABLE usuario (
    username VARCHAR(15) PRIMARY KEY,
    hash ,
    salt ,
    correo VARCHAR(320) NOT NULL,
    token ,
    puntuacion UNSIGNED INT NOT NULL,
    divisa UNSIGNED INT NOT NULL,
    timeEspera DATETIME,
    puesto UNSIGNED INT -- no puede ser 0
);

CREATE TABLE partida (
    id SERIAL PRIMARY KEY,
    timeInicio DATETIME NOT NULL,
    timeFin DATETIME,   -- será null cuando una partida esté en curso
    publica BOOL NOT NULL,    -- true si publica, false si privada
    ganador UNSIGNED INT  -- solo puede ser 0:abandonada,1:equipo1 ganador,2:equipo2 ganador o NULL:partida en curso
);

CREATE TABLE torneo (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    timeInicio DATETIME NOT NULL,
    timeCreacion DATETIME NOT NULL,
    descripcion TEXT,
    individual BOOL NOT NULL -- true si uno contra uno, false si parejas
);

CREATE TABLE fase (
    num UNSIGNED INT,
    torneo SERIAL,
    premioPunt ,    -- La cantidad de puntuación que recibe el equipo ganador en esa fase
    premioDiv ,     -- La cantidad de divisa que recibe el equipo ganador en esta fase
    FOREIGN KEY torneo REFERENCES torneo(id) ON DELETE CASCADE ON UPDATE CASCADE -- cuidado! Cascades de foreign key no activan triggers

);
