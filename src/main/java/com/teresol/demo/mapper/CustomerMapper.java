package com.teresol.demo.mapper;

import org.mapstruct.Mapper;

import com.teresol.demo.dto.request.CustomerRequest;
import com.teresol.demo.entity.Customer;

@Mapper(componentModel = "spring", uses = LoanMapper.class)
public interface CustomerMapper {
    CustomerRequest toResponse(Customer customer);
}