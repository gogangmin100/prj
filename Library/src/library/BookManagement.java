package library;

import java.util.List;

public interface BookManagement {

    // 도서 추가
    void addBook(BookDTO book);

    // 도서 대출
    void borrowBook(String title);

    // 도서 반납
    void returnBook(String title);

    // 도서 검색
    List<BookDTO> searchBooks(String keyword);

    // 도서 목록 갱신
    void updateBookTable();
}

