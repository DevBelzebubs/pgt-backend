package com.portable.microservices.ms_inventory.product.presentation.mapper;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import com.portable.microservices.ms_inventory.product.presentation.dto.BrandResponse;
import org.springframework.stereotype.Component;

@Component
public class BrandPresentationMapper {

    public BrandResponse toResponse(Brand brand) {
        return new BrandResponse(brand.id(), brand.brandName());
    }
}
