package com.nikhil.inventory.adaptiveinventoryforecastingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikhil.inventory.adaptiveinventoryforecastingservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	
Optional<User> findByUserName(String username);
  Optional<User> findByEmail(String email);
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
