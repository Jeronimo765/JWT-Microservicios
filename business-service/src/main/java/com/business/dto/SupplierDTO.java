package com.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private Long id;
    
    @NotBlank(message = "Supplier name is required")
    private String name;
    
    private String contactName;
    
    @Email(message = "Email must be valid")
    private String email;
    
    private String phone;
    private String address;
    private String city;
    private String country;
    private Boolean isActive = true;
}