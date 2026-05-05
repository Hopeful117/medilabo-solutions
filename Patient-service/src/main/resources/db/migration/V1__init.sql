CREATE TABLE patients (
  id INTEGER PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  date_of_birth DATE NOT NULL,
  gender VARCHAR(10),
  address VARCHAR(255),
  phone_number VARCHAR(255)
);

CREATE INDEX patients_index_0 ON patients (last_name);
