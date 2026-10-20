package com.cyrostack.library;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/** Core library logic built on Java collections (HashMap + ArrayList). */
public class Library {
    private final Map<Integer, Book> books = new LinkedHashMap<>();
    private final Map<Integer, Member> members = new LinkedHashMap<>();
    private final List<IssueRecord> records = new ArrayList<>();
    private int nextBookId = 1;
    private int nextMemberId = 1;

    public Book addBook(String title, String author, String category) {
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Title and author are required.");
        Book b = new Book(nextBookId++, title.trim(), author.trim(), category == null ? "General" : category.trim());
        books.put(b.getId(), b);
        return b;
    }

    public void removeBook(int id) {
        Book b = getBook(id);
        if (!b.isAvailable()) throw new IllegalStateException("Cannot remove a book that is currently issued.");
        books.remove(id);
    }

    public Member registerMember(String name, String phone) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name is required.");
        if (phone == null || !phone.matches("\\d{10}")) throw new IllegalArgumentException("Phone must be 10 digits.");
        Member m = new Member(nextMemberId++, name.trim(), phone);
        members.put(m.getId(), m);
        return m;
    }

    public IssueRecord issueBook(int bookId, int memberId) {
        return issueBook(bookId, memberId, LocalDate.now());
    }

    public IssueRecord issueBook(int bookId, int memberId, LocalDate date) {
        Book book = getBook(bookId);
        Member member = getMember(memberId);
        if (!book.isAvailable()) throw new IllegalStateException("Book is already issued.");
        if (!member.canBorrow())
            throw new IllegalStateException("Member has reached the limit of " + Member.MAX_BOOKS + " books.");
        if (hasOverdue(member, date))
            throw new IllegalStateException("Member has an overdue book. Return it first.");
        book.setAvailable(false);
        member.getBorrowed().add(book);
        IssueRecord r = new IssueRecord(book, member, date);
        records.add(r);
        return r;
    }

    /** Returns the fine charged (0 if returned on time). */
    public double returnBook(int bookId) {
        return returnBook(bookId, LocalDate.now());
    }

    public double returnBook(int bookId, LocalDate date) {
        Book book = getBook(bookId);
        IssueRecord r = records.stream()
                .filter(x -> x.getBook() == book && !x.isReturned())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("This book is not currently issued."));
        double fine = r.close(date);
        book.setAvailable(true);
        r.getMember().getBorrowed().remove(book);
        return fine;
    }

    public List<Book> searchBooks(String keyword) {
        String k = keyword.trim().toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(k)
                        || b.getAuthor().toLowerCase().contains(k)
                        || b.getCategory().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public List<Book> allBooks() { return new ArrayList<>(books.values()); }

    public List<Book> availableBooks() {
        return books.values().stream().filter(Book::isAvailable).collect(Collectors.toList());
    }

    public List<IssueRecord> currentlyIssued() {
        return records.stream().filter(r -> !r.isReturned()).collect(Collectors.toList());
    }

    public List<IssueRecord> historyOf(int memberId) {
        Member m = getMember(memberId);
        return records.stream().filter(r -> r.getMember() == m).collect(Collectors.toList());
    }

    public List<Member> allMembers() { return new ArrayList<>(members.values()); }

    public Book getBook(int id) {
        Book b = books.get(id);
        if (b == null) throw new NoSuchElementException("No book with ID " + id + ".");
        return b;
    }

    public Member getMember(int id) {
        Member m = members.get(id);
        if (m == null) throw new NoSuchElementException("No member with ID " + id + ".");
        return m;
    }

    private boolean hasOverdue(Member m, LocalDate today) {
        return records.stream().anyMatch(r -> r.getMember() == m && !r.isReturned() && today.isAfter(r.getDueDate()));
    }
}
