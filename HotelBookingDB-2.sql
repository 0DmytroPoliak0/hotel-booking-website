-- Create the Database 
CREATE DATABASE IF NOT EXISTS HotelBookingDB;

-- Use HotelBookingDB database
USE HotelBookingDB;

-- Create the User table with UUID as primary key
CREATE TABLE IF NOT EXISTS User (
   userID VARCHAR(36) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL
);

-- Create the Admin table with UUID as primary key
CREATE TABLE IF NOT EXISTS Admin (
   adminID VARCHAR(36) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL
);

-- Create the Staff table with UUID as primary key
CREATE TABLE IF NOT EXISTS Staff (
   staffID VARCHAR(36) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   adminID VARCHAR(36), 
   FOREIGN KEY (adminID) REFERENCES Admin(adminID)
);

-- Create the Hotel table with auto-increment primary key
CREATE TABLE IF NOT EXISTS Hotel (
   hotelID INT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   address VARCHAR(255) NOT NULL,
   contact VARCHAR(100) NOT NULL,
   adminID VARCHAR(36),
   FOREIGN KEY (adminID) REFERENCES Admin(adminID)
);

-- Create the RoomType table with roomType as primary key
CREATE TABLE IF NOT EXISTS RoomType (
   roomType VARCHAR(100) PRIMARY KEY
);

-- Create the Room table with auto-increment primary key and foreign key references to Hotel and RoomType
CREATE TABLE IF NOT EXISTS Room (
   roomID INT AUTO_INCREMENT PRIMARY KEY,
   roomType VARCHAR(100) NOT NULL,
   rate DECIMAL(10, 2) NOT NULL,
   availability BOOLEAN NOT NULL DEFAULT TRUE,
   numberOfPeople INT NOT NULL,
   hotelID INT,
   FOREIGN KEY (hotelID) REFERENCES Hotel(hotelID) ON DELETE CASCADE,
   FOREIGN KEY (roomType) REFERENCES RoomType(roomType) ON DELETE CASCADE
);

-- Create the Booking table with UUID as primary key
CREATE TABLE IF NOT EXISTS Booking (
   bookingID VARCHAR(36) PRIMARY KEY,
   roomID INT,
   checkInDate DATE NOT NULL,
   checkOutDate DATE NOT NULL,
   userID VARCHAR(36),
   bookingInfo TEXT,
   FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE,
   FOREIGN KEY (roomID) REFERENCES Room(roomID) ON DELETE CASCADE
);

-- Create the Reports table with UUID as primary key
CREATE TABLE IF NOT EXISTS Reports (
   reportID VARCHAR(36) PRIMARY KEY,
   title VARCHAR(255),
   reportType VARCHAR(100),
   content TEXT,
   creationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   bookingID VARCHAR(36),
   FOREIGN KEY (bookingID) REFERENCES Booking(bookingID) ON DELETE CASCADE
);

-- Create the Payment table 
CREATE TABLE IF NOT EXISTS Payment (
   creditCardNUmber VARCHAR(16),
   bookingID VARCHAR(36),
   paymentType VARCHAR(16),
   staffID VARCHAR(36),
   FOREIGN KEY (bookingID) REFERENCES Booking(bookingID),
   FOREIGN KEY (staffID) REFERENCES Staff(staffID),
   PRIMARY KEY (creditCardNUmber, bookingID)
);

-- Create the Email table 
CREATE TABLE IF NOT EXISTS Email (
   emailID INT AUTO_INCREMENT PRIMARY KEY,
   creditCardNUmber VARCHAR(16),
   bookingID VARCHAR(36),
   emailBody TEXT,
   dateofEmail TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   staffID VARCHAR(36),
   FOREIGN KEY (creditCardNUmber, bookingID) REFERENCES Payment(creditCardNUmber, bookingID) ON DELETE CASCADE, -- Adjust foreign key
   FOREIGN KEY (staffID) REFERENCES Staff(staffID)
);

-- Create the Preferences table
CREATE TABLE IF NOT EXISTS Preferences (
   userID VARCHAR(36) NOT NULL,
   roomType VARCHAR(100) ,
   numberOfPeople INT,
   rate DECIMAL(10, 2) NOT NULL,
   PRIMARY KEY (userID, roomType, numberOfPeople, rate),
   FOREIGN KEY (userID) REFERENCES User(userID),
   FOREIGN KEY (rate) REFERENCES Room(rate),
   FOREIGN KEY (roomType) REFERENCES RoomType(roomType)
);

