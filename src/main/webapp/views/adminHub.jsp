<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/23/2025
  Time: 3:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!-- Section-->
<section class="page-section portfolio" id="portfolio">
    <div class="container">
        <!-- Admin Section Heading-->
        <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Admin Hub</h2>
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
                    <h5 class="card-title">Gestion des halls</h5>
                    <p class="card-text">Ajoutez, modifiez ou désactivez les halls sportifs.</p>
                    <div class="mt-auto d-grid">
                        <a href="${pageContext.request.contextPath}/hall" class="btn btn-primary">Gérer les halls</a>
                    </div>
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
                    <h5 class="card-title">Gestion des terrains</h5>
                    <p class="card-text">Ajoutez, modifiez ou désactivez les terrains sportifs.</p>
                    <div class="mt-auto d-grid">
                        <a href="${pageContext.request.contextPath}/field" class="btn btn-primary">Gérer les terrains</a>
                    </div>
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
                    <h5 class="card-title">Gestion des sports</h5>
                    <p class="card-text">Ajoutez, modifiez ou désactivez les sports.</p>
                    <div class="mt-auto d-grid">
                        <a href="${pageContext.request.contextPath}/sport" class="btn btn-primary">Gérer les sports</a>
                    </div>
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

    </div>

</section>

