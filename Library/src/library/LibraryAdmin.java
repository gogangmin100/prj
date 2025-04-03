package library; // 패키지 선언: 이 클래스는 'library'라는 패키지에 속함

import javax.swing.*; // Swing GUI 컴포넌트를 사용하기 위한 import
import javax.swing.table.DefaultTableModel; // 테이블 데이터를 관리하기 위한 DefaultTableModel 클래스 import
import java.awt.*; // AWT 패키지: 기본적인 GUI 구성 요소와 레이아웃 사용
import java.awt.event.*; // 이벤트 처리를 위한 AWT 이벤트 패키지 import
import java.io.IOException; // 입출력 예외 처리를 위한 import
import java.util.ArrayList; // 동적 배열 리스트를 사용하기 위한 import
import java.util.List; // List 인터페이스를 사용하기 위한 import

public class LibraryAdmin extends JFrame implements BookManagement { // LibraryAdmin 클래스 정의: JFrame을 상속받고 BookManagement 인터페이스 구현
    private BookDAO bookDAO; // BookDAO 객체: 데이터베이스와의 상호작용을 담당

    private String loggedInUser; // 현재 로그인한 사용자의 아이디를 저장하는 변수

    private CardLayout cardLayout; // 화면 전환을 위한 CardLayout 객체
    private JPanel mainPanel; // 로그인 화면과 도서 관리 화면을 포함하는 메인 패널
    private JPanel loginPanel; // 로그인 화면을 표시하는 패널
    private JPanel bookPanel; // 도서 관리 화면을 표시하는 패널

    private JTable jTable; // 도서 목록을 테이블 형태로 표시하기 위한 JTable 객체
    private DefaultTableModel defaultTablemodel; // JTable의 데이터를 관리하는 모델

    public LibraryAdmin() { // 생성자: LibraryAdmin 객체 초기화
        // 초기화
        bookDAO = new BookDAO(); // BookDAO 객체 생성: 데이터베이스 작업을 처리

        loggedInUser = null; // 로그인된 사용자를 null로 초기화 (아직 로그인 안 됨)

        // JFrame 설정
        setTitle("도서 관리 시스템"); // 창 제목 설정
        setSize(800, 500); // 창 크기를 800x500 픽셀로 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 버튼 클릭 시 프로그램 종료
        setLayout(new BorderLayout()); // JFrame의 레이아웃을 BorderLayout으로 설정

        // CardLayout 설정
        cardLayout = new CardLayout(); // CardLayout 객체 생성: 화면 전환 관리
        mainPanel = new JPanel(cardLayout); // CardLayout을 적용한 메인 패널 생성

        // 로그인 화면 생성
        createLoginPanel(); // 로그인 화면을 생성하는 메서드 호출

        // 도서 관리 화면 생성
        createBookPanel(); // 도서 관리 화면을 생성하는 메서드 호출

        // 패널 추가
        mainPanel.add(loginPanel, "Login"); // 메인 패널에 로그인 화면 추가, 이름은 "Login"
        mainPanel.add(bookPanel, "Books"); // 메인 패널에 도서 관리 화면 추가, 이름은 "Books"

        // 초기 화면을 로그인 화면으로 설정
        add(mainPanel); // JFrame에 메인 패널 추가
        cardLayout.show(mainPanel, "Login"); // 초기 화면을 "Login"으로 설정

        setVisible(true); // 창을 화면에 보이게 설정
    }

