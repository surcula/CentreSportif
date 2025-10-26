<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/views/template/templateHeader.jsp"/>

<style>
    .container-xxl { max-width: 1400px; }
    .table td, .table th { vertical-align: middle; }
    .btn-fixed { min-width: 130px; }
</style>

<section class="page-section">
    <div class="container-xxl mt-5 pt-4">
        <h2 class="text-center text-uppercase mb-4">Gestion des utilisateurs</h2>

        <form id="filterForm" method="get" class="mb-3 d-flex gap-2 align-items-center">
            <input class="form-control" name="q" placeholder="Recherche nom / email" value="${q}"/>
            <button class="btn btn-primary" type="submit">Rechercher</button>

            <select name="status" class="form-select" style="max-width:180px" onchange="submitFilter()">
                <option value="all"      ${status=='all'?'selected':''}>Tous</option>
                <option value="active"   ${status=='active'?'selected':''}>Actifs</option>
                <option value="inactive" ${status=='inactive'?'selected':''}>Inactifs</option>
            </select>

            <select name="size" class="form-select" style="max-width:120px" onchange="submitFilter()">
                <option value="5"  ${size==5?'selected':''}>5 / pag</option>
                <option value="10" ${size==10?'selected':''}>10 / pag</option>
                <option value="20" ${size==20?'selected':''}>20 / pag</option>
                <option value="50" ${size==50?'selected':''}>50 / pag</option>
            </select>

            <input type="hidden" name="page" id="pageField" value="${page}"/>
        </form>

        <script>
            function submitFilter(){
                document.getElementById('pageField').value = 1;
                document.getElementById('filterForm').submit();
            }
        </script>

        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Rôle</th>
                    <th>Actif</th>
                    <th>Blacklist</th>
                    <th style="width:460px">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="u">
                    <tr>
                        <td>${u.lastName}</td>
                        <td>${u.firstName}</td>
                        <td>${u.email}</td>
                        <td><c:out value="${u.role.roleName}"/></td>

                        <td>
                            <span class="badge ${u.active ? 'bg-success' : 'bg-secondary'}">${u.active ? 'Oui' : 'Non'}</span>
                        </td>
                        <td>
                            <span class="badge ${u.blacklist ? 'bg-danger' : 'bg-success'}">${u.blacklist ? 'Oui' : 'Non'}</span>
                        </td>

                        <td>
                            <div class="d-flex align-items-center flex-nowrap gap-2">
                                <c:if test="${canEdit}">
                                    <!-- Activer / Désactiver -->
                                    <form method="post" action="${pageContext.request.contextPath}${postUrl}" class="d-inline-block m-0">
                                        <input type="hidden" name="id" value="${u.id}"/>
                                        <button name="action" value="toggleActive" class="btn btn-sm ${u.active?'btn-warning':'btn-success'} btn-fixed" type="submit">
                                                ${u.active?'Désactiver':'Activer'}
                                        </button>
                                    </form>

                                    <!-- Basculer Blacklist -->
                                    <form method="post" action="${pageContext.request.contextPath}${postUrl}" class="d-inline-block m-0">
                                        <input type="hidden" name="id" value="${u.id}"/>
                                        <button name="action" value="recomputeBlacklist" class="btn btn-sm btn-outline-danger btn-fixed" type="submit">
                                            Basculer blacklist
                                        </button>
                                    </form>
                                </c:if>

                                <c:if test="${isAdmin}">
                                    <!-- Changer le rôle -->
                                    <form method="post" action="${pageContext.request.contextPath}${postUrl}" class="d-inline-flex align-items-center gap-2 m-0">
                                        <input type="hidden" name="id" value="${u.id}"/>
                                        <select name="roleName" class="form-select form-select-sm" style="width:auto">
                                            <option value="ADMIN"     ${u.role.roleName=='ADMIN'?'selected':''}>ADMIN</option>
                                            <option value="BARMAN"    ${u.role.roleName=='BARMAN'?'selected':''}>BARMAN</option>
                                            <option value="SECRETARY" ${u.role.roleName=='SECRETARY'?'selected':''}>SECRÉTAIRE</option>
                                            <option value="USER"      ${u.role.roleName=='USER'?'selected':''}>UTILISATEUR</option>
                                        </select>
                                        <button name="action" value="setRole" class="btn btn-sm btn-primary btn-fixed" type="submit">Assigner</button>
                                    </form>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <nav class="d-flex justify-content-between align-items-center mt-3">
            <div>Page ${page} / ${totalPages}</div>
            <ul class="pagination mb-0">
                <li class="page-item ${page<=1?'disabled':''}">
                    <a class="page-link" href="?q=${q}&status=${status}&size=${size}&page=${page-1}">Précédent</a>
                </li>
                <li class="page-item ${page>=totalPages?'disabled':''}">
                    <a class="page-link" href="?q=${q}&status=${status}&size=${size}&page=${page+1}">Suivant</a>
                </li>
            </ul>
        </nav>
    </div>
</section>

<jsp:include page="/views/template/templateFooter.jsp"/>
