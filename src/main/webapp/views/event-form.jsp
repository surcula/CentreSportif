<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 20-09-25
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- formulaire version David -->
<section class="page-section">
    <h2 class="text-center">
        <c:choose>
            <c:when test="${not empty eventId}">Modifier un évènement</c:when>
            <c:otherwise>Ajouter un évènement</c:otherwise>
        </c:choose>
    </h2>
    <!-- RETOUR -->
    <div class="text-start mb-3">
        <a href="${pageContext.request.contextPath}/views/template/template.jsp?content=../event.jsp" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Retour vers les évènements
        </a>
    </div>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6"> <%-- Centré et largeur moyenne --%>
                <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/event" value="${not empty eventId ? 'edit' : 'create'}">

                    <!-- ID -->
                    <c:if test="${not empty formEventId}">
                        <!-- Champ caché pour envoyer l'ID -->
                        <input type="hidden" name="eventId" value="${formEventId}" />

                        <!-- Champ visible mais non modifiable -->
                        <div class="form-floating mb-3">
                            <input class="form-control" type="text" id="eventIdDisplay"
                                   placeholder="ID" value="${formEventId}" disabled />
                            <label for="eventIdDisplay">ID de l'évènement</label>
                        </div>
                    </c:if>
                    <!-- Nom de l'évènement -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="text" name="eventName" id="eventName"
                               placeholder="Nom de l'évènement" required
                               value="${formEventName}"/>
                        <label for="eventName">Nom du de l'évènement</label>
                    </div>
                    <c:if test="${not empty errorEventName}">
                        <div class="text-danger mb-3">${errorEventName}</div>
                    </c:if>

                    <!-- Date de début et heure -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="datetime-local"  name="startDateHour" id="startDateHour"
                               placeholder="Date et heure de début" required
                               value="${formStartDateHour}"/>
                        <label for="startDateHour">Date et heure de début</label>
                    </div>
                    <c:if test="${not empty errorStartDateHour}">
                        <div class="text-danger mb-3">${errorStartDateHour}</div>
                    </c:if>

                    <!-- Date de fin et heure -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="datetime-local" name="endDateHour" id="endDateHour"
                               placeholder="Date et heure de fin" required
                               value="${formEndDateHour}"/>
                        <label for="endDateHour">Date et heure de fin</label>
                    </div>
                    <c:if test="${not empty errorEndDateHour}">
                        <div class="text-danger mb-3">${errorEndDateHour}</div>
                    </c:if>

                    <!-- Description -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="text" name="description" id="description"
                               placeholder="Information" required
                               value="${formDescription}"/>
                        <label for="description">Description</label>
                    </div>
                    <c:if test="${not empty errorDesciption}">
                        <div class="text-danger mb-3">${errorDescription}</div>
                    </c:if>
                    <!-- Image -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="file" name="image" id="image"
                               placeholder="Sélectionnez une image" required/>
                        <label for="image">Image</label>
                    </div>
                    <c:if test="${not empty errorImage}">
                        <div class="text-danger mb-3">${errorImage}</div>
                    </c:if>
                    <!-- Statut -->
                    <div class="form-floating mb-3">
                        <select class="form-select" id="status" name="status" required>
                            <option value="1" ${formStatus == '1' ? 'selected' : ''}>En cours</option>
                            <option value="0" ${formStatus == '0' ? 'selected' : ''}>Terminé</option>
                        </select>
                        <label for="status">Statut de l'évènement</label>
                    </div>
                    <c:if test="${not empty errorStatus}">
                        <div class="text-danger mb-3">${errorStatus}</div>
                    </c:if>
                    <div class="text-center">
                        <button class="btn btn-success" type="submit">
                            <c:choose>
                                <c:when test="${not empty hallId}">Modifier</c:when>
                                <c:otherwise>Ajouter</c:otherwise>
                            </c:choose>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
