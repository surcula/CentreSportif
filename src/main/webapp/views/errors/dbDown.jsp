<%@ page contentType="text/html; charset=UTF-8" %>
<%
    response.setStatus(503);
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    response.setHeader("Pragma", "no-cache");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Service temporairement indisponible</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/error.css">
</head>
<body>
<h1>Service temporairement indisponible</h1>
<p>La base de données est actuellement hors ligne.<br>
    Merci de réessayer dans quelques minutes.</p>
<div class="retry">
    <a href="<%= request.getContextPath() %>/">GO HOME</a>
</div>
</body>
</html>
