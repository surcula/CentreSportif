<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .page-section { padding-top: 10rem; padding-bottom: 6rem; }
    .page-section .container { min-height: 60vh; }
    .page-section-heading {text-align: center;margin-bottom: 10rem;}
</style>
<!-- SECTION : Page de connexion -->
<section class="page-section">
    <div class="container mt-5 pt-4">
    <!-- Titre principal -->
    <h2 class="page-section-heading text-center text-uppercase text-secondary mb-0">Connexion</h2>

        <!-- Message de confirmation ou d'erreur -->
        <c:if test="${not empty param.msg}">
        <div class="alert ${param.type=='success'?'alert-success':'alert-danger'} mt-3 text-center">
                ${param.msg}
        </div>
        </c:if>

    <div class="row justify-content-center">
        <div class="col-lg-8 col-xl-7">

            <!-- FORMULAIRE DE CONNEXION -->
            <form method="post" action="${pageContext.request.contextPath}/login">

                <!-- Champ : Adresse e-mail -->
                <div class="form-floating mb-3">
                    <input class="form-control"
                           id="email"
                           name="email"
                           type="email"
                           placeholder="ex: moi@mail.com"
                           required/>
                    <label for="email">Adresse e-mail</label>
                </div>

                <!-- Champ : Mot de passe -->
                <div class="form-floating mb-3">
                    <input class="form-control"
                           id="password"
                           name="password"
                           type="password"
                           placeholder="Votre mot de passe"
                           required/>
                    <label for="password">Mot de passe</label>
                </div>

                <!-- Affichage du message d'erreur -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger text-center">${errorMessage}</div>
                </c:if>

                <!-- Bouton de soumission -->
                <div class="text-center">
                    <button class="btn btn-primary btn-xl" type="submit">
                        Se connecter
                    </button>
                </div>
            </form>
        </div>
    </div>
</section>
