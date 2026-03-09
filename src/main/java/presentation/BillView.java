package presentation;

import business.BillBLL;
import business.ClientBLL;
import model.Bill;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillView extends JPanel {
    private final BillBLL billBLL;
    private final ClientBLL clientBLL;
    private final JComboBox<String> clientEmailCombo;
    private final JTable billTable;
    private final DefaultTableModel tableModel;
    private final JTextArea billDetailsArea;

    public BillView() {
        billBLL = new BillBLL();
        clientBLL = new ClientBLL();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel pentru selectare client
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clientEmailCombo = new JComboBox<>();
        JButton viewBillsButton = new JButton("Afiseaza Facturi");
        selectionPanel.add(new JLabel("Selecteaza Email Client:"));
        selectionPanel.add(clientEmailCombo);
        selectionPanel.add(viewBillsButton);

        // Tabel pentru facturi
        tableModel = new DefaultTableModel(new Object[]{"ID", "Produs", "Cantitate", "Total", "Data"}, 0);
        billTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(billTable);

        // Zona de detalii factură
        billDetailsArea = new JTextArea(10, 50);
        billDetailsArea.setEditable(false);
        billDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailsScroll = new JScrollPane(billDetailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Detalii Factura"));

        // Layout
        add(selectionPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(detailsScroll, BorderLayout.SOUTH);

        // Incarcare emailuri clienti
        loadClientEmails();

        // Evenimente
        viewBillsButton.addActionListener(e -> showClientBills());
    }

    private void loadClientEmails() {
        clientEmailCombo.removeAllItems();
        clientBLL.getAllClients().stream()
                .map(Client::getEmail)
                .forEach(clientEmailCombo::addItem);
    }

    private void showClientBills() {
        String clientEmail = (String) clientEmailCombo.getSelectedItem();
        if (clientEmail != null && !clientEmail.isEmpty()) {
            tableModel.setRowCount(0);
            List<Bill> bills = billBLL.findByClientEmail(clientEmail);

            if (bills.isEmpty()) {
                billDetailsArea.setText("Clientul " + clientEmail + " nu are nicio factura.");
            } else {
                bills.forEach(bill -> tableModel.addRow(new Object[]{
                        bill.id(),
                        bill.productName(),
                        bill.quantity(),
                        String.format("%.2f RON", bill.totalPrice()),
                        bill.timestamp().toString()
                }));
            }
        }
    }

}

