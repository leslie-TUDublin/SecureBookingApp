package com.lesya.booking1.repository;

import com.lesya.booking1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    //Automatically generate SQL query to ідусе user by email
    Optional<User> findByEmail(String email);
}
