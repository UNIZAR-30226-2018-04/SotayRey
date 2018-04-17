package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.ArticuloVO;
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

@WebServlet(name = "EliminarObjetoTiendaServlet")
public class EliminarObjetoTiendaServlet extends HttpServlet {
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
                Integer pos = Integer.parseInt(request.getParameter("id_objeto"));
                ArrayList<ArticuloUsuarioVO> articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
                if (pos >= 0 && articulos != null && pos < articulos.size()){
                    ArticuloUsuarioVO art = articulos.get(pos);
                    facade.comprarArticuloUsuario(art);
                    System.out.println("Eliminado de la tienda el articulos: " + art.getNombre());
                    response.sendRedirect("/MostrarObjetosTienda.do");
                } else {
                    error = "artNotFound";
                    request.setAttribute("error", error);
                    RequestDispatcher dispatcher = request.getRequestDispatcher
                            ("/MostrarObjetosTienda.do");
                    dispatcher.forward(request, response);
                    //TODO: ver si se propaga el error al jsp
                }
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
        } catch (ExceptionCampoInvalido exceptionCampoInvalido) {
            error = "artNotValid";
            request.setAttribute("error", error);
            RequestDispatcher dispatcher = request.getRequestDispatcher
                    ("/MostrarObjetosTienda.do");
            dispatcher.forward(request, response);
        } catch (Exception e){
            System.err.println("ERROR: eliminando usuario");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
