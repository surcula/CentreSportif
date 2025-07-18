<%@ page contentType="text/html;charset=UTF-8" %> <%-- Page JSP encodée en UTF-8 --%>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
</head>
<body>

<h2>Connexion à votre compte</h2>

<!-- Formulaire de connexion -->
<form action="login" method="post">
    <label>Nom d'utilisateur :</label>
    <input type="text" name="username" required><br><br>

    <label>Mot de passe :</label>
    <input type="password" name="password" required><br><br>

    <button type="submit">Se connecter</button>
</form>

<!-- Message d'erreur si mauvais identifiants -->
<% if (request.getParameter("error") != null) { %>
<p style="color:red;">Nom d'utilisateur ou mot de passe incorrect</p>
<% } %>

</body>
</html>
