import java.sql.SQLException;
import java.util.Scanner;

public class OnlineBankingSystem {
    private static DatabaseManager dbManager = new DatabaseManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            menu();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            dbManager.closeConnection();
        }
    }

    private static void menu() throws SQLException {
        while (true) {
            System.out.println("\nOnline Banking System");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Create Account
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String accountHolderName = scanner.nextLine();
                    dbManager.createAccount(accountNumber, accountHolderName);
                    break;
                case 2:
                    // Deposit Money
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    dbManager.depositMoney(accountNumber, depositAmount);
                    break;
                case 3:
                    // Withdraw Money
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    dbManager.withdrawMoney(accountNumber, withdrawAmount);
                    break;
                case 4:
                    // Check Balance
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    double balance = dbManager.checkBalance(accountNumber);
                    if (balance >= 0) {
                        System.out.println("Current balance: " + balance);
                    }
                    break;
                case 5:
                    // Exit
                    System.out.println("Exiting the system. Goodbye!");
                    return; // Exit the menu loop
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
    }
}