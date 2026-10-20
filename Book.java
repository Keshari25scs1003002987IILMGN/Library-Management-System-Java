package com.cyrostack.library;

public class Book {
    private final int id;
    private final String title;
    private final String author;
    private final String category;
    private boolean available = true;

    public Book(int id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("[%d] %-32s %-22s %-12s %s", id, title, author, category,
                available ? "Available" : "Issued");
    }
}
