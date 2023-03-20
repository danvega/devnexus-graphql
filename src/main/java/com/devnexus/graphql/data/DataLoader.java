package com.devnexus.graphql.data;

import com.devnexus.graphql.model.Book;
import com.devnexus.graphql.model.Review;
import com.devnexus.graphql.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;

    public DataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(java.lang.String... args) {
        var reactiveSpring = new Book("Reactive Spring",
                484,
                "Josh Long",
                List.of(new Review("The Book on Reactive Spring", "I really learned a lot from this book!")));

        var learnSpringBoot3 = new Book("Learning Spring Boot 3.0",
                270,
                "Greg L. Turnquist",
                List.of(new Review("5 Stars","I thought this book was amazing!")));

        var springInAction = new Book("Spring in Action, Sixth Edition",
                520,
                "Craig Walls",
                List.of(new Review("Great from start to finish","So much Spring!")));

        bookRepository.saveAll(List.of(reactiveSpring,learnSpringBoot3,springInAction));
    }


}
