# This is a very simple Python 2.7 implementation of the Information Set Monte Carlo Tree Search algorithm.
# The function ISMCTS(rootstate, itermax, verbose = False) is towards the bottom of the code.
# It aims to have the clearest and simplest possible code, and for the sake of clarity, the code
# is orders of magnitude less efficient than it could be made, particularly by using a 
# state.GetRandomMove() or state.DoRandomRollout() function.
# 
# An example GameState classes for Knockout Whist is included to give some idea of how you
# can write your own GameState to use ISMCTS in your hidden information game.
# 
# Written by Peter Cowling, Edward Powley, Daniel Whitehouse (University of York, UK) September 2012 - August 2013.
# 
# Licence is granted to freely use and distribute for any sensible/legal purpose so long as this comment
# remains in any distributed code.
# 
# For more information about Monte Carlo Tree Search check out our web site at www.mcts.ai
# Also read the article accompanying this code at ***URL HERE***

from math import *
import random, sys
import copy

class GameState:
	""" A state of the game, i.e. the game board. These are the only functions which are
		absolutely necessary to implement ISMCTS in any imperfect information game,
		although they could be enhanced and made quicker, for example by using a 
		GetRandomMove() function to generate a random move during rollout.
		By convention the players are numbered 1, 2, ..., self.numberOfPlayers.
	"""
	def __init__(self):
		self.numberOfPlayers = 2
		self.playerToMove = 1
	
	def GetNextPlayer(self, p):
		""" Return the player to the left of the specified player
		"""
		return (p % self.numberOfPlayers) + 1
	
	def Clone(self):
		""" Create a deep clone of this game state.
		"""
		st = GameState()
		st.playerToMove = self.playerToMove
		return st
	
	def CloneAndRandomize(self, observer):
		""" Create a deep clone of this game state, randomizing any information not visible to the specified observer player.
		"""
		return self.Clone()
	
	def DoMove(self, move):
		""" Update a state by carrying out the given move.
			Must update playerToMove.
		"""
		self.playerToMove = self.GetNextPlayer(self.playerToMove)
		
	def GetMoves(self):
		""" Get all possible moves from this state.
		"""
		raise NotImplementedException()
	
	def GetResult(self, player):
		""" Get the game result from the viewpoint of player. 
		"""
		raise NotImplementedException()

	def __repr__(self):
		""" Don't need this - but good style.
		"""
		pass

class Card:
	""" A playing card, with rank and suit.
		rank must be an integer between 2 and 14 inclusive (Jack=11, Queen=12, King=13, Ace=14)
		suit must be a string of length 1, one of 'C' (Clubs), 'D' (Diamonds), 'H' (Hearts) or 'S' (Spades)
	"""
	def __init__(self, rank, suit):
		if rank not in range(0, 10):
			raise Exception("Invalid rank")
		if suit not in ['O', 'C', 'B', 'E']:
			raise Exception("Invalid suit")
		self.rank = rank
		self.suit = suit
		self.points = {0:0,1:0,2:0,3:0,4:0,5:2,6:3,7:4,8:10,9:11}[rank]

	def comparar(self,c,t):
		return (self.suit==c.suit and self.rank>c.rank) or (self.suit!=c.suit and c.suit!=t)
	
	def __repr__(self):
		return "24567CSR3A"[self.rank] + self.suit
	
	def __eq__(self, other):
		return self.rank == other.rank and self.suit == other.suit

	def __ne__(self, other):
		return self.rank != other.rank or self.suit != other.suit

