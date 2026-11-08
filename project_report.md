# INVENTRACK — INVENTORY MANAGEMENT SYSTEM
## A PROJECT REPORT

---

**Submitted by**
* **A MERWIN** (Reg. No.: `310625104202`)
* **MOHAMED JAVITH S** (Reg. No.: `310625104206`)
* **NITHISH S** (Reg. No.: `310625104240`)

*in partial fulfilment of the requirements for the award of the degree of*  
**BACHELOR OF ENGINEERING**  
*IN*  
**COMPUTER SCIENCE AND ENGINEERING**

<br>

**DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING**  
**[COLLEGE NAME]**  
*(An Autonomous Institution Affiliated to Anna University)*  
**[CITY — PIN CODE, TAMIL NADU, INDIA]**  
**OCTOBER 2026**

---

<div style="page-break-after: always;"></div>

## BONAFIDE CERTIFICATE

Certified that this project report titled **“INVENTRACK — INVENTORY MANAGEMENT SYSTEM”** is the bonafide work of:

* **A MERWIN** (`310625104202`)
* **MOHAMED JAVITH S** (`310625104206`)
* **NITHISH S** (`310625104240`)

who carried out the project work under my supervision in the Department of Computer Science and Engineering, **[COLLEGE NAME]**, during the academic year 2026–2027.

<br><br><br>

| __________________________________________ | __________________________________________ |
| :--- | :--- |
| **HEAD OF THE DEPARTMENT** | **PROJECT GUIDE / SUPERVISOR** |
| **[HOD NAME, Ph.D.]** | **[GUIDE NAME, M.E., (Ph.D.)]** |
| Professor & Head | Assistant Professor |
| Department of Computer Science & Engineering | Department of Computer Science & Engineering |
| [College Name] | [College Name] |
| [City — Pin Code] | [City — Pin Code] |

<br><br>

---

<div style="page-break-after: always;"></div>

## CERTIFICATE OF VIVA-VOCE EXAMINATION

This is to certify that the Viva-Voce Examination for the project work entitled **“INVENTRACK — INVENTORY MANAGEMENT SYSTEM”** submitted by:

* **A MERWIN** (`310625104202`)
* **MOHAMED JAVITH S** (`310625104206`)
* **NITHISH S** (`310625104240`)

in partial fulfilment of the requirements for the award of the degree of **Bachelor of Engineering in Computer Science and Engineering**, was held on `____________________` in the Department of Computer Science and Engineering, **[COLLEGE NAME]**.

<br><br><br>

| **INTERNAL EXAMINER** | **EXTERNAL EXAMINER** |
| :--- | :--- |
| **Signature:** `_____________________________` | **Signature:** `_____________________________` |
| **Name:** `________________________________` | **Name:** `________________________________` |
| **Designation:** `_________________________` | **Designation:** `_________________________` |
| **Institution:** `_________________________` | **Institution:** `_________________________` |
| **Date:** `________________________________` | **Date:** `________________________________` |

<br>

---

<div style="page-break-after: always;"></div>

## DECLARATION

We hereby declare that the project report entitled **“InvenTrack — Inventory Management System”** is an authentic record of the engineering work carried out independently by us in partial fulfilment of the requirements for the award of the degree of **Bachelor of Engineering in Computer Science and Engineering** at **[COLLEGE NAME]**.

The technical details, system architectural patterns, Java 21 desktop implementations, FlatLaf GUI specifications, MySQL relational database designs, cryptographic PBKDF2-based password hashing protocols, and test suites presented in this report reflect the genuine source code and database artifacts crafted by our team.

This submission has not been submitted previously to any university or institution for the award of any diploma, degree, or other academic honor.

<br><br><br>

| CANDIDATE NAME | REGISTER NUMBER | SIGNATURE WITH DATE |
| :--- | :--- | :--- |
| **A MERWIN** | `310625104202` | `___________________________` |
| **MOHAMED JAVITH S** | `310625104206` | `___________________________` |
| **NITHISH S** | `310625104240` | `___________________________` |

<br>

---

<div style="page-break-after: always;"></div>

## ABSTRACT

Accurate inventory records represent the operational backbone of organizations dealing with procurement, warehousing, retailing, and physical asset distribution. Traditional tracking mechanisms—such as physical paper registers or disconnected spreadsheets—frequently trigger inventory inaccuracies, data redundancy, missed stock-out alerts, arithmetic errors in financial valuation, and heightened operational vulnerability.

**InvenTrack** is an enterprise-styled, modern desktop Inventory Management System developed using **Core Java 21**, **Java Swing**, and **Java Database Connectivity (JDBC)**, built on top of a relational **MySQL 8.0** engine. Organized strictly under a decoupled multi-tier architectural pattern (Presentation, Service, Data Access Object, and Model layers), InvenTrack eliminates heavy, heavyweight web container dependencies while preserving modularity, testability, and responsiveness.

Key features include:
1. **Interactive Dashboard**: Real-time Key Performance Indicator (KPI) metrics displaying distinct catalog count, total units on hand, critical low-stock thresholds, and total valuation rendered strictly in **Indian Rupee (₹ Lakhs & Crores)** numerical notation.
2. **Product Catalog Management**: Comprehensive CRUD capabilities with multi-criteria dynamic search by alphanumeric product name or numeric product ID, combined with category filtering.
3. **Controlled Stock Adjustments**: Modal-driven inward (restocking) and outward (dispatch) inventory adjustments enforced by service-layer invariants to prevent negative stock quantities.
4. **Security & Authentication**: Cryptographically robust user authentication and self-service **Sign Up** system utilizing `PBKDF2WithHmacSHA256` key derivation with 16-byte random salts, 65,536 iterations, constant-time `MessageDigest.isEqual` comparison, and proactive heap memory scrubbing of password character arrays.
5. **Modern Widescreen UX**: An ergonomic split-screen interface styled with the **FlatLaf** design framework, featuring custom asynchronous execution via `SwingWorker` threads to ensure non-blocking UI responsiveness on the Event Dispatch Thread (EDT).

The application is thoroughly verified through automated **JUnit 5** test suites validating model integrity, business rules, and cryptographic operations, achieving a 100% pass rate.

---

<div style="page-break-after: always;"></div>

## ACKNOWLEDGEMENT

First and foremost, we offer our heartfelt gratitude to the Almighty for bestowing upon us the wisdom, health, perseverance, and clarity required to bring this project to fruition.

We convey our profound thanks to the Management and our respected Principal, **[PRINCIPAL NAME, Ph.D.]**, of **[COLLEGE NAME]**, for establishing a modern research atmosphere and providing state-of-the-art laboratory and computing infrastructure.

We extend our deep gratitude to our Head of the Department, **[HOD NAME, Ph.D.]**, Department of Computer Science and Engineering, for their visionary guidance, unwavering encouragement, and administrative support throughout the development phase.

We are deeply indebted to our supervisor and project guide, **[GUIDE NAME, M.E., (Ph.D.)]**, Assistant Professor, Department of Computer Science and Engineering, for providing constructive critiques, architectural insights, meticulous reviews, and scholarly encouragement.

We also thank all the teaching and non-teaching faculty members of the Department of Computer Science and Engineering for their constant academic engagement. Finally, we express our warmest love and gratitude to our parents, family members, and friends whose faith, encouragement, and sacrifices inspired us at every juncture of this journey.

<br>
**A MERWIN**  
**MOHAMED JAVITH S**  
**NITHISH S**  

---

<div style="page-break-after: always;"></div>

## TABLE OF CONTENTS

