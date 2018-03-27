import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;



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
        String login = request.getParameter("user");
        String apellidos = request.getParameter("apellidos");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("mail");
        String password = request.getParameter("contra");
        String passwordRep = request.getParameter("contraRep");
        String fech = request.getParameter("fecha");
        String error ="";
        try{
            //Evitar campos NULL
            if(login=="" || login ==null){
                error= "Introduce un nombre de usuario.";
                request.setAttribute("errors",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request,response);
            }
            else if(apellidos=="" || apellidos ==null){
                error= "Introduce tus apellidos.";
                request.setAttribute("errors",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request,response);
            }
            else if(nombre=="" || nombre ==null){
                error= "Introduce tu nombre.";
                request.setAttribute("errors",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request,response);
            }
            else if(email=="" || email ==null){
                error= "Introduce el email.";
                request.setAttribute("errors",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request,response);
            }
            else if(password=="" || password ==null){
                error= "Introduce la contraseña.";
                request.setAttribute("errors",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request,response);
            }
            else if(passwordRep=="" || passwordRep ==null){
                error= "Repite la contraseña.";
                request.setAttribute("errors",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request,response);
            }
            else if(!password.equals(passwordRep)){
                error= "La contraseña no coincide.";
                request.setAttribute("errors",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request,response);
            }
            else{//Si todos los datos no son null
                //UsuarioVO usuarioVo = null;
                //InterfazDatos facade = null;

                try {
                    //TODO: Falta añadir la contraseña
                    //usuarioVo = new UsuarioVO(login, email, nombre, apellidos, false);
                }
                catch(Exception e){
                    //TODO: Tratar error
                }

                try {
                    //facade = new InterfazDatos();
                }
                catch(Exception e){
                    //TODO: Tratar error
                }

                //facade.crearUsuario(usuarioVo);

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
