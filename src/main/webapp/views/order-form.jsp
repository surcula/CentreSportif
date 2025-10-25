<%--
  Created by IntelliJ IDEA.
  User: sophie
  Date: 25-10-25
  Time: 13:08
  To change this template use File | Settings | File Templates.
--%>
<%-- Order form (create/edit) — placeholder, même pattern que sport-form.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-4">
        ${mode == 'edit' ? 'Éditer la commande' : 'Créer une commande'}
    </h2>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <ul class="mb-0">
                <c:forEach var="e" items="${errors}">
                    <li><b>${e.key}</b> : ${e.value}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${mode == 'edit'}">
            <div class="card mb-3">
                <div class="card-body">
                    <p><b>ID :</b> ${order.id}</p>
                    <p><b>Date :</b> <fmt:formatDate value="${order.dateOrder}" pattern="yyyy-MM-dd HH:mm"/></p>
                    <p><b>Total TTC :</b> <fmt:formatNumber value="${order.totalPrice}" type="currency"/></p>
                    <p><b>Statut :</b> ${order.status}</p>
                </div>
            </div>

            <div class="d-flex flex-wrap gap-2">
                <!-- Appliquer une remise -->
                <form method="post" action="${pageContext.request.contextPath}/order" class="d-flex align-items-center gap-2">
                    <input type="hidden" name="action" value="applyDiscount"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <input type="number" name="discountId" class="form-control form-control-sm" placeholder="ID remise" required/>
                    <button type="submit" class="btn btn-outline-secondary btn-sm">% Remise</button>
                </form>

                <!-- Payer via abonnement -->
                <form method="post" action="${pageContext.request.contextPath}/order">
                    <input type="hidden" name="action" value="paySubscription"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn btn-outline-success btn-sm">Payer par abonnement</button>
                </form>

                <!-- Payer en ligne (mock) -->
                <form method="post" action="${pageContext.request.contextPath}/order">
                    <input type="hidden" name="action" value="payOnline"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn btn-outline-success btn-sm">Payer en ligne</button>
                </form>

                <!-- Annuler -->
                <form method="post" action="${pageContext.request.contextPath}/order" onsubmit="return confirm('Annuler cette commande ?')">
                    <input type="hidden" name="action" value="cancel"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn btn-outline-danger btn-sm">Annuler</button>
                </form>

                <!-- Supprimer si non payée -->
                <form method="post" action="${pageContext.request.contextPath}/order" onsubmit="return confirm('Supprimer la commande non payée ?')">
                    <input type="hidden" name="action" value="cancelUnpaid"/>
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn btn-outline-warning btn-sm">Supprimer non payée</button>
                </form>
            </div>
        </c:when>

        <c:otherwise>
            <!-- Création — à compléter selon ton flux de réservation -->
            <form method="post" action="${pageContext.request.contextPath}/order">
                <input type="hidden" name="action" value="create"/>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </form>
        </c:otherwise>
    </c:choose>
</section>
