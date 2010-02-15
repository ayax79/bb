;(function($) {

	var BBDialog = {

		_init: function() {
			$.bbDialog.dialogs.push(this);
			var ref = this;
			var template = $($.bbDialog.windowTemplate);
			if(this._getData("dialogClass") != "")
			{
				template.addClass(this._getData("dialogClass"));
			}
			template.appendTo(document.body);
			$(".dialog-content", template).append(this.element);
			this.container = this.getContainer();
			this.bringToFront();
			$(window).bind("resize scroll", function() {
				ref.element.trigger("browserResize");
				if(!ref._getData('draggable')) ref.center();
			});
			this.container.click(function() {
				ref.bringToFront();
			});
			this.center();
			if(this._getData('draggable')) this.container.draggable({ handle: 'h1' });
			this.setTitle();
			if(this._getData('modal')) this.createOverlay();

			if(this._getData('hideButtons')) {
				$(".button-panel", this.container).hide();
			}

			this._createButtons();

			if(!this._getData("autoOpen")) {
				this.close();
			}

			$.bbDialog.focusedDialog = this;
			this._trigger("onopen",this,this);
		},

		//PRIVATE METHODS

		_overlayExists: function() {
			 return ($("#bboverlay").length > 0);
		},

		_createOverlay: function() {
			$("body").append($('<div id="bboverlay"></div>'));
		},

		_removeOverlay: function() {
			 $("#bboverlay").remove();
		},

		_removeOverlayIfNoModals: function() {
			var pos =  $.inArray(this, $.bbDialog.dialogs);
			if(pos != -1)
			{
				$.bbDialog.dialogs.splice(pos, 1);
			}
			var hasModals = false;
			$.each($.bbDialog.dialogs, function(i,e) {
				 if(e._getData('modal')) hasModals = true;
			});

			if(!hasModals) this._removeOverlay();
		},

		_createButtons:function() {
			var ref = this;
			$("button .button-panel", this.container).unbind();
			$(".button-panel", this.container).empty();
			$.each(this._getData('buttons'), function(i,fn) {

				var button = $('<button><span>' + i + '</span></button>');
				button.addClass("bbbutton").addClass("grey27");
				$(".button-panel", ref.container).append(button);
				button.click(function() {
					fn.call(ref);
				});
			});
		},

		//PUBLIC METHODS

		remove:function() {
			this.container.unbind().empty().remove();
			this.destroy();
			this._removeOverlayIfNoModals();
			this._trigger("onclose");
		},

		close:function() {
			this.container.hide();
			this._removeOverlayIfNoModals();
			this._trigger("onclose");
			this._isOpen = false;
		},

		open:function() {
			if(this._isOpen) { return };
			if(this._getData('modal')) this.createOverlay();
			this.bringToFront();
			this.container.show();
			this._trigger("onopen");
			this._isOpen = true;
			$.bbDialog.focusedDialog = this;
		},

		createOverlay: function() {
			if(!this._overlayExists()) {
				this._createOverlay();
			}
		},

		moveTo: function(x, y) {
			if(x) this.container.css({left: x});
			if(y) this.container.css({top: y});
			
		},

		horizCenter: function() {
			if(this._getData("autoCenterX")) this.container.css({left: $(window).width() / 2 - this.container.width() / 2});
		},

		vertCenter: function() {
			var top = $(window).height() / 2 - this.container.height() / 2 + $(window).scrollTop();
			if(this._getData("autoCenterY")) {
				this.container.css({ top: top});
			}
		},

		center: function() {
			this.horizCenter();
			this.vertCenter();
		},

		getContainer: function() {
			return $(this.element.parents(".bb-dialog-container"));
		},

		getContentContainer: function() {
			return $(".dialog-content", this.container);
		},

		isOpen:function() {
			return this._isOpen;
		},

		bringToFront:function() {
			if($.bbDialog.focusedDialog != this && $.bbDialog.focusedDialog != null)
			{
				$.bbDialog.focusedDialog._trigger("onblur");
				$.bbDialog.focusedDialog = this;
			}
			this.container.css("z-index", $.bbDialog.zstack++);
		},

		setTitle:function() {
			$("h1>span", this.container).html(this._getData('title') + "&nbsp;");
		},

		buttons:function(buttons) {
			this._setData("buttons", buttons);
			this._createButtons();
		}
	};

	$.widget("ui.bbDialog", BBDialog);

	$.ui.bbDialog.getter = "getContainer, getContentContainer, isOpen";

	$.ui.bbDialog.defaults = {
		close:function() {},
		modal:false,
		autoOpen:true,
		autoCenterX:true,
		autoCenterY:true,
		draggable:true,
		title:"",
		buttons:{},
		onclose:function(){},
		onopen:function(){},
		onblur:function(){},
		hideButtons:false,
		preventRemove:false
	};

	$.bbDialog = {
		zstack:3000,
		dialogs:[],
		focusedDialog:null,
		dialogClass:"",
		windowTemplate: '<div class="dialog-rb-white bb-dialog-container"><h1 class="dkgrey2"><span class="title"></span></h1><div class="dialog-cl"><div class="dialog-cr"><div class="dialog-content"></div><div class="button-panel"></div></div></div><div class="dialog-bl"><span class="dialog-br">&nbsp;</span></div></div>'
	};

	$.bbDialog.closeAll = function() {
		$.each($.bbDialog.dialogs, function() {
			if(this._getData("preventRemove")) {
				this.close();
			} else {
				this.remove();	
			}
		});

	};

	//Static method for creating an alert dialog
	$.bbDialog.alert = function(msg, options) {
		var d = $('<div class="clearfix"><span class="alert-icon">&nbsp;</span><p style="padding-top:5px;margin-left:50px;"><strong>' + msg + '</strong></p></div>');
		$("body").prepend(d);
		var o = $.extend({}, {
			title:'Alert',
			modal:true,
			buttons:{
				'OK':function() {
					this.remove();
				}
			},
			draggable:false
		}, options);
		d.bbDialog(o);
		return d;
	};

	//Static method for creating an info dialog
	$.bbDialog.info = function(msg, options) {
		var d = $('<div class="clearfix"><span class="info-icon">&nbsp;</span><p style="padding-top:5px;margin-left:50px;">' + msg + '</p></div>');
		$("body").prepend(d);
		var o = $.extend({}, {
			title:'Info',
			modal:true,
			buttons:{
				'OK':function() {
					this.remove();
				}
			},
			draggable:false
		}, options);
		d.bbDialog(o);
		return d;
	};

	//Static method for creating an info dialog
	$.bbDialog.confirm = function(msg, trueFn, falseFn) {
		var d = $('<div class="clearfix"><p style="padding-top:5px;margin-left:50px;">' + msg + '</p></div>');
		$("body").prepend(d);
		var o = $.extend({}, {
			title:'Info',
			modal:true,
			buttons:{
				'Cancel': function() {
					if($.isFunction(falseFn)) {
						falseFn.call(this);
					}
					this.remove();
				},
				'OK':function() {
					if($.isFunction(trueFn)) {
						trueFn.call(this);
					};
					this.remove();
				}
			},
			draggable:false
		});
		d.bbDialog(o);
		return d;
	};

	//temp hax
	$.bbDialog.ageConfirm = function(msg, trueFn, falseFn) {
		var d = $('<div><p style="margin-left:50px;">' + msg + '</p></div>');
		$("body").prepend(d);
		var o = $.extend({}, {
			title:'Info',
			modal:true,
			buttons:{
				'Decline': function() {
					if($.isFunction(falseFn)) {
						falseFn.call(this);
					}
					this.remove();
				},
				'Accept':function() {
					if($.isFunction(trueFn)) {
						trueFn.call(this);
					};
					this.remove();
				}
			},
			draggable:false
		});
		d.bbDialog(o);
		return d;
	};

})(jQuery);



