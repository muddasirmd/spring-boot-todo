package com.teresol.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.teresol.demo.dto.LoanDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private Long organizationId;

    @Column(unique = true)
    private String name;      
    
    @Column(unique = true)
    private String email;    
    
    // @Column(columnDefinition = "jsonb")
    private Integer age;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

}