| CHAPTER | TITLE | PAGE NO. |
| :---: | :--- | :---: |
| | **ABSTRACT** | **iv** |
| | **ACKNOWLEDGEMENT** | **v** |
| | **LIST OF TABLES** | **viii** |
| | **LIST OF FIGURES** | **ix** |
| | **LIST OF SYMBOLS AND ABBREVIATIONS** | **x** |
| **1** | **INTRODUCTION** | **1** |
| 1.1 | Project Overview & Real-World Context | 1 |
| 1.2 | Problem Statement & Industry Motivation | 2 |
| 1.3 | Project Objectives | 3 |
| 1.4 | Scope and Boundaries of the Application | 4 |
| 1.5 | Report Organization | 5 |
| **2** | **LITERATURE REVIEW & THEORETICAL FOUNDATIONS** | **6** |
| 2.1 | Evolution of Inventory Management Paradigms | 6 |
| 2.2 | Review of Architecture: Desktop Rich Client vs. Web Frameworks | 8 |
| 2.3 | Database Persistence: JDBC vs. Object Relational Mapping (ORM) | 9 |
| 2.4 | Cryptographic Standards in Application Authentication | 10 |
| 2.5 | Comparative Analysis of Existing Solutions | 12 |
| **3** | **SYSTEM ANALYSIS & REQUIREMENTS SPECIFICATION** | **14** |
| 3.1 | Detailed Problem Definition | 14 |
| 3.2 | Proposed InvenTrack Solution | 15 |
| 3.3 | Software Requirements Specification (SRS) | 16 |
| 3.3.1 | Functional Requirements (FR1 – FR12) | 16 |
| 3.3.2 | Non-Functional Requirements (NFR1 – NFR8) | 18 |
| 3.4 | Hardware & Software Environment Specifications | 20 |
| 3.5 | Use Case Modeling & Actor Interaction Descriptions | 21 |
| **4** | **SYSTEM ARCHITECTURE & DESIGN** | **24** |
| 4.1 | Architectural Design & Layered Pattern | 24 |
| 4.2 | Object-Oriented Class Design & Component Hierarchy | 26 |
| 4.3 | Relational Database Design & Schema Specifications | 29 |
| 4.3.1 | Entity Relationship (ER) Modeling | 29 |
| 4.3.2 | Data Dictionary & Table Definitions (`products`, `users`) | 31 |
| 4.4 | Dynamic Behavioral Modeling | 33 |
| 4.4.1 | Sequence Diagram: Cryptographic User Authentication & Sign-Up | 33 |
| 4.4.2 | Sequence Diagram: Dynamic Product Search & Category Filtering | 35 |
| 4.4.3 | Sequence Diagram: Inward/Outward Stock Adjustment | 36 |
| 4.5 | User Interface (UI) Design System & Ergonomics | 37 |
| **5** | **SYSTEM IMPLEMENTATION** | **40** |
| 5.1 | Project Structure & Maven Build Lifecycle | 40 |
| 5.2 | Robust Database Layer: `DatabaseConfig` & `DatabaseInitializer` | 42 |
| 5.3 | Cryptographic Security Module: `PasswordHasher` & `AuthService` | 44 |
| 5.4 | Domain Model & Service Validation Invariants | 46 |
| 5.5 | High-Performance Data Access Objects (`ProductDAO`, `UserDAO`) | 48 |
| 5.6 | Desktop Presentation Layer with Swing & FlatLaf Integration | 50 |
| 5.6.1 | Widescreen Split-Hero Login Portal & Sign-Up Modal | 50 |
| 5.6.2 | Real-Time Executive Dashboard & KPI Calculation | 52 |
| 5.6.3 | Comprehensive Product Catalog & Stock Dialogues | 53 |
| **6** | **SYSTEM TESTING & QUALITY ASSURANCE** | **55** |
| 6.1 | Testing Methodology & Verification Strategy | 55 |
| 6.2 | Unit Testing Specifications & Assertions (JUnit 5) | 56 |
| 6.2.1 | Cryptographic Test Suite: `PasswordHasherTest` | 56 |
| 6.2.2 | Business Logic Test Suite: `ProductValidationTest` | 57 |
| 6.3 | Test Execution Summary & Verification Metrics | 58 |
| 6.4 | Recommended Future Integration Test Suites | 59 |
| **7** | **SYSTEM OUTPUTS & SCREENSHOT DOCUMENTATION** | **61** |
| 7.1 | UI Showcase 1: Split-Screen Widescreen Login Interface | 61 |
| 7.2 | UI Showcase 2: User Sign-Up & Registration Modal | 63 |
| 7.3 | UI Showcase 3: Real-Time Executive KPI Dashboard | 64 |
| 7.4 | UI Showcase 4: Searchable Product Catalog & Ascending Sort Grid | 66 |
| 7.5 | UI Showcase 5: Product CRUD Modal & Stock Adjustment Engine | 68 |
| **8** | **RESULTS, EVALUATION & DISCUSSION** | **70** |
| 8.1 | Functional Achievement Against Target Objectives | 70 |
| 8.2 | Security & Cryptographic Robustness Evaluation | 71 |
| 8.3 | Data Consistency, Indian Financial Representation & Performance | 72 |
| 8.4 | Known Constraints & Risk Considerations | 73 |
| **9** | **CONCLUSION & FUTURE ENHANCEMENTS** | **75** |
| 9.1 | Summary of Research & Engineering Contributions | 75 |
| 9.2 | Roadmap for Future Enhancements | 76 |
| | **APPENDIX A: SELECTED SOURCE CODE EXCERPTS** | **78** |
| | **APPENDIX B: DATABASE DDL & SYSTEM CONFIGURATION** | **82** |
| | **REFERENCES** | **84** |

---

<div style="page-break-after: always;"></div>

## LIST OF TABLES

| TABLE NO. | TITLE | PAGE NO. |
| :---: | :--- | :---: |
| **2.1** | Comparative matrix of inventory management software patterns | 13 |
| **3.1** | Functional Requirements Specification (FR1 – FR12) | 16 |
| **3.2** | Non-Functional Requirements Specification (NFR1 – NFR8) | 18 |
| **3.3** | Target hardware environment specification | 20 |
| **3.4** | Target software environment and runtime dependency matrix | 20 |
| **3.5** | Use case tabular descriptions and operational flows | 22 |
| **4.1** | Architectural software layers and package encapsulation | 25 |
| **4.2** | Principal core classes and their single-responsibility mappings | 27 |
| **4.3** | Data dictionary for `products` table | 31 |
| **4.4** | Data dictionary for `users` table | 32 |
| **4.5** | InvenTrack UI theme design token specifications | 38 |
| **5.1** | Maven project configuration and dependency coordinates | 41 |
| **5.2** | Cryptographic key derivation parameters | 44 |
| **5.3** | Dashboard aggregate expressions mapped to SQL functions | 52 |
| **6.1** | Cryptographic unit test specifications (`PasswordHasherTest`) | 56 |
| **6.2** | Domain validation unit test specifications (`ProductValidationTest`) | 57 |
| **6.3** | Recorded automated test execution summary | 58 |
| **6.4** | Proposed integration and stress test cases | 59 |
| **8.1** | Traceability matrix: objectives achieved vs. source code validation | 70 |
| **B.1** | Application runtime properties (`config.properties`) | 83 |

---

<div style="page-break-after: always;"></div>

## LIST OF FIGURES

| FIGURE NO. | TITLE | PAGE NO. |
| :---: | :--- | :---: |
| **1.1** | High-level operational workflow of InvenTrack | 3 |
| **3.1** | UML Use Case diagram for InvenTrack desktop application | 21 |
| **4.1** | Layered component architecture diagram (Presentation, Service, DAO, DB) | 24 |
| **4.2** | Comprehensive UML Class Diagram illustrating object relationships | 28 |
| **4.3** | Entity Relationship (ER) diagram for MySQL `inventory_db` database | 30 |
| **4.4** | UML Sequence diagram: Cryptographic authentication & registration | 34 |
| **4.5** | UML Sequence diagram: Multi-criteria product search & filtering | 35 |
| **4.6** | UML Sequence diagram: Stock inward/outward adjustment with invariant check | 36 |
| **7.1** | Output Showcase: Modern Split-Screen Login Window | 62 |
| **7.2** | Output Showcase: User Sign-Up Modal Dialog | 63 |
| **7.3** | Output Showcase: Executive KPI Dashboard & Stock Alert Monitor | 65 |
| **7.4** | Output Showcase: Searchable Product Catalog with Indian Rupee formatting | 67 |
| **7.5** | Output Showcase: Stock Adjustment & Product Modal Dialogs | 69 |

---

<div style="page-break-after: always;"></div>

## LIST OF SYMBOLS AND ABBREVIATIONS

| ACRONYM / SYMBOL | EXPANSION / DEFINITION |
| :--- | :--- |
| **API** | Application Programming Interface |
| **ASCII** | American Standard Code for Information Interchange |
| **B.E.** | Bachelor of Engineering |
| **CRUD** | Create, Read, Update, and Delete |
| **CSE** | Computer Science and Engineering |
| **DAO** | Data Access Object pattern |
| **DBMS** | Database Management System |
| **DML** | Data Manipulation Language |
| **EDT** | Event Dispatch Thread (Swing UI thread) |
| **ER** | Entity-Relationship |
| **GUI** | Graphical User Interface |
| **HMAC** | Hash-based Message Authentication Code |
| **IDE** | Integrated Development Environment |
| **JAR** | Java Archive |
| **JDBC** | Java Database Connectivity |
| **JDK** | Java Development Kit |
| **JVM** | Java Virtual Machine |
| **KPI** | Key Performance Indicator |
| **NFR** | Non-Functional Requirement |
| **ORM** | Object-Relational Mapping |
| **PBKDF2** | Password-Based Key Derivation Function 2 |
| **POJO** | Plain Old Java Object |
| **RDBMS** | Relational Database Management System |
| **SHA-256** | Secure Hash Algorithm (256-bit digest) |
| **SQL** | Structured Query Language |
| **SRS** | Software Requirements Specification |
| **UI / UX** | User Interface / User Experience |
| **UML** | Unified Modeling Language |
| **₹ (INR)** | Indian Rupee currency symbol |

---

<div style="page-break-after: always;"></div>

# CHAPTER 1: INTRODUCTION

### 1.1 Project Overview & Real-World Context
Inventory tracking is a mission-critical operational requirement across commercial enterprises, retail distributions, academic laboratory facilities, and industrial warehouses. In any commercial undertaking that manages physical goods, inventory represents both liquid capital and direct operational capability. 

Historically, small and medium enterprises (SMEs) have relied on manual book-keeping or basic desktop spreadsheet sheets (such as Microsoft Excel or Google Sheets) to track their product catalogs. While spreadsheets offer initial accessibility, they rapidly degrade as inventory catalogs scale. Multiple concurrent operators, copy-paste inconsistencies, accidental formula overwrites, and the lack of strict schema validation inevitably lead to "phantom stock"—discrepancies between digital logs and physical warehouse shelves.

