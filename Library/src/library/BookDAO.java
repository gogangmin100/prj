package library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	private static final String USERNAME = "system";
	private static final String PASSWORD = "1111";

	// 생성자: DB 드라이버 로드
	public BookDAO() {
		try {
			Class.forName(DRIVER);
			System.out.println("클래스 로드 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("클래스 로드 실패: " + e.getMessage());
		}
	}

	// 데이터베이스 연결 메서드
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	// 책 추가 (borrow_count 기본값 0 추가)
	public boolean addBook(BookDTO book) {
		String sql = "INSERT INTO books (title, author, borrowed, borrow_count) VALUES (?, ?, ?, 0)";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setInt(3, book.getBorrowed());

			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0; // 성공하면 true 반환
		} catch (SQLException e) {
			System.out.println("책 추가 실패: " + e.getMessage());
			return false; // 실패하면 false 반환
		}
	}

	// 대출 상태 업데이트 
	public boolean updateBook(String title, boolean isBorrowed) {
		
		String sql = isBorrowed ? "UPDATE books SET borrowed = 1, borrow_count = borrow_count + 1 WHERE REPLACE(UPPER(title), ' ', '') = REPLACE(UPPER(?), ' ', '')"
			    : "UPDATE books SET borrowed = 0 WHERE REPLACE(UPPER(title), ' ', '') = REPLACE(UPPER(?), ' ', '')"; // 반납할 때는 대출 상태만 변경

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, title);

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0; // 성공한 경우 true 반환, 실패한 경우 false 반환
		} catch (SQLException e) {
			System.out.println("대출 상태 업데이트 실패: " + e.getMessage());
			return false; // 예외 발생 시 false 반환
		}
		
		
	}

	// 전체 책 목록 조회
	public List<BookDTO> getAllBooks() {
		List<BookDTO> books = new ArrayList<>();
		String sql = "SELECT * FROM books";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				books.add(new BookDTO(rs.getString("title"), rs.getString("author"), rs.getInt("borrowed"),
						rs.getInt("borrow_count")));
			}
		} catch (SQLException e) {
			 e.printStackTrace();
			System.out.println("책 목록 조회 실패: " + e.getMessage());
		}
		return books;
	}

	// 책 검색 (제목으로 부분 검색)
	public List<BookDTO> searchBooks(String keyword) {
		List<BookDTO> foundBooks = new ArrayList<>();
		String sql = "SELECT * FROM books WHERE title LIKE ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, "%" + keyword + "%");
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					foundBooks.add(new BookDTO(rs.getString("title"), rs.getString("author"), rs.getInt("borrowed"),
							rs.getInt("borrow_count")));
				}
			}
		} catch (SQLException e) {
			System.out.println("책 검색 실패: " + e.getMessage());
		}
		return foundBooks;
	}

	// 인기 도서 조회 (Top 5)
	public List<BookDTO> getPopularBooks() {
		List<BookDTO> popularBooks = new ArrayList<>();
		String sql = "SELECT title, author, borrow_count "
				+ "FROM (SELECT title, author, borrow_count FROM books ORDER BY borrow_count DESC) "
				+ "WHERE ROWNUM <= 5";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				popularBooks.add(new BookDTO(rs.getString("title"), rs.getString("author"), 0, // 대출 상태는 중요하지 않음
						rs.getInt("borrow_count")));
			}
		} catch (SQLException e) {
			System.out.println("인기 도서 조회 실패: " + e.getMessage());
		}
		return popularBooks;
	}
	
	//대출 시
	public BookDTO getBookByTitle(String title) {
		String sql = "SELECT * FROM books WHERE REPLACE(UPPER(title), ' ', '') = REPLACE(UPPER(?), ' ', '')";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, title);
			
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					BookDTO book = new BookDTO("책 제목", "저자", 0, 0); // 대출 가능 상태
					book.setTitle(rs.getString("title"));
					book.setAuthor(rs.getString("author"));
					book.setBorrowed(rs.getInt("borrowed"));
					book.setBorrowCount(rs.getInt("borrow_count"));
					return book;
				} else {
					System.out.println("책을 찾을 수 없습니다: " + title); // 콘솔에 책이 없을 경우 메시지 추가
					return null;
				}
			}
		} catch (SQLException e) {
			System.out.println("책 검색 실패: " + e.getMessage());
			return null;
		}
	}

	// 회원가입기능
	public boolean libraryUsers(String id, String password) {
		String sql = "INSERT INTO libraryusers(id,password) VALUES (?, ?)";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, id);
			stmt.setString(2, password);

			int rowslnserted = stmt.executeUpdate();
			return rowslnserted > 0;
		} catch (SQLException e) {
			System.out.println("회원가입 실패" + e.getMessage());
			return false;
		}
	}
	


    // 로그인 기능
    public boolean loginUser(String id, String password) {
        String sql = "SELECT COUNT(*) FROM libraryusers WHERE id = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("로그인 성공");  // 디버깅용 출력
                    return count > 0; // 로그인 성공 시 true 반환
                }
            }
        } catch (SQLException e) {
            System.out.println("로그인 실패: " + e.getMessage());
        }
        return false; // 로그인 실패 시 false 반환
    }

    // 아이디 중복 검사 메서드
    public boolean Username(String username) {
        String sql = "SELECT COUNT(*) FROM libraryusers WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // 이미 존재하는 아이디이면 true 반환
                }
            }
        } catch (SQLException e) {
            System.out.println("아이디 중복 검사 실패: " + e.getMessage());
        }
        return false; // 존재하지 않으면 false 반환
    }
    
    //회원가입한 id와 password 조회
	public List<BookDTO> getLibraryUsers() {
		List<BookDTO> userList = new ArrayList<>();
        String sql = "SELECT id, password FROM libraryusers";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BookDTO user = new BookDTO(sql, sql, 0, 0);
                user.setId(rs.getString("id"));
                user.setPassword(rs.getString("password"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("회원 목록 조회 실패: " + e.getMessage());
        }

        return userList;
    }
}

