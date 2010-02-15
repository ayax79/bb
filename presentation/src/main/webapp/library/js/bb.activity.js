/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */


/*******************************************************************************
 * ACTIVITY
 *
 * Processes updates returned from the server
*******************************************************************************/
var Activity = {
	/**
	 * Creates DOM nodes, registers the object and triggers events
	 * @param data
	 */
	processUpdate: function(data) {
		var eventType;
		if(data.ownerType == Activity.Factory.ObjectType.MESSAGE) {
			eventType = Activity.Event.MESSAGE_EVENT;
		} else if(data.ownerType == Activity.Factory.ObjectType.MEDIA) {
			eventType = Activity.Event.MEDIA_EVENT;
		} else if(data.ownerType == Activity.Factory.ObjectType.OCCASION) {
			eventType = Activity.Event.OCCASION_EVENT;
		}
		if(eventType) {
			Activity.triggerEvent(eventType, data);
		}
	},
	/**
	 * Assigns a data store to a DOM node, and sets its parentEntity (if it's a reply).
	 * @param objects
	 */
	registerObject:function(objects, isFromTemplate) {
		for(var i = 0; i < objects.length; i++)
		{
			var object = objects[i];
			var objectType = this.getActivityTypePrefix(object);
			if(object.children) {
				this.registerObject(object.children, isFromTemplate);
			}
			$.each($("." + objectType + "_" + object.guid), function() {
				var entity = $(this);
				entity.data("data", object);
				var msgBody;
				if(objectType == "article") {
					msgBody = entity.find(".article-meat");
					entity.find(".comment-publish").data("parentEntity", $(this));
				} else if(objectType == "comment") {
					msgBody = entity.find(".comment-meat");
				}
				Activity.Utils.parseHTMLLinks(msgBody);
				msgBody.oembed();
				Activity.Utils.modifyPostActions(objectType, entity, object, isFromTemplate);
			});
		}
	},

	getActivityTypePrefix:function(data) {
		if(data.parentActivity && data.parentActivity.guid && data.parentActivity.guid != data.guid) {
			return "comment";
		} else {
			return "article";
		}
	}
};

/*******************************************************************************
  ACTIVITY EVENTS
*******************************************************************************/

Activity.events = []; // Holds all registered Activity Events

/**
 * Activity event
 * @param type
 * @param callback
 * @param scope
 */
Activity.Event = function(type, callback, scope) {
	this.type = type;
	this.callback = callback;
	this.scope = scope || window;
};

// Acitivty event constants
Activity.Event.MESSAGE_EVENT = "message";
Activity.Event.MEDIA_EVENT = "media";
Activity.Event.OCCASION_EVENT = "occasion";

/**
 * Binds event listeners
 * @param type
 * @param callback
 * @param scope
 */
Activity.bindEvent = function(type, callback, scope) {
	this.events.push(new Activity.Event(type, callback, scope));
};

Activity.unbindEventsByScope = function(scope) {
	for(var i = 0; i < this.events.length; i++) {
		if(this.events[i].scope == scope) {
			this.events.splice(i,1);
			i--;
		}
	}
};
    
/**
 * Calls registered event listeners by type
 * @param type type of event to trigger
 * @param data argument data for event listener
 */
Activity.triggerEvent = function(type, data) {
   	$.each(this.events, function(i, event) {
		var temp = type.split(".");
		type = temp[0];
		namespace = temp[1]||null;
		if(event.type == type) {
			event.callback.call(event.scope, data);
		}
	});
};

/*******************************************************************************
  ACTIVITY FACTORY
*******************************************************************************/

// TODO: Add event listener removal and cleanup methods!!

Activity.Factory = {};

Activity.Factory.ObjectType = {};
Activity.Factory.ObjectType.MESSAGE  = "MESSAGE";
Activity.Factory.ObjectType.MEDIA    = "MEDIA";
Activity.Factory.ObjectType.OCCASION = "OCCASION";