-- other way

-- щерукalter
-- Create the Preferences table
CREATE TABLE IF NOT EXISTS Preferences (
   userID VARCHAR(36) NOT NULL,
   roomType VARCHAR(100) NOT NULL,
   numberOfPeople INT NOT NULL,
   rate DECIMAL(10, 2),
   PRIMARY KEY (userID, roomType, numberOfPeople, rate),
   FOREIGN KEY (userID) REFERENCES User(userID),
   FOREIGN KEY (roomType) REFERENCES Room(roomType) ON DELETE CASCADE
);

-- Insert data into User table
INSERT INTO User (userID, name, email) VALUES 
(UUID(), 'John Doe', 'johndoe@example.com'),
(UUID(), 'Alice Smith', 'alice@example.com'),
(UUID(), 'Emma Johnson', 'emma@example.com'),
(UUID(), 'Michael Brown', 'michael@example.com');

-- Insert data into Admin table
INSERT INTO Admin (adminID, name, email, password) VALUES 
(UUID(), 'Admin User', 'admin@example.com', 'adminpassword'),
(UUID(), 'Super Admin', 'superadmin@example.com', 'superadminpassword');

-- Insert data into Staff table
INSERT INTO Staff (staffID, name, email, password, adminID) VALUES 
(UUID(), 'Staff Member', 'staff@example.com', 'staffpassword', (SELECT adminID FROM Admin WHERE name = 'Admin User')),
(UUID(), 'Assistant Manager', 'assistantmanager@example.com', 'assistantmanagerpassword', (SELECT adminID FROM Admin WHERE name = 'Super Admin'));

-- Insert data into Hotel table
INSERT INTO Hotel (name, address, contact, adminID) VALUES 
('Hotel C', '789 Elm St, Village', '456-789-0123', (SELECT adminID FROM Admin WHERE name = 'Admin User')),
('Hotel D', '789 Oak St, Town', '321-654-0987', (SELECT adminID FROM Admin WHERE name = 'Admin User')),
('Hotel E', '456 Pine St, City', '789-012-3456', (SELECT adminID FROM Admin WHERE name = 'Super Admin')),
('Hotel F', '123 Maple St, Suburb', '654-321-9870', (SELECT adminID FROM Admin WHERE name = 'Super Admin'));



 SET SQL_SAFE_UPDATES = 0;
 -- Assign Admin User to manage hotels C, D, E, and F
 UPDATE Hotel
 SET adminID = (SELECT adminID FROM Admin WHERE name = 'Admin User')
 WHERE hotelID IN (
     SELECT temp.hotelID FROM (
         SELECT hotelID FROM Hotel WHERE name IN ('Hotel C', 'Hotel D', 'Hotel E', 'Hotel F')
     ) AS temp
 );

 -- Assign Super Admin to manage hotels G, H, I, J, K, and L
 UPDATE Hotel
 SET adminID = (SELECT adminID FROM Admin WHERE name = 'Super Admin')
 WHERE hotelID IN (
     SELECT temp.hotelID FROM (
         SELECT hotelID FROM Hotel WHERE name IN ('Hotel G', 'Hotel H', 'Hotel I', 'Hotel J', 'Hotel K', 'Hotel L')
     ) AS temp
 );


 SET SQL_SAFE_UPDATES = 1;


-- Insert data into RoomType table
INSERT INTO RoomType (roomType) VALUES 
('Standard'),
('Deluxe'),
('Suite');

-- Insert rooms for each hotel along with room type and number of people
-- Hotel C
INSERT INTO Room (roomType, rate, availability, numberOfPeople, hotelID) VALUES
('Standard', 100.00, TRUE, 2, 1),
('Standard', 100.00, TRUE, 2, 1),
('Standard', 100.00, TRUE, 2, 1),
('Deluxe', 150.00, TRUE, 4, 1),
('Deluxe', 150.00, TRUE, 4, 1);

-- Hotel D
INSERT INTO Room (roomType, rate, availability, numberOfPeople, hotelID) VALUES
('Standard', 90.00, TRUE, 2, 2),
('Standard', 90.00, TRUE, 2, 2),
('Deluxe', 130.00, TRUE, 4, 2),
('Deluxe', 130.00, TRUE, 4, 2),
('Suite', 180.00, TRUE, 6, 2);

