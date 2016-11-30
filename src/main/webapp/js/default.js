function wrapWindowByMask() {

	var maskHeight = $(document).height();
	var maskWidth = $(window).width();

	$('#mask').css({
		'width' : maskWidth,
		'height' : maskHeight
	});

	$('#mask').fadeTo("slow", 0.8);

	$('.window').show();
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


function newMap() {
    var map = {};
    map.value = {};
    map.getKey = function(id) {
        return "k_"+id;
    };
    map.put = function(id, value) {
        var key = map.getKey(id);
        map.value[key] = value;
    };
    map.contains = function(id) {
        var key = map.getKey(id);
        if(map.value[key]) {
            return true;
        } else {
            return false;
        }
    };
    map.get = function(id) {
        var key = map.getKey(id);
        if(map.value[key]) {
            return map.value[key];
        }
        return null;
    };
    map.remove = function(id) {
        var key = map.getKey(id);
        if(map.contains(id)){
            map.value[key] = undefined;
        }
    };

    return map;
}