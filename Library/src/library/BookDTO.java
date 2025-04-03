package library;


import java.util.ArrayList;
import java.util.List;

public class BookDTO {
    private String title;  // 책 제목
    private String author; // 책 저자
    private int borrowed;  // 대출 여부 (0: 대출 가능, 1: 대출 중)
    private int borrowCount; // 대출 횟수 (추가)
    private String id;         // 회원 ID
    private String password;   // 회원 비밀번호
    private String borrowerId;  // 대출자 ID 추가
    
    
    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 생성자
    public BookDTO(String title, String author, int borrowed, int borrowCount) {
        this.title = title;  // 책 제목 설정
        this.author = author;  // 책 저자 설정
        this.borrowed = borrowed;  // 대출 상태 설정 (0 또는 1)
        this.borrowCount = borrowCount; // 대출 횟수 설정
        
    }

    // Getter & Setter
    public String getTitle() {
        return title;  // 책 제목 반환
    }

    public void setTitle(String title) {
        this.title = title;  // 책 제목 설정
    }

    public String getAuthor() {
        return author;  // 책 저자 반환
    }

    public void setAuthor(String author) {
        this.author = author;  // 책 저자 설정
    }

    public int getBorrowed() {
        return borrowed;  // 대출 상태 반환 (0 또는 1)
    }

    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;  // 대출 상태 설정 (0 또는 1)
    }

    public int getBorrowCount() {
        return borrowCount;  // 대출 횟수 반환
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;  // 대출 횟수 설정
    }

    // 대출 처리 메서드
    public void borrowBook() {
        if (this.borrowed == 0) {  // 대출 가능 상태에서만 대출
            this.borrowed = 1;  // 대출 상태를 1로 변경 (대출 중)
            this.borrowCount++;  // 대출 횟수 증가
        }
    }                                                                                                                                                                                       

    // 반납 처리 메서드
    public void returnBook() {
        if (this.borrowed == 1) {  // 대출 중인 상태에서만 반납
            this.borrowed = 0;  // 대출 상태를 0으로 변경 (대출 가능)
        }
    }
    
   
    
    
	// 객체 정보를 문자열로 반환
    @Override
    public String toString() {
        return "제목: " + title + ", 저자: " + author + (borrowed == 1 ? " (대출 중)" : " (대출 가능)");  
        // 대출 상태에 따라 (대출 중) 또는 (대출 가능) 문자열을 붙여서 반환
    }

	
}
