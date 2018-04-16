package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.StatsUsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ComprarObjetoServlet")
public class ComprarObjetoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String error;
        if (session != null){
            try {
                Integer i = Integer.parseInt(request.getParameter("id_objeto"));
                ArrayList<ArticuloUsuarioVO> articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
                ArticuloUsuarioVO art = articulos.get(i);
                InterfazDatos facade = null;
                try {
                    facade = InterfazDatos.instancia();
                } catch (Exception e){
                    System.err.println("ERROR: creando interfaz");
                }
                try {
                    facade.comprarArticuloUsuario(art);
                    System.out.println("Articulo " + art.getNombre() + " comprado");
                    response.sendRedirect("/MostrarObjetosTienda.do");
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
            } catch (Exception e){
                error = "objectNotExist";
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher
                        ("/MostrarObjetosTienda.do");
                dispatcher.forward(request,response);
            }
        }else {
            error = "sessionNotExist";
            request.setAttribute("error", error);
            RequestDispatcher dispatcher = request.getRequestDispatcher
                    ("index.jsp");
            dispatcher.forward(request, response);
        }

    }
}
