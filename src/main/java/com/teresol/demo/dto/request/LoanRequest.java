package com.teresol.demo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    private Long id;

    @NotNull
    private BigDecimal amount;
    
    private BigDecimal interestRate;
    
    @NotNull
    private String status;

}