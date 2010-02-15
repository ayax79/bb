//
var bb = {
	page:{},
	widgets:{},
	urls:{},
	activity:{},
	utils:{}
};

$.extend(bb.utils, {
	imgPoll:function(e, obj) {
		var $img = $(obj);
		if(!$img.data("attempts")) {
			$img.data("attempts", 0);
		}
		$img.data("attempts", $img.data("attempts") + 1);
		if($img.data("attempts") < 400) {
			setTimeout(function() {
				$img[0].src = $img[0].src + "?ch=" + new Date().getTime();
			}, 1000);
		}
	}
});