<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/23/2025
  Time: 2:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentPage" value="${empty page ? 1 : page}"/>
<c:set var="pageSize" value="${empty size ? 10 : size}"/>
<c:set var="pages" value="${empty totalPages ? 1 : totalPages}"/>
<c:set var="total" value="${empty totalElements ? fn:length(fields) : totalElements}"/>


<section class="page-section">
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Les terrains</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
        <div class="text-center mt-3">
            <a class="btn btn-success"
               href="${pageContext.request.contextPath}/field">Retry</a>
        </div>
    </c:if>
    <c:if test="${empty error}">
        <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
            <a class="btn btn-primary my-3" href="${pageContext.request.contextPath}/field?form=true">Ajouter un nouveau
                terrain</a>
        </c:if>

        <!-- Nb résultat + choix affichage -->
        <div class="d-flex justify-content-end align-items-center mb-2">
            <div class="me-3 text-muted small">
                    ${totalElements} résultat<c:if test="${totalElements > 1}">s</c:if>
                — Page ${page} / ${totalPages}
            </div>
            <form method="get" action="${pageContext.request.contextPath}/field" class="d-flex align-items-center">
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
                <th scope="col">Hall</th>
                <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
                    <th scope="col">Actif</th>
                    <th> Détails</th>
                </c:if>
            </tr>
            </thead>
            <tbody>

            <c:if test="${empty fields}">
                <tr> <td>Aucunes données trouvées.</td> </tr>
            </c:if>

            <c:forEach var="field" items="${fields}" varStatus="status">
                <tr>
                    <td>${(page - 1) * size + status.index + 1}</td>
                    <td>${field.fieldName}</td>
                    <td>${field.hall.hallName}</td>
                    <c:if test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
                        <td>${field.active ? 'Actif' : 'Non actif'}</td>
                        <td>
                            <div class="btn-group" role="group" aria-label="Actions field">
                                <!-- Bouton Modifier -->
                                <a href="${pageContext.request.contextPath}/field?editForm=${field.id}"
                                   class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-pencil-square"></i> Modifier
                                </a>

                                <!-- Bouton Supprimer ou Activer -->
                                <c:choose>
                                    <c:when test="${field.active}">
                                        <!-- Formulaire Supprimer -->
                                        <form method="post" action="${pageContext.request.contextPath}/field"
                                              onsubmit="return confirm('Supprimer ce terrain ?')"
                                              style="display: inline;">
                                            <input type="hidden" name="fieldId" value="${field.id}"/>
                                            <input type="hidden" name="action" value="delete"/>
                                            <button type="submit" class="btn btn-outline-danger btn-sm">
                                                <i class="bi bi-trash"></i> Supprimer
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Formulaire Activer -->
                                        <form method="post" action="${pageContext.request.contextPath}/field"
                                              onsubmit="return confirm('Activer ce terrain ?')"
                                              style="display: inline;">
                                            <input type="hidden" name="fieldId" value="${field.id}"/>
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
                <c:url var="firstUrl" value="/field">
                    <c:param name="page" value="1"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="prevUrl" value="/field">
                    <c:param name="page" value="${currentPage - 1}"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="nextUrl" value="/field">
                    <c:param name="page" value="${currentPage + 1}"/>
                    <c:param name="size" value="${pageSize}"/>
                </c:url>
                <c:url var="lastUrl" value="/field">
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
