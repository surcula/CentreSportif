package servlets;

import Tools.Result;
import business.ServletUtils;
import dto.EMF;
import entities.Field;
import entities.Sport;
import services.FieldServiceImpl;
import services.SportFieldServiceImpl;
import services.SportServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.*;

@WebServlet(name = "SportFieldServlet", value = "/sports-fields")
public class SportFieldServlet extends HttpServlet {


    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SportFieldServlet.class);



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sportFieldId = request.getParameter("sportFieldId");
        EntityManager em = EMF.getEM();
        if (sportFieldId == null) {
            SportServiceImpl sportServiceImpl = new SportServiceImpl(em);
            Result<List<Sport>> sports = sportServiceImpl.getAllSports(0,255);
        }
        FieldServiceImpl fieldService = new FieldServiceImpl(em);
        Result<List<Field>> fields = fieldService.getAllFields(0,255);
        request.setAttribute("fields", fields.getData());
        em.close();
        ServletUtils.forwardWithContent(request,response,SPORT_FIELD_FORM_JSP,TEMPLATE);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
 