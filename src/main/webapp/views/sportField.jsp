<%--
  Created by IntelliJ IDEA.
  User: Sarah
  Date: 10/13/2025
  Time: 7:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:choose>
    <c:when test="${sessionScope.role == 'ADMIN'
             or sessionScope.role == 'BARMAN'
             or sessionScope.role == 'SECRETARY'}">
        <section class="page-section">
            <h2 class="text-center">
               Les attributions sports - terrains
            </h2>

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
                    <th scope="col">Nom du terrain</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="field" items="${fields0}" varStatus="status">
                    <tr>
                        <td>${(pages - 1) * pageSize + status.index + 1}</td>
                        <td>${field.fieldName}</td>


                            <td>
                                <div class="btn-group" role="group" aria-label="Actions sport">
                                    <!-- Bouton Modifier -->
                                    <a href="${pageContext.request.contextPath}/sports-fields?editForm=${field.id}"
                                       class="btn btn-outline-primary btn-sm">
                                        <i class="bi bi-pencil-square"></i> Modifier
                                    </a>
                                    <!-- Bouton Supprimer ou Activer -->
                                    <c:choose>
                                        <c:when test="${sportField.active}">
                                            <!-- Formulaire Supprimer -->
                                            <form method="post" action="${pageContext.request.contextPath}/sports-fields"
                                                  onsubmit="return confirm('Supprimer cet attribution ?')" style="display: inline;">
                                                <input type="hidden" name="sportId" value="${field.id}"/>
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
                    </tr>
                </tbody>
            </table>
        </section>
    </c:when>
</c:choose>
