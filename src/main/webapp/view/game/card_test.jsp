<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <script type='text/javascript' src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
	<script type='text/javascript' src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
	<script type='text/javascript' src="/webjars/handlebars/4.0.5/handlebars.js"></script>
    
    <script id="entry-template" type="text/x-handlebars-template">
    <div class="entry">
        <h1>{{title}}</h1>
        <div class="body">
            {{body}}
        </div>
    </div>
    </script>
    <script>
        var source = $("#entry-template").html(); // ID 값으로 템플릿을 가져옴
        var template = Handlebars.compile(source); // Handlebars.compile을 이용하여 컴파일

        var context = {title: "My New Post", body: "This is my first post!"}; 
        var html = template(context); // context를 파라미터로 전달
        
        
    </script>
</head>

<body>
    
</body>

</html>