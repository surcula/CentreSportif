<%--
  Created by IntelliJ IDEA.
  User: Sarah
  Date: 10/13/2025
  Time: 4:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="formSportFieldId" value="${not empty param.sportFieldId ? param.sportFieldId : sportField.id}"/>
<c:set var="formFieldId" value="${not empty param.fieldId ? param.fieldId : field.id}"/>

<c:choose>
    <c:when test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
        <section class="page-section">
            <h2 class="text-center">
                <c:choose>
                    <c:when test="${not empty formSportFieldId}">Modifier l'attribution d'un terrain</c:when>
                    <c:otherwise>Attribuer un sport à un terrain</c:otherwise>
                </c:choose>
            </h2>

            <!-- Formulaire choix du Field -->
            <form method="get" action="${pageContext.request.contextPath}/sportfield">
                <div class="row">
                    <div class="col-12 col-md-6 col-lg-4">
                        <div class="form-floating mb-3">
                            <select class="form-select" id="sportFieldId" name="sportFieldId" required>
                                <option value="">-- Choisir un terrain --</option>
                                <c:forEach var="f" items="${fields}">
                                    <option value="${f.id}" ${formFieldId == f.id ? 'selected' : ''}>
                                            ${f.fieldName}
                                    </option>
                                </c:forEach>
                            </select>
                            <label for="sportFieldId">choix du terrain :</label>
                        </div>
                        <c:if test="${not empty errorFieldId}">
                            <div class="text-danger mb-3">${errorFieldId}</div>
                        </c:if>
                    </div>
                    <button class="btn btn-success" type="submit">
                        Rechercher
                    </button>
                </div>
            </form>
        </section>
    </c:when>
</c:choose>