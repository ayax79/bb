$(function() {
	bb.pageInit.headerInit();

	//Creates global error handling for jquery ajax calls. here we can detect if the server
	//returns a 403 forbidden which means we should redirect to the login page.
	$.ajaxSetup({
		error:function(xhr, e) {
			if (xhr.status == 403) {
				//TODO add localization for string.
				$.bbDialog.alert("Your session has timed out. Please log back in.", {
					onclose:function() {
						window.location = BB_BASE_PATH + "/login";
					}
				});
			}
		}
	});

});

// Define bb namespace for decorators where blackbox_js is not included
if(bb === undefined) { var bb = {}; }


$.extend(bb, {

	trackPageView: function(url) {
		try {
			pageTracker._trackPageview(url);
		} catch(e) {
			//
		}
	},

	trackPageEvent: function() {
		try {
			pageTracker._trackEvent.apply(window, arguments);
		} catch(e) {
			//
		}
	},

	style: {
		tooltips: {}
	},

	widgets: {
		createMiniPublisher: function() {
			bb.widgets.components.miniPublisher = new Publisher("#miniPublisher", "mini");
		},
		components: {
			miniPublisher:{}
		}
	},

	facebook: {
		updateFBConnect: function() {
			// the markup here uses FBML...
			$("#facebook_connect_status_container").html(
				"<div class='fb_avatar'><fb:profile-pic uid=loggedinuser facebook-logo=true></fb:profile-pic></div>"
				+ "<div class='fb_text'><fb:name uid=loggedinuser useyou=false></fb:name><br />You are connected to your Facebook account.<br />"
				+ "<span id='pstate_offline_access'></span><span id='pstate_publish_stream'></span><span id='pstate_create_event'></span></div>"
			);
			// parse the tree to populate the FBML tags above
			FB.XFBML.Host.parseDomTree();
			// Check for extended permissions;
			FB.Connect.showPermissionDialog("offline_access,publish_stream,create_event");
		},
		callInit: function(callback) {
			FB.init("445ade94d79c830c302a5cd7370b88a1","/community/xd_receiver.htm",{"ifUserConnected" : callback});
		}
	},

	feedback: {
		abuse: {
			report: function($el) {
				$el.click(function() {
					var d = $('<div class="report-abuse"></div>');
					$("body").append(d);
					d.load(ReportAbuseLoadURL, function() {
						var dialog = $(this).bbDialog({
							draggable:false,
							title:"FeedBack",
							modal:true,
							buttons:{
								'Cancel':function() {
									this.remove();
								},
								'Submit Report': function() {
									if ($("#abuse_subject").val() != '' || $("#abuse_subject").val() != '') {
										$.post(ReportAbusePostURL, $(".abuseForm", dialog).serialize(true),
												function(msg) {
													$(".abuseForm", dialog).hide();
													if (msg == 'success') {
														$(".success_message", dialog).show();
													} else {
														$(".fail_message", dialog).show();
													}
													$(dialog).bbDialog('buttons', {
														'OK': function() {
															this.remove();
														}
													});
												}
												);
									} else {
										alert("Subject and Description are required.");
									}
								}
							}
						});
					});
					return false;
				});
			}
		}

	},

	notifications: {
		removeUserNotification: function($user) {
			$user.slideUp(400, function() {
				$(this).remove();
			});
		},

		updateCounts: function(type, totalNotifications, totalTypes) {
			var type = type.toLowerCase();
			if($.trim(type).length < 1) return;
			$(".section." + type + " .count").text(totalNotifications);
			$(".section." + type + " .totalcount .num").text(totalTypes);

			if(totalNotifications < 1) {
				$(".section." + type + " .noMoreNotifications").show();
				$(".section." + type + " .see-all").hide();
			}
		},

		getGuidParams: function($link) {
			var hash = 	bb.utils.returnHashParam($link).split("_");
			return {ids:hash[0], entityIds:hash[1]};
		},

		_callAction:function(inc, type, params, url, $user, $container, isViewAll) {
			if(isViewAll) {
				$.post(url, params, function() {
                    $(".notification_" + params.ids).hide().remove();
					if($("#notifications .notification-item").length < 1) {
						$("#notifications").find(".noMoreNotifications").show();
					}
				});
			} else {
				$container.load(url, params, function() {
					var $this = $(this);
					switch(type) {
						case "wish":
								bb.notifications.bindWishes($container, isViewAll);
								break;
						case "friend":
							bb.notifications.bindFriends($container, isViewAll);
							break;
						case "follow":
							bb.notifications.bindFollows($container, isViewAll);
							break;
						case "gift":
							bb.notifications.bindGifts($container, isViewAll);
							break;
                        case "vouch":
							bb.notifications.bindVouches($container, isViewAll);
							break;
                        case "occasion":
							bb.notifications.bindOccasions($container, isViewAll);
							break;
					}
				});
			}
		},

		_bindWish: function($user, $container, isViewAll) {
			bb.pageInit.bindMiniProfiles($user);
			//ACKNOWLEDGE
			$user.find(".accept-wish-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(0,"wish", guidParams, bb.urls.notifications.wishAccept, $user, $container, isViewAll);
				return false;
			});
			//WISH BACK
			$user.find(".deny-wish-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(0,"wish", guidParams, bb.urls.notifications.wishReject, $user, $container, isViewAll);
				return false;
			});
		},

		_bindFollow: function($user, $container, isViewAll) {
			bb.pageInit.bindMiniProfiles($user);
			//ACKNOWLEDGE
			$user.find(".acknowledge-follow-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(0,"follow", guidParams, bb.urls.notifications.followAck, $user, $container, isViewAll);
				return false;
			});
			//FOLLOW BACK
			$user.find(".follow-back-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(1,"follow", guidParams, bb.urls.notifications.follow, $user, $container, isViewAll);
				return false;
			});
		},

		_bindFriend: function($user, $container, isViewAll) {
			bb.pageInit.bindMiniProfiles($user);
			//IGNORE
			$user.find(".ignore-friend-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(0,"friend", guidParams, bb.urls.notifications.friendIgnore, $user, $container, isViewAll);
				return false;
			});
			//ACCEPT
			$user.find(".accept-friend-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(1,"friend", guidParams, bb.urls.notifications.friendAccept, $user, $container, isViewAll);
				return false;
			});
		},

		_bindGift: function($user, $container, isViewAll) {
			bb.pageInit.bindMiniProfiles($user);
			//ACCEPT
			$user.find(".accept-gift-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(0,"gift", guidParams, bb.urls.notifications.giftAccept, $user, $container, isViewAll);
				return false;
			});
			//DENY
			$user.find(".deny-gift-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(1,"gift", guidParams, bb.urls.notifications.giftReject, $user, $container, isViewAll);
				return false;
			});
		},

        _bindVouch: function($user, $container, isViewAll) {
			bb.pageInit.bindMiniProfiles($user);
			//CONFIRM
			$user.find(".accept-vouch-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(0,"vouch", guidParams, bb.urls.notifications.vouchAccept, $user, $container, isViewAll);
				return false;
			});
			//REJECT
			$user.find(".deny-vouch-link").click(function() {
				guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(1,"vouch", guidParams, bb.urls.notifications.vouchReject, $user, $container, isViewAll);
				return false;
			});
		},

        _bindOccasion: function($user, $container, isViewAll) {
            //CONFIRM
			$user.find(".accept-invitation-link").click(function() {
                guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(0,"occasion", guidParams, bb.urls.notifications.occasionAccept, $user, $container, isViewAll);
				return false;
			});
			//REJECT
			$user.find(".decline-invitation-link").click(function() {
                guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(1,"occasion", guidParams, bb.urls.notifications.occasionReject, $user, $container, isViewAll);
				return false;
			});
			//MAYBE
			$user.find(".maybe-invitation-link").click(function() {
                guidParams = bb.notifications.getGuidParams($(this));
				bb.notifications._callAction(2,"occasion", guidParams, bb.urls.notifications.occasionMaybe, $user, $container, isViewAll);
				return false;
			});
		},

		bindWishes: function($scope, isViewAll) {
			$.each($(".notification-item", $scope), function() {
				bb.notifications._bindWish($(this), $scope, isViewAll);
			});
		},

		bindFollows: function($scope, isViewAll) {
			$.each($(".notification-item", $scope), function() {
				bb.notifications._bindFollow($(this), $scope, isViewAll);
			});
		},

		bindFriends: function($scope, isViewAll) {
			$.each($(".notification-item", $scope), function() {
				bb.notifications._bindFriend($(this), $scope, isViewAll);
			});
		},

		bindGifts: function($scope, isViewAll) {
			$.each($(".notification-item", $scope), function() {
				bb.notifications._bindGift($(this), $scope, isViewAll);
			});
		},

        bindVouches: function($scope, isViewAll) {
			$.each($(".notification-item", $scope), function() {
				bb.notifications._bindVouch($(this), $scope, isViewAll);
			});
		},

        bindOccasions: function($scope, isViewAll) {
			$.each($(".notification-item", $scope), function() {
				bb.notifications._bindOccasion($(this), $scope, isViewAll);
			});
		},


		bindEvents: function($scope, isViewAll) {
			//WISH
			var $wishes = $(".section.wish .items", $scope);
			bb.notifications.bindWishes($wishes, isViewAll);

			//FOLLOW
			var $following = $(".section.follow .items", $scope);
			bb.notifications.bindFollows($following, isViewAll);

			//FRIEND
			var $friends = $(".section.friend .items", $scope);
			bb.notifications.bindFriends($friends, isViewAll);

			//GIFTS
			var $gifts = $(".section.gift .items", $scope);
			bb.notifications.bindGifts($gifts, isViewAll);

            //VOUCHES
			var $vouches = $(".section.vouch .items", $scope);
			bb.notifications.bindVouches($vouches, isViewAll);

            //OCCAIONS
            var $occasions = $(".section.occasion .items", $scope);
            bb.notifications.bindOccasions($occasions, isViewAll);

		}

	},

	explore: {

		open: function(entityType) {
			//Note: entity type isn't supported right now...
			var d = $('<div id="explore"></div>');
			$("body").append(d);
			d.load(bb.urls.search.explorerOpen, function() {
				var dialog = $(this).bbDialog({
					draggable:false,
					title:"Explore",
					modal:true,
					dialogClass:"explorer-dialog",
					buttons:{
						'Close':function() {
							this.remove();
						}
					}
				});
			});
		}
	},

	messaging: {

		compose: function(params) {
			var d = $('<div class="compose-window"/>');
			$("body").append(d);
			d.load(bb.urls.mailbox.compose, params, function() {
				var dialog = $(this).bbDialog({
					draggable:false,
					title:"Create a message",
					modal:true,
					hideButtons: true,
					onclose: function() {
						$("#user-chooser").bbDialog('remove');
					}
				});

				$(".btnCancel", d).click(function() {
					d.bbDialog('remove');
					return false;
				});


				var sendMessage = function() {
					var params = $(".compose-form", d).serialize();
					d.load(bb.urls.mailbox.publish, params, function(response) {
						setTimeout(function() {
							d.bbDialog('getContainer').fadeOut(400, function() {
								d.trigger("messageSent");
								d.bbDialog('remove');
							});
						}, 1500);
					});
				};

				$(".btnSend", d).click(function() {

					if($.trim($("#recipient-guid").val()).length < 1) {
						$.bbDialog.alert("Please add a recipient for this message.");
						return false;

					}

					if($.trim($("#msg_subject").val()).length < 1) {
						$.bbDialog.alert("Please enter a subject for your message.");
						return false;

					}

					if($.trim($("#msg_body").val()).length < 1) {
						$.bbDialog.alert("Please enter text for your message.");
						return false;
					}

//					if($.trim($("#msg_body").val()).length < 1) {
//						$.bbDialog.confirm("Do you want to send this message without any text?", function() {
//							sendMessage();
//						}, function() {
//							return false;
//						});
//
//					} else {
//						sendMessage();
//					}

					sendMessage();
					return false;
				});

				$(".btnSave", d).click(function() {
					var params = $(".compose-form", d).serialize() + "&isDraft=true";
					d.load(bb.urls.mailbox.draft, params, function(response) {
						setTimeout(function() {
							d.bbDialog('getContainer').fadeOut(400, function() {
								d.trigger("messageSent");
								d.bbDialog('remove');
							});
						}, 1500);
					});
					return false;
				});
			});
			return d;
		}
	},

	userPicker: {
		open: function(options) {
			var chooser = $('<div id="user-chooser"/>');
			$("body").append(chooser);
			chooser.userChooser(options);
			return chooser;
		}
	},

	imageUploader: {
		open: function(fml) {
			var d = $('<div/>');
			$("body").append(d);
			if(!fml) var fml = "";
			d.load(fml + "/ajax/profileImageCreator.jspf", function() {
				$(this).bbDialog({
					draggable:true,
					title:"Picture upload",
					modal:true,
					dialogClass:"",
					buttons:{
						'Cancel':function() {
							this.remove();
						}
					}
				});
				d.bbDialog('moveTo', null, $(window).scrollTop() + 10);
			});
			return d;
		}
	},

	autocomplete: {
		bind: function($element, tokenLimit) {
			$element.tokenInput(bb.urls.search.autocomplete,
			{
				classes: {
					tokenList: "token-input-list-facebook",
					token: "token-input-token-facebook",
					tokenDelete: "token-input-delete-token-facebook",
					selectedToken: "token-input-selected-token-facebook",
					highlightedToken: "token-input-highlighted-token-facebook",
					dropdown: "token-input-dropdown-facebook",
					dropdownItem: "token-input-dropdown-item-facebook",
					dropdownItem2: "token-input-dropdown-item2-facebook",
					selectedDropdownItem: "token-input-selected-dropdown-item-facebook",
					inputToken: "token-input-input-token-facebook"
				},
				searchDelay: 50,
				minChars: 1,
				tokenLimit:tokenLimit
			});
		}
	},

	pageInit: {
		collapsePost: function(userId) {
			$.each($(".article.U" + userId), function() {
				$(this).addClass("collapsed-post");
			});
			$.each($(".article-comment.U" + userId), function() {
				$(this).addClass("collapsed-post");
			});
		},

		expandPost: function(userId) {
			$.each($(".article.U" + userId), function() {
				$(this).removeClass("collapsed-post");
			});
			$.each($(".article-comment.U" + userId), function() {
				$(this).removeClass("collapsed-post");
			});
		},

		bindMiniProfiles: function(scope) {
			$(".userProfileLink.allow-mini-profile", scope).miniPersona({personaType:'member'});
		},

		bindMiniPublisher: function(scope) {
			$(".comment-publish.publisher-msg-source", scope).focus(function() {
				bb.widgets.components.miniPublisher.attach(this);
			});
		},

		bindDefaultTextBoxes: function(scope) {
			$('input[type=text].dv, textarea.dv', scope).focus(function() {
				if (this.value == this.defaultValue) {
					this.value = '';
					$(this).removeClass("input-default");
				}
			});
			$('input.dv, textarea.dv', scope).blur(function() {
				if (this.value == '' || this.value.replace(/\s/g, '').length < 1) {
					this.value = this.defaultValue;
					$(this).addClass("input-default");
				}
			});
		},

		bindActionsMenu: function(scope, parentScope) {

			$(".article-filter-link").click(function(){return false;});

			$(".delete-link", scope).click(function() {
				var link = $(this);
				$.bbDialog.confirm("Are you sure you wish to delete this post?", function() {
					$.post(link.attr("href"), function(response) {
						parentScope.effect('blind', 1000, function() {
							$(this).remove();
						});
					});
				});
				return false;
			});

			$(".collapse-link", scope).click(function() {
				var userId = $(this).attr("href").split("#").pop();
				Activity.Stream.registerCollapsedPosts(userId);
				bb.pageInit.collapsePost(userId);
				return false;
			});

			$(".expand-link", scope).click(function() {
				var userId = $(this).attr("href").split("#").pop();
				Activity.Stream.unRegisterCollapsedPosts(userId);
				bb.pageInit.expandPost(userId);
				return false;
			});

			$(".block-link", scope).click(function() {
				var link = $(this);
				$.bbDialog.confirm("Are you sure you wish to block this user?", function() {
					$.post(link.attr("href"), function(response) {
						$.bbDialog.info("You've now blocked this user.");
					});
				});
				return false;
			});

			$(".ignore-link", scope).click(function() {
				var link = $(this);
				$.bbDialog.confirm("Are you sure you wish to ignore this user?", function() {
					$.post(link.attr("href"), function(response) {
						$.bbDialog.info("You've now ignored this user.");
					});
				});
				return false;
			});

			$(".bookmark-link", scope).click(function() {
				$.post($(this).attr("href"), function(response) {
					$.bbDialog.info("Your bookmark has been recorded.");
				});
				return false;
			});

		},

		bindArticleEvents: function(scope) {

			var scopeType, filterMenuScope;

			if (scope.is(".article")) {
				scopeType = "article";
				filterMenuScope = scope.find(".article-actions");
			} else if (scope.is(".article-comment")) {
				scopeType = "comment";
				filterMenuScope = scope.find(".comment-actions");
			}

			// If scope is a container of articles,
			// process each article individually.
			if (!scope.is(".article") && !scope.is(".article-comment")) {
				$.each(scope.find(".article"), function() {
					bb.pageInit.bindArticleEvents($(this));
				});
				return;
			}

			if (scopeType == "article") {
				$.each(scope.find(".article-comment"), function() {
					bb.pageInit.bindArticleEvents($(this));
				});
			}

			//$(".privacy_control", scope).privacyControl();

			bb.pageInit.bindActionsMenu(filterMenuScope, scope);
			bb.pageInit.bindMiniPublisher(scope);
			bb.pageInit.bindDefaultTextBoxes(scope);

			scope.hover(function(event) {
				filterMenuScope.show();
				event.stopPropagation();
			},
			function() {
				filterMenuScope.hide();
				$(".article-filter-menu", filterMenuScope).hide();
				$(".article-filter-link", filterMenuScope).removeClass("article-filter-link-over");
			});

			$(".article-filter-button:first", filterMenuScope).hover(function(event) {
				$(".article-filter-menu", $(this)).show();
				$(".article-filter-link", $(this)).addClass("article-filter-link-over");
			},
			function() {
				$(".article-filter-menu", $(this)).hide();
				$(".article-filter-link", $(this)).removeClass("article-filter-link-over");
			});

			/**********************************************/
			$('*[class*=timeago]:first', scope).timeago();
			bb.pageInit.bindMiniProfiles(scope);
			$.each($(".article-media img.lightbox", scope), function() {
				$(this).parent().lightBox({showImgNbr:'no'});
			});
			//$(".tt-ur", scope).qtip(bb.style.tooltips.ur);
			//$(".tt-fm", scope).qtip(bb.style.tooltips.ttfm);
			/**********************************************/

		},

		bindCollapsiblePanels: function(scope) {
			$(".widget-collapsible>h2>span", scope).append('<a href="#" class="widget-collapse"></a>');
			$(".widget-collapse", scope).click(function(event) {
				var $this = $(this);
				var hdr = $this.parent().parent();
				var spn = $("span", hdr);
				var wp = hdr.parent();
				$(".widget-body-container", wp).slideToggle(150, function() {
					$(".widget-bottom", wp).toggle();
					$this.toggleClass("widget-collapse-collapsed");
					hdr.toggleClass("closed");
					spn.toggleClass("closed");
				});
				return false;
			});
		},

		bindSliders: function(scope) {

			if (scope && scope.is(".private")) {

				scope.css({"position" : "relative"});
				scope.append("<div class='private-icon'></div>");

			}
			$.each($(".slider_container", scope), function() {
				var $slider_container = $(this);
				var min = $(this).find(".slider-val-min");
				var max = $(this).find(".slider-val-max");
				var nom = $(this).find(".slider-val");
				var range = null;
				var values = null;
				var change = null;
				if (min.length && max.length) {
					range = true;
					min.val((min.val() != "" && min.val() !== undefined) ? min.val() : -2);
					max.val((max.val() != "" && min.val() !== undefined) ? max.val() : 2);
					values = [min.val(), max.val()];
					change = function(event, ui) {
						min.val(ui.values[0]);
						max.val(ui.values[1]);
					};
				} else if (nom.length) {
					range = false;
					nom.val((nom.val()) ? nom.val() : 0);
					values = [nom.val()];

					change = function(event, ui) {
						nom.val(ui.value);
					};
				}
				$slider_container.find(".bbslider").slider({
					min:-2,
					max:2,
					range:range,
					values:values,
					change: change
				});
				$slider_container.find(".bbslider-disable-slider").click(function() {
					$slider_container.find(".bbslider").slider((($(this).is(":checked")) ? 'enable' : 'disable'));
					var isVisible = ($(this).is(":checked")) ? true : false;
					if (isVisible) {
						$slider_container.find(".ui-slider-range").show();
						$slider_container.find(".ui-slider-handle").show();
						if (min.length && max.length) {
							min.val($slider_container.find(".bbslider").slider('values')[0]);
							max.val($slider_container.find(".bbslider").slider('values')[1]);
						} else if (nom.length) {
							nom.val($slider_container.find(".bbslider").slider('value'));
						}
					} else {
						$slider_container.find(".ui-slider-range").hide();
						$slider_container.find(".ui-slider-handle").hide();
						if (min.length && max.length) {
							min.val("");
							max.val("");
						} else if (nom.length) {
							nom.val("");
						}
					}
				});
			});
		},

		bindWidgetTabs: function(scope) {
			$(".widget-tabs-container", scope).tabs();
			bb.pageInit.bindCollapsiblePanels(scope)
		},

		bindContainerEvents: function(scope) {
			$(".me-section.collapsible>h2>span", scope).append('<a href="#" class="widget-collapse"></a>');
			$(".widget-collapse", scope).click(function(event) {
				var $this = $(this);
				var hdr = $this.parent().parent();
				var spn = $("span", hdr);
				var wp = hdr.parent();
				$(".content", wp).slideToggle(150, function() {
					$this.toggleClass("widget-collapse-collapsed");
					hdr.toggleClass("closed");
					spn.toggleClass("closed");
				});
				return false;
			});
		},

		headerInit: function() {
			$(".header-nav>li>ul").hide();
			$(".header-nav>li#event-nav").mouseenter(function() {
				$("ul", this).show();
				$(this).addClass("nav-item-hover");
			});
			$(".header-nav>li#event-nav").mouseleave(function() {
				$("ul", this).hide();
				$(this).removeClass("nav-item-hover");
			});
			$("#user-settings-button").hover(function() {
				if (!$("#user-settings-button").hasClass("user-settings-open")) {
					$(this).addClass("user-settings-hover");
				}
			}, function() {
				$(this).removeClass("user-settings-hover");
			}
					);
			$("#user-settings-button").toggle(
					function() {
						$("#header-settings").slideDown();
						$(this).addClass("user-settings-open");
						$(this).removeClass("user-settings-hover");
						return false;
					},
					function() {
						$("#header-settings").slideUp();
						$(this).removeClass("user-settings-open");
						return false;
					}
					);

			if (typeof($.fn.liveSearch) != "undefined") {
				bb.pageInit.bindDefaultTextBoxes($("#quicksearch").parent());
				$("#quicksearch").liveSearch(bb.urls.search.quick);
			}

			$("#mm_explore").click(function() {
				bb.explore.open();
				return false;
			});


			$("#feedback_idd").click(function() {
				var d = $('<div id="feedback"></div>');
				$("body").append(d);
				d.load(FeedBackLoadURL, function() {
					var dialog = $(this).bbDialog({
						draggable:false,
						title:"FeedBack",
						modal:true,
						buttons:{
							'Ok':function() {
								this.remove();
							}
						}
					});
				});
				return false;
			});

		},

		defaultInit: function() {
			// Changes post to button label based on radio
			$(".post-to :radio").change(function() {
				$("#PostUpdate span").text($("#PostUpdate").attr("title") + " " + this.value);
			});
		}
	},

	utils: {

		/**
		 * Returns a guid from an anchor where the href format is #guid or http://www....#guid
		 * @param $a
		 */
		returnHashParam: function($a) {
			return $a.attr("href").split("#").pop();
		},

		/**
		 * Returns an array of query params. Allows duplicate key names
		 * @param url
		 */
		getQueryParamArray: function(url) {
			var params = [];
			var kvps = url.split("?").pop().split("&");
			for(var i = 0; i < kvps.length; i++) {
				var kvp = kvps[i].split("=");
				params.push({name:kvp[0], value:kvp[1]});
			}
			return params;
		},
		/**
		 * Returns an object with query params properties. Does not allow duplicate keys
		 * @param url
		 */
		getQueryParamObject: function(url) {
			var params = {};
			var kvps = url.split("?").pop().split("&");
			for(var i = 0; i < kvps.length; i++) {
				var kvp = kvps[i].split("=");
				params[kvp[0]] = kvp[1];
			}
			return params;
		}
	}

});

