package com.teresol.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teresol.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String name);
}
