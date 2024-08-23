package com.bernmpdev.productslambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static com.bernmpdev.productslambda.MessageUtil.returnMessage;
import static com.bernmpdev.productslambda.ResponseUtil.createResponse;

public class BookHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
    private static final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);
    private final BookRepository bookRepository = new BookRepository(dynamoDBMapper);

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String httpMethod = request.getHttpMethod();
        System.out.println("HTTP Method: " + httpMethod);

        try {
            return switch (httpMethod) {
                case "POST" -> saveBook(request, context);
                case "GET" -> getBooks(request, context);
                case "PATCH" -> updateBook(request, context);
                case "DELETE" -> deleteBookById(request, context);
                default -> createResponse(405, "Method Not Allowed");
            };
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    public APIGatewayProxyResponseEvent saveBook(APIGatewayProxyRequestEvent request, Context ignoredContext) {
        try {
            if (request.getBody() == null || request.getBody().isEmpty()) {
                return createResponse(400, returnMessage("Request body is missing"));
            }

            Book book = objectMapper.readValue(request.getBody(), Book.class);

            bookRepository.save(book);

            return createResponse(200, returnMessage("Book saved successfully!"));
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    public APIGatewayProxyResponseEvent getBooks(APIGatewayProxyRequestEvent request, Context ignoredContext) {
        try {
            Map<String, String> pathParameters = request.getPathParameters();

            if (pathParameters != null && pathParameters.containsKey("bookId")) {
                return getBookById(pathParameters.get("bookId"));
            } else {
                return getAllBooks();
            }
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    private APIGatewayProxyResponseEvent getBookById(String bookId) {
        try {
            if (bookId == null || bookId.isEmpty()) {
                return createResponse(400, returnMessage("Invalid path parameter: bookId is null or empty"));
            }

            Book book = bookRepository.getById(bookId);

            if (book != null) {
                String body = objectMapper.writeValueAsString(book);
                return createResponse(200, body);
            } else {
                return createResponse(404, returnMessage("Book not found"));
            }
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    private APIGatewayProxyResponseEvent getAllBooks() {
        try {
            List<Book> books = bookRepository.getAll();
            String body = objectMapper.writeValueAsString(books);
            return createResponse(200, body);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    public APIGatewayProxyResponseEvent deleteBookById(APIGatewayProxyRequestEvent request, Context ignoredContext) {
        try {
            String bookId = request.getPathParameters().get("bookId");
            Book book = bookRepository.getById(bookId);

            if (book != null) {
                bookRepository.delete(book);
                return createResponse(200, returnMessage("Book deleted successfully!"));
            } else {
                return createResponse(404, returnMessage("Book not found!"));
            }
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }

    public APIGatewayProxyResponseEvent updateBook(APIGatewayProxyRequestEvent request, Context ignoredContext) {
        try {

            if (request.getBody() == null || request.getBody().isEmpty()) {
                return createResponse(400, returnMessage("Request body is missing"));
            }

            Book updatedBook = objectMapper.readValue(request.getBody(), Book.class);

            if (updatedBook.getId() == null || updatedBook.getId().isEmpty()) {
                return createResponse(400, returnMessage("Book Id is missing"));
            }

            Book existingBook = bookRepository.getById(updatedBook.getId());

            if (existingBook != null) {
                existingBook.updateFields(updatedBook);

                bookRepository.save(existingBook);

                return createResponse(200, returnMessage("Book updated successfully!"));
            } else {
                return createResponse(404, returnMessage("Book not found!"));
            }
        } catch (Exception e) {
            return ExceptionHandler.handleException(e);
        }
    }
}
