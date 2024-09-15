package Ui;

import DAO.UserDao;
import POJOs.UserPojo;
import java.util.Scanner;

public class SignUpView {

    private UserDao userDao;
    private Scanner scanner;

    public SignUpView() {
        userDao = new UserDao();
        scanner = new Scanner(System.in);
    }

    public void showSignUpView() {
        System.out.println("Sign Up for GMDb");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm your password: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Please try again.");
            showSignUpView();
            return;
        }

        UserPojo newUser = new UserPojo();
        newUser.setUserName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);

        userDao.saveUser(newUser);

        System.out.println("Sign up successful! You can now log in.");
        new UserLoginView().showUserLoginView();
    }
}
