<a id="readme-top"></a>

# 💎 The African Royals Jewelry Store  

### 🧾 Requirements Analysis & Elicitation — Version 1  

After a detailed elicitation session with our customer, we identified the core features, goals, and constraints for **The African Royals Jewelry Store** application.  
This document summarizes the agreed-upon Version 1 scope and outlines the vision for future development.  

---

## 🧠 Project Overview  

The African Royals Jewelry Store is an online platform for buying and managing authentic African jewelry pieces.  
The system aims to provide customers with a simple, beautiful shopping experience and allow administrators to manage inventory and track sales effectively.  

---

## ⚙️ Requirements — Version 1  

### 🧍‍♀️ T8E-1: User Account Management  

#### 🪪 T8S-1: Register a New User  
- **Priority:** Must Have  
- **Effort:** 1 day  
- **Type:** Functional  
- The user must be able to self-register by creating an account with a unique username and a password that is at least 6 characters long. Admins cannot self-register.  

#### 🔐 T8S-2: Log in as a Registered User  
- **Priority:** Must Have  
- **Effort:** 0.5 day  
- **Type:** Functional  
- The user must be able to log in using their registered credentials. Login must validate credentials and direct the user to the main inventory screen upon success.  

---

### 💍 T8E-2: Jewelry Inventory & Browsing  

#### 🧭 T8S-3: View Jewelry Inventory  
- **Priority:** Must Have  
- **Effort:** 1 day  
- **Type:** Functional  
- After login, the user must see all available jewelry items sorted by price from highest to lowest. Sold items must not appear in this list.  

#### 🔎 T8S-4: Search by Jewelry Name  
- **Priority:** Must Have  
- **Effort:** 0.5 day  
- **Type:** Functional  
- The system must allow users to search jewelry items by **name only**. Description search will be included in a future version.  

---

### 🛒 T8E-3: Shopping Cart & Checkout  

#### ➕ T8S-5: Add Item to Cart  
- **Priority:** Must Have  
- **Effort:** 0.5 day  
- **Type:** Functional  
- The user must be able to add one or more jewelry items to their cart.  

#### 💳 T8S-6: Checkout (Mock Payment)  
- **Priority:** Must Have  
- **Effort:** 1 day  
- **Type:** Functional  
- The checkout must collect the user’s shipping and mock payment details. Payment processing is simulated (no real transaction).  

#### 🧮 T8S-7: Calculate Total with Tax and Shipping  
- **Priority:** Must Have  
- **Effort:** 0.5 day  
- **Type:** Functional  
- The system must apply a 6% tax to the subtotal and add a shipping fee based on the selected option:  
  - Overnight – $29  
  - 3-Day – $19  
  - Ground – Free  

#### 🧾 T8S-8: Generate and Display Receipt  
- **Priority:** Must Have  
- **Effort:** 1 day  
- **Type:** Functional  
- After payment confirmation, the system must show an on-screen receipt summarizing the items, subtotal, tax, shipping, and grand total. Purchased items must be automatically removed from inventory.  

---

### 👑 T8E-4: Admin Management & Reports  

#### 🔐 T8S-9: Admin Login  
- **Priority:** Must Have  
- **Effort:** 0.5 day  
- **Type:** Functional  
- Only existing admins can log in and transform a regular user into an admin.  

#### 🧰 T8S-10: Add Inventory (Manual Entry)  
- **Priority:** Must Have  
- **Effort:** 1 day  
- **Type:** Functional  
- For Version 1, admins will manually add jewelry items directly into the database.  

#### 📊 T8S-11: View Sales Report  
- **Priority:** Must Have  
- **Effort:** 1 day  
- **Type:** Functional  
- Admins can view a list of all completed transactions in their dashboard. Each record includes customer name, items purchased, and total price.  

#### 🧾 T8S-12: View Individual Receipts  
- **Priority:** Must Have  
- **Effort:** 0.5 day  
- **Type:** Functional  
- Admins can click on any sold item in the sales report to view the full related receipt.  

---

## 🧩 Version 2 – Future Enhancements  

Planned improvements for later versions include:  
- User interface for admins to add/edit/delete inventory.  
- Multi-image gallery per jewelry piece.  
- Search by both name and description.  
- CSV export for sales reports.  
- Real payment gateway integration.  
- Automatic email receipts.  
- High-fidelity UI polish and animations.  

---

## 🧮 Data Format Notes  

- **Currency Format:** all prices use U.S. dollars with `$`, commas, and decimals (e.g., `$1,200.00`).  
- **Storage:** prices must use **decimal/currency type (base-10)** — not floating point.  
- **Validation:** required fields include username, password, shipping details, and payment info.  

---

## 🎨 Mockups  

High-fidelity mockups were created and approved by the client before coding began.  
They illustrate the main flow:  
- Login/Register → Jewelry Catalog → Shopping Cart → Checkout → Confirm Order → Receipt.  
*(Link to mockups can be added here once uploaded.)*  

---

## 📘 Supporting Documents  

- [Use-Case Diagram](./use-case-diagram.md)  
- [Decision Table](./decision-table.md)  
- [Requirements Presentation](./presentation.md)  

---

## ✏️ Team Notes  

During elicitation, we clarified:  
| Question | Decision |
|-----------|-----------|
| How realistic should checkout be? | Mock form only |
| How will admins add items? | Directly in database |
| Number of pictures per item | One image (multi-image in future) |
| Search scope | By item name only |
| Include taxes and shipping? | Yes |
| Sales report visibility | Admin dashboard only |

---

## 💡 Summary  

Version 1 delivers a working prototype that meets all **core business requirements**.  
Future versions will improve usability, automation, and design.  
This incremental approach allows us to deliver a usable product early while keeping the path open for growth.  

---

<p align="right">(<a href="#readme-top">back to top</a>)</p>
