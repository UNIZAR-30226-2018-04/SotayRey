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
 * Servlet implementation class RegistrarUsuario
 */
@WebServlet("/RegistrarUsuario")
public class RegistrarUsuarioServlet extends HttpServlet {


    /**
     *
     */
    private static final long serialVersionUID = -2484162939395285925L;

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
                //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                //dispatcher.forward(request,response);
            }
            else if(apellidos=="" || apellidos ==null){
                error= "Introduce tus apellidos.";
                request.setAttribute("errors",error);
                //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                //dispatcher.forward(request,response);
            }
            else if(nombre=="" || nombre ==null){
                error= "Introduce tu nombre.";
                request.setAttribute("errors",error);
                //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                //dispatcher.forward(request,response);
            }
            else if(email=="" || email ==null){
                error= "Introduce el email.";
                request.setAttribute("errors",error);
                //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                //dispatcher.forward(request,response);
            }
            else if(password=="" || password ==null){
                error= "Introduce la contraseña.";
                request.setAttribute("errors",error);
                //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                //dispatcher.forward(request,response);
            }
            else if(passwordRep=="" || passwordRep ==null){
                error= "Repite la contraseña.";
                request.setAttribute("errors",error);
                //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                //dispatcher.forward(request,response);
            }
            else if(!password.equals(passwordRep)){
                error= "La contraseña no coincide.";
                request.setAttribute("errors",error);
                //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                //dispatcher.forward(request,response);
            }
            else{//NO hay dato null
                /*SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                Date fechaNacimiento=new java.sql.Date(sdf.parse(fech).getTime());

                SimpleDateFormat s = new SimpleDateFormat("yyyy");
                int year=Integer.parseInt((s.format(fechaNacimiento)));
                System.out.print(year);

                if(fechaNacimiento==null || year > 2012 || year < 1900){
                    error= "Introduce una fecha valida.";
                    request.setAttribute("errors",error);
                    RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
                    dispatcher.forward(request,response);
                }
                else{

                    UsuarioVO usuarioVo =  new UsuarioVO(email,login,password,null);
                    DatosPersonalesVO datosPersonalesVO = new DatosPersonalesVO(email,fechaNacimiento,nombre,apellidos);
                    System.out.println("LA FECHA ES" +fechaNacimiento);
                    WebLibrosFacade.crearCliente(usuarioVo);
                    WebLibrosFacade.crearDatosPersonales(datosPersonalesVO);
                    //HttpSession s= request.getSession();
                    //s.setAttribute("email", email);
                    Cookie emailCookie = new Cookie("email",email);
                    Cookie passwordCookie = new Cookie("password",password);

                    //Expira en una hora
                    emailCookie.setMaxAge(60*60);
                    passwordCookie.setMaxAge(60*60);

                    //Añade las cookies a la respuesta
                    response.addCookie( emailCookie );
                    response.addCookie( passwordCookie);

                    response.sendRedirect("home.jsp");
                }*/
            }

            //response.sendRedirect("home.html");
        } catch (Exception e){
            System.out.println(e);
            if((e.toString()).contains("PRIMARY")){
                //Registro incorrecto, email esta en uso
                error= "El e-mail ya esta en uso.";
            }
            else if((e.toString()).contains("login")){
                error= "El usuario ya esta en uso.";
            }
            else if((e.toString()).contains("date")){
                error= "Introduce una fecha de nacimiento.";
            }
            else{//No ha introducido la fecha
                error= "No se ha podido completar el registro. Intentalo de nuevo.";
            }
            request.setAttribute("errors",error);
            //RequestDispatcher dispatcher=request.getRequestDispatcher("registro.jsp");
            //dispatcher.forward(request,response);
        }
        //response.sendRedirect("home.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doPost(request,response);
    }

}
