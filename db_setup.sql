USE mysql

create database IF NOT EXISTS hse;

USE hse;
CREATE TABLE IF NOT EXISTS Admin(ID INT NOT NULL
,Name VARCHAR(225) NOT NULL
,Surname VARCHAR(225) NOT NULL
,Password VARCHAR(225) NOT NULL
,PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Venues(ID INT NOT NULL
,VenueName VARCHAR(225) NOT NULL
,VenueAddress VARCHAR(225) NOT NULL
,PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS Appointments(UserID INT NOT NULL
,ID INT NOT NULL
,Vaccine VARCHAR(225)
,Date VARCHAR(225)
,Time VARCHAR(225)
,Type VARCHAR(225)
,VenueID INT
,PRIMARY KEY (ID)
,FOREIGN KEY (VenueID) REFERENCES Venues(ID)
);

CREATE TABLE IF NOT EXISTS Users(ID INT NOT NULL
,DOB VARCHAR(225) NOT NULL
,Name VARCHAR(225) NOT NULL
,Surname VARCHAR(225) NOT NULL
,PPSN VARCHAR(225) NOT NULL
,Address VARCHAR(225) NOT NULL
,Phone VARCHAR(225)
,NextAppointmentID INT
,LastLogin VARCHAR(225)
,Password VARCHAR(225)
,PRIMARY KEY (ID)
,FOREIGN KEY (NextAppointmentID) REFERENCES Appointments(ID)


    CREATE TABLE IF NOT EXISTS `roles` (
    `role_id` bigint NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(225) NOT NULL,
    PRIMARY KEY (`role_id`)
    );

    CREATE TABLE IF NOT EXISTS `users_roles` (
    `user_id` bigint NOT NULL,
    `role_id` bigint NOT NULL,
    KEY `user_fk_idx` (`user_id`),
    KEY `role_fk_idx` (`role_id`),
    CONSTRAINT `role_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
    CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    );
);