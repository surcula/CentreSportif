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
        <a href="${pageContext.request.contextPath}/views/template/template.jsp?content=../reservation.jsp" class="btn btn-outline-secondary">
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
                        <input class="form-control" type="text" name="reservationSport" id="reservationSport"
                               placeholder="Choisissez un sport" required
                               value="${formReservationSport}"/>
                        <label for="reservationSport">Choisir un sport</label>
                    </div>
                    <c:if test="${not empty errorReservationSport}">
                        <div class="text-danger mb-3">${errorReservationSport}</div>
                    </c:if>
                    <!-- Choisir un local pour le foot il n'y en a qu'un pour les autres selon les réservations enregistrées-->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="text" name="reservationLocal" id="reservationLocal"
                               placeholder="Choisissez un local" required
                               value="${formReservationLocal}"/>
                        <label for="reservationLocal">Choisir un local</label>
                    </div>
                    <c:if test="${not empty errorReservationLocal}">
                        <div class="text-danger mb-3">${errorReservationLocal}</div>
                    </c:if>

                    <!-- Date de début -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="datetime-local"  name="startDate" id="startDate"
                               placeholder="Date de début" required
                               value="${formStartDateHour}"/>
                        <label for="startDate">Date de début</label>
                    </div>
                    <c:if test="${not empty errorStartDate}">
                        <div class="text-danger mb-3">${errorStartDate}</div>
                    </c:if>

                    <!-- Date de fin -->
                    <div class="form-floating mb-3">
                        <input class="form-control" type="datetime-local" name="endDate" id="endDate"
                               placeholder="Date de fin" required
                               value="${formEndDate}"/>
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
                            <option value="2" ${formStatus == '3' ? 'selected' : ''}>Payée</option>
                            <option value="2" ${formStatus == '2' ? 'selected' : ''}>A payer sur place</option>
                            <option value="1" ${formStatus == '1' ? 'selected' : ''}>Annulé</option>
                            <option value="0" ${formStatus == '0' ? 'selected' : ''}>Blacklist</option>
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
<!-- formulaire version Franz -->
<section class="page-section portfolio" id="portfolio">
    <h2>Réserver un Terrain</h2>

    <form action="/reserver-terrains" method="POST">
        <div>
            <label for="sport_reservation">Choisir un sport :</label>
            <select id="sport_reservation" name="sport" required>
                <option value="">Sélectionner un sport</option>
                <option value="football">Football</option>
                <option value="basketball">Basketball</option>
                <option value="tennis">Tennis</option>
                <option value="volleyball">Volleyball</option>
            </select>
        </div>

        <div>
            <label for="local_reservation">Choisir un local :</label>
            <select id="local_reservation" name="local" required>
                <option value="">Sélectionner un local</option>
                <option value="local_1">Local 1</option>
                <option value="local_2">Local 2</option>
                <option value="local_3">Local 3</option>
            </select>
        </div>

        <div>
            <label for="terrain_reservation">Choisir un terrain :</label>
            <select id="terrain_reservation" name="terrain" required>
                <option value="">Sélectionner un terrain</option>
                <option value="terrain_1">Terrain 1</option>
                <option value="terrain_2">Terrain 2</option>
                <option value="terrain_3">Terrain 3</option>
            </select>
        </div>

        <div>
            <label for="date_reservation">Date de réservation :</label>
            <input type="date" id="date_reservation" name="date" required>
        </div>

        <div>
            <label for="horaire_reservation">Horaire de réservation :</label>
            <input type="time" id="horaire_reservation" name="horaire" required>
        </div>

        <div>
            <label for="duree_reservation">Durée de la réservation (en heures) :</label>
            <input type="number" id="duree_reservation" name="duree" min="1" max="6" required>
        </div>

        <button type="submit">Réserver le terrain</button>
    </form>
</section>>
