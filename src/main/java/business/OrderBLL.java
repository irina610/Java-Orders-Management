package business;

import dataAccess.OrderDAO;
import dataAccess.ProductDAO;
import dataAccess.BillDAO;
import model.*;
import java.util.List;

public class OrderBLL {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final BillDAO billDAO = new BillDAO();

    public void placeOrder(Order order) {
        Product product = productDAO.findByName(order.getProductName())
                .orElseThrow(() -> new IllegalArgumentException("Produsul nu exista"));

        if (product.getStock() < order.getQuantity()) {
            throw new IllegalArgumentException("Stoc insuficient");
        }

        product.setStock(product.getStock() - order.getQuantity());
        productDAO.update(product);

        orderDAO.insert(order);

        Bill bill = new Bill(
                order.getClientEmail(),
                order.getProductName(),
                order.getQuantity(),
                order.getQuantity() * product.getPrice()
        );
        billDAO.insert(bill);
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }
}
