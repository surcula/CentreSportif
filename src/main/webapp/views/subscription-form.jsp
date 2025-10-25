<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="isEdit" value="${not empty usersSubscription}"/>

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-4">
        <c:choose>
            <c:when test="${isEdit}">Modifier l'abonnement utilisateur</c:when>
            <c:otherwise>Assigner un abonnement</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <ul>
                <c:forEach var="e" items="${errors}">
                    <li>${e.value}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/subscription">
        <c:if test="${isEdit}">
            <input type="hidden" name="usersSubscriptionId" value="${usersSubscription.id}"/>
        </c:if>

        <c:choose>
            <c:when test="${isEdit}">
                <!-- Edition : user/sub désactivés pour éviter changement d'appartenance -->
                <div class="mb-3">
                    <label class="form-label">Membre</label>
                    <input type="text" class="form-control" disabled
                           value="${usersSubscription.user.firstName} ${usersSubscription.user.lastName}">
                </div>
                <div class="mb-3">
                    <label class="form-label">Abonnement</label>
                    <input type="text" class="form-control" disabled
                           value="${usersSubscription.subscription.subscriptionName}">
                </div>
            </c:when>
            <c:otherwise>
                <!-- Assignation : STAFF saisit le userId & subscriptionId (ou un select si tu as la liste) -->
                <div class="mb-3">
                    <label class="form-label">User ID</label>
                    <input type="number" class="form-control" name="userId" min="1" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Subscription ID</label>
                    <input type="number" class="form-control" name="subscriptionId" min="1" required>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">Date de début</label>
                <input type="date" class="form-control" name="startDate"
                       value="${isEdit ? usersSubscription.startDate : ''}" required>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">Date de fin</label>
                <input type="date" class="form-control" name="endDate"
                       value="${isEdit ? usersSubscription.endDate : ''}" required>
            </div>
        </div>

        <div class="mb-3">
            <label class="form-label">Solde (quantité)</label>
            <input type="number" class="form-control" name="quantity" min="0"
                   value="${isEdit ? usersSubscription.quantityMax : ''}" ${isEdit ? "" : "required"}>
        </div>

        <div class="mb-3">
            <label class="form-label">Actif</label>
            <select class="form-select" name="active" required>
                <option value="1" ${isEdit && usersSubscription.active ? 'selected' : ''}>Oui</option>
                <option value="0" ${isEdit && !usersSubscription.active ? 'selected' : ''}>Non</option>
            </select>
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">${isEdit ? 'Enregistrer' : 'Assigner'}</button>
            <a href="${pageContext.request.contextPath}/subscription" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</section>
