package main.java;




/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Estado de la partida del guiñote que permite ver las cartas de la partida y sus jugadores
 */

import java.util.ArrayList;
import java.util.Random;

/**
 * Mazo:  almacena todas las cartas no utilizadas en la partida de la baraja española
 * CartasEnTapete: las cartas que están en la mesa en un turno
 * Jugadores:  información de cada uno de los jugadores que están
 * participando, almacenada en el mismo orden en el que jugarán. Si se juega
 * por equipos se almacena [j1_equipo1, j2_equipo2, j3_equipo1, j4_equipo2]
 * Turno:  indíce del jugador que tiene que lanzar lanzar la carta
 * Triunfo:  carta que indica el palo al que se juega
 */
public class EstadoPartidaIA {

    private int turno;

    //mano y puntos
    //private ArrayList<Jugador> jugadores;
    private Integer puntosIA;
    private Integer puntosRival;
    private ArrayList<Carta> manoIA;
    private ArrayList<Carta> manoRival;

    private ArrayList<Carta> mazo;

    private boolean vueltas;

    private boolean cantes[] = {false, false, false, false};

    private Carta triunfo;
    private ArrayList<Carta> cartasEnTapete;
    private Carta cartaTirada;

    private ArrayList<Carta> restantes;



    private boolean triunfo_entregado;
    private Random random;

    public static void main(String[] args){
        EstadoPartidaIA p= new EstadoPartidaIA();
        EstadoPartidaIA copia = p.CloneAndRandomize();
        for (int i=0;i<28;i++) {
            ArrayList<Carta> movs = copia.getMoves();
            copia.doMove(movs.get(0));
        }
        boolean ganaIA=false, ganaRival=false;
        while(!ganaIA && !ganaRival) {
            ArrayList<Carta> movs = copia.getMoves();
            copia.doMove(movs.get(0));
            ganaIA=copia.getResult(0);
            ganaRival=copia.getResult(1);
        }
    }

    public EstadoPartidaIA Clone() {
        return new EstadoPartidaIA(this);
    }


    public EstadoPartidaIA CloneAndRandomize(){
        EstadoPartidaIA clonacion = new EstadoPartidaIA(this);

        //ArrayList<Carta> cartas = crearBaraja();
        Carta uno, dos;
        int num;
        for (int i = 0; i < clonacion.restantes.size(); ++i){
            num = Math.abs(random.nextInt()) % clonacion.restantes.size();
            uno = clonacion.restantes.get(num);
            dos = clonacion.restantes.get(i);
            clonacion.restantes.set(num, dos);
            clonacion.restantes.set(i, uno);
        }

        clonacion.mazo = new ArrayList<>(clonacion.restantes);
        clonacion.manoRival = new ArrayList<>();

        for(int i=0;i<6;i++) {
            Carta cartaRival= clonacion.mazo.get(0);
            clonacion.mazo.remove(0);
            clonacion.manoRival.add(cartaRival);
        }
        return clonacion;
    }


    /**
     * Constructor que genera un estado de partida sin ninguna carta en
     * tapete, turno y triunfo. El mazo está compuesto por las
     * cartas de la baraja española en un orden aleatorio. Para cada
     * identificador de "jugadores" añade un nuevo jugador a la partida con
     * ese identificador, sin cartas en la mano ni ganadas y con 0 puntos.
     * @throws ExceptionEquipoIncompleto, si el número de jugadores es incorrecto
     */
    public EstadoPartidaIA() {
        this.random = new Random();

        this.mazo = barajear();
        this.restantes = new ArrayList<>(this.mazo);

        this.cartasEnTapete = new ArrayList<>();
        //this.jugadores = new ArrayList<>();
        this.manoRival = new ArrayList<>();
        this.manoIA = new ArrayList<>();
        for(int i=0; i<6;i++) {
            this.manoIA.add(mazo.get(0));
            mazo.remove(0);
            restantes.remove(0);
        }
        for(int i=0; i<6;i++) {
            this.manoRival.add(mazo.get(0));
            mazo.remove(0);
        }
        this.triunfo=new Carta(mazo.get(0));
        mazo.remove(0);

        this.puntosIA=0;
        this.puntosRival=0;

        this.triunfo_entregado = false;

        this.turno = 0;
        this.vueltas = false;
    }