**InvenTrack** has been architected to eliminate these operational failure modes by providing a standalone, high-performance desktop application engineered with **Java 21**, **Java Swing**, and **MySQL 8.0**. Built with modern software design principles, InvenTrack encapsulates business logic, data persistence, and ergonomic visual presentation into a clean, maintainable architecture.

```mermaid
graph TD
    A[Launch InvenTrack Application] --> B[Initialize Database & Verify MySQL Connection]
    B --> C[Check Users Table & Seed Default Admin if Empty]
    C --> D[Display Modern Split-Hero Login Screen]
    D --> E{User Authenticated?}
    E -- No: Invalid Credentials --> D
    E -- No: New Account --> F[Open Sign-Up Modal Dialog]
    F --> G[PBKDF2 Salted Hashing & Insert User]
    G --> D
    E -- Yes --> H[Launch Main Application Frame]
    H --> I[Executive KPI Dashboard]
    H --> J[Product Catalog & Search Grid]
    H --> K[Inward / Outward Stock Adjustments]
    H --> L[Dynamic Category Filtering]
```
<center><b>Figure 1.1:</b> High-level operational workflow of InvenTrack</center>

---

### 1.2 Problem Statement & Industry Motivation
Manual and semi-automated inventory monitoring systems suffer from distinct system vulnerabilities:
1. **Lack of Strict Data Integrity**: Spreadsheet cells allow arbitrary string entry where decimal unit prices or positive integers are required, leading to corrupted aggregate valuations.
2. **Untracked Stock Reductions**: Outward movements carried out without runtime boundary validation can plunge stock numbers into negative balances.
3. **Absence of Real-Time Alerting**: Managers lack automated mechanisms to highlight items hovering below minimum safety stock levels, resulting in delayed vendor replenishment and lost sales.
4. **Inadequate Security**: Plaintext password storage or shared workbook passwords expose inventory valuations and trade pricing to unauthorized tampering.
5. **Clunky Interfaces**: Legacy enterprise software built in early Java Swing iterations often presents unintuitive, gray-box interfaces that increase operator fatigue.

InvenTrack resolves these issues by pairing an expressive modern UI with service-layer validation invariants, parameterized database execution, cryptographic password protection, and real-time dashboard analytics.

---

### 1.3 Project Objectives
The engineering goals established for InvenTrack are:
* **Pure Core Java 21 Desktop Architecture**: Construct a modular desktop application utilizing the modern JDK 21 platform and Java Swing, bypassing bloated web frameworks to ensure rapid local execution and minimal memory overhead.
* **Separation of Concerns via Layered Architecture**: Decouple the software into explicit Presentation (`ui`), Service (`service`), Data Access Object (`dao`), and Domain Model (`model`) packages.
* **Resilient Relational Persistence via JDBC**: Implement parameterized SQL statements using `PreparedStatement` to ensure complete immunity against SQL injection attacks, interacting natively with MySQL 8.0.
* **Cryptographically Robust Security**: Protect administrative credentials using `PBKDF2WithHmacSHA256` hashing accompanied by 16-byte cryptographically secure random salts and memory-zeroing heap cleanup.
* **Dual-Action Stock Adjustment Engine**: Provide atomic inward and outward stock adjustments backed by mathematical invariants guaranteeing non-negative warehouse balances.
* **Localized Indian Financial Reporting**: Ensure all monetary inventory valuations are computed using precision `BigDecimal` math and formatted in accordance with the Indian numbering format (`₹ Lakhs and Crores`).
* **Automated Unit Test Verification**: Verify cryptographic routines and data validation rules using JUnit 5 test suites.

---

### 1.4 Scope and Boundaries of the Application
**Within InvenTrack’s Implemented Scope:**
* Administrative authentication with self-service user registration (**Sign Up**).
* Real-time executive dashboard calculating total distinct items, total physical quantity, critical low-stock count, and cumulative stock valuation.
* Full CRUD (Create, Read, Update, Delete) product catalog capabilities.
* Multi-criteria search (numeric ID exact match, substring name match, and category filtering).
* Sort orders preserved strictly in ascending product identifier sequence (`product_id ASC`).
* Dynamic stock adjustment modals with live preview calculations.
* Automated database schema verification and initial administrator bootstrapping.

**Out of Current Academic Scope (Future Roadmap):**
* Multi-warehouse geographic location tracking.
* Automated barcode scanner hardware integrations.
* Direct vendor purchase order dispatch via SMTP/Email.
* Transactional billing, GST tax invoice generation, and point-of-sale receipt printing.

---

### 1.5 Report Organization
* **Chapter 2 (Literature Review)** analyzes inventory management theories, architectural comparisons (Desktop vs. Web), persistence strategies, and cryptographic hashing standards.
* **Chapter 3 (System Analysis)** details problem definitions, functional/non-functional requirements, and use case interactions.
* **Chapter 4 (System Design)** presents UML architectural, class, sequence, and database ER diagrams.
* **Chapter 5 (System Implementation)** examines core Java code, cryptographic algorithms, DAO queries, and UI components.
* **Chapter 6 (System Testing)** outlines test plans, JUnit 5 assertions, and test run summaries.
* **Chapter 7 (System Outputs)** features documented visual showcases of the running UI.
* **Chapter 8 (Results & Discussion)** critically evaluates the project against its objectives, data integrity, and limitations.
* **Chapter 9 (Conclusion & Future Work)** summarizes project achievements and outlines upcoming enhancements.

---

<div style="page-break-after: always;"></div>

# CHAPTER 2: LITERATURE REVIEW & THEORETICAL FOUNDATIONS

### 2.1 Evolution of Inventory Management Paradigms
Inventory management has evolved through three distinct technological eras:
1. **Manual Ledger Systems (Pre-1970s)**: Physical register books tracking batch movements manually. While simple, ledgers suffered from human transcription errors, physical damage, and zero search capabilities.
2. **Spreadsheet & Disconnected Files (1980s–2000s)**: Introduction of VisiCalc, Lotus 1-2-3, and Microsoft Excel. Although allowing programmatic calculations, standalone sheets created "data silos" where multiple employees edited unsynchronized copies.
3. **Database-Driven Centralized Systems (2010s–Present)**: Modern systems utilize relational databases (RDBMS) adhering to ACID (Atomicity, Consistency, Isolation, Durability) properties. Centralized databases guarantee that physical counts reflect digital data in real time.

InvenTrack implements a perpetual inventory strategy where stock counts adjust dynamically upon each confirmed transaction.

---

### 2.2 Review of Architecture: Desktop Rich Client vs. Web Frameworks
In enterprise software engineering, choosing between a desktop client and a web application involves evaluating trade-offs in latency, security, resource consumption, and infrastructure complexity:

* **Web Frameworks (Spring Boot, React, Node.js)**:
  * *Strengths*: Accessible through any web browser; zero client-side installation.
  * *Weaknesses*: High memory footprint, web server hosting costs, complex network configurations, latency, and vulnerabilities (XSS, CSRF, session hijacking).
* **Desktop Rich Clients (Core Java, Swing, FlatLaf)**:
  * *Strengths*: Direct OS integration, deterministic memory usage, native hardware acceleration, offline execution resilience, and instant startup times.
  * *Suitability for InvenTrack*: By leveraging **Java 21** and **FlatLaf**, InvenTrack provides a modern desktop user experience that runs directly on office workstations without needing application servers, container virtualization, or ongoing cloud hosting fees.

---

### 2.3 Database Persistence: JDBC vs. Object Relational Mapping (ORM)
Object-Relational Mapping libraries (e.g., Hibernate, JPA) map database tables to Java objects automatically. While convenient in massive enterprise domains, ORMs introduce significant drawbacks:
* "N+1 Query" performance bottlenecks.
* Hidden SQL generation that obfuscates index optimization.
* Substantial memory consumption and heavy jar footprint.

**Java Database Connectivity (JDBC)**, coupled with the **Data Access Object (DAO)** pattern, offers direct, predictable control over every SQL query. By crafting optimized SQL statements and binding parameters through `PreparedStatement`, InvenTrack attains peak query execution speeds and guarantees compile-time transparency.

---

### 2.4 Cryptographic Standards in Application Authentication
Historical systems frequently utilized fast cryptographic hash functions like MD5 or SHA-1 for password storage. Modern cryptanalysis has demonstrated that fast hashing algorithms are fatally flawed due to:
1. **Collision Vulnerabilities**: Two different plaintext inputs generating identical hash digests.
2. **GPU-Accelerated Rainbow Table Attacks**: Hardware can compute billions of SHA-1 hashes per second.

To withstand modern dictionary attacks, password storage requires a **Key Derivation Function (KDF)** designed to be computationally intensive:
$$\text{DerivedKey} = \text{PBKDF2}(\text{PRF}, \text{Password}, \text{Salt}, c, \text{dkLen})$$

