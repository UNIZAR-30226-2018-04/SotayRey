import random
import math

triunfo = random.randint(0,3)

def palo(p):
	return {0:"Oros",1:"Copas",2:"Espadas",3:"Bastos"}[p]

def nombreCarta(c):
	return {0:"Dos",1:"Cuatro",2:"Cinco",3:"Seis",4:"Siete",5:"Caballo",6:"Sota",7:"Rey",8:"Tres",9:"As"}[c]

def puntos(c):
	return {0:0,1:0,2:0,3:0,4:0,5:2,6:3,7:4,8:10,9:11}[c]

def ganador(a,b,t):
	if (int(a/10)==int(b/10)):
		if (a>b):
			return (1,puntos(a%10)+puntos(b%10))
		else:
			return (0,-puntos(a%10)-puntos(b%10))
	elif t:
		if int(b/10)==triunfo:
			return (0,-puntos(a%10)-puntos(b%10))
		else:
			return (1, puntos(a%10)+puntos(b%10))
	else:
		if int(a/10)==triunfo:
			return (1,puntos(a%10)+puntos(b%10))
		else:
			return (0, -puntos(a%10)-puntos(b%10))

def cantes(mano):
	cantes = 0
	cantes = 20*all(elem in mano for elem in [7,6])
	cantes = 20*all(elem in mano for elem in [17,16])
	cantes = 20*all(elem in mano for elem in [27,26])
	cantes = 20*all(elem in mano for elem in [37,36])
	cantes = cantes+20*all(elem in mano for elem in [triunfo*10+7,triunfo*10+6])
	return cantes


def simular(mano1,mano2,restantes,turno):
	if len(mano1)==0:
		return 0
	eleccion1 = random.randint(0,len(mano1)-1)
	eleccion2 = random.randint(0,len(mano2)-1)
	(res,ptos) = ganador(mano1[eleccion1],mano2[eleccion2],turno)
	if len(restantes)>0:
		if len(restantes)==1:
			print("Aqui mal")
			print(restantes)
			print(mano1)
			print(mano2)
			print(res)
			print(eleccion1)
			print(eleccion2)
		mano1 = mano1[:eleccion1] + [restantes[1-res]] + mano1[eleccion1+1:]
		mano2 = mano2[:eleccion2] + [restantes[res]] + mano2[eleccion2+1:]
	else:
		#No quedan cartas
		mano1 = mano1[:eleccion1] + mano1[eleccion1+1:]
		mano2 = mano2[:eleccion2] + mano2[eleccion2+1:]		
	return ptos + cantes(mano1)*turno - cantes(mano2)*(1-turno) + simular(mano1,mano2,restantes[2:],res)

def elegirCarta(mano,sucarta,restantes,turno):
	mejorCarta = 0
	mejorRes = float("-inf")
	for i in range(0,len(mano)):
		resulSim = 0
		for j in range(0,5000):
			random.shuffle(restantes)
			restantesCopy = restantes.copy()
			#Si el rival ya habia jugado o no
			if sucarta>=0:
				(res,ptos) = ganador(mano[i],sucarta,turno)
			else:
				(res,ptos) = ganador(mano[i],restantesCopy[0],turno)
				restantesCopy = restantesCopy[1:]

			#Si se puede robar o no
			# if len(restantesCopy)%2 == 1:
			# 	print("Aca")
			# 	print(mano)
			# 	print(sucarta)
			# 	print(restantesCopy)
			if len(restantesCopy)>3:
				mano1 = mano[:i] + [restantesCopy[0]] + mano[i+1:]
				mano2 = restantesCopy[1:4]
				resulSim = resulSim + ptos + simular(mano1,mano2,restantesCopy[4:],res)
			else:
				mano1 = mano[:i] + mano[i+1:]
				mano2 = restantesCopy
				resulSim = resulSim + ptos + simular(mano1,mano2,[],res)
			del restantesCopy

		print("Simulacion: "+nombreCarta(mano[i]%10)+" "+palo(int(mano[i]/10))+" :"+str(resulSim))
		if (resulSim>mejorRes):
			mejorRes = resulSim
			mejorCarta = i
	return mejorCarta

