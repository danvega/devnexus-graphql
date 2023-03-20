package com.devnexus.graphql.controller;

import com.devnexus.graphql.model.Book;
import com.devnexus.graphql.repository.BookRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphQLController {

    private final BookRepository bookRepository;

    public GraphQLController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @QueryMapping
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @QueryMapping
    public Book findBookById(@Argument Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Book createBook(@Argument String title, @Argument Integer pages, @Argument String author) {
        return bookRepository.save(new Book(title, pages, author));
    }

}
