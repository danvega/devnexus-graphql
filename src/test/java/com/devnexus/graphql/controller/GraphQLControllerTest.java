package com.devnexus.graphql.controller;

import com.devnexus.graphql.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureGraphQlTester
class GraphQLControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    void findAllBooks() {
        // language=GraphQL
        String document = """
        query {
            findAllBooks {
                id
                title
                pages
                author
            }
        }
        """;

        graphQlTester.document(document)
                .execute()
                .path("findAllBooks")
                .entityList(Book.class)
                .hasSizeGreaterThan(2); // 3 or 4 books depending on execution order
    }

    @Test
    void findBookByValidIdShouldReturnBook() {
        // language=GraphQL
        String document = """
        query {
          findBookById(id:1) {
            id
            title
            pages
            author
            reviews {
              id
              title
              comment
            }
          }
        }
        """;

        graphQlTester.document(document)
                .variable("id",1)
                .execute()
                .path("findBookById")
                .entity(Book.class)
                .satisfies(book -> {
                    assertEquals(1,book.getId());
                    assertEquals("Reactive Spring",book.getTitle());
                    assertEquals(484,book.getPages());
                    assertEquals("Josh Long",book.getAuthor());
                    assertEquals(1,book.getReviews().size());
                });
    }

    @Test
    void createBookWithValidBook() {
        // language=GraphQL
        String document = """
        mutation CreateBook {
          createBook(title:"My new book",pages:99,author:"Dan Vega") {
            id
            title
            pages
            author
          }
        }
        """;

        graphQlTester.document(document)
                .execute()
                .path("createBook")
                .entity(Book.class)
                .satisfies(book -> {
                    assertEquals(4,book.getId());
                    assertEquals("My new book",book.getTitle());
                    assertEquals(99,book.getPages());
                    assertEquals("Dan Vega",book.getAuthor());
                });
    }

}