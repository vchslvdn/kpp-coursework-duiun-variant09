package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseworkTest {

    @Test
    void factor1WithoutExceedanceIsZero() {
        assertEquals(0.0, PriorityCalculator.factor1(20, 25), 0.001);
    }

    @Test
    void factor1CappedAt100ForExtremeExceedance() {
        assertEquals(100.0, PriorityCalculator.factor1(300, 100), 0.001);
    }

    @Test
    void integralPriorityCalculatedCorrectly() {
        assertEquals(59.3, PriorityCalculator.calculate(52, 60, 70), 0.001);
    }

    @Test
    void calculatorRejectsFactorAbove100() {
        assertThrows(IllegalArgumentException.class,
                () -> PriorityCalculator.calculate(101, 10, 10));
    }

    @Test
    void proposedToApprovedAllowed() {
        assertTrue(MeasureService.isAllowedTransition(MeasureState.PROPOSED, MeasureState.APPROVED));
    }

    @Test
    void approvedToImplementationAllowed() {
        assertTrue(MeasureService.isAllowedTransition(MeasureState.APPROVED, MeasureState.IMPLEMENTATION));
    }

    @Test
    void implementationToCompletedAllowed() {
        assertTrue(MeasureService.isAllowedTransition(MeasureState.IMPLEMENTATION, MeasureState.COMPLETED));
    }

    @Test
    void jumpingOverStatesIsRejected() {
        assertFalse(MeasureService.isAllowedTransition(MeasureState.PROPOSED, MeasureState.COMPLETED));
    }

    @Test
    void rollbackToPreviousStateIsRejected() {
        assertFalse(MeasureService.isAllowedTransition(MeasureState.COMPLETED, MeasureState.PROPOSED));
    }

    @Test
    void auditObjectRejectsEmptyCodeString() {
        assertThrows(IllegalArgumentException.class,
                () -> AuditObjectService.validate(new AuditObject(null, "", "Гуртожиток 5 НУПП", "м. Полтава, вул. Інститутський проріз, 24")));
    }

    @Test
    void energyAuditRejectsOutOfRangeScore() {
        assertThrows(IllegalArgumentException.class,
                () -> EnergyAuditService.validate(new EnergyAudit(
                        null, 1L, "2026-10-15", "Смарагдікова П.І.", 150.0, 100.0, 120.0, 50.0)));
    }

    @Test
    void consumptionRecordRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> ConsumptionRecordService.validate(new ConsumptionRecord(
                        null, 1L, "Електроенергія", -50.0, "2026-04")));
    }
}