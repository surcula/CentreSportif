<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Mes abonnements</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <c:if test="${empty error}">
        <c:choose>
            <c:when test="${empty subscriptions}">
                <div class="alert alert-info text-center mt-3">
                    Vous n'avez pas encore d'abonnement.
                </div>
            </c:when>
            <c:otherwise>
                <table class="table mt-3">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Abonnement</th>
                        <th>Début</th>
                        <th>Fin</th>
                        <th>Solde</th>
                        <th>Statut</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="us" items="${subscriptions}" varStatus="s">
                        <tr>
                            <td>${s.index + 1}</td>
                            <td>${us.subscription.subscriptionName}</td> <!-- adapte au champ réel -->
                            <td>${us.startDate}</td>
                            <td>${us.endDate}</td>
                            <td>${us.quantityMax}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${us.active}"><span class="badge bg-success">Actif</span></c:when>
                                    <c:otherwise><span class="badge bg-secondary">Inactif</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>
</section>
