package servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import basedatos.modelo.UsuarioVO;
import basedatos.InterfazDatos;

/**
 * @author Víctor Soria
 *
 * @version 1.0
 * @since 	1.0
 *
 * Servlet implementation class RegistroServlet
 */
public class RegistroServlet extends HttpServlet {

    private static final long serialVersionUID = 102831973239L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String login = request.getParameter("nick");
        String apellidos = request.getParameter("lastName");
        String nombre = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("passwd");
        String passwordRep = request.getParameter("passwdRep");
        String error ="";
        //Evitar campos NULL
        if(login == null || login.equals("")){
            error= "emptyUser";
            request.setAttribute("error",error);
            // response.sendRedirect("index.jsp");
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
            dispatcher.forward(request,response);
        }
        else if(apellidos == null || apellidos.equals("")){
            error= "emptyLastName";
            request.setAttribute("error",error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
            dispatcher.forward(request,response);
        }
        else if(nombre == null || nombre.equals("")){
            error= "emptyName";
            request.setAttribute("error",error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
            dispatcher.forward(request,response);
        }
        else if(email == null || email.equals("")){
            error= "emptyEmail";
            request.setAttribute("error",error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
            dispatcher.forward(request,response);
        }
        else if(password == null || password.equals("")){
            error= "emptyPass";
            request.setAttribute("error",error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
            dispatcher.forward(request,response);
        }
        else if(passwordRep == null || passwordRep.equals("")){
            error= "emptyRePass";
            request.setAttribute("error",error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
            dispatcher.forward(request,response);
        }
        else if(!password.equals(passwordRep)){
            error= "wrongRePass";
            request.setAttribute("error",error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
            dispatcher.forward(request,response);
        }
        else{//Si todos los datos no son null
            UsuarioVO usuarioVo = null;
            InterfazDatos facade = null;

            try {
                usuarioVo = new UsuarioVO(login, password, email, nombre, apellidos, false);
            }
            catch(Exception e){
                System.out.println(e.toString());
                //TODO: Tratar error
            }

            try {
                facade = InterfazDatos.instancia();
            }
            catch(Exception e){
                System.out.println(e.toString());
                //TODO: Tratar error
            }

            try {
                facade.crearUsuario(usuarioVo);
            } catch (Exception e){
                error= "existentUser";
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request,response);
            }

            HttpSession sesion= request.getSession();
            sesion.setAttribute("userId", login);
            sesion.setMaxInactiveInterval(24*60*60);


            response.sendRedirect("jsp/home.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doPost(request,response);
    }
}