bbDebug = {};
bbDebug.log = function() {
	if (!$.browser.safari && typeof window.console !== 'undefined' && typeof window.console.log === 'function') {
		console.log.apply(this, arguments);

	}
	$.each(arguments, function() {
		$("#bugger_text").append('<p>' + this + '</p>');
	});

};

bb.style.tooltips.base = {
	position: {
		corner: {
			target: 'bottomRight',
			tooltip: 'topLeft'
		}
	},
	style: {
		padding: 3,
		background:'#f0f0f0',
		border: {
			width: 1,
			radius: 6,
			color: '#f0f0f0'
		},
		tip: 'topLeft'
	},
	show: {
		delay:1400
	}
};

bb.style.tooltips.ur = {
	position: {
		corner: {
			target: 'topRight',
			tooltip: 'bottomLeft'
		}
	},
	style: {
		padding: 3,
		background:'#f0f0f0',
		border: {
			width: 1,
			radius: 6,
			color: '#f0f0f0'
		},
		tip: 'bottomLeft'
	},
	show: {
		delay:1400
	}
};

bb.style.tooltips.ttfm = {
	position: {
		corner: {
			target: 'rightMiddle',
			tooltip: 'leftMiddle'
		}
	},
	style: {
		padding: 3,
		background:'#f0f0f0',
		border: {
			width: 1,
			radius: 6,
			color: '#f0f0f0'
		},
		tip: 'leftMiddle'
	},
	show: {
		delay:1400
	}
};

