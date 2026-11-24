package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportConsumptionRecordsTask extends Task<Integer> {
    private static final Logger LOG = Logger.getLogger(ImportConsumptionRecordsTask.class.getName());
    private final Path file;
    private final HttpEnergyAuditClient api;

    public ImportConsumptionRecordsTask(Path file, HttpEnergyAuditClient api) {
        this.file = file;
        this.api = api;
    }

    @Override
    protected Integer call() throws Exception {
        LOG.info("Starting CSV import");
        updateMessage("Читання CSV-файла...");
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        String header = "object_id;energy_type;amount;period";

        if (lines.isEmpty() || !header.equals(lines.get(0).strip())) {
            throw new IllegalArgumentException("Invalid CSV header");
        }

        int total = (int) lines.stream().skip(1).filter(s -> !s.isBlank()).count();
        int processed = 0, imported = 0;
        updateProgress(0, Math.max(1, total));

        for (int i = 1; i < lines.size(); i++) {
            if (isCancelled()) break;
            if (lines.get(i).isBlank()) continue;

            String[] p = lines.get(i).split(";", -1);

            if (p.length != 4) {
                LOG.warning("Skipped line " + (i + 1));
            } else {
                try {
                    ConsumptionRecord r = new ConsumptionRecord(
                            null,
                            Long.parseLong(p[0].strip()),
                            p[1].strip(),
                            Double.parseDouble(p[2].strip()),
                            p[3].strip()
                    );

                    if (!Double.isFinite(r.getAmount())) {
                        LOG.warning("NaN or Infinity at line " + (i + 1));
                        throw new IllegalArgumentException("Invalid numeric value at line " + (i + 1));
                    }

                    try {
                        api.addRecord(r);
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "HTTP error during import of row " + (i + 1), e);
                        throw e;
                    }

                    imported++;
                } catch (NumberFormatException e) {
                    LOG.warning("Invalid number at line " + (i + 1));
                    throw e;
                }
            }
            updateProgress(++processed, Math.max(1, total));
            updateMessage("Опрацьовано " + processed + " із " + total + "; імпортовано " + imported);
        }

        if (total == 0) updateProgress(1, 1);
        LOG.info("Finishing import...");
        LOG.info("Imported: " + imported);
        return imported;
    }
}