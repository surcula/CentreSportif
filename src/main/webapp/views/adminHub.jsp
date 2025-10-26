<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/23/2025
  Time: 3:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!-- Section-->
<section class="page-section portfolio" id="portfolio">
    <div class="container">


        <c:choose>
            <c:when test="${sessionScope.role == 'ADMIN'
                or sessionScope.role == 'BARMAN'
                or sessionScope.role == 'SECRETARY'}">
                <!-- Admin Section Heading-->
                <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Admin Hub</h2>
            </c:when>
            <c:otherwise>
                <!-- user Section Heading-->
                <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Infos pratiques</h2>
            </c:otherwise>
        </c:choose>


        <!-- Icon Divider-->
        <div class="divider-custom">
            <div class="divider-custom-line"></div>
            <div class="divider-custom-icon"><i class="fas fa-star"></i></div>
            <div class="divider-custom-line"></div>
        </div>
    </div>


    <div class="row row-cols-1 row-cols-md-3 g-4">

        <!-- Card Halls -->
        <div class="col">
            <div class="card shadow-sm text-center">
                <div class="p-3">
                    <img src="assets/img/halls.png" class="imgCard " alt="Hall sportif">
                </div>
                <div class="card-body d-flex flex-column">

                    <c:choose>
                        <c:when test="${sessionScope.role == 'ADMIN'
                or sessionScope.role == 'BARMAN'
                or sessionScope.role == 'SECRETARY'}">
                            <h5 class="card-title">Gestion des halls</h5>
                            <p class="card-text">Ajoutez, modifiez ou désactivez les halls sportifs.</p>
                            <div class="mt-auto d-grid">
                                <a href="${pageContext.request.contextPath}/hall" class="btn btn-primary">Gérer les
                                    halls</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h5 class="card-title">Halls disponibles</h5>
                            <p class="card-text">Consultez la liste des halls et leurs disponibilités.</p>
                            <div class="mt-auto d-grid">
                                <a href="${pageContext.request.contextPath}/hall" class="btn btn-secondary">Voir les
                                    halls</a>
                            </div>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>

        <!-- Card Fields -->
        <div class="col">
            <div class="card shadow-sm text-center">
                <div class="p-3">
                    <img src="assets/img/fields.png" class="imgCard" alt="Terrain sportif">
                </div>
                <div class="card-body d-flex flex-column">
                    <c:choose>
                        <c:when test="${role == 'ADMIN' or role == 'BARMAN' or role == 'SECRETARY'}">
                            <h5 class="card-title">Gestion des terrains</h5>
                            <p class="card-text">Ajoutez, modifiez ou désactivez les terrains sportifs.</p>
                            <div class="mt-auto d-grid">
                                <a href="${pageContext.request.contextPath}/field" class="btn btn-primary">Gérer les
                                    terrains</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h5 class="card-title">Terrains disponibles</h5>
                            <p class="card-text">Consultez la liste des terrains et leurs disponibilités.</p>
                            <div class="mt-auto d-grid">
                                <a href="${pageContext.request.contextPath}/field" class="btn btn-secondary">Voir les
                                    terrains</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Card Sports -->
        <div class="col">
            <div class="card shadow-sm text-center">
                <div class="p-3">
                    <img src="assets/img/sports.png" class="imgCard" alt="Sports">
                </div>
                <div class="card-body d-flex flex-column">
                    <c:choose>
                        <c:when test="${role == 'ADMIN' or role == 'BARMAN' or role == 'SECRETARY'}">
                            <h5 class="card-title">Gestion des sports</h5>
                            <p class="card-text">Ajoutez, modifiez ou désactivez les sports.</p>
                            <div class="mt-auto d-grid">
                                <a href="${pageContext.request.contextPath}/sport" class="btn btn-primary">Gérer les
                                    sports</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h5 class="card-title">Sports</h5>
                            <p class="card-text">Parcourez les disciplines proposées et leurs créneaux.</p>
                            <div class="mt-auto d-grid">
                                <a href="${pageContext.request.contextPath}/sport" class="btn btn-secondary">Voir les
                                    sports</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <!-- Card Users -->
        <div class="col">
            <div class="card shadow-sm text-center">
                <div class="p-3">
                    <img src="assets/img/Utilisateurs.png" class="imgCard" alt="Utilisateurs">
                </div>
                <div class="card-body d-flex flex-column">
                    <h5 class="card-title">Gestion des utilisateurs</h5>
                    <p class="card-text">Activer/Désactiver, rôles, blacklist.</p>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary mt-auto">Gérer les utilisateurs</a>
                </div>
            </div>
        </div>

        <!-- Card SportField -->
        <c:choose>
            <c:when test="${role == 'ADMIN' or role == 'BARMAN' or role == 'SECRETARY'}">
                <div class="col">
                    <div class="card shadow-sm text-center">
                        <div class="p-3">
                            <img src="assets/img/sportField.png" class="imgCard" alt="Sports">
                        </div>
                        <div class="card-body d-flex flex-column">

                            <h5 class="card-title">Gestion de l'attribution des sports aux terrains</h5>
                            <p class="card-text">Ajoutez, modifiez ou désactivez les attributions.</p>
                            <div class="mt-auto d-grid">
                                <a href="${pageContext.request.contextPath}/sports-fields" class="btn btn-primary">Gérer
                                    les attributions.</a>
                            </div>

                        </div>
                    </div>
                </div>
            </c:when>
        </c:choose>


    </div>

</section>

