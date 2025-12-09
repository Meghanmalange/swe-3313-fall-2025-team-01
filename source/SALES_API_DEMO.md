# Sales Logic Implementation

## Overview
This document demonstrates the sales logic implementation for the African Royals jewelry e-commerce application. The sales system handles:

1. **Recording sales** from checkout receipts
2. **Updating inventory** when items are sold (TIS-8)
3. **Generating receipt summaries**
4. **Providing sales/{id} endpoint** for viewing receipts

## Implementation Details

### Entities Created

#### Sale Entity (`src/main/java/africanroyals/entity/Sale.java`)
- Stores sale information including user ID, pricing details, and timestamp
- Links to SaleItem entities via one-to-many relationship

#### SaleItem Entity (`src/main/java/africanroyals/entity/SaleItem.java`)
- Stores individual items sold in each sale
- Links back to Sale entity and references inventory items

### Services Implemented

#### SalesService Interface (`src/main/java/africanroyals/service/sales/SalesService.java`)
- `recordSale(Receipt receipt)` - Records a sale and updates inventory
- `getSaleById(Long saleId)` - Retrieves a sale for viewing
- `generateReceiptFromSale(Sale sale)` - Converts sale back to receipt format

#### SalesServiceImpl (`src/main/java/africanroyals/service/sales/SalesServiceImpl.java`)
- Implements the sales logic with transaction management
- Integrates with inventory service to reduce stock levels
- Handles conversion between Receipt and Sale entities

### API Endpoints

#### GET /api/sales/{id}
Returns a receipt for a specific sale ID.

**Example Response:**
```json
{
  "orderId": 1,
  "userId": 123,
  "items": [
    {
      "itemId": 1,
      "name": "Royal Necklace",
      "unitPrice": 1200.00
    },
    {
      "itemId": 2,
      "name": "Zulu Bracelet",
      "unitPrice": 80.00
    }
  ],
  "priceDetails": {
    "subtotal": 1280.00,
    "tax": 128.00,
    "shippingFee": 19.00,
    "grandTotal": 1427.00
  },
  "shippingOption": "THREE_DAY",
  "createdAt": "2025-12-09T14:30:00"
}
```

#### POST /api/sales/record
Records a new sale from a checkout receipt.

**Example Request:**
```json
{
  "orderId": null,
  "userId": 123,
  "items": [
    {
      "itemId": 1,
      "name": "Royal Necklace",
      "unitPrice": 1200.00
    }
  ],
  "priceDetails": {
    "subtotal": 1200.00,
    "tax": 120.00,
    "shippingFee": 19.00,
    "grandTotal": 1339.00
  },
  "shippingOption": "THREE_DAY",
  "createdAt": "2025-12-09T14:30:00"
}
```

## Integration with Checkout

The checkout service has been updated to automatically record sales:

```java
// In CheckoutServiceImpl.checkout()
Sale savedSale = salesService.recordSale(receipt);
receipt.setOrderId(savedSale.getId()); // Set the actual sale ID
```

## Inventory Management (TIS-8)

When a sale is recorded:
1. Each cart item's inventory is reduced by 1 (jewelry items are unique)
2. If inventory update fails, the entire transaction is rolled back
3. This ensures data consistency between sales and inventory

## Testing the Sales Endpoint

Once the application is running, you can test the sales endpoint:

```bash
# Test viewing a receipt (assuming sale ID 1 exists)
curl -X GET http://localhost:8080/api/sales/1

# Test recording a sale
curl -X POST http://localhost:8080/api/sales/record \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "items": [
      {
        "itemId": 1,
        "name": "Royal Necklace",
        "unitPrice": 1200.00
      }
    ],
    "priceDetails": {
      "subtotal": 1200.00,
      "tax": 120.00,
      "shippingFee": 19.00,
      "grandTotal": 1339.00
    },
    "shippingOption": "THREE_DAY"
  }'
```

## Database Schema

The sales logic creates the following tables:

### sales
- id (PRIMARY KEY, AUTO_INCREMENT)
- user_id (FOREIGN KEY to users)
- subtotal (DECIMAL)
- tax (DECIMAL)
- shipping_fee (DECIMAL)
- grand_total (DECIMAL)
- timestamp (DATETIME)

### sale_items
- id (PRIMARY KEY, AUTO_INCREMENT)
- sale_id (FOREIGN KEY to sales)
- inventory_item_id (FOREIGN KEY to inventory_items)
- item_name (VARCHAR)
- unit_price (DECIMAL)
- quantity (INT, always 1 for jewelry)

## Key Features Implemented

✅ **Record sale + sale-item entries**
✅ **Remove sold items from inventory (TIS-8)**
✅ **Generate receipt summary**
✅ **Sales/{id} view receipt endpoint**
✅ **Transaction management for data consistency**
✅ **Integration with checkout process**

## Next Steps

To fully test the sales functionality:
1. Resolve compilation issues with other application components
2. Start the Spring Boot application
3. Use the provided curl commands to test the endpoints
4. Verify that inventory is properly updated when sales are recorded
