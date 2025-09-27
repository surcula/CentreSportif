package servlets;


import business.EventBusiness;
import entities.Event;
import services.EventServiceImpl;
import business.ServletUtils;
import static constants.Rooting.*;
import mappers.EventMapper;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import dto.EMF;
import Tools.Result;


/**
 * The class to view the JSP form page and to send the data of the form in DB
 * @author Franz
 *
 */
@MultipartConfig
@WebServlet(name = "EventServlet", value = "/event")
public class EventServlet extends HttpServlet {
    /**
     * Variable for the entity manager
     */
    private EntityManager em;
    /**
     * variable to create the service for event
     */
    private EventServiceImpl eventService;
    /**
     * variable to create the business for event
     */
    private EventBusiness eventBusiness;
    //private EntityManagerFactory entityManagerFactory;
    /**
     * Method to view the form
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventForm = request.getParameter("form");
        String editEventForm = request.getParameter("editForm");

        if("true".equals(eventForm)) {
            ServletUtils.forwardWithContent(request, response,"/views/event-form.jsp", "/views/template/template.jsp");
        }

        if(editEventForm != null) {
            //modifier un évènement
            request.setAttribute("eventId", editEventForm);

            ServletUtils.forwardWithContent(request, response,"/views/event-form.jsp", "/views/template/template.jsp");
        }
    }
    /**
     * The method to send data in DB
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @return request
     * @retunr response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //vérifier les droits lorsque l'utilisateur est connecté mais la connexion n'est pas opérationnelle
        //HttpSession session = request.getSession(false);
        //if(session == null || ServletUtils.isFullAuthorized(session.getAttribute("role").toString())) {
        //    ServletUtils.redirectWithMessage(request, response, "Accès refusé", "error", "/home");
        //    return;
        //}
        //Validation des données
        Result<Event> baseresult = EventBusiness.initCreateForm(
                request.getParameter("eventName"),
                request.getParameter("startDateHour"),
                request.getParameter("endDateHour"),
                request.getParameter("description"),
                request.getParameter("image"),
                request.getParameter("status")
        );

        if(baseresult.isSuccess()){
            try {
                em = EMF.getEM();
                eventService = new EventServiceImpl(em);
                em.getTransaction().begin();

                eventService.create(baseresult.getData());
                em.getTransaction().commit();
                ServletUtils.redirectWithMessage(request, response, "Evènement créé avec succès", "success", "/event" );
            }catch (Exception e) {
                if(em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), EVENT_FORM_JSP, TEMPLATE);
            }finally {
                if (em != null) em.close();
            }
        }else {
            ServletUtils.forwardWithErrors(request, response, baseresult.getErrors(), EVENT_FORM_JSP,TEMPLATE);
        }
    }
}