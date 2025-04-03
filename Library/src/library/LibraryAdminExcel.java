package library;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class LibraryAdminExcel {

    public static void main(String[] args) {
        try {
            // 엑셀 파일 다운로드 메서드 호출
            new LibraryAdminExcel().downloadExcel();
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
        }
    }

    public void downloadExcel() throws IOException {
        // BookDAO 객체 생성하여 데이터베이스에서 책 목록 가져오기
        BookDAO bookDAO = new BookDAO();
        List<BookDTO> bookList = bookDAO.getAllBooks(); // DB에서 책 목록 가져오기 
        List<BookDTO> userList = bookDAO.getLibraryUsers(); // 회원 목록 가져오는 메서드 추가 필요

        // 새로운 워크북 생성 (엑셀 파일)
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("책 목록");
        Sheet sheet2 = workbook.createSheet("계정 목록");
        int rowNo1 = 0;
        int rowNo2 = 0;

        // 헤더 생성
        Row headerRow = sheet.createRow(rowNo1++);
        headerRow.createCell(0).setCellValue("책 제목");
        headerRow.createCell(1).setCellValue("저자");
        headerRow.createCell(2).setCellValue("대출 상태");
        headerRow.createCell(3).setCellValue("대출 횟수");
        
        // DB에서 가져온 데이터로 엑셀에 데이터 입력
        for (BookDTO book : bookList) {
            Row row = sheet.createRow(rowNo1++);
            row.createCell(0).setCellValue(book.getTitle());
            row.createCell(1).setCellValue(book.getAuthor());
            row.createCell(2).setCellValue(book.getBorrowed() == 1 ? "대출 중" : "대출 가능");
            row.createCell(3).setCellValue(book.getBorrowCount());
        }
        
        Row headerRow2 = sheet2.createRow(rowNo2++);
        headerRow2.createCell(0).setCellValue("아이디");
        headerRow2.createCell(1).setCellValue("비밀번호");
        
 
        // 회원 목록 가져와서 엑셀에 저장
        for (BookDTO user : userList) {
            Row row2 = sheet2.createRow(rowNo2++);
            row2.createCell(0).setCellValue(user.getId());  // ID 저장
            row2.createCell(1).setCellValue(user.getPassword());  // 비밀번호 저장
        }


        

        // 파일을 저장할 경로 설정
        String folderPath = "C:\\javasrc\\Library\\excel\\";
        File folder = new File(folderPath);
    
        // 저장할 파일 경로 (확장자 .xls로 설정)
        String filePath = folderPath + "bookList.xlxs";

        // 파일을 서버에 저장
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("엑셀 파일이 성공적으로 저장되었습니다: " + filePath);  // 성공 메시지 출력
    }

	
	}