Activity.Factory.ObjectClass = {};
Activity.Factory.ObjectClass.PARENT  = "PARENT";
Activity.Factory.ObjectClass.CHILD   = "CHILD";

/**
 * Creates and returns a dom node based on type/class
 * @param data
 */
Activity.Factory.createObject = function(data) {
	var objectClass;
	var domObject;
	if(data.parentActivity && data.parentActivity.guid) {
		objectClass = Activity.Factory.ObjectClass.CHILD;
	} else {
		objectClass = Activity.Factory.ObjectClass.PARENT;
	}
	switch(data.ownerType) {
		case Activity.Factory.ObjectType.MESSAGE:
			domObject = Activity.Factory.createMessage(objectClass, data);
			break;
		case Activity.Factory.ObjectType.MEDIA:
			domObject = Activity.Factory.createMedia(objectClass, data);
			break;
		default:
			domObject = null;
			alert("Unknown ownerType: " + data.ownerType);
	}
	return domObject;
};

/**
 * If url is null, returns default icon
 * @param url
 */
Activity.Factory.resolveImageUrl = function(url) {
	return url || DEFAULT_PROFILE_IMAGE;
};

/**
 * If url is null, returns default icon
 * @param url
 */
Activity.Factory.getDeleteUrl = function(object) {
	var baseUrl;
	if(object.ownerType == Activity.Factory.ObjectType.MESSAGE) {
		baseUrl = bb.urls.activity.message.deleteMessage;
	} else if(object.ownerType == Activity.Factory.ObjectType.MEDIA) {
		baseUrl = bb.urls.activity.message.deleteMedia;
	} else {
		baseUrl = "#";
	}
	baseUrl += "&recipientDepth=" + object.recipientDepth;
	if(object.parentActivity && object.parentActivity.guid) {
		baseUrl += "&parentGuid=" + object.parentActivity.guid;	
	}
	return baseUrl;
};

/**
 * Inserts core message fields into a message node
 * @param template
 * @param data
 */
Activity.Factory.addCoreMessageFields = function(template, data) {
	var body = (data.body)?data.body:"";
	body = body.replace(/[\n\r]/gm, "<br/>");
	template.find(".article-meat").html(body).end()
			.find(".article-date abbr").attr("title", data.postDate).text(data.postDate).end()
			.find(".article-user a.user-link").text(data.senderProfile.senderDisplayName).end()
			.find(".delete-link").attr("href", Activity.Factory.getDeleteUrl(data) + "&guid=" + data.guid).end()
			.find(".bookmark-link").attr("href", bb.urls.bookmark.create + "&target.guid=" + data.guid + "&target.ownerType=" + data.ownerType).end()
			.find(".collapse-link").attr("href", "#" + data.sender.guid).end()
			.find(".expand-link").attr("href","#" + data.sender.guid).end()
			.find(".article-pict img.profile-image").attr("src", Activity.Factory.resolveImageUrl(data.senderProfile.senderProfileImage)).end()
			.find(".comments-container").addClass("comments_" + data.guid).end();
	template.find(".article-user a.user-link").attr("href", template.find(".article-user a.user-link").attr("href") + data.senderProfile.senderDisplayUsername).end();
	template.find(".article-pict a.userProfileLink").attr("href", template.find(".article-pict a.userProfileLink").attr("href") + "/" + data.senderProfile.senderDisplayUsername).end();
	template.addClass(Activity.getActivityTypePrefix(data) + "_" + data.guid)
			.addClass("U" + data.sender.guid);

	template.find(".thread-starter").text(data.senderProfile.senderDisplayName);
	template.find(".create-date").text(data.prettyDate);

	//remove mini profiles for current user
//	if(data.sender.guid == CurrentUser.guid) {
//		template.find(".allow-mini-profile").removeClass("allow-mini-profile");
//	}
	return template;
};

