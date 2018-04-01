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
        try{
            //TODO: todos los == (equals) están mal
            //Evitar campos NULL
            if(login=="" || login ==null){
                error= "emptyUser";
                request.setAttribute("error",error);
                // response.sendRedirect("index.jsp");
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request,response);
            }
            else if(apellidos=="" || apellidos ==null){
                error= "emptyLastName";
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request,response);
            }
            else if(nombre=="" || nombre ==null){
                error= "emptyName";
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request,response);
            }
            else if(email=="" || email ==null){
                error= "emptyEmail";
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request,response);
            }
            else if(password=="" || password ==null){
                error= "emptyPass";
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                dispatcher.forward(request,response);
            }
            else if(passwordRep=="" || passwordRep ==null){
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
                    //TODO: Falta añadir la contraseña
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

                facade.crearUsuario(usuarioVo);

                HttpSession sesion= request.getSession();
                sesion.setAttribute("userId", login);
                sesion.setMaxInactiveInterval(24*60*60);


                response.sendRedirect("home.jsp");
            }

        } catch(Exception e){
            System.out.println(e);

            if((e.toString()).contains("PRIMARY")){
                //Registro incorrecto, email esta en uso
                error = "El e-mail ya esta en uso.";
            }
            else if((e.toString()).contains("login")){
                error= "El usuario ya esta en uso.";
            }
            else if((e.toString()).contains("date")){
                error= "Introduce una fecha de nacimiento.";
            }
            else{
                error= "No se ha podido completar el registro. Intentalo de nuevo.";
            }
            request.setAttribute("errors",error);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doPost(request,response);
    }

}
