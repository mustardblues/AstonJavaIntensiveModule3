package edu.aston.userservice.config;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionManagerTest {
    private static final String CONFIG_FILE = "test-hibernate.cfg.xml";

    @Getter
    private static SessionFactory sessionFactory;

    public static void init() {
        try {
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .configure(CONFIG_FILE);

            final String url = System.getProperty("hibernate.connection.url");
            final String username = System.getProperty("hibernate.connection.username");
            final String password = System.getProperty("hibernate.connection.password");

            if(url != null) {
                builder.applySetting("hibernate.connection.url", url);
            }

            if(username != null) {
                builder.applySetting("hibernate.connection.username", username);
            }

            if(password != null) {
                builder.applySetting("hibernate.connection.password", password);
            }

            sessionFactory = new MetadataSources(builder.build())
                    .buildMetadata()
                    .buildSessionFactory();
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to connect to the database", exception);
        }
    }

    public static void close() {
        if(sessionFactory == null) {
            return;
        }

        sessionFactory.close();
    }

    public static boolean isSessionOpen() {
        return sessionFactory != null;
    }
}

