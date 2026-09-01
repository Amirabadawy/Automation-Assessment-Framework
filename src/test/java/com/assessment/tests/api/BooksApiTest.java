package com.assessment.tests.api;

import com.assessment.api.model.Book;
import com.assessment.api.model.ProblemDetails;
import com.assessment.utils.TestDataReader;
import io.restassured.common.mapper.TypeRef;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BooksApiTest extends BaseApiTest {
    private static final int OK = 200;
    private static final int NOT_FOUND = 404;

    @DataProvider(name = "newBooks")
    public Object[][] newBooks() {
        return new Object[][]{
                {
                        new Book(
                                TestDataReader.getInt("book.created.id"),
                                TestDataReader.get("book.created.title"),
                                TestDataReader.get("book.created.description"),
                                TestDataReader.getInt("book.created.page.count"),
                                TestDataReader.get("book.created.excerpt"),
                                TestDataReader.get("book.created.publish.date"))
                }
        };
    }

    @Test(groups = {"api", "smoke"})
    public void shouldReturnBooksCollectionAndExistingBookDetails() {
        int existingBookId = TestDataReader.getInt("book.existing.id");

        List<Book> books = booksApi.getBooks()
                .then()
                .log().ifValidationFails()
                .statusCode(OK)
                .extract()
                .as(new TypeRef<List<Book>>() {
                });

        assertFalse(books.isEmpty(), "Books collection should not be empty.");
        assertTrue(
                books.stream().anyMatch(book -> book.getId() == existingBookId),
                "Books collection should contain the configured existing book id.");

        Book book = booksApi.getBookById(existingBookId)
                .then()
                .log().ifValidationFails()
                .statusCode(OK)
                .extract()
                .as(Book.class);

        assertEquals(book.getId(), existingBookId);
        assertNotNull(book.getTitle(), "Book title should be returned.");
        assertTrue(book.getPageCount() > 0, "Book page count should be positive.");
    }

    @Test(groups = {"api", "regression"}, dataProvider = "newBooks")
    public void shouldCreateBookAndReturnSubmittedPayload(Book expectedBook) {
        Book actualBook = booksApi.createBook(expectedBook)
                .then()
                .log().ifValidationFails()
                .statusCode(OK)
                .extract()
                .as(Book.class);

        assertEquals(actualBook.getId(), expectedBook.getId());
        assertEquals(actualBook.getTitle(), expectedBook.getTitle());
        assertEquals(actualBook.getDescription(), expectedBook.getDescription());
        assertEquals(actualBook.getPageCount(), expectedBook.getPageCount());
        assertEquals(actualBook.getExcerpt(), expectedBook.getExcerpt());
        assertTrue(
                actualBook.getPublishDate().startsWith(expectedBook.getPublishDate().substring(0, 19)),
                "API should return the submitted publish date value.");
    }

    @Test(groups = {"api", "negative"})
    public void shouldReturnNotFoundForUnknownBookId() {
        int invalidBookId = TestDataReader.getInt("book.invalid.id");

        ProblemDetails problem = booksApi.getBookById(invalidBookId)
                .then()
                .log().ifValidationFails()
                .statusCode(NOT_FOUND)
                .extract()
                .as(ProblemDetails.class);

        assertEquals(problem.getStatus(), NOT_FOUND);
        assertEquals(problem.getTitle(), TestDataReader.get("book.not.found.title"));
        assertNotNull(problem.getTraceId(), "Problem response should include a trace id.");
    }
}
