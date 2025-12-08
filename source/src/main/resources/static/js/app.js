// Frontend JavaScript - calls Spring Boot REST API endpoints only
// This file is served by Spring Boot and makes requests to Spring Boot controllers
// API_BASE is empty string to use relative URLs (same server/port as Spring Boot)
const API_BASE = '';

// Tab management
function showTab(tabName) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });

    // Show selected tab
    document.getElementById(`${tabName}-tab`).classList.add('active');
    event.target.classList.add('active');

    // Load data for the tab
    if (tabName === 'inventory') {
        loadInventory();
    } else if (tabName === 'cart') {
        loadUserCart();
    } else if (tabName === 'sales') {
        loadSales();
    }
}

// Show message
function showMessage(text, type = 'info') {
    const messageEl = document.getElementById('message');
    messageEl.textContent = text;
    messageEl.className = `message ${type}`;
    messageEl.style.display = 'block';
    setTimeout(() => {
        messageEl.style.display = 'none';
    }, 3000);
}

// Load inventory
async function loadInventory() {
    try {
        const response = await fetch(`${API_BASE}/inventory`);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Failed to load inventory: ${errorText}`);
        }
        const items = await response.json();
        
        const inventoryList = document.getElementById('inventory-list');
        if (!items || items.length === 0) {
            inventoryList.innerHTML = '<p style="text-align: center; padding: 20px; color: #666;">No items available in inventory</p>';
            return;
        }

        // Display inventory items with better formatting
        inventoryList.innerHTML = items.map(item => {
            // Escape HTML to prevent XSS
            const name = escapeHtml(item.name || 'Unnamed Item');
            const description = escapeHtml(item.description || 'No description available');
            const price = item.price ? parseFloat(item.price).toFixed(2) : '0.00';
            const itemId = item.id || item.itemId;
            const isSold = item.isSold === true || item.isSold === 1;
            
            return `
            <div class="item-card ${isSold ? 'sold' : ''}">
                <h3>${name}</h3>
                <div class="price">$${price}</div>
                <div class="description">${description}</div>
                <div class="item-info">
                    <strong>Item ID:</strong> ${itemId}<br>
                    <strong>Status:</strong> ${isSold ? '<span style="color: #f44336;">Sold Out</span>' : '<span style="color: #4caf50;">Available</span>'}
                </div>
                ${item.imageUrl ? `<img src="${item.imageUrl}" alt="${name}" style="max-width: 100%; border-radius: 8px; margin-bottom: 10px; max-height: 200px; object-fit: cover;">` : ''}
                <button onclick="addToCart(${itemId}, '${name.replace(/'/g, "\\'")}', ${price})" 
                        ${isSold ? 'disabled' : ''}
                        class="${isSold ? 'disabled-btn' : ''}">
                    ${isSold ? 'Sold Out' : 'Add to Cart'}
                </button>
            </div>
        `;
        }).join('');
    } catch (error) {
        console.error('Error loading inventory:', error);
        showMessage('Failed to load inventory: ' + error.message, 'error');
        document.getElementById('inventory-list').innerHTML = 
            `<p style="text-align: center; padding: 20px; color: #f44336;">Error loading inventory: ${error.message}</p>`;
    }
}

// Helper function to escape HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Add to cart
async function addToCart(itemId, name, unitPrice) {
    const userId = document.getElementById('userId').value;
    if (!userId) {
        showMessage('Please enter a User ID', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/cart/items`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: parseInt(userId),
                itemId: itemId,
                name: name,
                unitPrice: unitPrice
            })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        const cart = await response.json();
        showMessage('Item added to cart!', 'success');
        loadUserCart();
    } catch (error) {
        console.error('Error adding to cart:', error);
        showMessage('Failed to add item to cart', 'error');
    }
}