    /**
     * Crea un estado para la IA ha partir del estado de la partida
     * @param p
     */
    public EstadoPartidaIA(EstadoPartida p, ArrayList<Carta> mIA, ArrayList<Carta> mRival, Integer pIA, Integer pRival) {
        this.random = new Random();

        this.mazo = p.getMazo();

        this.restantes = new ArrayList<>(this.mazo);

        this.cartasEnTapete = new ArrayList<>(p.getCartasEnTapete());

        this.manoRival = new ArrayList<>(mRival);
        for(int i=0;i<6;i++) {
            this.restantes.add(this.manoRival.get(i));
        }
        this.manoIA = new ArrayList<>(mIA);
        this.puntosIA=pIA;
        this.puntosRival=pRival;

        this.triunfo_entregado = p.getTriunfoEntregado();

        this.turno = p.getTurno();
        this.triunfo = p.getTriunfo();
        this.vueltas = p.isFinVuelta();

    }


    /**
     * Constructor que genera una copia del estado partida "p".
     */
    public EstadoPartidaIA(EstadoPartidaIA p){
        this.random = new Random();
        this.mazo = new ArrayList<>(p.getMazo());
        this.cartasEnTapete = new ArrayList<>(p.getCartasEnTapete());
        //this.jugadores = p.getJugadores();
        this.manoRival=new ArrayList<>(p.getManoRival());
        this.manoIA=new ArrayList<>(p.getManoIA());
        this.puntosIA=p.getPuntosIA();
        this.puntosRival=p.getPuntosRival();

        this.turno = p.getTurno();
        this.triunfo_entregado = p.getTriunfoEntregado();

        this.triunfo = new Carta(p.getTriunfo());


        this.restantes=new ArrayList<>(p.getRestantes());
        this.cantes=p.getCantes();
        this.vueltas = p.isvueltas();
    }


    public boolean[] getCantes() {
        boolean[] res={false, false, false, false};
        for(int i=0;i<4;i++){
            if(cantes[i]==true){
                res[i]=true;
            }
        }
        return res;
    }

    public ArrayList<Carta> getRestantes() {
        return restantes;
    }

    public int getPuntosIA() {
        return puntosIA;
    }

    public int getPuntosRival() {
        return puntosRival;
    }

    public ArrayList<Carta> getManoIA() {
        return manoIA;
    }

    public ArrayList<Carta> getManoRival() {
        return manoRival;
    }

    public boolean isvueltas() {
        return vueltas;
    }

    /**
     * Devuelve una copia de las cartas del mazo.
     * @return
     */
    public ArrayList<Carta> getMazo(){
        ArrayList<Carta> res = copiarCartas(this.mazo);
        //if (res.size()>0){
        //    res.add(new Carta(this.triunfo));
        //}
        return res;
    }

