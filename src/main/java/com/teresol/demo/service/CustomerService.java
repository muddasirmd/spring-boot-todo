package com.teresol.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teresol.demo.dto.request.CustomerRequest;
import com.teresol.demo.dto.response.CustomerResponse;
import com.teresol.demo.dto.response.CustomerSummary;
import com.teresol.demo.dto.response.LoanResponse;
import com.teresol.demo.entity.Customer;
import com.teresol.demo.entity.Loan;
import com.teresol.demo.exception.DuplicateEmailException;
import com.teresol.demo.mapper.CustomerMapper;
import com.teresol.demo.repository.CustomerRepository;
import com.teresol.demo.util.ApiResponse;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // @Autowired
    // private CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        // this.customerMapper = customerMapper;
    }

    
    public List<CustomerResponse> getDummyCustomers() {
        return Arrays.asList(
            CustomerResponse.builder()
                .id(1L)
                .name("Alice")
                .age(20)
                .email("alice.smith@example.com")
                .dateOfBirth(LocalDate.of(1990, 5, 14))
                .build(),

            CustomerResponse.builder()
                .id(2L)
                .name("Bob")
                .age(25)
                .email("bob.johnson@example.com")
                .dateOfBirth(LocalDate.of(1985, 11, 22))
                .build(),

            CustomerResponse.builder()
                .id(3L)
                .name("Charlie")
                .age(29)
                .email("charlie.brown@example.com")
                .dateOfBirth(LocalDate.of(1998, 3, 30))
                .build()
        );
    }

    public List<CustomerSummary> getAllCustomersSummary() {

        List<CustomerSummary> customers = customerRepository.findSummary();

        return customers;
    }    
    
    public List<CustomerResponse> getAllCustomers(int page, int size, String sortBy) {

        // List<Customer> customers = customerRepository.findAll();
        // List<Customer> customers = customerRepository.findByAgeLessThan(30);        
        // List<Customer> customers = customerRepository.findAdults(20);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Customer> customers = customerRepository.findAll(pageable);
        
        
        List<CustomerResponse> customerDTOs = customers.stream()
            .map(customer -> CustomerResponse.builder()
                .id(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .age(customer.getAge())
                .loans(toList(customer))
                .build())
            .toList();

        // return customerRepository.findAll(pageable).map(customerMapper::toResponse);

        return customerDTOs;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<CustomerResponse>> findCustomerById(Long id){


        Customer customer = customerRepository.findById(id).orElse(null);
        // Customer customer = customerRepository.getReferenceById(id);

        // Customer customer = customerRepository.findByEmail(email).orElse(null);

        if(customer != null){
                
            CustomerResponse customerDTO = CustomerResponse.builder()
                .id(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .age(customer.getAge())
                .loans(toList(customer))
                .build();
            
            return ResponseEntity.ok(ApiResponse.success(customerDTO, "Customer found"));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Customer not found with ID: " + id));
        }
        
    }

    // public Page<CustomerResponse> search(String name, Pageable pageable) {

    //     return customerRepository.findByNameContainingIgnoreCase(name, pageable)
    //             .map(customerMapper::toResponse);
    // }


    @Transactional
    public String createCustomer(CustomerRequest requestDTO) {
        
        // List<CustomerDTO> customers = new ArrayList<>(getDummyCustomers());
        // customers.add(requestDTO);

        // return ResponseEntity.ok(customers);

         if (customerRepository.existsByEmail(requestDTO.getEmail())) {

            throw new DuplicateEmailException(requestDTO.getEmail());
        }

        // 1. Convert DTO to Entity
        Customer customer = new Customer();
        customer.setName(requestDTO.getName());
        customer.setAge(requestDTO.getAge());
        customer.setEmail(requestDTO.getEmail());

        customer = customerRepository.save(customer);
       
        return "Customer saved successfully";
        
    }

    @Transactional
    public String updateCustomer(Long id, CustomerRequest requestDTO) {

        Customer customer = customerRepository.findForUpdate(id).orElseThrow();
        customer.setName(requestDTO.getName());

            return "Customer with ID: " + id + " updated successfully";
        // } else {
        //     return ResponseEntity.status(404).body("Customer not found with ID: " + id);
        // }   
    }

    
    public ResponseEntity<Map<String, Object>> deleteCustomer(Long id) {
        
        // 1. Create a mutable list from dummy data
        List<CustomerResponse> customers = new ArrayList<>(getDummyCustomers());

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

    private LoanResponse mapLoan(Loan loan) {

        LoanResponse dto = new LoanResponse();

        dto.setId(loan.getLoanId());
        dto.setAmount(loan.getAmount());
        dto.setInterestRate(loan.getInterestRate());

        return dto;
    }

    public List<LoanResponse> toList(Customer customer){

        List<LoanResponse> loanResponses = customer.getLoans()
        .stream()
        .map(this::mapLoan)
        .toList();

        return loanResponses;
    }

}
