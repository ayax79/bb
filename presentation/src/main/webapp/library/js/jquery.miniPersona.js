/**
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 * Author:  Matthew Sweet
 * Last Modified Date: $DateTime: $
 *
 */

;(function($) {

	var MiniPersona = {

		_init: function() {
			var ref = this;
			if(this._getData("personaType") == null) {
				alert("Persona type unknown");
				return false;
			}
			this.showTimer = null;
			this.hideTimer = null;
			this.container = $('<div class="profile-container" />');
			this._bindEvents();
			$(window).bind("scroll.mp", function() {
				if(!ref.container.is(':visible')) return;
				ref._placePersona();
			});
		},

		_bindEvents: function() {
			var ref = this;
			this.element.mouseover(function() {
				clearTimeout(ref.hideTimer);
				if(!ref.container.is(':visible'))
				{
					ref.showTimer = setTimeout(function() {
						ref._addProfile();
					}, ref._getData("openDelay"));
				}
			});
			this.element.mouseout(function() {
				if(ref._getData("lockOpen")) return;
				clearTimeout(ref.showTimer);
				ref.hideTimer = setTimeout(function() {
					ref._removeContainer();
				}, ref._getData("closeDelay"));
			});

		},

		/**
		 * This will initiate a delayed a close on the persona in situations
		 * where we have locked the dialog open to allow clicks in other dialogs
		 * otherwise user will have to mousever/out of the element to close
		 */
		_clearLockAndClose: function() {
			var ref = this;
			this._setData("lockOpen", false);
			clearTimeout(this.showTimer);
			this.hideTimer = setTimeout(function() {
				ref._removeContainer();
			}, 2000);
		},

		_addProfile: function() {
			var ref = this;
			clearTimeout(this.hideTimer);
			$("body").append(this.container);
			this.container.hide();
			this.container.mouseover(function() {
				clearTimeout(ref.hideTimer);
			});
			this.container.mouseout(function() {
				ref.hideTimer = setTimeout(function() {
					ref._removeContainer();
				});
			}, ref._getData("closeDelay"));

			this._loadPersona();
		},

		_getActionUrl: function() {
			return bb.urls.user.miniProfile + "?identifier=" + this._getGuid();
		},

		_loadPersona: function() {
			var ref = this;
			bb.trackPageView(this._getActionUrl());
			this.container.load(this._getActionUrl(), {'ch':new Date().getTime()}, function() {
				ref._bindPersonaEvents();
				ref._placePersona();
				ref.container.show();
			});
		},

		_placePersona: function() {
			var position = this.element.parent().offset();
			var xOffset = 0;
			var yOffset = 0; //account for shadow
			this.container.css("top", (position.top + yOffset + "px")).css("left", (position.left + xOffset) + "px");
			if(position.top + this.container.height() > $(window).height() + $(window).scrollTop()) {
				this.container.css("top", ($(window).height() - this.container.height() + $(window).scrollTop()) + "px");
			}
			if(position.left + this.container.width() > $(window).width() + $(window).scrollLeft()) {
				this.container.css("left", ($(window).width() - this.container.width() + $(window).scrollLeft()) + "px");
			}
		},

		_bindPersonaEvents: function() {
			ref = this;
			var ref = this;
			$('*[class*=timeago]', this.container).timeago();
			/* BIND MEMBER PERSONA EVENTS */
			if(this._getData("personaType") == "member") {

				$(".friend_link").friendButton({
					beforeSet: function() {
						ref._setData("lockOpen", true);
					},
					set: function() {
						ref._clearLockAndClose();
					},
					cancelset:function() {
						ref._clearLockAndClose();
					}

				});
				$(".follow_link").followButton({
					beforeSet: function() {
						ref._setData("lockOpen", true);
					},
					set: function() {
						ref._clearLockAndClose();
					},
					cancelset:function() {
						ref._clearLockAndClose();
					}
				});

				$(".compose-link", this.container).click(function() {


					if($(this).is(".blocked")) {
						$.bbDialog.alert(bb.resources.messaging.blockedMessage);
						ref._clearLockAndClose();
						return false;
					}
					
					if($(this).is(".unvouched")) {
						$.bbDialog.alert(bb.resources.messaging.unVouchedMessage);
						ref._clearLockAndClose();
						return false;
					}


					var guid =  $(this).attr("href").split("#").pop();
					var composer = bb.messaging.compose({userIdentifier:guid});
					composer.bind("messageSent", function() {
						//w00t.
					});
				});

//				$(".donkey_link").blockButton({
//					beforeSet: function() {
//						ref._setData("lockOpen", true);
//					},
//					set: function() {
//						ref._clearLockAndClose();
//					}
//				});
			}
		},

		_getGuid: function() {
			return this.element.attr("href").split("/").pop();
		},
		
		_removeContainer: function() {
			if(this._getData("lockOpen")) return;
			this.container.unbind().empty().remove();
			this.destroy();
		}

	};

	$.widget("ui.miniPersona", MiniPersona);
	$.ui.miniPersona.getter = "";
	$.ui.miniPersona.defaults = {
		personaType:null,
		lockOpen:false,
		openDelay:600,
		closeDelay:300,
		position:"right"
	};


})(jQuery);		