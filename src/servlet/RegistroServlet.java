package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.LigaVO;
import basedatos.modelo.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

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
        String facebook = request.getParameter("facebook");
        String error ="";
        UsuarioVO usuarioVo = null;
        InterfazDatos facade = null;
        if (facebook != null) {
            try {
                // TODO: Refactorizar para evitar código duplicado
                usuarioVo = new UsuarioVO(login, null, email, nombre, apellidos, false);
            } catch (ExceptionCampoInvalido exceptionCampoInvalido) {
                exceptionCampoInvalido.printStackTrace();
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
            sesion.setAttribute("userId", usuarioVo);
            sesion.setMaxInactiveInterval(24*60*60);


            response.sendRedirect("jsp/home.jsp");


        } else {
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


                    try {
                        usuarioVo = new UsuarioVO(login, password, email, nombre, apellidos, false);
                    }
                    catch(Exception e){
                        System.out.println(e.toString());
                        return;
                    }

                    try {
                        facade = InterfazDatos.instancia();
                    }
                    catch(Exception e){
                        System.out.println(e.toString());
                        return;
                    }
                    ArrayList<LigaVO> ligas = null;
                    try {
                        facade.crearUsuario(usuarioVo);
                        ligas = facade.obtenerLigas();

                    } catch (Exception e){
                        error= "existentUser";
                        request.setAttribute("error",error);
                        RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/login.jsp");
                        dispatcher.forward(request,response);
                    }
                    HttpSession sesion= request.getSession();
                    sesion.setAttribute("userId", usuarioVo);
                    sesion.setAttribute("ligas", ligas);
                    sesion.setMaxInactiveInterval(24*60*60);


                    response.sendRedirect("jsp/matchmaking.jsp");
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doPost(request,response);
    }
}
