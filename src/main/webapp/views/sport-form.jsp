<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 10/2/2025
  Time: 3:50 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="formSportId" value="${not empty param.sportId ? param.sportId : sport.id}" />
<c:set var="formSportName" value="${not empty param.sportName ? param.sportName : sport.sportName}" />
<c:set var="formActive" value="${not empty param.active ? param.active : (sport.active ? '1' : '0')}" />


<c:choose>
    <c:when test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
        <section class="page-section">
            <h2 class="text-center">
                <c:choose>
                    <c:when test="${not empty formSportId}">Modifier un sport</c:when>
                    <c:otherwise>Ajouter un sport</c:otherwise>
                </c:choose>
            </h2>
            <!-- RETOUR -->
            <div class="text-start mb-3">
                <a href="${pageContext.request.contextPath}/sport" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Retour vers les sports
                </a>
            </div>

            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6"> <%-- Centré et largeur moyenne --%>
                        <form method="post" action="${pageContext.request.contextPath}/sport" value="${not empty formSportId ? 'edit' : 'create'}">

                            <!-- ID -->
                            <c:if test="${not empty formSportId}">
                                <!-- Champ caché pour envoyer l'ID -->
                                <input type="hidden" name="sportId" value="${formSportId}" />

                                <!-- Champ visible mais non modifiable -->
                                <div class="form-floating mb-3">
                                    <input class="form-control" type="text" id="sportIdDisplay"
                                           placeholder="ID" value="${formSportId}" disabled />
                                    <label for="sportIdDisplay">ID du sport</label>
                                </div>
                            </c:if>


                            <!-- sport Name -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="text" name="sportName" id="sportName"
                                       placeholder="Nom du sport" required
                                       value="${formSportName}"/>
                                <label for="sportName">Nom du sport</label>
                            </div>
                            <c:if test="${not empty errorSportName}">
                                <div class="text-danger mb-3">${errorSportName}</div>
                            </c:if>

                            <!-- Active -->
                            <div class="form-floating mb-3">
                                <select class="form-select" id="active" name="active" required>
                                    <option value="1" ${formActive == '1' ? 'selected' : ''}>Actif</option>
                                    <option value="0" ${formActive == '0' ? 'selected' : ''}>Non actif</option>
                                </select>
                                <label for="active">Statut du sport</label>
                            </div>
                            <c:if test="${not empty errorActive}">
                                <div class="text-danger mb-3">${errorActive}</div>
                            </c:if>
                            <div class="text-center">
                                <button class="btn btn-success" type="submit">
                                    <c:choose>
                                        <c:when test="${not empty formSportId}">Modifier</c:when>
                                        <c:otherwise>Ajouter</c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </c:when>
</c:choose>
