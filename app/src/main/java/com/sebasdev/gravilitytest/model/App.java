package com.sebasdev.gravilitytest.model;

/**
 * Created by m4605 on 16/03/16.
 */
public class App {

    private String name;
    private String author;
    private String price;
    private String description;
    private String image;
    private Category category;

    public App() {
    }

    public App(String name, String author, String price, String description, String image, Category category) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
