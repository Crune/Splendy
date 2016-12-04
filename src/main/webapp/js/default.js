function wrapWindowByMask() {

	var maskHeight = $(document).height();
	var maskWidth = $(window).width();

	$('#mask').css({
		'width' : maskWidth,
		'height' : maskHeight
	});

	if (true) {
        $('#mask').fadeTo("slow", 0.8);
        $('.window').show();
    } else {
        $('#mask').fadeTo("slow", 0.0);
        $('#mask').hide();
        $('.window').hide();
	}
}

function setControll(boolVar) {
    if (boolVar) {
        $('#mask, .window').hide();
    } else {
        wrapWindowByMask();
    }
}
/*
$(document).ready(function() {

	$('.openMask').click(function(e) {
		e.preventDefault();
		wrapWindowByMask();
	});

	$('.window .close').click(function(e) {
		e.preventDefault();
		$('#mask, .window').hide();
	});
	$('#mask').click(function() {
		$(this).hide();
		$('.window').hide();
	});
});
*/