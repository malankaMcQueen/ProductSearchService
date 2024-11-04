package com.example.Product.controller;

import com.example.Product.model.Product;
import com.example.Product.service.DataService;
import com.example.Product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final DataService dataService;
    private final ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProduct();
    }

    @PostMapping("/load")
    public ResponseEntity<String> loadData() {
        List<Product> products = productService.getAllProduct();
        dataService.loadData(products);

        return ResponseEntity.ok("Data loaded successfully.");
    }

    @GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }
}

