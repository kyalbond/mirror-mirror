CREATE TABLE IF NOT EXISTS weights(
UID INTEGER NOT NULL,
DateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
Weight REAL NOT NULL,
PRIMARY KEY (UID, DateTime));