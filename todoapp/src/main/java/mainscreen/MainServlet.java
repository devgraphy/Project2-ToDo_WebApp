package mainscreen;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import todo.TodoDao;
import todo.TodoDto;
/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public MainServlet() {
        super();
    }

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//TodoDao dao = new TodoDao();
		//List<TodoDto> list = dao.getTodos(); //list와 arrayList차이:: https://galgum.tistory.com/18
		//request객체에 정보를 담는 코드
		//list객체 request객체에 어떻게 담지? 그냥 넘겨짐 object는 모든 것을 포함하는 객체라는 것을 알 수 있음
		//req.setAttribute("todolist", list );
		
		String destination = "main.jsp";
		RequestDispatcher requestDispatcher = req.getRequestDispatcher(destination);
		requestDispatcher.forward(req, resp); 
	}

}
