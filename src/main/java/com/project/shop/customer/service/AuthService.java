package com.project.shop.customer.service;

import com.project.shop.customer.dto.request.SignUpDTO;
import com.project.shop.customer.entity.Customer;
import com.project.shop.customer.entity.enums.Grade;
import com.project.shop.customer.entity.enums.Roles;
import com.project.shop.customer.repository.CustomerRepository;
import com.project.shop.global.exception.AlreadyExistEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    public Long signUp(SignUpDTO signUpDTO) {

        if(customerRepository.existsByEmail(signUpDTO.getEmail())){
            throw new AlreadyExistEmailException("Already Exist Email");
        }

        Customer customer = Customer.builder()
                .email(signUpDTO.getEmail())
                .grade(Grade.BRONZE)
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .nickname(signUpDTO.getNickname())
                .build();
        customer.addRoles(Roles.CUSTOMER);
        Customer saveCustomer = customerRepository.save(customer);
        return saveCustomer.getId();
    }
}
