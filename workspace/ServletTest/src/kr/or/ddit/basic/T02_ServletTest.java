package kr.or.ddit.basic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class T02_ServletTest extends HttpServlet{
	/**
	  
	 서블릿 동작 방식에 대하여..
	 
	 
1. 사용자(클라이언트)가 URL을 클릭하면 HTTP Request를 서블릿 컨테이너로 전송(요청)

2. 컨테이너는 web.xml에 정의된 url패턴을 확인하여 어느 서블릿을 통해 처리해야 할지를 검색

3. Servlet Container는 요청을 처리할 개별 스레드 객체를 생성 
	해당 서블릿 객체의 service()메서드를 호출
	(HttpServletRequest 및 HttpServlietResponse 객체를 생성하여 파라미터로 넘겨줌

4. service()메서드는 메서드 타입을 체크하여 적절한 메서드를 호출
	(doGet, doPost, doPut, doDelete 등)

5. 요청 처리가 완료되면, (HttpServletRequest 및 HttpServletResponse 객체는 소멸)

6. 컨테이너로부터 서블릿이 제거되는 경우에 destroy()메서드가 호출
	*/
	@Override 
	// thread가 객체를 새로 생성하게 되면 그때 tomcat이 자동으로 HttpSevlet Request ,HttpServletResponse  객체를 만들어줌
	// HttpSevlet Request ,HttpServletResponse  객체는 요청처리가 끝나면 모두 사라진다(비연결성) => 5번항목
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//Post 방식으로 넘어오는 Body데이터를 인코딩처리함. get방식은 톰캣의 URIEncoding 설정을 이용함
		// 반드시 request에서 값을 가져오기 '전'에 먼저 설정해야 적용됨
//		req.setCharacterEncoding("utf-8"); 
		
		// 요청정보로부터 name값을 가져옴
		String name = req.getParameter("name");
		System.out.println("name => " + name);
		
		// 응답메시지 인코딩 설정(Content-Type의 charset = UTF-8)
//		resp.setCharacterEncoding("UTF-8");
		
		// 응답 메시지의 컨텐츠 타입 설정
		resp.setContentType("text/plain"); // 단순한 text파일
		
		// 실제 수행할 로직(기능)이 시작되는 부분...
		PrintWriter out = resp.getWriter(); // 문자기반
		// 이미지를 보내고 싶은 경우 바이트 기반으로 보내면 됨 : resp.getOutputStream();
		out.println("name =>" + name);
		out.println("Context Path(Root) =>" +req.getContextPath());
		out.println("Servlet Path =>" +req.getServletPath());
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

//		이번 예제에서 url에서 직접 데이터를 조회해본다(Get방식)
//		경로?/name=홍길동
//		url에 한글을 넣게되면 실제로는 깨지므로 web.xml에서 urlencoding을 utf-8로 바꿔줘야함.
//		URIEncoding="UTF-8" (port번호 수정하는 그곳	)
