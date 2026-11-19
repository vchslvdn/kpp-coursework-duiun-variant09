package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HttpEnergyAuditClient {
    private static final String BASE = "http://localhost:8080/energy-audit-server";
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<AuditObject> getObjects() {
        return Arrays.asList(read(get("/objects"), AuditObject[].class));
    }

    public void addObject(AuditObject o) {
        post("/objects", "action=add&code=" + enc(o.getCode()) + "&name=" + enc(o.getName()) + "&address=" + enc(o.getAddress()));
    }

    public void updateObject(AuditObject o) {
        post("/objects", "action=update&id=" + o.getId() + "&code=" + enc(o.getCode()) + "&name=" + enc(o.getName()) + "&address=" + enc(o.getAddress()));
    }

    public void deleteObject(long id) {
        post("/objects", "action=delete&id=" + id);
    }

    public List<EnergyAudit> getAudits() {
        return Arrays.asList(read(get("/audits"), EnergyAudit[].class));
    }

    public void addAudit(EnergyAudit a) {
        post("/audits", auditForm("add", a));
    }

    public void updateAudit(EnergyAudit a) {
        post("/audits", auditForm("update", a) + "&id=" + a.getId());
    }

    public void deleteAudit(long id) {
        post("/audits", "action=delete&id=" + id);
    }

    public List<Measure> getMeasures() {
        return Arrays.asList(read(get("/measures"), Measure[].class));
    }

    public void addMeasure(long auditId) {
        post("/measures", "action=add&auditId=" + auditId);
    }

    public void updateMeasure(long id, long auditId) {
        post("/measures", "action=update&id=" + id + "&auditId=" + auditId);
    }

    public void changeMeasureState(long id, MeasureState state) {
        post("/measures", "action=transition&id=" + id + "&state=" + state.name());
    }

    public void deleteMeasure(long id) {
        post("/measures", "action=delete&id=" + id);
    }

    public List<ConsumptionRecord> getRecords() {
        return Arrays.asList(read(get("/records"), ConsumptionRecord[].class));
    }

    public void addRecord(ConsumptionRecord r) {
        post("/records", recordForm("add", r));
    }

    public void updateRecord(ConsumptionRecord r) {
        post("/records", recordForm("update", r) + "&id=" + r.getId());
    }

    public void deleteRecord(long id) {
        post("/records", "action=delete&id=" + id);
    }

    private String auditForm(String action, EnergyAudit a) {
        return "action=" + action + "&objectId=" + a.getObjectId() +
                "&auditDate=" + enc(a.getAuditDate()) +
                "&auditorName=" + enc(a.getAuditorName()) +
                "&actualConsumption=" + a.getActualConsumption() +
                "&normConsumption=" + a.getNormConsumption() +
                "&paybackScore=" + a.getPaybackScore() +
                "&emissionScore=" + a.getEmissionScore();
    }

    private String recordForm(String action, ConsumptionRecord r) {
        return "action=" + action +
                "&measureId=" + r.getMeasureId() +
                "&energyType=" + enc(r.getEnergyType()) +
                "&amount=" + r.getAmount() +
                "&period=" + enc(r.getPeriod());
    }

    private String get(String path) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + path)).GET().build();
        return send(req);
    }

    private String post(String path, String form) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + path))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
        return send(req);
    }

    private String send(HttpRequest req) {
        try {
            HttpResponse<String> r = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (r.statusCode() >= 400) {
                throw new RuntimeException(r.body());
            }
            return r.body();
        } catch (Exception e) {
            throw new RuntimeException("HTTP error: " + e.getMessage(), e);
        }
    }

    private <T> T read(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("JSON error", e);
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}