package servlet;

import basedatos.InterfazDatos;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MostrarTorneoServlet")
public class MostrarTorneoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InterfazDatos facade;
        try{
            facade = InterfazDatos.instancia();
        }catch (Exception e){
            e.printStackTrace();
            response.sendRedirect("jsp/login.jsp");
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
