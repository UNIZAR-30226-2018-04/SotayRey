package servlet;

import basedatos.InterfazDatos;
import basedatos.modelo.TorneoPeriodicoVO;
import basedatos.modelo.TorneoVO;
import basedatos.modelo.UsuarioVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "GestionarTorneoServlet")
public class GestionarTorneoServlet extends HttpServlet {

    private void error(String error , HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        // Error con las fechas
        request.setAttribute("error", error);
        RequestDispatcher dispatcher = request.getRequestDispatcher
                ("jsp/torneos.jsp");
        dispatcher.forward(request, response);
    }

    private Timestamp transformarFechas(String date, String time,
                                        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        Timestamp res;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(date + " " + time + ":59");
            res = new java.sql.Timestamp(parsedDate.getTime());
            return res;
        } catch(Exception e) { //this generic but you can control another types of exception
            System.err.println("ERROR: transformando fechas");
            e.printStackTrace();
            error("date", request, response);
            return new Timestamp(System.currentTimeMillis());
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String error;
        if(session == null){
            response.sendRedirect("jsp/login.jsp");
        }else if (session.getAttribute("userId") == null) {
            response.sendRedirect("jsp/login.jsp");
        }else{
            UsuarioVO usuarioVO = (UsuarioVO) session.getAttribute("userId");
            if (usuarioVO.getAdmin() && request.getParameter("action_torneo") != null){
                InterfazDatos facade;
                try{
                    facade = InterfazDatos.instancia();
                }catch (Exception e){
                    e.printStackTrace();
                    response.sendRedirect("jsp/torneos.jsp");
                    return;
                }

                TorneoVO torneo;

                String action_torneo = request.getParameter("action_torneo");

                String nombre;
                String desc;
                int numFases;
                int premioPuntuacionPrimera;
                int premioDivisaPrimera;
                int dias = 7;
                String tipo;
                String date_ini;
                String time_ini;
                Timestamp timeInicio;

                switch (action_torneo){
                    case "crear":

                        nombre = request.getParameter("nombre");
                        desc = request.getParameter("desc");
                        numFases = Integer.parseInt(request.getParameter("fases"));
                        premioPuntuacionPrimera = Integer.parseInt(request.getParameter("punt"));
                        premioDivisaPrimera = Integer.parseInt(request.getParameter("divisas"));
                        dias = 7;
                        tipo = request.getParameter("puntual");

                        date_ini = request.getParameter("date_ini");
                        time_ini = request.getParameter("time_ini");

                       timeInicio = transformarFechas(date_ini, time_ini, request, response);

                        if (timeInicio.after(new Timestamp(System.currentTimeMillis()))){
                            try {
                                if (tipo.equals("0")){ // Torneo periódico
                                    dias = Integer.parseInt(request.getParameter("dias"));
                                    facade.crearTorneoPeriodico(new TorneoPeriodicoVO(nombre, desc,
                                            timeInicio, dias, numFases, premioPuntuacionPrimera, premioDivisaPrimera));
                                    System.out.println("Torneo periodico creado");
                                }
                                else {
                                    facade.crearTorneo(new TorneoVO(nombre, desc, timeInicio,
                                            true, numFases, premioPuntuacionPrimera, premioDivisaPrimera));
                                    System.out.println("Torneo puntual creado");
                                }

                            } catch (Exception e){
                                System.err.println("ERROR: creando torneo en la bd.");
                                e.printStackTrace();

                                error("interno", request, response);
                                return;
                            }
                        } else {
                            System.err.println("ERROR: creando torneo en la bd. Fecha incorrecta");
                            error("date", request, response);
                            return;
                        }

                        break;

                    case "modificar":

                        premioPuntuacionPrimera = Integer.parseInt(request.getParameter("punt_mod"));
                        premioDivisaPrimera = Integer.parseInt(request.getParameter("divisas_mod"));

                        date_ini = request.getParameter("date_ini");
                        time_ini = request.getParameter("time_ini");

                        timeInicio = transformarFechas(date_ini, time_ini, request, response);

                        if (timeInicio.after(new Timestamp(System.currentTimeMillis()))){
                            try {
                                if (session.getAttribute("torneos") != null) {
                                    int pos = Integer.parseInt(request.getParameter("pos_torneo_mod"));
                                    ArrayList<TorneoVO> torneos = (ArrayList<TorneoVO>) session.getAttribute("torneos");
                                    torneo = torneos.get(pos);
                                    torneo.setPremioDivisaPrimera(premioDivisaPrimera);
                                    torneo.setPremioPuntuacionPrimera(premioPuntuacionPrimera);
                                    torneo.setTimeInicio(timeInicio);
                                    facade.modificarTorneo( torneo);
                                    System.out.println("Torneo modificado");
                                } else {
                                    // Torneos no encontrado, es imposible si está registrado
                                    System.out.println("ERROR: problema al buscar torneo");

                                    error("interno", request, response);
                                }
                            } catch (Exception e){
                                System.err.println("ERROR: creando torneo en la bd.");
                                e.printStackTrace();

                                error("interno", request, response);
                                return;
                            }
                        } else {
                            System.err.println("ERROR: creando torneo en la bd. Fecha incorrecta");

                            error("date", request, response);
                            return;
                        }

                        break;
                    case "eliminar":
                        if (session.getAttribute("torneos") != null){
                            int pos = Integer.parseInt(request.getParameter("btnEliminar"));
                            ArrayList<TorneoVO> torneos = (ArrayList<TorneoVO>) session.getAttribute("torneos");
                            try {
                                TorneoVO t = torneos.get(pos);
                                facade.eliminarTorneo(t.getId());
                                System.out.println("Torneo eliminado");
                            } catch(Exception e){
                                System.err.println("ERROR: error elimnando torneo");
                                e.printStackTrace();

                                error("interno", request, response);
                                return;
                            }
                        } else {
                            // Torneos no encontrado al eliminarlos
                            System.out.println("ERROR: torneos no encontrado");
                            error = "userNotFound";
                            request.setAttribute("error", error);
                            RequestDispatcher dispatcher = request.getRequestDispatcher
                                    ("jsp/login.jsp");
                            dispatcher.forward(request, response);
                        }

                        break;
                }
                ArrayList<TorneoVO> torneos = null;
                try {
                    torneos = facade.obtenerTorneosProgramados();
                } catch(Exception e){
                    e.printStackTrace();

                    error("interno", request, response);
                    return;
                }



                session.setAttribute("torneos",torneos);
                // Correcto
                error = "done";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/torneos.jsp");
                dispatcher.forward(request, response);

            } else {
                // Error usuario no encontrado
                error = "userNotFound";
                request.setAttribute("error", error);
                RequestDispatcher dispatcher = request.getRequestDispatcher
                        ("jsp/login.jsp");
                dispatcher.forward(request, response);
                return;

            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
