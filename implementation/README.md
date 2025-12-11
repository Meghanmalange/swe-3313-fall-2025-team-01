# Environment Setup
   
## Install Java 21
Our project uses Java 21, which must be installed before running the application.
Download Java 21 (Temurin/JDK):
https://adoptium.net/temurin/releases/?version=21

Steps to install:
- Download the Java 21 installer compatiple with your device.
- Run the installer → click Continue → Install.
- Verify installation in terminal:
  
```bash
java -version
```
```
Expected output:
openjdk version "21.x.x" ...
```

## Install Maven
Mac users: macOS already includes the required system tools, so installation is simple.

Download Maven:
https://maven.apache.org/download.cgi

Install via Homebrew (recommended):

```bash
brew install maven
```

Verify installation:
```
mvn -v
```

Expected output:
```
Apache Maven 3.x.x
Java version: 17
```

## Clone the Repository
```
git clone https://github.com/Meghanmalange/swe-3313-fall-2025-team-01
cd swe-3313-fall-2025-team-01
```

### Optional (Clone and rename project folder)
```
git clone https://github.com/Meghanmalange/swe-3313-fall-2025-team-01 <custom project_folder name>
cd <custom project_folder name>
```

## Environment Variables
Our application does not require environment variables


# Data Storage Setup

This application uses a local SQLite database file, included directly in the project.
No external database installation is required (no MySQL, no PostgreSQL, no environment variables).

##  Database Location

The SQLite file used by the application is located at:
```
src/main/resources/db/africanroyals.db
```

Spring Boot connects to this database using the configuration in application.properties:
```
spring.datasource.url=jdbc:sqlite:src/main/resources/db/africanroyals.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.sqlite.hibernate.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```
Database is local to the project
Spring Boot will automatically create missing tables using Hibernate (ddl-auto=update)

## Schema Initialization (seed.sql)

A SQL schema file is included at:
```
src/main/resources/db/seed.sql
```
This file contains the full schema for:
users
InventoryItem
Sale
Sale_Inventory_Item
Indexes for performance

Although this file contains the schema, the app does not automatically execute this SQL file, because SQLite + Spring Boot do not run SQL scripts by default.
Instead, the schema is created by Hibernate (ddl-auto=update).

## Data Initialization (DataInitializer)

To ensure the application has default users and inventory items, we use a Spring Boot CommandLineRunner:
File:
```
src/main/java/africanroyals/config/DataInitializer.java
```

What DataInitializer does:
Users created automatically on first run:

|   Username	 |     Password	    |     Role      |
|--------------|------------------|---------------|
|yxngjnr       |	(bcrypt hash)   | Administrator |
|demean        |	(bcrypt hash)   | Administrator |
|zclark16      |	(bcrypt hash)   | Administrator |
|megthebaddest |	(bcrypt hash)	  | Administrator |
|aaliy4hslvr   |	(bcrypt hash)	  | Administrator |
|customer      |	(bcrypt hash)	  | Customer      |

Inventory items created automatically on first run:
9 jewelry items will be inserted with images located under:
```
src/main/resources/static/images/
```

If the database already contains data, initialization is skipped.

## When the database initializes
The database is automatically initialized on first run when:
The SQLite file does not exist, OR
The database exists but tables are empty.

## Resetting the Database (Optional)

If you want to reset to a clean state:
Delete the database file:
```
src/main/resources/db/africanroyals.db
```

Restart the app:
```
mvn spring-boot:run
```
Spring Boot will:
Recreate all tables
Reinsert all default users
Reinsert the 9 inventory items

# How to Start and Login

## Clean and Build the Project

From inside the project root:
```bash
mvn clean install
```
This installs all dependencies and compiles the project.

### Start the Server (Terminal Only – No IDE Required)
```bash
mvn spring-boot:run
```

### Open the Application in a Browser

Once the server starts, open:
```
http://localhost:8080
```

## Login Credentials
|   Username	 |     Password	    |     Role      |
|--------------|------------------|---------------|
|yxngjnr       |	244466666	      | Administrator |
|demean        |	113333555555    | Administrator |
|zclark16      |	12345           | Administrator |
|megthebaddest |	superstar4life	| Administrator |
|aaliy4hslvr   |	elephant	      | Administrator |
|customer      |	customer123	    | Customer      |

Admin features can be accessed here:
👉 http://localhost:8080/admin

# Troubleshooting

Below are common issues and fixes tested by the development team.

### Maven Command Not Found
Cause: Maven is not installed or PATH incorrect.
Fix:
```bash
brew install maven
```

### Application Fails to Start – Missing Dependencies
Fix:
```bash
mvn clean install -U
```
-U forces Maven to download fresh dependencies.

### Port Already in Use (8080)

Fix 1: Kill the process manually
```bash
lsof -i :8080
kill -9 <PID>
```

Fix 2: Change port in application.properties:
```
server.port=8081
```
### Database File Not Found

Delete the old database file:
```bash
rm africanroyals.db
```
Then restart the application to generate a fresh schema.

### "method does not exist" or Hibernate/SQLite dialect error

Run a full clean rebuild:
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

4.6 M1/M2/M3/M4 Architecture Issues
Ensure you are using a JDK built for ARM64 (the Temurin link provided above automatically detects Apple Silicon).

# Jewelry Store Presentation
Video 1: https://www.loom.com/share/1bf78ee79ec34572b4e3234ddd1f2ce1
Video 2: https://www.loom.com/share/26038e77fa9c4f3b8cbd2690e2fbb3ae

