<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="page-section">
    <div class="container">
        <h2 class="text-center text-uppercase mb-4">Gestion des utilisateurs</h2>

        <form method="get" class="mb-3 d-flex" action="">
            <input class="form-control me-2" name="q" placeholder="Recherche nom / email" value="${param.q}"/>
            <button class="btn btn-primary" type="submit">Rechercher</button>
        </form>

        <div class="table-responsive">
            <table class="table table-striped align-middle">
                <thead>
                <tr>
                    <th>#</th><th>Nom</th><th>Email</th><th>Rôle</th><th>Actif</th><th>Blacklist</th><th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.lastName} ${u.firstName}</td>
                        <td>${u.email}</td>
                        <td>${u.role.roleName}</td>
                        <td><span class="badge ${u.active?'bg-success':'bg-secondary'}">${u.active?'Oui':'Non'}</span></td>
                        <td><span class="badge ${u.blacklist?'bg-danger':'bg-success'}">${u.blacklist?'Oui':'Non'}</span></td>
                        <td>
                            <c:if test="${canEdit}">
                                <form method="post" style="display:inline">
                                    <input type="hidden" name="id" value="${u.id}"/>
                                    <input type="hidden" name="action" value="toggleActive"/>
                                    <button class="btn btn-sm ${u.active?'btn-warning':'btn-success'}" type="submit">
                                            ${u.active?'Désactiver':'Activer'}
                                    </button>
                                </form>
                                <form method="post" style="display:inline">
                                    <input type="hidden" name="id" value="${u.id}"/>
                                    <input type="hidden" name="action" value="recomputeBlacklist"/>
                                    <button class="btn btn-sm btn-outline-danger" type="submit">Basculer blacklist</button>
                                </form>
                            </c:if>

                            <c:if test="${isAdmin}">
                                <form method="post" class="d-inline-flex align-items-center">
                                    <input type="hidden" name="id" value="${u.id}"/>
                                    <input type="hidden" name="action" value="setRole"/>
                                    <select name="roleName" class="form-select form-select-sm me-2" style="width:auto">
                                        <option ${u.role.roleName=='ADMIN'?'selected':''}>ADMIN</option>
                                        <option ${u.role.roleName=='SECRETAIRE'?'selected':''}>SECRETAIRE</option>
                                        <option ${u.role.roleName=='BARMAN'?'selected':''}>BARMAN</option>
                                        <option ${u.role.roleName=='UTILISATEUR'?'selected':''}>UTILISATEUR</option>
                                    </select>
                                    <button class="btn btn-sm btn-primary" type="submit">Assigner</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <%-- numQuery utilisés: Role.findByName --%>
    </div>
</section>
