package ua.edu.nupp.kpp.server;

public class ConsumptionRecord {
    private Long id;
    private long measureId;
    private String energyType;
    private double amount;
    private String period;

    public ConsumptionRecord() {
    }

    public ConsumptionRecord(Long id,
                             long measureId,
                             String energyType,
                             double amount,
                             String period) {
        this.id = id;
        this.measureId = measureId;
        this.energyType = energyType;
        this.amount = amount;
        this.period = period;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMeasureId() {
        return measureId;
    }

    public void setMeasureId(long measureId) {
        this.measureId = measureId;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}