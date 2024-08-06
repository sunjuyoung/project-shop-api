package com.project.shop.customer.controller;

import com.project.shop.customer.dto.response.CustomerResponse;
import com.project.shop.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getProfile(@PathVariable("id")Long id){
        CustomerResponse profile = customerService.getProfile(id);
        return ResponseEntity.ok(profile);
    }
}
