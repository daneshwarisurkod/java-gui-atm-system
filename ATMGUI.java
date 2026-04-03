import java.util.*;
import java.io.*;

class ATMAccount {
    int accountNumber;
    int pin;
    double balance;

    ATMAccount(int accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }
}

public class ATMSystem {

    static ArrayList<ATMAccount> accounts = new ArrayList<>();
    static final String FILE_NAME = "accounts.txt";

    public static void loadAccounts() {
        try {
            File file = new File(FILE_NAME);
            if(!file.exists()) return;

            Scanner fileReader = new Scanner(file);
            while(fileReader.hasNext()) {
                accounts.add(new ATMAccount(
                        fileReader.nextInt(),
                        fileReader.nextInt(),
                        fileReader.nextDouble()
                ));
            }
            fileReader.close();
        } catch(Exception e) {
            System.out.println("Error loading accounts.");
        }
    }

    public static void saveAccounts() {
        try {
            PrintWriter writer = new PrintWriter(FILE_NAME);
            for(ATMAccount acc : accounts) {
                writer.println(acc.accountNumber + " " + acc.pin + " " + acc.balance);
            }
            writer.close();
        } catch(Exception e) {
            System.out.println("Error saving accounts.");
        }
    }

    public static void printReceipt(String type, double amount, ATMAccount user) {
        System.out.println("\n------ RECEIPT ------");
        System.out.println("Account : " + user.accountNumber);
        System.out.println("Transaction : " + type);
        System.out.println("Amount : " + amount);
        System.out.println("Balance : " + user.balance);
        System.out.println("---------------------\n");
    }

    public static void main(String[] args) {

        loadAccounts();

        // create default accounts if file empty
        if(accounts.isEmpty()) {
            accounts.add(new ATMAccount(1001,1234,5000));
            accounts.add(new ATMAccount(1002,4321,3000));
            accounts.add(new ATMAccount(1003,1111,7000));
            saveAccounts();
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();

        System.out.print("Enter PIN: ");
        int pin = sc.nextInt();

        ATMAccount user = null;

        for(ATMAccount acc : accounts) {
            if(acc.accountNumber == accNo && acc.pin == pin) {
                user = acc;
                break;
            }
        }

        if(user == null) {
            System.out.println("Invalid login!");
            return;
        }

        while(true) {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Change PIN");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch(choice) {

                case 1:
                    System.out.print("Enter amount: ");
                    double dep = sc.nextDouble();
                    user.balance += dep;
                    saveAccounts();
                    printReceipt("Deposit", dep, user);
                    break;

                case 2:
                    System.out.print("Enter amount: ");
                    double wit = sc.nextDouble();
                    if(wit <= user.balance) {
                        user.balance -= wit;
                        saveAccounts();
                        printReceipt("Withdraw", wit, user);
                    } else {
                        System.out.println("Insufficient balance!");
                    }
                    break;

                case 3:
                    System.out.println("Balance: " + user.balance);
                    break;

                case 4:
                    System.out.print("Enter new PIN: ");
                    user.pin = sc.nextInt();
                    saveAccounts();
                    System.out.println("PIN changed successfully!");
                    break;

                case 5:
                    System.out.println("Thank you!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
