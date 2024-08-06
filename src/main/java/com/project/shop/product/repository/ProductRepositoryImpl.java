package com.project.shop.product.repository;

import com.project.shop.category.entity.QCategory;
import com.project.shop.global.domain.QImages;
import com.project.shop.global.exception.ProductNotFoundException;
import com.project.shop.global.exception.enums.ExceptionCode;
import com.project.shop.product.dto.response.*;
import com.project.shop.product.entity.Product;
import com.project.shop.product.entity.QProduct;
import com.project.shop.product.vo.ProductSearchCondition;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.shop.category.entity.QCategory.*;
import static com.project.shop.global.domain.QImages.*;
import static com.project.shop.product.entity.QProduct.*;

public class ProductRepositoryImpl implements CustomProductRepository{

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ProductListDTO> searchProductListPage(ProductSearchCondition condition, Pageable pageable) {

        List<Product> result = getProductListDTOJPAQuery(condition, pageable);

        //orderByPrice(condition, result);
        List<ProductListDTO> productListDTOList = result.stream().map(ProductListDTO::new)
                .collect(Collectors.toList());

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(nameEq(condition.getKeyword()))
                ;

        return PageableExecutionUtils.getPage(productListDTOList, pageable, countQuery::fetchOne);

       // return null;
    }

    @Override
    public ProductViewDTO getProductView(Long productId) {

        ProductViewDTO productViewDTO = queryFactory.select(new QProductViewDTO(
                        product.id,
                        product.name,
                        product.description,
                        product.price,
                        product.quantity,
                        product.viewCount,
                category.id,
                category.name
                ))
                .from(product)
                .join(product.category, category)
                .where(product.id.eq(productId))
                .fetchOne();
        if(productViewDTO == null){
            throw new ProductNotFoundException(ExceptionCode.NOT_FOUND_PRODUCT);
        }

        List<ProductImagesDTO> imagesDTOS = queryFactory.select(new QProductImagesDTO(
                        images.id,
                        images.fileName
                ))
                .from(product)
                .leftJoin(product.productImages, images)
                .where(product.id.eq(productId))
                .fetch();
        productViewDTO.setProductImages(imagesDTOS);


        return productViewDTO;
    }


    private  List<Product> getProductListDTOJPAQuery(ProductSearchCondition condition, Pageable pageable) {
        List<Product> fetch = queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.productImages, images).fetchJoin()
                .distinct()
//                .where(nameEq(condition.getKeyword()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return fetch;
       // return null;
    }


    @Override
    public List<ProductListDTO> productListMD(ProductSearchCondition condition ) {


        JPAQuery<ProductListDTO> query = getProductListDTOJPAQuery();


        orderByPrice(condition, query);

        return query.fetch();
    }

    @Override
    public List<ProductListDTO> productListBrandNew(ProductSearchCondition condition) {
        JPAQuery<ProductListDTO> query = getProductListDTOJPAQuery();
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        query.where(product.createdAt.after(twoWeeksAgo));
        return null;
    }

    @Override
    public List<ProductListDTO> productListPopular(ProductSearchCondition condition) {
        JPAQuery<ProductListDTO> query = getProductListDTOJPAQuery();

        return null;
    }


    private JPAQuery<ProductListDTO> getProductListDTOJPAQuery() {
//        JPAQuery<ProductListDTO> query = queryFactory
//                .select(new QProductListDTO(
//                        product.id.as("productId"),
//                        product.name,
//                        product.price,
//                        product.discountRate,
//                        product.description
//                ))
//                .from(product);
//        return query;
        return  null;
    }

    private static void orderByPrice(ProductSearchCondition condition, JPAQuery<ProductListDTO> query) {
        if (StringUtils.hasText(condition.getOrderBy()) && StringUtils.hasText(condition.getDirection())) {
            if ("asc".equalsIgnoreCase(condition.getDirection())) {
                query.orderBy(new OrderSpecifier<>(Order.ASC, product.price));
            } else if ("desc".equalsIgnoreCase(condition.getDirection())) {
                query.orderBy(new OrderSpecifier<>(Order.DESC, product.price));
            }
        }
    }


    private BooleanExpression nameEq(String keyword) {
        return StringUtils.hasText(keyword) ? product.name.contains(keyword) : null;
    }
    private BooleanExpression priceGoe(Integer priceGoe) {
        return priceGoe != null ? product.price.goe(priceGoe) : null;
    }

}
