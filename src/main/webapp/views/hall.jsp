<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/23/2025
  Time: 2:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
        <a class="btn btn-primary my-3" href="${pageContext.request.contextPath}/hall?form=true">Ajouter un nouveau hall</a>
    </c:if>

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
                    <th> Détails </th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="hall" items="${halls}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${hall.hallName}</td>
                    <td>
                        <fmt:formatNumber value="${hall.width}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                        X <fmt:formatNumber value="${hall.length}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                        X <fmt:formatNumber value="${hall.height}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </td>
                    <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
                        <td>${hall.active}</td>
                        <td>
                            <div class="btn-group" role="group" aria-label="Actions Hall">
                                <!-- Bouton Modifier -->
                                <a href="${pageContext.request.contextPath}/hall?editForm=${hall.id}" class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-pencil-square"></i> Modifier
                                </a>

                                <!-- Bouton Supprimer ou Activer -->
                                <c:choose>
                                    <c:when test="${hall.active}">
                                        <!-- Formulaire Supprimer -->
                                        <form method="post" action="${pageContext.request.contextPath}/hall" onsubmit="return confirm('Supprimer ce hall ?')" style="display: inline;">
                                            <input type="hidden" name="hallId" value="${hall.id}" />
                                            <input type="hidden" name="action" value="delete" />
                                            <button type="submit" class="btn btn-outline-danger btn-sm">
                                                <i class="bi bi-trash"></i> Supprimer
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Formulaire Activer -->
                                        <form method="post" action="${pageContext.request.contextPath}/hall" onsubmit="return confirm('Activer ce hall ?')" style="display: inline;">
                                            <input type="hidden" name="hallId" value="${hall.id}" />
                                            <input type="hidden" name="action" value="activer" />
                                            <button type="submit" class="btn btn-outline-warning btn-sm">
                                                <i class="bi bi-trash"></i> Activer
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>



                            </div>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</section>