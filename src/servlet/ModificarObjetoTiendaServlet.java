package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.modelo.ArticuloUsuarioVO;
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

@WebServlet(name = "ModificarObjetoTiendaServlet")
public class ModificarObjetoTiendaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String error;
        if (session != null){
            try {
                Integer pos = Integer.parseInt(request.getParameter("posObjeto"));
                Integer precio = Integer.parseInt(request.getParameter("precio"));
                ArrayList<ArticuloUsuarioVO> articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
                ArticuloUsuarioVO art = articulos.get(pos);
                art.setPrecio(precio);
                ArrayList<LigaVO> ligas;
                InterfazDatos facade = null;
                try {
                    facade = InterfazDatos.instancia();
                } catch (Exception e){
                    System.err.println("ERROR: creando interfaz");
                }
                try {
                    facade.modificarArticulo(art);
//                    ligas = facade.lig
                    System.out.println("Modificado precio articulo: " + art.getNombre());
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
                //TODO: se conserva error
                request.setAttribute("error",error);
                RequestDispatcher dispatcher=request.getRequestDispatcher
                        ("/MostrarObjetosTienda.do");
                dispatcher.forward(request,response);
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
