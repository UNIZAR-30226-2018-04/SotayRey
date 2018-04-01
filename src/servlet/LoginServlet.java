package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marius Sorin Crisan
 *
 * @version 1.0
 * @since 	1.0
 *
 * Servlet implementation class LoginUsuarioServlet
 */
@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nick = request.getParameter("loginUser");
        String pass = request.getParameter("loginPass");
        String error = "";
        try {
            if (nick.equals("")) {
                error = "Introduce un nombre de usuario.";
                request.setAttribute("errors", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else if (pass.equals("")) {
                error = "Introduce la contraseña.";
                request.setAttribute("errors", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else {
                System.out.println("probar a registrar");
                InterfazDatos facade = null;
                boolean existe = facade.autentificarUsuario(nick, pass);
                if (existe){

                } else { // Usuario no existe

                }
            }
        } catch (ExceptionCampoInexistente e){
            //TODO: usuario no posee contraseña
        }
        catch (Exception e){
            e.printStackTrace();
            //TODO: hacer lo que corresponda
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
