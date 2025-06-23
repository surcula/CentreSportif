package servlets;

import business.ServletUtils;
import services.HallServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Hall", value = "/hall")
public class HallServlet extends HttpServlet {


    private EntityManagerFactory emf;
    private static final String TEMPLATE = "/views/template/template.jsp";
    private static final String HALL_JSP = "/views/hall.jsp";

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("default");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
 