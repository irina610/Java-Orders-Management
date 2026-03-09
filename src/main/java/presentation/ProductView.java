package presentation;

import business.ProductBLL;
import model.Product;
import util.TableGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductView extends JPanel {
    private final ProductBLL productBLL;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField stockField;
    private final JComboBox<Product> productComboBox;
    private final JTextArea logArea;

    private final JPanel rightPanel;
    private JTable productTable;
    private final TableGenerator<Product> tableGenerator;

    public ProductView() {
        productBLL = new ProductBLL();
        tableGenerator = new TableGenerator<>();
        setLayout(new BorderLayout());

        // --- Panel pentru inputuri: Name, Price, Stock ---
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField(15);
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Stock:"));
        stockField = new JTextField(15);
        inputPanel.add(stockField);

        // --- Panel pentru ComboBox ---
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productComboBox = new JComboBox<>();
        comboPanel.add(new JLabel("Select Product:"));
        comboPanel.add(productComboBox);

        // --- Panel stanga sus ---
        JPanel topLeftPanel = new JPanel(new BorderLayout());
        topLeftPanel.add(inputPanel, BorderLayout.NORTH);
        topLeftPanel.add(comboPanel, BorderLayout.CENTER);

        // --- Panel pentru butoane ---
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton viewButton = new JButton("View All Products");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // --- Panel stanga complet ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(topLeftPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Panel pentru tabel (dreapta) ---
        rightPanel = new JPanel(new BorderLayout());
        productTable = new JTable(); // inițial gol
        rightPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

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
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                float price = Float.parseFloat(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                Product newProduct = new Product(name, stock, price);
                productBLL.addProduct(newProduct);
                log("Added product: " + name);
                refreshComboBox();
            } catch (Exception ex) {
                log("Error adding product: " + ex.getMessage());
            }
        });

        updateButton.addActionListener(e -> {
            Product selected = (Product) productComboBox.getSelectedItem();
            if (selected != null) {
                try {
                    selected.setName(nameField.getText());
                    selected.setPrice(Float.parseFloat(priceField.getText()));
                    selected.setStock(Integer.parseInt(stockField.getText()));
                    productBLL.updateProduct(selected);
                    log("Updated product: " + selected);
                    refreshComboBox();
                } catch (Exception ex) {
                    log("Error updating product: " + ex.getMessage());
                }
            }
        });

        deleteButton.addActionListener(e -> {
            Product selected = (Product) productComboBox.getSelectedItem();
            if (selected != null) {
                productBLL.deleteProduct(selected.getId());
                log("Deleted product: " + selected);
                refreshComboBox();
            }
        });

        viewButton.addActionListener(e -> {
            List<Product> products = productBLL.getAllProducts();
            JTable newTable = tableGenerator.generateTable(products);
            rightPanel.removeAll();
            rightPanel.add(new JScrollPane(newTable), BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();
            log("Displayed all products.");
        });

        // --- Inițializare combo box ---
        refreshComboBox();
    }

    private void refreshComboBox() {
        productComboBox.removeAllItems();
        List<Product> products = productBLL.getAllProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }
}
