package edu.aston.userservice.dao;

import edu.aston.userservice.config.HibernateSessionManager;
import edu.aston.userservice.config.HibernateSessionManagerTest;
import edu.aston.userservice.models.User;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDAO userDAO;

    @BeforeAll
    void setup() {
        if (postgres.getJdbcUrl() == null || postgres.getJdbcUrl().isEmpty()) {
            throw new IllegalStateException("Failed to start PostgreSQL");
        }

        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        HibernateSessionManagerTest.init();

        this.userDAO = new UserDAO();
        this.userDAO.initSessionFactory(HibernateSessionManagerTest.getSessionFactory());
    }

    @AfterAll
    void close() {
        HibernateSessionManagerTest.close();
    }

    @BeforeEach
    void clean() {
        try(Session session = HibernateSessionManagerTest.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createMutationQuery("DELETE FROM User").executeUpdate();

            transaction.commit();
        }
    }

    @Test
    void testDAOCreate() {
        final String name = "Alex";
        final String email = "alex@email.com";
        final int age = 24;

        final User user = new User(name, email, age);

        final User result = this.userDAO.create(user);

        assertNotNull(result.getId());
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(age, result.getAge());
        assertNotNull(result.getCreatedAt());

        try (Session session = HibernateSessionManagerTest.getSessionFactory().openSession()) {
            final User userDB = session.find(User.class, result.getId());

            assertNotNull(userDB.getId());
            assertEquals(name, userDB.getName());
            assertEquals(email, userDB.getEmail());
            assertEquals(age, userDB.getAge());
            assertNotNull(userDB.getCreatedAt());
        }
    }

    @Test
    void testDAORead1() {
        Long id;
        final String name = "Alex";
        final String email = "alex@email.com";
        final int age = 24;

        try(Session session = HibernateSessionManagerTest.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            final User user = new User(name, email, age);

            session.persist(user);

            id = user.getId();

            transaction.commit();
        }

        final Optional<User> optional = this.userDAO.read(id);

        assertNotNull(optional);

        if(optional.isPresent()) {
            final User userDB = optional.get();

            assertNotNull(userDB.getId());
            assertEquals(name, userDB.getName());
            assertEquals(email, userDB.getEmail());
            assertEquals(age, userDB.getAge());
            assertNotNull(userDB.getCreatedAt());
        }
    }

    @Test
    void testDAORead2() {
        assertThrows(Exception.class, () -> this.userDAO.read(19L));
    }

    @Test
    void testDAOUpdate1() {
        Long id;
        final String name = "Alex";
        final String email = "alex@email.com";
        final int age = 24;

        try(Session session = HibernateSessionManagerTest.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            final User user = new User(name, email, age);

            session.persist(user);

            id = user.getId();

            transaction.commit();
        }

        final String updatedName = "Sasha";
        final String updatedEmail = "sasha@email.com";
        final int updatedAge = 25;

        final User updatedUser = new User(id, updatedName, updatedEmail, updatedAge);

        this.userDAO.update(updatedUser);

        try(Session session = HibernateSessionManagerTest.getSessionFactory().openSession()) {
            final User userDB = session.find(User.class, id);

            assertNotNull(userDB.getId());
            assertEquals(updatedName, userDB.getName());
            assertEquals(updatedEmail, userDB.getEmail());
            assertEquals(updatedAge, userDB.getAge());
            assertNotNull(userDB.getCreatedAt());
        }
    }

    @Test
    void testDAOUpdate2() {
        final String name = "Alex";
        final String email = "alex@email.com";
        final int age = 24;

        final String updatedName = "Sasha";
        final String updatedEmail = "sasha@email.com";
        final int updatedAge = 25;

        final User updatedUser = new User(1L, updatedName, updatedEmail, updatedAge);

        assertThrows(Exception.class, () -> this.userDAO.update(updatedUser));
    }

    @Test
    void testDAODelete1() {
        Long id;
        final String name = "Alex";
        final String email = "alex@email.com";
        final int age = 24;

        try(Session session = HibernateSessionManagerTest.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            final User user = new User(name, email, age);

            session.persist(user);

            id = user.getId();

            transaction.commit();
        }

        this.userDAO.delete(id);

        try(Session session = HibernateSessionManagerTest.getSessionFactory().openSession()) {
            final User userDB = session.find(User.class, id);
            assertNull(userDB);
        }
    }

    @Test
    void testDAODelete2() {
        assertThrows(Exception.class, () -> this.userDAO.delete(19L));
    }
}
