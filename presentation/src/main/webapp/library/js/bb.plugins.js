/*
Required: blackbox_js.jspf
Collection of jQuery plugins for Blackbox
Copyright 2009 Blackbox, Inc.
 */

/************************************************************************
 * CustomCheckbox
 ************************************************************************/
;(function($) {

	var CustomCheck = {

		_init: function() {
			var ref = this;
			this.psuedoCheckbox = $('<a href="#" class="' + this._getData("uiClass") + '"><span>&nbsp;</span></a>');
			this.psuedoCheckbox.click(function() {
				ref._toggleState();
				ref._setState();
				ref.element.trigger("customClick");
				return false;
			});

			//Add click handler to allow clicks on labels
			this.element.bind("click", function() {
				ref._setState();
			});

			this.element.after(this.psuedoCheckbox).hide();
			this._setState();
		},

		_isChecked: function() {
			return this.element.is(":checked");
		},

		_toggleState: function() {
			if(this.element.is(":checked")) {
				this.element.removeAttr("checked");
			} else {
				this.element.attr("checked", "checked");
			}
		},
		
		_setState: function() {
			if(this._isChecked()) {
				this.psuedoCheckbox.addClass("active");
			} else {
				this.psuedoCheckbox.removeClass("active");
			}
		},
		
		check: function() {
			this.element.attr("checked", "checked");
			this._setState();
		},

		uncheck: function() {
			this.element.removeAttr("checked");
			this._setState();
		}

	};

	$.widget("ui.customCheck", CustomCheck);
	$.ui.customCheck.getter = "";
	$.ui.customCheck.defaults = {
		uiClass: "bb-custom-check"
	};

})(jQuery);


/************************************************************************
 * OEMBED
 ************************************************************************/

;(function($) {
	var videoProvider = function(name, urlPattern) {
		this.name = name;
		this.urlPattern = urlPattern;
		this.matches = function(externalUrl) {
			return externalUrl.indexOf(this.urlPattern) >= 0;
		};
	};
	var VideoKilledTheRadioStar = {
		_init: function() {
			var links = this.element.find("a:eq(0)");
			if(!(links).length > 0) {
				return;
			}
			var url = links.attr("href");
			var provider = this._getProvider(url);
			if(provider == null) return;
			this._getOEEmbed(url, function(data) {
				var block = $('<div class="video-block"/>');
				links.parent().after(block);
				block.after('<div class="clear"></div>');
				block.append(links.parent());

				var img_url = (data.thumbnail_url)?data.thumbnail_url:DEFAULT_VIDEO_IMAGE;

				links.html('<span class="video-thumb"><span class="play-icon">&nbsp;</span><img src="' + img_url + '" alt="video" /></span>');

				var videoMeta = $("<p/>");

				if(data.title !== undefined) {
					videoMeta.append('<span class="video-title">' + data.title + '</span><br/>');
				}

				if(data.author_name !== undefined) {
					videoMeta.append('<span class="author-name">' + data.author_name + '</span><br/>');
				}
				
				if(data.provider_name !== undefined && data.provider_url !== undefined) {
					videoMeta.append('<span class="provider-name"><a href="' + data.provider_url + '">' + data.provider_name + '</a></span>');
				} else if (data.provider_name !== undefined) {
					videoMeta.append('<span class="provider-name">' + data.provider_name + '</span>');
				} else if (data.provider_url !== undefined) {
					videoMeta.append('<span class="provider-name"><a href="' + data.provider_url + '">' + data.provider_url + '</a></span>');
				}

				block.append(videoMeta);

				links.click(function() {
					$(this).parent().css("float", "none");
					$("p", block).css("float", "none");
					var parent = $(this).parent();
					$(this).remove();
					parent.append(data.html);
					return false;
				});
			});
		},
		_getOEEmbed: function(url, callback) {
			$.getJSON(this._getData("oembedEndpoint"), {"url": url}, function(data) {
				callback.call(this, data);
			});
		},
		_getProvider: function(url) {
			for (var i = 0; i < this._getData("providers").length; i++) {
				if (this._getData("providers")[i].matches(url))
					return this._getData("providers")[i];
			}
			return null;
		}
	};
	$.widget("ui.oembed", VideoKilledTheRadioStar);
	$.ui.oembed.getter = "";
	$.ui.oembed.defaults = {
		"providers" : [
			new videoProvider("collegehumor", "collegehumor.com"),
			new videoProvider("bliptv", "blip.tv"),
			new videoProvider("googlevideo", "video.google."),
			new videoProvider("hulu", "hulu.com"),
			new videoProvider("metacafe", "metacafe.com"),
			new videoProvider("viddler", "viddler.com"),
			new videoProvider("vimeo", "vimeo.com"),
			new videoProvider("youtube", "youtube.com")
		],
		"oembedEndpoint": "http://omnomembed.alien109.com/omnomembed/?callback=?"
	};

})(jQuery);

