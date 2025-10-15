<%--
  Created by IntelliJ IDEA.
  User: speed_1sntkjo
  Date: 04-10-25
  Time: 17:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="page-section">
    <div class="container">
        <c:if test="${param.registered == '1'}">
            <div class="alert alert-success text-center mb-4">
                Votre compte a bien été créé. Vous pouvez maintenant vous connecter.
            </div>
        </c:if>

        <div class="text-center">
            <a class="btn btn-primary mx-2" href="${pageContext.request.contextPath}/login">Se connecter</a>
            <a class="btn btn-outline-secondary mx-2" href="${pageContext.request.contextPath}/user">Créer un autre compte</a>
            <a class="btn btn-link mx-2" href="${pageContext.request.contextPath}/home">Retour à l’accueil</a>
        </div>
    </div>
</section>