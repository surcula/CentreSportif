<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<section class="page-section">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="page-section-heading text-uppercase text-secondary mb-0">
            Commande #${order.id}
        </h2>
        <a href="${ctx}/order" class="btn btn-outline-secondary btn-sm">⟵ Retour à la liste</a>
    </div>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <ul class="mb-0">
                <c:forEach var="e" items="${errors}">
                    <li><b>${e.key}</b> : ${e.value}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <!-- Infos commande -->
    <div class="card mb-4">
        <div class="card-body">
            <div class="row g-3">
                <div class="col-md-3">
                    <div class="text-muted small">Statut</div>
                    <div class="h6 mb-0">
                        <c:choose>
                            <c:when test="${order.status == 'CONFIRMED'}"><span class="badge bg-success">CONFIRMED</span></c:when>
                            <c:when test="${order.status == 'ONHOLD'}"><span class="badge bg-warning text-dark">ONHOLD</span></c:when>
                            <c:when test="${order.status == 'CANCELLED'}"><span class="badge bg-secondary">CANCELLED</span></c:when>
                            <c:otherwise>${order.status}</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="text-muted small">Date</div>
                    <div class="h6 mb-0">
                        <c:choose>
                            <c:when test="${not empty order.dateOrderAsDate}">
                                <fmt:formatDate value="${order.dateOrderAsDate}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${order.dateOrder}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="text-muted small">Client</div>
                    <div class="h6 mb-0">
                        <c:choose>
                            <c:when test="${not empty order.user}">
                                ${order.user.firstName} ${order.user.lastName}
                            </c:when>
                            <c:otherwise>—</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="text-muted small">Total TTC</div>
                    <div class="h5 mb-0">
                        <fmt:formatNumber value="${order.totalPrice}" type="number" minFractionDigits="2" maxFractionDigits="2"/> €
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Abonnements liés -->
    <div class="card mb-4">
        <div class="card-header fw-bold">Abonnements</div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty order.ordersSubscriptions}">
                    <table class="table mb-0">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Abonnement</th>
                            <th>Sport</th>
                            <th>Actif</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="os" items="${order.ordersSubscriptions}" varStatus="st">
                            <tr>
                                <td>${st.index + 1}</td>
                                <td>${os.subscription.subscriptionName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty os.subscription.sport}">
                                            ${os.subscription.sport.sportName}
                                        </c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                  <span class="badge ${os.active ? 'bg-success' : 'bg-secondary'}">
                          ${os.active ? 'Oui' : 'Non'}
                  </span>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="p-3 text-muted">Aucun abonnement lié à cette commande.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Remises appliquées -->
    <div class="card mb-4">
        <div class="card-header fw-bold d-flex justify-content-between align-items-center">
            <span>Remises</span>
            <form method="post" action="${ctx}/order" class="d-flex gap-2">
                <input type="hidden" name="action" value="applyDiscount"/>
                <input type="hidden" name="orderId" value="${order.id}"/>
                <input type="number" name="discountId" class="form-control form-control-sm" placeholder="ID remise" required/>
                <button class="btn btn-outline-secondary btn-sm" type="submit">% Appliquer</button>
            </form>
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty order.ordersDiscounts}">
                    <table class="table mb-0">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Nom</th>
                            <th>Pourcentage</th>
                            <th>Actif</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="od" items="${order.ordersDiscounts}" varStatus="st">
                            <tr>
                                <td>${st.index + 1}</td>
                                <td>${od.discount.discountName}</td>
                                <td><fmt:formatNumber value="${od.discount.percent}" minFractionDigits="0" maxFractionDigits="2"/> %</td>
                                <td>
                  <span class="badge ${od.active ? 'bg-success' : 'bg-secondary'}">
                          ${od.active ? 'Oui' : 'Non'}
                  </span>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="p-3 text-muted">Aucune remise pour l’instant.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Actions paiement -->
    <div class="d-flex flex-wrap gap-2">
        <!-- Payer par abonnement -->
        <form method="post" action="${ctx}/order">
            <input type="hidden" name="action" value="paySubscription"/>
            <input type="hidden" name="orderId" value="${order.id}"/>
            <button type="submit" class="btn btn-outline-success">Payer via abonnement</button>
        </form>

        <!-- Payer en ligne + promo -->
        <form method="post" action="${ctx}/order" class="d-flex flex-wrap gap-2 align-items-center">
            <input type="hidden" name="action" value="payOnline"/>
            <input type="hidden" name="orderId" value="${order.id}"/>
            <input type="text" name="promoCode" class="form-control form-control-sm" placeholder="Code promo (opt.)" style="max-width:160px"/>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="clubEligible" name="clubEligible">
                <label class="form-check-label small" for="clubEligible">Club -10%</label>
            </div>
            <button type="submit" class="btn btn-success">Payer en ligne</button>
        </form>

        <!-- Annuler -->
        <form method="post" action="${ctx}/order" onsubmit="return confirm('Annuler cette commande ?');">
            <input type="hidden" name="action" value="cancel"/>
            <input type="hidden" name="orderId" value="${order.id}"/>
            <button type="submit" class="btn btn-outline-danger">Annuler</button>
        </form>

        <!-- Supprimer si non payée -->
        <form method="post" action="${ctx}/order" onsubmit="return confirm('Supprimer la commande non payée ?');">
            <input type="hidden" name="action" value="cancelUnpaid"/>
            <input type="hidden" name="orderId" value="${order.id}"/>
            <button type="submit" class="btn btn-outline-warning">Supprimer non payée</button>
        </form>
    </div>

</section>
