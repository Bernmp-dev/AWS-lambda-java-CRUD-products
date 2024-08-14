package com.bernmpdev.productslambda;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import java.util.List;

class BookRepository {
    private final DynamoDBMapper dynamoDBMapper;

    public BookRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void save(Book book) {
        dynamoDBMapper.save(book);
    }

    public List<Book> getAll() {
        return dynamoDBMapper.scan(Book.class, new DynamoDBScanExpression());
    }

    public Book getById(String bookId) {
        return dynamoDBMapper.load(Book.class, bookId);
    }

    public void delete(Book book) {
        dynamoDBMapper.delete(book);
    }
}
