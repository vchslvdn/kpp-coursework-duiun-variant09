package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditObjectDAO {
    public void add(AuditObject o) throws SQLException {
        String sql = "INSERT INTO audit_object(code, name, address) VALUES(?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, o.getCode());
            p.setString(2, o.getName());
            p.setString(3, o.getAddress());
            p.executeUpdate();
        }
    }

    public List<AuditObject> getAll() throws SQLException {
        List<AuditObject> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM audit_object ORDER BY id");
             ResultSet r = p.executeQuery()) {
            while (r.next()) list.add(map(r));
        }
        return list;
    }

    public AuditObject getById(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM audit_object WHERE id=?")) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public void update(AuditObject o) throws SQLException {
        String sql = "UPDATE audit_object SET code=?, name=?, address=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, o.getCode());
            p.setString(2, o.getName());
            p.setString(3, o.getAddress());
            p.setLong(4, o.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM audit_object WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    private AuditObject map(ResultSet r) throws SQLException {
        return new AuditObject(r.getLong("id"),
                r.getString("code"),
                r.getString("name"),
                r.getString("address"));
    }
}