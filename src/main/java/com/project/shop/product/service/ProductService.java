package com.project.shop.product.service;

import com.project.shop.category.entity.Category;
import com.project.shop.category.repository.CategoryRepository;
import com.project.shop.global.exception.NotFoundCategory;
import com.project.shop.global.exception.enums.ExceptionCode;
import com.project.shop.product.dto.request.ProductCreateDTO;
import com.project.shop.product.dto.response.ProductListDTO;
import com.project.shop.product.dto.response.ProductViewDTO;
import com.project.shop.product.entity.Product;
import com.project.shop.product.repository.ProductRepository;
import com.project.shop.product.vo.ProductSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductViewCountService productViewCountService;

    private final CategoryRepository categoryRepository;

    public Page<ProductListDTO> searchProductListPage(ProductSearchCondition condition, Pageable pageable) {
        return productRepository.searchProductListPage(condition, pageable);
    }

    public ProductViewDTO getProductView(Long productId) {
        ProductViewDTO productView = productRepository.getProductView(productId);
        if(productView != null){
            productViewCountService.incrementViewCountAsync(productId);
        }
        return productView;
    }



    @Scheduled(fixedRate = 600000) //10분마다
    @Transactional
    public void updateViewCounts() {
        Map<Long, Long> viewCounts = productViewCountService.getAndResetViewCounts();
        if(viewCounts.isEmpty()){
            return;
        }
        for (Map.Entry<Long, Long> entry : viewCounts.entrySet()) {
            Long itemId = entry.getKey();
            Long count = entry.getValue();
            productRepository.incrementViewCountBatch(itemId, count);
        }
    }


    @Transactional
    public Long createProduct(ProductCreateDTO dto) {

        Product product = dtoToEntity(dto);
        Product save = productRepository.save(product);
        return save.getId();
    }

    private Product dtoToEntity(ProductCreateDTO productDTO){

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() ->  new NotFoundCategory(ExceptionCode.NOT_FOUND_CATEGORY));



        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .category(category)
                .build();

        //업로드 처리가 끝난 파일들의 이름 리스트
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames == null){
            return product;
        }

        uploadFileNames.stream().forEach(uploadName -> {

            product.addImageString(uploadName);
        });

        return product;
    }
}
