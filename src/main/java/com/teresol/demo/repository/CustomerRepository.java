package com.teresol.demo.repository;

import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teresol.demo.dto.CustomerSummary;
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

    @Query("""
            SELECT new com.teresol.demo.dto.CustomerSummary(
                c.id,
                c.name
            )
            FROM Customer c
            """)
    List<CustomerSummary> findSummary();

    // Page<Customer> findAll(Pageable pageable);

}