package com.devnexus.graphql.controller;

import com.devnexus.graphql.model.Book;
import com.devnexus.graphql.repository.BookRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

}
