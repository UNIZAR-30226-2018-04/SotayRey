package servlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import basedatos.InterfazDatos;
import basedatos.modelo.StatsUsuarioVO;
import basedatos.modelo.UsuarioVO;

@javax.servlet.annotation.WebServlet(name = "ConsultaPerfilServlet")
public class ConsultaPerfilServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        HttpSession session = request.getSession(false);
        if(session == null){
            response.sendRedirect("jsp/login.jsp");
        }else if (session.getAttribute("userId") == null) {
            response.sendRedirect("jsp/login.jsp");
        }else{
            UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
            StatsUsuarioVO statsVO = null;
            String username = usuarioVO.getUsername();
            InterfazDatos facade =  null;

            try{
                facade = InterfazDatos.instancia();
            } catch(Exception e){
                e.printStackTrace();
                throw new ServletException();
            }

            try{
                statsVO = facade.obtenerTodasStatsUsuario(username);
            }catch(Exception e ){
            }
            session.setAttribute("userStats",statsVO);

            response.sendRedirect("jsp/perfil.jsp");
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        doPost(request,response);
    }
}