    private void createLoginPanel() { // 로그인 화면을 생성하는 메서드
        loginPanel = new JPanel(new BorderLayout()); // 로그인 패널 생성, BorderLayout 사용

        JLayeredPane layeredPane = new JLayeredPane(); // 계층 구조를 위한 JLayeredPane 생성
        layeredPane.setPreferredSize(new Dimension(400, 300)); // layeredPane 크기 설정

        JPanel loginForm = new JPanel(new GridLayout(3, 2, 5, 5)); // 로그인 폼 패널 생성, 3행 2열 그리드 레이아웃
        loginForm.setBounds(50, 100, 300, 100); // 로그인 폼의 위치와 크기 설정
        loginForm.setBackground(new Color(255, 255, 255, 200)); // 배경색 설정 (반투명 흰색)

        JLabel usernameLabel = new JLabel("아이디:"); // "아이디:" 라벨 생성
        JTextField usernameField = new JTextField(10); // 아이디 입력 필드 생성, 10자 제한
        JLabel passwordLabel = new JLabel("비밀번호:"); // "비밀번호:" 라벨 생성
        JPasswordField passwordField = new JPasswordField(10); // 비밀번호 입력 필드 생성, 10자 제한
        JButton loginButton = new JButton("로그인"); // "로그인" 버튼 생성
        JButton signupButton = new JButton("회원가입"); // "회원가입" 버튼 생성

        loginButton.addActionListener(e -> { // 로그인 버튼에 액션 리스너 추가
            String username = usernameField.getText().trim(); // 입력된 아이디 가져오기 (공백 제거)
            String password = new String(passwordField.getPassword()).trim(); // 입력된 비밀번호 가져오기 (공백 제거)

            // BookDAO를 사용하여 로그인 처리
            if (bookDAO.loginUser(username, password)) { // BookDAO로 로그인 검증
                loggedInUser = username; // 로그인 성공 시 사용자 이름 저장
                JOptionPane.showMessageDialog(this, "로그인 성공"); // 성공 메시지 표시
                cardLayout.show(mainPanel, "Books"); // 도서 관리 화면으로 전환
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀렸습니다."); // 실패 메시지 표시
            }
        });

        signupButton.addActionListener(e -> showSignupScreen(bookDAO)); // 회원가입 버튼 클릭 시 회원가입 화면 표시

        loginForm.add(usernameLabel); // 로그인 폼에 아이디 라벨 추가
        loginForm.add(usernameField); // 로그인 폼에 아이디 입력 필드 추가
        loginForm.add(passwordLabel); // 로그인 폼에 비밀번호 라벨 추가
        loginForm.add(passwordField); // 로그인 폼에 비밀번호 입력 필드 추가
        loginForm.add(loginButton); // 로그인 폼에 로그인 버튼 추가
        loginForm.add(signupButton); // 로그인 폼에 회원가입 버튼 추가

        layeredPane.add(loginForm, Integer.valueOf(1)); // layeredPane에 로그인 폼 추가 (레이어 1)

        JPanel backgroundPanel = new JPanel() { // 배경 패널 생성
            @Override
            protected void paintComponent(Graphics g) { // 배경 이미지 그리기 메서드 오버라이드
                super.paintComponent(g); // 부모 메서드 호출
                g.drawImage(new ImageIcon("C:/javasrc/Library/image/library.jpg").getImage(), 0, 0, getWidth(),
                        getHeight(), null); // 배경 이미지를 패널 크기에 맞게 그리기
            }
        };

        backgroundPanel.setLayout(new BorderLayout()); // 배경 패널에 BorderLayout 설정
        backgroundPanel.add(layeredPane, BorderLayout.CENTER); // 배경 패널 중앙에 layeredPane 추가

        loginPanel.add(backgroundPanel, BorderLayout.CENTER); // 로그인 패널 중앙에 배경 패널 추가
    }

