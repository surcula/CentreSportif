<%--
  Created by IntelliJ IDEA.
  User: Sarah
  Date: 10/13/2025
  Time: 7:03 PM
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
    <h2 class="text-center">
        Les attributions sports - terrains
    </h2>

    <!-- Nb résultat + choix affichage -->
    <div class="d-flex justify-content-end align-items-center mb-2">
        <div class="me-3 text-muted small">
            ${total} résultat<c:if test="${total > 1}">s</c:if>
            — Page ${page} / ${pages}
        </div>
        <form method="get" action="${pageContext.request.contextPath}/sports-fields"
              class="d-flex align-items-center">
            <label for="size" class="me-2 small">Taille page</label>
            <select id="size" name="size" class="form-select form-select-sm" onchange="this.form.submit()">
                <option value="5"  ${pageSize == 5  ? 'selected' : ''}>5</option>
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
            <th scope="col">Nom du terrain</th>
            <th scope="col">Nom du sport</th>
            <th scope="col">Jour</th>
            <th scope="col">Prix</th>
            <th scope="col">Actif</th>
            <th scope="col">Action</th>
        </tr>
        </thead>

        <tbody>
        <c:if test="${empty sportsfields}">
            <tr> <td>Aucunes données trouvées.</td> </tr>
        </c:if>

        <c:forEach var="sportfield" items="${sportsfields}" varStatus="status">
            <tr>
                <td>${(page - 1) * pageSize + status.index + 1}</td>
                <td>${sportfield.field.fieldName}</td>
                <td>${sportfield.sport.sportName}</td>
                <td>${sportfield.dayLabel}</td>
                <td>
                    <fmt:formatNumber value="${sportfield.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                </td>
                <td>${sportfield.active ? 'Actif' : 'Non actif'}</td>
                <td>
                    <div class="btn-group" role="group" aria-label="Actions sport">
                        <!-- Modifier -->
                        <a href="${pageContext.request.contextPath}/sports-fields?editForm=${sportfield.id}"
                           class="btn btn-outline-primary btn-sm">
                            <i class="bi bi-pencil-square"></i> Modifier
                        </a>

                        <!-- Supprimer / Activer -->
                        <c:choose>
                            <c:when test="${sportfield.active}">
                                <form method="post" action="${pageContext.request.contextPath}/sports-fields"
                                      onsubmit="return confirm('Supprimer cette attribution ?')" style="display:inline;">
                                    <input type="hidden" name="sportId" value="${sportfield.id}"/>
                                    <input type="hidden" name="action" value="delete"/>
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-trash"></i> Supprimer
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/sports-fields"
                                      onsubmit="return confirm('Activer cette attribution ?')" style="display:inline;">
                                    <input type="hidden" name="sportId" value="${sportField.id}"/>
                                    <input type="hidden" name="action" value="activer"/>
                                    <button type="submit" class="btn btn-outline-warning btn-sm">
                                        <i class="bi bi-check2-circle"></i> Activer
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
</section>