/************************************************************************
 * Networking Buttons
 ************************************************************************/

;(function($) {

	var BaseButton = {

		_getTargetUser: function() {
			var qs = this.element.attr("href").split("?").pop().split("&");
			var params = {};
			for(var i = 0; i < qs.length; i++) {
				var kvp = qs[i].split("=");
				params[kvp[0]] = kvp[1];
			}
			return (params.identifier)?params.identifier:null;
		},

		_setRelationship: function() {
			var ref = this;
			$.post(this._getData("setUrl"), {"identifier": this._getTargetUser()}, function(response) {
				if(!ref._getData('setConfirmation')) {
					$.bbDialog.info(ref._doUserNameSubstitution(ref._getData("setMessage")), {
						onclose: function() {
							ref._trigger("set");
						}
					});
				} else {
					ref._trigger("set");
				}
				ref._updateSates(response);
			}, "json");
		},

		_unsetRelationship: function() {
			var ref = this;
			$.post(this._getData("unsetUrl"), {"identifier": this._getTargetUser()}, function(response) {
				if(!ref._getData('unsetConfirmation')) {
					$.bbDialog.info(ref._doUserNameSubstitution(ref._getData("unsetMessage")), {
						onclose: function() {
							ref._trigger("set");
						}
					});
				} else {
					ref._trigger("set");
				}
				ref._updateSates(response);
			}, "json");
		},

		_doUserNameSubstitution: function(str) {
			return str.replace("{0}", this._getTargetUser());
		},

		_clickHandler: function() {
			var ref = this;	
			if(this.element.is(".blocked")) {
				if(ref._getData("blockedMessage")) {
					$.bbDialog.alert(ref._getData("blockedMessage"));
				}
				return false;
			}

			if(this.element.is(".unvouched")) {
				if(ref._getData("unVouchedMessage")) {
					$.bbDialog.alert(ref._getData("unVouchedMessage"));
				}
				return false;
			}

			this._trigger("beforeSet");
			if(!this.onBeforeSet()) return;

			var ref = this;
			if(this.element.is(".state_active")) {
				if(this._getData('unsetConfirmation')) {
					$.bbDialog.confirm(ref._doUserNameSubstitution(ref._getData("unsetMessage")), function() {
						ref._unsetRelationship();
					}, function() {
						ref._trigger("cancelset");
					});
				} else {
					ref._setRelationship();
				}
			} else {
				if(this._getData('setConfirmation')) {
					$.bbDialog.confirm(ref._doUserNameSubstitution(ref._getData("setMessage")), function() {
						ref._setRelationship();
					});
				} else {
					ref._setRelationship();
				}
			}
		},

		_bindClick: function() {
			var ref = this;
			this.element.click(function() {
				ref._clickHandler();
				return false;
			});
		},

		// Override this in the button class
		_unsetCallback: function() {},

		// Override this in the button class
		_setCallback: function() {},

		onBeforeSet: function() {
			return true;	
		},

		_updateSates: function(response) {
			var userName = this._getTargetUser();

			if(response.isFriend) {
				$(".friend_link_" + userName).friendButton('setActive');
				$(".follow_link_" + userName).followButton('setImplied');
			} else if(response.isPending) {
				$(".friend_link_" + userName).friendButton('setPending');
				$(".follow_link_" + userName).followButton('setImplied');
			} else {
				$(".friend_link_" + userName).friendButton('unsetActive');
			}

			if(response.isFollowing) {
				$(".follow_link_" + userName).followButton('setActive');
			} else if(!response.isFriend && !response.isPending) {
				$(".follow_link_" + userName).followButton('unsetActive');
			}

			if(response.isBlocked) {
				$(".donkey_link_" + userName).blockButton('setActive');
			} else {
				$(".donkey_link_" + userName).blockButton('unsetActive');
			}

		},

		setActive: function() {
			this.element.addClass("state_active");
			this.element.find("span").text(this._getData("active"));
		},

		setPending: function() {
			this.element.addClass("state_active");
			this.element.find("span").text(this._getData("pending"));
		},

		setImplied: function() {
			this.element.addClass("state_implied");
			this.element.find("span").text(this._getData("active"));
		},

		unsetActive: function() {
			this.element.removeClass("state_active").removeClass("state_implied");
			this.element.find("span").text(this._getData("label"));
		}

	};

	/**********************************
	 * Friend Button
	 **********************************/
	var FriendButton = $.extend({}, BaseButton, {
		_init: function() {
			this._bindClick();
		}
	});

	$.widget("ui.friendButton", FriendButton);
	$.ui.friendButton.getter = "";
	$.ui.friendButton.defaults = {
		requiresRequest:true,
		setConfirmation:false,
		unsetConfirmation:true,
		label:bb.resources.friend.label,
		pending:bb.resources.friend.pending,
		active:bb.resources.friend.active,
		setMessage:bb.resources.friend.setMessage,
		unsetMessage:bb.resources.friend.unsetMessage,
		unVouchedMessage:bb.resources.friend.unVouchedMessage,
		blockedMessage:bb.resources.friend.blockedMessage,
		setUrl:bb.urls.social.relationship.setFriend,
		unsetUrl:bb.urls.social.relationship.unsetFriend
	};

	/**********************************
	 * Follow Button
	 **********************************/
	var FollowButton = $.extend({}, BaseButton, {
		_init: function() {
			this._bindClick();
		},
		onBeforeSet: function() {
			if(this.element.is(".state_implied")) {
				$.bbDialog.alert(bb.resources.follow.unfollowFriend);
			}
			return(!this.element.is(".state_implied"));
		}
	});

	$.widget("ui.followButton", FollowButton);
	$.ui.followButton.getter = "";
	$.ui.followButton.defaults = {
		requiresRequest:false,
		setConfirmation:false,
		unsetConfirmation:true,
		label:bb.resources.follow.label,
		active:bb.resources.follow.active,
		setMessage:bb.resources.follow.setMessage,
		unsetMessage:bb.resources.follow.unsetMessage,
		blockedMessage:bb.resources.follow.blockedMessage,		
		setUrl:bb.urls.social.relationship.setFollow,
		unsetUrl:bb.urls.social.relationship.unsetFollow
	};

	/**********************************
	 * Block Button
	 **********************************/
	var BlockButton = $.extend({}, BaseButton, {
		_init: function() {
			this._bindClick();
		}
	});

	$.widget("ui.blockButton", BlockButton);
	$.ui.blockButton.getter = "";
	$.ui.blockButton.defaults = {
		requiresRequest:false,
		setConfirmation:true,
		unsetConfirmation:true,
		label:bb.resources.donkey.label,
		active:bb.resources.donkey.active,
		setMessage:bb.resources.donkey.setMessage,
		unsetMessage:bb.resources.donkey.unsetMessage,
		setUrl:bb.urls.social.relationship.setBlock,
		unsetUrl:bb.urls.social.relationship.unsetBlock
	};
	

})(jQuery);