    public void doMove(Carta move){

        Boolean ganador=false;
        ArrayList<Carta> mano,manoOtro;
        Integer puntos, puntosOtro, who=turno;

        if(turno==1){
            mano=manoRival;
            manoOtro=manoIA;
            puntos = puntosRival;
            puntosOtro=puntosIA;
        }
        else{
            mano=manoIA;
            manoOtro=manoRival;
            puntos = puntosIA;
            puntosOtro=puntosRival;
        }

        mano.remove(move);

        if(cartasEnTapete.size()==0){
            cartasEnTapete.add(move);
            if(turno==0){
                turno=1;
            }
            else{
                turno=0;
            }
        }
        else{
            if(!move.mataCartaOtra(triunfo,cartasEnTapete.get(0))){
                ganador=false;
                //Pierde el jugador de este movimiento
                puntosOtro+=cartasEnTapete.get(0).getPuntuacion()+move.getPuntuacion();
                if(turno==0){
                    turno=1;
                }
                else{
                    turno=0;
                }
            }
            else{
                ganador=true;
                puntos+=cartasEnTapete.get(0).getValor()+move.getValor();
            }
            //Robar carta
            ArrayList<Carta> cartasRestantes= new ArrayList<Carta>(this.mazo);

            cartasRestantes.add(this.triunfo);
            if(cartasRestantes.size()>1){
                mano.add(cartasRestantes.get(0));
                manoOtro.add(cartasRestantes.get(1));
                mazo.remove(0);
                mazo.remove(0);
            }
            cartasEnTapete.remove(0);

            //Comprobar si el que gana la ronda tiene cantes
            if(ganador==true){ puntos=anyadirCante(mano,puntos); }
            else{ puntosOtro=anyadirCante(manoOtro,puntosOtro); }

            //Sumar las diez ultimas

            if((mano.size()==0) && (manoOtro.size()==0)){
                if(ganador=true) { puntos+=10; }else{ puntosOtro+=10; }
            }

            // Si tiene el 7 de triunfo lo cambia
            if(ganador==true){ cambiarSiete(mano); }
            else{ cambiarSiete(manoOtro); }



            //Si se ha acabado la partida de idas sin ganador, se reparten las vueltas
            if(((mano.size()==0) && (manoOtro.size()==0)) && (puntos<101 || puntosOtro<101)){
                if(ganador){ turno=0;} else{ turno=1;}
                vueltas=true;
                for(int i=0;i<4;i++) {
                    cantes[i]=false;
                }
                this.mazo=barajear();
                for(int i=0;i<6;i++) {
                    manoIA.add(mazo.get(i));
                    mazo.remove(i);
                    manoRival.add(mazo.get(i));
                    mazo.remove(i);
                }
                this.restantes=new ArrayList<>(mazo);
                for(int i=0;i<6;i++) {
                    restantes.add(manoRival.get(i));
                }
            }
            if (who == 1) {
                this.puntosIA= puntosOtro;
                this.puntosRival= puntos;

            }
            else{
                this.puntosIA= puntos;
                this.puntosRival= puntosOtro;
            }

        }

    }

    public ArrayList<Carta> getMoves(){
        ArrayList<Carta> mano;

        if(turno==1){ mano=manoRival;}
        else{ mano=manoIA; }

        ArrayList<Carta> lista= new ArrayList<>();
        if(vueltas && (puntosRival>100 || puntosIA>100)){
            return lista;
        }
        else if(mazo.size()>0 || cartasEnTapete.size()==0){
            return new ArrayList<>(mano);
        }
        else{
            ArrayList<Carta> cartasPalo= cartasDelPalo(cartasEnTapete.get(0),mano);
            ArrayList<Carta> cartasTriunfo= cartasDelPalo(triunfo,mano);

            if(cartasPalo.size()>0){
                ArrayList<Carta> cartasPaloMatan=cartasDelPaloMatan(cartasEnTapete.get(0),cartasPalo);
                if(cartasPaloMatan.size()>0){ return cartasPaloMatan;}
                else{return cartasPalo;}
            }
            else if(cartasTriunfo.size()>0){
                return cartasTriunfo;
            }
            else{
                return new ArrayList<>(mano);
            }
        }
    }

    private ArrayList<Carta> cartasDelPalo(Carta palo, ArrayList<Carta> mano ){
        ArrayList<Carta> res= new ArrayList<>();
        for(int i=0;i<mano.size();i++){
            if(mano.get(i).esMismoPalo(palo)){
                res.add(mano.get(i));
            }
        }
        return res;
    }

    private ArrayList<Carta> cartasDelPaloMatan(Carta otra, ArrayList<Carta> mano ){
        ArrayList<Carta> res= new ArrayList<>();
        for(int i=0;i<mano.size();i++){
            if(mano.get(i).mataCartaOtra(triunfo,otra)){
                res.add(mano.get(i));
            }
        }
        return res;
    }

    private void cambiarSiete(ArrayList<Carta> mano){
        for(int i=0; i<mano.size();i++){
            Carta c = mano.get(i);
            if(c.esMismoPalo(triunfo) && c.getValor()==7 && triunfo.getPuntuacion()>0){
                Carta aux=new Carta(c);
                mano.remove(c);
                mano.add(i,triunfo);
                triunfo=aux;

            }
        }
    }

