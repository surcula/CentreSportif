<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentPage" value="${empty page ? 1 : page}"/>
<c:set var="pageSize" value="${empty size ? 10 : size}"/>
<c:set var="pages" value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total" value="${empty totalElements ? fn:length(orders) : totalElements}"/>

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Commandes</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
        <div class="text-center mt-3">
            <a class="btn btn-success" href="${pageContext.request.contextPath}/order">Retry</a>
        </div>
    </c:if>

    <c:if test="${empty error}">

        <c:if test="${sessionScope.role == 'ADMIN'
                or sessionScope.role == 'BARMAN'
                or sessionScope.role == 'SECRETARY'}">
            <a class="btn btn-primary my-3" href="${pageContext.request.contextPath}/order?form=true">
                Créer une commande
            </a>
        </c:if>

        <!-- Nb résultat + taille d’affichage -->
        <div class="d-flex justify-content-end align-items-center mb-2">
            <div class="me-3 text-muted small">
                    ${total} résultat<c:if test="${total > 1}">s</c:if> — Page ${currentPage} / ${pages}
            </div>
            <form method="get" action="${pageContext.request.contextPath}/order" class="d-flex align-items-center">
                <label for="size" class="me-2 small">Taille page</label>
                <select id="size" name="size" class="form-select form-select-sm" onchange="this.form.submit()">
                    <option value="5"  ${pageSize == 5  ? 'selected' : ''}>5</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                </select>
                <input type="hidden" name="page" value="${currentPage}"/>
            </form>
        </div>

        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Date</th>
                <th scope="col">Total TTC</th>
                <th scope="col">Statut</th>
                <c:if test="${sessionScope.role == 'ADMIN'
                        or sessionScope.role == 'BARMAN'
                        or sessionScope.role == 'SECRETARY'}">
                    <th>Actions</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orders}" varStatus="status">
                <tr>
                    <td>${(currentPage - 1) * pageSize + status.index + 1}</td>
                    <td><fmt:formatDate value="${o.dateOrder}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td><fmt:formatNumber value="${o.totalPrice}" type="currency"/></td>
                    <td>${o.status}</td>

                    <c:if test="${sessionScope.role == 'ADMIN'
                            or sessionScope.role == 'BARMAN'
                            or sessionScope.role == 'SECRETARY'}">
                        <td>
                            <div class="btn-group" role="group" aria-label="Actions order">
                                <!-- Éditer -->
                                <a href="${pageContext.request.contextPath}/order?editForm=${o.id}"
                                   class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-pencil-square"></i> Modifier
                                </a>

                                <!-- Appliquer une remise -->
                                <form method="post" action="${pageContext.request.contextPath}/order" style="display:inline;">
                                    <input type="hidden" name="action" value="applyDiscount"/>
                                    <input type="hidden" name="orderId" value="${o.id}"/>
                                    <input type="number" name="discountId" class="form-control form-control-sm d-inline-block"
                                           placeholder="ID remise" style="width:110px" required />
                                    <button type="submit" class="btn btn-outline-secondary btn-sm">
                                        % Remise
                                    </button>
                                </form>

                                <!-- Payer via abonnement -->
                                <form method="post" action="${pageContext.request.contextPath}/order" style="display:inline;">
                                    <input type="hidden" name="action" value="paySubscription"/>
                                    <input type="hidden" name="orderId" value="${o.id}"/>
                                    <button type="submit" class="btn btn-outline-success btn-sm">
                                        Abo
                                    </button>
                                </form>

                                <!-- Payer en ligne (mock) -->
                                <form method="post" action="${pageContext.request.contextPath}/order" style="display:inline;">
                                    <input type="hidden" name="action" value="payOnline"/>
                                    <input type="hidden" name="orderId" value="${o.id}"/>
                                    <button type="submit" class="btn btn-outline-success btn-sm">
                                        CB
                                    </button>
                                </form>

                                <!-- Annuler (payée ou non) -->
                                <form method="post" action="${pageContext.request.contextPath}/order"
                                      onsubmit="return confirm('Annuler cette commande ?')" style="display:inline;">
                                    <input type="hidden" name="action" value="cancel"/>
                                    <input type="hidden" name="orderId" value="${o.id}"/>
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        Annuler
                                    </button>
                                </form>

                                <!-- Supprimer si non payée -->
                                <form method="post" action="${pageContext.request.contextPath}/order"
                                      onsubmit="return confirm('Supprimer la commande non payée ?')" style="display:inline;">
                                    <input type="hidden" name="action" value="cancelUnpaid"/>
                                    <input type="hidden" name="orderId" value="${o.id}"/>
                                    <button type="submit" class="btn btn-outline-warning btn-sm">
                                        Suppr. non payée
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

    <!-- Pagination -->
    <c:if test="${pages > 1}">
        <nav aria-label="Pagination">
            <ul class="pagination justify-content-center">
                <c:url var="firstUrl" value="/order">
                    <c:param name="page" value="1"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="prevUrl" value="/order">
                    <c:param name="page" value="${currentPage - 1}"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="nextUrl" value="/order">
                    <c:param name="page" value="${currentPage + 1}"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="lastUrl" value="/order">
                    <c:param name="page" value="${pages}"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>

                <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>'">
                    <a class="page-link" href="${firstUrl}">&laquo; Première</a>
                </li>
                <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>'">
                    <a class="page-link" href="${prevUrl}">Précédent</a>
                </li>
                <li class="page-item disabled"><span class="page-link">Page ${currentPage} / ${pages}</span></li>
                <li class="page-item <c:if test='${currentPage >= pages}'>disabled</c:if>'">
                    <a class="page-link" href="${nextUrl}">Suivant</a>
                </li>
                <li class="page-item <c:if test='${currentPage >= pages}'>disabled</c:if>'">
                    <a class="page-link" href="${lastUrl}">Dernière &raquo;</a>
                </li>
            </ul>
        </nav>
    </c:if>
</section>
