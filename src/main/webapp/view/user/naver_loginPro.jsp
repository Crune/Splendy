<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>

<link rel='stylesheet' href='/js/jquery.ajax-cross-origin.min.js'>
<title>Insert title here</title>
</head>
<body>
	<script>
		//location.href='https://nid.naver.com/oauth2.0/token?client_id=iM6rVSYTz69Duz5F99Mp&client_secret=xP3KOGIDSB&grant_type=authorization_code&state=${state}&code=${code}';
		var url = 'https://nid.naver.com/oauth2.0/token?client_id=iM6rVSYTz69Duz5F99Mp&client_secret=xP3KOGIDSB&grant_type=authorization_code&state=${state}&code=${code}';
		var author;
		var str;
		$.getJSON(url, function(data){
			author = data;
			console.log(author);

			$.ajax({
			    url: "https://openapi.naver.com/v1/nid/me",
			    headers: {
			        'Authorization':'Bearer ' + author.access_token,
			    },
			    method: 'POST',
			    dataType: 'json',
			    success: function(data){
			      console.log('succes: '+data);
			    }
			  });
		    }); 
		console.log(str);	
		
	</script>
</body>
</html>