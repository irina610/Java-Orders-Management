package presentation;

import business.ClientBLL;
import model.Client;
import util.TableGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientView extends JPanel {
    private final ClientBLL clientBLL;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JComboBox<Client> clientComboBox;
    private final JTextArea logArea;
    private final JTable clientTable;
    private final TableGenerator<Client> tableGenerator;

    public ClientView() {
        clientBLL = new ClientBLL();
        tableGenerator = new TableGenerator<>();
        setLayout(new BorderLayout());

        // --- Panel pentru inputurile Name si Email ---
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField(15);
        inputPanel.add(emailField);

        // --- Panel pentru Select Client (ComboBox) ---
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clientComboBox = new JComboBox<>();
        comboPanel.add(new JLabel("Select Client:"));
        comboPanel.add(clientComboBox);

        // --- Panel de sus: input + combo ---
        JPanel topLeftPanel = new JPanel(new BorderLayout());
        topLeftPanel.add(inputPanel, BorderLayout.NORTH);
        topLeftPanel.add(comboPanel, BorderLayout.CENTER);

        // --- Panel pentru butoane ---
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Client");
        JButton updateButton = new JButton("Update Client");
        JButton deleteButton = new JButton("Delete Client");
        JButton viewButton = new JButton("View All Clients");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // --- Left Panel complet: sus input+combo, jos butoane ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(topLeftPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Right Panel pentru tabel (cu TableGenerator) ---
        clientTable = new JTable();  // tabel gol inițial
        JScrollPane tableScroll = new JScrollPane(clientTable);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        // --- Bottom Panel pentru log ---
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
            String name = nameField.getText();
            String email = emailField.getText();
            Client newClient = new Client(name, email);
            clientBLL.addClient(newClient);
            log("Added client: " + name);
            refreshComboBox();
        });

        updateButton.addActionListener(e -> {
            Client selected = (Client) clientComboBox.getSelectedItem();
            if (selected != null) {
                selected.setName(nameField.getText());
                selected.setEmail(emailField.getText());
                clientBLL.updateClient(selected);
                log("Updated client: " + selected);
                refreshComboBox();
            }
        });

        deleteButton.addActionListener(e -> {
            Client selected = (Client) clientComboBox.getSelectedItem();
            if (selected != null) {
                clientBLL.deleteClient(selected.getId());
                log("Deleted client: " + selected);
                refreshComboBox();
            }
        });

        viewButton.addActionListener(e -> {
            List<Client> clients = clientBLL.getAllClients();
            JTable newTable = tableGenerator.generateTable(clients);
            replaceTable(newTable);
            log("Displayed all clients.");
        });

        refreshComboBox();
    }

    private void refreshComboBox() {
        clientComboBox.removeAllItems();
        List<Client> clients = clientBLL.getAllClients();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }
    }

    private void replaceTable(JTable newTable) {
        JScrollPane newScrollPane = new JScrollPane(newTable);
        Component[] components = getComponents();
        for (Component c : components) {
            if (c instanceof JPanel panel && panel.getLayout() instanceof BorderLayout) {
                if (panel.getComponent(0) instanceof JScrollPane) {
                    panel.remove(0);
                    panel.add(newScrollPane, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                    break;
                }
            }
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }
}
