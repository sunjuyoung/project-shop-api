package com.project.shop.product.entity;

import com.project.shop.category.entity.Category;
import com.project.shop.global.domain.BaseTime;
import com.project.shop.global.domain.Images;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "product")
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;


    @Column(name = "discount_rate")
    private int discountRate;

    @Column(nullable = false)
    private int quantity;


    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "md_recommended")
    private boolean mdRecommended;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @Builder.Default
    @OneToMany(mappedBy = "product",cascade = {CascadeType.REMOVE})
    private List<Images> productImages = new ArrayList<>();


    public void addImage(Images image) {

      //  image.setOrd(this.imageList.size());
        productImages.add(image);

    }
    public void addImageString(String fileName){

        Images productImage = Images.builder()
                .fileName(fileName)
                .build();
        addImage(productImage);

    }
}
