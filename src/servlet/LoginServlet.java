/**
 * @author Marius Sorin Crisan
 *
 * @version 1.0
 * @since 	1.0
 *
 * Servlet implementation class LoginUsuarioServlet
 */

package servlet;

import basedatos.InterfazDatos;
import basedatos.modelo.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nick = request.getParameter("loginUser");
        String pass = request.getParameter("loginPass");
        String token = request.getParameter("token");

        String error;
        try {
            if ((nick== null || nick.equals("")) && token == null) {
                error = "emptyUser";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else if (pass == null || pass.equals("") && token==null) {
                error = "emptyPass";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else {

                InterfazDatos facade;
                try{
                    facade = InterfazDatos.instancia();
                }catch (Exception e){
                    System.err.println("ERROR: Creando instancia");
                    e.printStackTrace();
                    response.sendRedirect("jsp/login.jsp");
                    return;
                }

                boolean existUser;
                UsuarioVO user = null;

                try{


                    if (token == null){
                        existUser = facade.autentificarUsuario(nick, pass);
                        if (existUser){
                            user = facade.obtenerDatosUsuario(nick);
                        }
                    } else {
                        user = facade.autentificarUsuarioFacebook(token);
                        // Existe usuario si no ha devuelto null la consulta
                        existUser = (user != null);
                    }
                }catch(Exception e){
                    error= "userNotFound";
                    request.setAttribute("error",error);
                    RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                    dispatcher.forward(request,response);
                    System.err.println("ERROR: Login al autentificar usuario");
                    e.printStackTrace();
                    response.sendRedirect("jsp/login.jsp");
                    return;
                }

                if (existUser){
                    System.out.println("Usuario autentificado");
                    HttpSession sesion= request.getSession();
                    ArrayList<TorneoVO> torneos = null;
                    ArrayList<TorneoPeriodicoVO> tor_period = null;
                    ArrayList<LigaVO> ligas = null;
                    try{
                        torneos = facade.obtenerTorneosProgramados();
                        tor_period = facade.obtenerTorneosPeriodicos();
                        ligas = facade.obtenerLigas();
                    }catch (Exception e){
                        System.err.println("ERROR: error en la interfazDatos");
                        e.printStackTrace();
                        response.sendRedirect("jsp/matchmaking.jsp");
                        return;
                    }

                    sesion.setAttribute("ligas", ligas);
                    sesion.setAttribute("torneos", torneos);
                    sesion.setAttribute("torneos_period", tor_period);
                    sesion.setAttribute("userId", user);
                    sesion.setMaxInactiveInterval(24*60*60);

                    String urlAbierta = null;
                    try {
                        urlAbierta = facade.obtenerUrlSesion(nick);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (urlAbierta != null) {
                        // Tiene una partida abierta
                        System.out.println("Jugador con partida abierta. Redirigiendo...");
                        response.sendRedirect("juego.html?"+urlAbierta);
                    } else {
                        response.sendRedirect("jsp/matchmaking.jsp");
                    }
                } else { // Usuario no existe
                    error= "userNotFound";
                    request.setAttribute("error",error);
                    RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                    dispatcher.forward(request,response);
                }
            }
        } catch (NullPointerException e){
            System.err.println("ERROR: NUll Pointer a Facade");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
