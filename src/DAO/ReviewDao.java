package DAO;

import POJOs.ReviewPojo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import org.hibernate.Query;

public class ReviewDao {

    private SessionFactory factory;

    public ReviewDao() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(ReviewPojo.class).buildSessionFactory();
    }

    public void saveReview(ReviewPojo review) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public ReviewPojo findById(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        ReviewPojo review = null;
        try {
            transaction = session.beginTransaction();
            review = (ReviewPojo) session.get(ReviewPojo.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return review;
    }

    // Update a review
    public void updateReview(ReviewPojo review) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    // Delete a Review
    public void deleteReview(int id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            ReviewPojo movie = (ReviewPojo) session.get(ReviewPojo.class, id);
            if (movie != null) {
                session.delete(movie);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    // List All Reviews
    public List<ReviewPojo> findAll() {
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
    } finally {
        session.close();
    }
    return reviews;
    }
}
