<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:set var="currentPage" value="${empty page ? 1 : page}"/>
<c:set var="pageSize"    value="${empty size ? 10 : size}"/>
<c:set var="pages"       value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total"       value="${empty totalElements ? fn:length(orders) : totalElements}"/>

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Commandes</h2>

    <!-- Panier en cours -->
    <c:if test="${not empty sessionScope.draftOrderId}">
        <div class="alert alert-success d-flex justify-content-between align-items-center my-3">
            <span>🧺 Panier en cours — tu peux finaliser quand tu veux.</span>
            <a class="btn btn-sm btn-success" href="${ctx}/order?editForm=${sessionScope.draftOrderId}">Voir / payer la commande</a>
        </div>
    </c:if>

    <!-- Erreur globale -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
        <div class="text-center mt-3"><a class="btn btn-success" href="${ctx}/order">Réessayer</a></div>
    </c:if>

    <c:if test="${empty error}">
        <!-- Bouton créer (staff) -->
        <c:if test="${sessionScope.role == 'ADMIN' or sessionScope.role == 'BARMAN' or sessionScope.role == 'SECRETARY'}">
            <a class="btn btn-primary my-3" href="${ctx}/order?form=true">Créer une commande</a>
        </c:if>

        <!-- Header liste -->
        <div class="d-flex justify-content-end align-items-center mb-2">
            <div class="me-3 text-muted small">
                    ${total} résultat<c:if test="${total > 1}">s</c:if> — Page ${currentPage} / ${pages}
            </div>
            <form method="get" action="${ctx}/order" class="d-flex align-items-center">
                <label for="size" class="me-2 small">Taille page</label>
                <select id="size" name="size" class="form-select form-select-sm" onchange="this.form.submit()">
                    <option value="5"  ${pageSize == 5  ? 'selected' : ''}>5</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                </select>
                <input type="hidden" name="page" value="${currentPage}"/>
            </form>
        </div>

        <!-- Liste / état vide -->
        <c:choose>
            <c:when test="${empty orders}">
                <div class="alert alert-info text-center">Aucune commande pour l’instant.</div>
            </c:when>
            <c:otherwise>
                <table class="table align-middle">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Date</th>
                        <th scope="col">Total TTC</th>
                        <th scope="col">Statut</th>
                        <th scope="col" class="text-end">Ouvrir</th>
                        <c:if test="${sessionScope.role == 'ADMIN' or sessionScope.role == 'BARMAN' or sessionScope.role == 'SECRETARY'}">
                            <th scope="col">Actions (staff)</th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="o" items="${orders}" varStatus="st">
                        <tr>
                            <td>${(currentPage - 1) * pageSize + st.index + 1}</td>

                            <!-- Date: utiliser helper Date si Order.dateOrder est Instant -->
                            <td>
                                <c:choose>
                                    <c:when test="${not empty o.dateOrderAsDate}">
                                        <fmt:formatDate value="${o.dateOrderAsDate}" pattern="yyyy-MM-dd HH:mm"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatDate value="${o.dateOrder}" pattern="yyyy-MM-dd HH:mm"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td><fmt:formatNumber value="${o.totalPrice}" type="number" minFractionDigits="2" maxFractionDigits="2"/> €</td>

                            <!-- Badge statut -->
                            <td>
                                <c:choose>
                                    <c:when test="${o.status == 'CONFIRMED'}"><span class="badge bg-success">CONFIRMED</span></c:when>
                                    <c:when test="${o.status == 'ONHOLD'}"><span class="badge bg-warning text-dark">ONHOLD</span></c:when>
                                    <c:when test="${o.status == 'CANCELLED'}"><span class="badge bg-secondary">CANCELLED</span></c:when>
                                    <c:otherwise>${o.status}</c:otherwise>
                                </c:choose>
                            </td>

                            <!-- Ouvrir / payer -->
                            <td class="text-end">
                                <a class="btn btn-sm btn-outline-primary" href="${ctx}/order?editForm=${o.id}">Ouvrir / Payer</a>
                            </td>

                            <!-- Actions staff -->
                            <c:if test="${sessionScope.role == 'ADMIN' or sessionScope.role == 'BARMAN' or sessionScope.role == 'SECRETARY'}">
                                <td>
                                    <div class="d-flex flex-wrap gap-2">
                                        <!-- Remise -->
                                        <form method="post" action="${ctx}/order" class="d-inline">
                                            <input type="hidden" name="action" value="applyDiscount"/>
                                            <input type="hidden" name="orderId" value="${o.id}"/>
                                            <input type="number" name="discountId" class="form-control form-control-sm d-inline-block"
                                                   placeholder="ID remise" style="width:110px" required />
                                            <button type="submit" class="btn btn-outline-secondary btn-sm">% Remise</button>
                                        </form>

                                        <!-- Payer via abo -->
                                        <form method="post" action="${ctx}/order" class="d-inline">
                                            <input type="hidden" name="action" value="paySubscription"/>
                                            <input type="hidden" name="orderId" value="${o.id}"/>
                                            <button type="submit" class="btn btn-outline-success btn-sm">Abo</button>
                                        </form>

                                        <!-- Payer CB -->
                                        <form method="post" action="${ctx}/order" class="d-inline">
                                            <input type="hidden" name="action" value="payOnline"/>
                                            <input type="hidden" name="orderId" value="${o.id}"/>
                                            <button type="submit" class="btn btn-outline-success btn-sm">CB</button>
                                        </form>

                                        <!-- Annuler -->
                                        <form method="post" action="${ctx}/order" onsubmit="return confirm('Annuler cette commande ?')" class="d-inline">
                                            <input type="hidden" name="action" value="cancel"/>
                                            <input type="hidden" name="orderId" value="${o.id}"/>
                                            <button type="submit" class="btn btn-outline-danger btn-sm">Annuler</button>
                                        </form>

                                        <!-- Supprimer non payée -->
                                        <form method="post" action="${ctx}/order" onsubmit="return confirm('Supprimer la commande non payée ?')" class="d-inline">
                                            <input type="hidden" name="action" value="cancelUnpaid"/>
                                            <input type="hidden" name="orderId" value="${o.id}"/>
                                            <button type="submit" class="btn btn-outline-warning btn-sm">Suppr. non payée</button>
                                        </form>
                                    </div>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>

    <!-- Pagination -->
    <c:if test="${pages > 1}">
        <nav aria-label="Pagination">
            <ul class="pagination justify-content-center">
                <c:url var="firstUrl" value="/order"><c:param name="page" value="1"/><c:param name="size" value="${pageSize}"/></c:url>
                <c:url var="prevUrl"  value="/order"><c:param name="page" value="${currentPage - 1}"/><c:param name="size" value="${pageSize}"/></c:url>
                <c:url var="nextUrl"  value="/order"><c:param name="page" value="${currentPage + 1}"/><c:param name="size" value="${pageSize}"/></c:url>
                <c:url var="lastUrl"  value="/order"><c:param name="page" value="${pages}"/><c:param name="size" value="${pageSize}"/></c:url>

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
