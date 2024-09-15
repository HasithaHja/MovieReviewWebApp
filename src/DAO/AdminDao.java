package DAO;

import POJOs.AdminPojo;
import POJOs.GenrePojo;
import POJOs.MoviePojo;
import POJOs.PurchasePojo;
import POJOs.ReviewPojo;
import POJOs.UserPojo;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import org.hibernate.Query;

public class AdminDao {

    private SessionFactory factory;

    public AdminDao() {
        factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(UserPojo.class)
                .addAnnotatedClass(MoviePojo.class)
                .addAnnotatedClass(PurchasePojo.class)
                .addAnnotatedClass(ReviewPojo.class)
                .buildSessionFactory();
   
    }

    // Add a new movie
    public void addMovie(MoviePojo movie) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } 
    }

    // Update a movie
    public void updateMovie(MoviePojo movie) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(movie);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Delete a movie
    public void deleteMovie(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            MoviePojo movie = (MoviePojo) session.get(MoviePojo.class, id);
            if (movie != null) {
                session.delete(movie);
            }else {
                System.out.println("Movie not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // List All Movies
    public List<MoviePojo> findAllMovies() {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<MoviePojo> movies = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from MoviePojo");
            movies = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return movies;
    }

    // List All Users
    public List<UserPojo> findAllUsers() {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<UserPojo> users = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from UserPojo");
            users = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return users;
    }

    // List All Purchases
    public List<PurchasePojo> findAllPurchases() {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<PurchasePojo> purchases = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from PurchasePojo");
            purchases = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return purchases;
    }

    // List All Reviews
    public List<ReviewPojo> findAllReviews() {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<ReviewPojo> reviews = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from ReviewPojo");
            reviews = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return reviews;
    }

    // Get a list of users who purchased a specific movie
    public List<UserPojo> getUsersByMovie(int movieId) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<UserPojo> users = null;
        try {
            transaction = session.beginTransaction();
            String hql = "SELECT p.user FROM PurchasePojo p WHERE p.movie.movieId = :movieId";
            Query query = session.createQuery(hql);
            query.setParameter("movieId", movieId);
            users = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return users;
    }

    // Get a list of movies purchased by a specific user
    public List<MoviePojo> getMoviesByUser(int userId) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<MoviePojo> movies = null;
        try {
            transaction = session.beginTransaction();
            String hql = "SELECT p.movie FROM PurchasePojo p WHERE p.user.userId = :userId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", userId);
            movies = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return movies;
    }

    // Get all reviews for a specific movie
    public List<ReviewPojo> getReviewsByMovie(int movieId) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<ReviewPojo> reviews = null;
        try {
            transaction = session.beginTransaction();
            String hql = "FROM ReviewPojo r WHERE r.movie.movieId = :movieId";
            Query query = session.createQuery(hql);
            query.setParameter("movieId", movieId);
            reviews = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return reviews;
    }

    // Get all reviews by a specific user
    public List<ReviewPojo> getReviewsByUser(int userId) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<ReviewPojo> reviews = null;
        try {
            transaction = session.beginTransaction();
            String hql = "FROM ReviewPojo r WHERE r.user.userId = :userId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", userId);
            reviews = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return reviews;
    }
    
    // Add this method in your AdminDao class
    public AdminPojo authenticate(String email, String password) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        AdminPojo admin = null;
        try {
            transaction = session.beginTransaction();
            String hql = "FROM AdminPojo WHERE email = :email AND password = :password";
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            query.setParameter("password", password);
            admin = (AdminPojo) query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return admin;
    }
    
    public MoviePojo getMovieByName(String movieName) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        MoviePojo movie = null;
        try {
            transaction = session.beginTransaction();
            String hql = "FROM MoviePojo WHERE movieName = :movieName";
            Query query = session.createQuery(hql);
            query.setParameter("movieName", movieName);
            movie = (MoviePojo) query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return movie;
    }
    
    // List all genres
    public List<GenrePojo> findAllGenres() {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        List<GenrePojo> genres = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from GenrePojo");
            genres = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return genres;
    }

    

}
