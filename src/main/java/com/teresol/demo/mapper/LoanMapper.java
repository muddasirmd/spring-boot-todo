package com.teresol.demo.mapper;

import org.mapstruct.Mapper;

import com.teresol.demo.dto.request.LoanRequest;
import com.teresol.demo.entity.Loan;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanRequest toResponse(Loan loan);
}