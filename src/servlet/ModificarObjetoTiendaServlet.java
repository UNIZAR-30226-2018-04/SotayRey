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
import java.util.Enumeration;

@WebServlet(name = "ModificarObjetoTiendaServlet")
public class ModificarObjetoTiendaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String error;
        if (session != null){
            if (session.getAttribute("userId") != null) {
                UsuarioVO user = (UsuarioVO) session.getAttribute("userId");
                if (user.getAdmin()) {
                    try {
                        String[] res = request.getParameterValues("indiceObjeto");
                        Integer precio = Integer.parseInt(request.getParameter("precio" + res[0]));
                        Integer indice = Integer.parseInt(res[0]);
                        ArrayList<ArticuloUsuarioVO> articulos = (ArrayList<ArticuloUsuarioVO>) session.getAttribute("articulos");
                        ArticuloUsuarioVO art = articulos.get(indice);
                        art.setPrecio(precio);
                        InterfazDatos facade = null;
                        try {
                            facade = InterfazDatos.instancia();
                        } catch (Exception e) {
                            System.err.println("ERROR: creando interfaz");
                        }
                        try {
                            facade.modificarArticulo(art);
                            System.out.println("Modificado precio articulo: " + art.getNombre());
                            articulos = facade.obtenerArticulosTienda(user.getUsername());
                            session.setAttribute("articulos", articulos);
                            System.out.println("Articulos usuario obtenidos");
                            response.sendRedirect("/jsp/tienda.jsp");
                        } catch (ExceptionCampoInexistente exceptionCampoInexistente) {
                            error = "userNotFound";
                            request.setAttribute("error", error);
                            RequestDispatcher dispatcher = request.getRequestDispatcher
                                    ("jsp/login.jsp");
                            dispatcher.forward(request, response);
                        } catch (NullPointerException e) {
                            System.err.println("ERROR: fachada creado, error al invocar " +
                                    "métodos");
                            e.printStackTrace();
                            throw new ServletException();
                        } catch (Exception e) {
                            System.err.println("ERROR: accediendo a la tienda");
                            e.printStackTrace();
                            throw new ServletException();
                        }
                    } catch (Exception e) { // Si hay un null pointer entra aqui y reenvia a la tienda
                        error = "objectNotExist";
                        request.setAttribute("error", error);
                        RequestDispatcher dispatcher = request.getRequestDispatcher
                                ("/jsp/tienda.jsp");
                        dispatcher.forward(request, response);
                    }
                } else { // No es admin
                    error = "adminNotFound";
                    request.setAttribute("error", error);
                    RequestDispatcher dispatcher = request.getRequestDispatcher
                            ("jsp/login.jsp");
                    dispatcher.forward(request, response);
                }
            } else { // Usuario no registrado
                error = "userNotFound";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
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
