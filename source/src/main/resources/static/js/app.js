// code that i (meghan) made
window.addEventListener('DOMContentLoaded', function () {
    const toast = document.getElementById('cart-toast');
    if (toast) {
        setTimeout(function () {
            toast.classList.add('cart-toast-hide');
        }, 3000);
    }
});


// API Base URL
const API_BASE = '';

// Global state
let currentView = 'admin';
let currentUser = null;
let cart = {
    items: [],
    userId: null
};

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    checkAuthStatus();
    initializeCart();
});

// ============================================================================
// AUTHENTICATION
// ============================================================================

async function checkAuthStatus() {
    try {
        const response = await fetch(`${API_BASE}/auth/current`);
        if (response.ok) {
            const user = await response.json();
            currentUser = user;
            onLoginSuccess(user);
        } else {
            // Not logged in - show auth view for customers
            currentUser = null;
            if (currentView === 'user') {
                showAuthView();
            } else {
                // Admin view doesn't require login
                loadCatalog();
            }
        }
    } catch (error) {
        console.error('Error checking auth status:', error);
        loadCatalog();
    }
}

function showAuthView() {
    const authView = document.getElementById('auth-view');
    const userView = document.getElementById('user-view');
    const adminView = document.getElementById('admin-view');

    authView.style.display = 'block';
    userView.classList.remove('active');
    adminView.classList.remove('active');

    // Hide cart and checkout buttons
    document.getElementById('cart-btn').style.display = 'none';
    document.getElementById('checkout-btn').style.display = 'none';
    document.getElementById('view-toggle-btn').style.display = 'inline-block';
}

function showLoginForm() {
    document.getElementById('login-form').style.display = 'block';
    document.getElementById('signup-form').style.display = 'none';
}

function showSignupForm() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('signup-form').style.display = 'block';
}

async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Login failed');
        }

        const user = await response.json();
        currentUser = user;
        onLoginSuccess(user);
        showMessage('Welcome back, ' + user.username + '!', 'success');
    } catch (error) {
        console.error('Login error:', error);
        showMessage(error.message, 'error');
    }
}

async function handleSignup(event) {
    event.preventDefault();

    const fullName = document.getElementById('signup-fullname').value;
    const username = document.getElementById('signup-username').value;
    const email = document.getElementById('signup-email').value;
    const password = document.getElementById('signup-password').value;
    const confirmPassword = document.getElementById('signup-confirm-password').value;

    // Validate passwords match
    if (password !== confirmPassword) {
        showMessage('Passwords do not match', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ fullName, username, email, password })
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Registration failed');
        }

        const user = await response.json();
        currentUser = user;
        onLoginSuccess(user);
        showMessage('Account created successfully! Welcome, ' + user.username + '!', 'success');
    } catch (error) {
        console.error('Signup error:', error);
        showMessage(error.message, 'error');
    }
}

function onLoginSuccess(user) {
    // Update cart with user ID
    cart.userId = user.userId;
    saveCart();

    // Show user info in header
    document.getElementById('logged-username').textContent = user.username;
    document.getElementById('user-info').style.display = 'flex';
    document.getElementById('logout-btn').style.display = 'inline-block';

    // Hide auth view, show user view
    document.getElementById('auth-view').style.display = 'none';
    document.getElementById('user-view').classList.add('active');
    document.getElementById('admin-view').classList.remove('active');

    // Update buttons
    document.getElementById('cart-btn').style.display = 'inline-block';
    document.getElementById('checkout-btn').style.display = 'inline-block';
    document.getElementById('view-toggle-btn').textContent = 'Admin View';
    document.getElementById('view-toggle-btn').onclick = () => switchView('admin');
    document.getElementById('header-subtitle').textContent = '';

    currentView = 'user';

    // Load catalog
    loadCatalog();
    updateCartDisplay();
}

async function logout() {
    try {
        await fetch(`${API_BASE}/auth/logout`, { method: 'POST' });

        // Clear user state
        currentUser = null;
        cart = { items: [], userId: null };
        localStorage.removeItem('africanJewelryCart');

        // Hide user info
        document.getElementById('user-info').style.display = 'none';
        document.getElementById('logout-btn').style.display = 'none';

        // Show auth view
        showAuthView();
        showLoginForm();

        showMessage('Logged out successfully', 'success');
    } catch (error) {
        console.error('Logout error:', error);
        showMessage('Logout failed', 'error');
    }
}

