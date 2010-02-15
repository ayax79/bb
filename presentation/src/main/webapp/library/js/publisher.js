/*
 Requires:
 json_parse.js
 bb.activity.js
 */
(function() {

	Publisher = function(selector, config, options) {
		var self = this;
		this.$rootNode = $(selector);
		this.config = config;
		this.id = selector.replace("#", "");
		this.mediaType = null;
		this.mediaSource = null;
		this.$mediaSourceSelectors = $(".select-media-source", this.$rootNode);
		this.$mediaSources = $(".media-source", this.$rootNode);
		this.initedTypes = {};
		Publisher.addInstance(this.id, this);
		this.lockOpen = false;
		this.options = $.extend({}, Publisher.defaults, options);

		this.$twitterform = $(".post-to-public-confirm", this.$rootNode);
		this.$twitterform.bbDialog({
			autoOpen:false,
			modal:true
		});

		this.fbUserIsConnected = false;

		var callUpdateFBConnect = function() {
			self.fbUserIsConnected = true;
			bb.facebook.updateFBConnect();
		};

		if(this.config == "main") {

			$("#publisherpublishToTwitter").click(function () {
      			$(".twitter-box").toggle("slide", { direction: "up" }, 300);
			});

			$("#publishertwitterMessageBody").keyup(function() {
                self.updateTwitterCharCount();
            });

			$("#publisherpublishToFacebook").click(function () {
				$check = $(this);
      			$(".facebook-box").toggle("slide", { direction: "up" }, 300);
				if($check.is(":checked")) {
					try{
						bb.facebook.callInit(callUpdateFBConnect);
					} catch(e) {
						//
					}
				}
			});

			this.updateTwitterCharCount();
		}
	};

	Publisher.prototype.updateTwitterCharCount = function() {
		var maxChars = 140;
		var charCount = $("#publishertwitterMessageBody").val().length;
		if (charCount > maxChars) {
			$(".chars-remaining").addClass("over-count");
		} else {
			$(".chars-remaining").removeClass("over-count");
		}
		$(".char-count").text(maxChars - charCount);
	};

	Publisher.defaults = {
		alwaysOpen: false
	};

	Publisher.instances = {};

	Publisher.addInstance = function(id, instance) {
		Publisher.instances[id] = instance;
	};

	Publisher.getInstance = function(id) {
		return Publisher.instances[id];
	};

	Publisher.prototype.close = function(isCancel) {

        if($(".recipientGuid_ac", this.$rootNode).length > 0) {
            $(".recipientGuid_ac", this.$rootNode).tokenInput('reset');
        }

		$(".media-type-inputs", this.$rootNode).hide();

		if(!this.options.alwaysOpen) {
			$(".publisher-contents", this.$rootNode).hide();
		}

		$(".pub-tabs", this.$rootNode).hide();
		$(document).unbind(".msg");

		this.$messageSource.unbind(".msg").removeClass("focused");

		this.resetMessageSource();
		this.$messageSource[0].blur();
		this.$messageSource.removeAttr("height");
		this.$messageSource.css("height","");

		if(!this.options.alwaysOpen) {
			this.$messageSource = null;
			$(".publisher-controls", this.$rootNode).hide();
			$("input[type=file]", this.$rootNode).unbind("change").val("");
		} else {
			$("input[type=file]", this.$rootNode).val("");
		}

		$(".media-source, .select-media-source", this.$rootNode).hide();
		$(".publisher-media-type .select-media-source", this.$rootNode).hide();

		this.mediaType = null;
		this.mediaSource = null;

		$("div.media-selection", this.$rootNode).removeClass("active");
		
		if($.isFunction(this.options.onclose)) {
		    this.options.onclose.call(this, isCancel);
		}

	};

	Publisher.prototype.enable = function() {
		$(".post-to-button", this.$rootNode).removeClass("disabled").attr("disabled", "");
	};

	Publisher.prototype.disable = function() {
		$(".post-to-button", this.$rootNode).addClass("disabled").attr("disabled", "disabled");
	};

	Publisher.prototype.attach = function(element) {

		$(".publisher-contents", this.$rootNode).show();
		$(".pub-tabs", this.$rootNode).show();

		var ref = this;
		if (this.$messageSource && this.$messageSource.index(element) != -1) return;
		this.bindEvents();
		this.enable();
		this.$messageSource = $(element);

		if (!this.$messageSource.is(".inited")) {
			this.$messageSource.autoResize();
			this.$messageSource.addClass("inited");
		}

		if(!this.options.alwaysOpen) {
			this.$messageSource.bind("mousedown.msg", function() {
				$(this).addClass("focused");
				$(this).focus();
			});

			this.$messageSource.bind("blur.msg", function() {
				$(this).removeClass("focused");
			});

			$(document).bind("mousedown.msg", function(event) {
				if (!ref.lockOpen && !ref.mouseIsOver(event, ref.$rootNode) && (ref.$messageSource.val() == "" || ref.$messageSource.hasClass("input-default")) && !ref.mouseIsOver(event, ref.$messageSource)) {
					ref.close();
				}
			});
		}

		this.$messageSource.after(this.$rootNode);
		$(".publisher-controls", this.$rootNode).hide().stop();
		$(".publisher-controls", this.$rootNode).slideDown();

		$("input[type=file]", this.$rootNode).change(function() {
			if ($(this).val() != "") {
				ref.enable();
				//$(".pub-attachment", ref.$rootNode).show();
			}
		});
	};

	Publisher.prototype.mouseIsOver = function(event, el) {
		var off = el.offset();
		return (event.pageX >= off.left && event.pageX <= el.width() + off.left && event.pageY >= off.top & event.pageY <= el.height() + off.top);
	};

	Publisher.Configs = {
		video: {
			webcam: {
				embedFn: function(id) {
					swfobject.embedSWF(BB_BASE_PATH + "/library/swf/VideoCamera.swf", id + "_videoWebcamSWF", "500", "370", "9", "", "", {id:id + "_videoWebcamSWF", name:id + "_videoWebcamSWF", wmode:'transparent'});
					return id + "_videoWebcamSWF";
				},
				submit:publishVideoWebCam,
				formId:"video_webcam"
			},
			upload: {
				submit:publishVideoUpload,
				formId:"video_upload"
			}
		},
		picture: {
			webcam: {
				embedFn: function(id) {
					swfobject.embedSWF(BB_BASE_PATH + "/library/swf/StillCamera.swf", id + "_pictureWebcamSWF", "500", "370", "9", "", "", {id:id + "_pictureWebcamSWF", name:id + "_pictureWebcamSWF", wmode:'transparent'});
					return id + "_pictureWebcamSWF";
				},
				submit:publishPictureWebCam,
				formId:"picture_webcam"
			},
			upload: {
				submit:publishPictureUpload,
				formId:"picture_upload"
			}
		},
		message: {
			post: {
				submit:publishMessage,
				formId:"message_post"
			}
		}
	};

	Publisher.prototype.init = function() {
		this.bindEvents();
	};

	Publisher.prototype.bindEvents = function() {
		var pubRef = this;

		$(".publisher-controls-close", this.$rootNode).unbind();
		$(".post-to-button", this.$rootNode).unbind();
		$("div.media-selection", this.$rootNode).unbind();
		$("a.media-source-link", this.$rootNode).unbind();
		$(".select-media-source .option-close", this.$rootNode).unbind();
		$(".media-source .option-close", this.$rootNode).unbind();

		$(".publisher-controls-close", this.$rootNode).click(function() {
			pubRef.close(true);
			return false;
		});

		$("button.post-to-button", this.$rootNode).click(function() {
			var button = $(this);
			if(button.is(".WORLD")) {
				$(".recipients_form", pubRef.$rootNode).find("*[name=recipientDepth]").val("WORLD");
			}
			if(button.is(".ALL_MEMBERS")) {
				$(".recipients_form", pubRef.$rootNode).find("*[name=recipientDepth]").val("ALL_MEMBERS");
			}
			if(button.is(".FRIENDS")) {
				$(".recipients_form", pubRef.$rootNode).find("*[name=recipientDepth]").val("FRIENDS");	
			}
			pubRef.publish();
			return false;
		});

		$(".uploadPicture", this.$rootNode).click(function() {
			pubRef.disable();
			pubRef.mediaType = "picture";
			pubRef.mediaSource = "upload";
			pubRef.showMediaSourceUI(".picture-upload");
			return false;
		});

		$(".uploadVideo", this.$rootNode).click(function() {
			pubRef.disable();
			pubRef.mediaType = "video";
			pubRef.mediaSource = "upload";
			pubRef.showMediaSourceUI(".video-upload");
			return false;
		});

		$(".webcamPicture", this.$rootNode).click(function() {
			pubRef.disable();
			pubRef.mediaType = "picture";
			pubRef.mediaSource = "webcam";
			pubRef.showMediaSourceUI(".picture-webcam-record");
			return false;
		});
	};

	Publisher.prototype.showMediaSourceUI = function(target) {
		$(".media-source").hide();
		$(".media-type-inputs", this.$rootNode).show();
		if (Publisher.Configs[this.mediaType][this.mediaSource].embedFn) {
			var embed = Publisher.Configs[this.mediaType][this.mediaSource].embedFn(this.id);
			// associate publisher with flash movie so flash can call methods on this instance
			$("#" + embed).parent().data("publisher", this);
		}
		//set to true to prevent calling the emebed function for than once.
		this.initedTypes[this.mediaType] = true;
		$(target, this.$rootNode).show();
	};

	Publisher.prototype.getMessage = function() {
		if (this.$messageSource.hasClass("input-default")) {
			return "";
		} else {
			return this.$messageSource.val();
		}
	};

	Publisher.prototype.addMessageToForm = function($targetForm, msg) {
		$("#publisherMessageField").remove();
		var msgInput = $("<input />");
		msgInput.attr("type", "hidden");
		msgInput.attr("name", "messageBody");
		msgInput.attr("id", "publisherMessageField");
		msgInput.val(msg);
		$targetForm.append(msgInput);
	};

	Publisher.prototype.resetMessageSource = function() {
		this.$messageSource.val("").blur();
	};

	Publisher.prototype.addhiddenFieldToForm = function($targetForm, name, id, value) {
		$("#" + id).remove();
		var input = $("<input />");
		input.attr("type", "hidden");
		input.attr("name", name);
		input.attr("class", "temp");
		if (id != null) {
			input.attr("id", id);
		}
		input.val(value);
		$targetForm.append(input);
	};

	// Do not remove : This is called by StillCamera.as
	Publisher.prototype.processResponse = function(res) {
		var json = json_parse(res);
		this.enable();
		Activity.processUpdate(json);
	};

	Publisher.prototype.publish = function() {
		var ms = this.mediaSource;
		var mt = this.mediaType;
		if (ms == null) {
			mt = "message";
			ms = "post";
		}
        
        bb.trackPageEvent('Publisher', 'Publish', mt);
		Publisher.Configs[mt][ms].submit.call(this);
	};

	Publisher.prototype.getRecipient = function() {
		return $("*[name=recipientIdentifier]", this.$rootNode).val();
	};

	Publisher.prototype.processForm = function(form) {
		var pubRef = this;
		this.addMessageToForm(form, this.getMessage());
		if((this.mediaType == "message" || this.mediaType == null) && $.trim(this.getMessage()).length < 1) {
			return false;
		}

		if(this.$messageSource.data("parentEntity")) {
			var messageData = this.$messageSource.data("parentEntity").data("data");
		}

		//remove previous hidden fields. this is also done in addhiddenFieldToForm which can be removed.
		form.find("*[name=guid]").remove();
		form.find("*[name=recipient.ownerType]").remove();
		form.find("*[name=recipientDepth]").remove();

		if (messageData) {
			this.addhiddenFieldToForm(form, "parent.guid", "parentGuid", messageData.guid);
			this.addhiddenFieldToForm(form, "parent.ownerType", "parentType", messageData.activityType);
			this.addhiddenFieldToForm(form, "recipientDepth", "recipientDepth", messageData.recipientDepth);

		} else if(this.config == "invitation") {
			this.addhiddenFieldToForm(form, "parent.guid", "parentGuid", $(".recipients_form", pubRef.$rootNode).find("*[name=parent.guid]").val());
			this.addhiddenFieldToForm(form, "parent.ownerType", "parentType", $(".recipients_form", pubRef.$rootNode).find("*[name=parent.ownerType]").val());
			this.addhiddenFieldToForm(form, "recipientDepth", "recipientDepth", $(".recipients_form", pubRef.$rootNode).find("*[name=recipientDepth]").val());

		} else {
			var recipient = this.getRecipient();
			var recipientDepth = $(".recipients_form", pubRef.$rootNode).find("*[name=recipientDepth]").val();
			this.addhiddenFieldToForm(form, "recipientDepth", "recipientDepth", recipientDepth);
			this.addhiddenFieldToForm(form, "guid", "recipient_guid", recipient);
			this.addhiddenFieldToForm(form, "recipient.ownerType", "recipient_ownerType", "USER");
		}
		return true;
	};

	Publisher.prototype.alertMessageSent = function() {
		$.bbDialog.alert("Your message has been sent");
	};

	Publisher.prototype.openTwitterPublish = function(callbackFn, returnString) {
		var ref = this;
		this.lockOpen = true;
		this.$twitterform.find("*[name=twitterMessageBody]").val((this.$messageSource.val()).substring(0,120));
        $.getJSON("/community/media/Publish.action?twitterCreds=", function(data) {
                $("#publishertwitterUsername").val(data.username);
                $("#publishertwitterPassword").val(data.password);
                document.getElementById('publishertwitterRemember').checked = true;
        });

		this.$twitterform.bbDialog('buttons', {
			'OK': function() {
				//attempt to get FB session id
				try {
					ref.$twitterform.find("*[name=facebookSessionId]").val(FB.Facebook.apiClient.get_session().session_key);
					//If the user is connected, track the event
					if(ref.fbUserIsConnected) {

						bb.trackPageEvent('Publisher', 'Publish', "Facebook");
					}
				} catch(e) {
					//
				}

				if($("#publisherpublishToTwitter").is(":checked")) {
					bb.trackPageEvent('Publisher', 'Publish', "Twitter");
				}

				if(returnString) {
					callbackFn.call(ref, ref.$twitterform.find("form").serialize());
				} else {
					callbackFn.call(ref, ref.$twitterform.find("form").serializeArray());
				}
				this.close();
			},
			'Cancel': function() {
				ref.lockOpen = false;
				this.close();
			}
		});
		this.$twitterform.bbDialog('open');
	};

	Publisher.prototype.checkTwitterPublish = function($form) {
		return ($form.find("*[name=recipientDepth]").val() == "WORLD" && this.config == "main");
	};

	function thisMovie(movieName) {
		if (navigator.appName.indexOf("Microsoft") != -1) {
			return window[movieName];
		} else {
			return document[movieName];
		}
	}

	function publishVideoWebCam() {
		//var form = $("." + Publisher.Configs["video"]["webcam"]["formId"], this.$rootNode);
		//thisMovie(this.id + "_videoWebcamSWF").submit({a:1,b:2,c:3,d:4});
		//this.addMessageToForm(form, this.getMessage());
		//form.attr("target", "publisher_target");
		//form.submit();
	}

	function getMessageData() {
		return this;
	}

	function publishVideoUpload() {
		var form = $("." + Publisher.Configs["video"]["upload"]["formId"], this.$rootNode);
		form.find(".temp").remove();
		form.attr("target", "publisher_target");
		if (!this.processForm(form)) return false;

		var publishWindow = window.open("uploadTarget.html", "publishWindow", "width=400,height=100");
		if (!publishWindow) {
			alert("It seems you might have a pop up blocker enabled. Please disable your pop up blocker for blackboxrepublic.com!\n\n Thank you :)");
			this.close();
			return false;
		}
		this.addhiddenFieldToForm(form, "pubId", "pubId", this.id);
		publishWindow.onload = function() {
			form.attr("target", "publishWindow");
			form.submit();
		};
	}

	function publishPictureWebCam() {
		var form = $("." + Publisher.Configs["picture"]["webcam"]["formId"], this.$rootNode);
		form.find(".temp").remove();
		if (!this.processForm(form)) return false;
		this.disable();
		var ref = this;
		var submitFunction = function(extraParams) {
			if(extraParams) {
				$.each(extraParams, function() {
					ref.addhiddenFieldToForm(form, this.name, this.name, this.value);
				});
			}
			var params = form.serializeArray();
			thisMovie(ref.id + "_pictureWebcamSWF").submit({params:params, action:form.attr("action")});
		};

		if(this.checkTwitterPublish(form)) {
			this.openTwitterPublish(function(data) {
				submitFunction(data);
			}, false);
		} else {
			submitFunction();
		}
	}

	function publishPictureUpload() {
		var form = $("." + Publisher.Configs["picture"]["upload"]["formId"], this.$rootNode);
		form.find(".temp").remove();
		form.attr("target", "publisher_target");
		this.processForm(form);
		var ref = this;
		var submitFunction = function(extraParams) {
			if(extraParams) {
				$.each(extraParams, function() {
					ref.addhiddenFieldToForm(form, this.name, this.name, this.value);
				});
			}
			form.submit();
			ref.lockOpen = false;
			ref.close();
		};
		if(this.checkTwitterPublish(form)) {
			this.openTwitterPublish(function(data) {
				submitFunction(data);
			}, false);
		} else {
			submitFunction();
		}
	}

	function publishMessage() {
		var form = $("." + Publisher.Configs["message"]["post"]["formId"], this.$rootNode);
		form.find(".temp").remove();
		if (!this.processForm(form)) return false;
		var ref = this;
		var submitFunction = function(extraParams) {
			var params = form.serialize();
			if(extraParams) params += "&" + extraParams;
			$.post(form.attr("action"), params, function(data) {
				Activity.processUpdate(data);
				ref.close();
			}, "json");
		};

		if(this.checkTwitterPublish(form)) {
			this.openTwitterPublish(function(data) {
				submitFunction(data);
			}, true);
		} else {
			submitFunction();
		}
	}

	Publisher.proxyCall = function(swfID, fn, arg) {
		// using the parent container, rather than the object due to jQuery 1.4.1 bugs...
		var swf = $("#" + swfID).parent();
		var publisher = swf.data("publisher");
		try {
			publisher[fn](arg);
		} catch(e) {
		}
	};

})();
