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
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.modelo.StatsUsuarioVO;
import basedatos.modelo.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.rmi.StubNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nick = request.getParameter("loginUser");
        String pass = request.getParameter("loginPass");
        String error = "";
        try {
            if (nick== null || nick.equals("")) {
                error = "emptyUser";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else if (pass == null || pass.equals("")) {
                error = "emptyPass";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else {

                InterfazDatos facade = null;
                try{
                    facade = InterfazDatos.instancia();
                }catch (Exception e){
                    e.printStackTrace();
                    throw new ServletException();
                }

                boolean existUser = false;
                try{
                    existUser = facade.autentificarUsuario(nick, pass);
                    System.out.println("Usuario autentificado");
                }catch(Exception e){
                    e.printStackTrace();
                    throw new ServletException();
                }

                if (existUser){
                    UsuarioVO user = null;
                    try{
                        user = facade.obtenerDatosUsuario(nick);
                    } catch (Exception e){
                        e.printStackTrace();
                        throw new ServletException();
                    }
                    HttpSession sesion= request.getSession();
                    boolean
                    if (user.getAdmin()){
                        sesion.setAttribute("isAdmin", true);
                        System.out.println("Administrador auntentificado");
                    }
                    sesion.setAttribute("userId", user);
                    sesion.setMaxInactiveInterval(24*60*60);
                    response.sendRedirect("jsp/home.jsp");
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
        catch (Exception e){
            System.err.println("ERROR: autentificando usuario");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
