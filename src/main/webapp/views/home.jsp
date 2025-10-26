<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 6/22/2025
  Time: 12:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Masthead-->
<header class="masthead bg-primary text-white text-center">
    <div class="container d-flex align-items-center flex-column">
        <!-- Masthead Avatar Image-->
        <img class="masthead-avatar mb-5" src="assets/img/centreSportif.png" alt="..."/>
        <!-- Masthead Heading-->
        <h1 class="masthead-heading text-uppercase mb-0">Friend Sport Smile Synergie</h1>
        <!-- Icon Divider-->
        <div class="divider-custom divider-light">
            <div class="divider-custom-line"></div>
            <div class="divider-custom-icon"><i class="fas fa-star"></i></div>
            <div class="divider-custom-line"></div>
        </div>
        <!-- Masthead Subheading-->
        <p class="masthead-subheading font-weight-light mb-0">Franz - Sophie - Soukaïna - Sarah</p>
        <c:if test="${not empty sessionScope.role}">
            <p class="text-center text-light mt-2">
                Connecté en tant que <strong>${sessionScope.role}</strong>
            </p>
        </c:if>
    </div>
</header>

<!-- Sports presentation Section-->
<section class="page-section portfolio" id="portfolio">
    <div class="container">
        <!-- presentation Section Heading-->
        <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Sports</h2>
        <!-- Icon Divider-->
        <div class="divider-custom">
            <div class="divider-custom-line"></div>
            <div class="divider-custom-icon"><i class="fas fa-star"></i></div>
            <div class="divider-custom-line"></div>
        </div>

        <!-- Cards des sports-->
        <div class="row row-cols-1 row-cols-md-5 g-4">

            <!-- Soccer Halls -->
            <div class="col">
                <div class="card shadow-sm text-center">
                    <div class="p-3">
                        <img src="assets/img/soccer.jpg" class="imgCard " alt="soccer">
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Futsall</h5>
                        <p class="card-text">Venez jouer en salle, quel que soit le temps !</p>
                    </div>
                </div>
            </div>

            <!-- Basket Halls -->
            <div class="col">
                <div class="card shadow-sm text-center">
                    <div class="p-3">
                        <img src="assets/img/basket.png" class="imgCard " alt="basket">
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Basket</h5>
                        <p class="card-text">Matchs et entraînements toute l’année.</p>
                    </div>
                </div>
            </div>
            <!-- Squash Halls -->
            <div class="col">
                <div class="card shadow-sm text-center">
                    <div class="p-3">
                        <img src="assets/img/squash.png" class="imgCard " alt="squash">
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Squash</h5>
                        <p class="card-text">Des terrains modernes pour les passionnés de raquettes</p>
                    </div>
                </div>
            </div>
            <!-- tennis Halls -->
            <div class="col">
                <div class="card shadow-sm text-center">
                    <div class="p-3">
                        <img src="assets/img/tennis.jpg" class="imgCard " alt="tennis">
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Tennis</h5>
                        <p class="card-text">Des terrains modernes pour les passionnés de raquettes</p>
                    </div>
                </div>
            </div>
            <!-- volleyball Halls -->
            <div class="col">
                <div class="card shadow-sm text-center">
                    <div class="p-3">
                        <img src="assets/img/volleyball.png" class="imgCard " alt="volleyball">
                    </div>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Volleyball</h5>
                        <p class="card-text">Un sport collectif fun et dynamique.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- About Section-->
<section class="page-section bg-primary text-white mb-0" id="about">
    <div class="container">
        <!-- About Section Heading-->
        <h2 class="page-section-heading text-center text-uppercase text-white">About</h2>
        <!-- Icon Divider-->
        <div class="divider-custom divider-light">
            <div class="divider-custom-line"></div>
            <div class="divider-custom-icon"><i class="fas fa-star"></i></div>
            <div class="divider-custom-line"></div>
        </div>
        <!-- About Section Content-->
        <div class="row">
            <div class="col-lg-4 ms-auto"><p class="lead">
                Bienvenue au <strong>Centre Sportif FSSS</strong>, un espace moderne et convivial dédié à la pratique sportive pour tous.
                Nous proposons plusieurs disciplines accessibles à tous les âges et niveaux : football en salle, tennis, squash, volleyball et bien plus encore.

            </p></div>
            <div class="col-lg-4 me-auto"><p class="lead">
                Nos infrastructures comprennent des <strong>halls sportifs spacieux</strong> et des <strong>terrains de qualité</strong>, disponibles toute l’année.
                Grâce à notre système de réservation en ligne, vous pouvez facilement planifier vos séances seul, en famille ou entre amis.
                Rejoignez-nous et profitez d’un lieu dynamique où sport rime avec plaisir et partage !

            </p></div>
        </div>

    </div>
</section>
<!-- Contact Section-->
<section class="page-section" id="contact">
    <div class="container">
        <!-- Contact Section Heading-->
        <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Contact Me</h2>
        <!-- Icon Divider-->
        <div class="divider-custom">
            <div class="divider-custom-line"></div>
            <div class="divider-custom-icon"><i class="fas fa-star"></i></div>
            <div class="divider-custom-line"></div>
        </div>
        <!-- Contact Section Form-->
        <div class="row justify-content-center">
            <div class="col-lg-8 col-xl-7">
                <form id="contactForm" data-sb-form-api-token="API_TOKEN">
                    <!-- Name input-->
                    <div class="form-floating mb-3">
                        <input class="form-control" id="name" type="text" placeholder="Enter your name..."
                               data-sb-validations="required"/>
                        <label for="name">Nom et Prénom</label>
                        <div class="invalid-feedback" data-sb-feedback="name:required">Nom et prénom required.</div>
                    </div>
                    <!-- Email address input-->
                    <div class="form-floating mb-3">
                        <input class="form-control" id="email" type="email" placeholder="name@example.com"
                               data-sb-validations="required,email"/>
                        <label for="email">Email</label>
                        <div class="invalid-feedback" data-sb-feedback="email:required">An email is required.</div>
                        <div class="invalid-feedback" data-sb-feedback="email:email">Email is not valid.</div>
                    </div>
                    <!-- Phone number input-->
                    <div class="form-floating mb-3">
                        <input class="form-control" id="phone" type="tel" placeholder="(123) 456-7890"
                               data-sb-validations="required"/>
                        <label for="phone">Téléphonne</label>
                        <div class="invalid-feedback" data-sb-feedback="phone:required">Téléphonne is required.
                        </div>
                    </div>
                    <!-- Message input-->
                    <div class="form-floating mb-3">
                        <textarea class="form-control" id="message" type="text" placeholder="Enter your message here..."
                                  style="height: 10rem" data-sb-validations="required"></textarea>
                        <label for="message">Message</label>
                        <div class="invalid-feedback" data-sb-feedback="message:required">A message is required.</div>
                    </div>
                    <!-- Submit success message-->
                    <!---->
                    <!-- This is what your users will see when the form-->
                    <!-- has successfully submitted-->
                    <div class="d-none" id="submitSuccessMessage">
                        <div class="text-center mb-3">
                            <div class="fw-bolder">Form submission successful!</div>
                        </div>
                    </div>
                    <!-- Submit error message-->
                    <!---->
                    <!-- This is what your users will see when there is-->
                    <!-- an error submitting the form-->
                    <div class="d-none" id="submitErrorMessage">
                        <div class="text-center text-danger mb-3">Error sending message!</div>
                    </div>
                    <!-- Submit Button-->
                    <button class="btn btn-primary btn-xl disabled" id="submitButton" type="submit">Envoyer</button>
                </form>
            </div>
        </div>
    </div>
</section>