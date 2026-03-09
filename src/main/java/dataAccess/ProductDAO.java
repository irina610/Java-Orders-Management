package dataAccess;

import model.Product;
import java.sql.*;
import java.util.Optional;

public class ProductDAO {

    public Optional<Product> findByName(String name) {
        String sql = "SELECT * FROM Product WHERE name = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getDouble("price")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by name", e);
        }
        return Optional.empty();
    }

    public void update(Product product) {
        String sql = "UPDATE Product SET name = ?, stock = ?, price = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getStock());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }
}