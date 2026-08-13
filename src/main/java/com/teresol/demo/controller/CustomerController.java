package com.teresol.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teresol.demo.dto.CustomerDTO;
import com.teresol.demo.dto.CustomerSummary;
import com.teresol.demo.service.CustomerService;
import com.teresol.demo.util.ApiResponse;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    // 1. Declare the service as a private final field
    private final CustomerService customerService;

    // 2. Create a constructor to inject it
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/summary")
    public ResponseEntity<List<CustomerSummary>> customersSummary() {

        List<CustomerSummary> customers = customerService.getAllCustomersSummary();
        
        return ResponseEntity.ok(customers);
    }    
    
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> customers(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy) {

        List<CustomerDTO> customers = customerService.getAllCustomers(page, size, sortBy);
        
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomersByID(@PathVariable Long id) {

        return customerService.findCustomerById(id);
    }

    @PostMapping("")
    public ResponseEntity<List<CustomerDTO>> createCustomer(@Valid @RequestBody CustomerDTO requestDTO) {

        return customerService.createCustomer(requestDTO);
    
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, 
            @Valid @RequestBody CustomerDTO requestDTO) {

        return customerService.updateCustomer(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCustomer(@PathVariable Long id) {

        return customerService.deleteCustomer(id);
    }
}
