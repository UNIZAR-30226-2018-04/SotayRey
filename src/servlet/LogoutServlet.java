/**
 * @author Marius Sorin Crisan
 *
 * @version 1.0
 * @since 	1.0
 *
 * Servlet implementation class LogoutUsuarioServlet
 */

package servlet;

import basedatos.modelo.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        String error;
        if (sesion != null){
            try {
                UsuarioVO usuarioVO = (UsuarioVO) sesion.getAttribute("userId");
                sesion.invalidate();
                sesion = null;
                RequestDispatcher dispatcher=request.getRequestDispatcher
                        ("/index.jsp");
                dispatcher.forward(request,response);
            } catch (Exception e){
                error = "userNotExist";
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher
                        ("/index.jsp");
                dispatcher.forward(request,response);
            }
        } else {
            error = "sessionNotExist";
            request.setAttribute("error", error);
            RequestDispatcher dispatcher = request.getRequestDispatcher
                    ("index.jsp");
            dispatcher.forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
