<%--
  Formulaire inscription utilisateur + affiches erreurs via Userservlet
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="page-section">
    <div class="container">
        <h2 class="text-center text-uppercase text-secondary mb-4">Créer un compte</h2>

        <!--erreurs globales-->
        <c:if test="${not empty errors}">
            <div class="alert alert-danger">
                <ul style="margin:0;">
                    <c:forEach var="e" items="${errors}">
                        <li><strong>${e.key}</strong> : ${e.value}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <!-- formulaire principal -->
        <form method="post" action="${pageContext.request.contextPath}/user">
            <input type="hidden" name="action" value="submit"/>
            <div class="col-md-6 col-lg-5 mx-auto" style="max-width: 560px;">

            <!-- identité de l'utilisateur -->
            <div class="mb-3">
                    <label>Civilité</label>
                    <select class="form-select" name="civilite">
                        <option ${old.civilite == 'M' ? 'selected' : ''} value="M">M</option>
                        <option ${old.civilite == 'Mme' ? 'selected' : ''} value="Mme">Mme</option>
                        <option ${old.civilite == 'Dr' ? 'selected' : ''} value="Dr">Dr</option>
                        <option ${old.civilite == 'Autre' ? 'selected' : ''} value="Autre">Autre</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label>Genre</label>
                    <select class="form-select" name="gender">
                        <option ${old.gender == 'M' ? 'selected' : ''} value="M">M</option>
                        <option ${old.gender == 'F' ? 'selected' : ''} value="F">F</option>
                        <option ${old.gender == 'Autre' ? 'selected' : ''} value="Autre">Autre</option>
                    </select>
                </div>
            <div class="mb-3">
                <label>Nom</label>
                <input class="form-control" name="lastName" value="${old.lastName}"/>
            </div>

            <div class="mb-3">
                <label>Prénom</label>
                <input class="form-control" name="firstName" value="${old.firstName}"/>
            </div>

            <div class="mb-3">
                <label>Date de naissance</label>
                <input class="form-control" type="date" name="birthdate" value="${old.birthdate}"/>
            </div>

            <div class="mb-3">
                <label>Email</label>
                <input class="form-control" name="email" value="${old.email}"/>
            </div>

            <div class="mb-3">
                <label>Mot de passe</label>
                <input class="form-control" type="password" name="password"/>
            </div>

            <div class="mb-3">
                <label>Confirmer mot de passe</label>
                <input class="form-control" type="password" name="password2"/>
            </div>

            <hr class="my-4"/>

            <!-- Adresse -->
            <div class="mb-3">
                <label>Numéro</label>
                <input class="form-control" name="number" value="${old.number}"/>
            </div>

            <div class="mb-3">
                <label>Rue</label>
                <input class="form-control" name="street" value="${old.street}"/>
            </div>

            <div class="mb-3">
                <label for="cityId" class="form-label">Ville</label>
                <select id="cityId" name="cityId" class="form-select">
                    <option value="">— sélectionnez une ville —</option>
                    <c:forEach var="city"
                               items="${cities}">
                        <option value="${city.id}"
                                <c:if test="${old.cityId == city.id || param.cityId == city.id}">selected</c:if>>
                                ${city.cityName} - ${city.zipCode}
                        </option>
                    </c:forEach>
                </select>
            </div>
                <div class="mb-3">
                    <label for="countryId" class="form-label">Pays</label>
                    <select id="countryId" name="countryId" class="form-select">
                        <option value="">— sélectionnez un pays —</option>
                        <c:forEach var="c" items="${countries}">
                            <option value="${c.id}"
                                    <c:if test="${old.countryId == c.id || param.countryId  == c.id}">selected</c:if>>
                                ${c.countryName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

            <!-- bouton final -->
            <div class="d-grid gap-2">
                <button class="btn btn-success" type="submit" name="action" value="submit">
                    S’inscrire
                </button>
            </div>

            <div class="text-center mt-2">
                <a class="btn btn-link p-0" href="${pageContext.request.contextPath}/home">Retour à l’accueil</a>
                <span class="mx-1">•</span>
                <a class="btn btn-link p-0" href="${pageContext.request.contextPath}/login">Se connecter</a>
            </div>

            </div>
        </form>
    </div>
</section>