package com.teresol.demo.mapper;

import org.mapstruct.Mapper;

import com.teresol.demo.dto.response.CustomerResponse;
import com.teresol.demo.entity.Customer;

@Mapper(componentModel = "spring", uses = LoanMapper.class)
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
}