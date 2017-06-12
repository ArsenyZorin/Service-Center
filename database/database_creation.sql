CREATE DATABASE servicecenter;
USE servicecenter;
CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	Name VARCHAR(100) NOT NULL,
	Surname VARCHAR(100) NOT NULL,
	Patronymic VARCHAR(100),
	PhoneNumber VARCHAR(45) NOT NULL,
	Email VARCHAR(100) NOT NULL,
	Login VARCHAR(100) UNIQUE NOT NULL,
	Password TEXT NOT NULL,
	Role VARCHAR(45) NOT NULL
);

CREATE TABLE receipt(
	id VARCHAR(100) PRIMARY KEY,
	ReceiptDate DATE NOT NULL,
	Device TEXT NOT NULL,
	Client INT NOT NULL,
	Receiver INT NOT NULL,
	Malfunction TEXT NOT NULL,
	Note TEXT,
	Master INT,
	RepairType VARCHAR(45) NOT NULL,
	Status VARCHAR(45) NOT NULL
);

CREATE TABLE device(
	SerialNumber VARCHAR(100) PRIMARY KEY,
	DeviceType VARCHAR(100) NOT NULL,
	Brand VARCHAR(100) NOT NULL,
	Model VARCHAR(100) NOT NULL,
	Purchase DATE,
	WarrantyExpiration DATE,
	PreviousRepair INT,
	RepairWarrantyExpiration DATE,
	Client INT NOT NULL
);

CREATE TABLE invoice(
	id VARCHAR(100) PRIMARY KEY,
	InvoiceDate DATE NOT NULL,
	Receipt VARCHAR(100) NOT NULL,
	Price DOUBLE NOT NULL,
	Client INT NOT NULL,
	Receiver INT NOT NULL,
	Status VARCHAR(45) NOT NULL
);

CREATE TABLE bankaccount(
	id VARCHAR(30) PRIMARY KEY,
	Client INT NOT NULL,
	ValidDate DATE NOT NULL,
	CVC INT NOT NULL,
	Balance DOUBLE NOT NULL
);

INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, Email, Login, Password, Role) VALUES (
	"Receiver1",		#Name	
	"Receiver1",		#Surname
	"Receiver1",		#Patronymic
	"8-921-577-57-77",  #Phone
	"email@email.com",  #E-mail
	"Receiver1",		#Login
	"123",				#Password
	"Receiver"			#Role
);
INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, Email, Login, Password, Role) VALUES (
	"Receiver2",		#Name
	"Receiver2",		#Surname
	"Receiver2",		#Patronymic
	"8-921-577-57-77",	#Phone
	"email@email.com",	#E-mail
	"Receiver2",		#Login
	"123",				#Password
	"Receiver"			#Role
);
INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, Email, Login, Password, Role) VALUES (
	"Receiver3",		#Name
	"Receiver3",		#Surname
	"Receiver3",		#Patronymic
	"8-921-577-57-77",	#Phone
	"email@email.com",  #E-mail
	"Receiver3",		#Login
	"123",				#Password
	"Receiver"			#Role
);
INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, Email, Login, Password, Role) VALUES (
	"Master1",			#Name
	"Master1",			#Surname
	"Master1",			#Patronymic
	"8-921-577-57-77",	#Phone
	"email@email.com",	#E-mail
	"Master1",			#Login
	"123",				#Password
	"Master"			#Role
);
INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, Email, Login, Password, Role) VALUES (
	"Master2",			#Name
	"Master2",			#Surname
	"Master2",			#Patronymic
	"8-921-577-57-77",	#Phone
	"email@email.com",	#E-mail
	"Master2",			#Login
	"123",				#Password
	"Master"			#Role
);
INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, Email, Login, Password, Role) VALUES (
	"User1",			#Name
	"User1",			#Surname
	"User1",			#Patronymic
	"8-921-577-57-77",	#Phone
	"email@email.com",	#E-mail
	"User1",			#Login
	"123",				#Password
	"Client"			#Role
);
INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, Email, Login, Password, Role) VALUES (
	"User2",			#Name
	"User2",			#Surname
	"User2",			#Patronymic
	"8-921-577-57-77",	#Phone
	"email@email.com",	#E-mail
	"User2",			#Login
	"123",				#Password
	"Client"			#Role
);

INSERT INTO device VALUES(
	"PHTC0005S03",		#SerialNumber 
	"Phone",			#DeviceType
	"OnePlus",			#Brand
	"One",				#Model
	DATE '2014-02-10',  #Purchase
	DATE '2015-02-10',	#WarrantyExpiration
	NULL,				#PreviousRepair
	NULL,				#RepairWarrntyExpiration
	6					#Client
);

INSERT INTO receipt VALUES(
	"201703171513251",													#id 
	DATE '2017-03-17',													#ReceiptDate
	"PHTC0005S03",														#Device
	(SELECT Client FROM device WHERE SerialNumber = "PHTC0005S03"),		#Client
	1,																	#Receiver
	"Display troubles",													#Malfunction
	NULL,																#Note
	NULL,																#Master
	"Not_warranty",														#RepairType
	"Ready_for_extr"													#Status
);

INSERT INTO invoice VALUES(
	"201703151513251",													#id
	DATE '2017-03-25',													#InvoiceDate
	"201703171513251",													#Receipt
	4500,																#Price
	(SELECT Client FROM receipt WHERE receipt.id = "201703171513251"),	#Client
	(SELECT Receiver FROM receipt WHERE id = "201703171513251"),		#Receiver
	"Waiting_For_Payment"												#Status
);

INSERT INTO bankaccount VALUES(
	"16356024",															#id
	6,																	#Client
	DATE '2017-02-25',													#ValidDate
	352,																#CVC
	15000.0																#Balance		
);
