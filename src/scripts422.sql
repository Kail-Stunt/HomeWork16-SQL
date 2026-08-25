CREATE TABLE person (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    age         INT NOT NULL,
    has_license BOOLEAN DEFAULT FALSE
);

CREATE TABLE car (
    id      BIGSERIAL PRIMARY KEY,
    make    TEXT NOT NULL,
    model   TEXT NOT NULL,
    cost    NUMERIC(12, 2) NOT NULL
);

ALTER TABLE person
    ADD CONSTRAINT fk_person_car FOREIGN KEY (car_id) REFERENCES car (id);
