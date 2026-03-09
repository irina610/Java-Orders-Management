package dataAccess;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    private final Class<T> type;

    public GenericDAO(Class<T> type) {
        this.type = type;
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        String query = "SELECT * FROM " + type.getSimpleName();

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(instance, rs.getObject(field.getName()));
                }
                list.add(instance);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding all " + type.getSimpleName(), e);
        }
        return list;
    }

    public void insert(T t) {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            fields.append(field.getName()).append(",");
            values.append("?,");
        }

        fields.setLength(fields.length() - 1);
        values.setLength(values.length() - 1);

        String query = "INSERT INTO " + type.getSimpleName() + " (" + fields + ") VALUES (" + values + ")";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            int index = 1;
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                stmt.setObject(index++, field.get(t));
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Field idField = type.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(t, generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error inserting " + type.getSimpleName(), e);
        }
    }

    public void update(T t, String fieldName, Object value) {
        StringBuilder setClause = new StringBuilder();

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            setClause.append(field.getName()).append("=?,");
        }

        setClause.setLength(setClause.length() - 1);
        String query = "UPDATE " + type.getSimpleName() + " SET " + setClause + " WHERE " + fieldName + "=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int index = 1;
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                stmt.setObject(index++, field.get(t));
            }

            stmt.setObject(index, value);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error updating " + type.getSimpleName(), e);
        }
    }

    public void delete(String fieldName, Object value) {
        String query = "DELETE FROM " + type.getSimpleName() + " WHERE " + fieldName + "=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, value);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting " + type.getSimpleName(), e);
        }
    }
}