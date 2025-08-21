CREATE TABLE IF NOT EXISTS users (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  password VARCHAR(120) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  last_login_at TIMESTAMP,
  token VARCHAR(512),
  active BOOLEAN
);

CREATE TABLE IF NOT EXISTS phones (
  id CHAR(36) PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  number VARCHAR(30),
  city_code VARCHAR(10),
  country_code VARCHAR(10),
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);