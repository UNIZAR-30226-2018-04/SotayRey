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
import java.io.IOException;

@WebServlet(name = "BorrarUsuarioServlet")
public class BorrarUsuarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String error = null;
        HttpSession sesion = request.getSession();
        UsuarioVO user = (UsuarioVO) sesion.getAttribute("userId");

        String passwd = request.getParameter("passwd");

        if(user == null || passwd == null){
            response.sendRedirect("jsp/perfil.jsp");
            return;
        }

        InterfazDatos facade;
        try{
            facade = InterfazDatos.instancia();
        }catch (Exception e){
            e.printStackTrace();
            error = new String("again");
            request.setAttribute("error", error);
            response.sendRedirect("jsp/perfil.jsp");
            return;
        }

        boolean existUser;
        try{
            existUser = facade.autentificarUsuario(user.getUsername(), passwd);
        }catch(Exception e){
            e.printStackTrace();
            error = new String("userNotFound");
            request.setAttribute("error", error);
            response.sendRedirect("jsp/perfil.jsp");
            return;
        }

        if(existUser){
            try{
                facade.eliminarUsuario(user.getUsername());
            } catch (Exception e){
                e.printStackTrace();
                response.sendRedirect("jsp/perfil.jsp");
                return;
            }
            try {
                sesion.invalidate();
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
        } else{
            error = new String("userNotFound");
            request.setAttribute("error", error);
            response.sendRedirect("jsp/perfil.jsp");
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
