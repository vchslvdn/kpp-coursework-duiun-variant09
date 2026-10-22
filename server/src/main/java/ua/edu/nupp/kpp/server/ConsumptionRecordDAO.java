package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumptionRecordDAO {
    public void add(ConsumptionRecord rec) throws SQLException {
        String sql = "INSERT INTO consumption_record(measure_id, energy_type, amount, period) VALUES(?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, rec);
            p.executeUpdate();
        }
    }

    public List<ConsumptionRecord> getAll() throws SQLException {
        List<ConsumptionRecord> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM consumption_record ORDER BY id");
             ResultSet r = p.executeQuery()) {
            while (r.next()) list.add(map(r));
        }
        return list;
    }

    public ConsumptionRecord getById(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM consumption_record WHERE id=?")) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public void update(ConsumptionRecord rec) throws SQLException {
        String sql = "UPDATE consumption_record SET measure_id=?, energy_type=?, amount=?, period=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, rec);
            p.setLong(5, rec.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM consumption_record WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    private void bind(PreparedStatement p, ConsumptionRecord rec) throws SQLException {
        p.setLong(1, rec.getMeasureId());
        p.setString(2, rec.getEnergyType());
        p.setDouble(3, rec.getAmount());
        p.setString(4, rec.getPeriod());
    }

    private ConsumptionRecord map(ResultSet r) throws SQLException {
        return new ConsumptionRecord(r.getLong("id"),
                r.getLong("measure_id"),
                r.getString("energy_type"),
                r.getDouble("amount"),
                r.getString("period"));
    }
}