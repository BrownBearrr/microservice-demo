package com.example.order_service.client.impl;

import com.example.order_service.client.ProductClient;
import com.example.order_service.dto.BaseResponse;
import com.example.order_service.dto.OrderItemDTO;
import com.example.order_service.dto.ProductDTO;
import com.example.order_service.dto.request.ProductFilter;
import com.example.order_service.entity.BaseEntity;
import com.example.order_service.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {
    private final WebClient.Builder webClientBuilder ;

    @Override
    public List<ProductDTO> getProductsByIds(ProductFilter productFilter) {
        WebClient.Builder builder = WebClient.builder() ;
        BaseResponse<List<ProductDTO>> response = builder.build()
                .post()
                .uri("http://localhost:8888/v1/products/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(productFilter)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponse<List<ProductDTO>>>() {
                })
                .block() ;
        if (response == null || response.getData() == null) {
            throw new ApplicationException("Không lấy đươc thông tin sản phẩm") ;
        }
        return response.getData();
    }

    @Override
    public void lockProducts(List<OrderItemDTO> listOrderItemDTO) {
        WebClient.Builder builder = WebClient.builder() ;
        String response = builder.build()
                .post()
                .uri("http://localhost:8888/v1/products/lock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(listOrderItemDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block() ;
        if (response == null || !response.equals("lock products success")) {
            throw new ApplicationException("Không lock được sản phẩm") ;
        }
    }
}