-- Hotel E
INSERT INTO Room (roomType, rate, availability, numberOfPeople, hotelID) VALUES
('Standard', 110.00, TRUE, 2, 3),
('Standard', 110.00, TRUE, 2, 3),
('Standard', 110.00, TRUE, 2, 3),
('Deluxe', 160.00, TRUE, 4, 3),
('Suite', 220.00, TRUE, 6, 3);

-- Hotel F
INSERT INTO Room (roomType, rate, availability, numberOfPeople, hotelID) VALUES
('Standard', 95.00, TRUE, 2, 4),
('Standard', 95.00, TRUE, 2, 4),
('Deluxe', 140.00, TRUE, 4, 4),
('Deluxe', 140.00, TRUE, 4, 4),
('Suite', 200.00, TRUE, 6, 4);

-- Insert data into Booking table
INSERT INTO Booking (bookingID, roomID, checkInDate, checkOutDate, userID, bookingInfo) VALUES 
(UUID(), 3, '2024-02-20', '2024-02-25', (SELECT userID FROM User WHERE email = 'emma@example.com'), 'Booking for a standard room'),
(UUID(), 4, '2024-03-10', '2024-03-15', (SELECT userID FROM User WHERE email = 'michael@example.com'), 'Booking for a deluxe room');

-- Insert data into Reports table
INSERT INTO Reports (reportID, title, reportType, content, bookingID) VALUES 
(UUID(), 'Monthly Report', 'Sales', 'This report contains sales data for the month of February.', (SELECT bookingID FROM Booking WHERE roomID = 3)),
(UUID(), 'Monthly Report', 'Sales', 'This report contains sales data for the month of March.', (SELECT bookingID FROM Booking WHERE roomID = 4));

-- Insert data into Payment table
INSERT INTO Payment (creditCardNUmber, bookingID, paymentType, staffID) VALUES 
('1234567890123456', (SELECT bookingID FROM Booking WHERE roomID = 3), 'Credit Card', (SELECT staffID FROM Staff WHERE name = 'Staff Member')),
('9876543210987654', (SELECT bookingID FROM Booking WHERE roomID = 4), 'Debit Card', (SELECT staffID FROM Staff WHERE name = 'Assistant Manager'));

-- Insert data into Email table
INSERT INTO Email (creditCardNUmber, bookingID, emailBody, staffID) VALUES 
('1234567890123456', (SELECT bookingID FROM Booking WHERE roomID = 3), 'Thank you for your booking.', (SELECT staffID FROM Staff WHERE name = 'Staff Member')),
('9876543210987654', (SELECT bookingID FROM Booking WHERE roomID = 4), 'Confirmation of your booking.', (SELECT staffID FROM Staff WHERE name = 'Assistant Manager'));

-- Insert data into Preferences table
INSERT INTO Preferences (userID, roomType, rate, numberOfPeople) VALUES 
((SELECT userID FROM User WHERE email = 'johndoe@example.com'), 'Standard', 100.00, 2),
((SELECT userID FROM User WHERE email = 'alice@example.com'), 'Deluxe', 150.00, 4),
((SELECT userID FROM User WHERE email = 'emma@example.com'), 'Standard', 100.00, 2),
((SELECT userID FROM User WHERE email = 'michael@example.com'), 'Deluxe', 150.00, 4);


-- Retrieve all users, admins, bookings, reports, staff, hotels
SELECT * FROM User;
SELECT * FROM Admin;
SELECT * FROM Booking;
SELECT * FROM Reports;
SELECT * FROM Staff;
SELECT * FROM Hotel;
SELECT * FROM Room;
SELECT * FROM Payment;
SELECT * FROM Email;
SELECT * FROM Preferences;



-- Retrieve all bookings with corresponding user information
SELECT Booking.bookingID, Booking.roomID, Booking.checkInDate, Booking.checkOutDate, Booking.bookingInfo, User.name AS customerName, User.email AS customerEmail
FROM Booking
INNER JOIN User ON Booking.userID = User.userID;

-- Retrieve all reports with corresponding booking information
SELECT Reports.reportID, Reports.title, Reports.reportType, Reports.content, Reports.creationDate, Booking.roomID, Booking.checkInDate, Booking.checkOutDate, Booking.bookingInfo
FROM Reports
INNER JOIN Booking ON Reports.bookingID = Booking.bookingID;

-- Retrieve all bookings made by a specific user
SELECT * FROM Booking WHERE userID = (SELECT userID FROM User WHERE email = 'johndoe@example.com');

-- Reports by Month and Year
SELECT YEAR(creationDate) AS Year, MONTH(creationDate) AS Month, COUNT(*) AS TotalReports
FROM Reports
GROUP BY YEAR(creationDate), MONTH(creationDate);

