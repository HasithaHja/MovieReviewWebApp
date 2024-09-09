package Ui;

import DAO.AdminDao;
import DAO.GenreDao;
import POJOs.AdminPojo;
import POJOs.GenrePojo;
import POJOs.MoviePojo;
import POJOs.ReviewPojo;
import POJOs.UserPojo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AdminLoginView {

    private AdminDao adminDao;
    private Scanner scanner;
    private GenreDao genreDao;

    public AdminLoginView() {
        adminDao = new AdminDao();
        scanner = new Scanner(System.in);
        this.genreDao = new GenreDao();
    }

    public void showAdminLoginView() {
        System.out.println("Welcome to GMDb Admin Login");
        System.out.print("Enter admin email: ");
        String email = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        AdminPojo admin = adminDao.authenticate(email, password);

        if (admin != null) {
            System.out.println("Admin login successful! Welcome, " + admin.getAdminName() + "!");
            showAdminMenu(admin);
        } else {
            System.out.println("Invalid admin email or password. Please try again.");
            showAdminLoginView();
        }
    }

     private void showAdminMenu(AdminPojo admin) {
        int choice;
        do {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. View All Movies");
            System.out.println("3. Add New Movie");
            System.out.println("4. Edit a Movie");
            System.out.println("5. Delete a Movie");
            System.out.println("6. View User Reviews");
            System.out.println("7. Add New Genre");
            System.out.println("8. Delete a Genre");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    viewAllMovies();
                    break;
                case 3:
                    addNewMovie();
                    break;
                case 4:
                    editMovie();
                    break;
                case 5:
                    deleteMovie();
                    break;
                case 6:
                    viewUserReviews();
                    break;
                case 7:
                    addGenre();
                    break;
                case 8:
                    deleteGenre();
                    break;  
                case 9:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        } while (choice != 9);
    }

    private void viewAllUsers() {
        System.out.println("\n--- All Users ---");
        List<UserPojo> users = adminDao.findAllUsers();

        if (users != null && !users.isEmpty()) {
            for (UserPojo user : users) {
                System.out.println("User ID: " + user.getUserId());
                System.out.println("Name: " + user.getUserName());
                System.out.println("Email: " + user.getEmail());
                System.out.println("------------------------");
            }
        } else {
            System.out.println("No users found.");
        }
    }

    private void viewAllMovies() {
        System.out.println("\n--- All Movies ---");
        List<MoviePojo> movies = adminDao.findAllMovies();

        if (movies != null && !movies.isEmpty()) {
            for (MoviePojo movie : movies) {
                System.out.println("Movie ID: " + movie.getMovieId());
                System.out.println("Movie Name: " + movie.getMovieName());
                System.out.println("Release Date: " + movie.getReleaseDate());
                // Print genres as a comma-separated list
                System.out.print("Genres: ");
                if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                    System.out.println(movie.getGenres());
                } else {
                    System.out.println("No genres assigned.");
                }
                System.out.println("------------------------");
            }
        } else {
            System.out.println("No movies found.");
        }
    }

    private void addNewMovie() {
        String movieName = null;
        String director = null;
        int runtime = 0;
        Date releaseDate = null;
        List<GenrePojo> genres = new ArrayList<>(); // List to store movie genres

        // Prompt for movie name with cancellation option
        while (true) {
            System.out.print("\nEnter movie name (or type 'cancel' to return to the menu): ");
            movieName = scanner.nextLine();
            if (movieName.equalsIgnoreCase("cancel")) {
                System.out.println("Movie addition cancelled.");
                return;  // Exit the method if the process is cancelled
            }
            if (!movieName.trim().isEmpty()) {
                break;  // Exit the loop if input is valid
            }
            System.out.println("Movie name cannot be empty. Please try again.");
        }

        // Prompt for director name with cancellation option
        while (true) {
            System.out.print("Enter director name (or type 'cancel' to return to the menu): ");
            director = scanner.nextLine();
            if (director.equalsIgnoreCase("cancel")) {
                System.out.println("Movie addition cancelled.");
                return;  // Exit the method if the process is cancelled
            }
            if (!director.trim().isEmpty()) {
                break;  // Exit the loop if input is valid
            }
            System.out.println("Director name cannot be empty. Please try again.");
        }

        // Prompt for runtime with cancellation option and input validation
        while (true) {
            System.out.print("Enter runtime (in minutes, or type 'cancel' to return to the menu): ");
            String runtimeInput = scanner.nextLine();
            if (runtimeInput.equalsIgnoreCase("cancel")) {
                System.out.println("Movie addition cancelled.");
                return;  // Exit the method if the process is cancelled
            }
            try {
                runtime = Integer.parseInt(runtimeInput);
                if (runtime > 0) {
                    break;  // Exit the loop if the runtime is valid
                }
                System.out.println("Runtime must be a positive number. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for runtime.");
            }
        }

        // Prompt for release date with cancellation option and input validation
        while (true) {
            System.out.print("Enter release date (yyyy-MM-dd, or type 'cancel' to return to the menu): ");
            String releaseDateString = scanner.nextLine();
            if (releaseDateString.equalsIgnoreCase("cancel")) {
                System.out.println("Movie addition cancelled.");
                return;  // Exit the method if the process is cancelled
            }
            try {
                releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateString);
                break;  // Exit the loop if the date is valid
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter date in yyyy-MM-dd format.");
            }
        }

        // Handle genres for the movie with interactive prompts and streamlined genre addition
        while (true) {
            System.out.print("Enter genre name (or type 'done' when finished, 'cancel' to return to the menu): ");
            String genreInput = scanner.nextLine();

            // Handle cancellation
            if (genreInput.equalsIgnoreCase("cancel")) {
                System.out.println("Movie addition cancelled.");
                return;  // Exit the method if the process is cancelled
            }

            // Handle done
            if (genreInput.equalsIgnoreCase("done")) {
                if (!genres.isEmpty()) {
                    break;  // Exit the loop if at least one genre is added
                } else {
                    System.out.println("Please add at least one genre.");
                    continue;
                }
            }

            // Check if genre exists
            GenrePojo genre = genreDao.findByName(genreInput);

            if (genre == null) {
                // Ask if the admin wants to add the new genre
                System.out.println("Genre not found. Would you like to add it? (yes/no): ");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("yes")) {
                    // Streamline genre addition using saveGenreIfNotExists
                    genre = genreDao.saveGenreIfNotExists(genreInput);  // Automatically saves if it doesn't exist
                    genres.add(genre);  // Add new genre to the movie's genre list
                    System.out.println("New genre added successfully!");
                } else {
                    System.out.println("Skipping genre addition.");
                }
            } else {
                // If genre exists, add it to the list
                genres.add(genre);
                System.out.println("Genre added: " + genre.getGenreName());
            }
        }


        // Create and add the movie after all inputs are valid
        MoviePojo movie = new MoviePojo();
        movie.setMovieName(movieName);
        movie.setDirector(director);
        movie.setRuntime(runtime);
        movie.setReleaseDate(releaseDate);
        movie.setGenres(genres);  // Set the genres for the movie

        adminDao.addMovie(movie);
        System.out.println("Movie added successfully!");
    }



    private void editMovie() {
        System.out.print("\nEnter the movie name to edit: ");
        String movieName = scanner.nextLine();

        // Check if the movie exists
        MoviePojo movie = adminDao.getMovieByName(movieName);
        if (movie != null) {
            System.out.println("Editing Movie: " + movie.getMovieName());

            // Movie Name
            System.out.print("Enter new movie name (leave blank to keep the same): ");
            String newMovieName = scanner.nextLine();
            if (!newMovieName.isEmpty()) {
                movie.setMovieName(newMovieName);
            }

            // Director Name
            System.out.print("Enter new director name (leave blank to keep the same): ");
            String newDirector = scanner.nextLine();
            if (!newDirector.isEmpty()) {
                movie.setDirector(newDirector);
            }

            // Runtime
            System.out.print("Enter new runtime (leave blank to keep the same): ");
            String newRuntime = scanner.nextLine();
            if (!newRuntime.isEmpty()) {
                movie.setRuntime(Integer.parseInt(newRuntime));
            }

            // Genres
            System.out.println("Current genres: " + movie.getGenres());

            List<GenrePojo> availableGenres = adminDao.findAllGenres();
            System.out.println("Available genres: ");
            for (GenrePojo genre : availableGenres) {
                System.out.println(genre.getGenreName());
            }

            System.out.print("Enter new genres separated by commas (leave blank to keep the same): ");
            String newGenresInput = scanner.nextLine();
            if (!newGenresInput.isEmpty()) {
                String[] newGenres = newGenresInput.split(",");
                List<GenrePojo> genreList = new ArrayList<>();
                for (String genreName : newGenres) {
                    genreName = genreName.trim(); // Remove extra spaces
                    boolean genreFound = false;
                    for (GenrePojo availableGenre : availableGenres) {
                        if (availableGenre.getGenreName().equalsIgnoreCase(genreName)) {
                            genreList.add(availableGenre);
                            genreFound = true;
                            break;
                        }
                    }
                    if (!genreFound) {
                        System.out.println("Genre '" + genreName + "' is not available. Please use only available genres.");
                    }
                }
                movie.setGenres(genreList);
            }

            // Update the movie in the database
            adminDao.updateMovie(movie);
            System.out.println("Movie updated successfully!");
        } else {
            System.out.println("Movie not found.");
        }
    }



    private void deleteMovie() {
        System.out.print("\nEnter the movie name to delete: ");
        String movieName = scanner.nextLine();

        MoviePojo movie = adminDao.getMovieByName(movieName);
        if (movie != null) {
            adminDao.deleteMovie(movie.getMovieId());
            System.out.println("Movie deleted successfully!");
        } else {
            System.out.println("Movie not found.");
        }
    }

    private void viewUserReviews() {
        System.out.print("\nEnter the movie name to view reviews: ");
        String movieName = scanner.nextLine();

        // Fetch the movie by name
        MoviePojo movie = adminDao.getMovieByName(movieName);

        if (movie != null) {
            // Fetch reviews using the movie ID
            int movieId = movie.getMovieId();
            List<ReviewPojo> reviews = adminDao.getReviewsByMovie(movieId);

            // Display the reviews
            if (reviews != null && !reviews.isEmpty()) {
                System.out.println("Reviews for movie: " + movie.getMovieName());
                for (ReviewPojo review : reviews) {
                    System.out.println(review);
                }
            } else {
                System.out.println("No reviews available for this movie.");
            }
        } else {
            System.out.println("Movie not found.");
        }
    }
    
    private void addGenre() {
        if (genreDao == null) {
            System.out.println("Error: genreDao is null.");
            return;
        }
        
        System.out.print("\nEnter the name of the new genre: ");
        String genreName = scanner.nextLine();

        if (!genreName.trim().isEmpty()) {
            // Check if the genre already exists
            GenrePojo existingGenre = genreDao.findByName(genreName);
            if (existingGenre == null) {
                // Save the new genre if it does not exist
                GenrePojo newGenre = new GenrePojo();
                newGenre.setGenreName(genreName);
                genreDao.saveGenre(newGenre);
                System.out.println("New genre added successfully!");
            } else {
                System.out.println("Genre already exists: " + existingGenre.getGenreName());
            }
        } else {
            System.out.println("Genre name cannot be empty.");
        }
    }
    
    private void deleteGenre() {
        System.out.print("\nEnter the name of the genre to delete: ");
        String genreName = scanner.nextLine();

        if (!genreName.trim().isEmpty()) {
            genreDao.deleteGenreByName(genreName);
        } else {
            System.out.println("Genre name cannot be empty.");
        }
    }

}
