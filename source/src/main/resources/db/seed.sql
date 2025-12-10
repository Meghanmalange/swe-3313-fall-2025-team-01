-- Enable foreign key constraints
PRAGMA foreign_keys = ON;

-- ============================
-- USERS TABLE
-- ============================
CREATE TABLE IF NOT EXISTS users (
                                     Userid INTEGER PRIMARY KEY AUTOINCREMENT,
                                     Username TEXT UNIQUE NOT NULL,
                                     email TEXT NOT NULL,
                                     Full_name TEXT,
                                     password_hash TEXT NOT NULL,
                                     role TEXT NOT NULL CHECK(role IN ('USER', 'ADMIN'))
);

-- ============================
-- INVENTORYITEM TABLE
-- ============================
CREATE TABLE IF NOT EXISTS InventoryItem (
                                             ItemId INTEGER PRIMARY KEY AUTOINCREMENT,
                                             Name TEXT NOT NULL,
                                             Description TEXT,
                                             Price REAL NOT NULL,
                                             ImageUrl TEXT,
                                             IsSold INTEGER NOT NULL DEFAULT 0   -- 0 = false, 1 = true
);

-- ============================
-- SALE TABLE
-- ============================
CREATE TABLE IF NOT EXISTS Sale (
                                    SaleId INTEGER PRIMARY KEY AUTOINCREMENT,
                                    UserId INTEGER NOT NULL,
                                    SubTotal REAL NOT NULL,
                                    Tax REAL NOT NULL,
                                    ShippingMethod TEXT,
                                    ShippingCost REAL NOT NULL,
                                    ShippingDetails TEXT,
                                    ShippingAddress TEXT,
                                    ShippingCity TEXT,
                                    ShippingState TEXT,
                                    ShippingZip TEXT,
                                    Total REAL NOT NULL,
                                    FOREIGN KEY (UserId) REFERENCES users(Userid) ON DELETE CASCADE
);

-- ============================
-- SALE_INVENTORY_ITEM JOIN TABLE
-- ============================
CREATE TABLE IF NOT EXISTS Sale_Inventory_Item (
                                                   SaleId INTEGER NOT NULL,
                                                   ItemId INTEGER NOT NULL,
                                                   PRIMARY KEY (SaleId, ItemId),
                                                   FOREIGN KEY (SaleId) REFERENCES Sale(SaleId) ON DELETE CASCADE,
                                                   FOREIGN KEY (ItemId) REFERENCES InventoryItem(ItemId) ON DELETE CASCADE
);

-- ============================
-- INDEXES FOR PERFORMANCE
-- ============================
CREATE INDEX IF NOT EXISTS idx_users_username ON users(Username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_inventory_item_name ON InventoryItem(Name);
CREATE INDEX IF NOT EXISTS idx_inventory_item_sold ON InventoryItem(IsSold);
CREATE INDEX IF NOT EXISTS idx_sale_user_id ON Sale(UserId);