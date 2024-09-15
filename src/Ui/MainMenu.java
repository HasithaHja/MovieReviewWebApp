package Ui;

import java.util.Scanner;

public class MainMenu {

    private Scanner scanner;

    public MainMenu() {
        scanner = new Scanner(System.in);
    }

    public void showMainMenu() {
        System.out.println("Welcome to GMDb");
        System.out.println("Please select your role:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.println("3. Sign up");
        System.out.print("Choose an option: ");
        
        String input = scanner.nextLine();
        int choice = -1;

        // Validate input: Check if it's a number and not empty
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            showMainMenu();
            return;
        }

        switch (choice) {
            case 1:
                // Redirect to Admin login
                new AdminLoginView().showAdminLoginView();
                break;
            case 2:
                // Redirect to User login
                new UserLoginView().showUserLoginView();
                break;
            case 3:
                // Redirect to Signup
                new SignUpView().showSignUpView();
                break;
            default:
                System.out.println("Invalid option. Please choose again.");
                showMainMenu();
                break;
        }
    }

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.showMainMenu();
    }
}
