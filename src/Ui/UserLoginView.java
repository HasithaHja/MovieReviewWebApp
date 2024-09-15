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

    // Display all purchased movies
    for (int i = 0; i < purchasedMovies.size(); i++) {
        MoviePojo movie = purchasedMovies.get(i);
        System.out.println((i + 1) + ". " + movie.getMovieName() + " - Rating: " + movie.getAverageRating());
    }

    // Loop to allow the user to select which movie to review
    while (true) {
        System.out.print("\nEnter the number of the movie you want to review (or 0 to exit): ");
        int movieIndex = Integer.parseInt(scanner.nextLine());

        // If the user enters 0, exit the review section
        if (movieIndex == 0) {
            System.out.println("Exiting the review section.");
            break;
        }

        // Check if the user selected a valid movie
        if (movieIndex > 0 && movieIndex <= purchasedMovies.size()) {
            MoviePojo selectedMovie = purchasedMovies.get(movieIndex - 1);
            System.out.println("You selected: " + selectedMovie.getMovieName());

            // Check if the user has already reviewed this movie
            if (userDao.hasReviewedMovie(user.getUserId(), selectedMovie.getMovieId())) {
                System.out.println("You have already reviewed this movie.");

                // Prompt the user to update the review
                System.out.print("Would you like to update your review? (yes/no): ");
                String updateChoice = scanner.nextLine();
                
                if (updateChoice.equalsIgnoreCase("yes")) {
                    updateReview(user, selectedMovie);  // Call the method to update the review
                }
            } else {
                // If no previous review exists, prompt for a new review
                addReview(user, selectedMovie);  // Call the method to add a new review
            }
        } else {
            System.out.println("Invalid selection. Please try again.");
        }
    }
}

    private void addReview(UserPojo user, MoviePojo movie) {
        int rating = 0;
        String reviewText = "";

        // Keep prompting for a valid rating until it's between 1 and 5
        while (true) {
            System.out.print("Enter your rating (1-5): ");
            rating = Integer.parseInt(scanner.nextLine());

            if (rating >= 1 && rating <= 5) {
                break;  // Valid rating, exit the loop
            } else {
                System.out.println("Invalid rating! Please enter a rating between 1 and 5.");
            }
        }

        System.out.print("Enter your review: ");
        reviewText = scanner.nextLine();

        userDao.addReview(user.getUserId(), movie.getMovieId(), rating, reviewText);
        System.out.println("Thank you for your review!");
    }

    
    // Method to update an existing review
    private void updateReview(UserPojo user, MoviePojo movie) {
        // First, delete the existing review
        userDao.deleteReview(user.getUserId(), movie.getMovieId());
        System.out.println("Your previous review has been deleted.");

        // Now prompt the user to add a new review
        addReview(user, movie);
    }

    private void showSignUpView() {
        // Redirect to signup view
        new SignUpView().showSignUpView();
    }
}