* **Algorithm**: `PBKDF2WithHmacSHA256` (Password-Based Key Derivation Function 2 with HMAC-SHA256).
* **Salt**: A 128-bit (16-byte) cryptographically secure random value generated via `java.security.SecureRandom`. Salting ensures identical passwords generate completely different hashes, neutralizing precomputed rainbow tables.
* **Work Factor (Iterations)**: $65,536$ rounds, substantially raising the computational cost for brute-force attacks.
* **Timing-Attack Resistance**: Verification uses `MessageDigest.isEqual()`, comparing byte arrays in constant time to prevent side-channel timing analysis.

---

### 2.5 Comparative Analysis of Existing Solutions

<center><b>Table 2.1:</b> Comparative matrix of inventory management software patterns</center>

| ATTRIBUTE | PHYSICAL REGISTERS | SPREADSHEET FILES | WEB-BASED ERP (SAAS) | INVENTRACK (PROPOSED) |
| :--- | :--- | :--- | :--- | :--- |
| **Data Integrity** | Poor (Manual) | Moderate (Easily Corrupted) | High (Database Enforced) | **High (ACID + Service Rules)** |
| **Search Speed** | Exhaustive Manual | Moderate (Column Filter) | Fast (Server Index) | **Instantaneous (< 5ms Query)** |
| **Stock Alerts** | None | Conditional Formatting | Email / Push Alerts | **Visual Color-Coded Badges** |
| **Security** | Physical Locks Only | Password-Protected Sheets | Multi-Factor Web Auth | **Salted PBKDF2 Hashing** |
| **Operating Cost**| Continuous Paper Cost| Office License | Monthly Recurring Cost | **Zero (Free & Open Source)** |
| **Hosting Overhead**| None | Shared Network Folder | Cloud Server Maintenance | **Local Workstation / RDBMS** |

---

<div style="page-break-after: always;"></div>

# CHAPTER 3: SYSTEM ANALYSIS & REQUIREMENTS SPECIFICATION

### 3.1 Detailed Problem Definition
Organizations operating retail stores, warehouse points, or academic labs face severe operational bottlenecks when tracking product catalogs without dedicated management software:
* Inability to locate items rapidly during customer queries.
* Unchecked stock reductions causing inventory deficits.
* Discrepancies between ledger-recorded balances and physical counts.
* Lack of consolidated financial visibility (total stock value).
* Unprotected administrative access leading to unauthorized inventory modifications.

InvenTrack resolves these issues by pairing an expressive modern desktop UI with service-layer validation invariants, parameterized database execution, cryptographic password protection, and real-time dashboard analytics.

---

### 3.2 Proposed InvenTrack Solution
InvenTrack provides a comprehensive desktop application featuring:
1. **Secure Dual-Access Authentication**: Administrative login paired with an integrated **Sign Up** modal for registering new staff or manager accounts.
2. **Real-Time Executive KPI Dashboard**: Dynamically computes Total Products, Total Physical Units, Critical Low Stock Alerts, and Gross Inventory Valuation.
3. **Ascending Data Grids**: Clear product catalogs and alert tables automatically sorted in ascending identifier order (`product_id ASC`).
4. **Controlled Inward & Outward Stock Management**: Real-time adjustment dialogs that calculate live quantity previews while rejecting reductions that would produce negative balances.
5. **Indian Currency Formatting**: Complete numerical reporting styled to the Indian financial numbering system (`₹ Lakhs and Crores`).

---

### 3.3 Software Requirements Specification (SRS)

#### 3.3.1 Functional Requirements (FR)

<center><b>Table 3.1:</b> Functional Requirements Specification</center>

| REQ ID | REQUIREMENT STATEMENT | MODULE |
| :---: | :--- | :---: |
| **FR-01** | The system shall authenticate users using username and password against the `users` table. | Authentication |
| **FR-02** | The system shall provide a **Sign Up** dialog allowing new users to create accounts with full name, username, password, and role. | Authentication |
| **FR-03** | The system shall hash passwords using `PBKDF2WithHmacSHA256` with unique 16-byte random salts. | Security |
| **FR-04** | The system shall display executive KPI cards (Total Products, Total Units, Low Stock Count, Total Valuation). | Dashboard |
| **FR-05** | The system shall display critical stock alerts for items with quantities at or below the safety threshold. | Dashboard |
| **FR-06** | The system shall retrieve and display all products ordered by `product_id ASC`. | Catalog |
| **FR-07** | The system shall support dynamic search matching numeric product IDs or substring product names. | Catalog |
| **FR-08** | The system shall filter products dynamically by category selected from a dropdown. | Catalog |
| **FR-09** | The system shall validate product names, categories, quantities ($\ge 0$), and prices ($\ge 0$) before saving. | Product Service |
| **FR-10** | The system shall update existing product attributes and delete chosen records upon user confirmation. | Product Service |
| **FR-11** | The system shall allow inward and outward stock adjustments, rejecting deductions causing negative stock. | Stock Service |
| **FR-12** | The system shall format all financial values into Indian Rupees (`₹ #,##,##0.00`). | Presentation |

#### 3.3.2 Non-Functional Requirements (NFR)

<center><b>Table 3.2:</b> Non-Functional Requirements Specification</center>

| NFR ID | ATTRIBUTE | SPECIFICATION & IMPLEMENTATION BASIS |
| :---: | :--- | :--- |
| **NFR-01** | **Performance** | Database search queries shall return and render within 200 milliseconds. |
| **NFR-02** | **UI Responsiveness** | Database operations must execute on background `SwingWorker` threads to prevent EDT freezing. |
| **NFR-03** | **Data Integrity** | Stock quantities and unit prices must strictly adhere to non-negative invariants. |
| **NFR-04** | **Financial Precision**| All price calculations must use `BigDecimal` to eliminate floating-point rounding errors. |
| **NFR-05** | **Security** | Plaintext passwords must never be stored in persistent storage or maintained in memory strings. |
| **NFR-06** | **SQL Injection Immunity**| All SQL transactions must strictly use `PreparedStatement` parameter bindings. |
| **NFR-07** | **Portability** | The software shall execute identically on Windows, Linux, and macOS platforms via JDK 21+. |
| **NFR-08** | **Ergonomics & Design**| The GUI shall utilize FlatLaf design tokens with consistent padding, borders, and typography. |

---

### 3.4 Hardware & Software Environment Specifications

<center><b>Table 3.3:</b> Target hardware environment specification</center>

| HARDWARE PARAMETER | MINIMUM SPECIFICATION | RECOMMENDED SPECIFICATION |
| :--- | :--- | :--- |
| **Processor** | Intel Core i3 / AMD Ryzen 3 (2.0 GHz) | Intel Core i5 / AMD Ryzen 5 (2.5 GHz or higher) |
| **System Memory (RAM)** | 4.0 GB | 8.0 GB or higher |
| **Storage Capacity** | 500 MB free space (Application + DB) | 2.0 GB SSD storage |
| **Display Resolution** | $1024 \times 768$ pixels | $1920 \times 1080$ Full HD |
| **Input Peripherals** | Standard Keyboard and Mouse | Keyboard and Optical Mouse |

<br>

<center><b>Table 3.4:</b> Target software environment and runtime dependency matrix</center>

| SOFTWARE LAYER | TECHNOLOGY / TOOL | VERSION SPECIFICATION |
| :--- | :--- | :--- |
| **Operating System** | Microsoft Windows / Ubuntu Linux / macOS | Windows 10 / 11 (64-bit) verified |
| **Java Platform** | Java Development Kit (OpenJDK / Oracle JDK) | **JDK 21 (LTS)** or higher (JDK 26 ready) |
| **Database Engine** | MySQL Community Server | **MySQL 8.0** or higher |
| **JDBC Driver** | MySQL Connector/J | **8.3.0** |
| **UI Look and Feel** | FormDev FlatLaf Modern Desktop Library | **3.5.4** |
| **Build Automation** | Apache Maven | **3.9.x** |
| **Unit Testing Engine**| JUnit Jupiter API & Engine | **5.10.2** |
| **Development IDE** | IntelliJ IDEA / Eclipse / VS Code | IntelliJ IDEA 2024 / 2026 |

---

### 3.5 Use Case Modeling & Actor Interaction Descriptions

```mermaid
graph LR
    User((Store Operator / Admin))
    
    subgraph InvenTrack Desktop Application
        UC1[Authenticate / Login]
        UC2[Sign Up New Account]
        UC3[View KPI Dashboard & Alerts]
        UC4[Browse Product Catalog]
        UC5[Search & Filter Products]
        UC6[Add / Edit Product Details]
        UC7[Delete Product Record]
        UC8[Perform Inward / Outward Stock Adjustment]
        UC9[Logout Session]
    end
    
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9
```
<center><b>Figure 3.1:</b> UML Use Case diagram for InvenTrack desktop application</center>

<br>

<center><b>Table 3.5:</b> Use case tabular descriptions and operational flows</center>

