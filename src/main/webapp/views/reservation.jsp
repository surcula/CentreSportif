<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 13-09-25
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentPage" value="${empty page ? 1 : page}"/>
<c:set var="pageSize" value="${empty size ? 10 : size}"/>
<c:set var="pages" value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total" value="${empty totalElements ? fn:length(reservations) : totalElements}"/>
<!-- formulaire version David -->
<section class="page-section">
    <!--<h1>Debug JSP: ${reservations != null ? fn:length(reservations) : 'null'}</h1>-->
    <!--<p>Reservations list: ${not empty reservations ? reservations.size() : "vide ou null"}</p>-->
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Les réservations</h2>
    <c:if test="${not empty error}">
    <div class="alert alert-danger text-center">${error}</div>
    <div class="text-center mt-3">
        <a class="btn btn-success"
           href="${pageContext.request.contextPath}/reservation">Retry</a>
    </div>
    </c:if>
    <a class="btn btn-primary my-3" href="${pageContext.request.contextPath}/reservation?form=true">Ajouter une nouvelle
        réservation</a>
    <div class="d-flex justify-content-end align-items-center mb-2">
        <div class="me-3 text-muted small">
            ${totalElements} résultat<c:if test="${totalElements >1}">s</c:if>
            — Page ${page} / ${totalPages}
        </div>
        <form method="get" action="${pageContext.request.contextPath}/reservation" class="d-flex align-items-center">
            <label for="size" class="me-2 small">Taille page</label>
            <select id="size" name="size" class="form-select form-select-sm"
                    onchange="this.form.submit()">
                <option value="5" ${size == 5 ? 'selected' : ''}>5</option>
                <option value="10" ${size == 10 ? 'selected' : ''}>10</option>
                <option value="20" ${size == 20 ? 'selected' : ''}>20</option>
            </select>
            <input type="hidden" name="page" value="${page}"/>
        </form>
    </div>
    <table class="table">
        <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Nom</th>
                <th scope="col">Date de début</th>
                <th scope="col">Date de fin</th>
                <th scope="col">Prix</th>
                <th scope="col">TVA</th>
                <th scope="col">Statut</th>
                <th scope="col">Identifiant client</th>
                <th scope="col">Identifiant terrain</th>
                <th scope="col">Etat</th>
                <th> Détails</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var = "reservation" items ="${reservations}" varStatus = "status">
        <tr>
            <td>${(page - 1) * size + status.index + 1}</td>
            <td>${reservation.reservationName}</td>
            <td>${reservation.startDateReservation}</td>
            <td>${reservation.endDateReservation}</td>
            <td>${reservation.price}</td>
            <td>${reservation.tva}</td>
            <td>${reservation.statut}</td>
            <td>Voir table User</td>
            <td>Voir table SportField</td>
            <td>
                <c:choose>
                    <c:when test = "${reservation.active}">
                        <span class = "badge bg-success">En cours</span>
                    </c:when>
                    <c:otherwise>
                        <span class = "badge bg-secondary">Terminée</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <div class="btn-group" role="group" aria-label="Actions Reservation">
                    <!-- Bouton Modifier -->
                    <a href="${pageContext.request.contextPath}/reservation?editForm=${reservation.id}"
                       class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-pencil-square"></i> Modifier
                    </a>

                    <!-- Choix entre le bouton activer ou supprimer en fonction du statut -->
                    <c:choose>
                        <c:when test = "${reservation.active}">
                            <!-- Formulaire Supprimer -->
                            <form method="post" action="${pageContext.request.contextPath}/reservation"
                                  onsubmit="return confirm('Supprimer cette réservation ?')" style="display: inline;">
                                <input type="hidden" name="reservationId" value="${reservation.id}"/>
                                <input type="hidden" name="action" value="delete"/>
                                <button type="submit" class="btn btn-outline-danger btn-sm">
                                    <i class="bi bi-trash"></i> Supprimer
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <!-- Formulaire Activer -->
                            <form method="post" action="${pageContext.request.contextPath}/reservation"
                                  onsubmit="return confirm('Activer cette réservation ?')" style="display: inline;">
                                <input type="hidden" name="reservationId" value="${reservation.id}"/>
                                <input type="hidden" name="action" value="activer"/>
                                <button type="submit" class="btn btn-outline-warning btn-sm">
                                    <i class="bi bi-check-circle"></i> Activer
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${pages>1}">
    <nav aria-label="Pagination">
        <ul class="pagination justify-content-center">

            <!-- URLs avec conservation du size -->
            <c:url var="firstUrl" value="/reservation">
                <c:param name="page" value="1"/>
                <c:param name="size" value="${pageSize}"/>
            </c:url>
            <c:url var="prevUrl" value="/reservation">
                <c:param name="page" value="${currentPage - 1}"/>
                <c:param name="size" value="${pageSize}"/>
            </c:url>
            <c:url var="nextUrl" value="/reservation">
                <c:param name="page" value="${currentPage + 1}"/>
                <c:param name="size" value="${pageSize}"/>
            </c:url>
            <c:url var="lastUrl" value="/reservation">
                <c:param name="page" value="${pages}"/>
                <c:param name="size" value="${pageSize}"/>
            </c:url>

            <!-- First -->
            <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>">
                <a class="page-link" href="${firstUrl}">&laquo; Première</a>
            </li>

            <!-- Prev -->
            <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>">
                <a class="page-link" href="${prevUrl}">Précédent</a>
            </li>

            <!-- Indicateur simple (tu peux ajouter des numéros si tu veux) -->
            <li class="page-item disabled">
                <span class="page-link">Page ${currentPage} / ${pages}</span>
            </li>

            <!-- Next -->
            <li class="page-item <c:if test='${currentPage >= pages}'>disabled</c:if>">
                <a class="page-link" href="${nextUrl}">Suivant</a>
            </li>

            <!-- Last -->
            <li class="page-item <c:if test='${currentPage >= pages}'>disabled</c:if>">
                <a class="page-link" href="${lastUrl}">Dernière &raquo;</a>
            </li>
        </ul>
    </nav>
    </c:if>
</section>




