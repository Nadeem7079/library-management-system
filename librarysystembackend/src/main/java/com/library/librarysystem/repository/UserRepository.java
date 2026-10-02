package com.library.librarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.library.librarysystem.entity.User;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}