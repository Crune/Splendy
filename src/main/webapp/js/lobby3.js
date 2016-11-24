
window.onload = function(){
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