$(function() {
	$(".friend_link").friendButton();
	$(".follow_link").followButton();
	$(".donkey_link").blockButton();
});

/************************************************************************
 * User chooser
 ************************************************************************/

;(function($) {

	var UserChooser = {

		_init: function() {
			var ref = this;
			this.element.load(this._getData("baseUrl"), {allowMultiple:this._getData("allowMultiple")}, function() {
				var buttons = {
					'Close': function() {
						this.remove();
					}
				};

				if(ref._getData("allowMultiple")) {
					$.extend(buttons, {
						'Add users': function() {
							ref._serializeSelection();
						}
					});
				}

				ref.element.bbDialog({
					buttons:buttons,
					dialogClass:"user-chooser-dialog",
					onblur:function() {
						$(this).bbDialog('remove');
					}
				});
				ref._bindSelections();
				ref._bindFilterSelect();
			});
		},

		_bindFilterSelect: function() {
			var ref = this;
			this.element.find(".user-filter").change(function() {
				var filter = $(this).val();
				var params = {};
				params[filter] = "";
				ref.element.find(".up-users-container").load(ref._getData("baseUrl"), params, function(response) {
					ref._bindSelections();
				});
			});
		},

		_bindSelections: function() {
			var ref = this;
			$.each(this.element.find(".picker-user"), function() {
				var $userRow = $(this);
				if(ref._getData("allowMultiple")) {
					$userRow.find("a").click(function() {
						$(this).toggleClass("selected");
						return false;
					});
				} else {
					$userRow.find("a").click(function() {
						ref._selectUser(
							$userRow.find(".up-name").text(),
							$(this).attr("href").split("#").pop(),
							$userRow.find("img").attr("src")
						);
						return false;
					});
				}
			});
		},

		_selectUser: function(username, guid, avatarUrl) {
			this.element.trigger("selectUser", {"username":username, "guid":guid, "avatarUrl":avatarUrl});
		},

		_serializeSelection: function() {
			var stuff = $.map(this.element.find(".picker-user a.selected"), function(a) {
				return {
					avatarUrl:$(a).find("img").attr("src"),
					guid:$(a).attr("href").split("#").pop(),
					name:$(a).find(".up-name").text()
				};
			});
			this.element.trigger("selectUsers", [stuff]);
		},

		close: function() {
			this.element.bbDialog('remove');
			this.destroy();
		}
	};

	$.widget("ui.userChooser", UserChooser);
	$.ui.userChooser.getter = "";
	$.ui.userChooser.defaults = {
		baseUrl:bb.urls.search.userPicker,
		allowMultiple:false
	};


})(jQuery);

