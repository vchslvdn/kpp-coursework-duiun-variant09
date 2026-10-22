package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeasureDAO {
    public void add(Measure m) throws SQLException {
        String sql = "INSERT INTO measure(audit_id, state, f1, f2, f3, priority_index) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, m);
            p.executeUpdate();
        }
    }

    public List<Measure> getAll() throws SQLException {
        List<Measure> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM measure ORDER BY id");
             ResultSet r = p.executeQuery()) {
            while (r.next()) list.add(map(r));
        }
        return list;
    }

    public Measure getById(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM measure WHERE id=?")) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public void update(Measure m) throws SQLException {
        String sql = "UPDATE measure SET audit_id=?, state=?, f1=?, f2=?, f3=?, priority_index=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, m);
            p.setLong(7, m.getId());
            p.executeUpdate();
        }
    }

    public void updateState(long id, MeasureState state) throws SQLException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("UPDATE measure SET state=? WHERE id=?")) {
            p.setString(1, state.name());
            p.setLong(2, id);
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM measure WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    private void bind(PreparedStatement p, Measure m) throws SQLException {
        p.setLong(1, m.getAuditId());
        p.setString(2, m.getState().name());
        p.setDouble(3, m.getF1());
        p.setDouble(4, m.getF2());
        p.setDouble(5, m.getF3());
        p.setDouble(6, m.getPriorityIndex());
    }

    private Measure map(ResultSet r) throws SQLException {
        return new Measure(r.getLong("id"),
                r.getLong("audit_id"),
                MeasureState.valueOf(r.getString("state")),
                r.getDouble("f1"),
                r.getDouble("f2"),
                r.getDouble("f3"),
                r.getDouble("priority_index"));
    }
}