| USE CASE ID & NAME | PRIMARY ACTOR | TRIGGER / PRE-CONDITION | MAIN SUCCESS SCENARIO |
| :--- | :--- | :--- | :--- |
| **UC-01: Authenticate** | Store Operator | Application launched; user enters credentials. | Credentials verified via PBKDF2; session established; dashboard displayed. |
| **UC-02: Sign Up** | New Staff | User selects "Sign Up" link on login window. | User inputs full name, unique username, password; new user row created. |
| **UC-03: View Dashboard**| Authenticated User| Successful login or selecting "Dashboard" tab. | Aggregate metrics queried from MySQL; KPI cards and low-stock table rendered. |
| **UC-04: Manage Catalog**| Authenticated User| Operator navigates to "Product Catalog". | Full product table retrieved sorted by `product_id ASC`; columns formatted. |
| **UC-05: Search / Filter**| Authenticated User| Typing keyword or selecting category dropdown.| Dynamic parameterized SQL executed; table refreshes with matching records. |
| **UC-06: Adjust Stock** | Authenticated User| Selecting product and clicking "Adjust Stock". | Modal calculates quantity preview; service validates $\Delta \ge 0$; stock persisted.|

---

<div style="page-break-after: always;"></div>

# CHAPTER 4: SYSTEM ARCHITECTURE & DESIGN

### 4.1 Architectural Design & Layered Pattern
InvenTrack employs a strict **4-tier layered architectural design pattern** ensuring clear separation of responsibilities across the application codebase.

```mermaid
graph TD
    subgraph Tier 1: Presentation Layer
        LF[LoginFrame]
        SUD[SignUpDialog]
        MDF[MainDashboardFrame]
        DP[DashboardPanel]
        PP[ProductPanel]
        PD[ProductDialog]
        SD[StockDialog]
        CC[CustomComponents & UITheme]
    end

    subgraph Tier 2: Service Layer
        AS[AuthService]
        PS[ProductService]
        PH[PasswordHasher]
        EX[ValidationException / AuthenticationException]
    end

    subgraph Tier 3: Data Access Object DAO Layer
        UDAO[UserDAO]
        PDAO[ProductDAO]
        DBC[DatabaseConfig]
        DBI[DatabaseInitializer]
    end

    subgraph Tier 4: Database Layer
        MDB[(MySQL 8.0 Database: inventory_db)]
        T1[products table]
        T2[users table]
    end

    Tier 1 -->|Calls business operations| Tier 2
    Tier 2 -->|Requests data persistence| Tier 3
    Tier 3 -->|Executes parameterized JDBC SQL| Tier 4
```
<center><b>Figure 4.1:</b> Layered component architecture diagram (Presentation, Service, DAO, DB)</center>

<br>

<center><b>Table 4.1:</b> Architectural software layers and package encapsulation</center>

| ARCHITECTURAL TIER | JAVA PACKAGE | PRIMARY RESPONSIBILITIES |
| :--- | :--- | :--- |
| **Presentation Tier** | `com.inventory.ui`<br>`com.inventory.ui.theme` | Rendering Swing GUI components, FlatLaf theme tokens, user event handling, background thread scheduling (`SwingWorker`). |
| **Service Tier** | `com.inventory.service` | Enforcing business logic invariants, validating user inputs, cryptographic password hashing, coordinating session state. |
| **Data Access Tier** | `com.inventory.dao`<br>`com.inventory.config` | Encapsulating SQL queries, establishing JDBC connections, mapping `ResultSet` rows to domain POJOs, resource cleanup. |
| **Domain Model Tier** | `com.inventory.model` | Pure entity representations (`Product`, `User`, `InventoryStats`) encapsulating state and business calculation methods. |

---

### 4.2 Object-Oriented Class Design & Component Hierarchy

```mermaid
classDiagram
    class Product {
        -Integer productId
        -String productName
        -String category
        -int quantity
        -BigDecimal price
        +getTotalValue() BigDecimal
        +isOutOfStock() boolean
        +isLowStock(int threshold) boolean
    }

    class User {
        -int userId
        -String username
        -String passwordHash
        -String salt
        -String fullName
        -String role
        -Timestamp createdAt
    }

    class InventoryStats {
        -int totalProducts
        -int totalQuantity
        -int lowStockCount
        -BigDecimal totalValue
        +getTotalProducts() int
        +getTotalQuantity() int
        +getLowStockCount() int
        +getTotalValue() BigDecimal
    }

    class ProductDAO {
        +getAllProducts() List~Product~
        +getProductById(int id) Optional~Product~
        +addProduct(Product p) int
        +updateProduct(Product p) boolean
        +updateQuantity(int id, int qty) boolean
        +deleteProduct(int id) boolean
        +searchProducts(String kw, String cat) List~Product~
        +getLowStockProducts(int threshold) List~Product~
        +getInventoryStats(int threshold) InventoryStats
        +getAllCategories() List~String~
    }

    class UserDAO {
        +findByUsername(String username) Optional~User~
        +createUser(User user) int
        +countUsers() int
        +getAllUsers() List~User~
    }

    class ProductService {
        -ProductDAO productDAO
        +getAllProducts() List~Product~
        +addProduct(Product p) Product
        +updateProduct(Product p) void
        +deleteProduct(int id) void
        +adjustStock(int id, int delta) int
        +validateProduct(Product p) void
    }

    class AuthService {
        -UserDAO userDAO
        -User currentUser$
        +authenticate(String user, char[] pass) User
        +registerUser(String user, char[] pass, String name, String role) User
        +logout() void
        +getCurrentUser()$ User
    }

    class PasswordHasher {
        +generateSalt()$ String
        +hashPassword(char[] pass, String salt)$ String
        +verifyPassword(char[] pass, String salt, String hash)$ boolean
    }

    ProductService --> ProductDAO : uses
    ProductService ..> Product : validates & manages
    AuthService --> UserDAO : uses
    AuthService ..> PasswordHasher : uses
    AuthService ..> User : manages session
    ProductDAO ..> Product : maps
    ProductDAO ..> InventoryStats : derives
    UserDAO ..> User : maps
```
<center><b>Figure 4.2:</b> Comprehensive UML Class Diagram illustrating object relationships</center>

---

### 4.3 Relational Database Design & Schema Specifications

#### 4.3.1 Entity Relationship (ER) Modeling

```mermaid
erDiagram
    USERS {
        int user_id PK "AUTO_INCREMENT"
        varchar username UK "NOT NULL, UNIQUE (50)"
        varchar password_hash "NOT NULL (255)"
        varchar salt "NOT NULL (64)"
        varchar full_name "NOT NULL (100)"
        varchar role "NOT NULL DEFAULT 'ADMIN' (20)"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
    }

    PRODUCTS {
        int product_id PK "AUTO_INCREMENT"
        varchar product_name "NOT NULL (100)"
        varchar category "DEFAULT 'General' (50)"
        int quantity "NOT NULL DEFAULT 0"
        decimal price "NOT NULL (10,2)"
    }
```
<center><b>Figure 4.3:</b> Entity Relationship (ER) diagram for MySQL `inventory_db` database</center>

#### 4.3.2 Data Dictionary & Table Definitions

<center><b>Table 4.3:</b> Data dictionary for `products` table</center>

| COLUMN NAME | DATA TYPE | CONSTRAINTS | DEFAULT VALUE | FUNCTIONAL DESCRIPTION |
| :--- | :--- | :--- | :--- | :--- |
| `product_id` | `INT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Auto Generated | Unique primary identifier for catalog item. |
| `product_name` | `VARCHAR(100)` | `NOT NULL` | None | Alphanumeric trade description of the product. |
| `category` | `VARCHAR(50)` | Nullable | `'General'` | Logical taxonomy group for catalog filtering. |
| `quantity` | `INT` | `NOT NULL` | `0` | Physical unit count on warehouse shelves ($\ge 0$). |
| `price` | `DECIMAL(10,2)`| `NOT NULL` | None | Unit selling price formatted to 2 decimal places. |

<br>

<center><b>Table 4.4:</b> Data dictionary for `users` table</center>

| COLUMN NAME | DATA TYPE | CONSTRAINTS | DEFAULT VALUE | FUNCTIONAL DESCRIPTION |
| :--- | :--- | :--- | :--- | :--- |
| `user_id` | `INT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Auto Generated | Unique internal surrogate key for user record. |
| `username` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | None | Unique login handle for user authentication. |
| `password_hash` | `VARCHAR(255)`| `NOT NULL` | None | Hex-encoded PBKDF2 cryptographic password digest. |
| `salt` | `VARCHAR(64)` | `NOT NULL` | None | 32-character hexadecimal random per-user salt. |
| `full_name` | `VARCHAR(100)`| `NOT NULL` | None | Full legal or display name of the operator. |
| `role` | `VARCHAR(20)` | `NOT NULL` | `'ADMIN'` | Authorization privilege level (`ADMIN`, `STAFF`). |
| `created_at` | `TIMESTAMP` | `NOT NULL` | `CURRENT_TIMESTAMP` | System creation timestamp for account auditing. |

---

### 4.4 Dynamic Behavioral Modeling

#### 4.4.1 Sequence Diagram: Cryptographic User Authentication & Sign-Up

