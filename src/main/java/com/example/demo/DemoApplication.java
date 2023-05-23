package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.EnumSet;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    public DemoApplication(UserRepository userRepository, PlatformTransactionManager transactionManager) {
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        createUsers();
        deleteUsers();
    }

    private void deleteUsers() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute((status) -> {
            userRepository.deleteAll();
            return null;
        });
    }

    private void createUsers() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute((status) -> {
            User a = new User();
            a.setUserRoles(EnumSet.of(UserRole.USER));
            a = userRepository.save(a);

            User user = new User();
            user.setUserRoles(EnumSet.of(UserRole.USER));
            user.setCreatedBy(a);
            user = userRepository.save(user);
            System.out.println(user.getCreatedBy().getId());
            return null;
        });
    }
}
