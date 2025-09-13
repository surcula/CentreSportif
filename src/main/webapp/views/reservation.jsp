<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 13-09-25
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reservation
    </title>
</head>
<body>
<h2>Réserver un Terrain</h2>

<form action="/reserver-terrains" method="POST">
    <div>
        <label for="sport_reservation">Choisir un sport :</label>
        <select id="sport_reservation" name="sport" required>
            <option value="">Sélectionner un sport</option>
            <option value="football">Football</option>
            <option value="basketball">Basketball</option>
            <option value="tennis">Tennis</option>
            <option value="volleyball">Volleyball</option>
        </select>
    </div>

    <div>
        <label for="local_reservation">Choisir un local :</label>
        <select id="local_reservation" name="local" required>
            <option value="">Sélectionner un local</option>
            <option value="local_1">Local 1</option>
            <option value="local_2">Local 2</option>
            <option value="local_3">Local 3</option>
        </select>
    </div>

    <div>
        <label for="terrain_reservation">Choisir un terrain :</label>
        <select id="terrain_reservation" name="terrain" required>
            <option value="">Sélectionner un terrain</option>
            <option value="terrain_1">Terrain 1</option>
            <option value="terrain_2">Terrain 2</option>
            <option value="terrain_3">Terrain 3</option>
        </select>
    </div>

    <div>
        <label for="date_reservation">Date de réservation :</label>
        <input type="date" id="date_reservation" name="date" required>
    </div>

    <div>
        <label for="horaire_reservation">Horaire de réservation :</label>
        <input type="time" id="horaire_reservation" name="horaire" required>
    </div>

    <div>
        <label for="duree_reservation">Durée de la réservation (en heures) :</label>
        <input type="number" id="duree_reservation" name="duree" min="1" max="6" required>
    </div>

    <button type="submit">Réserver le terrain</button>
</form>
</body>
</html>
