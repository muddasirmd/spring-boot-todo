package com.teresol.demo.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDate dateOfBirth;
    private List<LoanResponse> loans;
}