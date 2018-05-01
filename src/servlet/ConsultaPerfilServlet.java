package servlet;
import basedatos.InterfazDatos;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.StatsUsuarioVO;
import basedatos.modelo.UsuarioVO;

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
            }

            try{
                statsVO = facade.obtenerTodasStatsUsuario(username);
            }catch(Exception e ){
                e.printStackTrace();
            }
            session.setAttribute("userStats",statsVO);

            ArrayList<ArticuloUsuarioVO> lista = null;
            try{
                lista = facade.obtenerArticulosUsuario(username);
            } catch (Exception e){
                e.printStackTrace();
            }

            for(ArticuloUsuarioVO iter : lista){
                if(iter.getTipo() == 'A' && iter.isFavorito()){
                    session.setAttribute("avatar",iter);
                }
            }

            response.sendRedirect("jsp/perfil.jsp");
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        doPost(request,response);
    }
}
