import com.cyrostack.library.*;
import java.time.LocalDate;

/** Self-checking tests. Run: java -cp out LibraryTest */
public class LibraryTest {
    static int failures = 0;

    public static void main(String[] args) {
        Library lib = new Library();
        Book b1 = lib.addBook("Java Basics", "A. Author", "Java");
        Book b2 = lib.addBook("Data Structures", "B. Author", "CS");
        Member m = lib.registerMember("Asha", "9999999999");
        LocalDate d = LocalDate.of(2026, 1, 1);

        lib.issueBook(b1.getId(), m.getId(), d);
        check("issued book is unavailable", !b1.isAvailable());
        check("cannot issue twice", throwsIllegalState(() -> lib.issueBook(b1.getId(), m.getId(), d)));
        check("on-time return has no fine", lib.returnBook(b1.getId(), d.plusDays(14)) == 0);
        check("book available after return", b1.isAvailable());

        lib.issueBook(b2.getId(), m.getId(), d);
        check("5 days late = Rs. 25 fine", lib.returnBook(b2.getId(), d.plusDays(19)) == 25.0);

        for (int i = 0; i < 3; i++) lib.issueBook(lib.addBook("Extra " + i, "X", "X").getId(), m.getId(), d);
        check("4th book blocked by limit", throwsIllegalState(() -> lib.issueBook(b1.getId(), m.getId(), d)));
        check("search by author", lib.searchBooks("b. author").size() == 1);
        check("cannot remove issued book", throwsIllegalState(() -> lib.removeBook(3)));
        check("overdue member blocked", throwsIllegalState(() -> {
            lib.returnBook(3, d.plusDays(1));
            lib.issueBook(b1.getId(), m.getId(), d.plusDays(30));
        }));
        System.out.println(failures == 0 ? "\nAll tests passed." : "\n" + failures + " FAILED.");
        System.exit(failures);
    }

    static boolean throwsIllegalState(Runnable r) {
        try { r.run(); return false; } catch (IllegalStateException e) { return true; }
    }

    static void check(String name, boolean ok) {
        if (!ok) failures++;
        System.out.printf("%-4s %s%n", ok ? "PASS" : "FAIL", name);
    }
}
