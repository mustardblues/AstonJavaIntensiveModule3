package edu.aston.userservice.dao;

import edu.aston.userservice.config.HibernateSessionManager;
import edu.aston.userservice.models.User;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class UserDAO {
    private SessionFactory sessionFactory;

    private static final Logger logger = LogManager.getLogger(UserDAO.class);

    public void initSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User create(final User user) {
        Transaction transaction = null;

        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

            logger.info("User has been created {}", user);

            return user;
        }
        catch(Exception exception) {
            logger.error("Failed to create a new user to the database {}", user, exception);
            throw new RuntimeException("Failed to create a new user to the database", exception);
        }
    }

    public Optional<User> read(final Long id) {
        try(Session session = sessionFactory.openSession()) {
            final User user = session.find(User.class, id);

            if(user != null) {
                logger.info("The user has been read {}", user);
            }
            else {
                logger.error("Failed tp find the user in the database");
                throw new RuntimeException("Failed to find the user in the database");
            }

            return Optional.of(user);
        }
    }

    public void update(final User user) {
        Transaction transaction = null;

        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();

            logger.info("The user has been updated {}", user);
        }
        catch(Exception exception) {
            logger.error("Failed to update user information in the database {}", user, exception);
            throw new RuntimeException("Failed to update user information in the database", exception);
        }
    }

    @Transactional
    public void delete(final Long id) {
        Transaction transaction = null;

        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            final User user = session.find(User.class, id);

            if(user != null) {
                session.remove(user);

                logger.info("The user has been removed {}", user);
            }
            else {
                logger.error("Failed to delete user from the database");
                throw new RuntimeException("Failed to delete user from the database");
            }

            transaction.commit();
        }
    }
}
