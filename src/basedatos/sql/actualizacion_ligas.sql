CREATE 
	EVENT `actualizacion_ligas` 
	ON SCHEDULE EVERY 1 DAY STARTS '2018-05-04 03:00:00'
	DO
		INSERT INTO pertenece_liga(usuario,liga)
		SELECT perus.user, l.nombre
		FROM
			(SELECT ((@rank:=@rank+1)/nu.numUsers)*100 AS porcentaje, u1.username user, u1.puntuacion
			FROM (SELECT @rank:=0) r, usuario u1, (SELECT COUNT(*) numUsers FROM usuario) nu
			ORDER BY puntuacion DESC) perus, liga l
		WHERE l.porcentaje_min < perus.porcentaje AND perus.porcentaje <= l.porcentaje_max;
