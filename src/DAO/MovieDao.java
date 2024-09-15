package DAO;

import POJOs.MoviePojo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import org.hibernate.Query;

public class MovieDao {

    private SessionFactory factory;

    public MovieDao() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(MoviePojo.class).buildSessionFactory();
    }

    // Save a Movie
    public void saveMovie(MoviePojo movie) {
        Session session = null;
    Transaction transaction = null;
    try {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.save(movie);
        transaction.commit();
    } catch (Exception ex) {
        if (transaction != null) transaction.rollback();
        ex.printStackTrace();
    } finally {
        if (session != null) session.close();
    }
    }

    // Get Movie by ID
    public MoviePojo findById(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        MoviePojo movie = null;
        try {
            transaction = session.beginTransaction();
            movie = (MoviePojo) session.get(MoviePojo.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return movie;
    }

    // Update a Movie
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
        } finally {
            session.close();
        }
    }

    // Delete a Movie
    public void deleteMovie(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            MoviePojo movie = (MoviePojo) session.get(MoviePojo.class, id);
            if (movie != null) {
                session.delete(movie);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // List All Movies
    public List<MoviePojo> findAll() {
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
    
    public MoviePojo getMovieByName(String movieName) {
    Session session = factory.getCurrentSession();
    Transaction transaction = null;
    MoviePojo movie = null;
    try {
        transaction = session.beginTransaction();
        
        // Query to get the movie by name
        Query query = session.createQuery("FROM MoviePojo WHERE movieName = :movieName");
        query.setParameter("movieName", movieName);
        movie = (MoviePojo) query.uniqueResult(); // Retrieve a single result
        
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    } 
    return movie;
}
}
