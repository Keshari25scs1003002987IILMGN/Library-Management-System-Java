# Library Management System in Java

A console-based library system that issues and returns books using Java collections (`LinkedHashMap`, `ArrayList`) and the Streams API.

## Features
- Add and remove books (a book that is issued cannot be removed)
- Register members
- Issue and return books with automatic due dates
- Rules: 14-day loan period, maximum 3 books per member, Rs. 5 fine per late day
- Members with an overdue book cannot borrow more
- Search by title, author or category
- View all books, issued books and a member's borrowing history

## Project Structure
```
src/com/cyrostack/library/
├── Main.java          # console menu (loads 4 sample books)
├── Library.java       # core logic using collections
├── Book.java
├── Member.java
└── IssueRecord.java   # one issue/return event, calculates the fine
test/
└── LibraryTest.java   # 9 self-checking tests (late fines, limits, etc.)
```

## How to Run (JDK 8+)
```bash
mkdir out
javac -d out src/com/cyrostack/library/*.java
java -cp out com.cyrostack.library.Main
```

### Run the tests
```bash
javac -cp out -d out test/LibraryTest.java
java -cp out LibraryTest
```

## Author
Keshari Nandan Ray - Cyrostack Internship Project
