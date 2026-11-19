DROP DATABASE IF EXISTS energy_audit;
CREATE DATABASE energy_audit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE energy_audit;

DROP TABLE IF EXISTS consumption_record;
DROP TABLE IF EXISTS measure;
DROP TABLE IF EXISTS energy_audit;
DROP TABLE IF EXISTS audit_object;

CREATE TABLE audit_object (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(40) NOT NULL,
    name VARCHAR(160) NOT NULL,
    address VARCHAR(120) NOT NULL
);

CREATE TABLE energy_audit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    object_id BIGINT NOT NULL,
    audit_date VARCHAR(10) NOT NULL,
    auditor_name VARCHAR(120) NOT NULL,
    actual_consumption DOUBLE NOT NULL,
    norm_consumption DOUBLE NOT NULL,
    payback_score DOUBLE NOT NULL,
    emission_score DOUBLE NOT NULL,
    FOREIGN KEY (object_id) REFERENCES audit_object(id) ON DELETE CASCADE
);

CREATE TABLE measure (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    audit_id BIGINT NOT NULL,
    state VARCHAR(30) NOT NULL,
    f1 DOUBLE NOT NULL,
    f2 DOUBLE NOT NULL,
    f3 DOUBLE NOT NULL,
    priority_index DOUBLE NOT NULL,
    FOREIGN KEY (audit_id) REFERENCES energy_audit(id) ON DELETE CASCADE
);

CREATE TABLE consumption_record (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     measure_id BIGINT NOT NULL,
     energy_type VARCHAR(40) NOT NULL,
     amount DOUBLE NOT NULL,
     period VARCHAR(20) NOT NULL,
     FOREIGN KEY (measure_id) REFERENCES measure(id) ON DELETE CASCADE
);

INSERT INTO audit_object(code, name, address) VALUES
('OBJ-01', 'Національний університет "Полтавська політехніка імені Юрія Кондратюка"', 'м. Полтава, просп. Віталія Грицаєнка, 24'),
('OBJ-02', 'Гуртожиток №5 Національного університету "Полтавська політехніка імені Юрія Кондратюка"', 'м. Полтава, вул. Інститутський проріз, 24'),
('OBJ-03', 'Гуртожиток №2 Національного університету "Полтавська політехніка імені Юрія Кондратюка"', 'м. Полтава, просп. Віталія Грицаєнка, 25');

INSERT INTO energy_audit(object_id, audit_date, auditor_name, actual_consumption, norm_consumption, payback_score, emission_score) VALUES
(1, '2026-10-22', 'Кантенко С.І.', 150.0, 100.0, 80.0, 70.0),
(2, '2026-10-25', 'Петренко О.В.', 240.0, 200.0, 60.0, 50.0),
(3, '2026-11-02', 'Привітенко М.М.', 350.0, 200.0, 90.0, 85.0);

INSERT INTO measure(audit_id, state, f1, f2, f3, priority_index) VALUES
(1, 'PROPOSED', 50.0, 80.0, 70.0, 65.5),
(2, 'APPROVED', 20.0, 60.0, 50.0, 41.5),
(3, 'COMPLETED', 75.0, 90.0, 85.0, 82.75);


INSERT INTO consumption_record(measure_id, energy_type, amount, period) VALUES
(1, 'Електроенергія', 1420.5, '2026-10'),
(2, 'Природний газ', 410.8, '2026-03'),
(3, 'Теплова енергія', 2150.0, '2026-11');
