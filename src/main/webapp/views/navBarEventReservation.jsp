<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 16-09-25
  Time: 21:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--Barre de navigation commune à la page réservation et évènement rste à régler l'affichage qui doit être à gauche.  -->
<section class="page-section portfolio sidebar" id="portfolio">
    <div>
        <h2>GESTION</h2>

        <div class="submenu">
            <form action="${pageContext.request.contextPath}/liste" method="get">
                <button type="submit" class="nav-subitem">LISTE</button>
            </form>
            <form action="${pageContext.request.contextPath}/aConfirmer" method="get">
                <button type="submit" class="nav-subitem">A CONFIRMER</button>
            </form>
        </div>

        <form action="${pageContext.request.contextPath}/ajouter" method="get">
            <button type="submit" class="nav-item">AJOUTER</button>
        </form>
    </div>
</section>
