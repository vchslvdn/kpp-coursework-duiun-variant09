package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class MainApp extends Application {
    private static final Logger LOG = Logger.getLogger(MainApp.class.getName());
    private final HttpEnergyAuditClient api = new HttpEnergyAuditClient();

    private final TableView<AuditObject> objects = new TableView<>();
    private final TableView<EnergyAudit> audits = new TableView<>();
    private final TableView<Measure> measures = new TableView<>();
    private final TableView<ConsumptionRecord> records = new TableView<>();

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane(
                new Tab("Об'єкти", objectPane()),
                new Tab("Енергоаудити", auditPane()),
                new Tab("Заходи", measurePane()),
                new Tab("Споживання", recordPane())
        );
        tabs.getTabs().forEach(t -> t.setClosable(false));
        stage.setScene(new Scene(tabs, 1050, 650));
        stage.setTitle("Система управління енергоаудитом");
        stage.show();
        refreshAll();
    }

    private Pane objectPane() {
        objects.getColumns().setAll(
                col("ID", "id"),
                col("Код", "code"),
                col("Назва", "name"),
                col("Адреса", "address")
        );
        TextField code = new TextField(), name = new TextField(), address = new TextField();
        Button add = new Button("Додати"), update = new Button("Змінити"), delete = new Button("Видалити"), refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addObject(new AuditObject(null, code.getText(), name.getText(), address.getText()));
            refreshObjects();
        }));
        update.setOnAction(e -> safe(() -> {
            AuditObject o = selected(objects);
            o.setCode(code.getText());
            o.setName(name.getText());
            o.setAddress(address.getText());
            api.updateObject(o);
            refreshObjects();
        }));
        delete.setOnAction(e -> safe(() -> {
            api.deleteObject(selected(objects).getId());
            refreshAll();
        }));
        refresh.setOnAction(e -> refreshObjects());

        objects.getSelectionModel().selectedItemProperty().addListener((obs, oldV, o) -> {
            if (o != null) {
                code.setText(o.getCode());
                name.setText(o.getName());
                address.setText(o.getAddress());
            }
        });

        return layout(objects, new VBox(8,
                new Label("Код"), code,
                new Label("Назва"), name,
                new Label("Адреса"), address,
                new HBox(8, add, update, delete, refresh)
        ));
    }

    private Pane auditPane() {
        audits.getColumns().setAll(
                col("ID", "id"),
                col("Object ID", "objectId"),
                col("Дата", "auditDate"),
                col("Аудитор", "auditorName"),
                col("Фактичне значення", "actualConsumption"),
                col("Нормативне значення", "normConsumption"),
                col("F2 (привабливість строку окупності)", "paybackScore"),
                col("F3 (потенціал скорочення викидів)", "emissionScore")
        );
        TextField objectId = new TextField(), auditDate = new TextField(), auditorName = new TextField(),
                actual = new TextField(), norm = new TextField(), f2 = new TextField(), f3 = new TextField();
        Button add = new Button("Додати"), update = new Button("Змінити"), delete = new Button("Видалити"), refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addAudit(readAudit(null, objectId, auditDate, auditorName, actual, norm, f2, f3));
            refreshAudits();
        }));
        update.setOnAction(e -> safe(() -> {
            EnergyAudit a = selected(audits);
            api.updateAudit(readAudit(a.getId(), objectId, auditDate, auditorName, actual, norm, f2, f3));
            refreshAudits();
        }));
        delete.setOnAction(e -> safe(() -> {
            api.deleteAudit(selected(audits).getId());
            refreshAll();
        }));
        refresh.setOnAction(e -> refreshAudits());

        audits.getSelectionModel().selectedItemProperty().addListener((obs, oldV, a) -> {
            if (a != null) {
                objectId.setText("" + a.getObjectId());
                auditDate.setText(a.getAuditDate());
                auditorName.setText(a.getAuditorName());
                actual.setText("" + a.getActualConsumption());
                norm.setText("" + a.getNormConsumption());
                f2.setText("" + a.getPaybackScore());
                f3.setText("" + a.getEmissionScore());
            }
        });

        VBox form = new VBox(6,
                new Label("ID об'єкта"), objectId,
                new Label("Дата (YYYY-MM-DD)"), auditDate,
                new Label("Аудитор"), auditorName,
                new Label("Фактичне споживання"), actual,
                new Label("Нормативне споживання"), norm,
                new Label("F2 (привабливість строку окупності)"), f2,
                new Label("F3 (потенціал скорочення викидів)"), f3,
                new HBox(8, add, update, delete, refresh)
        );
        return layout(audits, form);
    }

    private Pane measurePane() {
        measures.getColumns().setAll(
                col("ID", "id"),
                col("Audit ID", "auditId"),
                col("Стан", "state"),
                col("F1", "f1"),
                col("F2", "f2"),
                col("F3", "f3"),
                col("I (індекс пріоритету)", "priorityIndex")
        );
        TextField auditId = new TextField();
        ComboBox<MeasureState> state = new ComboBox<>(FXCollections.observableArrayList(MeasureState.values()));
        Button add = new Button("Створити"), update = new Button("Перерахувати"), change = new Button("Змінити стан"),
                delete = new Button("Видалити"), refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addMeasure(Long.parseLong(auditId.getText()));
            refreshMeasures();
        }));
        update.setOnAction(e -> safe(() -> {
            api.updateMeasure(selected(measures).getId(), Long.parseLong(auditId.getText()));
            refreshMeasures();
        }));
        change.setOnAction(e -> safe(() -> {
            api.changeMeasureState(selected(measures).getId(), state.getValue());
            refreshMeasures();
        }));
        delete.setOnAction(e -> safe(() -> {
            api.deleteMeasure(selected(measures).getId());
            refreshAll();
        }));
        refresh.setOnAction(e -> refreshMeasures());

        measures.getSelectionModel().selectedItemProperty().addListener((obs, oldV, m) -> {
            if (m != null) {
                auditId.setText("" + m.getAuditId());
                state.setValue(m.getState());
            }
        });

        return layout(measures, new VBox(8,
                new Label("ID енергоаудиту"), auditId,
                new Label("Новий стан"), state,
                new HBox(8, add, update, change, delete, refresh)
        ));
    }

    private Pane recordPane() {
        records.getColumns().setAll(
                col("ID", "id"),
                col("Measure ID", "measureId"),
                col("Тип енергії", "energyType"),
                col("Обсяг", "amount"),
                col("Період", "period")
        );
        TextField measureId = new TextField(), energyType = new TextField(), amount = new TextField(), period = new TextField();
        Button add = new Button("Додати"), update = new Button("Змінити"), delete = new Button("Видалити"), refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addRecord(new ConsumptionRecord(null, Long.parseLong(measureId.getText()), energyType.getText(), Double.parseDouble(amount.getText()), period.getText()));
            refreshRecords();
        }));
        update.setOnAction(e -> safe(() -> {
            ConsumptionRecord r = selected(records);
            api.updateRecord(new ConsumptionRecord(r.getId(), Long.parseLong(measureId.getText()), energyType.getText(), Double.parseDouble(amount.getText()), period.getText()));
            refreshRecords();
        }));
        delete.setOnAction(e -> safe(() -> {
            api.deleteRecord(selected(records).getId());
            refreshRecords();
        }));
        refresh.setOnAction(e -> refreshRecords());

        records.getSelectionModel().selectedItemProperty().addListener((obs, oldV, r) -> {
            if (r != null) {
                measureId.setText("" + r.getMeasureId());
                energyType.setText(r.getEnergyType());
                amount.setText("" + r.getAmount());
                period.setText(r.getPeriod());
            }
        });

        return layout(records, new VBox(8,
                new Label("ID заходу"), measureId,
                new Label("Тип енергії"), energyType,
                new Label("Обсяг"), amount,
                new Label("Період (YYYY-MM)"), period,
                new HBox(8, add, update, delete, refresh)
        ));
    }

    private EnergyAudit readAudit(Long id, TextField objectId, TextField auditDate, TextField auditorName,
                                  TextField actual, TextField norm, TextField f2, TextField f3) {
        return new EnergyAudit(
                id,
                Long.parseLong(objectId.getText()),
                auditDate.getText(),
                auditorName.getText(),
                Double.parseDouble(actual.getText()),
                Double.parseDouble(norm.getText()),
                Double.parseDouble(f2.getText()),
                Double.parseDouble(f3.getText())
        );
    }

    private void refreshAll() {
        refreshObjects();
        refreshAudits();
        refreshMeasures();
        refreshRecords();
    }

    private void refreshObjects() {
        safe(() -> objects.setItems(FXCollections.observableArrayList(api.getObjects())));
    }

    private void refreshAudits() {
        safe(() -> audits.setItems(FXCollections.observableArrayList(api.getAudits())));
    }

    private void refreshMeasures() {
        safe(() -> measures.setItems(FXCollections.observableArrayList(api.getMeasures())));
    }

    private void refreshRecords() {
        safe(() -> records.setItems(FXCollections.observableArrayList(api.getRecords())));
    }

    private <T> T selected(TableView<T> table) {
        T v = table.getSelectionModel().getSelectedItem();
        if (v == null) throw new IllegalArgumentException("Виберіть запис");
        return v;
    }

    private void safe(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
            showError(e);
        }
    }

    private void showError(Throwable e) {
        javafx.scene.control.Alert d = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR,
                e == null ? "Помилка" : e.getMessage(),
                ButtonType.OK
        );
        d.showAndWait();
    }

    private Pane layout(TableView<?> table, Pane form) {
        SplitPane p = new SplitPane(table, form);
        p.setDividerPositions(0.68);
        BorderPane root = new BorderPane(p);
        root.setPadding(new Insets(10));
        return root;
    }

    private <S, T> TableColumn<S, T> col(String title, String property) {
        TableColumn<S, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }

    public static void main(String[] args) {
        launch(args);
    }
}