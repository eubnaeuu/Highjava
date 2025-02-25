package kr.or.ddit.board.vo;
/**
 * 
 * DB 테이블에 있는 컬럼을 기준으로 데이터를 객체화한 클래스이다.
 * 
 * @author PC-09
 * <p>
 * DB테이블의 '컬럼'이 이 클래스의 '멤버변수'가 된다<br>
 * DB테이블의 컬럼과 클래스의 멤버변수를 매핑하는 역할을 수행한다<br>
 * </p>
 */
public class BoardVO {
	private String boardNo;
	private String boardWriter;
	private String boardTitle;
	private String boardContent;
	private String boardDate;
	
	public String getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(String boardNo) {
		this.boardNo = boardNo;
	}
	public String getBoardWriter() {
		return boardWriter;
	}
	public void setBoardWriter(String boardWriter) {
		this.boardWriter = boardWriter;
	}
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public String getBoardContent() {
		return boardContent;
	}
	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}
	public String getBoardDate() {
		return boardDate;
	}
	public void setBoardDate(String boardDate) {
		this.boardDate = boardDate;
	}
	@Override
	public String toString() {
		return "BoardVO [boardNo=" + boardNo + ", boardWriter=" + boardWriter + ", boardTitle=" + boardTitle
				+ ", boardContent=" + boardContent + ", boardDate=" + boardDate + "]";
	}
	
	
	
	
	
}
