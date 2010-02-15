// EZPZ Tooltip v1.0; Copyright (c) 2009 Mike Enriquez, http://theezpzway.com; Released under the MIT License
(function($){
	var cur_timer = null, cur_open = null;
	$.fn.ezpz_tooltip = function(options){
		var settings = $.extend({}, $.fn.ezpz_tooltip.defaults, options);
		
		return this.each(function() {
			if(settings.template)
			{
				var	content = $(settings.template);
				var ac = $("#" + getContentId(this.id));
				ac.after(content);
				$(".cgh", content).append(ac);
				content.attr("id", ac.attr("id"));
				ac.attr("id", "");
				ac.removeClass("tooltip-content");
			}
			else
			{
				var	content = $("#" + getContentId(this.id));
			}

			var targetMousedOver = $(this).mouseover(function(){
/*				if(cur_open !== null){
					clearTimeout(cur_timer);
					$('#'+getContentId(cur_open)).css('display','none'); 
					cur_open = null;
				}*/
				settings.beforeShow(content, $(this));
			}).mousemove(function(e){
				contentInfo = getElementDimensionsAndPosition(content);
				targetInfo = getElementDimensionsAndPosition($(this));
				contentInfo = $.fn.ezpz_tooltip.positions[settings.contentPosition](contentInfo, e.pageX, e.pageY, settings.offset, targetInfo);
				contentInfo = keepInWindow(contentInfo);
				
				content.css('top', contentInfo['top']);
				content.css('left', contentInfo['left']);

				//If tip isn't visible, then show after delay
				if(!content.is(':visible'))
				{
					setTimeout(function(){settings.showContent(content)}, settings.delay)
				}

			});
			
			if (settings.stayOnContent && this.id != "") {
				$("#" + this.id + ", #" + getContentId(this.id)).mouseover(function(){
					if(cur_open == this.id)
						clearTimeout(cur_timer);
					content.css('display', 'block');
				}).mouseout(function(){
					if(cur_open == this.id)
						clearTimeout(cur_timer);
					if(this.id == getContentId(this.id)){
						cur_open = this.id;
						cur_timer = setTimeout(function(){content.css('display','none'); settings.afterHide(); },1500);
					}
				});
			}
			else {
				targetMousedOver.mouseout(function(){
					settings.hideContent(content);
					settings.afterHide();
				})
			}
			
		});
		
		function getContentId(targetId){
			if (settings.contentId == "") {
				var name = targetId.split('-')[0];
				var id = targetId.split('-')[2];
				return name + '-content-' + id;
			}
			else {
				return settings.contentId;
			}
		};
		
		function getElementDimensionsAndPosition(element){
			var height = element.outerHeight(true);
			var width = element.outerWidth(true);
			var top = $(element).offset().top;
			var left = $(element).offset().left;
			var info = new Array();
			
			// Set dimensions
			info['height'] = height;
			info['width'] = width;
			
			// Set position
			info['top'] = top;
			info['left'] = left;
			
			return info;
		};
		
		function keepInWindow(contentInfo){
			var windowWidth = $(window).width();
			var windowTop = $(window).scrollTop();
			var output = new Array();
			
			output = contentInfo;
			
			if (contentInfo['top'] < windowTop) { // Top edge is too high
				output['top'] = windowTop;
			}
			if ((contentInfo['left'] + contentInfo['width']) > windowWidth) { // Right edge is past the window
				output['left'] = windowWidth - contentInfo['width'];
			}
			if (contentInfo['left'] < 0) { // Left edge is too far left
				output['left'] = 0;
			}
			
			return output;
		};
	};
	
	$.fn.ezpz_tooltip.positionContent = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = mouseY - offset - contentInfo['height'];
		contentInfo['left'] = mouseX + offset;
		
		return contentInfo;
	};
	
	$.fn.ezpz_tooltip.positions = {
		aboveRightFollow: function(contentInfo, mouseX, mouseY, offset, targetInfo) {
			contentInfo['top'] = mouseY - offset - contentInfo['height'];
			contentInfo['left'] = mouseX + offset;

			return contentInfo;
		}
	};
	
	$.fn.ezpz_tooltip.defaults = {
		contentPosition: 'aboveRightFollow',
		stayOnContent: false,
		offset: 10,
		contentId: "",
		delay:0,
		beforeShow: function(content){},
		showContent: function(content) {
			content.show();
			$("*", content).show();
		},
		hideContent: function(content){
			content.hide();
		},
		afterHide: function(){}
	};
	
})(jQuery);

// Plugin for different content positions. Keep what you need, remove what you don't need to reduce file size.

(function($){
	$.fn.ezpz_tooltip.positions.aboveFollow = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = mouseY - offset - contentInfo['height'];
		contentInfo['left'] = mouseX - (contentInfo['width'] / 2);
		
		return contentInfo;
	};
	
	$.fn.ezpz_tooltip.positions.rightFollow = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = mouseY - (contentInfo['height'] / 2);
		contentInfo['left'] = mouseX + offset;
		
		return contentInfo;
	};
	
	$.fn.ezpz_tooltip.positions.belowRightFollow = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = mouseY + offset;
		contentInfo['left'] = mouseX + offset;
		
		return contentInfo;
	};
	
	$.fn.ezpz_tooltip.positions.belowFollow = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = mouseY + offset;
		contentInfo['left'] = mouseX - (contentInfo['width'] / 2);
		
		return contentInfo;
	};
	
	$.fn.ezpz_tooltip.positions.aboveStatic = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = targetInfo['top'] - offset - contentInfo['height'];
		contentInfo['left'] = (targetInfo['left'] + (targetInfo['width'] / 2)) - (contentInfo['width'] / 2);
		
		return contentInfo;
	};
	
	$.fn.ezpz_tooltip.positions.rightStatic = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = (targetInfo['top'] + (targetInfo['height'] / 2)) - (contentInfo['height'] / 2);
		contentInfo['left'] = targetInfo['left'] + targetInfo['width'] + offset;
		
		return contentInfo;
	};
	
	$.fn.ezpz_tooltip.positions.belowStatic = function(contentInfo, mouseX, mouseY, offset, targetInfo) {
		contentInfo['top'] = targetInfo['top'] + targetInfo['height'] + offset;
		contentInfo['left'] = (targetInfo['left'] + (targetInfo['width'] / 2)) - (contentInfo['width'] / 2);
		
		return contentInfo;
	};
	
})(jQuery);