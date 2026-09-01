package com.assessment.api.client;

import com.assessment.utils.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class ApiClient {
    private ApiClient() {
    }

    public static RequestSpecification booksRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("apiBaseUrl"))
                .setBasePath(ConfigReader.get("booksPath"))
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build();
    }
}
