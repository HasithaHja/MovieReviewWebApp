package DAO;

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
import javax.persistence.TypedQuery;
import org.hibernate.Query;

public class UserDao {

    private SessionFactory factory;

    public UserDao() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserPojo.class).buildSessionFactory();
    }

    public void saveUser(UserPojo user) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } 
    }

    public UserPojo findById(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        UserPojo user = null;
        try {
            transaction = session.beginTransaction();
            user = (UserPojo) session.get(UserPojo.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }
    
    // Update a user
    public void updateUser(UserPojo user) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    // Delete a User
    public void deleteUser(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            UserPojo user = (UserPojo) session.get(UserPojo.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    // List All Users
    public List<UserPojo> findAll() {
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
    } finally {
        session.close();
    }
    return users;
    }
    
    // Authenticate user (Login)
    public UserPojo authenticate(String email, String password) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        UserPojo user = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from UserPojo where email = :email and password = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            user = (UserPojo) query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } 
        return user;
    }

    public List<MoviePojo> getPurchasedMovies(int userId) {
    Session session = null;
    Transaction transaction = null;
    List<MoviePojo> purchasedMovies = null;
    
    try {
        session = HibernateUtil.getSessionFactory().openSession();
        // Fetch the user by their userId
        //UserPojo user = (UserPojo) session.get(UserPojo.class, userId);
        transaction = session.beginTransaction();
        String hql = "SELECT p.movie FROM PurchasePojo p WHERE p.user.userId = :userId";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);
        purchasedMovies = query.list();

        // Commit the transaction
        transaction.commit();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return purchasedMovies;
}
    
    public void addReview(int userId, int movieId, int rating, String description) {
    Session session = null;
    Transaction transaction = null;
    try {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        
        // Fetch the user and movie objects from the database
        UserPojo user = (UserPojo) session.get(UserPojo.class, userId);
        MoviePojo movie = (MoviePojo) session.get(MoviePojo.class, movieId);
        
        // Create a new review
        ReviewPojo review = new ReviewPojo();
        review.setRating(rating);
        review.setDescription(description);
        review.setUser(user);  // Link the user
        review.setMovie(movie); // Link the movie
        
        // Save the review in the database
        session.save(review);
        
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    } finally {
        if (session != null) session.close();
    }
}
    
    public boolean hasReviewedMovie(int userId, int movieId) {
    Session session = null;
    Transaction transaction = null;
    boolean hasReviewed = false;
    
    try {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        
        String hql = "FROM ReviewPojo WHERE user.userId = :userId AND movie.movieId = :movieId";
        List<ReviewPojo> reviews = session.createQuery(hql)
                                          .setParameter("userId", userId)
                                          .setParameter("movieId", movieId)
                                          .list();
        hasReviewed = !reviews.isEmpty();
        
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    } finally {
        if (session != null) session.close();
    }
    
    return hasReviewed;
}


    public void deleteReview(int userId, int movieId) {
    Session session = null;
    Transaction transaction = null;

    try {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        
        String hql = "DELETE FROM ReviewPojo WHERE user.userId = :userId AND movie.movieId = :movieId";
        session.createQuery(hql)
               .setParameter("userId", userId)
               .setParameter("movieId", movieId)
               .executeUpdate();
        
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    } finally {
        if (session != null) session.close();
    }
}


    
    public boolean hasPurchasedMovie(int userId, int movieId) {
    Session session = null;
    boolean hasPurchased = false;
    try {
        session = factory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        
        String hql = "SELECT COUNT(p) FROM PurchasePojo p WHERE p.user.userId = :userId AND p.movie.movieId = :movieId";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("movieId", movieId);
        
        Long count = (Long) query.uniqueResult();
        transaction.commit();
        
        hasPurchased = count > 0;
    } catch (Exception e) {
        e.printStackTrace();
    } 
    return hasPurchased;
}



    public void purchaseMovie(int userId, int movieId) {
    Session session = null;
    Transaction transaction = null;
    try {
        session = factory.getCurrentSession();
        transaction = session.beginTransaction();
        
        UserPojo user = (UserPojo) session.get(UserPojo.class, userId);
        MoviePojo movie = (MoviePojo) session.get(MoviePojo.class, movieId);
        
        if (user != null && movie != null) {
            PurchasePojo purchase = new PurchasePojo();
            purchase.setUser(user);
            purchase.setMovie(movie);
            purchase.setPurchaseDate(new Date()); // Set current date or use another date if required
            
            session.save(purchase);
            transaction.commit();
        }
    } catch (Exception e) {
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    } 
  }
}

