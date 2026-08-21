package com.lesya.booking1.repository;

import com.lesya.booking1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // EN: Hey Spring Data JPA, create all basic database operations automatically
    // (save, findById, delete, findAll, etc.)
    // UA: Привіт Spring Data JPA, автоматично створи базові операції для БД
    // (save, findById, delete, findAll і т.д.)

    Optional<User> findByEmail(String email);

    // EN: Hey Spring, automatically generate SQL query:
    // SELECT * FROM users WHERE email = ?
    // UA: Привіт Spring, автоматично створи SQL-запит:
    // SELECT * FROM users WHERE email = ?
}
