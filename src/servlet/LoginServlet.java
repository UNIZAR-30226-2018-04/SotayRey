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
import basedatos.modelo.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String nick = request.getParameter("loginUser");
        String pass = request.getParameter("loginPass");
        String error;
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

                InterfazDatos facade;
                try{
                    facade = InterfazDatos.instancia();
                }catch (Exception e){
                    e.printStackTrace();
                    throw new ServletException();
                }

                boolean existUser;
                try{
                    existUser = facade.autentificarUsuario(nick, pass);
                    System.out.println("Usuario autentificado");
                }catch(Exception e){
                    e.printStackTrace();
                    throw new ServletException();
                }

                if (existUser){
                    UsuarioVO user;
                    try{
                        user = facade.obtenerDatosUsuario(nick);
                    } catch (Exception e){
                        e.printStackTrace();
                        throw new ServletException();
                    }
                    HttpSession sesion= request.getSession();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }
}
