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

    public List<ProductDto> searchProducts(String keyword) {
        List<ProductDto> products = new ArrayList<>();

        try {
            // Создаем запрос для поиска по индексу "products_index"
            SearchRequest searchRequest = new SearchRequest("products_index");

            // Указываем параметры поиска, включая поля для поиска
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "name", "description"));
            searchRequest.source(searchSourceBuilder);

            // Выполняем запрос к Elasticsearch
            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

            // Обрабатываем результаты поиска
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                // Преобразуем каждый результат в ProductDto
                ProductDto product = convertToProductDto(hit);
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

}
