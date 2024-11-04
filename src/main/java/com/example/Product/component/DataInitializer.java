package com.example.Product.component;

import com.github.javafaker.Faker;
import com.example.Product.model.Product;
import com.example.Product.model.SKU;
import com.example.Product.repository.ProductRepository;
import com.example.Product.repository.SkuRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;

    public DataInitializer(ProductRepository productRepository, SkuRepository skuRepository) {
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Faker faker = new Faker();
        Random random = new Random();

        List<Product> products = new ArrayList<>();

        // Генерация данных для продуктов
        for (int i = 1; i <= 20; i++) {
            Product product = new Product();
            product.setName("Product" + i);
            product.setDescription(faker.lorem().sentence());
            product.setActive(random.nextBoolean());
            product.setStartDate(LocalDate.now().minusDays(random.nextInt(365)));

            products.add(product);
        }

        productRepository.saveAll(products);

        // Генерация данных для SKU и присвоение их продуктам
        List<SKU> skus = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            SKU sku = new SKU();
            sku.setSkuCode("SKU" + i);
            sku.setPrice(BigDecimal.valueOf(5 + (50 - 5) * random.nextDouble()));
            sku.setColor(faker.color().name());
            sku.setSize(faker.options().option("S", "M", "L", "XL"));
            sku.setProduct(products.get(random.nextInt(products.size()))); // Случайный продукт для SKU

            skus.add(sku);
        }

        skuRepository.saveAll(skus);

        System.out.println("Data initialized in the database.");
    }
}

