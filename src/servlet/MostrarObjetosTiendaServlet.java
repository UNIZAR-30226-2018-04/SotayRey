/**
 * @author Marius Sorin Crisan
 *
 * @version 1.0
 * @since 	1.0
 *
 * Servlet implementation class MostrarObjetosTiendaServlet
 */

package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.ArticuloVO;
import basedatos.modelo.StatsUsuarioVO;
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

@WebServlet(name = "MostrarObjetosTiendaServlet")
public class MostrarObjetosTiendaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UsuarioVO user = (UsuarioVO) session.getAttribute("userId");
        String error = "";
        InterfazDatos facade = null;
        try {
            facade = InterfazDatos.instancia();
        } catch (Exception e){
            System.err.println("ERROR: creando interfaz");
        }
        try {
            ArrayList<ArticuloUsuarioVO> articulos = facade
                    .obtenerArticulosTienda(user.getUsername());
            session.setAttribute("articulos", articulos);
            System.out.println("Articulos usuario obtenidos");

            StatsUsuarioVO stats = null;
            try {
                stats = (StatsUsuarioVO) facade
                        .obtenerStatsUsuario(user.getUsername());
                System.out.println("Stats usuario añadidos");
            } catch (Exception e){
                System.err.println("ERROR: obteniendo stats usuario");
                e.printStackTrace();
            }

            session.setAttribute("userMainStats", stats);
            response.sendRedirect("jsp/tienda.jsp");

        } catch (ExceptionCampoInexistente exceptionCampoInexistente){
            error = "userNotFound";
            request.setAttribute("error", error);
            RequestDispatcher dispatcher = request.getRequestDispatcher
                    ("jsp/login.jsp");
            dispatcher.forward(request, response);
        } catch (NullPointerException e){
            System.err.println("ERROR: fachada creado, error al invocar " +
                    "métodos");
            e.printStackTrace();
        } catch (Exception e){
            System.err.println("ERROR: accediendo a la tienda");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
