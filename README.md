# Devnexus Booth GraphQL Demo

This is a demo application that will be used at the booth at Devnexus 2023. This demo shows how to convert a REST API to a GraphQL API. 
It walks through a simple example of a book review API.

## Getting Started

The final code is located in the `main` branch. To get started, clone the repository and checkout the `start-here` branch. Start by 
examining the `pom.xml` build file to show off the existing dependencies of this application.

- Spring Web
- Spring for GraphQL
- Spring Data JPA
- H2 Database

Take some time to examine the existing code and make note of the `DataLoader` class. This class is used to load some data into the database.

### Running the Starter Code

Before adding any code to the project run the application and make sure it works. You can run the application from the command line using
the following command:

`./mvnw spring-boot:run`

Or by running the `Application` class from your IDE. When the application is running you can visit the following URL to see the REST API:

http://localhost:8080/api/books 

## GraphQL 

Now that we have a REST API up and running, let's see what it takes to add a GraphQL API.

### Create a GraphQL Schema

Start by creating a new file called `schema.graphqls` in the `src/main/resources/graphql` directory. This file will contain the GraphQL schema.
The first thing you need to do is to create object types for `Book` and `Review`. 

```graphql
type Book {
    id: ID!
    title: String
    pages: Int
    author: String
    reviews: [Review]
}

type Review {
    id: ID!
    title: String
    comment: String
}
```

There are 3 main operation types in GraphQL: `query`, `mutation`, and `subscription`. For this demo we will only be using the `query` and `mutation` types. Create a new type called `Query` and add a field to it called `findAllBooks`. This field will return a list of `Book` objects.

```graphql
type Query {
    findAllBooks: [Book]
}
```

Now that we have a working schema we need to create a GraphQL controller to handle the requests.

### Create a GraphQL Controller

Spring for GraphQL provides an annotation-based programming model where `@Controller` components use annotations to declare handler methods with flexible method signatures to fetch the data for specific GraphQL fields. Create a new class in the `controller` package called 
`GraphQLController` and add the following code:

```java
@Controller
public class GraphQLController {
    
}
```
Next you will need an instance of the `BookRepository` class to fetch and persist data to the database. Add the following code to get 
an instance of the `BookRepository` class using constructor injection.

```java
@Controller
public class GraphQLController {

    private final BookRepository bookRepository;

    public GraphQLController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
}
```

### Find All Books

You are now ready to write the handler method for the `findAllBooks` query. Add the following code to the `GraphQLController` class:

```java
@QueryMapping
public List<Book> findAllBooks() {
    return bookRepository.findAll();
}
```

The `@QueryMapping` annotation is used to map a GraphQL query to a handler method. This is shorthand for `@SchemaMapping(typeName = "Query", field = "findAllBooks")`. The `findAllBooks` method will return a list of `Book` objects.

### GraphiQL UI

Open up application.properties and add the following configuration:

```properties
spring.graphql.graphiql.enabled=true
```

This will enable the GraphiQL UI. Restart the application and visit the following URL: http://localhost:8080/graphiql to see the GraphiQL UI. You
can now test the `findAllBooks` query by entering the following query in the left panel:

```graphql
query {
    findAllBooks {
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
```

If you run that query you should get the following response: 

```json
{
  "data": {
    "findAllBooks": [
      {
        "id": "1",
        "title": "Reactive Spring",
        "pages": 484,
        "author": "Josh Long",
        "reviews": [
          {
            "id": "1",
            "title": "The Book on Reactive Spring",
            "comment": "I really learned a lot from this book!"
          }
        ]
      },
      {
        "id": "2",
        "title": "Learning Spring Boot 3.0",
        "pages": 270,
        "author": "Greg L. Turnquist",
        "reviews": [
          {
            "id": "2",
            "title": "5 Stars",
            "comment": "I thought this book was amazing!"
          }
        ]
      },
      {
        "id": "3",
        "title": "Spring in Action, Sixth Edition",
        "pages": 520,
        "author": "Craig Walls",
        "reviews": [
          {
            "id": "3",
            "title": "Great from start to finish",
            "comment": "So much Spring!"
          }
        ]
      }
    ]
  }
}
```

### Find By ID

Now that we have a working query, let's add a query to find a book by ID. First add a new field to the `Query` type named `findBookById`. This
field will take an `ID` argument which can't be null and return a single `Book` object.

```graphql
type Query {
    findAllBooks: [Book]
    findBookById(id: ID!): Book
}
```

Next you will need to add a new method to the `GraphQLController` class to find a book by its id. This method will bind 
the named GraphQL argument to the method parameter using the `@Argument` annotation. Add the following code to the 
`GraphQLController` class:

```java
@QueryMapping
public Book findBookById(@Argument Integer id) {
    return bookRepository.findById(id).orElse(null);
}
```

Run the application and execute the following mutation in the GraphiQL UI:

```graphql
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
```

If everything worked correctly you should get the following response:

```json
{
  "data": {
    "findBookById": {
      "id": "1",
      "title": "Reactive Spring",
      "pages": 484,
      "author": "Josh Long",
      "reviews": [
        {
          "id": "1",
          "title": "The Book on Reactive Spring",
          "comment": "I really learned a lot from this book!"
        }
      ]
    }
  }
}
```

### Mutation 

Now that we have a working query, let's add a mutation to create a new book. First add a new type called `Mutation` to the schema. This field will be named createBook and will return a `Book` object. It will accept 3 arguments: title, pages, and author.

```graphql
type Mutation {
    createBook(title: String, pages: Int, author: String): Book
}
```

Next you will need to add a new method to the `GraphQLController` class to create a new book. This method will bind the named GraphQL arguments
to the method parameters using the `@Argument` annotation. Add the following code to the `GraphQLController` class:

```java
@MutationMapping
public Book createBook(@Argument String title, @Argument Integer pages, @Argument String author) {
    return bookRepository.save(new Book(title, pages, author));
}
```
Run the application and execute the following mutation in the GraphiQL UI:

```graphql
mutation CreateBook {
  createBook(title:"My new book",pages:99,author:"Dan Vega") {
    id
    title
    pages
    author
  }
}
```
If everything worked correctly you should get the following response:

```json
{
  "data": {
    "createBook": {
      "id": "4",
      "title": "My new book",
      "pages": 99,
      "author": "Dan Vega"
    }
  }
}
```


## Conclusion

Congratulations! You have successfully created a GraphQL API using Spring Boot and Spring Data JPA.