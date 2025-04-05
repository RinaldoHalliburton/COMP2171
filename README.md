# Rent A Bike (RAB) - Bicycle Transportation System

## Problem Definition

The University of the West Indies spans 653 acres, housing seven faculties and accommodating approximately 19,000 students. Navigating such a vast campus presents challenges, including extreme weather conditions, tardiness, and fatigue. While walking promotes fitness, these issues often overshadow its benefits, making campus traversal inefficient and inconvenient.

## Proposed Solution

To address these challenges while retaining the benefits of exercise, we introduce Rent A Bike (RAB), a bicycle transportation system designed for students and staff. RAB allows users to rent bicycles via a mobile app, enhancing mobility and reducing navigation difficulties on campus.

## Key Features

Account Creation: Users register using their UWI ID numbers to ensure only university-affiliated individuals access the service.

Bike Rental & Selection: Customers can rent a bike by entering a unique code assigned to each bicycle.

Automated Tracking: Smart racks located at faculties and major locations detect when a bike is taken or returned, updating availability in real-time.

Rental Time-Based Pricing: The system calculates rental fees based on the duration for which a bike is assigned to a user.

Incident Reporting & Rating: Users can report bike issues and provide feedback after a ride.

Data Collection & Synchronization: User inputs and smart racks ensure accurate tracking of rentals, returns, and payments.

This system not only streamlines campus transportation but also encourages sustainable and efficient travel. With seamless integration between the mobile app and automated bike stations, RAB provides a hassle-free experience for the university community.

## Technologies Used

Front-end: Swing JFrame (Java UI framework)

Back-end: Java

Database: MySQL

Authentication: Secure user registration & login with UWI ID validation

Payment Processing: Secure transactions for rental services

JAR Dependencies: Activation JAR, Mail JAR (for email services), JCalender JAR, MYSQL-Connector JAR

## Installation & Setup (ECLIPSE IDE or VS Code)

To get started with RAB on your local machine using ECLIPSE IDE or VS Code:

Clone the repository:

git clone https://https://github.com/RinaldoHalliburton/COMP2171

cd Object_Oriented_Design

Set up the Java environment:

Ensure you have JDK installed.

Open the project in editor and install the Java Extension Pack.

Place the required JAR files in the lib folder and add them to the classpath.

Create own database with schema provided:

Connect to MYSQL with your own environment variables

## Compile and Run Java UI:

run the Main.java file