    private void createBookPanel() { // 도서 관리 화면을 생성하는 메서드
        bookPanel = new JPanel(new BorderLayout()); // 도서 관리 패널 생성, BorderLayout 사용

        // 상단 버튼 패널
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6)); // 버튼 패널 생성, 1행 6열 그리드 레이아웃
        JButton addBookBtn = new JButton("도서 추가"); // "도서 추가" 버튼 생성
        JButton borrowBookBtn = new JButton("도서 대출"); // "도서 대출" 버튼 생성
        JButton returnBookBtn = new JButton("도서 반납"); // "도서 반납" 버튼 생성
        JButton viewBooksBtn = new JButton("인기 도서"); // "인기 도서" 버튼 생성
        JButton searchBookBtn = new JButton("도서 검색"); // "도서 검색" 버튼 생성
        JButton logoutBtn = new JButton("로그아웃"); // "로그아웃" 버튼 생성

        // 버튼 이벤트 추가
        addBookBtn.addActionListener(e -> { // "도서 추가" 버튼에 액션 리스너 추가
            showAddBookDialog(); // 도서 추가 다이얼로그 표시
            refreshBookPanel(); // UI 새로고침
        });
        borrowBookBtn.addActionListener(e -> { // "도서 대출" 버튼에 액션 리스너 추가
            borrowBookAction(); // 도서 대출 처리
            refreshBookPanel(); // UI 새로고침
        });
        returnBookBtn.addActionListener(e -> { // "도서 반납" 버튼에 액션 리스너 추가
            returnBookAction(); // 도서 반납 처리
            refreshBookPanel(); // UI 새로고침
        });
        viewBooksBtn.addActionListener(e -> { // "인기 도서" 버튼에 액션 리스너 추가
            PopularBooks(); // 인기 도서 표시
            refreshBookPanel(); // UI 새로고침
        });
        searchBookBtn.addActionListener(e -> { // "도서 검색" 버튼에 액션 리스너 추가
            searchBooksAction(); // 도서 검색 처리
            refreshBookPanel(); // UI 새로고침
        });
        logoutBtn.addActionListener(e -> { // "로그아웃" 버튼에 액션 리스너 추가
            logoutAction(); // 로그아웃 처리
            refreshBookPanel(); // UI 새로고침 (필요 없을 수 있음)
        });

        // 버튼을 패널에 추가
        buttonPanel.add(addBookBtn); // 버튼 패널에 "도서 추가" 버튼 추가
        buttonPanel.add(borrowBookBtn); // 버튼 패널에 "도서 대출" 버튼 추가
        buttonPanel.add(returnBookBtn); // 버튼 패널에 "도서 반납" 버튼 추가
        buttonPanel.add(viewBooksBtn); // 버튼 패널에 "인기 도서" 버튼 추가
        buttonPanel.add(searchBookBtn); // 버튼 패널에 "도서 검색" 버튼 추가
        buttonPanel.add(logoutBtn); // 버튼 패널에 "로그아웃" 버튼 추가

        // DB에서 저장된 도서 순서대로 버튼 추가
        JPanel bookImagePanel = new JPanel(); // 도서 이미지 버튼을 담을 패널 생성
        bookImagePanel.setLayout(new GridLayout(0, 4, 10, 10)); // 가변 행, 4열 그리드 레이아웃 설정 (간격 10px)

        loadBooks(bookImagePanel); // 도서 목록을 로드하여 버튼 생성

        // 도서 목록을 스크롤 가능하게 설정
        JScrollPane bookScrollPane = new JScrollPane(bookImagePanel); // 도서 패널을 스크롤 가능하게 감쌈

        // 패널에 추가
        bookPanel.add(buttonPanel, BorderLayout.NORTH); // 도서 패널 상단에 버튼 패널 추가
        bookPanel.add(bookScrollPane, BorderLayout.CENTER); // 도서 패널 중앙에 스크롤 패널 추가

        updateBookTable(); // 테이블 데이터 초기 로딩
    }

    private void loadBooks(JPanel bookImagePanel) { // 도서 목록을 로드하고 버튼을 생성하는 메서드
        bookImagePanel.removeAll(); // 기존 버튼 모두 제거
        List<BookDTO> books = bookDAO.getAllBooks(); // DB에서 도서 목록 가져오기

        for (BookDTO book : books) { // 각 도서에 대해 반복
            String title = book.getTitle(); // 도서 제목 가져오기
            String author = book.getAuthor(); // 저자 가져오기
            String statusText = book.getBorrowed() == 1 ? "대출 중" : "대출 가능"; // 대출 상태 텍스트 설정

            // 도서 제목에 따라 이미지 경로를 하드코딩으로 지정
            String imagePath = getImagePath(title); // 도서 제목으로 이미지 경로 가져오기

            // 이미지 크기 조정 후 버튼에 추가
            ImageIcon bookImage = resizeImage(imagePath, 100, 150); // 이미지 크기를 100x150으로 조정
            JButton bookButton = new JButton(title, bookImage); // 제목과 이미지를 포함한 버튼 생성

            // 버튼에 마우스 리스너 추가
            bookButton.addMouseListener(new MouseAdapter() { // 마우스 이벤트 리스너 추가
                @Override
                public void mouseEntered(MouseEvent e) { // 마우스가 버튼 위에 올라갈 때
                    bookButton.setToolTipText(statusText); // 툴팁으로 대출 상태 표시
                }
            });

            // 버튼 스타일 조정
            bookButton.setHorizontalTextPosition(JButton.CENTER); // 텍스트를 수평 중앙 정렬
            bookButton.setVerticalTextPosition(JButton.BOTTOM); // 텍스트를 이미지 아래로 배치
            bookButton.setToolTipText(title + " - " + author); // 기본 툴팁 설정 (제목 - 저자)
            bookButton.addActionListener(e -> { // 버튼 클릭 이벤트 추가
            	borrowBook(title); // 도서 대출 처리
            	refreshBookPanel(); // UI 새로고침
                try {
                    new LibraryAdminExcel().downloadExcel(); // 엑셀 파일 다운로드 시도
                } catch (IOException e1) {
                    e1.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
                }
            });

            // 패널에 버튼 추가
            bookImagePanel.add(bookButton); // 도서 이미지 패널에 버튼 추가
        }

        bookImagePanel.revalidate(); // 패널 레이아웃 재검증
        bookImagePanel.repaint(); // 패널 다시 그리기
    }

    private String getImagePath(String s) { // 도서 제목에 따라 이미지 경로를 반환하는 메서드
        switch (s) { // 입력된 제목에 따라 분기
        case "이것이자바다": // 제목이 "이것이자바다"일 때
            return "C:/javasrc/Library/image/이것이 자바다.jpg"; // 해당 이미지 경로 반환
        case "자바의 정석": // 제목이 "자바의 정석"일 때
            return "C:/javasrc/Library/image/자바의 정석.jpg"; // 해당 이미지 경로 반환
        case "미움받을 용기": // 제목이 "나의 투쟁"일 때
            return "C:/javasrc/Library/image/미움받을 용기.jpg"; // 해당 이미지 경로 반환
        case "젊은 베르테르의 슬픔": // 제목이 "젊은 베르테르의 슬픔"일 때
            return "C:/javasrc/Library/image/젊은 베르테르의 슬픔.jpg"; // 해당 이미지 경로 반환
        case "혼진 살인사건": // 제목이 "혼진 살인사건"일 때
            return "C:/javasrc/Library/image/혼진 살인사건.jpg"; // 해당 이미지 경로 반환
        case "인플레이션에서살아남기": // 제목이 "인플레이션에서살아남기"일 때
            return "C:/javasrc/Library/image/인플레이션에서살아남기.jpg"; // 해당 이미지 경로 반환
        case "소년이 온다": // 제목이 "소년이 온다"일 때
            return "C:/javasrc/Library/image/소년이 온다.jpg"; // 해당 이미지 경로 반환
        case "수학의 정석": // 제목이 "수학의 정석"일 때
            return "C:/javasrc/Library/image/수학의정석.jpg"; // 해당 이미지 경로 반환
        default: // 정의되지 않은 제목일 때
            return ""; // 빈 문자열 반환 (기본 이미지 없음)
        }
    }

    private void refreshBookPanel() { // 도서 패널을 새로고침하는 메서드
        // bookImagePanel을 새로 고침
        JPanel bookImagePanel = (JPanel) ((JScrollPane) bookPanel.getComponent(1)).getViewport().getView(); // 스크롤 패널에서 도서 이미지 패널 가져오기
        loadBooks(bookImagePanel); // 도서 목록 다시 로드
    }

    // 이미지 크기 조정 메서드
    private ImageIcon resizeImage(String path, int width, int height) { // 이미지 크기를 조정하는 메서드
        ImageIcon icon = new ImageIcon(path); // 주어진 경로로 ImageIcon 생성
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // 이미지 크기 조정
        return new ImageIcon(image); // 조정된 이미지를 새로운 ImageIcon으로 반환
    }

    private void logoutAction() { // 로그아웃 처리 메서드
        loggedInUser = null; // 로그인 사용자 정보 초기화
        JOptionPane.showMessageDialog(this, "로그아웃 되었습니다."); // 로그아웃 메시지 표시
        cardLayout.show(mainPanel, "Login"); // 로그인 화면으로 전환
    }

    private void showSignupScreen(BookDAO dao) { // 회원가입 화면을 표시하는 메서드
        JDialog signupDialog = new JDialog(this, "회원가입", true); // 모달 다이얼로그 생성
        signupDialog.setSize(300, 250); // 다이얼로그 크기 설정
        signupDialog.setLayout(new GridLayout(4, 2)); // 4행 2열 그리드 레이아웃 설정

        JLabel usernameLabel = new JLabel("아이디:"); // "아이디:" 라벨 생성
        JTextField usernameField = new JTextField(); // 아이디 입력 필드 생성
        JLabel passwordLabel = new JLabel("비밀번호:"); // "비밀번호:" 라벨 생성
        JPasswordField passwordField = new JPasswordField(); // 비밀번호 입력 필드 생성
        JLabel confirmPasswordLabel = new JLabel("비밀번호 확인:"); // "비밀번호 확인:" 라벨 생성
        JPasswordField confirmPasswordField = new JPasswordField(); // 비밀번호 확인 입력 필드 생성
        JButton signupButton = new JButton("회원가입"); // "회원가입" 버튼 생성

        signupDialog.add(usernameLabel); // 다이얼로그에 아이디 라벨 추가
        signupDialog.add(usernameField); // 다이얼로그에 아이디 입력 필드 추가
        signupDialog.add(passwordLabel); // 다이얼로그에 비밀번호 라벨 추가
        signupDialog.add(passwordField); // 다이얼로그에 비밀번호 입력 필드 추가
        signupDialog.add(confirmPasswordLabel); // 다이얼로그에 비밀번호 확인 라벨 추가
        signupDialog.add(confirmPasswordField); // 다이얼로그에 비밀번호 확인 입력 필드 추가
        signupDialog.add(new JLabel()); // 빈 공간용 라벨 추가
        signupDialog.add(signupButton); // 다이얼로그에 회원가입 버튼 추가

        signupButton.addActionListener(e -> { // 회원가입 버튼에 액션 리스너 추가
            String username = usernameField.getText().trim(); // 입력된 아이디 가져오기 (공백 제거)
            String password = new String(passwordField.getPassword()); // 입력된 비밀번호 가져오기
            String confirmPassword = new String(confirmPasswordField.getPassword()); // 입력된 비밀번호 확인 가져오기

            // 아이디와 비밀번호가 비어있는지 확인
            if (username.isEmpty() || password.isEmpty()) { // 아이디 또는 비밀번호가 비어있으면
                JOptionPane.showMessageDialog(signupDialog, "아이디와 비밀번호를 입력하세요."); // 경고 메시지 표시
                return; // 메서드 종료
            }

            // 비밀번호와 비밀번호 확인이 일치하는지 확인
            if (!password.equals(confirmPassword)) { // 비밀번호가 일치하지 않으면
                JOptionPane.showMessageDialog(signupDialog, "비밀번호가 일치하지 않습니다."); // 경고 메시지 표시
                return; // 메서드 종료
            }

            // 아이디 중복 검사
            if (dao.Username(username)) { // 아이디가 이미 존재하면
                JOptionPane.showMessageDialog(signupDialog, "이미 존재하는 아이디입니다."); // 경고 메시지 표시
                return; // 메서드 종료
            }

            // 회원가입 처리 (DB에 저장)
            if (dao.libraryUsers(username, password)) { // 회원가입 성공 시
                JOptionPane.showMessageDialog(signupDialog, "회원가입 성공"); // 성공 메시지 표시
                signupDialog.dispose(); // 다이얼로그 닫기
            } else {
                JOptionPane.showMessageDialog(signupDialog, "회원가입 실패. 다시 시도하세요."); // 실패 메시지 표시
            }
        });
        try {
            new LibraryAdminExcel().downloadExcel(); // 엑셀 파일 다운로드 시도
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
        }
        signupDialog.setVisible(true); // 회원가입 다이얼로그 표시
    }

    @Override
    public void updateBookTable() { // 테이블 데이터를 갱신하는 메서드 (BookManagement 인터페이스 구현)
        if (defaultTablemodel == null) { // 테이블 모델이 null이면
            defaultTablemodel = new DefaultTableModel(); // 새 DefaultTableModel 생성
        }
        if (bookDAO == null) { // BookDAO가 null이면 (불필요한 조건, 생성자에서 초기화됨)
            bookDAO = new BookDAO(); // 새 BookDAO 객체 생성
        }

        // 테이블 초기화
        defaultTablemodel.setRowCount(0); // 테이블 행 수를 0으로 초기화 (기존 데이터 제거)

        // 책 목록을 가져오기
        List<BookDTO> books = bookDAO.getAllBooks(); // DB에서 도서 목록 가져오기
        if (books == null) { // 도서 목록이 null이면
            books = new ArrayList<>(); // 빈 ArrayList로 초기화
        }

        // 테이블에 책 데이터 추가
        for (BookDTO book : books) { // 각 도서에 대해 반복
            defaultTablemodel.addRow( // 테이블에 새 행 추가
                    new Object[] { book.getTitle(), book.getAuthor(), book.getBorrowed() == 1 ? "대출 중" : "대출 가능" }); // 제목, 저자, 대출 상태 추가
        }
    }

    private void showAddBookDialog() { // 도서 추가 다이얼로그를 표시하는 메서드
        JTextField titleField = new JTextField(20); // 도서 제목 입력 필드 생성
        JTextField authorField = new JTextField(20); // 저자 입력 필드 생성
        JTextField statusField = new JTextField(1); // 대출 상태 입력 필드 생성 (1자 제한)
        statusField.setText("0"); // 기본값으로 "0" (대출 가능) 설정

        JPanel panel = new JPanel(); // 다이얼로그용 패널 생성
        panel.add(new JLabel("도서 제목:")); // "도서 제목:" 라벨 추가
        panel.add(titleField); // 제목 입력 필드 추가
        panel.add(new JLabel("저자:")); // "저자:" 라벨 추가
        panel.add(authorField); // 저자 입력 필드 추가

        int option = JOptionPane.showConfirmDialog(this, panel, "도서 추가", JOptionPane.OK_CANCEL_OPTION); // 확인/취소 다이얼로그 표시

        if (option == JOptionPane.OK_OPTION) { // 확인 버튼을 눌렀을 때
            String title = titleField.getText(); // 입력된 제목 가져오기
            String author = authorField.getText(); // 입력된 저자 가져오기
            int status = Integer.parseInt(statusField.getText()); // 입력된 상태를 정수로 변환

            BookDTO newBook = new BookDTO(title, author, status, status); // 새 BookDTO 객체 생성
            addBook(newBook); // 도서 추가 메서드 호출
        }
    }

    private void borrowBookAction() { // 도서 대출 액션을 처리하는 메서드
        String title = JOptionPane.showInputDialog(this, "대출할 도서 제목을 입력하세요."); // 대출할 도서 제목 입력 다이얼로그 표시
        borrowBook(title); // 도서 대출 메서드 호출
    }

    private void returnBookAction() { // 도서 반납 액션을 처리하는 메서드
        String title = JOptionPane.showInputDialog(this, "반납할 도서 제목을 입력하세요."); // 반납할 도서 제목 입력 다이얼로그 표시
        returnBook(title); // 도서 반납 메서드 호출
    }

    public void PopularBooks() { // 인기 도서를 표시하는 메서드
        List<BookDTO> popularBooks = bookDAO.getPopularBooks(); // DB에서 인기 도서 목록 가져오기
        StringBuilder message = new StringBuilder("인기 도서 Top 5\n"); // 메시지 빌더 생성
        if (popularBooks.isEmpty()) { // 인기 도서가 없으면
            message.append("인기 도서가 없습니다."); // 메시지에 추가
        } else {
            for (int i = 0; i < popularBooks.size(); i++) { // 인기 도서 목록 반복
                BookDTO book = popularBooks.get(i); // 현재 도서 객체 가져오기
                message.append((i + 1) + ". " + book.getTitle() + " - " + book.getAuthor() + " (대출 횟수: "
                        + book.getBorrowCount() + ")\n"); // 도서 정보 추가
            }
        }

        JOptionPane.showMessageDialog(this, message.toString(), "인기 도서", JOptionPane.INFORMATION_MESSAGE); // 인기 도서 메시지 표시
    }

    private void searchBooksAction() { // 도서 검색 액션을 처리하는 메서드
        // 새로운 JDialog 생성
        JDialog searchDialog = new JDialog(this, "도서 검색", true); // 모달 검색 다이얼로그 생성
        searchDialog.setSize(300, 300); // 다이얼로그 크기 설정
        searchDialog.setLocationRelativeTo(this); // 다이얼로그를 창 중앙에 배치

        // 레이아웃 및 패널 설정
        JPanel searchPanel = new JPanel(); // 검색 패널 생성
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS)); // 세로 박스 레이아웃 설정

        // 검색 입력 폼 구성
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 입력 패널 생성, 왼쪽 정렬
        JLabel searchLabel = new JLabel("도서 제목 또는 저자."); // "도서 제목 또는 저자:" 라벨 생성
        JTextField searchField = new JTextField(20); // 검색어 입력 필드 생성
        inputPanel.add(searchLabel); // 입력 패널에 라벨 추가
        inputPanel.add(searchField); // 입력 패널에 검색 필드 추가

        // 검색 버튼
        JButton searchButton = new JButton("검색"); // "검색" 버튼 생성
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 버튼을 수평 중앙 정렬

        // 결과 표시 영역 (초기에는 빈 텍스트)
        JTextArea resultArea = new JTextArea(5, 30); // 검색 결과를 표시할 텍스트 영역 생성
        resultArea.setEditable(false); // 텍스트 영역 수정 불가 설정
        JScrollPane scrollPane = new JScrollPane(resultArea); // 텍스트 영역을 스크롤 가능하게 감쌈

        // 검색 버튼 클릭 시 처리
        searchButton.addActionListener(e -> { // 검색 버튼에 액션 리스너 추가
            String keyword = searchField.getText(); // 입력된 검색어 가져오기
            if (keyword.isEmpty()) { // 검색어가 비어있으면
                JOptionPane.showMessageDialog(this, "검색어를 입력해주세요."); // 경고 메시지 표시
                return; // 메서드 종료
            }

            // 도서 검색 처리
            List<BookDTO> foundBooks = searchBooks(keyword); // 검색어로 도서 검색

            if (foundBooks.isEmpty()) { // 검색 결과가 없으면
                resultArea.setText("검색 결과가 없습니다."); // 텍스트 영역에 메시지 설정
            } else {
                StringBuilder result = new StringBuilder(); // 검색 결과 빌더 생성
                for (BookDTO book : foundBooks) { // 검색된 도서 반복
                    result.append("제목: ").append(book.getTitle()).append("\n저자: ").append(book.getAuthor())
                            .append("\n\n"); // 도서 정보 추가
                }
                resultArea.setText(result.toString()); // 텍스트 영역에 결과 표시
            }
        });

        // 팝업에 구성 요소 추가
        searchPanel.add(inputPanel); // 검색 패널에 입력 패널 추가
        searchPanel.add(searchButton); // 검색 패널에 검색 버튼 추가
        searchPanel.add(scrollPane); // 검색 패널에 결과 스크롤 패널 추가

        // JDialog에 패널 추가
        searchDialog.add(searchPanel); // 다이얼로그에 검색 패널 추가

        searchDialog.setVisible(true); // 검색 다이얼로그 표시
    }

    @Override
    public void addBook(BookDTO book) { // 도서를 추가하는 메서드 (BookManagement 인터페이스 구현)
        try {
            boolean success = bookDAO.addBook(book); // DB에 도서 추가 시도
            if (success) { // 추가 성공 시
                JOptionPane.showMessageDialog(null, "도서가 추가되었습니다."); // 성공 메시지 표시
                updateBookTable(); // 테이블 데이터 갱신
            } else {
                JOptionPane.showMessageDialog(null, "도서 추가에 실패했습니다."); // 실패 메시지 표시
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
            JOptionPane.showMessageDialog(null, "도서 추가 중 오류가 발생했습니다."); // 오류 메시지 표시
        }
        try {
            new LibraryAdminExcel().downloadExcel(); // 엑셀 파일 다운로드 시도
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
        }
    }

    @Override
    public void borrowBook(String title) { // 도서를 대출하는 메서드 (BookManagement 인터페이스 구현)
        // 대출할 책을 찾기
        BookDTO book = bookDAO.getBookByTitle(title); // 제목으로 도서 객체 가져오기

        if (book != null) { // 도서가 존재하면
            if (book.getBorrowed() == 0) { // 대출 가능 상태일 때
                book.setBorrowed(1); // 대출 상태로 변경
                book.setBorrowCount(book.getBorrowCount() + 1); // 대출 횟수 증가

                // 대출 상태 업데이트
                boolean success = bookDAO.updateBook(title, true); // DB에서 대출 상태로 업데이트
                if (success) { // 업데이트 성공 시
                    JOptionPane.showMessageDialog(this, "도서 대출이 완료되었습니다."); // 성공 메시지 표시
                    updateBookTable(); // 테이블 데이터 갱신
                } else {
                    JOptionPane.showMessageDialog(this, "도서 대출에 실패했습니다."); // 실패 메시지 표시
                }
            } else {
                JOptionPane.showMessageDialog(this, "이 도서는 이미 대출 중입니다."); // 이미 대출 중인 경우 메시지 표시
            }
        } else {
            JOptionPane.showMessageDialog(this, "해당 도서를 찾을 수 없습니다."); // 도서가 없으면 메시지 표시
        }
    }

    @Override
    public void returnBook(String title) { // 도서를 반납하는 메서드 (BookManagement 인터페이스 구현)
        // 반납할 책을 찾기
        BookDTO book = bookDAO.getBookByTitle(title); // 제목으로 도서 객체 가져오기
        if (book != null && book.getBorrowed() == 1) { // 도서가 존재하고 대출 중이면
            book.setBorrowed(0); // 대출 상태를 반납으로 변경

            // 반납 상태로 책을 업데이트
            boolean success = bookDAO.updateBook(title, false); // DB에서 반납 상태로 업데이트

            if (success) { // 업데이트 성공 시
                JOptionPane.showMessageDialog(this, "도서 반납이 완료되었습니다."); // 성공 메시지 표시
                updateBookTable(); // 테이블 데이터 갱신
                try {
                    new LibraryAdminExcel().downloadExcel(); // 엑셀 파일 다운로드 시도
                } catch (IOException e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
                }
            } else {
                JOptionPane.showMessageDialog(this, "도서 반납에 실패했습니다."); // 실패 메시지 표시
            }
        } else {
            JOptionPane.showMessageDialog(this, "이 도서는 대출되지 않았습니다."); // 대출되지 않은 경우 메시지 표시
        }
    }

    @Override
    public List<BookDTO> searchBooks(String keyword) { // 도서를 검색하는 메서드 (BookManagement 인터페이스 구현)
        // 제목 또는 저자로 도서 검색
        List<BookDTO> books = bookDAO.searchBooks(keyword); // DB에서 검색어로 도서 목록 가져오기
        if (books.isEmpty()) { // 검색 결과가 없으면
            JOptionPane.showMessageDialog(this, "검색 결과가 없습니다."); // 메시지 표시
        }
        return books; // 검색된 도서 목록 반환
    }
}