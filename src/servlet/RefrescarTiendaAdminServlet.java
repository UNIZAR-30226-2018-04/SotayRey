package servlet;

import basedatos.InterfazDatos;
import basedatos.dao.LigaDAO;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.modelo.LigaVO;
import basedatos.modelo.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "RefrescarTiendaAdminServlet")
public class RefrescarTiendaAdminServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UsuarioVO user = (UsuarioVO) session.getAttribute("userId");
        String error;
        InterfazDatos facade = null;
        try {
            facade = InterfazDatos.instancia();
        } catch (Exception e){
            System.err.println("ERROR: creando interfaz");
        }
        try {
            if (facade.esAdministrador(user.getUsername())) {
                String accion = request.getParameter("optionAdmin");
                if (accion != null && accion.equals("modify")) {
                    session.setAttribute("accionAdmin", "modificar");
                } else {
                    session.setAttribute("accionAdmin", "nada");
                }
                response.sendRedirect("/jsp/tienda.jsp");
            }
        } catch (ExceptionCampoInexistente exceptionCampoInexistente){
            error = "adminNotFound";
            request.setAttribute("error", error);
            RequestDispatcher dispatcher = request.getRequestDispatcher
                    ("jsp/login.jsp");
            dispatcher.forward(request, response);
        } catch (NullPointerException e){
            System.err.println("ERROR: fachada creado, error al invocar " +
                    "métodos");
            e.printStackTrace();
        } catch (Exception e){
            System.err.println("ERROR: comprobando usuario");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