// ============================================================================
// VIEW MANAGEMENT
// ============================================================================

function switchView(view) {
    currentView = view;
    const userView = document.getElementById('user-view');
    const adminView = document.getElementById('admin-view');
    const authView = document.getElementById('auth-view');
    const toggleBtn = document.getElementById('view-toggle-btn');
    const cartBtn = document.getElementById('cart-btn');
    const checkoutBtn = document.getElementById('checkout-btn');
    const subtitle = document.getElementById('header-subtitle');

    if (view === 'user') {
        // Check if user is logged in
        if (!currentUser) {
            showAuthView();
            return;
        }

        authView.style.display = 'none';
        userView.classList.add('active');
        adminView.classList.remove('active');
        toggleBtn.textContent = 'Admin View';
        toggleBtn.onclick = () => switchView('admin');
        cartBtn.style.display = 'inline-block';
        checkoutBtn.style.display = 'inline-block';
        subtitle.textContent = '';
        loadCatalog();
        updateCartDisplay();
    } else {
        authView.style.display = 'none';
        adminView.classList.add('active');
        userView.classList.remove('active');
        toggleBtn.textContent = 'Customer View';
        toggleBtn.onclick = () => switchView('user');
        cartBtn.style.display = 'none';
        checkoutBtn.style.display = 'none';
        subtitle.textContent = 'ADMIN';
    }
}

function showUserTab(tabName) {
    // Update tab buttons
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.classList.remove('active');
    });
    event.target.classList.add('active');

    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(content => {
        content.style.display = 'none';
    });

    // Show selected tab
    const selectedTab = document.getElementById(`${tabName}-tab`);
    if (selectedTab) {
        selectedTab.style.display = 'block';
    }

    // Load data if needed
    if (tabName === 'catalog') {
        loadCatalog();
    } else if (tabName === 'cart') {
        updateCartDisplay();
    }
}

function showAdminSection(section) {
    // Update sidebar
    document.querySelectorAll('.sidebar-item').forEach(item => {
        item.classList.remove('active');
    });
    event.target.classList.add('active');

    // Hide all sections
    document.querySelectorAll('.admin-content').forEach(content => {
        content.style.display = 'none';
    });

    // Show selected section
    const sectionMap = {
        'search': 'admin-search',
        'add-item': 'admin-add-item',
        'sales-report': 'admin-sales-report'
    };

    const selectedSection = document.getElementById(sectionMap[section]);
    if (selectedSection) {
        selectedSection.style.display = 'block';
    }

    // Load data if needed
    if (section === 'sales-report') {
        loadSalesReport();
    }
}

// ============================================================================
// CATALOG / INVENTORY
// ============================================================================

async function loadCatalog() {
    try {
        const response = await fetch(`${API_BASE}/inventory`);
        if (!response.ok) throw new Error('Failed to load catalog');
        const items = await response.json();

        const grid = document.getElementById('products-grid');
        if (!items || items.length === 0) {
            grid.innerHTML = '<div class="empty-state"><h3>No items available</h3><p>Check back soon for new jewelry</p></div>';
            return;
        }

        grid.innerHTML = items.map(item => createProductCard(item)).join('');
    } catch (error) {
        console.error('Error loading catalog:', error);
        showMessage('Failed to load catalog', 'error');
        document.getElementById('products-grid').innerHTML =
            '<div class="empty-state"><h3>Error loading catalog</h3><p>Please try again later</p></div>';
    }
}

function createProductCard(item) {
    const isSold = item.isSold === true || item.isSold === 1;
    const imageUrl = item.imageUrl || 'https://via.placeholder.com/400x350/1a1a1a/D4AF37?text=No+Image';

    return `
        <div class="product-card">
            <img src="${imageUrl}" alt="${escapeHtml(item.name)}" class="product-image" 
                 onerror="this.src='https://via.placeholder.com/400x350/1a1a1a/D4AF37?text=No+Image'">
            <div class="product-info">
                <h3 class="product-name">${escapeHtml(item.name)}</h3>
                ${item.description ? `<p class="product-description">${escapeHtml(item.description)}</p>` : ''}
                <div class="product-price">$${parseFloat(item.price).toFixed(2)}</div>
                <span class="product-status ${isSold ? 'status-sold' : 'status-available'}">
                    ${isSold ? 'Sold Out' : 'Available'}
                </span>
                <button class="btn btn-primary" style="width: 100%;" 
                        onclick="addToCart(${item.id}, '${escapeHtml(item.name).replace(/'/g, "\\'")}', ${item.price})"
                        ${isSold ? 'disabled' : ''}>
                    ${isSold ? 'Sold Out' : 'Add to Cart'}
                </button>
            </div>
            </div>
        `;
}

