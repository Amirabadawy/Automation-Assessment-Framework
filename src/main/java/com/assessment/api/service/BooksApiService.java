package com.assessment.api.service;

import com.assessment.api.client.ApiClient;
import com.assessment.api.model.Book;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BooksApiService {
    private static final String BOOK_BY_ID_PATH = "/{id}";
    private static final String BOOK_ID_PARAM = "id";

    public Response getBooks() {
        return given()
                .spec(ApiClient.booksRequestSpec())
                .when()
                .get();
    }

    public Response createBook(Book book) {
        return given()
                .spec(ApiClient.booksRequestSpec())
                .body(book)
                .when()
                .post();
    }

    public Response getBookById(int bookId) {
        return given()
                .spec(ApiClient.booksRequestSpec())
                .pathParam(BOOK_ID_PARAM, bookId)
                .when()
                .get(BOOK_BY_ID_PATH);
    }
}
