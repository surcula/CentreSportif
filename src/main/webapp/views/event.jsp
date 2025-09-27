<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 13-09-25
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentPage" value="${empty page ? 1 : page}"/>
<c:set var="pageSize" value="${empty size ? 10 : size}"/>
<c:set var="pages" value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total" value="${empty totalElements ? fn:length(halls) : totalElements}"/>

<!-- formulaire version David -->
<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Les évènements</h2>
    <!--<div class="alert alert-danger text-center">${error}</div>
    <div class="text-center mt-3">
        <a class="btn btn-success"
           href="">Retry</a>
    </div>-->
    <a class="btn btn-primary my-3" href="${pageContext.request.contextPath}/event?form=true">Ajouter un nouvel
        évènement</a>
    <div class="d-flex justify-content-end align-items-center mb-2">
        <div class="me-3 text-muted small">
            ${totalElements} résultat
            — Page ${page} / ${totalPages}
        </div>
        <form method="get" action="" class="d-flex align-items-center">
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
            <th scope="col">Date de début, heure</th>
            <th scope="col">Date de fin, heure</th>
            <th scope="col">Description</th>
            <th scope="col">Image</th>
            <th scope="col">Statut</th><!--en cours, terminé-->
            <th> Détails</th>
        </tr>
        </thead>
        <tbody>
            <tr>
                <td>${(page - 1) * size + status.index + 1}</td>
                <td>${event.eventName}</td>
                <td>${event.startDateHour}</td>
                <td>${event.endDateHour}</td>
                <td>${event.description}</td>
                <td>${event.image}</td>
                <td>${hall.active}</td>
                <td>
                    <div class="btn-group" role="group" aria-label="Actions Event">
                        <!-- Bouton Modifier ne fonctionne pas encore car il faut ajouter un évènement-->
                        <a href="${pageContext.request.contextPath}/event?editForm=${421}"
                           class="btn btn-outline-primary btn-sm">
                            <i class="bi bi-pencil-square"></i> Modifier
                        </a>

                        <!-- Bouton Supprimer ou Activer -->

                                <!-- Formulaire Supprimer -->
                                <form method="post" action=""
                                      onsubmit="return confirm('Supprimer cet évènement ?')" style="display: inline;">
                                    <input type="hidden" name="eventId" value="${event.id}"/>
                                    <input type="hidden" name="action" value="delete"/>
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-trash"></i> Supprimer
                                    </button>
                                </form>
                                <!-- Formulaire Activer -->
                                <form method="post" action="${pageContext.request.contextPath}/event"
                                      onsubmit="return confirm('Activer cet évènement ?')" style="display: inline;">
                                    <input type="hidden" name="eventId" value="${event.id}"/>
                                    <input type="hidden" name="action" value="activer"/>
                                    <button type="submit" class="btn btn-outline-warning btn-sm">
                                        <i class="bi bi-trash"></i> Activer
                                    </button>
                                </form>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
    <nav aria-label="Pagination">
        <ul class="pagination justify-content-center">
            <!-- URLs avec conservation du size -->
            <c:url var="firstUrl" value="/hall">
                <c:param name="page" value="1"/>
                <c:param name="size" value="${pageSize}"/>
            </c:url>
            <c:url var="prevUrl" value="/hall">
                <c:param name="page" value="${currentPage - 1}"/>
                <c:param name="size" value="${pageSize}"/>
            </c:url>
            <c:url var="nextUrl" value="/hall">
                <c:param name="page" value="${currentPage + 1}"/>
                <c:param name="size" value="${pageSize}"/>
            </c:url>
            <c:url var="lastUrl" value="/hall">
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
</section>



