package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class EnergyAuditService {
    private static final Logger LOG = Logger.getLogger(EnergyAuditService.class.getName());
    private final EnergyAuditDAO dao = new EnergyAuditDAO();
    private final AuditObjectDAO objectDAO = new AuditObjectDAO();

    public List<EnergyAudit> getAll() throws SQLException {
        return dao.getAll();
    }

    public EnergyAudit getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(EnergyAudit a) throws SQLException {
        validate(a);
        requireObject(a.getObjectId());
        dao.add(a);
        LOG.info("Додано енергоаудит");
    }

    public void update(EnergyAudit a) throws SQLException {
        validate(a);
        requireObject(a.getObjectId());
        dao.update(a);
        LOG.info("Оновлено енергоаудит");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено енергоаудит");
    }

    static void validate(EnergyAudit a) {
        if (a == null || a.getObjectId() <= 0
                || a.getAuditDate() == null || a.getAuditDate().isBlank()
                || a.getAuditorName() == null || a.getAuditorName().isBlank()) {
            throw new IllegalArgumentException("Некоректні основні дані енергоаудиту");
        }
        if (a.getActualConsumption() < 0 || a.getNormConsumption() <= 0) {
            throw new IllegalArgumentException("Некоректні числові дані споживання");
        }
        if (a.getPaybackScore() < 0 || a.getPaybackScore() > 100
                || a.getEmissionScore() < 0 || a.getEmissionScore() > 100) {
            throw new IllegalArgumentException("F2 і F3 мають бути в діапазоні 0..100");
        }
    }

    private void requireObject(long id) throws SQLException {
        if (objectDAO.getById(id) == null) {
            throw new IllegalArgumentException("Об'єкт енергоаудиту не знайдено");
        }
    }
}