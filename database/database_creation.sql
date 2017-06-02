CREATE DATABASE `servicecenter`;
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  Name VARCHAR(100) NOT NULL,
  Surname VARCHAR(100) NOT NULL,
  Patronymic VARCHAR(100),
  Phone number VARCHAR(45) NOT NULL,
  E-mail VARCHAR(100) NOT NULL,
  Login VARCHAR(100) UNIQUE NOT NULL,
  Password TEXT NOT NULL,
  Role VARCHAR(45) NOT NULL
);

CREATE TABLE receipt(
  id INT PRIMARY KEY,
  Date DATE NOT NULL,
  Device TEXT NOT NULL,
  Client INT NOT NULL,
  Receiver INT NOT NULL,
  Malfunction TEXT NOT NULL,
  Note TEXT,
  Master INT,
  Repair type VARCHAR(45) NOT NULL,
  Status VARCHAR(45) NOT NULL
);

CREATE TABLE device(
	Serial number TEXT PRIMARY KEY,
	Type VARCHAR(100) NOT NULL,
	Brand VARCHAR(100) NOT NULL,
	Model VARCHAR(100) NOT NULL,
	Purchase DATE,
	Warranty expiration DATE,
	Previous repair INT,
	Repair warranty expiration DATE,
	Client INT NOT NULL
);

CREATE TABLE invoice(
	id INT PRIMARY KEY,
	Date DATE NOT NULL,
	Receipt INT NOT NULL,
	Price DOUBLE NOT NULL,
	Cient INT NOT NULL,
	Receiver INT NOT NULL,
	Status VARCHAR(45) NOT NULL
);

CREATE TABLE bankaccount(
	id INT PRIMARY KEY,
	Client INT NOT NULL,
	Date DATE NOT NULL,
	CVC INT NOT NULL
);

INSERT INTO users VALUES ("Receiver1", "Receiver1", "Receiver1", "8-921-577-57-77", "email@email.com", "Receiver1", "123", "Receiver");
INSERT INTO users VALUES ("Receiver2", "Receiver2", "Receiver2", "8-921-577-57-77", "email@email.com", "Receiver2", "123", "Receiver");
INSERT INTO users VALUES ("Receiver3", "Receiver3", "Receiver3", "8-921-577-57-77", "email@email.com", "Receiver3", "123", "Receiver");
INSERT INTO users VALUES ("Master1", "Master1", "Master1", "8-921-577-57-77", "email@email.com", "Master1", "123", "Master");
INSERT INTO users VALUES ("Master2", "Master2", "Master2", "8-921-577-57-77", "email@email.com", "Master2", "123", "Master2");
INSERT INTO users VALUES ("User1", "User1", "User1", "8-921-577-57-77", "email@email.com", "User1", "123", "User");
INSERT INTO users VALUES ("User2", "User2", "User2", "8-921-577-57-77", "email@email.com", "User2", "123", "User");

INSERT INTO device VALUES("PHTC0005S03", "Phone", "OnePlus", "One", DATE '2014-02-10', DATE '2015-02-10', NULL, NULL, 6);

INSERT INTO receipt VALUES(
	201703171513251, 
	DATE '2017-03-17', 
	"PHTC0005S03", 
	(SELECT Client FROM device WHERE Serial number = "PHTC0005S03"), 
	1, 
	"Display troubles", 
	NULL, 
	NULL, 
	"Not_warranty",
	"Ready_for_extr"
);

INSERT INTO invoice VALUES(
	201703251513251,
	DATE '2017-03-25',
	201703171513251,
	4500,
	(SELECT Client FROM receipt WHERE id = 201703171513251),
	(SELECT Receiver FROM receipt WHERE id = 201703171513251),
	"Waiting_For_Payment"
);

INSERT INTO bankaccount VALUES(
	16356024,
	6,
	DATE '2018-02-31',
	352
);