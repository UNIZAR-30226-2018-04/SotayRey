package servlet;
import javax.servlet.http.HttpSession;

import basedatos.InterfazDatos;
import basedatos.modelo.UsuarioVO;

@javax.servlet.annotation.WebServlet(name = "ConsultaPerfilServlet")
public class ConsultaPerfilServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        HttpSession session = request.getSession(false);
        if(session == null){
            //TODO:Tratar error, redirigir a login
        }else if (session.getAttribute("userId") == null) {
            //TODO:Tratar error, redirigir a login
        }else{
            UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
            String username = usuarioVO.getUsername();
            InterfazDatos facade =  null;
            try{
                facade = InterfazDatos.instancia();
            } catch(Exception e){
                //TODO:Tratar error.
            }

            try{
                facade.obtenerStatsUsuario(username);
            }catch(Exception e ){

            }

        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        doPost(request,response);
    }
}
