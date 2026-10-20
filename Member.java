package com.cyrostack.library;

import java.util.ArrayList;
import java.util.List;

public class Member {
    public static final int MAX_BOOKS = 3;

    private final int id;
    private final String name;
    private final String phone;
    private final List<Book> borrowed = new ArrayList<>();

    public Member(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public List<Book> getBorrowed() { return borrowed; }
    public boolean canBorrow() { return borrowed.size() < MAX_BOOKS; }

    @Override
    public String toString() {
        return String.format("[%d] %-20s %-12s Books held: %d/%d", id, name, phone, borrowed.size(), MAX_BOOKS);
    }
}
