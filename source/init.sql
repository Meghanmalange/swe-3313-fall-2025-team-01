CREATE TABLE IF NOT EXISTS users (
  Userid INTEGER PRIMARY KEY AUTOINCREMENT,
  Username TEXT UNIQUE NOT NULL,
  email TEXT NOT NULL,
  Full_name TEXT,
  password_hash TEXT NOT NULL,
  role TEXT NOT NULL CHECK(role IN ('USER', 'ADMIN'))
);

-- Example seed data (you may adjust as needed)
INSERT OR IGNORE INTO users (Userid, Username, email, Full_name, password_hash, role) VALUES
(1, 'alice', 'alice@example.com', 'Alice Example', 'bcrypt_hash_here', 'USER'),
(2, 'bob', 'bob@example.com', 'Bob Example', 'bcrypt_hash_here', 'USER'),
(3, 'carol', 'carol@example.com', 'Carol Example', 'bcrypt_hash_here', 'ADMIN');

PRAGMA foreign_keys = ON;

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

-- Sample Inventory Items
INSERT INTO InventoryItem (Name, Description, Price, ImageUrl, IsSold) VALUES
('Vintage Camera', '1960s film camera in great condition', 120.00, 'camera.jpg', 0),
('Leather Wallet', 'Handmade full-grain leather wallet', 45.50, 'wallet.jpg', 0),
('Antique Vase', 'Ceramic vase from 19th century', 250.00, 'vase.jpg', 1);

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

    FOREIGN KEY (UserId) REFERENCES users(Userid)
);

INSERT INTO Sale (
    UserId, SubTotal, Tax, ShippingMethod, ShippingCost,
    ShippingDetails, ShippingAddress, ShippingCity, ShippingState, ShippingZip, Total
) VALUES (
    1,                      -- UserId
    165.50,                 -- SubTotal
    15.00,                  -- Tax
    'UPS Ground',           -- ShippingMethod
    10.00,                  -- ShippingCost
    'Leave at front door',  -- ShippingDetails
    '123 Main St',          -- ShippingAddress
    'Springfield',          -- ShippingCity
    'IL',                   -- ShippingState
    '62704',                -- ShippingZip
    190.50                  -- Total
);

CREATE TABLE IF NOT EXISTS Sale_Inventory_Item (
    SaleId INTEGER NOT NULL,
    ItemId INTEGER NOT NULL,

    PRIMARY KEY (SaleId, ItemId),

    FOREIGN KEY (SaleId) REFERENCES Sale(SaleId),
    FOREIGN KEY (ItemId) REFERENCES InventoryItem(ItemId)
);

INSERT INTO Sale_Inventory_Item (SaleId, ItemId) VALUES
(1, 1),   -- Sale 1 includes Vintage Camera
(1, 2);   -- Sale 1 includes Leather Wallet

