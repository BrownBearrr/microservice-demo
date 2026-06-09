package com.example.product_service.mapper;

import com.example.product_service.dto.request.CreateProductReq;
import com.example.product_service.dto.request.UpdateProductReq;
import com.example.product_service.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product fromCreateRequest(CreateProductReq req) ;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateRequest(
            @MappingTarget Product product,
            UpdateProductReq req
    );
}