/**
 * Inserts core comment fields into a comment node
 * @param template
 * @param data
 */
Activity.Factory.addCoreCommentFields = function(template, data) {
	var body = (data.body)?data.body:"";
	body = body.replace(/[\n\r]/gm, "<br/>");
	template.find(".comment-meat").html(body).end()
			.find(".article-comment-date abbr").attr("title", data.postDate).text(data.postDate).end()
			.find(".article-comment-user a.user-link").text(data.senderProfile.senderDisplayName).end()
			.find(".delete-link").attr("href", Activity.Factory.getDeleteUrl(data) + "&guid=" + data.guid).end()
			.find(".bookmark-link").attr("href", bb.urls.bookmark.create + "&target.guid=" + data.guid + "&target.ownerType=" + data.ownerType).end()
			.find(".collapse-link").attr("href", "#" + data.sender.guid).end()
			.find(".expand-link").attr("href","#" + data.sender.guid).end()
			.find(".article-comment-pict img.profile-image").attr("src", Activity.Factory.resolveImageUrl(data.senderProfile.senderProfileImage)).end();
	template.addClass(Activity.getActivityTypePrefix(data) + "_" + data.guid);
	template.addClass("U" + data.sender.guid);
	template.find(".article-comment-user a.user-link").attr("href", template.find(".article-comment-user a.user-link").attr("href") + data.senderProfile.senderDisplayUsername).end();
	template.find(".article-comment-pict a.userProfileLink").attr("href", template.find(".article-comment-pict a.userProfileLink").attr("href") + "/" + data.senderProfile.senderDisplayUsername).end();

	//remove mini profiles for current user
//	if(data.sender.guid == CurrentUser.guid) {
//		template.find(".allow-mini-profile").removeClass("allow-mini-profile");
//	}

	return template;
};

/**
 * Returns the appropriate DOM nodes for displaying the media for a message
 * @param data
 */
Activity.Factory.getArticleMedia = function(objectClass, data) {
	var template;
	if(data.mimeType == "application/octet-stream") {
		if(objectClass == Activity.Factory.ObjectClass.PARENT) {
			template = $("#video-template a").clone();
		} else if(objectClass == Activity.Factory.ObjectClass.CHILD) {
			template = $("#comment-video-template a").clone();
		}
		template.attr("id", "video_" + data.guid)
				.attr("href", data.location);
		return template;
	} else if(data.mimeType.indexOf("image") != -1) {
		template = $("#image-template a").clone();
		template.find("img").attr("src", data.thumbnailLocation).end()
				.attr("href", data.location).end();
		var img = template.find("img");
		Activity.Utils.imgLoadHander(img);
		return template;
	} else {
		return $('<span></span>');
	}
};

Activity.Factory.getRecipientDepthLabel = function(data) {
	return((data.recipientDepth == "ALL_MEMBERS")?"All members":((data.recipientDepth == "FRIENDS")?"Friends":((data.recipientDepth == "WORLD")?"Public life":((data.recipientDepth == "DIRECT")?"Direct":data.recipientDepth))));
};

/**
 * Creates and returns a root or child message template
 * @param objectClass
 * @param data
 */
Activity.Factory.createMessage = function(objectClass, data) {
	var message, template;
	if(objectClass == Activity.Factory.ObjectClass.PARENT) {
		if(data.virtualGift) {
			template = $("#gift-message-template .article").clone();
		} else {
			template = $("#message-template .article").clone();
		}
		message = Activity.Factory.addCoreMessageFields(template, data);
		message.find(".privacy_control .menu_selection").addClass(data.recipientDepth).end()
				.find(".pval").text(this.getRecipientDepthLabel(data));
	} else if(objectClass == Activity.Factory.ObjectClass.CHILD) {
		template = $("#message-comment-template .article-comment").clone();
		message = Activity.Factory.addCoreCommentFields(template, data);
	} else {
		alert("Message class unknown: " + objectClass);
		message = null;
	}
	if(message && Activity.Stream.isCollapsedPost(data.sender.guid)) {
		message.addClass("collapsed-post");
	}
	return message;
};

