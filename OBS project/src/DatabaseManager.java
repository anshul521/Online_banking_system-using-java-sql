import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:banking.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            connectToDatabase();
            createTable();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void connectToDatabase() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        System.out.println("Connected to the database.");
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number TEXT PRIMARY KEY," +
                "account_holder_name TEXT NOT NULL," +
                "balance REAL NOT NULL DEFAULT 0.0)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public void createAccount(String accountNumber, String accountHolderName) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, account_holder_name) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountHolderName);
            pstmt.executeUpdate();
            System.out.println("Account created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    public void depositMoney(String accountNumber, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deposited: " + amount);
            } else {
                System.out.println("Account not found.");
            }
        }
    }

    public void withdrawMoney(String accountNumber, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNumber);
            pstmt.setDouble(3, amount);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Withdrew: " + amount);
            } else {
                System.out.println("Insufficient balance or account not found.");
            }
        }
    }

    public double checkBalance(String accountNumber) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                System.out.println("Account not found.");
                return -1;
            }
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}