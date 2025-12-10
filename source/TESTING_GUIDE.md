# Login Testing Guide

## Test Accounts

The following test accounts are automatically created when the application starts (if database is empty):

### Admin Accounts (Multiple for Team Testing)
- **Username:** `admin` | **Password:** `admin123` | **Role:** ADMIN
- **Username:** `admin2` | **Password:** `admin123` | **Role:** ADMIN
- **Username:** `admin3` | **Password:** `admin123` | **Role:** ADMIN
- **Username:** `admin4` | **Password:** `admin123` | **Role:** ADMIN

All admin accounts redirect to `/admin-catalog` with full admin access.

### Regular User Account
- **Username:** `customer`
- **Password:** `password123`
- **Role:** USER
- **Expected Behavior:** Redirects to `/catalog` with limited access

**Note:** These accounts are only created when the database is empty. If you need to reset and recreate them, delete the database file at `src/main/resources/db/africanroyals.db` and restart the application.

---

## Step-by-Step Testing Instructions

### 1. Start the Application

```bash
mvn spring-boot:run
```

Or run from your IDE by executing `Application.java`

The application will start on `http://localhost:8080` (default Spring Boot port)

---

### 2. Test Admin Login

#### Test Case 1: Successful Admin Login
1. Navigate to `http://localhost:8080/login`
2. Enter:
   - Username: `admin`
   - Password: `admin123`
3. Click "Login"
4. **Expected Result:** 
   - ✅ Redirected to `/admin-catalog`
   - ✅ Can access admin features (Add Item, Sales Report, Promote User)
   - ✅ Session contains: userId, username="admin", role="ADMIN"

#### Test Case 2: Admin Accessing Admin Routes
1. While logged in as admin, try accessing:
   - `http://localhost:8080/admin-catalog` ✅ Should work
   - `http://localhost:8080/add-item` ✅ Should work
   - `http://localhost:8080/sales-report` ✅ Should work
   - `http://localhost:8080/promote-user` ✅ Should work

#### Test Case 3: Invalid Admin Credentials
1. Navigate to `http://localhost:8080/login`
2. Enter:
   - Username: `admin`
   - Password: `wrongpassword`
3. Click "Login"
4. **Expected Result:**
   - ✅ Stays on login page
   - ✅ Shows error message: "Invalid username or password"

---

### 3. Test Regular User Login

#### Test Case 4: Successful User Login
1. First, logout if logged in as admin (or use incognito/private window)
2. Navigate to `http://localhost:8080/login`
3. Enter:
   - Username: `customer`
   - Password: `password123`
4. Click "Login"
5. **Expected Result:**
   - ✅ Redirected to `/catalog`
   - ✅ Can browse items and make purchases
   - ✅ Session contains: userId, username="customer", role="USER"

#### Test Case 5: User Trying to Access Admin Routes
1. While logged in as regular user, try accessing:
   - `http://localhost:8080/admin-catalog` → ✅ Should redirect to `/catalog`
   - `http://localhost:8080/add-item` → ✅ Should redirect to `/catalog`
   - `http://localhost:8080/sales-report` → ✅ Should redirect to `/catalog`
   - `http://localhost:8080/promote-user` → ✅ Should redirect to `/catalog`

#### Test Case 6: Invalid User Credentials
1. Navigate to `http://localhost:8080/login`
2. Enter:
   - Username: `customer`
   - Password: `wrongpassword`
3. Click "Login"
4. **Expected Result:**
   - ✅ Stays on login page
   - ✅ Shows error message: "Invalid username or password"

---

### 4. Test Security Features

#### Test Case 7: Unauthenticated Access
1. Open a new incognito/private browser window
2. Try accessing protected routes directly:
   - `http://localhost:8080/catalog` → ✅ Should redirect to `/login`
   - `http://localhost:8080/admin-catalog` → ✅ Should redirect to `/login`
   - `http://localhost:8080/add-item` → ✅ Should redirect to `/login`

#### Test Case 8: Already Logged In
1. Log in as admin
2. Navigate to `http://localhost:8080/login` again
3. **Expected Result:**
   - ✅ Automatically redirected to `/admin-catalog` (if admin)
   - ✅ Or `/catalog` (if regular user)

#### Test Case 9: Logout Functionality
1. While logged in, navigate to `http://localhost:8080/logout`
2. **Expected Result:**
   - ✅ Session is invalidated
   - ✅ Redirected to `/login`
   - ✅ Trying to access `/catalog` redirects back to `/login`

---

### 5. Test Input Validation

#### Test Case 10: Empty Username
1. Navigate to `http://localhost:8080/login`
2. Leave username empty, enter any password
3. Click "Login"
4. **Expected Result:**
   - ✅ Shows error: "Username is required"

#### Test Case 11: Empty Password
1. Navigate to `http://localhost:8080/login`
2. Enter username, leave password empty
3. Click "Login"
4. **Expected Result:**
   - ✅ Shows error: "Password is required"

#### Test Case 12: Non-existent Username
1. Navigate to `http://localhost:8080/login`
2. Enter:
   - Username: `nonexistent`
   - Password: `anything`
3. Click "Login"
4. **Expected Result:**
   - ✅ Shows error: "Invalid username or password"

---

## Quick Test Checklist

- [ ] Admin can login with correct credentials
- [ ] Admin is redirected to `/admin-catalog`
- [ ] Admin can access all admin routes
- [ ] Regular user can login with correct credentials
- [ ] Regular user is redirected to `/catalog`
- [ ] Regular user cannot access admin routes (redirected to `/catalog`)
- [ ] Invalid credentials show error message
- [ ] Empty fields show validation errors
- [ ] Unauthenticated users are redirected to login
- [ ] Logout works correctly
- [ ] Already logged-in users are auto-redirected

---

## Browser Developer Tools Testing

You can verify session data in browser developer tools:

1. Open Developer Tools (F12)
2. Go to Application/Storage tab
3. Check Cookies → `JSESSIONID` should exist when logged in
4. Or check Network tab → Request Headers → Cookie contains session ID

---

## Database Verification (Optional)

To verify users exist in database, you can check:
- Database file: `src/main/resources/db/africanroyals.db`
- Use SQLite browser or command line to query:
  ```sql
  SELECT Userid, Username, role FROM users;
  ```

Expected output:
```
Userid | Username  | role
-------|-----------|-------
1      | customer  | USER
2      | admin     | ADMIN
```

