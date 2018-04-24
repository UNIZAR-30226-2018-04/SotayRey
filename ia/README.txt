-- Ejecutar el prototipo de IA en Python:
python3 ISMCTS2.py

-- Al final del fichero elegir en el main si se quiere ejecutar:
	PlayGame(): verbose, va mostrando el estado en cada movimiento
	PlayGame50(): no verbose, solo muestra el resultado final

-- Dentro de cualquiera de esas dos funciones se puede elegir descomentar una
	de las opciones siguientes:
	ISMCTS con 1000 iteraciones (peor pero mas rapido)
	ISMCTS con 5000 iteraciones (mejor pero mas lento)
	Algoritmo voraz (maximiza la puntuación en cada tirada)
	Jugador random
	Jugador real por terminal (se elige el índice de la carta que se desea tirar: cuidado en el arrastre con los índices, solo los posibles!)
