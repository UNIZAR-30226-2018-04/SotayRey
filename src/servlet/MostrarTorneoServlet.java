package servlet;

import basedatos.InterfazDatos;
import basedatos.modelo.TorneoVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "MostrarTorneoServlet")
public class MostrarTorneoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        InterfazDatos facade;
        try{
            facade = InterfazDatos.instancia();
        }catch (Exception e){
            e.printStackTrace();
            response.sendRedirect("jsp/matchmaking.jsp");
            return;
        }

        ArrayList<TorneoVO> lista_torneos = null;
        try {
            facade.obtenerTorneosProgramados();
        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("jsp/matchmaking.jsp");
            return;
        }

        session.setAttribute("torneos",lista_torneos);
        response.sendRedirect("jsp/torneos.jsp");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
