<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- �ΰ����� �׸� -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- �������� �ּ�ȭ�� �ֽ� �ڹٽ�ũ��Ʈ -->
<script>
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	<script>
		function naverLogin(){
			
			location.href = "https://nid.naver.com/oauth2.0/authorize?client_id=iM6rVSYTz69Duz5F99Mp&response_type=code&redirect_uri=http://127.0.0.1/login/naver_loginPro&state=${state}";
		}
	</script>
	<style>
		div{
			margin : 20px;
		}
	</style>
</head>
<body>
	<div class="btn-group" role="group" aria-label="...">
		<button type="button" class="btn btn-default" onClick="naverLogin()">naverLogin</button>
	</div>
</body>

</html>