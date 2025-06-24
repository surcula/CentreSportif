<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/23/2025
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${sessionScope.role =='ADMIN'}">
        <section class="page-section">
            <h2 class="text-center">Ajouter un hall</h2>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6"> <%-- Centré et largeur moyenne --%>
                        <form method="post" action="${pageContext.request.contextPath}/hall">
                            <!-- Hall Name -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="text" name="hallName" id="hallName"
                                       placeholder="Nom du hall" required
                                       value="${param.hallName}"/>
                                <label for="hallName">Nom du hall</label>
                            </div>
                            <c:if test="${not empty errorHallName}">
                                <div class="text-danger mb-3">${errorHallName}</div>
                            </c:if>

                            <!-- Length -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="number" step="0.01" name="length" id="length"
                                       placeholder="Longueur" required
                                       value="${param.length}"/>
                                <label for="length">Longueur (m)</label>
                            </div>
                            <c:if test="${not empty errorLength}">
                                <div class="text-danger mb-3">${errorLength}</div>
                            </c:if>

                            <!-- Width -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="number" step="0.01" name="width" id="width"
                                       placeholder="Largeur" required
                                       value="${param.width}"/>
                                <label for="width">Largeur (m)</label>
                            </div>
                            <c:if test="${not empty errorWidth}">
                                <div class="text-danger mb-3">${errorWidth}</div>
                            </c:if>

                            <!-- Height -->
                            <div class="form-floating mb-3">
                                <input class="form-control" type="number" step="0.01" name="height" id="height"
                                       placeholder="Hauteur" required
                                       value="${param.height}"/>
                                <label for="height">Hauteur (m)</label>
                            </div>
                            <c:if test="${not empty errorHeight}">
                                <div class="text-danger mb-3">${errorHeight}</div>
                            </c:if>
                            <!-- Active -->
                            <div class="form-floating mb-3">
                                <select class="form-select" id="active" name="active" required>
                                    <option value="1" ${param.active == '1' ? 'selected' : ''}>Actif</option>
                                    <option value="0" ${param.active == '0' ? 'selected' : ''}>Non actif</option>
                                </select>
                                <label for="active">Statut du hall</label>
                            </div>
                            <c:if test="${not empty errorActive}">
                                <div class="text-danger mb-3">${errorActive}</div>
                            </c:if>
                            <div class="text-center">
                                <button class="btn btn-success" type="submit">Ajouter</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </c:when>
</c:choose>