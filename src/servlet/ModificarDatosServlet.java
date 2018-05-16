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

@WebServlet(name = "ModificarDatosServlet")
public class ModificarDatosServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        UsuarioVO user = (UsuarioVO) sesion.getAttribute("userId");

        String email = request.getParameter("modEmail");
        String nombre = request.getParameter("modNombre");
        String apellidos = request.getParameter("modApellidos");
        String password = request.getParameter("modPass");
        String rePassword = request.getParameter("modRePass");
        String oldPassword = request.getParameter("modOldPass");

        if(oldPassword == null){
            System.out.println("ERROR1");
            String error = new String("emptyPass");
            request.setAttribute("error", error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/perfil.jsp");
            dispatcher.forward(request,response);
            return;
        }

        if(email.equals("")){
            email = user.getCorreo();
        }
        if(nombre.equals("")){
            nombre = user.getNombre();
        }
        if(apellidos.equals("")){
            apellidos = user.getApellidos();
        }
        if(password.equals("")){
            password = user.getPlaintextPassword();
        } else{
            if(!password.equals(rePassword)){
                String error = new String("wrongRePass");
                request.setAttribute("error", error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/perfil.jsp");
                dispatcher.forward(request,response);
                return;
            }
        }

        InterfazDatos facade;
        try{
            facade = InterfazDatos.instancia();
        }catch (Exception e){
            e.printStackTrace();
            String error = new String("again");
            request.setAttribute("error", error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/perfil.jsp");
            dispatcher.forward(request,response);
            return;
        }

        boolean existUser;
        try{
            existUser = facade.autentificarUsuario(user.getUsername(), oldPassword);
        }catch(Exception e){
            e.printStackTrace();
            String error = new String("userNotFound");
            request.setAttribute("error", error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/perfil.jsp");
            dispatcher.forward(request,response);
            return;
        }

        if(existUser){
            UsuarioVO nuevoUsuario = null;
            try {
                nuevoUsuario = new UsuarioVO(user.getUsername(), password, email, nombre, apellidos, user.getAdmin());
            } catch (Exception e){
                e.printStackTrace();
                String error = new String("again");
                request.setAttribute("error", error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/perfil.jsp");
                dispatcher.forward(request,response);
                return;
            }

            try {
                facade.modificarDatosUsuario(nuevoUsuario);
                System.out.println("Modificado");
            } catch (Exception e){
                e.printStackTrace();
                String error = new String("again");
                request.setAttribute("error", error);
                RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/perfil.jsp");
                dispatcher.forward(request,response);
                return;
            }

            System.out.println("Cambio de datos");

            sesion.setAttribute("userId", nuevoUsuario);
            sesion.setMaxInactiveInterval(24*60*60);

            response.sendRedirect("jsp/perfil.jsp");
            return;

        } else{
            String error = new String("userNotFound");
            request.setAttribute("error", error);
            RequestDispatcher dispatcher=request.getRequestDispatcher("jsp/perfil.jsp");
            dispatcher.forward(request,response);
            return;
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
