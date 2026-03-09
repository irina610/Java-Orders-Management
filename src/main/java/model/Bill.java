package model;

import java.time.LocalDateTime;

public record Bill(
        int id,
        String clientEmail,
        String productName,
        int quantity,
        double totalPrice,
        LocalDateTime timestamp
) {
    public Bill(String clientEmail, String productName,
                int quantity, double totalPrice) {
        this(0, clientEmail, productName, quantity, totalPrice, LocalDateTime.now());
    }
}
