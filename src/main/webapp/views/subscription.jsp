<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Valeurs par défaut si pas de pagination fournie --%>
<c:set var="currentPage"   value="${empty page ? 1 : page}"/>
<c:set var="pageSize"      value="${empty size ? 10 : size}"/>
<c:set var="pages"         value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total"         value="${empty totalElements ? fn:length(subscriptions) : totalElements}"/>

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">
        Mes abonnements
    </h2>

    <%-- Zone erreur globale --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
        <div class="text-center mt-3">
            <a class="btn btn-success" href="${pageContext.request.contextPath}/subscription">Réessayer</a>
        </div>
    </c:if>

    <c:if test="${empty error}">
        <%-- Bouton Ajouter (back-office) --%>
        <c:if test="${sessionScope.role == 'ADMIN'
                 or sessionScope.role == 'BARMAN'
                 or sessionScope.role == 'SECRETARY'}">
            <a class="btn btn-primary my-3"
               href="${pageContext.request.contextPath}/subscription?form=true">
                Assigner un abonnement à un membre
            </a>
        </c:if>

        <%-- Barre info + choix taille de page (si pagination fournie) --%>
        <div class="d-flex justify-content-end align-items-center mb-2">
            <div class="me-3 text-muted small">
                    ${total} résultat<c:if test="${total > 1}">s</c:if>
                — Page ${currentPage} / ${pages}
            </div>
            <form method="get" action="${pageContext.request.contextPath}/subscription"
                  class="d-flex align-items-center">
                <label for="size" class="me-2 small">Taille page</label>
                <select id="size" name="size" class="form-select form-select-sm"
                        onchange="this.form.submit()">
                    <option value="5"  ${pageSize == 5  ? 'selected' : ''}>5</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                </select>
                <input type="hidden" name="page" value="${currentPage}"/>
            </form>
        </div>

        <%-- Message si aucun abonnement --%>
        <c:if test="${empty subscriptions}">
            <div class="alert alert-info text-center">
                Vous n'avez pas encore d'abonnement.
            </div>
        </c:if>

        <%-- Tableau des UsersSubscription --%>
        <c:if test="${not empty subscriptions}">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Nom de l’abonnement</th>
                    <th scope="col">Début</th>
                    <th scope="col">Fin</th>
                    <th scope="col">Solde</th>
                    <th scope="col">Actif</th>
                    <c:if test="${sessionScope.role == 'ADMIN'
                       or sessionScope.role == 'BARMAN'
                       or sessionScope.role == 'SECRETARY'}">
                        <th scope="col">Actions</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="us" items="${subscriptions}" varStatus="st">
                    <tr>
                        <td>${(currentPage - 1) * pageSize + st.index + 1}</td>
                        <td>${us.subscription.subscriptionName}</td>
                        <td>
                            <fmt:formatDate value="${us.startDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${us.endDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>${us.quantityMax}</td>
                        <td>
              <span class="badge ${us.active ? 'bg-success' : 'bg-secondary'}">
                      ${us.active ? 'Oui' : 'Non'}
              </span>
                        </td>

                        <c:if test="${sessionScope.role == 'ADMIN'
                         or sessionScope.role == 'BARMAN'
                         or sessionScope.role == 'SECRETARY'}">
                            <td>
                                <div class="btn-group" role="group" aria-label="Actions abonnement">
                                    <a href="${pageContext.request.contextPath}/subscription?editForm=${us.id}"
                                       class="btn btn-outline-primary btn-sm">
                                        Modifier
                                    </a>

                                    <form method="post" action="${pageContext.request.contextPath}/subscription"
                                          style="display:inline;"
                                          onsubmit="return confirm('Basculer l’état actif ?');">
                                        <input type="hidden" name="action" value="toggleActive"/>
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

        <%-- Pagination --%>
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
