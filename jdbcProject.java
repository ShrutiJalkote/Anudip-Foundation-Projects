import java.sql.*;
import java.util.Scanner;

public class jdbcProject{
    static final String DB_URL = "jdbc:mysql://localhost:3306/jdbcexample";
    static final String USER = "root";
    static final String PASS = "Shruti@06";

    static Connection conn;
    static Scanner sc = new Scanner(System.in); 

    public static void main(String[] args) {
        try {
            //Load the Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            //Connection establishment
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database!");
            createTable();

        while (true) {
                System.out.println("\n1. Add Product\n2. View All Products\n3. Update Product\n4. Delete Product\n5. View Low Stock Products \n6. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: addProduct(); break;
                    case 2: viewAllProduct(); break;
                    case 3: updateProduct(); break;
                    case 4: deleteProduct(); break;
                    case 5: viewLowStockProducts(); break;
                    case 6:
                        System.out.println("Exiting...");
                        conn.close();
                        sc.close();
                        return;
                    default: System.out.println("Invalid option.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createTable() {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS product_Inventory (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "category VARCHAR(100), " +
                    "price DOUBLE, " +
                    "stock INT)";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createTableQuery);
            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    static void addProduct() {
        try {
            System.out.print("Enter product name: ");
            String name=sc.next();
            System.out.print("Enter product category: ");
            String category=sc.next();
            System.out.print("Enter product price: ");
            double price=sc.nextDouble();
            System.out.print("Enter stock quantity: ");
            int stock=sc.nextInt();

            String query = "INSERT INTO product_Inventory (name, category, price, stock) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);

            int rows=ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    System.out.println("Product added successfully with ID: " + generatedId);
                }
            } else {
                System.out.println("Failed to add product.");
            }
            System.out.println("Product inserted successfully.");
        } catch (Exception e) {
            System.out.println("Error inserting product: " + e.getMessage());
        }
    }

    static void viewAllProduct() {
        try {
            String query = "SELECT * FROM product_Inventory";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Category: " + rs.getString("category")+
                                   ", Price: " + rs.getDouble("price") +
                                    ", Stock: " + rs.getInt("stock"));
            }
        } catch (Exception e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
    }

    static void updateProduct() {
        try {
            System.out.print("Enter ID to update: ");
            int id = sc.nextInt();
            System.out.print("Enter new name: ");
            String name = sc.next();
            System.out.print("Enter new category: ");
            String category = sc.next();
            System.out.print("Enter new category: ");
            Double price = sc.nextDouble();
            System.out.print("Enter new category: ");
            int stock = sc.nextInt();
            


            String query = "UPDATE product_Inventory SET name=?, category=?, price=?, stock=?  WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);

            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    static void deleteProduct() {
        try {
            System.out.print("Enter ID to delete: ");
            int id = sc.nextInt();

            String query = "DELETE FROM product_Inventory WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    static void viewLowStockProducts() {
        try {
            String query = "SELECT * FROM product_Inventory WHERE stock <= 5";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nLow Stock Products (Stock <= 5):");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Category: " + rs.getString("category") +
                                   ", Price: " + rs.getDouble("price") +
                                   ", Stock: " + rs.getInt("stock"));
            }

            if (!found) {
                System.out.println("No low-stock products found.");
            }

        } catch (Exception e) {
            System.out.println("Error retrieving low stock products: " + e.getMessage());
        }
    }

}


