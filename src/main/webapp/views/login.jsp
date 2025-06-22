<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/22/2025
  Time: 2:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section class="page-section">

<h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Connexion</h2>

<div class="row justify-content-center">
    <div class="col-lg-8 col-xl-7">
        <form method="post" action="${pageContext.request.contextPath}/login">
            <!-- User ID input -->
            <div class="form-floating mb-3">
                <input class="form-control" id="userId" name="userId" type="text" placeholder="Entrez votre identifiant..." required />
                <label for="userId">ID Utilisateur</label>
            </div>

            <!-- Role select -->
            <div class="form-floating mb-3">
                <select class="form-select" id="role" name="role" required>
                    <option value="" disabled selected>Choisissez un rôle</option>
                    <option value="ADMIN">ADMIN</option>
                    <option value="USER">USER</option>
                </select>
                <label for="role">Rôle</label>
            </div>

            <!-- Error message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">
                        ${error}
                </div>
            </c:if>

            <!-- Submit Button -->
            <div class="text-center">
                <button class="btn btn-primary btn-xl" type="submit">Se connecter</button>
            </div>
        </form>
    </div>
</div>


<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>
</section>