/*SELECT p.id, p.timeInicio, p.timeFin, p.publica, p.ganador, 
       j1.usuario usuario1, j2.usuario usuario2, 
       j1.cuarentas cuarentas1, j2.cuarentas cuarentas2, 
       j1.veintes veintes1, j2.veintes veintes2, 
       j1.puntos puntos1, j2.puntos puntos2, 
       (j1.abandonador + 2*j2.abandonador) abandonador  
FROM partida p, juega j1, juega j2
WHERE p.id = j1.partida AND p.id = j2.partida 
      AND (j1.usuario = 'serizba' OR j2.usuario = 'serizba') 
      AND j1.usuario > j2.usuario 
      AND (NOT EXISTS (SELECT * FROM juega jj WHERE jj.partida = p.id 
      AND j1.usuario <> jj.usuario AND j2.usuario <> jj.usuario));


SELECT p.id, p.timeInicio, p.timeFin, p.publica, p.ganador,
       j1.usuario usuario1, j2.usuario usuario2,
       j3.usuario usuario3, j4.usuario usuario4,
       j1.cuarentas cuarentas1, j2.cuarentas cuarentas2,
       j3.cuarentas cuarentas3, j4.cuarentas cuarentas4, 
       j1.veintes veintes1, j2.veintes veintes2, 
       j3.veintes veintes3, j4.veintes veintes4, 
       j1.puntos puntos1, j2.puntos puntos2, 
       j3.puntos puntos3, j4.puntos puntos4, 
       (j1.abandonador + 2*j2.abandonador + 3*j3.abandonador + 4*j4.abandonador) abandonador 
FROM partida p, juega j1, juega j2, juega j3, juega j4
WHERE p.id = j1.partida AND p.id = j2.partida AND p.id = j3.partida AND p.id = j4.partida 
       AND (j1.usuario = 'serizba' OR j2.usuario = 'serizba' 
       OR j3.usuario = 'serizba' OR j4.usuario = 'serizba') 
       AND j1.usuario > j2.usuario AND j2.usuario > j3.usuario AND j3.usuario > j4.usuario;


*/


SELECT p.id, p.timeInicio, p.timeFin, p.publica,
                    j1.usuario usuario1, j2.usuario usuario2,
                    j3.usuario usuario3, j4.usuario usuario4,
                    j1.equipo equipo1, j2.equipo equipo2, 
                    j3.equipo equipo3, j4.equipo equipo4 
FROM partida p, juega j1, juega j2, juega j3, juega j4
WHERE p.id = j1.partida AND p.id = j2.partida AND p.id = j3.partida AND p.id = j4.partida 
					AND p.publica = 1 AND p.timeFin IS NULL
                    AND j1.usuario > j2.usuario AND j2.usuario > j3.usuario AND j3.usuario > j4.usuario;