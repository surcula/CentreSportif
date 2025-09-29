package controllers.helpers;

import business.ServletUtils;
import entities.Field;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.*;

public class FieldControllerHelper {

    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, FIELD_FORM_JSP, TEMPLATE);
    }

    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Field> fields) throws ServletException, IOException {
        request.setAttribute("fields", fields);
        ServletUtils.forwardWithContent(request, response, FIELD_JSP, TEMPLATE);
    }

    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Field field) throws ServletException, IOException {
        request.setAttribute("fieldName", field.getFieldName());
        request.setAttribute("fieldActive", field.isActive());
        request.setAttribute("fieldId", field.getId());
        ServletUtils.forwardWithContent(request, response, FIELD_FORM_JSP, TEMPLATE);
    }
}

