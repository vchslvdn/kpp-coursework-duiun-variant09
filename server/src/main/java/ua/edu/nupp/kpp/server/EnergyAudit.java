package ua.edu.nupp.kpp.server;

public class EnergyAudit {
    private Long id;
    private long objectId;
    private String auditDate;
    private String auditorName;
    private double actualConsumption;
    private double normConsumption;
    private double paybackScore;
    private double emissionScore;

    public EnergyAudit() {
    }

    public EnergyAudit(Long id,
                       long objectId,
                       String auditDate,
                       String auditorName,
                       double actualConsumption,
                       double normConsumption,
                       double paybackScore,
                       double emissionScore) {
        this.id = id;
        this.objectId = objectId;
        this.auditDate = auditDate;
        this.auditorName = auditorName;
        this.actualConsumption = actualConsumption;
        this.normConsumption = normConsumption;
        this.paybackScore = paybackScore;
        this.emissionScore = emissionScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public double getActualConsumption() {
        return actualConsumption;
    }

    public void setActualConsumption(double actualConsumption) {
        this.actualConsumption = actualConsumption;
    }

    public double getNormConsumption() {
        return normConsumption;
    }

    public void setNormConsumption(double normConsumption) {
        this.normConsumption = normConsumption;
    }

    public double getPaybackScore() {
        return paybackScore;
    }

    public void setPaybackScore(double paybackScore) {
        this.paybackScore = paybackScore;
    }

    public double getEmissionScore() {
        return emissionScore;
    }

    public void setEmissionScore(double emissionScore) {
        this.emissionScore = emissionScore;
    }
}