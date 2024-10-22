package com.bernmpdev.productslambda;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.UUID;

@DynamoDBTable(tableName = "Books")
public class Book {
    private String id = UUID.randomUUID().toString();
    private String name;
    private int rating;
    private String author;
    private double price;

    public void updateFields(Book updatedBook) {
        if (updatedBook.name != null) {
            this.name = updatedBook.name;
        }
        if (updatedBook.rating >= 0) {
            this.rating = updatedBook.rating;
        }
        if (updatedBook.author != null) {
            this.author = updatedBook.author;
        }
        if (updatedBook.price >= 0) {
            this.price = updatedBook.price;
        }
    }

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "Rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @DynamoDBAttribute(attributeName = "Author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @DynamoDBAttribute(attributeName = "Price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}