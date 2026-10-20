package com.cyrostack.library;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final Library lib = new Library();

    public static void main(String[] args) {
        seed();
        System.out.println("===== LIBRARY MANAGEMENT SYSTEM =====");
        while (true) {
            System.out.println("\n1. Add Book        2. Remove Book     3. View All Books");
            System.out.println("4. Search Books    5. Register Member 6. View Members");
            System.out.println("7. Issue Book      8. Return Book     9. Issued Books");
            System.out.println("10. Member History 0. Exit");
            int choice = readInt("Choose: ");
            if (choice == 0) { System.out.println("Goodbye!"); return; }
            try {
                handle(choice);
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handle(int choice) {
        switch (choice) {
            case 1:
                System.out.print("Title: "); String t = in.nextLine();
                System.out.print("Author: "); String a = in.nextLine();
                System.out.print("Category: "); String c = in.nextLine();
                System.out.println("Added: " + lib.addBook(t, a, c));
                break;
            case 2:
                lib.removeBook(readInt("Book ID: "));
                System.out.println("Book removed.");
                break;
            case 3: show(lib.allBooks()); break;
            case 4:
                System.out.print("Search (title/author/category): ");
                show(lib.searchBooks(in.nextLine()));
                break;
            case 5:
                System.out.print("Name: "); String n = in.nextLine();
                System.out.print("Phone (10 digits): "); String p = in.nextLine();
                System.out.println("Registered: " + lib.registerMember(n, p));
                break;
            case 6: show(lib.allMembers()); break;
            case 7:
                int b = readInt("Book ID: "), m = readInt("Member ID: ");
                IssueRecord r = lib.issueBook(b, m);
                System.out.println("Issued. Due date: " + r.getDueDate());
                break;
            case 8:
                double fine = lib.returnBook(readInt("Book ID: "));
                System.out.println(fine > 0 ? String.format("Returned late. Fine: Rs. %.2f", fine) : "Returned on time. No fine.");
                break;
            case 9: show(lib.currentlyIssued()); break;
            case 10: show(lib.historyOf(readInt("Member ID: "))); break;
            default: System.out.println("Invalid option.");
        }
    }

    private static <T> void show(List<T> items) {
        if (items.isEmpty()) System.out.println("(nothing to show)");
        else items.forEach(System.out::println);
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try { return Integer.parseInt(in.nextLine().trim()); } catch (NumberFormatException e) { return -1; }
    }

    private static void seed() {
        lib.addBook("Effective Java", "Joshua Bloch", "Java");
        lib.addBook("Clean Code", "Robert C. Martin", "Software");
        lib.addBook("Head First Java", "Kathy Sierra", "Java");
        lib.addBook("Introduction to Algorithms", "Thomas Cormen", "Algorithms");
        lib.registerMember("Demo Member", "9876543210");
    }
}
