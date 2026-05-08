package com.example.product_service.repository;

import com.example.product_service.entity.Category;
import com.example.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