/**
 * Creates and returns a root or child media message template
 * @param objectClass
 * @param data
 */
Activity.Factory.createMedia = function(objectClass, data) {
	var template, message;
	var body = (data.body)?data.body:"";
	body = body.replace(/[\n\r]/gm, "<br/>");
	if(objectClass == Activity.Factory.ObjectClass.PARENT) {
		if(data.virtualGift) {
			template = $("#gift-media-template .article").clone();
		} else {
			template = $("#media-template .article").clone();
		}
		message = Activity.Factory.addCoreMessageFields(template, data);
		message.find(".article-media").append(Activity.Factory.getArticleMedia(objectClass, data)).end();
		message.find(".privacy_control .menu_selection").addClass(data.recipientDepth).end()
				.find(".pval").text(this.getRecipientDepthLabel(data));
	} else if(objectClass == Activity.Factory.ObjectClass.CHILD) {
		template = $("#media-comment-template .article-comment").clone();
		message = Activity.Factory.addCoreCommentFields(template, data);
		message.find(".article-media").append(Activity.Factory.getArticleMedia(objectClass, data)).end();
	} else {
		alert("Message class unknown: " + objectClass);
		message = null;
	}

	if(message && Activity.Stream.isCollapsedPost(data.sender.guid)) {
		message.addClass("collapsed-post");
	}

	return message;
};

/*******************************************************************************
 * ACTIVITY STREAM OBJECT
*******************************************************************************/
Activity.StreamObject = function(domObject, objectClass, objectType, data) {
	this.domObject = domObject;
	this.objectClass = objectClass;
	this.objectType = objectType;
	this.data = data;
};

/*******************************************************************************
 * ACTIVITY STREAM
*******************************************************************************/

Activity.Stream = function(selector) {
	this.$container = $(selector);
	this.active = false;
	Activity.Stream.registeredStreams.push(this);
	this.currentFilter = null;
	this.filterParamName = "filter";
	this.startIndexParamName = "bounds.startIndex";
	this.maxResultsParamName = "bounds.maxResults";

};

Activity.Stream.PAGESIZE = 10;

Activity.Stream.prototype.setStreamHeader = function(hdr) {
	this.$header = hdr;
};

Activity.Stream.prototype.getMsgOffsets = function() {
	return $(".article", this.$container).length;
};

Activity.Stream.prototype.setFilterParamName = function(str) {
	this.filterParamName = str;
};

Activity.Stream.prototype.setStartIndexParamName = function(str) {
	this.startIndexParamName = str;
};

Activity.Stream.prototype.setMaxResultsParamName = function(str) {
	this.maxResultsParamName = str;
};

Activity.Stream.prototype.bindShowMore = function($showMore, url) {
	var ref = this;
	this.$showMoreButton = $showMore;
	this.$showMoreButton.click(function() {
		var offset = ref.getMsgOffsets();

		paramsObj = {};
		paramsObj["view"] = "json";
		if(ref.currentFilter !== null) {
			paramsObj[ref.filterParamName] = ref.currentFilter;
		}
		paramsObj[ref.startIndexParamName] = offset;

		paramsObj[ref.maxResultsParamName] = Activity.Stream.PAGESIZE;

		$.post(url, paramsObj, function(response) {
			if(response.length < 1) {
				$.bbDialog.alert("No more results to display.");	
			} else {
				for(var i = 0; i < response.length; i++) {
					ref.addStreamObject(response[i], true);
					if(response[i].children.length > 0) {
						for(var ii = 0; ii < response[i].children.length; ii++) {
							ref.addStreamObject(response[i].children[ii]);
						}
					}
				}
			}
		}, "json");

		return false;
	});
};

