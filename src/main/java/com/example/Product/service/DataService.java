package com.example.Product.service;

import com.example.Product.model.Product;
import com.example.Product.model.SKU;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataService {

    private final RestHighLevelClient client;

    @Autowired
    public DataService(RestHighLevelClient client) {
        this.client = client;
    }

    public void loadData(List<Product> products) {
        for (Product product : products) {
            // Индексируем продукт
            try {
                // Собираем SKU для данного продукта
                List<SKU> skus = product.getSkus();

                // Создаем JSON-документ для индексации
                Map<String, Object> document = new HashMap<>();
                document.put("name", product.getName());
                document.put("description", product.getDescription());
                document.put("active", product.isActive());
                document.put("startDate", product.getStartDate());
                document.put("skus", skus.stream()
                        .map(sku -> Map.of(
                                "skuCode", sku.getSkuCode(),
                                "price", sku.getPrice(),
                                "color", sku.getColor(),
                                "size", sku.getSize()
                        )).collect(Collectors.toList()));

                IndexRequest request = new IndexRequest("products")
                        .id(String.valueOf(product.getId()))
                        .source(document);

                IndexResponse response = client.index(request, RequestOptions.DEFAULT);
                System.out.println("Response: " + response);
                System.out.println("Indexed product: " + response.getId());
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
//                e.printStackTrace();
            } catch (NullPointerException e) {
                System.err.println("NullPointerException: " + e.getMessage());
//                e.printStackTrace();
            }

//                e.printStackTrace();
        }
    }
}

