package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class AuditObjectService {
    private static final Logger LOG = Logger.getLogger(AuditObjectService.class.getName());
    private final AuditObjectDAO dao = new AuditObjectDAO();

    public List<AuditObject> getAll() throws SQLException {
        return dao.getAll();
    }

    public AuditObject getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(AuditObject o) throws SQLException {
        validate(o);
        dao.add(o);
        LOG.info("Додано об'єкт енергоаудиту");
    }

    public void update(AuditObject o) throws SQLException {
        validate(o);
        dao.update(o);
        LOG.info("Оновлено об'єкт енергоаудиту");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено об'єкт енергоаудиту");
    }

    static void validate(AuditObject o) {
        if (o == null || blank(o.getCode()) || blank(o.getName()) || blank(o.getAddress())) {
            throw new IllegalArgumentException("Усі поля об'єкта є обов'язковими");
        }
    }

    private static boolean blank(String v) {
        return v == null || v.isBlank();
    }
}