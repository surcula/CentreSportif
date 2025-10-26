<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentPage" value="${empty page ? 1 : page}"/>
<c:set var="pageSize"    value="${empty size ? 10 : size}"/>
<c:set var="pages"       value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total"       value="${empty totalElements ? fn:length(subscriptions) : totalElements}"/>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">
        Mes abonnements
    </h2>

    <!-- Bandeau panier si un draft existe -->
    <c:if test="${not empty sessionScope.draftOrderId}">
        <div class="alert alert-success d-flex justify-content-between align-items-center my-3">
            <span>🧺 Panier en cours — tu peux finaliser quand tu veux.</span>
            <a class="btn btn-sm btn-success" href="${ctx}/order?editForm=${sessionScope.draftOrderId}">
                Voir / payer la commande
            </a>
        </div>
    </c:if>

    <!-- Erreur globale -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
        <div class="text-center mt-3">
            <a class="btn btn-success" href="${ctx}/subscription">Réessayer</a>
        </div>
    </c:if>

    <c:if test="${empty error}">
        <!-- Bouton staff : assigner -->
        <c:if test="${sessionScope.role == 'ADMIN'
                 or sessionScope.role == 'BARMAN'
                 or sessionScope.role == 'SECRETARY'}">
            <a class="btn btn-primary my-3" href="${ctx}/subscription?form=true">
                Assigner un abonnement à un membre
            </a>
        </c:if>

        <!-- Pagination (top) -->
        <div class="d-flex justify-content-end align-items-center mb-2">
            <div class="me-3 text-muted small">
                    ${total} résultat<c:if test="${total > 1}">s</c:if> — Page ${currentPage} / ${pages}
            </div>
            <form method="get" action="${ctx}/subscription" class="d-flex align-items-center">
                <label for="size" class="me-2 small">Taille page</label>
                <select id="size" name="size" class="form-select form-select-sm" onchange="this.form.submit()">
                    <option value="5"  ${pageSize == 5  ? 'selected' : ''}>5</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                </select>
                <input type="hidden" name="page" value="${currentPage}"/>
            </form>
        </div>

        <!-- Aucun abonnement -->
        <c:if test="${empty subscriptions}">
            <div class="alert alert-info text-center">
                Vous n'avez pas encore d'abonnement.
            </div>
        </c:if>

        <!-- Tableau -->
        <c:if test="${not empty subscriptions}">
            <table class="table align-middle">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Nom de l’abonnement</th>
                    <th>Début</th>
                    <th>Fin</th>
                    <th>Solde</th>
                    <th>Actif</th>
                    <th>Paiement</th>
                    <c:if test="${sessionScope.role == 'ADMIN'
                       or sessionScope.role == 'BARMAN'
                       or sessionScope.role == 'SECRETARY'}">
                        <th>Actions</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="us" items="${subscriptions}" varStatus="st">
                    <tr>
                        <td>${(currentPage - 1) * pageSize + st.index + 1}</td>
                        <td>${us.subscription.subscriptionName}</td>
                        <td>${us.startDate}</td>
                        <td>${us.endDate}</td>
                        <td>${us.quantityMax}</td>
                        <td>
              <span class="badge ${us.active ? 'bg-success' : 'bg-secondary'}">
                      ${us.active ? 'Oui' : 'Non'}
              </span>
                        </td>

                        <!-- Paiement -->
                        <td>
                            <div class="d-flex gap-2">
                                <!-- Ajouter => ouvre /order?form=fromSubscription -->
                                <form method="get" action="${ctx}/order" class="d-inline">
                                    <input type="hidden" name="form" value="fromSubscription"/>
                                    <!-- Ici on envoie l'ID de UsersSubscription -->
                                    <input type="hidden" name="subscriptionId" value="${us.id}"/>
                                    <button type="submit" class="btn btn-outline-primary btn-sm">Ajouter</button>
                                </form>

                                <!-- Payer => POST quickPayFromSubscription -->
                                <form method="post" action="${ctx}/order" class="d-inline">
                                    <input type="hidden" name="action" value="quickPayFromSubscription"/>
                                    <!-- Ici on envoie l'ID de UsersSubscription -->
                                    <input type="hidden" name="subscriptionId" value="${us.id}"/>
                                    <button type="submit" class="btn btn-primary btn-sm">Payer</button>
                                </form>
                            </div>
                        </td>

                        <!-- Actions staff -->
                        <c:if test="${sessionScope.role == 'ADMIN'
                         or sessionScope.role == 'BARMAN'
                         or sessionScope.role == 'SECRETARY'}">
                            <td>
                                <div class="btn-group" role="group">
                                    <a href="${ctx}/subscription?editForm=${us.id}"
                                       class="btn btn-outline-primary btn-sm">Modifier</a>

                                    <form method="post" action="${ctx}/subscription" class="d-inline"
                                          onsubmit="return confirm('Basculer l’état actif ?');">
                                        <input type="hidden" name="action" value="activer"/>
                                        <input type="hidden" name="usersSubscriptionId" value="${us.id}"/>
                                        <button type="submit" class="btn btn-outline-warning btn-sm">
                                                ${us.active ? 'Désactiver' : 'Activer'}
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- Pagination (bottom) -->
        <c:if test="${pages > 1}">
            <nav aria-label="Pagination">
                <ul class="pagination justify-content-center">
                    <c:url var="firstUrl" value="/subscription">
                        <c:param name="page" value="1"/>
                        <c:param name="size" value="${pageSize}"/>
                    </c:url>
                    <c:url var="prevUrl" value="/subscription">
                        <c:param name="page" value="${currentPage - 1}"/>
                        <c:param name="size" value="${pageSize}"/>
                    </c:url>
                    <c:url var="nextUrl" value="/subscription">
                        <c:param name="page" value="${currentPage + 1}"/>
                        <c:param name="size" value="${pageSize}"/>
                    </c:url>
                    <c:url var="lastUrl" value="/subscription">
                        <c:param name="page" value="${pages}"/>
                        <c:param name="size" value="${pageSize}"/>
                    </c:url>

                    <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>">
                        <a class="page-link" href="${firstUrl}">&laquo; Première</a>
                    </li>
                    <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>">
                        <a class="page-link" href="${prevUrl}">Précédent</a>
                    </li>
                    <li class="page-item disabled">
                        <span class="page-link">Page ${currentPage} / ${pages}</span>
                    </li>
                    <li class="page-item <c:if test='${currentPage >= pages}'>disabled</c:if>">
                        <a class="page-link" href="${nextUrl}">Suivant</a>
                    </li>
                    <li class="page-item <c:if test='${currentPage >= pages}'>disabled</c:if>">
                        <a class="page-link" href="${lastUrl}">Dernière &raquo;</a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </c:if>
</section>