// ============================================================================
// CART MANAGEMENT
// ============================================================================

function initializeCart() {
    const savedCart = localStorage.getItem('africanJewelryCart');
    if (savedCart) {
        cart = JSON.parse(savedCart);
    }
    updateCartCount();
}

function saveCart() {
    localStorage.setItem('africanJewelryCart', JSON.stringify(cart));
    updateCartCount();
}

function updateCartCount() {
    const countEl = document.getElementById('cart-count');
    if (countEl) {
        countEl.textContent = cart.items.length;
    }
}

async function addToCart(itemId, name, price) {
    // Check if user is logged in
    if (!currentUser) {
        showMessage('Please log in to add items to cart', 'error');
        showAuthView();
        return;
    }

    // Check if item already in cart
    if (cart.items.find(item => item.itemId === itemId)) {
        showMessage('Item already in cart', 'info');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/cart/items`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: cart.userId,
                itemId: itemId,
                name: name,
                unitPrice: price
            })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        // Add to local cart
        cart.items.push({
            itemId: itemId,
            name: name,
            unitPrice: price
        });

        saveCart();
        showMessage('Added to cart!', 'success');
    } catch (error) {
        console.error('Error adding to cart:', error);
        showMessage('Failed to add to cart', 'error');
    }
}

function removeFromCart(itemId) {
    cart.items = cart.items.filter(item => item.itemId !== itemId);
    saveCart();
    updateCartDisplay();
    showMessage('Item removed from cart', 'success');
}

function showCart() {
    switchView('user');
    showUserTab('cart');
}

function updateCartDisplay() {
    const cartItemsEl = document.getElementById('cart-items');
    const proceedBtn = document.getElementById('proceed-checkout-btn');

    if (cart.items.length === 0) {
        cartItemsEl.innerHTML = '<div class="empty-state"><h3>Your cart is empty</h3><p>Add some beautiful jewelry to get started</p></div>';
        proceedBtn.disabled = true;
        updateCartSummary();
        return;
    }

    proceedBtn.disabled = false;

    cartItemsEl.innerHTML = cart.items.map(item => `
        <div class="order-item">
            <div class="order-item-details">
                <div class="order-item-name">${escapeHtml(item.name)}</div>
                <div class="order-item-price">$${parseFloat(item.unitPrice).toFixed(2)}</div>
            </div>
            <button class="btn btn-secondary" onclick="removeFromCart(${item.itemId})">Remove</button>
            </div>
        `).join('');

    updateCartSummary();
}

function updateCartSummary() {
    const subtotal = cart.items.reduce((sum, item) => sum + parseFloat(item.unitPrice), 0);
    const tax = subtotal * 0.06;

    const shippingOption = document.getElementById('shipping-option').value;
    let shippingCost = 0;
    if (shippingOption === 'THREE_DAY') shippingCost = 19.00;
    if (shippingOption === 'OVERNIGHT') shippingCost = 29.00;

    const total = subtotal + tax + shippingCost;

    document.getElementById('cart-subtotal').textContent = `$${subtotal.toFixed(2)}`;
    document.getElementById('cart-tax').textContent = `$${tax.toFixed(2)}`;
    document.getElementById('cart-shipping').textContent = `$${shippingCost.toFixed(2)}`;
    document.getElementById('cart-total').textContent = `$${total.toFixed(2)}`;
}

// ============================================================================
// CHECKOUT FLOW
// ============================================================================

function proceedToCheckout() {
    if (!currentUser) {
        showMessage('Please log in to proceed to checkout', 'error');
        showAuthView();
        return;
    }

    if (cart.items.length === 0) {
        showMessage('Your cart is empty', 'error');
        return;
    }
    showCheckout();
}

function showCheckout() {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(content => {
        content.style.display = 'none';
    });

    // Show checkout
    const checkoutTab = document.getElementById('checkout-tab');
    checkoutTab.style.display = 'block';

    // Populate checkout items
    const checkoutItemsEl = document.getElementById('checkout-items');
    checkoutItemsEl.innerHTML = cart.items.map(item => `
        <div class="order-item">
            <div class="order-item-details">
                <div class="order-item-name">${escapeHtml(item.name)}</div>
                <div class="order-item-price">$${parseFloat(item.unitPrice).toFixed(2)}</div>
            </div>
        </div>
    `).join('');

    // Update checkout summary
    updateCheckoutSummary();

    // Copy shipping option from cart
    const cartShipping = document.getElementById('shipping-option').value;
    document.getElementById('checkout-shipping-option').value = cartShipping;
}

function updateCheckoutSummary() {
    const subtotal = cart.items.reduce((sum, item) => sum + parseFloat(item.unitPrice), 0);
    const tax = subtotal * 0.06;

    const shippingOption = document.getElementById('checkout-shipping-option')?.value || 'GROUND';
    let shippingCost = 0;
    if (shippingOption === 'THREE_DAY') shippingCost = 19.00;
    if (shippingOption === 'OVERNIGHT') shippingCost = 29.00;

    const total = subtotal + tax + shippingCost;

    document.getElementById('checkout-summary').innerHTML = `
        <div class="summary-line">
            <span>Subtotal</span>
            <span>$${subtotal.toFixed(2)}</span>
        </div>
        <div class="summary-line">
            <span>Tax (6%)</span>
            <span>$${tax.toFixed(2)}</span>
        </div>
        <div class="summary-line">
            <span>Shipping (${shippingOption === 'GROUND' ? 'Ground' : shippingOption === 'THREE_DAY' ? 'Three Day' : 'Overnight'})</span>
            <span>$${shippingCost.toFixed(2)}</span>
        </div>
        <div class="summary-line total">
            <span>Total</span>
            <span>$${total.toFixed(2)}</span>
        </div>
    `;
}

async function completePurchase() {
    if (!currentUser) {
        showMessage('Please log in to complete purchase', 'error');
        showAuthView();
        return;
    }

    const cardName = document.getElementById('card-name').value;
    const cardNumber = document.getElementById('card-number').value;

    if (!cardName || !cardNumber) {
        showMessage('Please fill in all payment information', 'error');
        return;
    }

    // Get last 4 digits of card
    const lastFour = cardNumber.replace(/\s/g, '').slice(-4);

    const shippingOption = document.getElementById('checkout-shipping-option').value;

    try {
        const response = await fetch(`${API_BASE}/checkout`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: cart.userId,
                shippingOption: shippingOption,
                cardHolderName: cardName,
                cardLastFourDigits: lastFour
            })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        const receipt = await response.json();

        // Clear cart
        cart.items = [];
        saveCart();

        // Show confirmation
        showConfirmation();

        showMessage('Order completed successfully!', 'success');
    } catch (error) {
        console.error('Error completing purchase:', error);
        showMessage('Checkout failed: ' + error.message, 'error');
    }
}

function showConfirmation() {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(content => {
        content.style.display = 'none';
    });

    // Show confirmation
    const confirmationTab = document.getElementById('confirmation-tab');
    confirmationTab.style.display = 'block';
}

function continueShopping() {
    // Reset to catalog
    document.querySelectorAll('.tab-content').forEach(content => {
        content.style.display = 'none';
    });
    document.getElementById('catalog-tab').style.display = 'block';

    // Reset tabs
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelector('[data-tab="catalog"]').classList.add('active');

    // Reload catalog
    loadCatalog();
}

// ============================================================================
// ADMIN FUNCTIONS
// ============================================================================

async function addItem(event) {
    event.preventDefault();

    const name = document.getElementById('item-name').value;
    const price = parseFloat(document.getElementById('item-price').value);
    const description = document.getElementById('item-description').value;
    const imageUrl = document.getElementById('item-image-url').value;

    if (!name || !price || price <= 0) {
        showMessage('Please provide valid name and price', 'error');
        return;
    }

    const item = {
        name: name,
        price: price,
        description: description || null,
        imageUrl: imageUrl || null,
        isSold: false
    };

    try {
        const response = await fetch(`${API_BASE}/admin/items`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        const savedItem = await response.json();
        showMessage('Item added successfully!', 'success');

        // Reset form
        document.getElementById('add-item-form').reset();
        document.getElementById('upload-text').textContent = 'Click to upload or drag and drop';

        // Reload catalog if in user view
        if (currentView === 'user') {
            loadCatalog();
        }
    } catch (error) {
        console.error('Error adding item:', error);
        showMessage('Failed to add item: ' + error.message, 'error');
    }
}

function handleImageUpload(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            document.getElementById('item-image-url').value = e.target.result;
            document.getElementById('upload-text').textContent = file.name;
            showMessage('Image loaded (preview only - not uploaded)', 'info');
        };
        reader.readAsDataURL(file);
    }
}

async function searchItems() {
    const query = document.getElementById('search-query').value.toLowerCase();

    if (!query) {
        showMessage('Please enter a search term', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/inventory`);
        if (!response.ok) throw new Error('Failed to search items');
        const items = await response.json();

        const results = items.filter(item =>
            item.name.toLowerCase().includes(query) ||
            (item.description && item.description.toLowerCase().includes(query))
        );

        const resultsEl = document.getElementById('search-results');

        if (results.length === 0) {
            resultsEl.innerHTML = '<div class="empty-state"><h3>No results found</h3><p>Try a different search term</p></div>';
            return;
        }

        resultsEl.innerHTML = `
            <div class="products-grid">
                ${results.map(item => createProductCard(item)).join('')}
            </div>
        `;
    } catch (error) {
        console.error('Error searching items:', error);
        showMessage('Search failed', 'error');
    }
}

