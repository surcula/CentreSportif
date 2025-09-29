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

<c:set var="formfieldId" value="${not empty param.fieldId ? param.fieldId : fieldId}" />
<c:set var="formfieldName" value="${not empty param.fieldName ? param.fieldName : fieldName}" />


<c:choose>
    <c:when test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
        <section class="page-section">
            <h2 class="text-center">
                <c:choose>
                    <c:when test="${not empty fieldId}">Modifier un terrain</c:when>
                    <c:otherwise>Ajouter un terrain</c:otherwise>
                </c:choose>
            </h2>
            <!-- RETOUR -->
            <div class="text-start mb-3">
                <a href="${pageContext.request.contextPath}/field" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Retour vers les terrains
                </a>
            </div>

            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6"> <%-- Centré et largeur moyenne --%>
                        <form method="post" action="${pageContext.request.contextPath}/field" value="${not empty fieldId ? 'edit' : 'create'}">

                            <!-- ID -->
                            <c:if test="${not empty formfieldId}">
                                <!-- Champ caché pour envoyer l'ID -->
                                <input type="hidden" name="fieldId" value="${formFieldId}" />

                                <!-- Champ visible mais non modifiable -->
                                <div class="form-floating mb-3">
                                    <input class="form-control" type="text" id="fieldIdDisplay"
                                           placeholder="ID" value="${formFieldId}" disabled />
                                    <label for="fieldIdDisplay">ID du field</label>
                                </div>
                            </c:if>


                            <!-- field Name -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="text" name="fieldName" id="fieldName"
                                       placeholder="Nom du terrain" required
                                       value="${formfieldName}"/>
                                <label for="fieldName">Nom du terrain</label>
                            </div>
                            <c:if test="${not empty errorfieldName}">
                                <div class="text-danger mb-3">${errorfieldName}</div>
                            </c:if>


                            <!-- Active -->
                            <div class="form-floating mb-3">
                                <select class="form-select" id="active" name="active" required>
                                    <option value="1" ${formActive == '1' ? 'selected' : ''}>Actif</option>
                                    <option value="0" ${formActive == '0' ? 'selected' : ''}>Non actif</option>
                                </select>
                                <label for="active">Statut du terrain</label>
                            </div>
                            <c:if test="${not empty errorActive}">
                                <div class="text-danger mb-3">${errorActive}</div>
                            </c:if>
                            <div class="text-center">
                                <button class="btn btn-success" type="submit">
                                    <c:choose>
                                        <c:when test="${not empty fieldId}">Modifier</c:when>
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