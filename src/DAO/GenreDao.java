package DAO;

import POJOs.GenrePojo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import org.hibernate.Query;

public class GenreDao {

    private final SessionFactory factory;

    public GenreDao() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(GenrePojo.class).buildSessionFactory();
    }

    public void saveGenre(GenrePojo genre) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(genre);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public GenrePojo findById(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        GenrePojo genre = null;
        try {
            transaction = session.beginTransaction();
            genre = (GenrePojo) session.get(GenrePojo.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return genre;
    }
    
    // Update a Genre
    public void updateGenre(GenrePojo genre) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(genre);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // Delete a Genre
    public void deleteGenre(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            GenrePojo movie = (GenrePojo) session.get(GenrePojo.class, id);
            if (movie != null) {
                session.delete(movie);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    public void deleteGenreByName(String name) {
        GenrePojo genre = findByName(name);
        if (genre != null) {
            deleteGenre(genre.getGenreId()); // Assuming GenrePojo has a getId() method
        } else {
            System.out.println("Genre not found.");
        }
    }
    
    // List All Genres
    public List<GenrePojo> findAll() {
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
    
    // Find a Genre by name
    public GenrePojo findByName(String name) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        GenrePojo genre = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from GenrePojo where genreName = :genreName");
            query.setParameter("genreName", name);
            genre = (GenrePojo) query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return genre;
    }
    
    // Save genre if it does not exist
    public GenrePojo saveGenreIfNotExists(String genreName) {
        GenrePojo genre = findByName(genreName);
        if (genre == null) {
            genre = new GenrePojo();
            genre.setGenreName(genreName);
            saveGenre(genre);
        }
        return genre;
    }


}