class KnockoutWhistState(GameState):
	""" A state of the game Knockout Whist.
		See http://www.pagat.com/whist/kowhist.html for a full description of the rules.
		For simplicity of implementation, this version of the game does not include the "dog's life" rule
		and the trump suit for each round is picked randomly rather than being chosen by one of the players.
	"""
	def __init__(self, puntos = {1:0, 2:0}):
		""" Initialise the game state. n is the number of players (from 2 to 7).
		"""
		self.playerToMove   = random.randint(1,2)
		self.playerPoints   = {p:puntos[p] for p in range(1,3)}
		self.playerHands    = {p:[] for p in range(1,3)}
		self.vueltas 		= self.playerPoints[1]>0
		self.restantes      = [] #Cartas que quedan por jugar
		self.cantadas		= []
		self.triunfo        = None
		self.cartaTirada	= None
		self.Deal()
	
	def Clone(self):
		""" Create a deep clone of this game state.
		"""
		st = KnockoutWhistState()
		st.playerToMove = self.playerToMove
		st.playerHands  = copy.deepcopy(self.playerHands)
		st.vueltas		= self.vueltas
		st.restantes    = copy.deepcopy(self.restantes)
		st.cantadas 	= copy.deepcopy(self.cantadas)
		st.triunfo 		= self.triunfo
		st.cartaTirada  = self.cartaTirada
		st.playerPoints = copy.deepcopy(self.playerPoints)
		return st
	
	def CloneAndRandomize(self, observer):
		""" Create a deep clone of this game state, randomizing any information not visible to the specified observer player.
		"""
		st = self.Clone()
		
		# The observer can see his own hand and the cards in the current trick, and can remember the cards played in previous tricks
		# seenCards = st.playerHands[observer] + st.discards
		# The observer can't see the rest of the deck
		unseenCards = self.restantes + self.playerHands[3-observer]
		
		# Deal the unseen cards to the other players
		random.shuffle(unseenCards)
		# Anyado el triunfo
		unseenCards.append(self.triunfo)
		for p in range(1, 3):
			if p != observer:
				# Deal cards to player p
				# Store the size of player p's hand
				numCards = len(self.playerHands[p])
				# Give player p the first numCards unseen cards
				st.playerHands[p] = unseenCards[ : numCards]
				# Remove those cards from unseenCards
				unseenCards = unseenCards[numCards : ]
		
		st.restantes = unseenCards[:-1]
		return st
	
	def GetCardDeck(self):
		""" Construct a standard deck of 40 cards.
		"""
		return [Card(rank, suit) for rank in range(0, 10) for suit in ['O', 'C', 'B', 'E']]
	
	def Deal(self):
		""" Reset the game state for the beginning of a new round, and deal the cards.
		"""
		self.restantes = []
		
		# Construct a deck, shuffle it, and deal it to the players
		deck = self.GetCardDeck()
		random.shuffle(deck)
		for p in range(1, 3):
			self.playerHands[p] = deck[ : 6]
			deck = deck[6 : ]
		
		self.restantes = deck[:-1]
		# Choose the trump suit for this round
		self.triunfo = deck[-1]
	
	
	def DoMove(self, move):
		""" Update a state by carrying out the given move.
			Must update playerToMove.
		"""		
		# Remove the card from the player's hand
		self.playerHands[self.playerToMove].remove(move)
		

		if (self.cartaTirada is None):
			self.cartaTirada = move
			self.playerToMove = 3-self.playerToMove
		else:
			if self.cartaTirada.comparar(move,self.triunfo.suit):
				#Pierde el jugador de este movimiento
				self.playerPoints[3-self.playerToMove] += (self.cartaTirada.points + move.points)
				self.playerToMove = 3-self.playerToMove
			else:
				self.playerPoints[self.playerToMove] += (self.cartaTirada.points + move.points)
			#Robar carta
			cartasRestantes = self.restantes + [self.triunfo]
			#print(cartasRestantes)
			if len(cartasRestantes)>1:
				self.playerHands[self.playerToMove].append(cartasRestantes[0])
				self.playerHands[3-self.playerToMove].append(cartasRestantes[1])
				self.restantes = cartasRestantes[2:-1]
			self.cartaTirada = None

			# Comprobar si el que gana la ronda tiene cantes
			cantes = [(c1.suit) for c1 in self.playerHands[self.playerToMove] for c2 in self.playerHands[self.playerToMove] if (c1.rank==6 and c2.rank==7 and c1.suit==c2.suit and c1.suit not in self.cantadas)]
			self.cantadas += cantes
			cantes = [20 if c != self.triunfo.suit else 40 for c in cantes]
			self.playerPoints[self.playerToMove] += sum(cantes)

			# Sumar las diez ultimas
			if len(self.playerHands[self.playerToMove])==0 and len(self.playerHands[3-self.playerToMove])==0:
				self.playerPoints[self.playerToMove] += 10

			# Si tiene el 7 de triunfo lo cambia
			if Card(4,self.triunfo.suit) in self.playerHands[self.playerToMove]:
				self.playerHands[self.playerToMove].remove(Card(4,self.triunfo.suit))
				self.playerHands[self.playerToMove].append(self.triunfo)
				self.triunfo = Card(4,self.triunfo.suit)

			# Si se ha acabado la partida de idas sin ganador, se reparten las vueltas
			if len(self.playerHands[self.playerToMove]) == 0 and self.playerPoints[1]<101 and self.playerPoints[2]<101:
				self.playerToMove   = 3-self.playerToMove
				self.vueltas 		= True
				self.cantadas		= []
				self.cartaTirada	= None
				self.Deal()
	
	def GetMoves(self):
		""" Get all possible moves from this state.
		"""
		if self.vueltas and (self.playerPoints[1]>=101 or self.playerPoints[2]>=101):
			return []
		elif len(self.restantes)>0 or self.cartaTirada is None:
			return self.playerHands[self.playerToMove]
		# Arrastre
		else:
			#Cartas del palo
			cartasPalo = [c for c in self.playerHands[self.playerToMove] if c.suit == self.cartaTirada.suit]
			#Cartas triunfo
			cartasTriunfo = [c for c in self.playerHands[self.playerToMove] if c.suit == self.triunfo.suit]
			if len(cartasPalo)>0:
				#Si tiene cartas del palo tiene que matar si puede
				cartasPaloMatan = [c for c in cartasPalo if c.rank > self.cartaTirada.rank]
				return cartasPaloMatan if len(cartasPaloMatan)>0 else cartasPalo
			elif len(cartasTriunfo)>0:
				#Si tiene triunfo debe tirar
				return cartasTriunfo
			else:
				# Si no tiene nada pues que tire lo que tenga
				return self.playerHands[self.playerToMove]

	
	def GetResult(self, player):
		""" Get the game result from the viewpoint of player. 
		"""
		return 1 if (self.playerPoints[player]>=101) else 0
	
	def __repr__(self):
		""" Return a human-readable representation of the state
		"""
		result =  "Puntos 1: " + str(self.playerPoints[1]) + "\n"
		result += "Puntos 2: " + str(self.playerPoints[2]) + "\n"
		result += "Cartas 1: " + ",".join(str(card) for card in self.playerHands[1]) + "\n"
		result += "Cartas 2: " + ",".join(str(card) for card in self.playerHands[2]) + "\n"
		result += "Triunfo: " + str(self.triunfo) + "\n"
		result += "Cantadas: " + str(self.cantadas) + "\n"
		result += "Cartas Restantes: " + ",".join(str(card) for card in self.restantes) + "\n"
		result += "Turno: " + str(self.playerToMove) + "\n"

		return result

