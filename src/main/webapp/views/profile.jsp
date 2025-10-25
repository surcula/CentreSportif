
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- PAGE JSP : AFFICHAGE ET MISE À JOUR DU PROFIL UTILISATEUR -->

<section class="page-section">
    <div class="container" style="max-width:560px">
        <h2 class="text-center text-uppercase mb-4">Mon profil</h2>

        <!-- Message de confirmation ou d'erreur -->
        <c:if test="${not empty param.msg}">
            <div class="alert ${param.type=='success'?'alert-success':'alert-danger'}">${param.msg}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/profile">

            <!-- modification du profil -->
            <h5>Identité</h5>

            <div class="mb-3">
                <label>Civilité</label>
                <select class="form-select" name="civilite">
                    <option value="">— Sélectionnez —</option>
                    <option value="M" ${user.civilite=='M'?'selected':''}>M</option>
                    <option value="Mme" ${user.civilite=='Mme'?'selected':''}>Mme</option>
                    <option value="Dr" ${user.civilite=='Dr'?'selected':''}>Dr</option>
                    <option value="Autre" ${user.civilite=='Autre'?'selected':''}>Autre</option>
                </select>
            </div>

            <div class="mb-3">
                <label>Genre</label>
                <select class="form-select" name="gender">
                    <option value="">— Sélectionnez —</option>
                    <option value="M" ${user.gender=='M'?'selected':''}>M</option>
                    <option value="F" ${user.gender=='F'?'selected':''}>F</option>
                    <option value="Autre" ${user.gender=='Autre'?'selected':''}>Autre</option>
                </select>
            </div>

            <div class="mb-3">
                <label>Nom</label>
                <input class="form-control" name="lastName" value="${user.lastName}">
            </div>

            <div class="mb-3">
                <label>Prénom</label>
                <input class="form-control" name="firstName" value="${user.firstName}">
            </div>

            <div class="mb-3">
                <label>Date de naissance</label>
                <input class="form-control" type="date" name="birthdate" value="${user.birthdate}">
            </div>

            <div class="mb-3">
                <label>Téléphone</label>
                <input class="form-control" name="phone" value="${user.phone}">
            </div>

            <div class="mb-3">
                <label>Email</label>
                <input class="form-control" value="${user.email}" disabled>
                <small class="text-muted">Email non modifiable.</small>
            </div>

            <!-- Adresse -->
            <h5 class="mt-4">Adresse</h5>

            <div class="mb-3">
                <label>N°</label>
                <input class="form-control" name="number"
                       value="${user.address != null ? user.address.streetNumber : ''}">
            </div>

            <div class="mb-3">
                <label>Rue</label>
                <input class="form-control" name="street"
                       value="${user.address != null ? user.address.streetName : ''}">
            </div>

            <div class="mb-3">
                <label>Ville</label>
                <select class="form-select" name="cityId" required>
                    <option value="">— sélectionnez une ville —</option>
                    <c:forEach items="${cities}" var="c">
                        <option value="${c.id}" ${selectedCityId != null && selectedCityId == c.id ? 'selected' : ''}>
                                ${c.zipCode} - ${c.cityName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label>Pays</label>
                <select class="form-select" name="countryId" required>
                    <option value="">— sélectionnez un pays —</option>
                    <c:forEach items="${countries}" var="co">
                        <option value="${co.id}" ${selectedCountryId != null && selectedCountryId == co.id ? 'selected' : ''}>
                                ${co.countryName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <!-- boutons retour et enregistrer -->
            <div class="d-flex gap-2 mt-4">
                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/home">Retour</a>
                <button class="btn btn-primary" type="submit">Enregistrer</button>
            </div>
        </form>
    </div>
</section>
