import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;

public class inventory {
    static ArrayList<String> names = new ArrayList<>();
    static double[] prices = new double[0];
    static HashMap<String, Integer> stock = new HashMap<>();
    static double purchaseTotals = 0;

    public static void main(String[] args) {
        String option;
        do {
            option = JOptionPane.showInputDialog(null,
                    "Choose an option:\n" +
                            "1. Add product\n" +
                            "2. List inventory\n" +
                            "3. Buy product\n" +
                            "4. Show statistics\n" +
                            "5. Search product\n" +
                            "6. Exit",
                    "Main Menu",
                    JOptionPane.QUESTION_MESSAGE);

            if (option == null) break; // usuario cerró la ventana

            switch (option) {
                case "1": addProduct(); break;
                case "2": listProducts(); break;
                case "3": buyProduct(); break;
                case "4": showStatistics(); break;
                case "5": searchProduct(); break;
                case "6": showFinalTicket(); break;
                default: JOptionPane.showMessageDialog(null, "Invalid option");
            }
        } while (!"6".equals(option));
    }

    private static void showFinalTicket() {
        String message = String.format("Thanks for using us.\nPurchase total: $%.2f", purchaseTotals);
        JOptionPane.showMessageDialog(null, message);
    }

    private static void searchProduct() {
        String term = JOptionPane.showInputDialog("Enter name or part of the name:");
        if (term == null || term.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Input empty.");
            return;
        }

        String results = "Results of search:\n";
        boolean found = false;

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (name.toLowerCase().contains(term.toLowerCase())) {
                results += String.format("%s - $%.2f - Stock: %d\n",
                        name, prices[i], stock.get(name));
                found = true;
            }
        }

        if (!found) {
            results += "No matches found.";
        }

        JOptionPane.showMessageDialog(null, results);
    }

    private static void showStatistics() {
        if (prices.length == 0) {
            JOptionPane.showMessageDialog(null, "There are no products to analyze.");
            return;
        }

        double min = prices[0], max = prices[0];
        String prodMin = names.get(0), prodMax = names.get(0);

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < min) {
                min = prices[i];
                prodMin = names.get(i);
            }
            if (prices[i] > max) {
                max = prices[i];
                prodMax = names.get(i);
            }
        }

        String message = String.format("Cheapest product: %s ($%.2f)\nMost expensive product: %s ($%.2f)",
                prodMin, min, prodMax, max);
        JOptionPane.showMessageDialog(null, message);
    }

    private static void buyProduct() {
        if (names.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no products to buy.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter the product name to buy:");
        if (name == null || !names.contains(name)) {
            JOptionPane.showMessageDialog(null, "Product not found.");
            return;
        }

        int index = indexOfName(name);
        int available = stock.get(name);

        String amountStr = JOptionPane.showInputDialog("Enter the amount:");
        if (amountStr == null || !amountStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid amount.");
            return;
        }

        int amount = Integer.parseInt(amountStr);

        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Amount must be greater than 0.");
            return;
        }

        if (amount > available) {
            JOptionPane.showMessageDialog(null, "Insufficient stock: " + available);
            return;
        }

        double cost = amount * prices[index];
        int newStock = available - amount;
        stock.put(name, newStock);
        purchaseTotals += cost;

        JOptionPane.showMessageDialog(null,
                String.format("Successful purchase: %d x %s = $%.2f", amount, name, cost));
    }

    private static void listProducts() {
        if (names.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventory empty.");
            return;
        }

        String inventory = "Inventory:\n";

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            double price = prices[i];
            int amount = stock.get(name);

            inventory += String.format("%d. %s - $%.2f - Stock: %d\n",
                    i + 1, name, price, amount);
        }

        JOptionPane.showMessageDialog(null, inventory);
    }

    private static void addProduct() {
        String name = JOptionPane.showInputDialog("Enter product name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty.");
            return;
        }

        if (names.contains(name)) {
            JOptionPane.showMessageDialog(null, "Product already exists.");
            return;
        }

        String priceStr = JOptionPane.showInputDialog("Enter price:");
        if (priceStr == null || !priceStr.matches("\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(null, "Invalid price.");
            return;
        }
        double price = Double.parseDouble(priceStr);
        if (price <= 0) {
            JOptionPane.showMessageDialog(null, "Price must be greater than 0.");
            return;
        }

        String stockStr = JOptionPane.showInputDialog("Enter quantity in stock:");
        if (stockStr == null || !stockStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid stock.");
            return;
        }
        int amount = Integer.parseInt(stockStr);
        if (amount < 0) {
            JOptionPane.showMessageDialog(null, "Stock cannot be negative.");
            return;
        }

        names.add(name);
        expandPrices(price);
        stock.put(name, amount);

        JOptionPane.showMessageDialog(null, "Product successfully added.");
    }

    private static void expandPrices(double newPrice) {
        double[] newArray = new double[prices.length + 1];
        for (int i = 0; i < prices.length; i++) {
            newArray[i] = prices[i];
        }
        newArray[prices.length] = newPrice;
        prices = newArray;
    }

    static int indexOfName(String name) {
        return names.indexOf(name);
    }
}