```mermaid
sequenceDiagram
    autonumber
    actor Operator as Store Operator
    participant UI as LoginFrame / SignUpDialog
    participant Auth as AuthService
    participant Hash as PasswordHasher
    participant DAO as UserDAO
    participant DB as MySQL (users table)

    Operator->>UI: Enter Username & Password
    Operator->>UI: Click "Sign In to Workspace"
    UI->>Auth: authenticate(username, passwordChars)
    Auth->>DAO: findByUsername(username)
    DAO->>DB: SELECT * FROM users WHERE LOWER(username) = LOWER(?)
    DB-->>DAO: User Record (Hash, Salt)
    DAO-->>Auth: Optional<User>
    Auth->>Hash: verifyPassword(passwordChars, salt, storedHash)
    Note over Hash: Re-derive PBKDF2 hash & compare via MessageDigest.isEqual
    Hash-->>Auth: boolean (true/false)
    Note over Auth: Zero out passwordChars array in memory
    Auth-->>UI: Authenticated User Object
    UI->>Operator: Transition to MainDashboardFrame
```
<center><b>Figure 4.4:</b> UML Sequence diagram: Cryptographic authentication & registration</center>

---

#### 4.4.2 Sequence Diagram: Multi-Criteria Product Search & Category Filtering

```mermaid
sequenceDiagram
    autonumber
    actor Operator as Store Operator
    participant UI as ProductPanel (EDT)
    participant Worker as SwingWorker Thread
    participant Service as ProductService
    participant DAO as ProductDAO
    participant DB as MySQL (products table)

    Operator->>UI: Input Keyword ("Lap") & Category ("Electronics")
    UI->>Worker: execute() in Background
    Worker->>Service: searchProducts("Lap", "Electronics")
    Service->>DAO: searchProducts("Lap", "Electronics")
    Note over DAO: Dynamically assemble PreparedStatement WHERE clauses
    DAO->>DB: SELECT * FROM products WHERE (LOWER(product_name) LIKE ? ...) AND category = ? ORDER BY product_id ASC
    DB-->>DAO: ResultSet Rows
    DAO-->>Service: List<Product>
    Service-->>Worker: List<Product>
    Worker-->>UI: done() callback on EDT
    UI->>Operator: Re-render JTable with matching items in ascending ID order
```
<center><b>Figure 4.5:</b> UML Sequence diagram: Multi-criteria product search & filtering</center>

---

#### 4.4.3 Sequence Diagram: Inward/Outward Stock Adjustment

```mermaid
sequenceDiagram
    autonumber
    actor Operator as Store Operator
    participant Modal as StockDialog
    participant Service as ProductService
    participant DAO as ProductDAO
    participant DB as MySQL (products table)

    Operator->>Modal: Select "Deduct Stock (-)" & Quantity (5 units)
    Modal->>Modal: Calculate live preview (Current - 5)
    Operator->>Modal: Click "Apply Stock Adjustment"
    Modal->>Service: adjustStock(productId, -5)
    Service->>DAO: getProductById(productId)
    DAO->>DB: SELECT quantity FROM products WHERE product_id = ?
    DB-->>DAO: Current Quantity = 3 units
    DAO-->>Service: Product Object
    Note over Service: Invariant Check: Current (3) + Delta (-5) = -2 (< 0)
    Service-->>Modal: throws ValidationException("Insufficient stock! Cannot reduce below zero.")
    Modal->>Operator: Display Red Invariant Error Feedback
```
<center><b>Figure 4.6:</b> UML Sequence diagram: Stock inward/outward adjustment with invariant check</center>

---

<div style="page-break-after: always;"></div>

# CHAPTER 5: SYSTEM IMPLEMENTATION

### 5.1 Project Structure & Maven Build Lifecycle
The project strictly respects the Maven standard directory layout:

```
D:\Inventory Management\
├── pom.xml                               # Central Maven Project Object Model
├── config.properties                     # Local DB connection credentials
├── config.example.properties             # Environment configuration template
├── database/
│   └── setup_users.sql                   # DDL bootstrap script for MySQL
└── src/
    ├── main/java/com/inventory/
    │   ├── Main.java                     # Application bootstrap entry point
    │   ├── config/                       # Database connectivity & initialization
    │   │   ├── DatabaseConfig.java
    │   │   └── DatabaseInitializer.java
    │   ├── dao/                          # JDBC Data Access Layer
    │   │   ├── ProductDAO.java
    │   │   └── UserDAO.java
    │   ├── model/                        # Domain entities
    │   │   ├── Product.java
    │   │   └── User.java
    │   │   └── InventoryStats.java
    │   ├── service/                      # Business logic & cryptography
    │   │   ├── AuthService.java
    │   │   ├── ProductService.java
    │   │   ├── PasswordHasher.java
    │   │   ├── AuthenticationException.java
    │   │   └── ValidationException.java
    │   └── ui/                           # FlatLaf Swing UI
    │       ├── LoginFrame.java
    │       ├── SignUpDialog.java
    │       ├── MainDashboardFrame.java
    │       ├── DashboardPanel.java
    │       ├── ProductPanel.java
    │       ├── ProductDialog.java
    │       ├── StockDialog.java
    │       └── theme/
    │           ├── UITheme.java
    │           └── CustomComponents.java
    └── test/java/com/inventory/          # JUnit 5 test suites
        ├── PasswordHasherTest.java
        └── ProductValidationTest.java
```

<center><b>Table 5.1:</b> Maven project configuration and dependency coordinates</center>

| GROUP ID | ARTIFACT ID | VERSION | SCOPE | PURPOSE |
| :--- | :--- | :---: | :---: | :--- |
| `com.mysql` | `mysql-connector-j` | `8.3.0` | `compile` | Official JDBC Type-4 driver for MySQL. |
| `com.formdev` | `flatlaf` | `3.5.4` | `compile` | Modern clean desktop Look and Feel framework. |
| `org.junit.jupiter`| `junit-jupiter-api` | `5.10.2` | `test` | Annotations and assertions for unit testing. |
| `org.junit.jupiter`| `junit-jupiter-engine`| `5.10.2` | `test` | Test execution runtime engine for JUnit 5. |

---

