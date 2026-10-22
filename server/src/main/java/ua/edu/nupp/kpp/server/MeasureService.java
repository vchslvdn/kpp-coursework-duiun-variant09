package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class MeasureService {
    private static final Logger LOG = Logger.getLogger(MeasureService.class.getName());
    private final MeasureDAO dao = new MeasureDAO();
    private final EnergyAuditDAO auditDAO = new EnergyAuditDAO();

    public List<Measure> getAll() throws SQLException {
        return dao.getAll();
    }

    public Measure getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(long auditId) throws SQLException {
        dao.add(fromAudit(null, auditId, MeasureState.PROPOSED));
        LOG.info("Створено захід енергозбереження");
    }

    public void update(long id, long auditId) throws SQLException {
        Measure old = dao.getById(id);
        if (old == null) {
            throw new IllegalArgumentException("Захід не знайдено");
        }
        dao.update(fromAudit(id, auditId, old.getState()));
        LOG.info("Оновлено захід енергозбереження");
    }

    public void changeState(long id, MeasureState next) throws SQLException {
        Measure current = dao.getById(id);
        if (current == null) {
            throw new IllegalArgumentException("Захід не знайдено");
        }
        if (!isAllowedTransition(current.getState(), next)) {
            LOG.warning("Недопустимий перехід стану: " + current.getState() + " -> " + next);
            throw new IllegalArgumentException("Недопустимий перехід стану");
        }
        dao.updateState(id, next);
        LOG.info("Змінено стан заходу: " + current.getState() + " -> " + next);
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено захід енергозбереження");
    }

    public static boolean isAllowedTransition(MeasureState from, MeasureState to) {
        return (from == MeasureState.PROPOSED && to == MeasureState.APPROVED)
                || (from == MeasureState.APPROVED && to == MeasureState.IMPLEMENTATION)
                || (from == MeasureState.IMPLEMENTATION && to == MeasureState.COMPLETED);
    }

    private Measure fromAudit(Long id, long auditId, MeasureState state) throws SQLException {
        EnergyAudit a = auditDAO.getById(auditId);
        if (a == null) {
            throw new IllegalArgumentException("Енергоаудит не знайдено");
        }
        double f1 = PriorityCalculator.factor1(a.getActualConsumption(), a.getNormConsumption());
        double f2 = a.getPaybackScore();
        double f3 = a.getEmissionScore();
        return new Measure(id, auditId, state, f1, f2, f3, PriorityCalculator.calculate(f1, f2, f3));
    }
}