package todo;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.lang.AutoCloseable;
public class TodoDao {
	private static String dburl = "jdbc:mysql://localhost:3306/todo_DB?serverTimezone=UTC&characterEncoding=utf8";
	private static String dbUser = "connectuser";
	private static String dbpasswd = "connect123!@#";
	//1. todo테이블에 입력
	public int addTodo(TodoDto todo) {
		String sql="insert into todo (name, regDate, sequence, title, type) values (?,?,?,?,?)";
		try {	//JDBC Driver loading 예외 처리
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//JDBC 연결~결과 반환까지 예외처리
		try (Connection conn = DriverManager.getConnection(dburl,dbUser,dbpasswd);				
				PreparedStatement ps= conn.prepareStatement(sql);){	//try with resource 구문
	
			//form전송으로 받은 request 객체에 담겨진 Dto사용->SQL에 인자 지정
				//날짜 받기
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date time= new Date();
				String time1 = format1.format(time);
			
				
				ps.setString(1, todo.getName());
				ps.setInt(3, todo.getSequence());
				ps.setString(4, todo.getTitle());
				
				ps.setString(2, time1);
				ps.setString(5, "todo");
			
				try {
					ps.execute();
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;	//수정 필요한 부분
	}
	
	
	
	//2. todo테이블에 수정
	public int updateType(int key_id, String todo_type) {
		String sql="update todo set type=? where id=?";
		try {	//JDBC Driver loading 예외 처리
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//JDBC 연결~결과 반환까지 예외처리
		try (Connection conn = DriverManager.getConnection(dburl,dbUser,dbpasswd);				
				PreparedStatement ps= conn.prepareStatement(sql);){	//try with resource 구문

				if("todo".contentEquals(todo_type)) {	//== 으로 인식 못한다.
					ps.setString(1,"doing");
				}
				else if("doing".contentEquals(todo_type)) {
					ps.setString(1, "done");
				}else {
					System.out.println("type in else:" + todo_type);
				}
				ps.setInt(2, key_id);
				
				try {
					ps.executeUpdate();
				} catch(Exception e) {
					e.printStackTrace();
				}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	
	public  TodoDto getSingleTodo(int key_id){
		TodoDto dto = new TodoDto();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//3. 실행해서 값 받아와야지
		String sql="select id,name,regDate,sequence,title,type from todo where id=?";
		try(Connection conn = DriverManager.getConnection(dburl, dbUser,dbpasswd);
				PreparedStatement ps=conn.prepareStatement(sql);){
			ps.setInt(1, key_id);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {	//받을 객체가 하나든 여러개든 while문을 써줘야 한다. -의미 되새기기
					int id = rs.getInt("id");
					String name = rs.getString("name");
					int sequence = rs.getInt("sequence");
					String title = rs.getString("title");
					String type = rs.getString("type");
					String[] regDateArray = rs.getString("regDate").split(" ");	//띄어쓰기 기준으로 문자열 자르기
					String regDate = regDateArray[0];
					dto = new TodoDto(id, title, name, sequence, type, regDate);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//4. 넘겨야지
		return dto;

	}
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	//3. todo테이블에 조회
	public  List<TodoDto> getTodos(){
		//1. sql 작성해야지
		List<TodoDto> list = new ArrayList<>();
		//2. jdbc 연결해야지, 예외처리도 포함해야지
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//3. 실행해서 값 받아와야지
		String sql="select id,name,regDate,sequence,title,type from todo;";
		try(Connection conn = DriverManager.getConnection(dburl, dbUser,dbpasswd);
				PreparedStatement ps=conn.prepareStatement(sql);){
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					int sequence = rs.getInt("sequence");
					String title = rs.getString("title");
					String type = rs.getString("type");
					String[] regDateArray = rs.getString("regDate").split(" ");	//띄어쓰기 기준으로 문자열 자르기
					String regDate = regDateArray[0];
					TodoDto dto = new TodoDto(id, title, name, sequence, type, regDate);
					list.add(dto);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//4. 넘겨야지
		return list;

	}
}
