package servlet;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.ArticuloVO;
import basedatos.modelo.LigaVO;
import basedatos.modelo.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;



@MultipartConfig
@WebServlet(name = "AnyadirArticuloTiendaServlet")
public class AnyadirArticuloTiendaServlet extends HttpServlet {

    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String error;
        if (session != null){
            if (session.getAttribute("userId") != null){
                UsuarioVO user = (UsuarioVO) session.getAttribute("userId");
                if (user.getAdmin()){
                    String nombre = request.getParameter("modNombre");
                    Integer precio = Integer.parseInt(request.getParameter("modPrecio"));
                    String desc = request.getParameter("modDesc");
                    String tipo = request.getParameter("tipo");
                    String liga = request.getParameter("liga");
                    String imagen = request.getParameter("imagen");
//                    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
//                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
//                    String fileName = getSubmittedFileName(filePart);
//                    InputStream fileContent = filePart.getInputStream();
                    imagen = "/img/" + imagen;


                    InterfazDatos facade = null;
                    try {
                        facade = InterfazDatos.instancia();
                    } catch (Exception e){
                        System.err.println("ERROR: creando interfaz");
                    }
                    try {
                        ArticuloVO art = new ArticuloVO(nombre, precio, desc, imagen, tipo.charAt(0), new LigaVO(liga, 0, 100));
                        facade.crearArticulo(art);
                        System.out.println("Artículo añadido: " + art.getNombre());
                        response.sendRedirect("/MostrarObjetosTienda.do");
                    } catch (ExceptionCampoInvalido exceptionCampoInvalido){
                        error = "camposArt";
                        request.setAttribute("error", error);
                        RequestDispatcher dispatcher = request.getRequestDispatcher
                                ("jsp/anyadirArticulo.jsp");
                        dispatcher.forward(request, response);
                    } catch (SQLException sql) {
                        error = "sqlArt";
                        request.setAttribute("error", error);
                        RequestDispatcher dispatcher = request.getRequestDispatcher
                                ("jsp/anyadirArticulo.jsp");
                        dispatcher.forward(request, response);
                    }catch (NullPointerException e){
                        System.err.println("ERROR: fachada creado, error al invocar " +
                                "métodos");
                        e.printStackTrace();
                        throw new ServletException();
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
