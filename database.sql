DROP DATABASE IF EXISTS energy_audit;
CREATE DATABASE energy_audit;
USE energy_audit;

CREATE TABLE object (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     code VARCHAR(40) NOT NULL,
     name VARCHAR(160) NOT NULL,
     address VARCHAR(120) NOT NULL
);

CREATE TABLE consumption_record (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     object_id BIGINT NOT NULL,
     energy_type VARCHAR(40) NOT NULL,
     amount DOUBLE NOT NULL,
     period VARCHAR(20) NOT NULL,
     CONSTRAINT fk_consumption_object
     FOREIGN KEY (object_id)
     REFERENCES object(id)
     ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE energy_audit (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     object_id BIGINT NOT NULL,
     audit_date VARCHAR(10) NOT NULL,
     auditor_name VARCHAR(120) NOT NULL,
     CONSTRAINT fk_audit_object
     FOREIGN KEY (object_id)
     REFERENCES object(id)
     ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE measure (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      audit_id BIGINT NOT NULL,
      state VARCHAR(30) NOT NULL,
      f1 DOUBLE NOT NULL,
      f2 DOUBLE NOT NULL,
      f3 DOUBLE NOT NULL,
      integral_index DOUBLE NOT NULL,
      CONSTRAINT fk_measure_audit
      FOREIGN KEY (audit_id)
      REFERENCES energy_audit(id)
      ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO object (code, name, address) VALUES
     ('OBJ-001', 'Головний навчальний корпус', 'пр. Віталія Грицаєнка, 24'),
     ('OBJ-002', 'Гуртожиток №5', 'вул. Інститутський проріз, 24');

INSERT INTO consumption_record (object_id, energy_type, amount, period) VALUES
     (1, 'Електроенергія', 12500.50, '2026-09'),
     (1, 'Теплова енергія', 450.00, '2026-09');

INSERT INTO energy_audit (object_id, audit_date, auditor_name) VALUES
     (1, '2026-10-15', 'Олександр Коваленко');

INSERT INTO measure (audit_id, state, f1, f2, f3, integral_index) VALUES
     (1, 'PROPOSED', 85.0, 70.0, 60.0, 73.5),
     (1, 'APPROVED', 90.0, 80.0, 75.0, 82.75);