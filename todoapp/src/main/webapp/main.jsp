<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="todo.TodoDao" %>
<%@ page import="todo.TodoDto" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="value" scope="request" value="kang"></c:set>	<!-- el표기법 -->
<!--모두 작성한 다음에 jstl, el로 변환-->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#head{
	padding:40px;
	display:flex;
	flex-direction:row;	/*자식 요소 수평 배치*/
}
#head h1{
	margin:5px;
	/*글자 회전*/
	-ms-transform: rotate(-35deg);
	-webkit-transform:rotate(-35deg);
	transform:rotate(-35deg);
}
#head .todo_create_btn{
	position:absolute;
	right:30px;
	background-color:#00BFFF;
	color:white;/*글자색*/
	border: none;
	outline:0;
	border-radius:4px;/*모서리 깎기*/
	cursor:pointer;
}

.task_boxes {
	margin-left:110px;
}
.task_boxes{
	display:flex;
	flex-direction:row;
}
.task_box{
	margin:4px;
}
.task_title{
	width:300px; 
	padding:25px;
	font-size:13px;
	background-color: #0B3B24;
	text-align:center;
	color:white;
	font-size:15px;
}

.todo_Content{
	width:100%px;
	height:100px;
	padding: 5px;
	background-color:#ADD8E6;
	border:3px solid #FAEBD7;
	margin-bottom: 5px;
	
}

.content_title{
margin:3px 1px;
}


.content_btn .cont-box-pseudo{
	width:0px;
	height:0px;
	border-top: 10px solid transparent;
	border-bottom:10px solid transparent;
	border-left:10px solid cornflowerblue;
}

.content_btn{
	margin-left:40px;
}
</style>
</head>
<header id="head">
	<h1>나의 해야할 일들</h1>
	<form class="todo_create_btn" action="/todo/TodoFormServlet" method="post">
	<!-- method 속성 생략시 디폴트인 get으로 인식 -->
		<input type="submit" value="새로운 TODO 등록">
	</form>	
</header>
<body id="body">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script>
function moveCard(key_id,todo_type){
		//var msg=confirm("카드를 이동합니다."+key_id+" "+todo_type);
		//if(msg == true){
			updateType(key_id, todo_type);
		//}else{
			//return false;
		//}
	}

function updateType(key_id, todo_type){	//이 함수만으로도 해당 객체에 대한 json받아옴
	var httpRequest;
	var param="key_id="+key_id+"&todo_type="+todo_type;
	httpRequest = new XMLHttpRequest();
	httpRequest.onreadystatechange = changeCardState;	//대상과의 통신이 끝났을 때 호출되는 이벤트
	httpRequest.open("POST","UpdateTypeServlet",true);
	httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
	httpRequest.send(param);
	
	function changeCardState(){
		if(httpRequest.readyState === XMLHttpRequest.DONE){	//통신상태
			if(httpRequest.status === 200){	//통신 결과
				//json데이터 파싱하여 원하는 값 가져오기
				var rs_id =JSON.parse(httpRequest.responseText).id;
				var rs_name=JSON.parse(httpRequest.responseText).name;
				var rs_regDate=JSON.parse(httpRequest.responseText).regDate;
				var rs_sequence=JSON.parse(httpRequest.responseText).sequence;
				var rs_title=JSON.parse(httpRequest.responseText).title;
				var rs_type = JSON.parse(httpRequest.responseText).type;
				if(rs_type == 'done'){
					var str = '<div>';
					str+="<div class='todo_Content'><h4 class='content_title'>"+rs_title;
					str+="</h4><span class='content_regDate' id='${item.getId()}'>등록날짜:"+rs_regDate;
					str+="</span><span class='content_Name' ></span><span class='content_prior' >, 우선순위"+rs_sequence;
					str+="</span></div>";
					$('#'+todo_type+'_box').children('#'+key_id).remove();
					$('#'+rs_type+'_box').append(str);
				}else{
					var str = '<div>';
					str+="<div class='todo_Content'><h4 class='content_title'>"+rs_title;
					str+="</h4><span class='content_regDate' id='${item.getId()}'>등록날짜:"+rs_regDate;
					str+="</span><span class='content_Name' ></span><span class='content_prior' >, 우선순위"+rs_sequence;
					str+="</span><button class='content_btn' onclick='moveCard(";
					str+=rs_id+","+rs_type+")'><div class='cont-box-pseudo'></div></button>";
					str+='</div>';
					$('#'+todo_type+'_box').children('#'+key_id).remove();
					$('#'+rs_type+'_box').append(str);

				}
			
				
				//document.getElementById(key_id).remove();
				//document.getElementById(rs_type+'_box').insertAdjacentHTML('beforeend',str);
			}else{
				alert('request에 뭔가 문제가 있어요.')
			}
		}
		
	}

}

</script>		
		<div class="task_boxes">
			<div class="task_box" id="todo_box">
				<h1 id="todo_title" class="task_title">TODO</h1>
				<c:forEach items="${list}" var="item">
					<c:if test="${item.getType() == 'todo'}"> <!--java 클래스 메소드 호출 기능-->
					<div class="todo_Content" id="${item.getId() }">
					<h4 class="content_title">${item.getTitle()}</h4>
					<span class="content_regDate" >등록날짜:${item.getRegDate()}</span>
					<span class="content_Name" ></span>
					<span class="content_prior" >, 우선순위${item.getSequence() }</span>
					<button class="content_btn" onclick="moveCard(${item.getId() },'${item.getType() }')"><div class="cont-box-pseudo"></div></button>
					
					</div>
					</c:if>
				</c:forEach>
			</div>
			<div class="task_box" id="doing_box">
				<h1 id="doing_title" class="task_title">DOING</h1>
					<c:forEach items="${list}" var="item">
					<c:if test="${item.getType() == 'doing'}"> <!--java 클래스 메소드 호출 기능-->
					<div class="todo_Content" id="${item.getId() }">
					<h4 class="content_title">${item.getTitle()}</h4>
					<span class="content_regDate" >등록날짜:${item.getRegDate()}</span>
					<span class="content_Name" ></span>
					<span class="content_prior" >, 우선순위${item.getSequence() }</span>
					<button class="content_btn" onclick="moveCard(${item.getId() },'${item.getType() }')"><div class="cont-box-pseudo"></div></button>
					
					</div>
					</c:if>
				</c:forEach>
			</div>
			<div class="task_box" id="done_box">
				<h1 id="done_title" class="task_title">DONE</h1>
					<c:forEach items="${list}" var="item">
					<c:if test="${item.getType() == 'done'}">
						<div class="todo_Content" id="${item.getId() }">
						<h4 class="content_title">${item.getTitle()}</h4>
						<span class="content_regDate">등록날짜:${item.getRegDate()}</span>
						<span class="content_Name" ></span>
						<span class="content_prior" >, 우선순위${item.getSequence() }</span>	
						</div>
					</c:if>
					</c:forEach>
			</div>

		</div>
		
</body>
</html>

	