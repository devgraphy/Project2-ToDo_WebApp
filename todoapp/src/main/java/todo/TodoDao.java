package todo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.lang.AutoCloseable;

public class TodoDao {
	private static String dburl = "jdbc:mysql://localhost:3306/todo_DB?serverTimezone=UTC";
	private static String dbUser = "connectuser";
	private static String dbpasswd = "connect123!@#";
	
	//todo테이블에 입력
	public int addTodo(TodoDto todo) {
		String sql="insert into todo (id, name, regDate, sequence, title, type) values (?,?,?,?,?,?)";
		try {	//JDBC Driver loading 예외 처리
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//JDBC 연결~결과 반환까지 예외처리
		try (Connection conn = DriverManager.getConnection(dburl,dbUser,dbpasswd);				
				PreparedStatement ps= conn.prepareStatement(sql);){	//try with resource 구문
	
			//SQL에 인자 지정
				ps.setLong(1, todo.getId());
				ps.setString(2, todo.getName());
				ps.setString(3, todo.getRegDate());
				ps.setInt(4, todo.getSequence());
				ps.setString(5, todo.getTitle());
				ps.setString(6, todo.getType());
			
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
	//todo테이블에 수정
	public int updateTodo(TodoDto todo) {
		return 0;
	}
	
	//todo테이블에 조회
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
					long id=rs.getLong("id");//[점검 쟁점]rs.getId()로는 안 되나?
					String name = rs.getString("name");
					String regDate = rs.getString("regDate");
					int sequence = rs.getInt("sequence");
					String title = rs.getString("title");
					String type = rs.getString("type");
					
					TodoDto dto = new TodoDto(id, name, regDate, sequence, title, type);
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
