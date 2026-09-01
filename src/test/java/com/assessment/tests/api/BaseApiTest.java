package com.assessment.tests.api;

import com.assessment.api.service.BooksApiService;
import org.testng.annotations.BeforeMethod;

public abstract class BaseApiTest {
    protected BooksApiService booksApi;

    @BeforeMethod(alwaysRun = true)
    public void setUpApiService() {
        booksApi = new BooksApiService();
    }
}
