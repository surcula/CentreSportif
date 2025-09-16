<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 13-09-25
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/navBarEventReservation.jsp" %>
<section class="page-section portfolio" id="portfolio">

    <h2>Créer un Nouvel Événement</h2>

    <form action="/ajouter-evenement" method="POST">
        <div>
            <label for="nom_evenement">Nom de l'événement :</label>
            <input type="text" id="nom_evenement" name="nom_evenement" required>
        </div>

        <div>
            <label for="description_evenement">Description :</label>
            <textarea id="description_evenement" name="description_evenement" rows="4" required></textarea>
        </div>

        <div>
            <label for="sport">Choisir un sport :</label>
            <select id="sport" name="sport" required>
                <option value="">Sélectionner un sport</option>
                <option value="football">Football</option>
                <option value="basketball">Basketball</option>
                <option value="tennis">Tennis</option>
                <option value="volleyball">Volleyball</option>
            </select>
        </div>

        <div>
            <label for="local">Choisir un local :</label>
            <select id="local" name="local" required>
                <option value="">Sélectionner un local</option>
                <option value="local_1">Local 1</option>
                <option value="local_2">Local 2</option>
                <option value="local_3">Local 3</option>
            </select>
        </div>

        <div>
            <label for="terrain">Choisir un terrain :</label>
            <select id="terrain" name="terrain" required>
                <option value="">Sélectionner un terrain</option>
                <option value="terrain_1">Terrain 1</option>
                <option value="terrain_2">Terrain 2</option>
                <option value="terrain_3">Terrain 3</option>
            </select>
        </div>

        <div>
            <label for="date">Date de l'événement :</label>
            <input type="date" id="date" name="date" required>
        </div>

        <div>
            <label for="horaire">Horaire de l'événement :</label>
            <input type="time" id="horaire" name="horaire" required>
        </div>

        <button type="submit">Créer l'événement</button>
    </form>
</section>