class Node:
	""" A node in the game tree. Note wins is always from the viewpoint of playerJustMoved.
	"""
	def __init__(self, move = None, parent = None, playerJustMoved = None):
		self.move = move # the move that got us to this node - "None" for the root node
		self.parentNode = parent # "None" for the root node
		self.childNodes = []
		self.wins = 0
		self.visits = 0
		self.avails = 1
		self.playerJustMoved = playerJustMoved # the only part of the state that the Node needs later
	
	def GetUntriedMoves(self, legalMoves):
		""" Return the elements of legalMoves for which this node does not have children.
		"""
		
		# Find all moves for which this node *does* have children
		triedMoves = [child.move for child in self.childNodes]
		
		# Return all moves that are legal but have not been tried yet
		return [move for move in legalMoves if move not in triedMoves]
		
	def UCBSelectChild(self, legalMoves, exploration = 0.7):
		""" Use the UCB1 formula to select a child node, filtered by the given list of legal moves.
			exploration is a constant balancing between exploitation and exploration, with default value 0.7 (approximately sqrt(2) / 2)
		"""
		
		# Filter the list of children by the list of legal moves
		legalChildren = [child for child in self.childNodes if child.move in legalMoves]
		
		# Get the child with the highest UCB score
		s = max(legalChildren, key = lambda c: float(c.wins)/float(c.visits) + exploration * sqrt(log(c.avails)/float(c.visits)))
		
		# Update availability counts -- it is easier to do this now than during backpropagation
		for child in legalChildren:
			child.avails += 1
		
		# Return the child selected above
		return s
	
	def AddChild(self, m, p):
		""" Add a new child node for the move m.
			Return the added child node
		"""
		n = Node(move = m, parent = self, playerJustMoved = p)
		self.childNodes.append(n)
		return n
	
	def Update(self, terminalState):
		""" Update this node - increment the visit count by one, and increase the win count by the result of terminalState for self.playerJustMoved.
		"""
		self.visits += 1
		if self.playerJustMoved is not None:
			self.wins += terminalState.GetResult(self.playerJustMoved)

	def __repr__(self):
		return "[M:%s W/V/A: %4i/%4i/%4i]" % (self.move, self.wins, self.visits, self.avails)

	def TreeToString(self, indent):
		""" Represent the tree as a string, for debugging purposes.
		"""
		s = self.IndentString(indent) + str(self)
		for c in self.childNodes:
			s += c.TreeToString(indent+1)
		return s

	def IndentString(self,indent):
		s = "\n"
		for i in range (1,indent+1):
			s += "| "
		return s

	def ChildrenToString(self):
		s = ""
		for c in self.childNodes:
			s += str(c) + "\n"
		return s


