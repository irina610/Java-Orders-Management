package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.List;

public class TableGenerator<T> {
    public JTable generateTable(List<T> data) {
        if (data == null || data.isEmpty()) return new JTable();

        Class<?> clazz = data.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        String[] columnNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            columnNames[i] = fields[i].getName();
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (T item : data) {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    row[i] = fields[i].get(item);
                } catch (IllegalAccessException e) {
                    row[i] = "N/A";
                }
            }
            model.addRow(row);
        }

        return new JTable(model);
    }
}
