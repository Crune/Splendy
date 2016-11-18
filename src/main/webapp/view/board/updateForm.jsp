<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/view/board/include/boardinclude.jspf" %>
</head>
<body>
    <form id="frm">
        <table class="board_view">
            <colgroup>
                <col width="15%"/>
                <col width="35%"/>
                <col width="15%"/>
                <col width="35%"/>
            </colgroup>
            <caption>게시글 상세</caption>
            <tbody>
                <tr>
                    <th scope="row">글 번호</th>
                    <td>
                        ${article.at_id }
                        <input type="hidden" id="at_id" name="at_id" value="${article.at_id }">
                    </td>
                    <th scope="row">조회수</th>
                    <td>${article.at_readcount }</td>
                </tr>
                <tr>
                    <th scope="row">작성자</th>
                    <td>${article.u_id }</td>
                    <th scope="row">작성시간</th>
                    <td>${article.at_reg_date }</td>
                </tr>
                <tr>
                    <th scope="row">제목</th>
                    <td colspan="3">
                        <input type="text" id="at_subject" name="at_subject" class="wdp_90" value="${article.at_subject }"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="view_text">
                        <textarea rows="20" cols="100" title="내용" id="at_content" name="at_content">${article.at_content }</textarea>
                    </td>
                </tr>
            </tbody>
        </table>
    </form>
     
    <a href="#this" class="btn" id="list">목록으로</a>
    <a href="#this" class="btn" id="update">저장하기</a>
    <a href="#this" class="btn" id="delete">삭제하기</a>
     
    <%@ include file="/view/board/include/boardinclude.jspf" %>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#list").on("click", function(e){ //목록으로 버튼
                e.preventDefault();
                fn_openBoardList();
            });
             
            $("#update").on("click", function(e){ //저장하기 버튼
                e.preventDefault();
                fn_updateBoard();
            });
             
         
        });
         
        function fn_openBoardList(){
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/sample/openBoardList.do' />");
            comSubmit.submit();
        }
         
        function fn_updateBoard(){
            var comSubmit = new ComSubmit("frm");
            comSubmit.setUrl("<c:url value='/sample/updateBoard.do' />");
            comSubmit.submit();
        }
         
       
    </script>
</body>
</html>