/**
 * TODO: I don't really like that the Stream object is setting buttons states and the header text.
 * Doesn't seem clean or a good design pattern, but works for now...
 */

Activity.Stream.prototype.bindFilterButtons = function($scope) {
	var ref = this;
	$("li a", $scope).click(function() {
		var url = $(this).attr("href");
		ref.currentFilter = Activity.Utils.extractRequestParam(url, ref.filterParamName);
		$("li a", $scope).removeClass("active");
		$(this).addClass("active");
		bb.trackPageView(url);

		ref.$container.load(url, function() {
			bb.pageInit.bindArticleEvents(ref.$container);
		});
		return false;
	});
};

Activity.Stream.registeredStreams = [];

/**
 * Adds a guid to the "cu" cookie for registering a user's posts as collapsed
 * @param guid user entity guid
 */
Activity.Stream.registerCollapsedPosts = function(guid) {
	var cu;
	if($.cookie("cu")) {
		cu = $.cookie("cu").split(",");
	} else {
		cu = [];
	}
	if(cu.indexOf(guid) == -1) {
		cu.push(guid);
	}
	var expireDate = new Date();
	expireDate.setYear(expireDate.getFullYear() + 1);
	$.cookie("cu", cu.join(","), {expires:expireDate});
};

/**
 * Removes a guid from the "cu" cookie for unregistering a user's posts as collapsed
 * @param guid user entity guid
 */
Activity.Stream.unRegisterCollapsedPosts = function(guid) {
	var cu;
	if($.cookie("cu")) {
		cu = $.cookie("cu").split(",");
	    cu.splice(cu.indexOf(guid), 1);
		$.cookie("cu", cu.join(","));
	}
};

Activity.Stream.isCollapsedPost = function(guid) {
	var cu;
	if($.cookie("cu")) {
		cu = $.cookie("cu").split(",");
	} else {
		cu = [];
	}
	return cu.indexOf(guid) != -1;
};

Activity.Stream.prototype.activate = function() {
	this.active = true;
};

Activity.Stream.unregisterStreams = function() {
	for(var i = 0; i < Activity.Stream.registeredStreams.length; i++)
	{
		Activity.Stream.registeredStreams[i].active = false;
		Activity.unbindEventsByScope(Activity.Stream.registeredStreams[i]);
		Activity.Stream.registeredStreams.splice(i, 1);
		i--;
	}
};

Activity.Stream.prototype.addEventTypes = function(types, namespace) {
	for(var i = 0; i < types.length; i++)
	{
		 Activity.bindEvent(types[i], this.addStreamObject, this);
	}
};

Activity.Stream.prototype.addStreamObject = function(data, append) {
	append = append || false;
	if(!this.active) return;
	var domObject = Activity.Factory.createObject(data);
	var objectClass, objectType;
	if(data.ownerType == Activity.Factory.ObjectType.MESSAGE) {
		objectType = Activity.Factory.ObjectType.MESSAGE;
	} else if(data.ownerType == Activity.Factory.ObjectType.MEDIA) {
		objectType = Activity.Factory.ObjectType.MEDIA;
	} else if(data.ownerType == Activity.Factory.ObjectType.OCCASION) {
		objectType = Activity.Factory.ObjectType.OCCASION;
	}
	if(data.parentActivity && data.parentActivity.guid) {
		objectClass = Activity.Factory.ObjectClass.CHILD;
	} else {
		objectClass = Activity.Factory.ObjectClass.PARENT;
	}
	if(objectClass == Activity.Factory.ObjectClass.CHILD) {
		var parentContainer = $(".article_" + data.parentActivity.guid, this.$container);
		parentContainer.find(".comments-container").append(domObject);
	} else {
		if(append) {
			this.$container.append(domObject);
		} else {
			this.$container.prepend(domObject);
		}
	}
//	bb.embedFlowPlayer($("a.video", domObject).attr("id"));
//	bb.embedFlowPlayer($("a.video-small", domObject).attr("id"));
	Activity.registerObject([data], true);
	bb.pageInit.bindArticleEvents(domObject);
};