/************************************************************************
 * BBTabs
 ************************************************************************/

;(function($) {

	var BBTabs = {

		_init: function() {

			var rootRef = this;
			var menu = this.element;

			// Create data stores for each tab
			$.each($("li>a", menu), function() {
				var tab = $(this).parent();
				var id = tab.attr("id");
				var rootId = tab.attr("id").replace(/Tab/, "");
				tab.data("link", $(this));
				tab.data("id", id);
				tab.data("label", tab.find(".tabLabel").text());
				tab.data("url", $(this).attr("href"));
				tab.data("container", $("#" + rootId + "_container"));
				tab.data("target", $("#" + rootId + "_target"));
				//tab.data("label", tab.find(".tabLabel").text());
				$("#" + rootId + "_container").hide();
			});
			
			// Assign handlers for click to non disabled tabs
			$("li:not(.ui-state-disabled)", menu).click(function() {
				var $tab = $(this);

				rootRef.element.trigger("tabClicked", $tab);
				rootRef._selectTab($tab);

				if(!rootRef._getData("disableLoad")) {
					rootRef._loadTabContent($tab);
				}

			    return false;
			});

			// Handle disabled tabs
			$("li.ui-state-disabled a", this.element).click(function() {
				return false;
			});

			// Select default tab
			var defaultTab = $(this.element.find("li")[this._getData("selected")]);

			this._selectTab(defaultTab);

			// Load tab content, unless flag is set
			if(this._getData("skipLoadOnInit")) {
				defaultTab.data("container").show();
				defaultTab.addClass("ui-tabs-selected");
				this.element.trigger("tabLoaded", defaultTab);
			} else {
				this._loadTabContent(defaultTab);
			}
		},

		/**
		 * Sets initial selection params
		 * @param tab
		 */
		_selectTab: function(tab) {
			tab.find(".tabLabel").text("Loading");
			this.selectedTab = tab.data("id");
		},

		/**
		 * Loads content into a tab nd then sets tabs state and label
		 * @param tab
		 * @param url
		 */

		_loadTabContent: function(tab, url) {
			var ref = this;
			$(".tab-containers").find('.loadingModal').show();
			var urlToLoad = url || tab.data("url");
			tab.data("container").load(urlToLoad, function() {
				$(".tab-containers").find('.loadingModal').hide();
				tab.addClass("ui-tabs-selected");

				$.each($(">li", ref.element), function() {
					$(this).find(".tabLabel").text($(this).data("label"));
				});
				$.each($(">li[id!=" + ref.selectedTab + "]", ref.element), function() {
					$(this).data("container").hide();
					$(this).removeClass("ui-tabs-selected");
					$(this).data("container").empty();
				});
				ref.element.trigger("tabLoaded", tab);
				tab.data("container").show();
			});
		},

		/**
		 * Public load command
		 * @param index
		 * @param url
		 */
		load:function(index, url) {
			//note: nth-child index is not 0 based
			var tab =  this.element.find("li:nth-child(" + index + 1 + ")");
			this._loadTabContent(tab, url);
		},

		/**
		 * Returns the id of the currently selected tab
		 */
		getSelectedTabId: function() {
			return this.selectedTab;
		},

		/**
		 * Returns the currently selected tab node
		 */
		getSelectedTab: function() {
			return $("#" + this.selectedTab);
		},

		/**
		 * Sets and loads a tab
		 * @param tabId
		 * @param url
		 */
		setTab:function(tabId, url) {
			if(tabId === undefined) {
				var $tab =  this.element.find("li:first-child");
			} else {
				var $tab = $("#" + tabId);
			}
			this._selectTab($tab);
			this._loadTabContent($tab, url);
		}
	};

	$.widget("ui.bbTabs", BBTabs);
	$.ui.bbTabs.getter = "getSelectedTabId, getSelectedTab";
	$.ui.bbTabs.defaults = {
		selected:0,
		spinner:null,
		skipLoadOnInit:false,
		disableLoad:false
	};

})(jQuery);