-- Top 3 Customers (by number of bookings)
SELECT User.userID, User.name AS CustomerName, COUNT(Booking.bookingID) AS TotalBookings
FROM User
JOIN Booking ON User.userID = Booking.userID
GROUP BY User.userID, User.name
ORDER BY TotalBookings DESC
LIMIT 3;

-- Least Booked Room
SELECT roomID, COUNT(*) AS TotalBookings
FROM Booking
GROUP BY roomID
ORDER BY TotalBookings ASC
LIMIT 1;

-- Most Booked Room
SELECT roomID, COUNT(*) AS TotalBookings
FROM Booking
GROUP BY roomID
ORDER BY TotalBookings DESC
LIMIT 1;

-- Summary of Rooms Categorized
SELECT Room.roomType, COUNT(*) AS TotalRooms
FROM Room
GROUP BY Room.roomType;

-- List of Rooms Categorized
SELECT Room.roomID, Room.roomType, Room.rate
FROM Room
ORDER BY Room.roomType;

-- Additional Data Analysis

-- Average Booking Duration
SELECT AVG(DATEDIFF(checkOutDate, checkInDate)) AS AvgBookingDuration
FROM Booking;

-- Average Room Rate
SELECT AVG(rate) AS AvgRoomRate
FROM Room;

-- Total Revenue Generated
SELECT SUM(rate * DATEDIFF(checkOutDate, checkInDate)) AS TotalRevenue
FROM Booking
JOIN Room ON Booking.roomID = Room.roomID;

-- Monthly Revenue Report
SELECT YEAR(Booking.checkInDate) AS Year, MONTH(Booking.checkInDate) AS Month, SUM(Room.rate * DATEDIFF(Booking.checkOutDate, Booking.checkInDate)) AS Revenue
FROM Booking
INNER JOIN Room ON Booking.roomID = Room.roomID
GROUP BY YEAR(Booking.checkInDate), MONTH(Booking.checkInDate)
ORDER BY Year, Month;

-- Top 3 Hotels by Total Revenue
SELECT Hotel.name, SUM(Room.rate * DATEDIFF(Booking.checkOutDate, Booking.checkInDate)) AS TotalRevenue
FROM Hotel
INNER JOIN Room ON Hotel.hotelID = Room.hotelID
INNER JOIN Booking ON Room.roomID = Booking.roomID
GROUP BY Hotel.name
ORDER BY TotalRevenue DESC
LIMIT 3;

-- Occupancy Rate by Hotel
SELECT Hotel.name, 
       (COUNT(Booking.bookingID) / (DATEDIFF(MAX(Booking.checkOutDate), MIN(Booking.checkInDate)) + 1)) AS OccupancyRate
FROM Hotel
INNER JOIN Room ON Hotel.hotelID = Room.hotelID
LEFT JOIN Booking ON Room.roomID = Booking.roomID
GROUP BY Hotel.name;

-- Most Booked Room Type by Hotel
SELECT Hotel.name, Room.roomType, COUNT(Booking.bookingID) AS TotalBookings
FROM Hotel
INNER JOIN Room ON Hotel.hotelID = Room.hotelID
LEFT JOIN Booking ON Room.roomID = Booking.roomID
GROUP BY Hotel.name, Room.roomType
ORDER BY Hotel.name, TotalBookings DESC;

-- Monthly Report Count
SELECT YEAR(creationDate) AS Year, MONTH(creationDate) AS Month, COUNT(*) AS TotalReports
FROM Reports
GROUP BY Year, Month
ORDER BY Year, Month;











-- Drop the Email table
DROP TABLE IF EXISTS Email;
-- Drop the Payment table
DROP TABLE IF EXISTS Payment;
-- Drop the Reports table
DROP TABLE IF EXISTS Reports;
-- Drop the Booking table
DROP TABLE IF EXISTS Booking;
-- Drop the Room table
DROP TABLE IF EXISTS Room;
-- Drop the Hotel table
DROP TABLE IF EXISTS Hotel;
-- Drop the Preferences table
DROP TABLE IF EXISTS Preferences;
-- Drop the User table
DROP TABLE IF EXISTS User;
-- Drop the Staff table
DROP TABLE IF EXISTS Staff;
-- Drop the Admin table
DROP TABLE IF EXISTS Admin;
-- Drop the RoomType table
DROP TABLE IF EXISTS RoomType;