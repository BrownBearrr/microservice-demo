package com.example.product_service.controller;

import com.example.product_service.dto.BaseResponse;
import com.example.product_service.dto.request.CreateProductReq;
import com.example.product_service.dto.request.ProductFilterReq;
import com.example.product_service.dto.request.ProductLockReq;
import com.example.product_service.dto.request.UpdateProductReq;
import com.example.product_service.entity.Product;
import com.example.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService ;

    @PostMapping
    public ResponseEntity<BaseResponse<Product>> create(@RequestBody @Valid CreateProductReq createProductReq) {
        return ResponseEntity.ok(new BaseResponse<>(productService.create(createProductReq) , "create product succeess")) ;
    }

    @PostMapping("/search")
    public ResponseEntity<BaseResponse<List<Product>>> search(@RequestBody ProductFilterReq productFilter) {
        List<Product> products = productService.search(productFilter);
        return ResponseEntity.ok(new BaseResponse<>(products, "Search products success"));
    }

    @PostMapping("/lock")
    public String lockProducts(@RequestBody List<ProductLockReq> productLockReq) {
        return productService.lockProducts(productLockReq) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Product>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new BaseResponse<>(productService.getById(id) , "get product by id succeess")) ;
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<BaseResponse<Product>> update(@RequestBody @Valid UpdateProductReq updateProductReq , @PathVariable String id) {
        return ResponseEntity.ok(new BaseResponse<>(productService.update(id, updateProductReq), "update product succeess"));
    }

}









