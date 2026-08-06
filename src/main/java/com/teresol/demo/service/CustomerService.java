package com.teresol.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.teresol.demo.dto.CustomerDTO;
import com.teresol.demo.dto.LoanDTO;
import com.teresol.demo.entity.Customer;
import com.teresol.demo.entity.Loan;
import com.teresol.demo.repository.CustomerRepository;
import com.teresol.demo.util.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    
    public List<CustomerDTO> getDummyCustomers() {
        return Arrays.asList(
            CustomerDTO.builder()
                .id(1L)
                .name("Alice")
                .age(20)
                .email("alice.smith@example.com")
                .dateOfBirth(LocalDate.of(1990, 5, 14))
                .build(),

            CustomerDTO.builder()
                .id(2L)
                .name("Bob")
                .age(25)
                .email("bob.johnson@example.com")
                .dateOfBirth(LocalDate.of(1985, 11, 22))
                .build(),

            CustomerDTO.builder()
                .id(3L)
                .name("Charlie")
                .age(29)
                .email("charlie.brown@example.com")
                .dateOfBirth(LocalDate.of(1998, 3, 30))
                .build()
        );
    }

    public List<CustomerDTO> getAllCustomers() {

        // List<Customer> customers = customerRepository.findAll();
        // List<Customer> customers = customerRepository.findByAgeLessThan(20);
        
        List<Customer> customers = customerRepository.findAdults(20);
        
        List<CustomerDTO> customerDTOs = customers.stream()
            .map(customer -> CustomerDTO.builder()
                .id(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .age(customer.getAge())
                .build())
            .toList();

        return customerDTOs;
    }

    public ResponseEntity<ApiResponse<CustomerDTO>> findById(Long id){


        Customer customer = customerRepository.findById(id).orElse(null);
        // Customer customer = customerRepository.getReferenceById(id);

        // Customer customer = customerRepository.findByEmail(email).orElse(null);
        

        if(customer != null){
            List<LoanDTO> loanResponses = customer.getLoans()
                .stream()
                .map(this::mapLoan)
                .toList();
                
            CustomerDTO customerDTO = CustomerDTO.builder()
                .id(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .age(customer.getAge())
                .loans(loanResponses)
                .build();
            
            return ResponseEntity.ok(ApiResponse.success(customerDTO, "Customer found"));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Customer not found with ID: " + id));
        }
        
    }


    public ResponseEntity<List<CustomerDTO>> createCustomer(CustomerDTO requestDTO) {
        
        List<CustomerDTO> customers = new ArrayList<>(getDummyCustomers());
        customers.add(requestDTO);

        return ResponseEntity.ok(customers);
    }

    @Transactional
    public ResponseEntity<String> updateCustomer(Long id, CustomerDTO requestDTO) {

        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setName(requestDTO.getName());

            return ResponseEntity.ok("Customer with ID: " + id + " updated successfully");
        // } else {
        //     return ResponseEntity.status(404).body("Customer not found with ID: " + id);
        // }   
    }

    
    public ResponseEntity<Map<String, Object>> deleteCustomer(Long id) {
        
        // 1. Create a mutable list from dummy data
        List<CustomerDTO> customers = new ArrayList<>(getDummyCustomers());

        // 2. Remove the record using removeIf() instead of an unassigned stream filter
        boolean removed = customers.removeIf(c -> c.getId().equals(id));

        if (!removed) {
            return ResponseEntity.status(404).body(Map.of("message", "Customer not found with ID: " + id));
        }

        // 3. Build a response containing both a String message and the updated List
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Record Deleted Successfully");
        response.put("data", customers);

        return ResponseEntity.ok(response);
    }

    private LoanDTO mapLoan(Loan loan) {

        LoanDTO dto = new LoanDTO();

        dto.setId(loan.getLoanId());
        dto.setAmount(loan.getAmount());
        dto.setInterestRate(loan.getInterestRate());

        return dto;
    }
}
