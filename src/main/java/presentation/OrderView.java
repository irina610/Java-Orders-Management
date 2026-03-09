package presentation;

import business.ClientBLL;
import business.OrderBLL;
import business.ProductBLL;
import model.Order;
import util.TableGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderView extends JPanel {
    private final JComboBox<String> clientEmailCombo;
    private final JComboBox<String> productNameCombo;
    private final JTextField quantityField;
    private final JTextArea logArea;
    private JTable orderTable;

    private final ClientBLL clientBLL;
    private final ProductBLL productBLL;
    private final OrderBLL orderBLL;

    private final JPanel rightPanel;
    private final TableGenerator<Order> tableGenerator;

    public OrderView() {
        clientBLL = new ClientBLL();
        productBLL = new ProductBLL();
        orderBLL = new OrderBLL();
        tableGenerator = new TableGenerator<>();

        setLayout(new BorderLayout());

        // --- Panel pentru inputuri: Client Email, Product Name, Quantity ---
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        clientEmailCombo = new JComboBox<>();
        productNameCombo = new JComboBox<>();
        quantityField = new JTextField();

        inputPanel.add(new JLabel("Client Email:"));
        inputPanel.add(clientEmailCombo);
        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameCombo);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        // --- Panel pentru butoane ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton placeOrderBtn = new JButton("Place Order");
        JButton viewOrdersBtn = new JButton("View Orders");
        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(viewOrdersBtn);

        // --- Panel stanga sus: inputuri ---
        JPanel leftTopPanel = new JPanel(new BorderLayout());
        leftTopPanel.add(inputPanel, BorderLayout.NORTH);

        // --- Panel stanga complet: sus inputuri, jos butoane ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(leftTopPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Panel pentru tabel (dreapta) ---
        rightPanel = new JPanel(new BorderLayout());
        orderTable = new JTable(); // inițial gol
        rightPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // --- Panel log jos ---
        logArea = new JTextArea(5, 40);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.add(logScroll, BorderLayout.CENTER);

        // --- Adaugare în layout principal ---
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        // --- Actiuni butoane ---
        placeOrderBtn.addActionListener(e -> placeOrder());
        viewOrdersBtn.addActionListener(e -> refreshOrderTable());

        // --- Inițializari ---
        refreshClientEmails();
        refreshProductNames();
    }

    private void refreshClientEmails() {
        clientEmailCombo.removeAllItems();
        clientBLL.getAllClients().forEach(client ->
                clientEmailCombo.addItem(client.getEmail()));
    }

    private void refreshProductNames() {
        productNameCombo.removeAllItems();
        productBLL.getAllProducts().forEach(product ->
                productNameCombo.addItem(product.getName()));
    }

    private void refreshOrderTable() {
        List<Order> orders = orderBLL.getAllOrders();
        JTable newTable = tableGenerator.generateTable(orders);
        rightPanel.removeAll();
        rightPanel.add(new JScrollPane(newTable), BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
        orderTable = newTable;
        log("Displayed all orders.");
    }

    private void placeOrder() {
        try {
            String clientEmail = (String) clientEmailCombo.getSelectedItem();
            String productName = (String) productNameCombo.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());

            if (clientEmail == null || productName == null) {
                log("Please select both client and product");
                return;
            }

            if (quantity <= 0) {
                log("Quantity must be positive");
                return;
            }

            Order order = new Order(clientEmail, productName, quantity);
            orderBLL.placeOrder(order);

            log("Order placed successfully!");
            quantityField.setText("");
            refreshProductNames(); // Refresh stock
            refreshOrderTable();
        } catch (NumberFormatException e) {
            log("Invalid quantity");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }
}
