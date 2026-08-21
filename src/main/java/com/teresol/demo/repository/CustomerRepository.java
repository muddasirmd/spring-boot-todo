package com.teresol.demo.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teresol.demo.dto.response.CustomerSummary;
import com.teresol.demo.entity.Customer;

import jakarta.persistence.LockModeType;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByEmail(String email);

    List<Customer> findByAgeGreaterThan(Integer age);

    Boolean existsByEmail(String email);

    // Todo: implement filtering EP
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"loans"})
    List<Customer> findByAgeLessThan(Integer age);
    
    @Query("""
            Select c
            From Customer c
            where c.age > :age
            """)
    List<Customer> findAdults(@Param("age") Integer age);

    @Query("""
            SELECT new com.teresol.demo.dto.response.CustomerSummary(
                c.id,
                c.name
            )
            FROM Customer c
            """)
    List<CustomerSummary> findSummary();


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT c
            FROM Customer c
            WHERE c.id = :id
            """)
    Optional<Customer> findForUpdate(@Param("id") Long id);

    

}