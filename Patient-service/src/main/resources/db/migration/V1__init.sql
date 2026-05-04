CREATE TABLE patients (
  id INTEGER PRIMARY KEY,
  firstName VARCHAR(255) NOT NULL,
  lastName VARCHAR(255) NOT NULL,
  dateOfBirth DATE NOT NULL,
  gender VARCHAR(10),
  address VARCHAR(255),
  phoneNumber VARCHAR(255)
);

CREATE INDEX patients_index_0 ON patients (lastName);
