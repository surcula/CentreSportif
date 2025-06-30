package servlets;

import Tools.Result;
import business.HallBusiness;
import business.ServletUtils;
import dto.EMF;
import dto.HallCreateForm;
import entities.Hall;
import services.HallServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Hall", value = "/hall")
public class HallServlet extends HttpServlet {


    private EntityManager em;
    private HallServiceImpl hallService;
    private static final String TEMPLATE = "/views/template/template.jsp";
    private static final String HALL_JSP = "/views/hall.jsp";
    private static final String HALL_FORM_JSP = "/views/hall-form.jsp";
    private static final String HOME_JSP = "/views/home.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        em = EMF.getEM();

        if (request.getParameter("form") != null) {
            HttpSession session = request.getSession(false);
            if (ServletUtils.hasRole((String) session.getAttribute("role"))) {
                ServletUtils.forwardWithContent(request, response, HALL_FORM_JSP, TEMPLATE);
            } else {
                ServletUtils.redirectToURL(response, request.getContextPath() + "/home");
            }

        } else {
            try {
                HallServiceImpl hallService = new HallServiceImpl(em);
                Result<List<Hall>> result = hallService.getAllActiveHalls();
                if (result.isSuccess()) {
                    request.setAttribute("halls", result.getData());
                    ServletUtils.forwardWithContent(request, response, HALL_JSP, TEMPLATE);
                } else {

                    String errorMsg = String.join("; ",
                            result.getErrors().entrySet().stream()
                                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                                    .toArray(String[]::new)
                    );

                    ServletUtils.forwardWithError(request, response,
                            "Could not load halls: " + errorMsg,
                            HALL_JSP, TEMPLATE
                    );
                }
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
        Result<HallCreateForm> result = HallBusiness.initCreateForm(
                request.getParameter("hallName"),
                request.getParameter("width"),
                request.getParameter("length"),
                request.getParameter("height"),
                request.getParameter("active")
        );
        if (result.isSuccess()) {
            try{
                em = EMF.getEM();
                hallService = new HallServiceImpl(em);
                em.getTransaction().begin();
                hallService.create(result.getData());
                em.getTransaction().commit();
                ServletUtils.redirectWithSucces(
                        request,
                        response,
                        "Hall créé avec succès",
                        "/hall"
                );
            }catch (Exception e){
                ServletUtils.forwardWithError(
                        request,
                        response,
                        e.getMessage(),
                        HALL_FORM_JSP,
                        TEMPLATE
                );
            }finally {
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
                em.close();
            }

        } else {
            ServletUtils.forwardWithErrors(
                    request,
                    response,
                    result.getErrors(),
                    HALL_FORM_JSP,
                    TEMPLATE
            );

        }

    }
}
