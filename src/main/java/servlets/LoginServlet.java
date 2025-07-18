package servlets; // 📦 Ce fichier appartient au package 'servlets'

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/login") // 🔗 Ce Servlet est lié à l'URL '/login'
public class LoginServlet extends HttpServlet {

    // 🟩 Méthode appelée quand le formulaire est envoyé en POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🧠 On récupère les données du formulaire HTML
        String login = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // 🛠️ On charge le driver JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 🔌 On établit la connexion à la base de données
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/centreSportif", "root", "");

            // ❓ On prépare une requête SQL pour vérifier l'utilisateur
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE login = ? AND password = ?");

            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // 🎉 Si utilisateur trouvé, on démarre une session
                HttpSession session = request.getSession();
                session.setAttribute("user", login); // On stocke le login

                // ⏩ Redirection vers la page d’accueil
                response.sendRedirect("views/home.jsp");
            } else {
                // ❌ Sinon on retourne à login.jsp avec un message d’erreur
                response.sendRedirect("views/login.jsp?error=true");
            }

            conn.close(); // ✅ On ferme la connexion

        } catch (Exception e) {
            // ⚠️ Si une erreur arrive, on l'affiche dans la page
            e.printStackTrace();
            response.getWriter().println("Erreur : " + e.getMessage());
        }
    }
}