// Load user cart
async function loadUserCart() {
    const userId = document.getElementById('userId').value;
    if (!userId) {
        document.getElementById('cart-items').innerHTML = '<p>Please enter a User ID</p>';
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/cart/${userId}`);
        if (!response.ok) throw new Error('Failed to load cart');
        const cart = await response.json();
        
        const cartItems = document.getElementById('cart-items');
        const cartSummary = document.getElementById('cart-summary');

        if (!cart.items || cart.items.length === 0) {
            cartItems.innerHTML = '<p>Cart is empty</p>';
            cartSummary.style.display = 'none';
            return;
        }

        cartItems.innerHTML = cart.items.map(item => `
            <div class="cart-item">
                <div class="cart-item-info">
                    <h4>${item.name}</h4>
                    <div class="price">$${item.unitPrice.toFixed(2)}</div>
                </div>
                <button onclick="removeFromCart(${item.itemId})">Remove</button>
            </div>
        `).join('');

        // Show summary
        cartSummary.style.display = 'block';
        const subtotal = cart.items.reduce((sum, item) => sum + parseFloat(item.unitPrice), 0);
        document.getElementById('cart-totals').innerHTML = `
            <div class="total-line">
                <span>Subtotal:</span>
                <span>$${subtotal.toFixed(2)}</span>
            </div>
            <div class="total-line">
                <span>Items:</span>
                <span>${cart.items.length}</span>
            </div>
        `;
    } catch (error) {
        console.error('Error loading cart:', error);
        showMessage('Failed to load cart', 'error');
        document.getElementById('cart-items').innerHTML = '<p>Error loading cart</p>';
    }
}

// Remove from cart
async function removeFromCart(itemId) {
    const userId = document.getElementById('userId').value;
    if (!userId) {
        showMessage('Please enter a User ID', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/cart/${userId}/items/${itemId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Failed to remove item');
        
        showMessage('Item removed from cart', 'success');
        loadUserCart();
    } catch (error) {
        console.error('Error removing from cart:', error);
        showMessage('Failed to remove item', 'error');
    }
}

// Checkout
async function checkout() {
    const userId = document.getElementById('userId').value;
    if (!userId) {
        showMessage('Please enter a User ID', 'error');
        return;
    }

    const shippingOption = document.getElementById('shipping-option').value;

    try {
        const response = await fetch(`${API_BASE}/checkout`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: parseInt(userId),
                shippingOption: shippingOption,
                cardHolderName: 'Test User',
                cardLastFourDigits: '1234'
            })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        const receipt = await response.json();
        showMessage(`Checkout successful! Order Total: $${receipt.priceDetails.grandTotal.toFixed(2)}`, 'success');
        
        // Clear cart display
        document.getElementById('cart-items').innerHTML = '<p>Cart is empty</p>';
        document.getElementById('cart-summary').style.display = 'none';
        
        // Reload sales to show the new sale
        if (document.getElementById('sales-tab').classList.contains('active')) {
            loadSales();
        }
    } catch (error) {
        console.error('Error during checkout:', error);
        showMessage('Checkout failed: ' + error.message, 'error');
    }
}

// Load sales
async function loadSales() {
    try {
        const userId = document.getElementById('sales-user-id').value;
        let url = `${API_BASE}/sales`;
        if (userId) {
            url = `${API_BASE}/sales/user/${userId}`;
        }

        const response = await fetch(url);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Failed to load sales: ${response.status} ${errorText}`);
        }
        const sales = await response.json();
        
        const salesList = document.getElementById('sales-list');
        if (sales.length === 0) {
            salesList.innerHTML = '<p>No sales found</p>';
            return;
        }

        salesList.innerHTML = sales.map(sale => {
            const saleDate = sale.saleDate ? new Date(sale.saleDate).toLocaleString() : 'N/A';
            const items = sale.items || [];
            
            return `
                <div class="sale-card">
                    <h3>Sale #${sale.id}</h3>
                    <div class="sale-info">
                        <div class="info-item">
                            <div class="info-label">Date</div>
                            <div class="info-value">${saleDate}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">User ID</div>
                            <div class="info-value">${sale.user ? sale.user.id : 'N/A'}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Subtotal</div>
                            <div class="info-value">$${sale.subTotal ? sale.subTotal.toFixed(2) : '0.00'}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Tax</div>
                            <div class="info-value">$${sale.tax ? sale.tax.toFixed(2) : '0.00'}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Shipping</div>
                            <div class="info-value">$${sale.shippingCost ? sale.shippingCost.toFixed(2) : '0.00'}</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Total</div>
                            <div class="info-value" style="color: #667eea; font-size: 1.2em;">$${sale.total.toFixed(2)}</div>
                        </div>
                    </div>
                    ${items.length > 0 ? `
                        <div class="sale-items">
                            <h4>Items:</h4>
                            ${items.map(item => `
                                <div class="sale-item">
                                    <span>${item.name || 'Unknown Item'}</span>
                                    <span>$${item.price ? item.price.toFixed(2) : '0.00'}</span>
                                </div>
                            `).join('')}
                        </div>
                    ` : ''}
                </div>
            `;
        }).join('');
    } catch (error) {
        console.error('Error loading sales:', error);
        showMessage('Failed to load sales: ' + error.message, 'error');
        document.getElementById('sales-list').innerHTML = 
            `<p style="text-align: center; padding: 20px; color: #f44336;">Error loading sales: ${error.message}</p>`;
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    loadInventory();
});

