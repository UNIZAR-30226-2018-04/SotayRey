package servlet;

import basedatos.InterfazDatos;
import basedatos.modelo.LigaVO;
import basedatos.modelo.StatsUsuarioVO;
import basedatos.modelo.UsuarioVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "MostrarLigaServlet")
public class MostrarLigaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UsuarioVO user = (UsuarioVO) session.getAttribute("userId");
        if(user == null){
            response.sendRedirect("jsp/login.jsp");
        }
        String error;
        InterfazDatos facade = null;
        try {
            facade = InterfazDatos.instancia();
        } catch (Exception e){
            System.err.println("ERROR: creando interfaz");
            response.sendRedirect("jsp/home.jsp");

        }

        ArrayList<LigaVO> ligas = null;
        try{
            ligas = facade.obtenerLigas();
        }catch (Exception e){
            System.err.println("ERROR: obteniendo ligas");
            response.sendRedirect("jsp/home.jsp");
        }

        session.setAttribute("ligas", ligas);

        for(LigaVO iterador : ligas){
            ArrayList<StatsUsuarioVO> clasificacion;
            try{
                clasificacion = facade.obtenerClasificacionLiga(iterador.getNombre());
                session.setAttribute(iterador.getNombre(),clasificacion);
            }catch (Exception e){
                System.err.println("ERROR: obteniendo clasificacion de la liga " + iterador.getNombre());
            }
        }
        response.sendRedirect("jsp/liga.jsp");




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