//Prototype indexOf method for IE, since it sux.
if (!Array.indexOf) {
	Array.prototype.indexOf = function(obj) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == obj) {
				return i;
			}
		}
		return -1;
	};
}

/**
 * jQuery cookie.
 *
 * @param name
 * @param value
 * @param options
 */
jQuery.cookie = function(name, value, options) {
	if (typeof value != 'undefined') {
		options = options || {};
		if (value === null) {
			value = '';
			options.expires = -1;
		}
		var expires = '';
		if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
			var date;
			if (typeof options.expires == 'number') {
				date = new Date();
				date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
			} else {
				date = options.expires;
			}
			expires = '; expires=' + date.toUTCString();
		}
		var path = options.path ? '; path=' + (options.path) : '';
		var domain = options.domain ? '; domain=' + (options.domain) : '';
		var secure = options.secure ? '; secure' : '';
		document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
	} else {
		var cookieValue = null;
		if (document.cookie && document.cookie != '') {
			var cookies = document.cookie.split(';');
			for (var i = 0; i < cookies.length; i++) {
				var cookie = jQuery.trim(cookies[i]);
				if (cookie.substring(0, name.length + 1) == (name + '=')) {
					cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
					break;
				}
			}
		}
		return cookieValue;
	}
};

;(function($) {
	var PrivacyControl = {
		_init: function() {
			var ref = this;
			this.element.find(".top").css("padding-right", "22px");
			this.element.find(".menu_selection").hover(function() {
				$(this).addClass("active");
				$(this).find(".privacy_options").slideDown(150);
			}, function() {
				var ref = $(this);
				$(this).find(".privacy_options").slideUp(150, function() {
					ref.removeClass("active");
				});
			});
			this.element.find(":radio").click(function() {
				var ref2 = $(this);
				var params = ref.element.find("form").serializeArray();
				$.post(ref.element.find("form").attr("action"), params, function(response) {
					$.each(ref.element.find(":radio"), function() {
						ref.element.find(".menu_selection").removeClass($(this).val());
					});
					ref.element.find(".pval").text(ref2.next("label").text());
					ref.element.find(".menu_selection").addClass(ref2.val());
				});
			});
		}
	};
	$.widget("ui.privacyControl", PrivacyControl);
})(jQuery);

