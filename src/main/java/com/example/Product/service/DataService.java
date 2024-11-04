package com.example.Product.service;

import com.example.Product.model.Product;
import com.example.Product.model.SKU;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
            } catch (NullPointerException e) {
                System.err.println("NullPointerException: " + e.getMessage());
            }
        }
    }

    public List<Map<String, Object>> searchProducts(String keyword) {
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            // Создаем запрос на поиск
            SearchRequest searchRequest = new SearchRequest("products");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // Добавляем MultiMatch запрос для поиска по полям продукта и SKU
            BoolQueryBuilder query = QueryBuilders.boolQuery()
                    .should(QueryBuilders.multiMatchQuery(keyword)
                            .field("name")
                            .field("description")
                            .field("skus.skuCode")
                            .field("skus.color")
                            .field("skus.size"));

            sourceBuilder.query(query);
            searchRequest.source(sourceBuilder);

            // Выполняем поиск
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // Обрабатываем результаты
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                results.add(hit.getSourceAsMap());
            }
        } catch (IOException e) {
            System.err.println("IOException при выполнении поиска: " + e.getMessage());
        }

        return results;
    }
}

