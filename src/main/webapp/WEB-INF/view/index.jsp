<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form>
	id : <input type="text" id="name" name="name"/>
	</form>
	<br/>
	total-result : <p id="results"></p>
	<br/>
	<br/>
	id : <p id="rst-id"></p><br/>
	pw : <p id="rst-pw"></p><br/>
	age : <p id="rst-age"></p><br/>
	name : <p id="rst-name"></p><br/>
	reg : <p id="rst-reg"></p><br/>
	<input type="button" id="execute" value="실행" />
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script>
		$('#execute').click(function() {
			$.ajax({
				url : './test.do',
	            type:'post',
	            data:$('form').serialize(),
				dataType : 'json',
				success : function(data) {
					var str = '';
					for ( var name in data) {
						str += '<li>' + data[name] + '</li>';
					}
					$('#results').html('<ul>' + str + '</ul>');
					$('#rst-id').html(data.id);
					$('#rst-pw').html(data.pw);
					$('#rst-age').html(data.age);
					$('#rst-name').html(data.name);
					$('#rst-reg').html(data.reg);
				}
			})
		})
	</script>
</body>
</html>