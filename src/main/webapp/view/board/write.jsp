<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
<title>�Խ���</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="script.js"></script>
</head>

   
<body bgcolor="${bodyback_c}">  
<center><b>�۾���</b>
<br>
<form method="post" name="writeform" action="/bbs/writePro">
<input type="hidden" name="num" value="${at_id}">
<input type="hidden" name="ref" value="${at_reply}">
<input type="hidden" name="re_step" value="${at_re_step}">
<input type="hidden" name="re_level" value="${at_re_level}">

<table width="400" border="1" cellspacing="0" cellpadding="0"  align="center">
   <tr>
    <td align="right" colspan="2" bgcolor="${value_c}">
	    <a href="/study/ch19/list.do"> �۸��</a> 
   </td>
   </tr>

  <tr>
    <td  width="70"  bgcolor="${value_c}" align="center" >�� ��</td>
    <td  width="330">
	<c:if test="${at_id == 0}">
       <input type="text" size="40" maxlength="50" name="at_subject"></td>
	</c:if>
	<c:if test="${at_id != 0}">
	   <input type="text" size="40" maxlength="50" name="at_subject" value="[�亯]"></td>
	</c:if>
  </tr>  
  <tr>
    <td  width="70"  bgcolor="${value_c}" align="center" >�� ��</td>
    <td  width="330" >
     <textarea name="at_content" rows="13" cols="40"></textarea> </td>
  </tr>
  <tr>
    <td  width="70"  bgcolor="${value_c}" align="center" >��й�ȣ</td>
    <td  width="330" >
     <input type="password" size="8" maxlength="12" name="at_pass"> 
	 </td>
  </tr>
<tr>      
 <td colspan=2 bgcolor="${value_c}" align="center"> 
  <input type="submit" value="�۾���" >  
  <input type="reset" value="�ٽ��ۼ�">
  <input type="button" value="��Ϻ���" OnClick="window.location='/study/ch19/list.do'">
</td></tr></table>    
</form>      
</body>
</html>      
