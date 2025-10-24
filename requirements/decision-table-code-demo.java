public class DecisionTableDemo {

    public static void main(String[] args) {
        // USER RULES
        System.out.println("=== USER DECISION TABLE DEMO ===\n");
        simulateUserRule(1, false, false, false, false, false, false, "");  // Rule 1
        simulateUserRule(2, true, true, true, false, false, false, "");    // Rule 2
        simulateUserRule(5, true, true, false, true, true, true, "Ground"); // Rule 5
        simulateUserRule(6, true, true, false, true, true, true, "3-Day");  // Rule 6
        simulateUserRule(7, true, true, false, true, true, true, "Overnight"); // Rule 7

        // ADMIN RULES
        System.out.println("\n=== ADMIN DECISION TABLE DEMO ===\n");
        simulateAdminRule(1, true, true, false, false, true);  // View Sales
        simulateAdminRule(2, true, false, true, false, true);  // Add Inventory
        simulateAdminRule(3, true, false, false, true, true);  // Promote User
        simulateAdminRule(4, false, false, false, false, false); // Invalid Access
    }

    // USER METHOD
    public static void simulateUserRule(int rule, boolean hasAccount, boolean validPassword,
                                        boolean cartEmpty, boolean bankVerified, boolean paymentConfirmed,
                                        boolean usCitizen, String shippingMethod) {

        System.out.println("Rule " + rule + " Results:");

        // Rule 1: Prompt to register
        if (!hasAccount) {
            System.out.println("→ Prompt user to register");
            return;
        }

        // Rule 2: Show inventory when logged in
        if (hasAccount && validPassword && cartEmpty) {
            System.out.println("→ Grant access and show inventory (sorted high→low)");
            return;
        }

        // Rule 3: Disable checkout when cart empty
        if (hasAccount && validPassword && !cartEmpty && !paymentConfirmed) {
            System.out.println("→ Show checkout page, highlight missing payment info");
            return;
        }

        // Rules 5–7: Shipping options
        if (paymentConfirmed) {
            switch (shippingMethod.toLowerCase()) {
                case "ground":
                    System.out.println("→ Complete order with Ground Shipping");
                    break;
                case "3-day":
                    System.out.println("→ Complete order with 3-Day Shipping");
                    break;
                case "overnight":
                    System.out.println("→ Complete order with Overnight Shipping");
                    break;
                default:
                    System.out.println("→ Complete order (no shipping specified)");
            }
            System.out.println("→ Generate receipt and email confirmation");
        }

        System.out.println();
    }

    // ADMIN METHOD
    public static void simulateAdminRule(int rule, boolean isAdmin, boolean viewSales,
                                         boolean addInventory, boolean promoteUser,
                                         boolean inputValid) {

        System.out.println("Rule " + rule + " Results:");

        if (!isAdmin) {
            System.out.println("→ Access Denied: 'Admin only' message");
            return;
        }

        if (viewSales && inputValid) {
            System.out.println("→ Display sales report");
        } else if (addInventory && inputValid) {
            System.out.println("→ Prompt admin for item details & add item to database");
        } else if (promoteUser && inputValid) {
            System.out.println("→ Transform selected user account into admin");
        } else {
            System.out.println("→ Invalid input fields");
        }

        System.out.println();
    }
}
