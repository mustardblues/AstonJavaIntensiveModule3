package edu.aston.userservice.app;

import edu.aston.userservice.dao.UserManager;
import edu.aston.userservice.models.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleInterface {
    private interface Method {
        void method();
    }

    private String[] userInput;

    private final List<String> consoleCommands = List.of("create", "read", "update", "delete", "exit");

    final Method[] consoleMethods = {
            this::create,
            this::read,
            this::update,
            this::delete
    };

    private final UserManager userManager;

    public ConsoleInterface() {
        this.userManager = new UserManager();
    }

    public void start() {
        final Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("-> ");

            this.userInput = scanner.nextLine().split(" ");

            if("exit".equals(this.userInput[0])) {
                break;
            }

            final int index = this.consoleCommands.indexOf(this.userInput[0]);

            if(index >= 0 && index < this.consoleMethods.length) {
                consoleMethods[index].method();
            }
        }
    }

    private void create() {
        if(this.userInput.length != 4) {
            return;
        }

        final String name = this.userInput[1];
        final String email = this.userInput[2];
        final int age = Integer.parseInt(this.userInput[3]);

        final User user = new User(name, email, age);

        userManager.create(user);
    }

    private void read() {
        if(this.userInput.length != 2) {
            return;
        }

        final Long id = Long.parseLong(this.userInput[1]);

        Optional<User> optional = userManager.read(id);

        if(optional.isPresent()) {
            final User user = optional.get();

            final String name = user.getName();
            final String email = user.getEmail();
            final int age = user.getAge();

            final String format = String.format("name: %s\nemail: %s\nage: %d", name, email, age);

            System.out.println(format);
        }
    }

    private void update() {
        if(this.userInput.length != 4) {
            return;
        }

        final String name = this.userInput[1];
        final String email = this.userInput[2];
        final int age = Integer.parseInt(this.userInput[3]);

        final User user = new User(name, email, age);

        userManager.update(user);
    }

    private void delete() {
        if(this.userInput.length != 2) {
            return;
        }

        final Long id = Long.parseLong(this.userInput[1]);

        userManager.delete(id);
    }
}
