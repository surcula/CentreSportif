<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 20-09-25
  Time: 16:57
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
            <c:when test="${not empty reservationId}">Modifier une réservation</c:when>
            <c:otherwise>Ajouter une réservation</c:otherwise>
        </c:choose>
    </h2>
    <!-- RETOUR -->
    <div class="text-start mb-3">
        <a href="${pageContext.request.contextPath}/reservation" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Retour vers les réservations
        </a>
    </div>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6"> <%-- Centré et largeur moyenne --%>
                <form method="post" action="${pageContext.request.contextPath}/reservation" value="${not empty reservationId ? 'edit' : 'create'}">

                    <!-- ID -->
                    <c:if test="${not empty formReservationId}">
                        <!-- Champ caché pour envoyer l'ID -->
                        <input type="hidden" name="eventId" value="${formReservationId}" />

                        <!-- Champ visible mais non modifiable -->
                        <div class="form-floating mb-3">
                            <input class="form-control" type="text" id="reservationIdDisplay"
                                   placeholder="ID" value="${formreservationId}" disabled />
                            <label for="reservationIdDisplay">ID de la réservation</label>
                        </div>
                    </c:if>
                    <!-- Nom de la réservation mettre un select ou un enum?-->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="text" name="reservationName" id="reservationName"
                               placeholder="Nom de la réservation" required
                               value="${formReservationName}"/>
                        <label for="reservationName">Nom de la réservation</label>
                    </div>
                    <c:if test="${not empty errorReservationName}">
                        <div class="text-danger mb-3">${errorReservationName}</div>
                    </c:if>
                    <!-- Choisir un sport-->
                    <div class="form-floating mb-3">
                        <select class="form-select" id="reservationsport" name="reservationsport" required>
                            <option value="6" ${formStatus == '6' ? 'selected' : ''}>Badminton</option>
                            <option value="5" ${formStatus == '5' ? 'selected' : ''}>Basketball</option>
                            <option value="4" ${formStatus == '4' ? 'selected' : ''}>Football</option>
                            <option value="3" ${formStatus == '3' ? 'selected' : ''}>Handball</option>
                            <option value="2" ${formStatus == '2' ? 'selected' : ''}>Squash</option>
                            <option value="1" ${formStatus == '1' ? 'selected' : ''}>Tennis</option>
                            <option value="0" ${formStatus == '0' ? 'selected' : ''}>Volleyball</option>
                        </select>
                        <label for="reservationSport">Choisir un sport</label>
                    </div>
                    <c:if test="${not empty errorReservationSport}">
                        <div class="text-danger mb-3">${errorReservationSport}</div>
                    </c:if>
                    <!-- Choisir un local pour le foot il n'y en a qu'un pour les autres selon les réservations enregistrées-->
                    <div class="form-floating mb-3">
                        <select class="form-select" id="reservationhall" name="reservationhall" required>
                            <option value="4" ${formStatus == '4' ? 'selected' : ''}>Hall 1</option>
                            <option value="3" ${formStatus == '3' ? 'selected' : ''}>Hall 2</option>
                            <option value="2" ${formStatus == '2' ? 'selected' : ''}>Hall 3</option>
                            <option value="1" ${formStatus == '1' ? 'selected' : ''}>Squash</option>
                            <option value="0" ${formStatus == '0' ? 'selected' : ''}>Tennis</option>
                        </select>
                        <label for="reservationhall">Choisir un hall</label>
                    </div>
                    <c:if test="${not empty errorReservationLocal}">
                        <div class="text-danger mb-3">${errorReservationLocal}</div>
                    </c:if>
                    <!-- Choisir un un terrain selon les réservations enregistrées dans l'ordre hall 1 si complet hall 2 ...-->
                    <div class="form-floating mb-3">
                        <select class="form-select" id="reservationfield" name="reservationfield" required>
                            <c:forEach var="f" items="${fields}">
                            <option value="${f.id}" ${formStatus == '2' ? 'selected' : ''} >${f.fieldName}</option>
                            <!--<option value="2" ${formStatus == '2' ? 'selected' : ''}>Terrain 2</option>-->
                            <!--option value="1" ${formStatus == '1' ? 'selected' : ''}>Terrain 3</option>-->
                            <!--<option value="0" ${formStatus == '0' ? 'selected' : ''}>Terrain 4</option>-->
                            </c:forEach>
                        </select>
                        <label for="reservationfield">Choisir un terrain</label>
                    </div>
                    <c:if test="${not empty errorReservationLocal}">
                        <div class="text-danger mb-3">${errorReservationLocal}</div>
                    </c:if>
                    <!-- Date de début -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="date"  name="startDate" id="startDate"
                               placeholder="Date de début" required
                               value="${formStartDateHour}"/>
                        <select class="form-select" id="startHour2" name="startHour2" required >
                            <option value="10:00">10:00</option>
                            <option value="10:30">10:30</option>
                            <option value="11:00">11:00</option>
                            <option value="11:30">11:30</option>
                            <option value="12:00">12:00</option>
                            <option value="12:30">12:30</option>
                            <option value="13:00">13:00</option>
                            <option value="13:30">13:30</option>
                            <option value="14:00">14:00</option>
                            <option value="14:30">14:30</option>
                            <option value="15:00">15:00</option>
                            <option value="15:30">15:30</option>
                            <option value="16:00">16:00</option>
                            <option value="16:30">16:30</option>
                            <option value="17:00">17:00</option>
                            <option value="17:30">17:30</option>
                            <option value="18:00">18:00</option>
                            <option value="18:30">18:30</option>
                            <option value="19:00">19:00</option>
                            <option value="19:30">19:30</option>
                            <option value="20:00">20:00</option>
                            <option value="20:30">20:30</option>
                            <option value="21:00">21:00</option>
                            <option value="21:30">21:30</option>
                        </select>
                        <label for="startDate">Date de début</label>
                    </div>
                    <c:if test="${not empty errorStartDate}">
                        <div class="text-danger mb-3">${errorStartDate}</div>
                    </c:if>

                    <!-- Date de fin -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="date" name="endDate" id="endDate"
                               placeholder="Date de fin" required
                               value="${formEndDate}"/>
                        <select class="form-select" id="endHour2" name="endHour2" required >
                            <option value="10:00">10:00</option>
                            <option value="10:30">10:30</option>
                            <option value="11:00">11:00</option>
                            option value="11:30">11:30</option>
                            <option value="12:00">12:00</option>
                            <option value="12:30">12:30</option>
                            <option value="13:00">13:00</option>
                            <option value="13:30">13:30</option>
                            <option value="14:00">14:00</option>
                            <option value="14:30">14:30</option>
                            <option value="15:00">15:00</option>
                            <option value="15:30">15:30</option>
                            <option value="16:00">16:00</option>
                            <option value="16:30">16:30</option>
                            <option value="17:00">17:00</option>
                            <option value="17:30">17:30</option>
                            <option value="18:00">18:00</option>
                            <option value="18:30">18:30</option>
                            <option value="19:00">19:00</option>
                            <option value="19:30">19:30</option>
                            <option value="20:00">20:00</option>
                            <option value="20:30">20:30</option>
                            <option value="21:00">21:00</option>
                            <option value="21:30">21:30</option>
                        </select>
                        <label for="endDate">Date de fin</label>
                    </div>
                    <c:if test="${not empty errorEndDate}">
                        <div class="text-danger mb-3">${errorEndDate}</div>
                    </c:if>
                    <!-- Heure -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="datetime-local" name="hour" id="hour"
                               placeholder="Heure" required
                               value="${formHour}"/>
                        <label for="hour">Durée de la réservation</label>
                    </div>
                    <c:if test="${not empty errorHour}">
                        <div class="text-danger mb-3">${errorHour}</div>
                    </c:if>
                    <!-- Statut -->
                    <div class="form-floating mb-3">
                        <select class="form-select" id="status" name="status" required>
                            <option value="2" ${formStatus == '3' ? 'selected' : ''}>Maintenue</option>
                            <option value="2" ${formStatus == '2' ? 'selected' : ''}>Confirmée</option>
                            <option value="1" ${formStatus == '1' ? 'selected' : ''}>Payée</option>
                            <option value="0" ${formStatus == '0' ? 'selected' : ''}>Remboursé</option>
                        </select>
                        <label for="status">Statut de la réservation</label>
                    </div>
                    <c:if test="${not empty errorStatus}">
                        <div class="text-danger mb-3">${errorStatus}</div>
                    </c:if>
                    <div class="text-center">
                        <button class="btn btn-success" type="submit">
                            <c:choose>
                                <c:when test="${not empty reservationId}">Modifier</c:when>
                                <c:otherwise>Ajouter</c:otherwise>
                            </c:choose>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
