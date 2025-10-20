package edu.aston.userservice.service;

import edu.aston.userservice.dao.UserDAO;
import edu.aston.userservice.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private User testResult;

    @BeforeEach
    void setup() {
        final String name = "Alex";
        final String email = "alex@email.com";
        final int age = 24;

        this.testResult = new User(name, email, age);
    }

    @Test
    void testServiceCreate() {
        when(this.userDAO.create(any(User.class))).thenReturn(testResult);

        this.userService.create("Alex", "alex@email.com", 24);

        ArgumentCaptor<User> capturedUser = ArgumentCaptor.forClass(User.class);
        verify(this.userDAO, times(1)).create(capturedUser.capture());

        final User userDB = capturedUser.getValue();

        assertNotNull(userDB);
        assertEquals("Alex", userDB.getName());
        assertEquals("alex@email.com", userDB.getEmail());
        assertEquals(24, userDB.getAge());
        assertNotNull(userDB.getCreatedAt());
    }

    @Test
    void testServiceRead() {
        when(this.userDAO.read(1L)).thenReturn(Optional.of(testResult));

        Optional<User> optional = this.userService.read(1L);

        assertNotNull(optional);

        if(optional.isPresent()) {
            final User userDB = optional.get();

            verify(this.userDAO, times(1)).read(1L);

            assertEquals("Alex", userDB.getName());
            assertEquals("alex@email.com", userDB.getEmail());
            assertEquals(24, userDB.getAge());
            assertNotNull(userDB.getCreatedAt());
        }
    }

    @Test
    void testServiceUpdate() {
        this.userService.update(1L, "Sasha", "sasga@email.com", 24);

        verify(this.userDAO, times(1)).update(any(User.class));
    }

    @Test
    void testServiceDelete() {
        this.userService.delete(1L);

        verify(this.userDAO, times(1)).delete(1L);
    }
}
