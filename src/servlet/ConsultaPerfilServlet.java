package servlet;

import basedatos.InterfazDatos;
import basedatos.modelo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

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
                response.sendRedirect("jsp/matchmaking.jsp");
                return;
            }

            try{
                statsVO = facade.obtenerTodasStatsUsuario(username);
            }catch(Exception e ){
                e.printStackTrace();
                response.sendRedirect("jsp/matchmaking.jsp");
                return;
            }
            session.setAttribute("userStats",statsVO);

            ArrayList<ArticuloUsuarioVO> lista = null;
            try{
                lista = facade.obtenerArticulosUsuario(username);
            } catch (Exception e){
                e.printStackTrace();
                response.sendRedirect("jsp/matchmaking.jsp");
                return;
            }

            for(ArticuloUsuarioVO iter : lista){
                if(iter.getTipo() == 'A' && iter.isFavorito()){
                    session.setAttribute("avatar",iter);
                }
            }

            ArrayList<PartidaVO> historial = null;

            try{
                historial = facade.obtenerHistorialPartidas(username);
            } catch (Exception e){
                e.printStackTrace();
                response.sendRedirect("jsp/matchmaking.jsp");
                return;
            }

            session.setAttribute("historial", historial);

            response.sendRedirect("jsp/perfil.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        doPost(request,response);
    }
}
