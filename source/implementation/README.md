# ▶️ How to Build & Run the African Royals Marketplace

This guide explains how to **setup, build, run, and test** the application.

---

## 📦 Project Requirements
- Java 23+
- Maven Installed
- SQLite Installed (comes bundled, no extra setup required)

---

## 📁 Directory to Run From
Navigate to:

---

## 🛠️ Build the Application
Run this command:

This will:
- Resolve dependencies
- Build the project
- Prepare the application for execution

---

## ▶️ Run the Application
After building, run:

---

## 🌐 Access the Application
Once running, open your web browser:

---

## 🔑 Application Usage

### 1) Register / Login
- Users and Admins share one login screen
- New users register under "Create Account"

---

### 2) View Inventory
- Shows all available inventory
- Search filters inventory as you type

---

### 3) Shopping Cart
- Add multiple items
- Remove items (removed items return to inventory)

---

### 4) Checkout
- Requires at least 1 item in cart
- Displays a receipt on screen
- Purchase persists after restarting the application

---

### 5) Administrator
Admins see a link to:

Sales Report shows:
- Items sold
- Date sold
- Buyer

---

## 🔁 Restart Persistence Requirement
After restarting `mvn spring-boot:run`, purchased items **must remain purchased**.

This is handled through:
- SQLite Database
- Transactions stored in `Sales` table
