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

## ⚙️ Version 1 Scope  

Version 1 focuses on all **MUST HAVE** functionalities required to make the store operational.  
These include user management, jewelry browsing, cart handling, checkout, and basic admin control.  

### 👥 User Features  
- **Account Registration & Login** — users can create an account and log in securely.  
- **Unique Credentials** — usernames must be unique with a minimum 6-character password.  
- **View Inventory** — users can browse all available jewelry items, sorted by price (high → low).  
- **Search** — search by **item name** only (description search will be added later).  
- **Add to Cart** — users can add multiple jewelry items to their cart.  
- **Checkout** — mock checkout form to collect shipping and payment details (not a real transaction).  
- **Cost Calculations** — subtotal, 6 % tax, and shipping options:  
  - Overnight – $29  
  - 3-Day – $19  
  - Ground – Free  
- **Order Confirmation & Receipt** — displays a breakdown of total cost and shipping info.  
- **Automatic Inventory Update** — purchased items are removed from inventory.  

### 👑 Admin Features  
- **Admin Login** — only existing admins can transform a user into an admin.  
- **Add Inventory (Manual)** — for Version 1, admins will manually add items into the database.  
- **View Sales Report** — admins can see all completed transactions in the admin dashboard.  
- **View Receipts** — admins can click on sold items to view related receipts.  

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