def ISMCTS(rootstate, itermax, verbose = False):
	""" Conduct an ISMCTS search for itermax iterations starting from rootstate.
		Return the best move from the rootstate.
	"""

	rootnode = Node()
	
	for i in range(itermax):
		node = rootnode
		
		# Determinize
		state = rootstate.CloneAndRandomize(rootstate.playerToMove)
		
		# Select
		while state.GetMoves() != [] and node.GetUntriedMoves(state.GetMoves()) == []: # node is fully expanded and non-terminal
			node = node.UCBSelectChild(state.GetMoves())
			state.DoMove(node.move)

		# Expand
		untriedMoves = node.GetUntriedMoves(state.GetMoves())
		if untriedMoves != []: # if we can expand (i.e. state/node is non-terminal)
			m = random.choice(untriedMoves) 
			player = state.playerToMove
			state.DoMove(m)
			node = node.AddChild(m, player) # add child and descend tree

		# Simulate
		while state.GetMoves() != []: # while state is non-terminal
			state.DoMove(random.choice(state.GetMoves()))

		# Backpropagate
		while node != None: # backpropagate from the expanded node and work back to the root node
			node.Update(state)
			node = node.parentNode

	# Output some information about the tree - can be omitted
	#if (verbose): print rootnode.TreeToString(0)
	#else: print rootnode.ChildrenToString()

	return max(rootnode.childNodes, key = lambda c: c.visits).move # return the move that was most visited

def greedyPlay(st):
	if st.cartaTirada is None:
		return min([(p.points,p) if p.suit!=st.triunfo.suit else (p.points+9,p) for p in st.playerHands[st.playerToMove]], key = lambda p: p[0])[1]
	else:
		return max([(-p.points-st.cartaTirada.points,p) if st.cartaTirada.comparar(p,st.triunfo.suit) else (p.points+st.cartaTirada.points,p) for p in st.playerHands[st.playerToMove]], key = lambda p: p[0])[1]

def PlayGame():
	""" Play a sample game between two ISMCTS players.
	"""
	state = KnockoutWhistState()
	
	while (state.GetMoves() != []):
		print(str(state))
		# Use different numbers of iterations (simulations, tree nodes) for different players
		if state.playerToMove == 1:
			# Arbol lento
			# m = ISMCTS(rootstate = state, itermax = 5000, verbose = False)
			# Arbol rapido
			# m = ISMCTS(rootstate = state, itermax = 1000, verbose = False)
			# Greedy
			m = greedyPlay(state)
			# Random
			# m = random.choice(state.GetMoves())
		else:
			# Arbol lento
			# m = ISMCTS(rootstate = state, itermax = 5000, verbose = False)
			# Arbol rapido
			# m = ISMCTS(rootstate = state, itermax = 1000, verbose = False)
			# Greedy
			# m = greedyPlay(state)
			# Random
			m = random.choice(state.GetMoves())
		print("Best Move: " + str(m) + "\n")
		state.DoMove(m)
	
	print("Finaaaaaaaal")
	print(state)
	for p in range(1, 3):
		if state.GetResult(p) > 0:
			print("Player " + str(p) + " wins!")
			someoneWon = True

def PlayGame50():
	""" Play a sample game between two ISMCTS players.
	"""
	puntosTotal = {1: 0, 2:0}
	partidasTotal = {1: 0, 2:0}
	for kk in range(0,50):
		state = KnockoutWhistState()	
		while (state.GetMoves() != []):
			if state.playerToMove == 1:
				# Arbol lento
				# m = ISMCTS(rootstate = state, itermax = 5000, verbose = False)
				# Arbol rapido
				m = ISMCTS(rootstate = state, itermax = 1000, verbose = False)
				# Greedy
				# m = greedyPlay(state)
				# Random
				# m = random.choice(state.GetMoves())
			else:
				# Arbol lento
				# m = ISMCTS(rootstate = state, itermax = 5000, verbose = False)
				# Arbol rapido
				# m = ISMCTS(rootstate = state, itermax = 1000, verbose = False)
				# Greedy
				m = greedyPlay(state)
				# Random
				# m = random.choice(state.GetMoves())
			state.DoMove(m)
		
		if state.GetResult(1)>0:
			partidasTotal[1] += 1
		else:
			partidasTotal[2] += 1
		puntosTotal[1] += state.playerPoints[1]
		puntosTotal[2] += state.playerPoints[2]

		print(str(kk)+":"+str(partidasTotal[1])+":"+str(partidasTotal[2])+":"+str(puntosTotal[1])+":"+str(puntosTotal[2]))

if __name__ == "__main__":
	PlayGame50()