package com.project.shop.customer.service;

import com.project.shop.customer.dto.response.CustomerResponse;
import com.project.shop.customer.entity.Customer;
import com.project.shop.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse getProfile(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("해당유저를 찾을 수 없습니다"));


        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.entityToDTO(customer);

        return customerResponse;

    }
}
