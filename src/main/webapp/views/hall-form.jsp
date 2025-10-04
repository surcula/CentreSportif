<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/23/2025
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US" /> <!-- Pour forcer le point comme séparateur décimal -->

<c:set var="formHallId" value="${not empty param.hallId ? param.hallId : hallId}" />
<c:set var="formHallName" value="${not empty param.hallName ? param.hallName : hallName}" />
<c:set var="formLengthRaw" value="${not empty param.length ? param.length : hallLength}" />
<c:set var="formWidthRaw" value="${not empty param.width ? param.width : hallWidth}" />
<c:set var="formHeightRaw" value="${not empty param.height ? param.height : hallHeight}" />
<c:set var="formActive" value="${not empty param.active ? param.active : (hallActive ? '1' : '0')}" />

<fmt:formatNumber var="formLength" value="${formLengthRaw}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
<fmt:formatNumber var="formWidth" value="${formWidthRaw}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
<fmt:formatNumber var="formHeight" value="${formHeightRaw}" type="number" minFractionDigits="2" maxFractionDigits="2"/>


<c:choose>
    <c:when test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
        <section class="page-section">
            <h2 class="text-center">
                <c:choose>
                    <c:when test="${not empty hallId}">Modifier un hall</c:when>
                    <c:otherwise>Ajouter un hall</c:otherwise>
                </c:choose>
            </h2>
            <!-- RETOUR -->
            <div class="text-start mb-3">
                <a href="${pageContext.request.contextPath}/hall" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Retour vers les halls
                </a>
            </div>

            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6"> <%-- Centré et largeur moyenne --%>
                        <form method="post" action="${pageContext.request.contextPath}/hall" value="${not empty hallId ? 'edit' : 'create'}">

                            <!-- ID -->
                            <c:if test="${not empty formHallId}">
                                <!-- Champ caché pour envoyer l'ID -->
                                <input type="hidden" name="hallId" value="${formHallId}" />

                                <!-- Champ visible mais non modifiable -->
                                <div class="form-floating mb-3">
                                    <input class="form-control" type="text" id="hallIdDisplay"
                                           placeholder="ID" value="${formHallId}" disabled />
                                    <label for="hallIdDisplay">ID du hall</label>
                                </div>
                            </c:if>


                            <!-- Hall Name -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="text" name="hallName" id="hallName"
                                       placeholder="Nom du hall" required
                                       value="${formHallName}"/>
                                <label for="hallName">Nom du hall</label>
                            </div>
                            <c:if test="${not empty errorHallName}">
                                <div class="text-danger mb-3">${errorHallName}</div>
                            </c:if>

                            <!-- Length -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="number" step="0.01" name="length" id="length"
                                       placeholder="Longueur" required
                                       value="${formLength}"/>
                                <label for="length">Longueur (m)</label>
                            </div>
                            <c:if test="${not empty errorLength}">
                                <div class="text-danger mb-3">${errorLength}</div>
                            </c:if>

                            <!-- Width -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="number" step="0.01" name="width" id="width"
                                       placeholder="Largeur" required
                                       value="${formWidth}"/>
                                <label for="width">Largeur (m)</label>
                            </div>
                            <c:if test="${not empty errorWidth}">
                                <div class="text-danger mb-3">${errorWidth}</div>
                            </c:if>

                            <!-- Height -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="number" step="0.01" name="height" id="height"
                                       placeholder="Hauteur" required
                                       value="${formHeight}"/>
                                <label for="height">Hauteur (m)</label>
                            </div>
                            <c:if test="${not empty errorHeight}">
                                <div class="text-danger mb-3">${errorHeight}</div>
                            </c:if>
                            <!-- Active -->
                            <div class="form-floating mb-3">
                                <select class="form-select" id="active" name="active" required>
                                    <option value="1" ${formActive == '1' ? 'selected' : ''}>Actif</option>
                                    <option value="0" ${formActive == '0' ? 'selected' : ''}>Non actif</option>
                                </select>
                                <label for="active">Statut du hall</label>
                            </div>
                            <c:if test="${not empty errorActive}">
                                <div class="text-danger mb-3">${errorActive}</div>
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
    </c:when>
</c:choose>