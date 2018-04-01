package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
                error = "emptyUser";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else if (pass.equals("")) {
                error = "emptyPass";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
            } else {
                System.out.println("probar a registrar");
                //InterfazDatos facade = InterfazDatos.instancia();
                boolean existUser = InterfazDatos.instancia()
                        .autentificarUsuario(nick, pass);
                if (existUser){
                    HttpSession sesion= request.getSession();
                    sesion.setAttribute("userId", nick);
                    sesion.setMaxInactiveInterval(24*60*60);

                    response.sendRedirect("home.jsp");
                } else { // Usuario no existe
                    error= "userNotFound";
                    request.setAttribute("error",error);
                    RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                    dispatcher.forward(request,response);
                }
            }
        } catch (ExceptionCampoInexistente e){
            System.err.println("ERROR: usuario mal registrado.");
            e.printStackTrace();
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