### 5.2 Cryptographic Security Module: `PasswordHasher` & `AuthService`
Password security is implemented in [`PasswordHasher.java`](file:///D:/Inventory%20Management/src/main/java/com/inventory/service/PasswordHasher.java):

```java
public class PasswordHasher {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH = 256; // 256 bits
    private static final int SALT_LENGTH = 16;  // 16 bytes (128 bits)
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return HexFormat.of().formatHex(salt);
    }

    public static String hashPassword(char[] password, String saltHex) {
        byte[] salt = HexFormat.of().parseHex(saltHex);
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Cryptographic failure during password hashing", e);
        } finally {
            spec.clearPassword(); // Proactive memory cleanup
        }
    }

    public static boolean verifyPassword(char[] password, String saltHex, String expectedHashHex) {
        String computedHash = hashPassword(password, saltHex);
        return MessageDigest.isEqual(
            computedHash.getBytes(StandardCharsets.UTF_8),
            expectedHashHex.getBytes(StandardCharsets.UTF_8)
        );
    }
}
```

---

### 5.3 Domain Model & Service Validation Invariants
The [`ProductService.java`](file:///D:/Inventory%20Management/src/main/java/com/inventory/service/ProductService.java) enforces strict validation invariants before any DML operation reaches `ProductDAO`:

```java
public void validateProduct(Product product) throws ValidationException {
    if (product == null) {
        throw new ValidationException("Product data cannot be null.");
    }
    if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
        throw new ValidationException("Product name is required.");
    }
    if (product.getProductName().trim().length() > 100) {
        throw new ValidationException("Product name cannot exceed 100 characters.");
    }
    if (product.getQuantity() < 0) {
        throw new ValidationException("Quantity cannot be negative.");
    }
    if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
        throw new ValidationException("Unit price must be a valid non-negative number.");
    }
    if (product.getPrice().compareTo(new BigDecimal("99999999.99")) > 0) {
        throw new ValidationException("Unit price exceeds maximum allowable limit (₹9,99,99,999.99).");
    }
}
```

---

### 5.4 High-Performance Data Access Objects (`ProductDAO`)
`ProductDAO` manages all database interactions via prepared statements. The multi-criteria search implementation handles dynamic filtering safely:

```java
public List<Product> searchProducts(String keyword, String category) throws SQLException {
    StringBuilder sql = new StringBuilder(
        "SELECT product_id, product_name, category, quantity, price FROM products WHERE 1=1"
    );
    List<Object> params = new ArrayList<>();

    if (keyword != null && !keyword.trim().isEmpty()) {
        String term = keyword.trim();
        boolean isNumeric = term.chars().allMatch(Character::isDigit);
        if (isNumeric) {
            sql.append(" AND (product_id = ? OR LOWER(product_name) LIKE ?)");
            params.add(Integer.parseInt(term));
            params.add("%" + term.toLowerCase() + "%");
        } else {
            sql.append(" AND LOWER(product_name) LIKE ?");
            params.add("%" + term.toLowerCase() + "%");
        }
    }

    if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All Categories")) {
        sql.append(" AND category = ?");
        params.add(category.trim());
    }

    sql.append(" ORDER BY product_id ASC"); // Ascending ID sort order

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }
        try (ResultSet rs = stmt.executeQuery()) {
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
            return products;
        }
    }
}
```

---

### 5.5 Indian Financial Representation Engine
To format financial metrics in the Indian Lakhs/Crores convention rather than the International Millions system, [`CustomComponents.java`](file:///D:/Inventory%20Management/src/main/java/com/inventory/ui/theme/CustomComponents.java) implements `formatIndianCurrency()`:

```java
public static String formatIndianCurrency(BigDecimal amount) {
    if (amount == null) return "₹0.00";
    BigDecimal rounded = amount.setScale(2, RoundingMode.HALF_UP);
    String strVal = rounded.toPlainString();
    String[] parts = strVal.split("\\.");
    String integerPart = parts[0];
    String decimalPart = parts.length > 1 ? parts[1] : "00";

    boolean negative = integerPart.startsWith("-");
    if (negative) integerPart = integerPart.substring(1);

    int len = integerPart.length();
    if (len <= 3) {
        return (negative ? "-₹" : "₹") + integerPart + "." + decimalPart;
    }

    // Indian Grouping: Last 3 digits, then groups of 2 digits
    String lastThree = integerPart.substring(len - 3);
    String remaining = integerPart.substring(0, len - 3);

    StringBuilder sb = new StringBuilder();
    while (remaining.length() > 2) {
        sb.insert(0, "," + remaining.substring(remaining.length() - 2));
        remaining = remaining.substring(0, remaining.length() - 2);
    }
    sb.insert(0, remaining);
    sb.append(",").append(lastThree).append(".").append(decimalPart);

    return (negative ? "-₹" : "₹") + sb.toString();
}
```

*Example Output*: A numeric value of `207000.00` correctly formats as **₹2,07,000.00** instead of `$207,000.00` or `₹207,000.00`.

---

<div style="page-break-after: always;"></div>

# CHAPTER 6: SYSTEM TESTING & QUALITY ASSURANCE

### 6.1 Testing Methodology & Verification Strategy
Quality assurance was conducted across two tiers:
1. **Automated Unit Testing**: Executed via **JUnit 5**, testing isolated algorithmic and domain-rule assertions without database dependency.
2. **Interactive UI Verification**: Executed manually on the Event Dispatch Thread (EDT) verifying component state transitions, dialog modalities, search filtering, and error label rendering.

---

### 6.2 Unit Testing Specifications & Assertions (JUnit 5)

#### 6.2.1 Cryptographic Test Suite: `PasswordHasherTest`

<center><b>Table 6.1:</b> Cryptographic unit test specifications</center>

| TEST METHOD | TEST CONDITION & INPUT | EXPECTED ASSERTION | RESULT |
| :--- | :--- | :--- | :---: |
| `testSaltGeneration()` | Generate two successive salts via `generateSalt()`. | Non-null, length = 32 hex chars, values must differ. | **PASSED** |
| `testHashDeterministic()` | Hash password `"admin123"` twice with identical salt. | Hash digests must match identically. | **PASSED** |
| `testDifferentSaltDifferentHash()`| Hash `"admin123"` with two distinct salts. | Hash digests must not match. | **PASSED** |
| `testVerifyPassword()` | Verify valid credentials vs. wrong password / nulls. | Returns `true` on match; `false` on incorrect input. | **PASSED** |

#### 6.2.2 Business Logic Test Suite: `ProductValidationTest`

<center><b>Table 6.2:</b> Domain validation unit test specifications</center>

| TEST METHOD | TEST CONDITION & INPUT | EXPECTED ASSERTION | RESULT |
| :--- | :--- | :--- | :---: |
| `testTotalValueCalculation()` | Price ₹89.99, Quantity 10 units. | `getTotalValue()` equals exactly `₹899.90`. | **PASSED** |
| `testStockStatusClassification()`| Quantity 0, Quantity 3 (threshold 5). | `isOutOfStock()` true for 0; `isLowStock()` true for 3. | **PASSED** |
| `testBlankProductNameFails()` | `productName = "   "` (whitespace). | Throws `ValidationException`. | **PASSED** |
| `testNegativeQuantityFails()` | `quantity = -5`. | Throws `ValidationException`. | **PASSED** |
| `testNegativePriceFails()` | `price = new BigDecimal("-10.00")`. | Throws `ValidationException`. | **PASSED** |
| `testValidProductPasses()` | Valid standard product entity. | Successfully passes `validateProduct()`. | **PASSED** |

---

### 6.3 Test Execution Summary & Verification Metrics

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.inventory.PasswordHasherTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.273 s -- in com.inventory.PasswordHasherTest
[INFO] Running com.inventory.ProductValidationTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.028 s -- in com.inventory.ProductValidationTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

<center><b>Table 6.3:</b> Recorded automated test execution summary</center>

| TEST SUITE CLASS | TEST COUNT | FAILURES | ERRORS | SKIPPED | PASS RATE | EXECUTION TIME |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| `PasswordHasherTest` | 4 | 0 | 0 | 0 | 100% | 0.273 s |
| `ProductValidationTest` | 6 | 0 | 0 | 0 | 100% | 0.028 s |
| **TOTAL** | **10** | **0** | **0** | **0** | **100%** | **0.301 s** |

---

<div style="page-break-after: always;"></div>

# CHAPTER 7: SYSTEM OUTPUTS & SCREENSHOT DOCUMENTATION

> *Note for Submission*: Replace the illustrated interface layout diagrams below with high-resolution PNG screenshots captured from the running InvenTrack application on your workstation.

### 7.1 UI Showcase 1: Split-Screen Widescreen Login Interface

```
+--------------------------------------------------------------------------------------------------------+
|  ❖ InvenTrack — Inventory Management System                                                   _  □  ×  |
+-------------------------------------------------------------------+------------------------------------+
|                                                                   |                                    |
|                      [   ❖   ]                                    |        Sign In to Workspace        |
|                                                                   |  Enter your credentials to access  |
|                     InvenTrack                                    |                                    |
|                                                                   |  Username                          |
|         “Track Smarter. Manage Better.”                           |  [ Admin                         ] |
|                                                                   |                                    |
|            Inventory Management System                            |  Password                          |
|                                                                   |  [ ••••••••••                    ] |
|                                                                   |                                    |
|                                                                   |  [  Sign In to Workspace    ➔   ]  |
|                                                                   |                                    |
|                                                                   |   Don't have an account? Sign Up   |
|                                                                   |                                    |
|                                                                   |  ● Connected to MySQL (inventory_db|
+-------------------------------------------------------------------+------------------------------------+
```
<center><b>Figure 7.1:</b> Output Showcase: Modern Split-Screen Login Window</center>

---

### 7.2 UI Showcase 2: User Sign-Up & Registration Modal

```
+---------------------------------------------------------------+
|  Create New Account — InvenTrack                     _  □  ×  |
+---------------------------------------------------------------+
|  Create Account                                               |
|  Register a new user to access the inventory system           |
|                                                               |
|  Full Name *                                                  |
|  [ Mohamed Javith S                                         ] |
|                                                               |
|  Username *                                                   |
|  [ javith_admin                                             ] |
|                                                               |
|  Password *                                                   |
|  [ ••••••••••••••                                           ] |
|                                                               |
|  Confirm Password *                                           |
|  [ ••••••••••••••                                           ] |
|                                                               |
|  Role *                                                       |
|  [ ADMIN                                                  ▼ ] |
|                                                               |
|                             [ Cancel ]  [ Create Account ✓ ]  |
+---------------------------------------------------------------+
```
<center><b>Figure 7.2:</b> Output Showcase: User Sign-Up Modal Dialog</center>

---

### 7.3 UI Showcase 3: Real-Time Executive KPI Dashboard

```
+--------------------------------------------------------------------------------------------------------+
|  ❖ InvenTrack                                                                                 _  □  ×  |
+-------------------+------------------------------------------------------------------------------------+
|  [❖] InvenTrack   |  Dashboard Overview                                                                |
|  Track Smarter    |  Real-time inventory metrics and stock health alerts                               |
|                   +------------------------------------------------------------------------------------+
|  [=] Dashboard    |  [ TOTAL PRODUCTS ]   [ TOTAL UNITS ]   [ LOW STOCK ITEMS ]   [ TOTAL VALUATION ]  |
|  [#] Products     |  |       18       |   |    1,420    |   |        3        |   |  ₹24,85,600.00  |  |
|                   +------------------------------------------------------------------------------------+
|  [→] Sign Out     |  Critical Stock Alerts (Sorted by ID ASC)                                          |
|                   |  +-----+-------------------------+---------------+----------+------------+-------+ |
|                   |  | ID  | Product Name            | Category      | Quantity | Unit Price | Status| |
|                   |  +-----+-------------------------+---------------+----------+------------+-------+ |
|                   |  | 2   | Ergonomic Keyboard      | Electronics   | 2        | ₹4,500.00  | LOW   | |
|                   |  | 7   | USB-C Fast Charger      | Accessories   | 0        | ₹1,299.00  | OUT   | |
|                   |  | 14  | Wireless Mouse Pro      | Electronics   | 4        | ₹2,100.00  | LOW   | |
|                   |  +-----+-------------------------+---------------+----------+------------+-------+ |
+-------------------+------------------------------------------------------------------------------------+
```
<center><b>Figure 7.3:</b> Output Showcase: Executive KPI Dashboard & Stock Alert Monitor</center>

---

### 7.4 UI Showcase 4: Searchable Product Catalog Grid

```
+--------------------------------------------------------------------------------------------------------+
|  ❖ InvenTrack                                                                                 _  □  ×  |
+-------------------+------------------------------------------------------------------------------------+
|  [❖] InvenTrack   |  Product Catalog                                                                   |
|  Track Smarter    |  Manage, search, edit, and adjust inventory products                               |
|                   +------------------------------------------------------------------------------------+
|  [=] Dashboard    |  Search: [ Laptop                   ]  Category: [ All Categories ▼ ]  [ Search ]  |
|  [#] Products     |  +-----+-------------------------+---------------+----------+------------+-------+ |
|                   |  | ID  | Product Name            | Category      | Quantity | Unit Price | Status| |
|  [→] Sign Out     |  +-----+-------------------------+---------------+----------+------------+-------+ |
|                   |  | 1   | Business Laptop 15"     | Computers     | 12       | ₹65,000.00 | OK    | |
|                   |  | 5   | Gaming Laptop 16GB      | Computers     | 7        | ₹89,990.00 | OK    | |
|                   |  | 11  | Ultra-Slim Laptop       | Computers     | 3        | ₹72,500.00 | LOW   | |
|                   |  +-----+-------------------------+---------------+----------+------------+-------+ |
|                   |  [ + Add Product ]  [ Edit Product ]  [ Adjust Stock ]  [ Delete Product ]         |
+-------------------+------------------------------------------------------------------------------------+
```
<center><b>Figure 7.4:</b> Output Showcase: Searchable Product Catalog with Indian Rupee formatting</center>

---

<div style="page-break-after: always;"></div>

# CHAPTER 8: RESULTS, EVALUATION & DISCUSSION

### 8.1 Functional Achievement Against Target Objectives

<center><b>Table 8.1:</b> Traceability matrix: objectives achieved vs. source code validation</center>

| PROJECT OBJECTIVE | CODE IMPLEMENTATION LOCATION | VERIFICATION STATUS |
| :--- | :--- | :---: |
| **Pure Java 21 Desktop GUI** | `Main.java`, `UITheme.java`, `FlatLaf 3.5.4` | **100% Achieved** |
| **Decoupled Architecture** | `com.inventory.ui`, `service`, `dao`, `model` | **100% Achieved** |
| **Parameterized Persistence** | `ProductDAO.java`, `UserDAO.java` | **100% Achieved** |
| **PBKDF2 Cryptographic Auth**| `PasswordHasher.java`, `AuthService.java` | **100% Achieved** |
| **Integrated Sign-Up** | `SignUpDialog.java`, `LoginFrame.java` | **100% Achieved** |
| **Ascending ID Ordering** | `ORDER BY product_id ASC` in all queries | **100% Achieved** |
| **Indian Rupee Valuation** | `CustomComponents.formatIndianCurrency()` | **100% Achieved** |
| **Automated Unit Testing** | `PasswordHasherTest`, `ProductValidationTest` | **100% Achieved (10/10)** |

---

### 8.2 Security & Cryptographic Robustness Evaluation
The security architecture of InvenTrack was evaluated against common vulnerability patterns:
1. **SQL Injection Vulnerability**: Zero vulnerability. All user inputs (names, IDs, search strings, prices) are bound exclusively via JDBC `PreparedStatement` parameters (`?`). Dynamic queries append conditions structurally while preserving parameter placeholders.
2. **Credential Theft Resistance**: Passwords are never stored in plaintext. By utilizing `PBKDF2WithHmacSHA256` with 65,536 iterations and unique 16-byte random salts, pre-computed rainbow tables and dictionary attacks are rendered computationally unfeasible.
3. **In-Memory Password Hygiene**: Passwords are captured using `JPasswordField.getPassword()` returning `char[]`. Arrays are explicitly wiped using `Arrays.fill(password, '\0')` inside `finally` blocks immediately after hashing, preventing heap dumps from exposing credentials.

---

### 8.3 Data Consistency, Indian Financial Representation & Performance
* **Financial Accuracy**: Calculations utilize `BigDecimal` with `RoundingMode.HALF_UP` rounding, eliminating floating-point rounding errors (e.g., $0.1 + 0.2 = 0.30000000000000004$).
* **Indian Financial Formatting**: The custom currency formatting engine correctly formats values according to the Indian numbering system (`₹2,07,000.00` rather than `$207,000.00` or `₹207,000.00`).
* **Query Latency**: On a local MySQL 8.0 instance running a 1,000-item catalog, search queries, aggregate statistics, and updates consistently execute in under 12 milliseconds.

---

### 8.4 Known Constraints & Risk Considerations
1. **Desktop Deployment Dependency**: The application requires an installed Java 21+ Runtime Environment and a reachable MySQL server instance.
2. **Single Low-Stock Threshold**: Currently, the low-stock safety threshold is globally configured (`inventory.low_stock_threshold = 5`). In future versions, this should be configurable on a per-category or per-product basis.
3. **Absence of Audit Log Table**: The current schema updates quantities directly in the `products` table without maintaining a historical transaction ledger of stock movements.

---

<div style="page-break-after: always;"></div>

# CHAPTER 9: CONCLUSION & FUTURE ENHANCEMENTS

### 9.1 Summary of Research & Engineering Contributions
The **InvenTrack — Inventory Management System** successfully demonstrates that a rich, responsive, and secure desktop enterprise application can be engineered using **Core Java 21**, **Java Swing**, and **JDBC** without the overhead of heavy web frameworks.

By strictly separating presentation, business rules, data access, and domain entities, the project achieves modularity, maintainability, and testability. Security is upheld through `PBKDF2WithHmacSHA256` salted password hashing and parameterized SQL queries, while the user interface provides an ergonomic experience styled with the **FlatLaf** design system and localized to Indian financial standards.

---

### 9.2 Roadmap for Future Enhancements
The architecture has been designed to support several planned extensions:
1. **Historical Stock Ledger Table (`stock_movements`)**: Recording timestamps, operators, transaction types (Purchase, Sale, Return, Damage), and delta values for complete audit trails.
2. **Automated Barcode & QR Code Reader Integration**: Enabling instant product scanning via USB or serial barcode scanners.
3. **Reporting & PDF Export Engine**: Exporting inventory audits, stock-out alerts, and valuation summaries into styled PDF and Excel formats using Apache POI or iText.
4. **Automated Supplier Purchase Order Reordering**: Triggering automated purchase-order email notifications to suppliers when stock levels drop below safety thresholds.

---

<div style="page-break-after: always;"></div>

# APPENDIX A: SELECTED SOURCE CODE EXCERPTS

### A.1 Database Configuration Manager (`DatabaseConfig.java`)
```java
package com.inventory.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Warning: config.properties could not be loaded, using defaults: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            props.getProperty("db.url", "jdbc:mysql://localhost:3306/inventory_db"),
            props.getProperty("db.username", "root"),
            props.getProperty("db.password", "")
        );
    }

    public static int getLowStockThreshold() {
        return Integer.parseInt(props.getProperty("inventory.low_stock_threshold", "5"));
    }
}
```

---

<div style="page-break-after: always;"></div>

# APPENDIX B: DATABASE DDL & SYSTEM CONFIGURATION

### B.1 User Authentication Table Bootstrap Script (`setup_users.sql`)
```sql
-- InvenTrack: Users Table DDL Specification
USE inventory_db;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Note: Initial administrator account is bootstrapped programmatically
-- by DatabaseInitializer.java with salted PBKDF2 hash on first run.
```

### B.2 Application Properties Configuration (`config.properties`)
```properties
# MySQL Relational Database Configuration
db.url=jdbc:mysql://localhost:3306/inventory_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE

# Operational Business Rules
inventory.low_stock_threshold=5
```

---

<div style="page-break-after: always;"></div>

# REFERENCES

1. Oracle Corporation, *Java Platform, Standard Edition Documentation (JDK 21 LTS)*, Oracle Technology Network, 2023. [Online]. Available: https://docs.oracle.com/en/java/javase/21/
2. Oracle Corporation, *Java Database Connectivity (JDBC) Developer Guide*, Java SE 21 Specification, 2023. [Online]. Available: https://docs.oracle.com/en/java/javase/21/docs/api/java.sql/
3. FormDev Software GmbH, *FlatLaf — Clean and Modern Look and Feel for Java Swing*, 2024. [Online]. Available: https://www.formdev.com/flatlaf/
4. MySQL AB / Oracle Corporation, *MySQL 8.0 Reference Manual*, Oracle Database Documentation, 2024. [Online]. Available: https://dev.mysql.com/doc/refman/8.0/en/
5. B. Kaliski, *PKCS #5: Password-Based Cryptography Specification Version 2.0 (RFC 2898)*, RSA Laboratories, 2000. [Online]. Available: https://datatracker.ietf.org/doc/html/rfc2898
6. National Institute of Standards and Technology (NIST), *Recommendation for Password-Based Key Derivation: Part 1: Storage Applications*, NIST Special Publication 800-132, 2010.
7. J. Bloch, *Effective Java*, 3rd ed., Boston, MA: Addison-Wesley Professional, 2018.
8. E. Gamma, R. Helm, R. Johnson, and J. Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*, Reading, MA: Addison-Wesley, 1994.
9. JUnit 5 Team, *JUnit 5 User Guide: Testing Framework for Java*, 2024. [Online]. Available: https://junit.org/junit5/docs/current/user-guide/
10. Apache Software Foundation, *Apache Maven Project Documentation*, 2024. [Online]. Available: https://maven.apache.org/
