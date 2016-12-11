$(document).ready(function() {
	$(".cn").click(function(){
        $("#coin_modal").show();
	});
	$("#btn_coin_esc").click(function(){
        $("#coin_modal").hide();
	});
	
	// 모달 창 여는 버튼에 이벤트 걸기
	$("article img").on('click', function() {
        $("#card_modal").show(); // 모달창 보여주기
        $("img#select_card").attr("src", this.src);
	    setControll(false);
	});

	$("#card_modal ul li:last-child img").click(function() {
	    setControll(true);
        $("#card_modal").hide(); // 모달창 감추기
	});
});

