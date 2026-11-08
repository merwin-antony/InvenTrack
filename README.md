# 📦 InvenTrack - Smart Inventory Management System

A modern desktop-based Inventory Management System built using **Java, Swing, JDBC, and MySQL**, featuring secure authentication, an executive dashboard, and comprehensive inventory management.

---

## 🌟 Project Overview & Key Features

### A. 🔐 Modern Login Screen

- **Decoupled Application Authentication:** Uses dedicated application user accounts instead of exposing MySQL root credentials.
- **Cryptographic Security:** Passwords are never stored in plain text. Uses **PBKDF2 with HMAC-SHA256** and unique 128-bit cryptographic salts.
- **Automatic Bootstrap:** Automatically creates the companion `users` table and initial administrator account on first startup.
- **Live Connection Diagnostics:** Visual status badge indicating the current MySQL database connection health.

### B. 📊 Executive Dashboard

- **Four Aggregated KPI Stat Cards:**
  1. **Distinct Products:** Total unique products in the catalog (`COUNT(product_id)`).
  2. **Total Units in Stock:** Combined physical quantity across the inventory (`SUM(quantity)`).
  3. **Low-Stock Alerts:** Number of products requiring restocking (`quantity <= 5`).
  4. **Total Inventory Valuation:** Overall inventory value calculated using exact financial arithmetic (`SUM(price * quantity)`).

- **Critical Stock Table:** Highlights out-of-stock and low-stock products, with double-click quick restocking.
- **Quick Actions Panel:** Direct navigation to add products and adjust inventory.

### C. 📦 Product Management

- **Interactive Data Table:** Displays Product ID, Name, Category, Quantity, Unit Price, Total Valuation, and Stock Status.
- **Dynamic Stock Status Badges:** Custom color-coded indicators:
  - 🟢 `IN STOCK`
  - 🟡 `LOW STOCK`
  - 🔴 `OUT OF STOCK`
- **Search & Filters:** Real-time keyword searching by product name or numeric ID, combined with dynamic category dropdown filtering.
- **Add & Edit Modal:** Validates required product names, positive prices, and non-negative quantities.
- **Quick Stock Adjustment:** Instantly perform restocking or outward stock dispatch without opening the full edit form.
- **Safe Deletion:** Two-step confirmation dialog before permanently removing catalog items.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Graphical User Interface |
| JDBC | Database connectivity |
| MySQL | Relational database management |
| PBKDF2 / HMAC-SHA256 | Password hashing |
| Git & GitHub | Version control |

---

## 🏗️ Project Architecture

InvenTrack follows a layered architecture to ensure modularity, maintainability, and separation of concerns.

```text
src/main/java/com/inventory/
│
├── Main.java
│
├── config/
│   ├── DatabaseConfig.java
│   └── DatabaseInitializer.java
│
├── model/
│   ├── Product.java
│   ├── User.java
│   └── InventoryStats.java
│
├── dao/
│   ├── ProductDAO.java
│   └── UserDAO.java
│
├── service/
│   ├── ProductService.java
│   ├── AuthService.java
│   ├── PasswordHasher.java
│   ├── ValidationException.java
│   └── AuthenticationException.java
│
└── ui/
    ├── theme/
    │   ├── UITheme.java
    │   └── CustomComponents.java
    │
    ├── LoginFrame.java
    ├── MainDashboardFrame.java
    ├── DashboardPanel.java
    ├── ProductPanel.java
    ├── ProductDialog.java
    └── StockDialog.java
```

### 📁 Package Responsibilities

| Package | Responsibility |
|---|---|
| `config` | Database configuration and initialization |
| `model` | Product, user, and inventory data models |
| `dao` | Parameterized JDBC queries and database operations |
| `service` | Business logic, authentication, and validation |
| `ui` | Application windows, panels, and dialogs |
| `ui.theme` | UI styling, reusable components, and design system |

### 🔄 Application Architecture

```text
          User
           │
           ▼
      Swing UI Layer
           │
           ▼
       Service Layer
           │
           ▼
         DAO Layer
           │
           ▼
       JDBC Driver
           │
           ▼
      MySQL Database
```

---

## 🔐 Security Implementation

Security is an important part of InvenTrack.

- **Password Hashing:** PBKDF2WithHmacSHA256 with unique 128-bit salts.
- **SQL Injection Prevention:** Parameterized SQL queries through JDBC.
- **Credential Separation:** Dedicated application authentication independent of MySQL administrator credentials.
- **Secure Configuration:** Database credentials stored separately in `config.properties`.
- **Version Control Protection:** Sensitive configuration files excluded using `.gitignore`.

---

## 🗒️ Database Configuration & Setup

### 1. Existing `products` Table (Preserved Intact)
Your existing table in `inventory_db` is **never** dropped, altered, or overwritten:
```sql
USE inventory_db;

CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(10,2) NOT NULL
);
```

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

---

## ⚙️ Getting Started

### Prerequisites

- Java Development Kit (JDK)
- MySQL Server
- IntelliJ IDEA or another compatible Java IDE

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/InvenTrack.git
```

### 2. Open the Project

Open the cloned project in IntelliJ IDEA.

### 3. Configure MySQL

- Install and start MySQL Server.
- Create the required database.
- Execute the SQL scripts provided in the `database/` directory.

### 4. Configure Database Credentials

A template file `config.example.properties` is provided in the project root.
Create or edit your local `config.properties` file with your MySQL root password:
```properties
db.url=jdbc:mysql://localhost:3306/inventory_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE
inventory.low_stock_threshold=5
```

Update the configuration with your own MySQL credentials.

### 5. Launch InvenTrack

Run `Main.java` from your IDE.

The application will initialize the required user table and initial administrator account automatically on first startup.

---

## 🔮 Future Enhancements

- Inventory analytics with visual charts.
- Automated low-stock notifications.
- Exportable inventory reports.
- Advanced user roles and permissions.

---
