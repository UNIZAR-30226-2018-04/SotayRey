DROP EVENT IF EXISTS torneo_semanal;


DELIMITER |

CREATE 
	EVENT `torneo_semanal` 
	ON SCHEDULE EVERY 1 WEEK
	DO
	BEGIN
		DECLARE i INTEGER;
		DECLARE p INTEGER;
		DECLARE d INTEGER;
		SET i = 3;
		SET p = 5;
		SET d = 7;
		INSERT INTO torneo (nombre,timeInicio,individual,descripcion) VALUES ('torneo_semanal',DATE_ADD(NOW(), INTERVAL 1 DAY),TRUE,'El torneo de cada sábado');
		
		WHILE i > 0 DO
			INSERT INTO fase (num,torneo,premioPunt,premioDiv) VALUES (i, (SELECT MAX(id) FROM torneo WHERE nombre = 'torneo_semanal'),p,d);
			SET i = i - 1;
			SET p = p*2;
			SET d = d*2;
		END WHILE;
	END |

DELIMITER ;
