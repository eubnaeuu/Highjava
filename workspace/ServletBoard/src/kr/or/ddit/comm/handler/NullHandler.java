package kr.or.ddit.comm.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NullHandler implements CommandHandler{

	@Override
	public boolean isRedirect(HttpServletRequest req) {
		return false;
	}

	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}

	
	
}
