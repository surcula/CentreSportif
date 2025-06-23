<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/23/2025
  Time: 2:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Les halls</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
        <div class="text-center mt-3">
            <a class="btn btn-success"
               href="${pageContext.request.contextPath}/hall">Retry</a>
        </div>

    </c:if>
    <c:if test="${empty error}">
        <p>${error}</p>

        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Nom</th>
                <th scope="col">Dimension (Largeur X Longueur X Hauteur)</th>
                <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
                    <th scope="col">Actif</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="hall" items="${halls}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${hall.hallName}</td>
                    <td>${hall.width} X ${hall.length} X ${hall.height}</td>
                    <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
                        <td>${hall.active}</td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</section>