package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ConsumptionRecordService {
    private static final Logger LOG = Logger.getLogger(ConsumptionRecordService.class.getName());
    private final ConsumptionRecordDAO dao = new ConsumptionRecordDAO();
    private final MeasureDAO measureDAO = new MeasureDAO();

    public List<ConsumptionRecord> getAll() throws SQLException {
        return dao.getAll();
    }

    public ConsumptionRecord getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(ConsumptionRecord r) throws SQLException {
        validate(r);
        requireMeasure(r.getMeasureId());
        dao.add(r);
        LOG.info("Додано запис споживання");
    }

    public void update(ConsumptionRecord r) throws SQLException {
        validate(r);
        requireMeasure(r.getMeasureId());
        dao.update(r);
        LOG.info("Оновлено запис споживання");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено запис споживання");
    }

    static void validate(ConsumptionRecord r) {
        if (r == null || r.getMeasureId() <= 0
                || r.getEnergyType() == null || r.getEnergyType().isBlank()
                || r.getPeriod() == null || r.getPeriod().isBlank()
                || r.getAmount() < 0) {
            throw new IllegalArgumentException("Некоректні дані запису споживання");
        }
    }

    private void requireMeasure(long id) throws SQLException {
        if (measureDAO.getById(id) == null) {
            throw new IllegalArgumentException("Захід не знайдено");
        }
    }
}