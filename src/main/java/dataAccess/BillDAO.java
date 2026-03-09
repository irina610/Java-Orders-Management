package dataAccess;

import model.Bill;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    public void insert(Bill bill) {
        String sql = "INSERT INTO Log (clientEmail, productName, quantity, totalPrice, timestamp) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bill.clientEmail());
            stmt.setString(2, bill.productName());
            stmt.setInt(3, bill.quantity());
            stmt.setDouble(4, bill.totalPrice());
            stmt.setTimestamp(5, Timestamp.valueOf(bill.timestamp()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting bill", e);
        }
    }

    public List<Bill> findByClientEmail(String clientEmail) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM Log WHERE clientEmail = ? ORDER BY timestamp DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clientEmail);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("id"),
                        rs.getString("clientEmail"),
                        rs.getString("productName"),
                        rs.getInt("quantity"),
                        rs.getDouble("totalPrice"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                );
                bills.add(bill);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bills by client email", e);
        }
        return bills;
    }

}