/************************************************************************
 * Gear Menu
 ************************************************************************/

;(function($) {
	var GearMenu = {
		_init: function() {
			this.$menuTrigger = this.element.find(".gear-icon");
			this.$menuTrigger.hover(function() {
				$(this).find(".icon-link").addClass("active");
				$(this).find(".menu").stop(true, true).fadeIn();
			}, function() {
				$(this).find(".icon-link").removeClass("active");
				$(this).find(".menu").stop(true, true).fadeOut();
			});
		}
	};
	$.widget("ui.gearMenu", GearMenu);
})(jQuery);

/************************************************************************
 * Live Search
 ************************************************************************/

;(function($) {
	$.fn.liveSearch = function(url, options) {

		var options = $.extend({}, $.fn.liveSearch.defaults, {
			url: url
		}, options);

		return this.each(function() {

			var hasFocus = 0;
			var $input = $(this);
			var timeout;
			var previousValue = "";
			var select = $(options.panelTemplate);

			$(options.resultsTargetSelector).append(select);

			select.bind("click", function() {
			    $input.focus();
				clearTimeout(timeout);
			});

			$input.focus(function(){
				hasFocus++;
			}).blur(function() {
				timeout = setTimeout(hideSelect, 200);
			}).click(function() {
				if ( hasFocus++ > 1 && visible(!select) ) {
					onChange(0, true);
				}
			});

			$input.bind(($.browser.opera ? "keypress" : "keydown") + ".liveSearch", function(event) {
				clearTimeout(timeout);
				timeout = setTimeout(onChange, options.delay);
			});

			function onChange(crap, skipPrevCheck) {
				var currentValue = $input.val();

				if ( !skipPrevCheck && currentValue == previousValue ) return;

				previousValue = currentValue;
				currentValue = lastWord(currentValue);

				if ( currentValue.length >= options.minChars) {
					$input.addClass(options.loadingClass);
					if (!options.matchCase)
						currentValue = currentValue.toLowerCase();
					request(currentValue);
				} else {
					stopLoading();
					hideSelect();
				}
			};

			function trimWords(value) {
				if (!value) {
					return [""];
				}
				var words = value.split(options.multipleSeparator);
				var result = [];
				$.each(words, function(i, value) {
					if ($.trim(value)) result[i] = $.trim(value);
				});
				return result;
			};

			function lastWord(value) {
				if (!options.multiple) return value;
				var words = trimWords(value);
				return words[words.length - 1];
			};

			function request(term) {
				var timer1 = new Date().getTime();
				$.ajax({
					dataType: options.dataType,
					url: options.url,
                    data: { term: term },
					success: function(data) {
						$input.removeClass(options.loadingClass);
						select.show();
						$(".content-container", select).html(data);
						$(".content-container li").hover(function() {
							$(this).addClass("active");
						}, function() {
							$(this).removeClass("active");
						});
						var stuff = $(".ls-result .slink", select);
						for(var i = 0; i < stuff.length; i++)
						{
							var $stuff = $(stuff[i]);
							$stuff.html(highlight($stuff.text(), $input.val()));
						}
					}
				});
			};

			function stopLoading() {
				$input.removeClass(options.loadingClass);
			};

			function visible(e) {
				return e && e.is(":visible");
			};

			function hideSelect() {
				select.hide();
				clearTimeout(timeout);
			};

			function highlight(value, term) {
				return value.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + term.replace(/([\^\$\(\)\[\]\{\}\*\.\+\?\|\\])/gi, "\\$1") + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>");
			};
		});
	};

	$.fn.liveSearch.defaults = {
		inputClass: "ac_input",
		resultsClass: "ac_results",
		loadingClass: "ac_loading",
		minChars: 1,
		url:"",
		delay: 200,
		dataType:"html",
		resultsTargetSelector:".header-content .container",
		panelTemplate:'<div class="rounded-box livesearch"><div class="rb-header"><div class="rb-header-r">&nbsp;</div></div><div class="rb-content"><div class="rb-content-r"><div class="content-container"></div></div></div><div class="rb-footer"><div class="rb-footer-r">&nbsp;</div></div></div>'
	};

})(jQuery);

