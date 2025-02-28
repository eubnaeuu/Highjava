package kr.or.ddit.basic;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * 자카르타 프로젝트의 fileupload 모듈을 이용한 파일업로드 예제
 */
public class UploadServlet2 extends HttpServlet{

	
	private static final String UPLOAD_DIR = "upload_files";   // workspace내에 있는 upload_files 폴더가 생성되고 여기에 파일 저장됨.
	// 메모리 임계크기(이 크기가 넘어가면 레파지토리 위치에 임시파일로 저장됨)
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  //3mb
	// 파일 1개당 최대 크기
	private static final long MAX_FILE_SIZE = 1024 * 1024 * 40;
	// 요청 파일 최대 크기
	private static final long MAX_REQUEST_SIZE = 1024 * 1024 * 50;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(ServletFileUpload.isMultipartContent(req)) { // 인코딩 타입이 multipart/form-data 인지 확인(맞다면..)
			
			// 폼필드 데이터 저장용...
			Map<String, String> formMap = new HashMap<String, String>();
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(MEMORY_THRESHOLD); // 임계크기 설정한듯(넘어가면 임시파일로 저장되고 그렇지 않으면 서버 메모리에 저장)
			factory.setRepository(new File(System.getProperty("java.io.tmpdir"))); // 임시파일로 저장될때의 저장경로, 지금 임시폴더에 저장되게끔 되어있음
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			upload.setFileSizeMax(MAX_FILE_SIZE); // 1개당 업로드사이즈
			upload.setSizeMax(MAX_REQUEST_SIZE); // 총 업로드사이즈
			
			// 웹 애플리케이션 루트 디렉토리 기준 업로드 경로 설정하기   : webcontent를 기준으로 적는 루트 경로는 virtual path다. getRealPath 실제 경로를 알려준다( 파일IO 작업을 할시에는 실제경로를 알아야 하기에)
			//											File.separator : 경로상 쓰는 /, \ 구분자를 os에 따라 알아서 적용해주는 상수임
			String uploadPath = getServletContext().getRealPath("")+ File.separator + UPLOAD_DIR; 
			File uploadDir = new File(uploadPath);
			if(!uploadDir.exists()) {
				uploadDir.mkdir(); // upload_files 폴더 생성하는 것.
			}
			
			List<FileItem> formItems;
			
			try {
				formItems = upload.parseRequest(req); // req를 파싱시작
			if(formItems != null && formItems.size() > 0) {
				for(FileItem item : formItems) {
					if(!item.isFormField()) { // 파일인 경우...
					// 전체경로를 제외한 파일명만 추출하기
						String fileName = new File(item.getName()).getName();
						String filePath = uploadPath + File.separator + fileName;
								File storeFile = new File(filePath);
								item.write(storeFile);
								req.setAttribute("message", "업로드 완료됨. => 파일명 : " + fileName);
					} else {// 폼 데이터인 경우..
						// 폼필드의 값이 한글인 경우에는 해당 문자열을 적절히 변환을 
						// 해주어야 한다
						// 방법1. value = new String(fileItem.getString().getBytes("8859-1"), "UTF-8");
						// 방법2. value = fileItem.getStirng("UTF-8");
						formMap.put(item.getFieldName() // '이름', '주소' 등등 
								,item.getString("UTF-8")); // 그에 해당하는 값들
					}
				}
			}
		} catch(Exception e) {
			req.setAttribute("message", "예외발생: " + e.getMessage());
			e.printStackTrace();
		}
			for (Entry<String, String> entry : formMap.entrySet()) {
				System.out.println("파라미터명 : " + entry.getKey());
				System.out.println("파라미터값 : "+ entry.getValue());
			}
			resp.setContentType("text/html");
			resp.getWriter().print("업로드 완료됨.!!!");
		}
	}
}
