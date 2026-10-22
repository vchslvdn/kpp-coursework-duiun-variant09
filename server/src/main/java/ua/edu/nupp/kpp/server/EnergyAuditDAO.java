package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnergyAuditDAO {
    public void add(EnergyAudit a) throws SQLException {
        String sql = "INSERT INTO energy_audit(object_id, audit_date, auditor_name, actual_consumption, norm_consumption, payback_score, emission_score) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, a);
            p.executeUpdate();
        }
    }

    public List<EnergyAudit> getAll() throws SQLException {
        List<EnergyAudit> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM energy_audit ORDER BY id");
             ResultSet r = p.executeQuery()) {
            while (r.next()) list.add(map(r));
        }
        return list;
    }

    public EnergyAudit getById(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM energy_audit WHERE id=?")) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public void update(EnergyAudit a) throws SQLException {
        String sql = "UPDATE energy_audit SET object_id=?, audit_date=?, auditor_name=?, actual_consumption=?, norm_consumption=?, payback_score=?, emission_score=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, a);
            p.setLong(8, a.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM energy_audit WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    private void bind(PreparedStatement p, EnergyAudit a) throws SQLException {
        p.setLong(1, a.getObjectId());
        p.setString(2, a.getAuditDate());
        p.setString(3, a.getAuditorName());
        p.setDouble(4, a.getActualConsumption());
        p.setDouble(5, a.getNormConsumption());
        p.setDouble(6, a.getPaybackScore());
        p.setDouble(7, a.getEmissionScore());
    }

    private EnergyAudit map(ResultSet r) throws SQLException {
        return new EnergyAudit(r.getLong("id"),
                r.getLong("object_id"),
                r.getString("audit_date"),
                r.getString("auditor_name"),
                r.getDouble("actual_consumption"),
                r.getDouble("norm_consumption"),
                r.getDouble("payback_score"),
                r.getDouble("emission_score"));
    }
}