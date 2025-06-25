package servlets;

import business.HallBusiness;
import business.ServletUtils;
import services.HallServiceImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "Hall", value = "/hall")
public class HallServlet extends HttpServlet {


    private EntityManagerFactory emf;
    private static final String TEMPLATE = "/views/template/template.jsp";
    private static final String HALL_JSP = "/views/hall.jsp";
    private static final String HALL_FORM_JSP = "/views/hall-form.jsp";
    private static final String HOME_JSP = "/views/home.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();
        if(request.getParameter("form") != null) {
            HttpSession session = request.getSession(false);
            if(session != null && "ADMIN".equals(session.getAttribute("role"))) {
                ServletUtils.forwardWithContent(request, response, HALL_FORM_JSP, TEMPLATE);
            }else{
                ServletUtils.redirectToURL(response, request.getContextPath() + "/home");
            }

        }else {
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                request.setAttribute("halls", hallService.getAllHalls());
                ServletUtils.forwardWithContent(request, response, HALL_JSP, TEMPLATE);
            } catch (Exception e) {
                ServletUtils.forwardWithError(request, response,
                        e.getMessage(), HALL_JSP, TEMPLATE);
            } finally {
                em.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = HallBusiness.initCreateForm(
                request.getParameter("hallName"),
                request.getParameter("width"),
                request.getParameter("length"),
                request.getParameter("height"),
                request.getParameter("active")
        );
        if(!errors.isEmpty()) {
            ServletUtils.forwardWithErrors(
                    request,
                    response,
                    errors,
                    HALL_JSP,
                    TEMPLATE
            );
        }else{
            ServletUtils.forwardWithSucces(
                    request,
                    response,
                    "Hall créé avec succès",
                    HALL_JSP,
                    TEMPLATE
            );
        }

    }
}