ganadasIA = 0
cantadasIA = 0
ganadasRival = 0
cantadasRival = 0

#elegirCarta([12,13,14],0,[21,22,23,24],False)

for kk in range(0,100):
	triunfo = random.randint(0,3)
	print("Triunfo: " + palo(triunfo))
	baraja = list(range(0,40))
	random.shuffle(baraja)
	manoIA,baraja = baraja[:3], baraja[3:]
	turnoIA = True
	puntosIA, puntosRival = 0,0
	while(len(baraja)>0):
		print("Cartas Rival: "+', '.join(list(map(lambda x: nombreCarta(x%10)+" "+palo(int(x/10)),baraja[:3]))))
		print("Cartas IA: "+', '.join(list(map(lambda x: nombreCarta(x%10)+" "+palo(int(x/10)),manoIA))))
		cantesIA = cantes(manoIA)*turnoIA
		cantesRival = cantes(baraja[:3])*(not turnoIA)
		if(cantesIA>0):
			cantadasIA = cantadasIA + 1
			print("¡¡¡¡ Cantador IA !!!!")
		if (cantesRival>0):
			cantesRival = cantesRival + 1
			print("¡¡¡¡ Cantador Rival !!!!")

		if (not turnoIA):
			#Elige una carta el rival
			indexRival = random.randint(0,min(2,len(baraja)-1))
			#Sacamo la carta del rival y la borramos
			cartaRival = baraja[indexRival]
			del baraja[indexRival]
			#Se simula para elegir carta
			indexIA = elegirCarta(manoIA.copy(),cartaRival,baraja.copy(),False)
			cartaIA = manoIA[indexIA]
			del manoIA[indexIA]
			print("Rival Tira: "+nombreCarta(cartaRival%10)+" "+palo(int(cartaRival/10)))
			print("IA Tira: "+nombreCarta(cartaIA%10)+" "+palo(int(cartaIA/10)))
		else:
			#Se simula para elegir carta
			indexIA = elegirCarta(manoIA.copy(),-1,baraja.copy(),True)
			cartaIA = manoIA[indexIA]
			del manoIA[indexIA]	
			#Elige una carta el rival
			indexRival = random.randint(0,min(2,len(baraja)-1))	
			#Sacamos la carta del rival y la borramos
			cartaRival = baraja[indexRival]
			del baraja[indexRival]
			print("IA Tira: "+nombreCarta(cartaIA%10)+" "+palo(int(cartaIA/10)))
			print("Rival Tira: "+nombreCarta(cartaRival%10)+" "+palo(int(cartaRival/10)))
			

		#Obtenemos el ganador de la ronda
		(resRonda,ptosRonda) = ganador(cartaIA,cartaRival,turnoIA)

		if (len(baraja)>3):
			manoIA = manoIA + [baraja[2+1-resRonda]]
			del baraja[2+1-resRonda]

		if resRonda>0:
			print("Gana IA")
			turnoIA = True
			puntosIA = puntosIA + ptosRonda + cantesIA
		else:
			print("Gana Rival")
			turnoIA = False
			puntosRival = puntosRival - ptosRonda + cantesRival

		print("Puntos IA: " + str(puntosIA))
		print("Puntos Rival: " + str(puntosRival))
		print(30*"-")
	print(30*"+")
	if puntosIA>puntosRival:
		print("Ganador final: IA")
		ganadasIA = ganadasIA + 1
	else:
		print("Ganador final: Rival")
		ganadasRival = ganadasRival + 1
	print(str(kk)+":"+str(puntosIA)+":"+str(puntosRival)+":"+str(cantadasIA)+":"+str(cantadasRival)+":"+str(ganadasIA)+":"+str(ganadasRival))
	print(30*"+")

