# Product Search Service

Проект для поиска продуктов с использованием PostgreSQL и Elasticsearch. Приложение на Spring Boot интегрируется с базой данных и позволяет выполнять поиск по атрибутам продуктов через API.

## Запуск проекта

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/malankaMcQueen/ProductSearchService
   cd ProductSearchService
2. Запустите необходимые службы (PostgreSQL и Elasticsearch) с помощью Docker Compose:
    ```bash
    docker-compose up -d
3. Соберите и запустите Spring Boot приложение:
    ```bash
    mvn clean package -DskipTests
    java -jar target/app.jar
Spring Boot приложение будет запущено на порту 8080.

## Использование API

API предоставляет доступ к следующим эндпоинтам:
Получение списка всех продуктов
    
    URL: /api/products
    Метод: GET
    Описание: Возвращает список всех продуктов, хранящихся в базе данных.

Пример запроса:

    curl -X GET http://localhost:8080/api/products

Загрузка данных из базы данных в Elasticsearch

    URL: /api/products/load
    Метод: POST
    Описание: Загружает данные всех продуктов из PostgreSQL в индекс Elasticsearch для последующего поиска.

Пример запроса:

    curl -X POST http://localhost:8080/api/products/load

Поиск продуктов по ключевому слову

    URL: /api/products/search
    Метод: GET
    Параметры: keyword (строка) - ключевое слово для поиска.
    Описание: Выполняет поиск по атрибутам продуктов в индексе Elasticsearch.

Пример запроса:

    curl -X GET "http://localhost:8080/api/products/search?keyword=example"

## Завершение работы

Чтобы остановить контейнеры PostgreSQL и Elasticsearch, выполните:
    
    docker-compose down