/************************************************************************
 * Alphanumeric http://itgroup.com.ph/alphanumeric/
 ************************************************************************/
(function($){

	$.fn.alphanumeric = function(p) {

		p = $.extend({
			ichars: "!@#$%^&*()+=[]\\\';,/{}|\":<>?~`.- ",
			nchars: "",
			allow: ""
		  }, p);

		return this.each
			(
				function()
				{

					if (p.nocaps) p.nchars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
					if (p.allcaps) p.nchars += "abcdefghijklmnopqrstuvwxyz";

					s = p.allow.split('');
					for ( i=0;i<s.length;i++) if (p.ichars.indexOf(s[i]) != -1) s[i] = "\\" + s[i];
					p.allow = s.join('|');

					var reg = new RegExp(p.allow,'gi');
					var ch = p.ichars + p.nchars;
					ch = ch.replace(reg,'');

					$(this).keypress
						(
							function (e)
								{

									if (!e.charCode) k = String.fromCharCode(e.which);
										else k = String.fromCharCode(e.charCode);

									if (ch.indexOf(k) != -1) e.preventDefault();
									if (e.ctrlKey&&k=='v') e.preventDefault();

								}

						);

					$(this).bind('contextmenu',function () {return false});

				}
			);

	};

	$.fn.numeric = function(p) {

		var az = "abcdefghijklmnopqrstuvwxyz";
		az += az.toUpperCase();

		p = $.extend({
			nchars: az
		  }, p);

		return this.each (function()
			{
				$(this).alphanumeric(p);
			}
		);

	};

	$.fn.alpha = function(p) {

		var nm = "1234567890";

		p = $.extend({
			nchars: nm
		  }, p);

		return this.each (function()
			{
				$(this).alphanumeric(p);
			}
		);

	};

})(jQuery);

