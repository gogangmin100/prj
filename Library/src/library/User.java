//package library; // 패키지 선언 (library 패키지에 속함)
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class User { // 사용자(User) 클래스를 정의
//    private String id; // 사용자의 아이디를 저장하는 변수
//    private String password; // 사용자의 비밀번호를 저장하는 변수
//    private String name; // 사용자의 이름을 저장하는 변수
//    private boolean loggedIn; // 로그인 상태를 나타내는 변수 (true: 로그인됨, false: 로그아웃됨)
//
//    // 사용자 정보를 저장할 HashMap (아이디를 키로 사용)
//    private static Map<String, User> userDatabase = new HashMap<>();
//
//    // 생성자: 객체 생성 시 아이디, 비밀번호, 이름을 설정하고, 초기 로그인 상태를 false로 설정
//    public User(String id, String password, String name) {
//        this.id = id; // 입력받은 id 값을 클래스의 id 변수에 저장
//        this.password = password; // 입력받은 password 값을 클래스의 password 변수에 저장
//        this.name = name; // 입력받은 name 값을 클래스의 name 변수에 저장
//        this.loggedIn = false; // 초기 로그인 상태는 false (로그아웃 상태)
//    }
//
//    // 사용자 ID를 반환하는 메서드
//    public String getId() {
//        return id;
//    }
//
//    // 사용자 이름을 반환하는 메서드
//    public String getName() {
//        return name;
//    }
//
//    // 사용자가 입력한 비밀번호가 저장된 비밀번호와 일치하는지 확인하고 로그인 처리
//    public boolean login(String password) {
//        if (this.password.equals(password)) { // 입력한 비밀번호와 저장된 비밀번호가 같은지 비교
//            this.loggedIn = true; // 비밀번호가 일치하면 로그인 상태를 true로 변경
//            return true; // 로그인 성공 시 true 반환
//        }
//        return false; // 비밀번호가 일치하지 않으면 false 반환 (로그인 실패)
//    }
//
//    // 로그아웃 메서드: 로그인 상태를 false로 변경
//    public void logout() {
//        this.loggedIn = false; // 로그아웃 시 로그인 상태를 false로 변경
//    }
//
//    // 현재 로그인 상태를 반환하는 메서드
//    public boolean isLoggedIn() {
//        return loggedIn; // 현재 로그인 상태(true/false)를 반환
//    }
//
//    // 회원가입 처리 메서드 (기존 사용자 목록에 추가할 수 있음)
//    public static boolean registerUser(String id, String password, String name) {
//        if (!userDatabase.containsKey(id)) { // 이미 존재하는 아이디인지 체크
//            User newUser = new User(id, password, name);
//            userDatabase.put(id, newUser); // 사용자 목록에 추가
//            return true; // 회원가입 성공
//        }
//        return false; // 이미 존재하는 아이디일 경우 회원가입 실패
//    }
//
//    // 아이디로 사용자를 찾는 메서드
//    public static User findUserById(String id) {
//        return userDatabase.get(id); // 아이디에 해당하는 사용자 반환
//    }
//
//    // 비밀번호를 반환하는 메서드
//    public String getPassword() {
//        return password; // 비밀번호 반환
//    }
//}
