package dataAccess;
import model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAO {

    public Optional<Client> findByEmail(String email) {
        String sql = "SELECT * FROM Client WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                return Optional.of(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding client by email", e);
        }
        return Optional.empty();
    }

}


