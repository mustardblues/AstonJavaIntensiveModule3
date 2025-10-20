package edu.aston.userservice.service;

import edu.aston.userservice.dao.UserDAO;
import edu.aston.userservice.models.User;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;

    public UserService(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User create(final String name, final String email, final int age) {
        final User user = new User(name, email, age);

        this.userDAO.create(user);

        return user;
    }

    public Optional<User> read(final Long id) {
        return userDAO.read(id);
    }

    public void update(final Long id, final String name, final String email, final int age) {
        userDAO.update(new User(id, name, email, age));
    }

    public void delete(final Long id) {
        userDAO.delete(id);
    }
}
