<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 10/2/2025
  Time: 3:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentPage" value="${empty page ? 1 : page}"/>
<c:set var="pageSize" value="${empty size ? 10 : size}"/>
<c:set var="pages" value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total" value="${empty totalElements ? fn:length(sports) : totalElements}"/>


<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Les sports</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
        <div class="text-center mt-3">
            <a class="btn btn-success"
               href="${pageContext.request.contextPath}/sport">Retry</a>
        </div>

    </c:if>
    <c:if test="${empty error}">
        <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
            <a class="btn btn-primary my-3" href="${pageContext.request.contextPath}/sport?form=true">Ajouter un nouveau
                sport</a>
        </c:if>

        <!-- Nb résultat + choix affichage -->
        <div class="d-flex justify-content-end align-items-center mb-2">
            <div class="me-3 text-muted small">
                    ${total} résultat<c:if test="${total > 1}">s</c:if>
                — Page ${pages} / ${total}
            </div>
            <form method="get" action="${pageContext.request.contextPath}/sport" class="d-flex align-items-center">
                <label for="size" class="me-2 small">Taille page</label>
                <select id="size" name="size" class="form-select form-select-sm"
                        onchange="this.form.submit()">
                    <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                    <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                </select>
                <input type="hidden" name="page" value="${page}"/>
            </form>
        </div>


        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Nom du sport</th>
                <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
                    <th scope="col">Actif</th>
                    <th> Détails</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="sport" items="${sports}" varStatus="status">
                <tr>
                    <td>${(pages - 1) * pageSize + status.index + 1}</td>
                    <td>${sport.sportName}</td>
                    <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
                        <td>${sport.active}</td>
                        <td>
                            <div class="btn-group" role="group" aria-label="Actions sport">
                                <!-- Bouton Modifier -->
                                <a href="${pageContext.request.contextPath}/sport?editForm=${sport.id}"
                                   class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-pencil-square"></i> Modifier
                                </a>

                                <!-- Bouton Supprimer ou Activer -->
                                <c:choose>
                                    <c:when test="${sport.active}">
                                        <!-- Formulaire Supprimer -->
                                        <form method="post" action="${pageContext.request.contextPath}/sport"
                                              onsubmit="return confirm('Supprimer ce sport ?')" style="display: inline;">
                                            <input type="hidden" name="sportId" value="${sport.id}"/>
                                            <input type="hidden" name="action" value="delete"/>
                                            <button type="submit" class="btn btn-outline-danger btn-sm">
                                                <i class="bi bi-trash"></i> Supprimer
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Formulaire Activer -->
                                        <form method="post" action="${pageContext.request.contextPath}/sport"
                                              onsubmit="return confirm('Activer ce sport ?')" style="display: inline;">
                                            <input type="hidden" name="sportId" value="${sport.id}"/>
                                            <input type="hidden" name="action" value="activer"/>
                                            <button type="submit" class="btn btn-outline-warning btn-sm">
                                                <i class="bi bi-trash"></i> Activer
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <!-- pagination -->
    <c:if test="${pages > 1}">
        <nav aria-label="Pagination">
            <ul class="pagination justify-content-center">

                <!-- URLs avec conservation du size -->
                <c:url var="firstUrl" value="/sport">
                    <c:param name="page" value="1"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="prevUrl" value="/sport">
                    <c:param name="page" value="${currentPage - 1}"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="nextUrl" value="/sport">
                    <c:param name="page" value="${currentPage + 1}"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="lastUrl" value="/sport">
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

