<%--Formulaire inscription utilisateur + affiches erreurs via Userservlet--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="page-section">
    <div class="container mt-5 pt-4">
        <h2 class="text-center text-uppercase text-secondary mb-4">Créer un compte</h2>


        <!-- formulaire principal -->
        <form method="post" action="${pageContext.request.contextPath}/user">
            <input type="hidden" name="action" value="submit"/>
            <div class="col-md-6 col-lg-5 mx-auto" style="max-width: 560px;">

            <!-- identité de l'utilisateur -->
                <div class="mb-3">
                    <label for="civilite" class="form-label">Civilité</label>
                    <select id="civilite" name="civilite"
                            class="form-select ${errors != null && errors.civilite != null ? 'is-invalid' : ''}" required>
                        <option value="">— Sélectionnez —</option>
                        <option value="M"     ${old.civilite == 'M'     ? 'selected' : ''}>M</option>
                        <option value="Mme"   ${old.civilite == 'Mme'   ? 'selected' : ''}>Mme</option>
                        <option value="Dr"    ${old.civilite == 'Dr'    ? 'selected' : ''}>Dr</option>
                        <option value="Autre" ${old.civilite == 'Autre' ? 'selected' : ''}>Autre</option>
                    </select>
                    <div class="invalid-feedback">${errors != null ? errors.civilite : ''}</div>
                </div>

                <div class="mb-3">
                    <label for="gender" class="form-label">Genre</label>
                    <select id="gender" name="gender"
                            class="form-select ${errors != null && errors.gender != null ? 'is-invalid' : ''}" required>
                        <option value="">— Sélectionnez —</option>
                        <option value="M"     ${old.gender == 'M'     ? 'selected' : ''}>M</option>
                        <option value="F"     ${old.gender == 'F'     ? 'selected' : ''}>F</option>
                        <option value="Autre" ${old.gender == 'Autre' ? 'selected' : ''}>Autre</option>
                    </select>
                    <div class="invalid-feedback">${errors != null ? errors.gender : ''}</div>
                </div>

            <div class="mb-3">
                <label>Nom</label>
                <input class="form-control ${errors != null && errors.lastName != null ? 'is-invalid' : ''}"
                       name="lastName" value="${old.lastName}" />
                <div class="invalid-feedback">${errors != null ? errors.lastName : ''}</div>
            </div>

            <div class="mb-3">
                <label>Prénom</label>
                <input class="form-control ${errors != null && errors.firstName != null ? 'is-invalid' : ''}"
                       name="firstName" value="${old.firstName}" />
                <div class="invalid-feedback">${errors != null ? errors.firstName : ''}</div>            </div>

            <div class="mb-3">
                <label>Date de naissance</label>
                <input class="form-control ${errors != null && errors.birthdate != null ? 'is-invalid' : ''}"
                       type="date" name="birthdate" value="${old.birthdate}" />
                <div class="invalid-feedback">${errors != null ? errors.birthdate : ''}</div>            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" id="email" name="email"
                       class="form-control ${errors != null && errors.email != null ? 'is-invalid' : ''}"
                       value="${old != null && old.email != null ? old.email : ''}"
                       pattern="^[^@\s]+@[^@\s]+\.[^@\s]{2,}$"
                       title="Ex. nom@domaine.be"
                       required />
                <div class="invalid-feedback">${errors != null ? errors.email : ''}</div>            </div>

           <div class="mb-3">
               <label for="phone" class="form-label">Téléphone</label>
               <input
                       type="text" id="phone" name="phone"
                       class="form-control ${errors != null && errors.phone != null ? 'is-invalid' : ''}"
                       value="${old != null && old.phone != null ? old.phone : ''}"
                       placeholder="+32 4 12 34 56 78"
                       minlength="8"
                       required
               />
               <div class="invalid-feedback">${errors != null ? errors.phone : ''}</div>
           </div>

            <div class="mb-3">
                <label>Mot de passe</label>
                <input class="form-control ${errors != null && errors.password != null ? 'is-invalid' : ''}"
                       type="password" name="password" />
                <div class="invalid-feedback">${errors != null ? errors.password : ''}</div>
            </div>

            <div class="mb-3">
                <label>Confirmer mot de passe</label>
                <input class="form-control ${errors != null && errors.password2 != null ? 'is-invalid' : ''}"
                       type="password" name="password2" />
                <div class="invalid-feedback">${errors != null ? errors.password2 : ''}</div>            </div>

            <hr class="my-4"/>

            <!-- Adresse -->
            <div class="mb-3">
                <label>Numéro</label>
                <input class="form-control ${errors != null && errors.number != null ? 'is-invalid' : ''}"
                       name="number" value="${old.number}" />
                <div class="invalid-feedback">${errors != null ? errors.number : ''}</div>
            </div>

            <div class="mb-3">
                <label>Rue</label>
                <input class="form-control ${errors != null && errors.street != null ? 'is-invalid' : ''}"
                       name="street" value="${old.street}" />
                <div class="invalid-feedback">${errors != null ? errors.street : ''}</div>
            </div>

            <div class="mb-3">
                <label for="cityId" class="form-label">Ville</label>
                <select id="cityId" name="cityId" class="form-select  ${errors != null && errors.cityId != null ? 'is-invalid' : ''}">
                    <option value="">— sélectionnez une ville —</option>
                    <c:forEach var="cityItem" items="${cities}">
                        <option value="${cityItem.id}"
                                <c:if test="${old.cityId == cityItem.id || param.cityId == cityItem.id}">selected</c:if>>
                                ${cityItem.cityName} - ${cityItem.zipCode}
                        </option>
                    </c:forEach>
                </select>
                <div class="invalid-feedback">${errors != null ? errors.cityId : ''}</div>
            </div>

                <div class="mb-3">
                    <label for="countryId" class="form-label">Pays</label>
                    <select id="countryId" name="countryId"
                            class="form-select ${errors != null && errors.countryId != null ? 'is-invalid' : ''}">
                        <option value="">— sélectionnez un pays —</option>
                        <c:forEach var="countryItem" items="${countries}">
                            <option value="${countryItem.id}"
                                    <c:if test="${old.countryId == countryItem.id || param.countryId == countryItem.id}">selected</c:if>>
                                    ${countryItem.countryName}
                            </option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">${errors != null ? errors.countryId : ''}</div>
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