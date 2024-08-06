package com.project.shop.product.repository;

import com.project.shop.product.vo.ProductSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void searchProductListPage() {
        // given
        ProductSearchCondition condition = new ProductSearchCondition();
        condition.setKeyword("test");
        condition.setPriceGoe(1000);
        condition.setPriceLoe(2000);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        productRepository.searchProductListPage(condition, pageable);

        // then
    }

    @Test
    void searchProductList() {
        // given
        ProductSearchCondition condition = new ProductSearchCondition();
        condition.setKeyword("test");
        condition.setPriceGoe(1000);

        condition.setPriceLoe(2000);

        Pageable pageable = PageRequest.of(0, 10);

        // when

        // then
    }

}