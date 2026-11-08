# 📦 InvenTrack Pro — Desktop Inventory Management System
> **College Mini-Project | Java Desktop Application (Java Swing + Core Java JDBC + MySQL 8 + Maven)**

A modern, responsive desktop inventory management application built strictly with **Java 21**, **Java Swing**, and **Core JDBC**. Designed with a clean web-dashboard aesthetic using FlatLaf styling, KPI stat cards, low-stock warnings, and full product catalog CRUD operations.

---

## 📋 Table of Contents
1. [Project Overview & Key Features](#-project-overview--key-features)
2. [Technology Stack](#-technology-stack)
3. [Architecture & Design](#-architecture--design)
4. [Database Configuration & Setup](#-database-configuration--setup)
5. [IntelliJ IDEA Import & Run Guide](#-intellij-idea-import--run-guide)
6. [Command Line Run Guide](#-command-line-run-guide)
7. [Default Application Accounts](#-default-application-accounts)
8. [College Project Viva / Defense Q&A](#-college-project-viva--defense-qa)

---

## 🌟 Project Overview & Key Features

### A. Modern Login Screen
* **Decoupled Application Authentication**: Uses dedicated application user accounts (never MySQL root credentials).
* **Cryptographic Security**: Passwords are never stored in plain text. Hashed using **PBKDF2 with HMAC-SHA256** and unique 128-bit cryptographic salts.
* **Auto-Bootstrap**: Automatically creates the companion `users` table and initial administrator account on first startup.
* **Live Connection Diagnostics**: Visual badge reporting live MySQL connection health.

### B. Executive Dashboard
* **4 Aggregated KPI Stat Cards**:
  1. **Distinct Products**: Total unique items in the catalog (`COUNT(product_id)`).
  2. **Total Units in Stock**: Total physical quantity across the warehouse (`SUM(quantity)`).
  3. **Low-Stock Alerts**: Products requiring restocking (`quantity <= 5`).
  4. **Total Inventory Valuation**: Total value calculated via exact financial arithmetic (`SUM(price * quantity)`).
* **Critical Stock Table**: Highlighting out-of-stock and low-stock items with double-click quick restock.
* **Quick Actions Panel**: Direct navigation to add products or adjust inventory.

### C. Product Management
* **Interactive Table**: Displays ID, Name, Category, Quantity, Unit Price, Total Valuation, and Stock Status.
* **Status Badges**: Custom color-coded badges (`IN STOCK` green, `LOW STOCK` amber, `OUT OF STOCK` red).
* **Search & Filters**: Real-time keyword search (by product name or numeric ID) and dynamic category dropdown filtering.
* **Add & Edit Modal**: Form validation enforcing positive prices, non-negative quantities, and required names.
* **Quick Stock Adjustment**: Instant restock or outward dispatch without opening the full edit form.
* **Safe Deletion**: Two-step confirmation dialog before removing catalog items.

---

## 🛠 Technology Stack
* **Java**: OpenJDK 21+ (Compatible with JDK 21 to JDK 26).
* **GUI Framework**: Pure Java Swing (`JFrame`, `JPanel`, `JTable`, `CardLayout`, `SwingWorker`).
* **UI Styling**: FlatLaf (`FlatLightLaf`) for crisp, modern typography, rounded borders, and web-dashboard layout.
* **Persistence**: Java Database Connectivity (JDBC) with `java.sql.PreparedStatement` and `try-with-resources`.
* **Database**: MySQL Community Server 8.0+ / MySQL Workbench.
* **JDBC Driver**: MySQL Connector/J (`com.mysql:mysql-connector-j:8.3.0`).
* **Build & Dependency Management**: Apache Maven.
* **Testing**: JUnit 5 Jupiter.

---

## 🏗 Architecture & Design

The application is structured according to the **Layered Architecture (MVC / DAO Pattern)**:

```
src/main/java/com/inventory/
├── Main.java                    # Entry point: initializes L&F and launches GUI on EDT
├── config/
│   ├── DatabaseConfig.java      # Reads config.properties & manages JDBC connections
│   └── DatabaseInitializer.java # Auto-bootstraps users table and initial admin
├── model/
│   ├── Product.java             # Product entity POJO
│   ├── User.java                # User entity POJO
│   └── InventoryStats.java      # Dashboard KPI metrics model
├── dao/
│   ├── ProductDAO.java          # Parameterized JDBC queries for products
│   └── UserDAO.java             # Parameterized JDBC queries for users
├── service/
│   ├── ProductService.java      # Business logic, calculations, and input validation
│   ├── AuthService.java         # Authentication & user session management
│   ├── PasswordHasher.java      # PBKDF2WithHmacSHA256 salted password hashing
│   ├── ValidationException.java # Business validation exception
│   └── AuthenticationException.java # Auth failure exception
└── ui/
    ├── theme/
    │   ├── UITheme.java         # Design system: colors, typography, border styling
    │   └── CustomComponents.java# StatCards, primary buttons, pill status renderers
    ├── LoginFrame.java          # Card-based login window
    ├── MainDashboardFrame.java  # Main frame with dark sidebar and CardLayout
    ├── DashboardPanel.java      # Executive KPI overview panel
    ├── ProductPanel.java        # Full catalog CRUD & filter panel
    ├── ProductDialog.java       # Add / Edit product modal dialog
    └── StockDialog.java         # Quick restock adjustment modal
```

---

## 🗄 Database Configuration & Setup

### 1. Existing `products` Table (Preserved Intact)
Your existing table in `inventory_db` is **never** dropped, altered, or overwritten:
* `product_id INT AUTO_INCREMENT PRIMARY KEY`
* `product_name VARCHAR(100) NOT NULL`
* `category VARCHAR(50)`
* `quantity INT NOT NULL DEFAULT 0`
* `price DECIMAL(10,2) NOT NULL`

### 2. Companion `users` Table
To support application logins, a companion `users` table is created:
```sql
USE inventory_db;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
*(You can run `database/setup_users.sql` in MySQL Workbench, or simply start the application — it will automatically create this table if missing!)*

### 3. Local Configuration File (`config.properties`)
A template file `config.example.properties` is provided in the project root.
Create or edit your local `config.properties` file with your MySQL root password:
```properties
db.url=jdbc:mysql://localhost:3306/inventory_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE
inventory.low_stock_threshold=5
```
*(Note: `config.properties` is included in `.gitignore` so your personal database credentials will never be accidentally committed to git).*

---

## 💻 IntelliJ IDEA Import & Run Guide

Follow these exact steps to open and run the project in IntelliJ IDEA:

### Step 1: Open the Project in IntelliJ IDEA
1. Launch **IntelliJ IDEA**.
2. Click **Open** (or go to **File → Open...** in the top menu).
3. Browse to the folder: `c:\Users\merwi\Downloads\Inventory Management`.
4. Select either the folder or the `pom.xml` file and click **OK**.
5. When prompted, choose **"Open as Project"** and select **"Trust Project"**.

### Step 2: Verify Project SDK (JDK 21+)
1. In the top menu, go to **File → Project Structure...** (or press `Ctrl + Alt + Shift + S`).
2. Under **Project Settings**, select **Project**.
3. In the **SDK** dropdown, ensure **Java 21** or **Java 26** is selected.
   - If not listed, click **Add SDK → JDK...** and select your installed JDK path (e.g., `C:\Program Files\Java\jdk-26`).
4. Set **Language Level** to **SDK default** or **21**.
5. Click **Apply** and **OK**.

### Step 3: Reload Maven Dependencies
1. Open the **Maven** tool window on the right sidebar (or view **View → Tool Windows → Maven**).
2. Click the **Reload All Maven Projects** icon (the two circular arrows in the top-left of the Maven panel).
3. Maven will index `mysql-connector-j` and `flatlaf` automatically.

### Step 4: Configure Database Password
1. In IntelliJ's Project Explorer, locate `config.properties` in the project root.
2. Update `db.password` to your MySQL root password:
   ```properties
   db.password=your_actual_password
   ```

### Step 5: Run the Application
1. In the Project Explorer, expand:
   `src` → `main` → `java` → `com` → `inventory` → **`Main.java`**.
2. Right-click `Main.java` and select **Run 'Main.main()'** (or click the green triangle play icon next to `public static void main`).
3. The InvenTrack Pro login window will appear!

---

## ⚡ Command Line Run Guide

You can also test, package, and execute the application using Maven from PowerShell or Command Prompt:

```powershell
# 1. Run unit test suite
mvn test

# 2. Package into an executable JAR
mvn package -DskipTests

# 3. Launch application directly via Maven
mvn exec:java
```

---

## 🔑 Default Application Accounts

On the very first launch, if no user exists, the application automatically registers the initial administrator account:

| Username | Password | Full Name | Role |
| :--- | :--- | :--- | :--- |
| **`admin`** | **`admin`** | System Administrator | ADMIN |

*(You can enter these credentials directly on the login screen to access the system).*

---

## 🎓 College Project Viva / Defense Q&A

When presenting this project to your college professors or examiners, use these authoritative explanations:

### Q1: What architecture did you follow in this project?
> **Answer**: We followed the **Layered MVC / DAO (Data Access Object) Pattern**.
> - The **Model Layer** (`Product`, `User`) contains plain Java POJOs representing table rows.
> - The **DAO Layer** (`ProductDAO`, `UserDAO`) isolates all JDBC SQL operations.
> - The **Service Layer** (`ProductService`, `AuthService`) enforces business validation rules (like preventing negative quantities or blank names) before talking to the database.
> - The **UI Layer** (`LoginFrame`, `MainDashboardFrame`, `ProductPanel`) handles user interaction in Swing.
> This separation of concerns ensures maintainability and testability.

### Q2: Why did you use `PreparedStatement` instead of regular `Statement`?
> **Answer**:
> 1. **Security**: `PreparedStatement` completely prevents **SQL Injection** by treating parameters strictly as literals rather than executable SQL fragments.
> 2. **Performance**: The MySQL server pre-compiles and caches the query execution plan. Subsequent executions reuse the compiled plan with new parameters.
> 3. **Type Safety**: It handles data conversion automatically (e.g. converting Java `BigDecimal` to MySQL `DECIMAL(10,2)` without string formatting bugs).

### Q3: How do you store user passwords securely?
> **Answer**: We use **PBKDF2 with HMAC-SHA256** combined with **unique 128-bit cryptographic salts** (`java.security.SecureRandom`).
> - Plain MD5 or SHA-256 are too fast and vulnerable to precomputed rainbow table attacks.
> - PBKDF2 applies 65,536 hash iterations (work factor), intentionally slowing down brute-force attacks.
> - Each user receives a unique random salt, ensuring identical passwords generate completely different hashes.
> - We also zero out `char[]` arrays immediately after authentication to prevent memory dump inspection.

### Q4: Why use `BigDecimal` for prices instead of `float` or `double`?
> **Answer**: Standard `float` and `double` use binary IEEE 754 floating-point representations, which cannot represent base-10 fractions (like 0.10 or 0.70) accurately and produce rounding errors (e.g., `0.1 + 0.2 = 0.30000000000000004`). In financial and inventory calculations, `BigDecimal` provides arbitrary-precision arithmetic with explicit rounding modes (`RoundingMode.HALF_UP`).

### Q5: What is the Event Dispatch Thread (EDT) and `SwingWorker`?
> **Answer**:
> - Swing is a single-threaded GUI framework: all component creation and UI updates must happen on the **Event Dispatch Thread (EDT)**. We use `SwingUtilities.invokeLater()` to launch the GUI on the EDT.
> - For long-running operations (like MySQL database queries or network requests), running them on the EDT would freeze the application window ("Not Responding"). We use **`SwingWorker`** to perform database queries on background worker threads, and then publish the results back to the EDT smoothly.

### Q6: How do you ensure database connections do not leak?
> **Answer**: We use Java 7+ **`try-with-resources`** blocks for every `Connection`, `PreparedStatement`, and `ResultSet`. The Java compiler automatically generates `finally` clauses that invoke `.close()` on all resources, even if an unexpected `SQLException` or runtime error occurs.

### Q7: How does this project preserve the existing `products` table?
> **Answer**: The application executes zero `DROP TABLE`, `ALTER TABLE`, or destructive DDL on the `products` table. It operates purely via DML (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) on existing records. The auxiliary `users` table is created using `CREATE TABLE IF NOT EXISTS users`, ensuring existing product data is 100% safe.