;(function($) {

	var ImageCropper = {
		_init: function() {
		},
		setImage: function(url) {
			var self = this;

			var cropImage = $("#" + self.element.attr("id") + "_cropImage");
			var previewImage = $("#" + self.element.attr("id") + "_previewImage");
			cropImage.attr("src", url).show();
			previewImage.attr("src", url).show();

			//create closures for in scope callbacks
			var uv = function(c) {
				self.updateValues(c);
			};

			var up = function(c) {
				self.updatePreview(c);
			};

			cropImage.unbind("load").bind("load", function() {
				self.api = $.Jcrop(cropImage, {
					aspectRatio: self._getData("ratio"),
					onSelect: uv,
					onChange: up,
					boxWidth:400,
					boxHeight:400,
					setSelect:[0,0,cropImage.width(),cropImage.height()]
				});
				self.updateValues(self.api.tellSelect());
			});
		},

		destroyCrop: function() {
			this.api.destroy();
			$("#" + this.element.attr("id") + "_cropImage").attr("src", "");
			$("#" + this.element.attr("id") + "_previewImage").attr("src", "");
			this.element.find(".xf").val("");
			this.element.find(".yf").val("");
			this.element.find(".wf").val("");
			this.element.find(".hf").val("");
		},

		updateValues: function(c) {
			this.element.find(".xf").val(c.x);
			this.element.find(".yf").val(c.y);
			this.element.find(".wf").val(c.w);
			this.element.find(".hf").val(c.h);
			this.updatePreview(c);
		},

		updatePreview: function(c) {
			var cropImage = $("#" + this.element.attr("id") + "_cropImage");
			var previewImage = $("#" + this.element.attr("id") + "_previewImage");
			var scaleX = this._getData("ratio");

			// normalize ratios
			if (scaleX > 1) {
				scaleY = 1 / scaleX;
				scaleX = 1;
			} else {
				scaleY = 1;
			}

			var max = this._getData("previewMaxSize");
			var rx = (max * scaleX) / c.w;
			var ry = (max * scaleY) / c.h;
			this.element.find(".preview-wrap").css({
				width: max * scaleX,
				height:max * scaleY
			});

			previewImage.css({
				width: Math.round(rx * cropImage.width()) + 'px',
				height: Math.round(ry * cropImage.height()) + 'px',
				marginLeft: '-' + Math.round(rx * c.x) + 'px',
				marginTop: '-' + Math.round(ry * c.y) + 'px'
			});
		},

		serialize: function() {
			return {
				'x': this.element.find(".xf").val(),
				'y': this.element.find(".yf").val(),
				'w': this.element.find(".wf").val(),
				'h': this.element.find(".hf").val()
			};
		}
	};


	$.widget("ui.imageCropper", ImageCropper);
	$.ui.imageCropper.getter = "serialize";
	$.ui.imageCropper.defaults = {
		ratio:1,
		previewMaxSize:100
	};



})(jQuery);
