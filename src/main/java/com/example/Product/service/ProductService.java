package com.example.Product.service;

import com.example.Product.model.Product;
import com.example.Product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

}