    private Integer anyadirCante(ArrayList<Carta> cartasEnMano, Integer puntos){


        boolean reyes[] = {false, false, false, false};
        boolean sotas[] = {false, false, false, false};

        for(Carta iterador : cartasEnMano){
            if(iterador.getValor() == 10){
                marcaSotaORey(sotas, iterador);
            }
            if(iterador.getValor() == 12){
                marcaSotaORey(reyes, iterador);
            }
        }
        int sumaTotal=0;
        for(int i = 0; i < 4; i++){
            if(reyes[i] && sotas[i] && !cantes[i]){
                cantes[i] = true;
                if(esPaloTriunfo(i)) {
                    sumaTotal += 40;

                }
                else{
                    sumaTotal += 20;
                }
            }
        }
        puntos += sumaTotal;
        return puntos;
    }

    private void marcaSotaORey(boolean[] vectorApariciones, Carta iterador) {
        switch (iterador.getPalo()){
            case "B":
                vectorApariciones[0] = true;
                break;
            case "C":
                vectorApariciones[1] = true;
                break;
            case "E":
                vectorApariciones[2] = true;
                break;
            case "O":
                vectorApariciones[3] = true;
                break;
        }
    }

    private boolean esPaloTriunfo(int i){
        String palo = "B";
        switch (i){
            case 0:
                palo="B";
                break;
            case 1:
                palo="C";
                break;
            case 2:
                palo="E";
                break;
            case 3:
                palo="O";
                break;
        }

        if (triunfo.getPalo().equals(palo)){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Devuelve true si y solo si el jugador que le toca jugar ha ganado
     * @return
     */
    public boolean getResult(int jugador){
        if(jugador==0){
            if(puntosIA>100 && vueltas){
                if(puntosRival>100 && turno==1){
                    return false;
                }
                else {
                    return true;
                }
            }
            else{
                return false;
            }
        }
        else{
            if(puntosRival>100 && vueltas){
                if(puntosIA>100 && turno==0){
                    return false;
                }
                else{
                    return true;
                }
            }
            else{
                return false;
            }
        }
    }



    /**
     * Devuelve la posición del jugador que le toca lanzar una carta
     * @return
     */
    public int getTurno(){
        return turno;
    }



    /**
     * Devuelve las cartas que están encima de la mesa.
     * @return ArrayList<Carta>
     */
    public ArrayList<Carta> getCartasEnTapete(){
        return copiarCartas(this.cartasEnTapete);
    }



    /**
     * Devuelve una copia del triunfo de la partida.
     * @return
     */
    public Carta getTriunfo(){
        Carta copia = new Carta(triunfo);
        return copia;
    }


    /**
     * Cambia el triunfo por nuevoTriunfo, de forma segura
     * @param nuevoTriunfo
     */
    public void setTriunfo(Carta nuevoTriunfo){
        Carta copia = new Carta(nuevoTriunfo);
        this.triunfo = copia;
    }


    /**
     * Devuelve las cartas de la baraja española en un orden aleatorio. Para ello hace 40 permutaciones sobre
     * la baraja ordenada.
     */
    private ArrayList<Carta> barajear(){
        ArrayList<Carta> cartas = crearBaraja();
        Carta uno, dos;
        int num;
        for (int i = 0; i < 40; ++i){
            num = Math.abs(random.nextInt()) % 40;
            uno = cartas.get(num);
            dos = cartas.get(i);
            cartas.set(num, dos);
            cartas.set(i, uno);
        }
        return cartas;
    }

    /**
     * Añade una carta al jugador "jugador". Si "jugador" no está en la partida lanza una excepción.
     * Si el jugador ya posee la carta "carta" en la mano o, posee 6 o más cartas lanza las correspondientes
     * excepciones.
     * @throws ExceptionNumeroMaximoCartas
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionCartaYaExiste
     */
    public void anyadirCartaJugador(int i, Carta carta){
        if(i==0){
            manoIA.add(carta);
        }
        else{
            manoRival.add(carta);
        }
    }


    /**
     * Quita la carta c de las cartas en mano del jugador. Si el jugador no
     * pertenece a la partida o no tiene la carta en la mano lanza una
     * excepción.
     * @param c
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionJugadorSinCarta
     */
    public void quitarCartaJugador(int i, Carta c){
        if(i==0){
            manoIA.remove(c);
        }
        else{
            manoRival.remove(c);
        }
    }






    //TODO: funcion para pruebas por terminal, eliminar al final
    /**
     * Asignar un mazo concreto a la partida
     * @param baraja tiene que tener 40 cartas de la baraja española
     */
    public void setMazo(ArrayList<Carta> baraja){
        if (baraja.size() == 40){
            this.mazo = copiarCartas(baraja);
            this.triunfo_entregado = false;
        }
    }



    //TODO: funcion para pruebas por terminal, eliminar al final

    /**
     * Modifica mazo para que tenga tamaño 0
     */
    public void eliminaMazo(){
        mazo = new ArrayList<>();
    }



    /***************************** FUNCIONES AUXILIARES ***********************/

    /**
     * Devuelve la lista de cartas de la baraja española
     * @return ArrayList<Carta>
     */
    private ArrayList<Carta> crearBaraja(){
        ArrayList<Carta> baraja = new ArrayList<>();
        try {
            int num;
            for (int i = 0; i < 40; ++i){
                num = i%10+1;
                if (num == 8 || num == 9) {
                    num += 3;
                }
                baraja.add(new Carta(num, i/10+1));
            }
        } catch (Exception e){
            System.err.println("Excepción generando baraja: " + e.getMessage());
        }
        return baraja;
    }


    /**
     * Devuelve la posición ocupada en la lista por el jugador.
     * @param jugador
     * @return >= 0
     *
    private int posJugador(Jugador jugador) throws ExceptionJugadorIncorrecto {
    int i = 0;
    for (Jugador j : jugadores) {
    if (jugador.equals(j)){
    return i;
    }
    ++i;
    }
    throw new ExceptionJugadorIncorrecto();

    }

     */

    /**
     * Asigna el turno al jugador que ocupa la siguiente posición en
     * la lista de jugadores respecto al "jugador" indica
     * @param jugador
     */
    private void pasarTurno(String jugador){
        //Asigna el turno al siguiente jugador
        if (turno==0){
            turno++;
        }
        else{
            turno = 0;
        }
    }

    /**
     * Devuelve una copia de cartas
     * @param cartas
     * @return ArrayList<Carta>
     */
    private ArrayList<Carta> copiarCartas(ArrayList<Carta> cartas) {
        ArrayList<Carta> res = new ArrayList<>();
        for (Carta c: cartas) {
            res.add(new Carta(c));
        }
        return res;
    }

    /**
     * Asigna las carta del tapete al jugador j y le suma el total de
     * la puntuación de todas las cartas en tapete
     * @return
     */
    private void asignaCartasJugador(Jugador j) throws
            ExceptionCartaYaExiste {
        int res = 0;
        for (Carta c: cartasEnTapete){
            res += c.getPuntuacion();
            j.anyadirCartaGanadas(c);
        }

        //Elimina todas las del tapete
        cartasEnTapete = new ArrayList<Carta>();
        j.sumarPuntos(res);
    }

    /**
     * Devuelve true si y solo el jugador tiene una carta en mano con el
     * mismo palo que la "carta".
     * @param j
     * @return
     */
    private boolean tienePaloEnMano(Jugador j, Carta carta){
        boolean tienePalo = false;
        for (Carta c: j.getCartasEnMano()) {
            tienePalo = tienePalo || c.esMismoPalo(carta);
        }
        return tienePalo;
    }

    /**
     * Devuelve true si y solo el jugador tiene una carta del mismo palo con
     * mejor puntuación
     * @param j
     * @param carta
     * @return
     */
    private boolean tieneOtraMejorDelPalo(Jugador j, Carta carta){
        boolean tieneMejor = false;
        for (Carta c: j.getCartasEnMano()) {
            if (!c.equals(carta) && carta.esMismoPalo(c)){
                tieneMejor = tieneMejor || carta.masPuntuacion(c);
            }
        }
        return tieneMejor;
    }

    /**
     * Quita la carta de la mano del jugador y la pone en la mesa. Además
     * pasa el turno al siguiente jugador
     * @param carta
     * @param j
     * @throws ExceptionJugadorSinCarta
     */
    private void ponerCartaMesa(Carta carta, Jugador j) throws
            ExceptionJugadorSinCarta {
        j.quitarCartaEnMano(carta);
        cartasEnTapete.add(carta);
        pasarTurno(j.getId());
    }

    /**
     * Devuelve true si y solo la carta del triunfo ha sido entregada a algún
     * jugador.
     * @return
     */
    private boolean getTriunfoEntregado(){
        return this.triunfo_entregado;
    }

}




