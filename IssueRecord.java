package com.cyrostack.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IssueRecord {
    public static final int LOAN_DAYS = 14;
    public static final double FINE_PER_DAY = 5.0;

    private final Book book;
    private final Member member;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public IssueRecord(Book book, Member member, LocalDate issueDate) {
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = issueDate.plusDays(LOAN_DAYS);
    }

    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getFine() { return fine; }
    public boolean isReturned() { return returnDate != null; }

    public double close(LocalDate date) {
        returnDate = date;
        long late = ChronoUnit.DAYS.between(dueDate, date);
        fine = late > 0 ? late * FINE_PER_DAY : 0;
        return fine;
    }

    @Override
    public String toString() {
        String status = isReturned()
                ? "Returned " + returnDate + (fine > 0 ? String.format(" (fine Rs. %.2f)", fine) : "")
                : "Due " + dueDate;
        return String.format("%-30s -> %-16s Issued %s | %s",
                book.getTitle(), member.getName(), issueDate, status);
    }
}
