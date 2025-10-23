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
            <c:when test="${not empty event.id}">Modifier un évènement</c:when>
            <c:otherwise>Ajouter un évènement</c:otherwise>
        </c:choose>
    </h2>
    <!-- RETOUR -->
    <div class="text-start mb-3">
        <a href="${pageContext.request.contextPath}/event" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Retour vers les évènements
        </a>
    </div>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6"> <%-- Centré et largeur moyenne --%>
                <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/event" value="${not empty eventId ? 'edit' : 'create'}">

                    <!-- ID -->
                    <c:if test="${not empty event.id}">
                        <!-- Champ caché pour envoyer l'ID -->
                        <input type="hidden" name="eventId" value="${event.id}" />

                        <!-- Champ visible mais non modifiable -->
                        <div class="form-floating mb-3">
                            <input class="form-control" type="text" id="eventIdDisplay"
                                   placeholder="ID" value="${event.id}" disabled />
                            <label for="eventIdDisplay">ID de l'évènement</label>
                        </div>
                    </c:if>
                    <!-- Nom de l'évènement -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="text" name="eventName" id="eventName"
                               placeholder="Nom de l'évènement" required
                               value="${event.eventName}"/>
                        <label for="eventName">Nom du de l'évènement</label>
                    </div>
                    <c:if test="${not empty errorEventName}">
                        <div class="text-danger mb-3">${errorEventName}</div>
                    </c:if>

                    <!-- Date de début et heure -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="datetime-local"  name="startDateHour" id="startDateHour"
                               placeholder="Date et heure de début" required
                               value="${event.beginDateHour}"/>
                        <label for="startDateHour">Date et heure de début</label>
                    </div>
                    <c:if test="${not empty errorStartDateHour}">
                        <div class="text-danger mb-3">${errorStartDateHour}</div>
                    </c:if>

                    <!-- Date de fin et heure -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="datetime-local" name="endDateHour" id="endDateHour"
                               placeholder="Date et heure de fin" required
                               value="${event.endDateHour}"/>
                        <label for="endDateHour">Date et heure de fin</label>
                    </div>
                    <c:if test="${not empty errorEndDateHour}">
                        <div class="text-danger mb-3">${errorEndDateHour}</div>
                    </c:if>

                    <!-- Description -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="text" name="description" id="description"
                               placeholder="Information" required
                               value="${event.info}"/>
                        <label for="description">Description</label>
                    </div>
                    <c:if test="${not empty errorDesciption}">
                        <div class="text-danger mb-3">${errorDescription}</div>
                    </c:if>
                    <!-- Image -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="file" name="image" id="image"
                               placeholder="Sélectionnez une image"  ${empty event.id ? 'required' : ''}/>
                        <label for="image">Image</label>
                    </div>
                    <c:if test="${not empty errorImage}">
                        <div class="text-danger mb-3">${errorImage}</div>
                    </c:if>
                    <!-- Statut -->
                    <div class="form-floating mb-3">
                        <select class="form-select" id="status" name="status" required>
                            <option value="1" ${event.active ? 'selected' : ''}>En cours</option>
                            <option value="0" ${not event.active  ? 'selected' : ''}>Terminé</option>
                        </select>
                        <label for="status">Statut de l'évènement</label>
                    </div>
                    <c:if test="${not empty errorStatus}">
                        <div class="text-danger mb-3">${errorStatus}</div>
                    </c:if>
                    <div class="text-center">
                        <button class="btn btn-success" type="submit">
                            <c:choose>
                                <c:when test="${not empty event.id}">Modifier</c:when>
                                <c:otherwise>Ajouter</c:otherwise>
                            </c:choose>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