async function loadSalesReport() {
    const userId = document.getElementById('filter-user-id').value;
    const resultsEl = document.getElementById('sales-report-content');

    try {
        let url = `${API_BASE}/sales`;
        if (userId) {
            url = `${API_BASE}/sales/user/${userId}`;
        }

        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to load sales');
        const sales = await response.json();

        if (sales.length === 0) {
            resultsEl.innerHTML = '<div class="empty-state"><h3>No sales found</h3><p>No transactions match your criteria</p></div>';
            return;
        }

        resultsEl.innerHTML = `
            <table class="sales-table">
                <thead>
                    <tr>
                        <th>Sale ID</th>
                        <th>Date</th>
                        <th>User ID</th>
                        <th>Items</th>
                        <th>Subtotal</th>
                        <th>Tax</th>
                        <th>Shipping</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    ${sales.map(sale => `
                        <tr>
                            <td>#${sale.id}</td>
                            <td>${new Date(sale.saleDate).toLocaleDateString()}</td>
                            <td>${sale.user ? sale.user.id : 'N/A'}</td>
                            <td>${sale.items ? sale.items.length : 0}</td>
                            <td>$${sale.subTotal ? sale.subTotal.toFixed(2) : '0.00'}</td>
                            <td>$${sale.tax ? sale.tax.toFixed(2) : '0.00'}</td>
                            <td>$${sale.shippingCost ? sale.shippingCost.toFixed(2) : '0.00'}</td>
                            <td style="color: var(--gold); font-weight: 700;">$${sale.total.toFixed(2)}</td>
                        </tr>
                            `).join('')}
                </tbody>
            </table>
            `;
    } catch (error) {
        console.error('Error loading sales report:', error);
        showMessage('Failed to load sales report', 'error');
        resultsEl.innerHTML = '<div class="empty-state"><h3>Error loading report</h3><p>Please try again</p></div>';
    }
}

// ============================================================================
// UTILITY FUNCTIONS
// ============================================================================

function showMessage(text, type = 'info') {
    const messageEl = document.getElementById('message');
    messageEl.textContent = text;
    messageEl.className = `message ${type}`;
    messageEl.style.display = 'block';

    setTimeout(() => {
        messageEl.style.display = 'none';
    }, 4000);
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Format card number with spaces
document.addEventListener('DOMContentLoaded', () => {
    const cardNumberInput = document.getElementById('card-number');
    if (cardNumberInput) {
        cardNumberInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\s/g, '');
            let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
            e.target.value = formattedValue;
        });
    }

    const cardExpiryInput = document.getElementById('card-expiry');
    if (cardExpiryInput) {
        cardExpiryInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 2) {
                value = value.slice(0, 2) + '/' + value.slice(2, 4);
            }
            e.target.value = value;
        });
    }

    // Update checkout summary when shipping changes
    const checkoutShippingOption = document.getElementById('checkout-shipping-option');
    if (checkoutShippingOption) {
        checkoutShippingOption.addEventListener('change', updateCheckoutSummary);
    }
});