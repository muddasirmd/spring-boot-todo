package com.teresol.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teresol.demo.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByEmail(String email);

    List<Customer> findByAgeGreaterThan(Integer age);

    List<Customer> findByAgeLessThan(Integer age);
    
    @Query("""
            Select c
            From Customer c
            where c.age > :age
            """)
    List<Customer> findAdults(@Param("age") Integer age);

}