<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>facebook_Login</title>
</head>
<body>

<form action="/connect/facebook" method="POST">
    <input type="hidden" name="scope" value="user_posts" />
    <div class="formInfo">
        <p>You aren't connected to Facebook yet. <br/>
        Click the button to connect this application with your Facebook account.</p>
    </div>
    <p>
        <button type="submit">Connect to Facebook</button>
    </p>
</form>

</body>
</html>