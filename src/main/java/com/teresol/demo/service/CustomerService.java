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
import com.teresol.demo.entity.Customer;
import com.teresol.demo.repository.CustomerRepository;

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
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .dateOfBirth(LocalDate.of(1990, 5, 14))
                .build(),

            CustomerDTO.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Johnson")
                .email("bob.johnson@example.com")
                .dateOfBirth(LocalDate.of(1985, 11, 22))
                .build(),

            CustomerDTO.builder()
                .id(3L)
                .firstName("Charlie")
                .lastName("Brown")
                .email("charlie.brown@example.com")
                .dateOfBirth(LocalDate.of(1998, 3, 30))
                .build()
        );
    }

    public ResponseEntity<Map<String, Object>> findById(Long id){

        // List<CustomerDTO> customers = new ArrayList<>(getDummyCustomers());

        // Optional<CustomerDTO> customer = customers.stream().
        //     filter(c -> c.getId().equals(id)).
        //     findFirst();
        
        Customer customer = customerRepository.findById(id).orElse(null);

        Map<String, Object> response = new HashMap<>();

        if(customer != null){
            response.put("message", "Customer found");
            response.put("data", customer);
            return ResponseEntity.ok(response);
        }
        else{
            response.put("message", "Customer not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
    }


    public ResponseEntity<List<CustomerDTO>> createCustomer(CustomerDTO requestDTO) {
        
        List<CustomerDTO> customers = new ArrayList<>(getDummyCustomers());
        customers.add(requestDTO);

        return ResponseEntity.ok(customers);
    }

    public ResponseEntity<String> updateCustomer(Long id, CustomerDTO requestDTO) {

        List<CustomerDTO> customers = new ArrayList<>(getDummyCustomers());

        Optional<CustomerDTO> existingCustomerOpt = customers.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst();

        if (existingCustomerOpt.isPresent()) {
            CustomerDTO existingCustomer = existingCustomerOpt.get();
            existingCustomer.setFirstName(requestDTO.getFirstName());
            existingCustomer.setLastName(requestDTO.getLastName());
            existingCustomer.setEmail(requestDTO.getEmail());
            existingCustomer.setDateOfBirth(requestDTO.getDateOfBirth());

            return ResponseEntity.ok("Customer with ID: " + id + " updated successfully");
        } else {
            return ResponseEntity.status(404).body("Customer not found with ID: " + id);
        }   
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
}
