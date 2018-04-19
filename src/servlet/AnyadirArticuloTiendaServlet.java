package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.ArticuloVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "AnyadirArticuloTiendaServlet")
public class AnyadirArticuloTiendaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String error;
        if (session != null){
            String nombre = request.getParameter("modNombre");
            Integer precio = Integer.parseInt(request.getParameter("modPrecio"));
            String desc = request.getParameter("modDesc");
            String tipo = request.getParameter("tipo");
            String liga = null;
            String imagen = request.getParameter("imagen");
            imagen = "/img/" + imagen;
            InterfazDatos facade = null;
            try {
                facade = InterfazDatos.instancia();
            } catch (Exception e){
                System.err.println("ERROR: creando interfaz");
            }
            try {
                ArticuloVO art = new ArticuloVO(nombre, precio, desc, imagen, tipo.charAt(0));
                facade.crearArticulo(art);
                System.out.println("Modificado precio articulo: " + art.getNombre());
                response.sendRedirect("/MostrarObjetosTienda.do");
            } catch (ExceptionCampoInvalido exceptionCampoInvalido){
                error = "camposArt";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
            } catch (SQLException sql) {
                error = "sqlArt";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
            }catch (NullPointerException e){
                System.err.println("ERROR: fachada creado, error al invocar " +
                        "métodos");
                e.printStackTrace();
            }
        }else {
            error = "sessionNotExist";
            request.setAttribute("error", error);
            RequestDispatcher dispatcher = request.getRequestDispatcher
                    ("/jsp/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
