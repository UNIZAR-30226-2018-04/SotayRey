DELIMITER |

CREATE 
	EVENT `crear_torneo1` 
	ON SCHEDULE EVERY 30 SECOND
	DO
	BEGIN
		DECLARE i INTEGER;
		DECLARE p INTEGER;
		DECLARE d INTEGER;
		SET i = 3;
		SET p = 5;
		SET d = 7;
		INSERT INTO torneo (nombre,timeInicio,individual,descripcion) VALUES ('Torneo Semanal',DATE_ADD(NOW(), INTERVAL 1 DAY),TRUE,'El torneo de cada sábado');
		
		WHILE i > 0 DO
			INSERT INTO fase (num,torneo,premioPunt,premioDiv) VALUES (i, (SELECT MAX(id) FROM torneo WHERE nombre = 'Torneo Semanal'),p,d);
			SET i = i - 1;
			SET p = p*2;
			SET d = d*2;
		END WHILE;
	END |

DELIMITER ;