package edu.aston.userservice.config;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionManager {
    @Getter
    private static SessionFactory sessionFactory;

    private static final Logger logger = LogManager.getLogger(HibernateSessionManager.class);

    public static void init() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();

            logger.info("Connection to the database was completed successfully");

        }
        catch (Exception exception) {
            logger.error("Failed to connect to the database", exception);
            throw new RuntimeException("Failed to connect to the database", exception);
        }
    }

    public static void close() {
        if(sessionFactory == null) {
            return;
        }

        sessionFactory.close();
        logger.info("The session was closed");
    }

    public static boolean isSessionOpen() {
        return sessionFactory != null;
    }
}