Activity.Utils = {};

/**
 * Modifies article actions menu to remove items based on current user
 * @param entity
 * @param object
 */
Activity.Utils.modifyPostActions = function(objectType, entity, object, isFromTemplate) {
	var filterMenuScope;

	if(objectType == "article") {
		filterMenuScope = entity.find(".article-actions");
	} else if(objectType == "comment") {
		filterMenuScope = entity.find(".comment-actions");
	}

	//if the html was generated from the JSP then we already handle this logic there
	if(isFromTemplate) {
		if(object.sender.guid != CurrentUser.guid) {
			filterMenuScope.find(".delete-link").parent().remove();
		} else {
			filterMenuScope.find(".ignore-link").parent().remove();
			filterMenuScope.find(".block-link").parent().remove();
		}
	}
	filterMenuScope.find(".article-filter-menu li:first-child").addClass("first");
};

// TODO: Update this so that the textnodes don't get wrapped in a span tag or instead, get wrapped in inline-block
Activity.Utils.parseHTMLLinks = function(node) {
	var uriRe = /\b(?:(?:(?:file|gopher|news|nntp|telnet|http|ftp|https|ftps|sftp|irc):\/\/(?:[^:@\s\.\,\/\\]+(?::[^:@\s\.\,\/\\]+)?@)?)|(?:www\.|ftp\.|irc\.))(?:(?:[\w\.-]+\.[a-zA-Z]{2,6})|(?:[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(?:\:\d+)?(?:\/(?:[\w\-\.\?\!\,\'\/\\\+&%\$#\=~:;\[\]\(\)]+[\w\-\/\\\+&%\$#\=~]+)?)?/gi;
	var elements = node.contents().not($("a"));
	$.each(elements, function() {
		if (this.nodeType == 3) {
			if (this.nodeValue.match(uriRe))
			{
				var v = this.nodeValue.replace(/&/g, '&amp;');
				v = v.replace(/</g, '&lt;');
				v = v.replace(/>/g, '&gt;');
				v = v.replace(uriRe, function(str) {
					var url = str;
					if (url.indexOf('www.') == 0)
						url = 'http://' + url;
					if (url.indexOf('ftp.') == 0)
						url = 'ftp://' + url;
					if (url.indexOf('irc.') == 0)
						url = 'irc://' + url;
					return '<span class="article-link"><a href="' + url + '" target="_blank">' + str + '</a></span>';
				});
				var s = document.createElement('div');
				s.className = 'node-wrap';
				document.documentElement.appendChild(s);
				this.parentNode.replaceChild(s, this);
				s.innerHTML = v;
			}
		}
		else if (this.tagName != "A") {
			Activity.Utils.parseHTMLLinks($(this));
		}
	});
};

/* Parses a URL and returns an object of query params */
Activity.Utils.parseURL = function(url) {
	var params = {};
	if(url.indexOf("?") != -1) {
		var kvps = url.split("?").pop().split("&");
		for(var i = 0; i < kvps.length; i++) {
			var kvp = kvps[i].split("=");
			params[kvp[0]] = kvp[1];
		}
	}
	return params;
};

Activity.Utils.extractRequestParam = function(url, key) {
	return Activity.Utils.parseURL(url)[key];
};

/* TODO : Remove this and point to bb.utils */
Activity.Utils.imgPoll = function(e, obj) {
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
};

Activity.Utils.imgLoadHander = function($img) {
	$.post(bb.urls.checkUrl, {url:$img.attr("src")}, function(response) {
		if(response == "404") {
			setTimeout(function() {
				Activity.Utils.imgLoadHander($img);
			}, 1000);
		} else if( response == "302" || response == "200") {
			$img[0].src = $img[0].src + "?ch=" + new Date().getTime();
			$img.trigger("loadComplete");
		}
	});
};