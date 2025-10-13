package edu.aston.userservice.dao;

import edu.aston.userservice.models.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class UserManager {
    private final SessionFactory sessionFactory;

    private static final Logger logger = LogManager.getLogger(UserManager.class);

    public UserManager() {
        try {
            this.sessionFactory = new Configuration().configure().buildSessionFactory();

            logger.info("Connection to the database was completed successfully");
        }
        catch (Exception exception) {
            logger.error("Failed to connect to the database", exception);
            throw new RuntimeException("Failed to connect to the database", exception);
        }
    }

    public void create(final User user) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

            logger.info("User has been created {}", user);
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
                logger.info("Couldn't find the user in the database");
            }

            return Optional.ofNullable(user);
        }
        catch(Exception exception) {
            logger.error("Failed tp find the user in the database", exception);
            throw new RuntimeException("Failed to find the user in the database", exception);
        }
    }

    public void update(final User user) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();

            logger.info("The user has been updated {}", user);
        }
        catch(Exception exception) {
            logger.error("Failed to update user information in the database {}", user, exception);
            throw new RuntimeException("Failed to update user information in the database", exception);
        }
    }

    public void delete(final Long id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            final User user = session.find(User.class, id);

            if(user != null) {
                session.remove(user);

                logger.info("The user has been removed {}", user);
            }
            else {
                logger.info("Couldn't update user information in the database");
            }

            transaction.commit();
        }
        catch(Exception exception) {
            logger.error("Failed to delete user from the database", exception);
            throw new RuntimeException("Failed to delete user from the database", exception);
        }
    }
}
