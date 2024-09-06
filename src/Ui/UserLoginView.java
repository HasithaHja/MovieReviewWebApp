package Ui;

import DAO.MovieDao;
import DAO.UserDao;
import POJOs.MoviePojo;
import POJOs.UserPojo;
import java.util.List;
import java.util.Scanner;

public class UserLoginView {

    private UserDao userDao;
    private MovieDao movieDao;
    private Scanner scanner;

    public UserLoginView() {
        userDao = new UserDao();
        scanner = new Scanner(System.in);
        movieDao = new MovieDao();
    }

    public void showUserLoginView() {
        System.out.println("Welcome to GMDb User Login");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        UserPojo user = userDao.authenticate(email, password);

        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getUserName() + "!");
            // Redirect to user-specific view
            showUserMenu(user);
        } else {
            System.out.println("Invalid email or password. Please try again.");
            System.out.println("1. Try again");
            System.out.println("2. Sign up");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 2) {
                showSignUpView();
            } else {
                showUserLoginView();
            }
        }
    }

    private void showUserMenu(UserPojo user) {
        int choice;
        do {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View All Movies");
            System.out.println("2. Search for a Movie");
            System.out.println("3. View and Review Purchased Movies");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewAllMovies(user);
                    break;
                case 2:
                    searchMovie(user);
                    break;
                case 3:
                    viewPurchasedMovies(user);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        } while (choice != 4);
    }

    private void viewAllMovies(UserPojo user) {
        System.out.println("\n--- All Movies ---");
        List<MoviePojo> movies = movieDao.findAll();
        
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        
        for (MoviePojo movie : movies) {
            System.out.println(movie.getMovieName() + " - Rating: " + movie.getAverageRating());
        }
        
        // Allow the user to purchase a movie after viewing the list
        System.out.print("\nWould you like to purchase a movie? (yes/no): ");
        String purchaseChoice = scanner.nextLine();
        if (purchaseChoice.equalsIgnoreCase("yes")) {
            purchaseMovie(user);
        }
    }

    private void searchMovie(UserPojo user) {
        System.out.print("\nEnter the name of the movie: ");
        String movieName = scanner.nextLine();
        MoviePojo movie = movieDao.getMovieByName(movieName);

        if (movie != null) {
            System.out.println("Movie found: " + movie.getMovieName() + " - Rating: " + movie.getAverageRating());
            
            // Ask the user if they would like to purchase the movie
            System.out.print("Would you like to purchase this movie? (yes/no): ");
            String purchaseChoice = scanner.nextLine();
            if (purchaseChoice.equalsIgnoreCase("yes")) {
                purchaseMovie(user, movie);
            }
        } else {
            System.out.println("Movie not found.");
        }
    }
    
    // Method to handle the user entering the movie name to purchase
    private void purchaseMovie(UserPojo user) {
        System.out.print("Enter the name of the movie you want to purchase: ");
        String movieName = scanner.nextLine();
        MoviePojo movie = movieDao.getMovieByName(movieName);

        if (movie != null) {
            // Call the purchaseMovie method with both the user and the movie
            purchaseMovie(user, movie);
        } else {
            System.out.println("Movie not found.");
        }
    }

    // Method that checks and processes the movie purchase
    private void purchaseMovie(UserPojo user, MoviePojo movie) {
        // Check if the user already purchased this movie
        if (userDao.hasPurchasedMovie(user.getUserId(), movie.getMovieId())) {
            System.out.println("You have already purchased this movie.");
            return;
        }

        // Proceed with purchase
        userDao.purchaseMovie(user.getUserId(), movie.getMovieId());
        System.out.println("You have successfully purchased the movie: " + movie.getMovieName());
    }


    private void viewPurchasedMovies(UserPojo user) {
        System.out.println("\n--- Your Purchased Movies ---");
        List<MoviePojo> purchasedMovies = userDao.getPurchasedMovies(user.getUserId());
        if (purchasedMovies.isEmpty()) {
            System.out.println("You have not purchased any movies.");
            return;
        }

        for (MoviePojo movie : purchasedMovies) {
            System.out.println(movie.getMovieName() + " - Rating: " + movie.getAverageRating());
            System.out.print("Would you like to review this movie? (yes/no): ");
            String reviewChoice = scanner.nextLine();
            if (reviewChoice.equalsIgnoreCase("yes")) {
                addReview(user, movie);
            }
        }
    }

    private void addReview(UserPojo user, MoviePojo movie) {
        System.out.print("Enter your rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your review: ");
        String reviewText = scanner.nextLine();

        userDao.addReview(user.getUserId(), movie.getMovieId(), rating, reviewText);
        System.out.println("Thank you for your review!");
    }

    private void showSignUpView() {
        // Redirect to signup view
        new SignUpView().showSignUpView();
    }
}
