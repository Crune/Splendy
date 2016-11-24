
window.onload = function(){
	
	$.ajax({
		url:"/prof/iconModify",		
		success:function(data){
			console.log(data);
			$("#userProfIcon").attr("src", data);			
		},
		error:function(request, status, error){
			console.log('modify icon fail');
		}
	});

	$("#btn_logout").on('click', function () {
		document.location.href='/user/logout';
		var revokeAllScopes = function() {
			  auth2.disconnect();
			}
	})
	
	$("#btn_modify").on('click', function () {
		$('#modify_modal').modal('show');
	})
	
	$("#btn_modify_prc").on('click', function() {
		modifyRequest();
		$('#modify_modal').modal('hide');
		if($("#nickname") != '' ){
			$('.lobby_text_nick').html($('#nickname').prop('value'));
		}
		$('#password').val('');
	})
	
	$("#icon1").click(function (){
		changeIcon('icon1');
	});
	$("#icon2").click(function (){
		changeIcon('icon2');
	});
	$("#icon3").click(function (){
		changeIcon('icon3');
	});
	$("#icon4").click(function (){
		changeIcon('icon4');
	});
	
}

function modifyRequest() {
	$.ajax({
		url:'/user/modify_suc',
		type:'post',
		data:$("#modal_form").serialize(),
		success:function(data){
			console.log(data);
			alert("정보를 수정하였습니다.");
		},error:function(request,status,error){
			alert("정보 수정에 실패하였습니다. 다시 시도해주세요.");
		}
	})
} 

var changeIcon = function(icon_src){
	var icon = {"icon" : $("#"+icon_src).attr("src")};
	$("#userProfIcon").attr("src", "/img/top_"+icon_src+".png");
	
	$.ajax({
		url:"/prof/iconChange",
		type:'post',
		data:icon,
		success:function(data){
			alert("아이콘 변경 완료!");
		},
		error:function(request, status, error){
			alert("아이콘 변경 실패!");
		}
	});
}
