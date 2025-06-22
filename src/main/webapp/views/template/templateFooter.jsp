<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<footer class="site-footer">
  <div class="footer-container">

    <!-- Colonne 1 : Informations de l'entreprise -->
    <div class="footer-column">
      <h3>Notre entreprise</h3>
      <p>Nom : Wolf Corp</p>
      <p>Adresse : 123 Rue des Loups, Paris</p>
      <p>Email : contact@wolfcorp.com</p>
      <p>Tél : +33 1 23 45 67 89</p>
    </div>

    <!-- Colonne 2 : Liens du site -->
    <div class="footer-column">
      <h3>Navigation</h3>
      <ul>
        <li><a href="${pageContext.request.contextPath}/home">Accueil</a></li>
        <li><a href="${pageContext.request.contextPath}/countries">Liste des pays</a></li>
        <li><a href="#">À propos</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
    </div>

    <!-- Colonne 3 : Autres liens -->
    <div class="footer-column">
      <h3>Ressources</h3>
      <ul>
        <li><a href="#">Mentions légales</a></li>
        <li><a href="#">Politique de confidentialité</a></li>
        <li><a href="#">FAQ</a></li>
        <li><a href="#">Support</a></li>
      </ul>
    </div>

  </div>

  <div class="footer-bottom">
    &copy; 2025 Wolf Corp. Tous droits réservés.
  </div>
</footer>

</body>
</html>