/************************************************************************
 * Photo albums
 *
 * This simply creates a scrollable list of dom elements used primarily in
 * the persona photos tab for paging albums and picutres
 ************************************************************************/

;(function($) {

	var PhotoAlbum = {

		_init: function() {

			var ref = this;
			this.offset = 0;
			this.page = 1;
			this.pageCount = Math.max(Math.ceil(this.element.find(this._getData("itemSelector")).length / this._getData("size")), 1);
			this.total = this.element.find(this._getData("itemSelector")).length;
			this.element.show();
			this.$btnPrevPage = this.element.find(".paging-button.previous");
			this.$btnNextPage = this.element.find(".paging-button.next");
			this._refresh();

			this.$btnPrevPage.click(function() {
				if(!$(this).is(".disabled")) ref._previousPage();
				return false;
			});

			this.$btnNextPage.click(function() {
				if(!$(this).is(".disabled")) ref._nextPage();
				return false;
			});

			this.$btnNextPage.hover(function() {
				if(!$(this).is(".disabled")) $(this).addClass("hover");
			}, function() {
				$(this).removeClass("hover");
			});
				this.$btnPrevPage.hover(function() {
				if(!$(this).is(".disabled")) $(this).addClass("hover");
			}, function() {
				$(this).removeClass("hover");
			});
		},

		_resetPagingCounts: function() {
			this.pageCount = Math.max(Math.ceil(this.element.find(this._getData("itemSelector")).length / this._getData("size")), 1);
			this.total = this.element.find(this._getData("itemSelector")).length;
			if(this.page > this.pageCount) this.page = Math.max(this.pageCount, 1);
			if(this.offset >= this.total) this.offset = (this.page - 1) * this._getData("size");
			this._refresh();
		},

		_refresh: function() {
			if(this.page == 1) {
				this.$btnPrevPage.addClass("disabled");
			} else {
				this.$btnPrevPage.removeClass("disabled");
			}
			if(this.page == this.pageCount) {
				this.$btnNextPage.addClass("disabled");
			} else {
				this.$btnNextPage.removeClass("disabled");
			}
			var selector = this._getData("itemSelector");
			var endIndex = this._getData("size") + this.offset;
			this.element.find(selector).hide();
			this.element.find(selector).slice(this.offset, endIndex).show();
		},

		_previousPage: function() {
			if(this.page > 1) this.page--;
			this.offset = (this.page -1) * this._getData("size");
			this._refresh();
		},

		_nextPage: function() {
			if(this.page < this.pageCount) this.page++;
			this.offset = (this.page - 1) * this._getData("size");
			this._refresh();
		},

		updateCounts: function() {
			this._resetPagingCounts();
		}

	};

	$.widget("ui.photoAlbum", PhotoAlbum);
	$.ui.photoAlbum.getter = "";
	$.ui.photoAlbum.defaults = {
		size:6,
		itemSelector:""
	};

})(jQuery);


/*
 * jQuery Untils - v1.0 - 12/1/2009
 * http://benalman.com/projects/jquery-untils-plugin/
 *
 * Copyright (c) 2009 "Cowboy" Ben Alman
 * Dual licensed under the MIT and GPL licenses.
 * http://benalman.com/about/license/
 */
(function($){$.each({nextUntil:"nextAll",prevUntil:"prevAll",parentsUntil:"parents"},function(a,b){$.fn[a]=function(c,g){var d=[],e=this.get(),f=a.indexOf("p")===0;if(f&&e.length>1){e=e.reverse()}$.each(e,function(){var i=$(this)[b](g),h=$.browser;if(f){if(e.length>1&&h.msie&&h.version<8){i=i.filter("*,*").get().reverse()}else{if(/,/.test(g)){i=i.get().reverse()}}}$.each(i,function(){return $(this).is(c)?false:d.push(this)})});return this.pushStack($.unique(d),a,c+(g?","+g:""))}})})(jQuery);
