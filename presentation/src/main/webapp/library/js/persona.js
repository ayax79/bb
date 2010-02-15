var updateRelationshipsList = function() {
	$("#tog_list_container").load(bb.urls.social.relationship.load, {displayMode:'horizontal', ch: new Date().getTime()}, function() {
		bb.pageInit.bindMiniProfiles($(this));
	});
};

function popUpProfileUploadDialog() {
	target = $("#profile_img_id");
	var win = bb.imageUploader.open(BB_BASE_PATH);

	$(window).unbind(".reg4").bind("loaderClose.reg4", function(event, avatarUrl, profileUrl) {
		var avatar_v = $("#avatar").imageCropper("serialize");
		var persona_v = $("#persona").imageCropper("serialize");
		var url = bb.urls.user.uploadActionBean + ";jsessionid=" + CURRENT_SESSION_ID;
		$.get(url,
		{'_eventName':'cropImage',
			imageX: persona_v.x,
			imageY: persona_v.y,
			imageWidth: persona_v.w,
			imageHeight: persona_v.h,
			avatarX:avatar_v.x,
			avatarY:avatar_v.y,
			avatarW:avatar_v.w,
			avatarH:avatar_v.h},
				function(image_location) {
					var image_location_url = image_location;
					$(target).attr('src', image_location_url);
					Activity.Utils.imgLoadHander($(target));
				});
		$(win).bbDialog('remove');
	});
	return false;
}

var createACDialog = function(el) {
	var d = $("<div/>");
	$("body").append(d);
	d.load(BB_BASE_PATH + "/ajax/find-user.jspf", function() {
		var dialog = $(this).bbDialog({
			draggable:false,
			title:"Select user",
			modal:true,
			buttons:{
				'Cancel':function() {
					this.remove();
				}
			},
			onblur:function() {
				$(this).bbDialog('remove');
			}
		});
		d.find(".direct-autocomplete").autocomplete(bb.urls.search.autocomplete, {
			onItemSelect:function(a, b) {
				$("#rel_guid").val($(a).attr("extra"));
				$("#rel_user_name").val($(a).text());
				$(dialog).bbDialog("remove");
			},
			lineSeparator:"^"
		});
	});
	return false;
};

$(document).ready(function() {

	$(".viewMoreLink").click(function() {
	    var $link = $(this);
		var identifier = bb.utils.returnHashParam($(this));
		var viewAllType = (($link.is(".more-friends-link"))?"friends":($link.is(".more-following-link"))?"following":($link.is(".more-viewedBy-link"))?"viewedBy":"");
		var d = $('<div></div>');
		$("body").append(d);
		d.load(bb.urls.persona.showmore, {identifier: identifier, viewAllType: viewAllType}, function() {
			var dialog = $(this).bbDialog({
				draggable:false,
				title:"Show more",
				modal:true,
				buttons:{
					'Close':function() {
						this.remove();
					}
				}
			});
		});
		return false;		
	});

	$("#edit-relationships").bind("click", function() {
		var d = $("<div/>");
		$("body").append(d);
		var url = bb.urls.social.relationship.edit + "&ch=" + new Date().getTime();
		d.load(url, function() {
			$(this).bbDialog({
				draggable:false,
				title:"Edit Relationships",
				dialogClass:"nopad-dialog",
				modal:true,
				buttons:{
					'OK':function() {
						this.remove();
						updateRelationshipsList();
						$("#remove-relationship").die();
					}
				}
			});
			// bind tokenized input
			bb.autocomplete.bind($("#rel_guid"), 1);

			$(".remove-relationship").live("click", function() {
				var link = $(this);
				var guid = link.attr("href").split("#").pop();
				$.post(bb.urls.social.relationship.remove, {guid:guid}, function(response) {
					link.parents(".relationship-container").remove();
				});
				return false;
			});

			$("#add-relationship", $(this)).click(function() {
				if (d.find("#rel_guid").val() == "") {
					$.bbDialog.alert("Please enter a name for the person<br/>that you are connecting with.", {onfocus:function() {
						$(this).bbDialog('remove');
					}});
					return false;
				}
				if (d.find("#rel_label").val() == "") {
					$.bbDialog.alert("Please coin your relationship.", {onfocus:function() {
						$(this).bbDialog('remove');
					}});
					return false;
				}
				if (d.find("#rel_label").val().length > 30) {
					$.bbDialog.alert("Please use 30 or less characters for relationship description.", {onfocus:function() {
						$(this).bbDialog('remove');
					}});
					return false;
				}

				var params = d.find(".addRelationshipForm").serialize();
				params += "&ch=" + new Date().getTime();
				$.post(bb.urls.user.relationshipsAddUrl, params, function(response) {
					d.find(".no-relationships-message").remove();
					d.find(".relationships-container").prepend(response);
					d.find(".addRelationshipForm input").val("");
					$("#rel_guid").tokenInput('reset');
				}, "html");
				return false;
			});
		});
		return false;
	});

	/******************************************************************
	 * VOUCHING
	 *****************************************************************/
	$("#vouch_btn").click(function() {
		if($(this).is(".disabled")) {
			if($(this).is(".blocked")) {
				$.bbDialog.alert(bb.resources.vouch.blockedMessage);
				return false;
			}
			if($(this).is(".unvouched")) {
				$.bbDialog.alert(bb.resources.vouch.unVouchedMessage);
				return false;
			}
			return false;
		}
		$('#vouchForm').bbDialog('open');
		return false;
	});

	$('#vouchForm').bbDialog({
		title:'Vouch ' + currentPersonaUser.name,
		modal:true,
		autoOpen:false,
		buttons: {
			'OK':function() {
				$.post(bb.urls.social.vouch.create, $('#vouchForm form').serializeArray(), function() {
					var vouchButton = $("#vouch_btn");
					var newVouchCount =  parseInt(vouchButton.find("span.vouch-icon").text()) + 1;
					vouchButton.addClass("disabled").find("span.vouch-label").text("Vouches").end().find("span.vouch-icon").text(newVouchCount).attr("id", "vouched");
					$('#vouchForm').bbDialog('close');
				});
			},
			'Cancel':function() {
				this.close();
			}
		}
	});

    /******************************************************************
	 * WISHING
	 *****************************************************************/
	$("#wish_btn").click(function() {
		if($(this).is(".disabled")) {
			if($(this).is(".blocked")) {
				$.bbDialog.alert(bb.resources.wish.blockedMessage);
				return false;
			}
			if($(this).is(".unvouched")) {
				$.bbDialog.alert(bb.resources.wish.unVouchedMessage);
				return false;
			}
			return false;
		}
		$('#wishForm').bbDialog('open');
		return false;
	});

	$('#wishForm').bbDialog({
		title:'Wish ' + currentPersonaUser.name,
		modal:true,
		autoOpen:false,
		buttons: {
			'OK':function() {
				$.post(bb.urls.social.relationship.wish, $('#wishForm form').serializeArray(), function(response) {
					$("#wish_btn").addClass("disabled");
					$(".wish-label").removeClass("active");
					$("#" + response + "_LABEL").addClass("active");
					$("#wished-icon").removeClass("NEITHER").removeClass("WISHED").removeClass("WISHED_BY").removeClass("MUTUAL").addClass(response);
					$('#wishForm').bbDialog('close');
				});
			},
			'Cancel':function() {
				this.close();
			}
		}
	});

//	bb.pageInit.bindMiniProfiles($(".pro_con_main1"));

	bb.pageInit.bindMiniProfiles($("#tog_list_container"));
	bb.pageInit.bindMiniProfiles($("#friends-panel"));
	bb.pageInit.bindMiniProfiles($("#following-panel"));
	bb.pageInit.bindMiniProfiles($("#viewed-panel"));

//	bb.pageInit.bindMiniProfiles($("#relations"));

	bb.pageInit.bindCollapsiblePanels();
	
	var position = $('#profile_img_id').position(),
		disabledTabs;

	$("#profile_upload_id").show();
	$("#profile_upload_id").css({ top:position.top, left: position.left});

	//currentUserVouched is set in the template
	//and determines if certain tabs are disabled.

	if(disablePersonaTabs) {
		disabledTabs = [1,2,3]
	} else {
		disabledTabs = [];
	}
	$("#stream-tabs_t").tabs({disabled: disabledTabs});

	$.fn.search = function() {
		return this.focus(function() {
			if (this.value == this.defaultValue) {
				this.value = "";
			}
		}).blur(function() {
			if (!this.value.length) {
				this.value = this.defaultValue;
			}
		});
	};
	bb.pageInit.bindSliders($(".info-sliders"));
	$(".bbslider", $(".info-sliders")).slider('disable');

	$("#searchbox").search();

	$(".relatedUsersProfile").miniPersona({personaType:'member'});

	$("#persona-icons .compose-link").click(function() {

		if($(this).is(".disabled")) {
			if($(this).is(".blocked")) {
				$.bbDialog.alert(bb.resources.messaging.blockedMessage);
				return false;
			}
			if($(this).is(".unvouched")) {
				$.bbDialog.alert(bb.resources.messaging.unVouchedMessage);
				return false;
			}
			return false;
		}

		var guid = $(this).attr("href").split("#").pop();
		var composer = bb.messaging.compose({userIdentifier:guid});
		composer.bind("messageSent", function() {
			//w00t.
		});
		return false;
	});

	var closeGiftPublisher = function(isCancel) {
		$('#gift-publisher-container').bbDialog('close');
		if (!isCancel) {
			$.bbDialog.info("Your gift has been sent!");
		}
	};

	var giftPublisher = new Publisher("#giftPublisher", "gift", {
		alwaysOpen:true,
		onclose:closeGiftPublisher
	});

	$("#gift-button").click(function() {
		if($(this).is(".disabled")) {
			if($(this).is(".blocked")) {
				$.bbDialog.alert(bb.resources.gifting.blockedMessage);
				return false;
			}
			if($(this).is(".unvouched")) {
				$.bbDialog.alert(bb.resources.gifting.unVouchedMessage);
				return false;
			}
			return false;
		}
		$('#gift-publisher-container').bbDialog('open');
		return false;
	});

	bb.pageInit.bindDefaultTextBoxes($("#gift-publisher-container"));
	giftPublisher.attach($("#gift-publisher"));
	$('#gift-publisher-container').bbDialog({
		title: 'Send a gift!',
		modal: true,
		autoOpen:false,
		hideButtons:true
	});

});

/**************************************************
Pictures tab
**************************************************/

var picturesTab = function($container) {
	var ref = this;
	this.$container = $container;
	//This assumes a default state where the albums view page is pre-populated
	$("#view-create-album").click(function() {
		ref.getCreateAlbumView();
		return false;
	});
};

/**************************************************
EVENT BINDING
**************************************************/

picturesTab.prototype.bindAlbumCovers = function(scope) {
	var ref = this;
	$("a.album-link").click(function() {
		ref.getAlbumView(bb.utils.returnHashParam($(this)));
		return false;
	});
	$(".delete-album-link", ref.$container).click(function() {
		ref.deleteAlbum($(this));
		return false;
	});

	$("#main-albums .album-cover").hover(function() {
		$(this).find(".image-action-link, .delete-image-link").show();
	}, function() {

		$(this).find(".image-action-link, .delete-image-link").hide();
	});

	$(".image-action-menu a").click(function() {
	    var $link = $(this);
		var $menu = $(this).parents(".image-action-menu");
		var params = bb.utils.returnHashParam($link) + "&identifier=" + currentPersonaUser.guid;
		$.post(bb.urls.photos.setAlbumPermissions, params, function() {
			$menu.find("a").removeClass("active");
			$link.addClass("active");
		});
		return false;
	});

	$(".image-action-menu", scope).mouseleave(function() {
		if(!$(this).is(".lockOpen")) {
			$(this).hide();
		}
	});

	$(".image-action-link", scope).click(function() {
		$(this).parent().find(".image-action-menu").toggle();
		return false;
	});

};

picturesTab.prototype.bindImages = function(scope) {
	var ref = this;

	$(".delete-image-link", scope).click(function() {
		$(this);
		ref.deleteImage($(this));
		return false;
	});

	$(".image-action-menu a").click(function() {
	    var $link = $(this);
		var $menu = $(this).parents(".image-action-menu");
		var params = bb.utils.returnHashParam($link) + "&identifier=" + currentPersonaUser.guid;
		$.post(bb.urls.photos.setPermissions, params, function() {
			$menu.find("a").removeClass("active");
			$link.addClass("active");
		});
		return false;
	});

	$(".image-action-menu", scope).mouseleave(function() {
		if(!$(this).is(".lockOpen")) {
			$(this).hide();
		}
	});

	$(".image-action-link", scope).click(function() {
		$(this).parent().find(".image-action-menu").toggle();
		return false;
	});

	$(".view-image-link").lightBox({showImgNbr:'no'});
};

/**************************************************
CRUD
**************************************************/

picturesTab.prototype.deleteAlbum = function($link, callback) {
	var ref = this;
	$.bbDialog.confirm("Are you sure you want to delete this album?", function() {
		var guid = bb.utils.returnHashParam($link);
		ref.deleteAlbumCall(guid, callback);
	});
};

picturesTab.prototype.deleteAlbumCall = function(guid, element, callback) {
	var ref = this;
	$.post(bb.urls.photos.deleteAlbum, {albumGuid:guid}, function(response) {
		$(".all-albums-total").text(response.allAlbumTotal);
		$(".total-number-albums").text(response.totalNumberAlbums);

		var element = $(".album_" + guid);
		element.hide(50, function() {
			element.remove();
			ref.$container.trigger("deleteAlbum");
		});
		if($.isFunction(callback)) {
			callback.call(ref);
		}
	}, "json");
};

picturesTab.prototype.deleteImage = function($link, callback) {
	var ref = this;
	$.bbDialog.confirm("Are you sure you want to delete this image?", function() {
		var guid = bb.utils.returnHashParam($link);
		ref.deleteImageCall(guid, callback);
	});
};

picturesTab.prototype.deleteImageCall = function(guid, callback) {
	var ref = this;
	$.post(bb.urls.photos.deleteImage, {imageGuid:guid}, function(response) {

		$(".total-images-" + $("#currentAlbumGuid").val()).text(response.albumTotal);
		$(".all-albums-total").text(response.allAlbumTotal);
		var element = $(".image_" + guid);
		element.hide(50, function() {
			element.remove();
			ref.$container.trigger("deleteImage");
		});
		if($.isFunction(callback)) {
			callback.call(ref);
		}
	}, "json");
};

picturesTab.prototype.resetRows = function(album, selector, count) {
	$(selector, album).removeClass("row-start");
	$(selector + ":nth-child(" + count + "n+1)", album).addClass("row-start");
};

/**************************************************
VIEWS
**************************************************/

picturesTab.prototype.getAlbumsView = function() {
	var ref = this;
	this.$container.load(bb.urls.photos.viewAlbums, {identifier:currentPersonaUser.guid}, function() {
		$("#main-albums").photoAlbum({
			size:6,
			itemSelector:".photo-album"
		});
		ref.bindAlbumCovers($("#main-albums"));
		$("#view-create-album").click(function() {
			ref.getCreateAlbumView();
			return false;
		});
	});
};

picturesTab.prototype.getAlbumView = function(guid, pollImages) {
	var ref = this;
	this.$container.load(bb.urls.photos.viewAlbum, {albumGuid: guid, identifier:currentPersonaUser.guid}, function() {

		if(pollImages) {
			var image =
			$(".album-image .single-thumb").hide();
			$(".album-image .single-thumb").parent().append("<span class='load-message'>queued...</span>");

			var pollIndex = 0;
			var pollNextImage = function() {
				var image = $(".album-image .single-thumb:eq(" + pollIndex + ")");

				image.parent().addClass("queued-image").find(".load-message").text("loading...");
				if(image.length < 1) return;
				image.bind("loadComplete", function() {
					image.show();
					$(this).parent().removeClass("queued-image").find(".load-message").remove().find("img").show();
					pollNextImage();
					image.unbind();
				});
				Activity.Utils.imgLoadHander(image);
				pollIndex ++;
			};
			pollNextImage();
		}

		$("#album-sidebar").photoAlbum({
			size:6,
			itemSelector:".album-cover"
		});
		ref.$container.bind("deleteImage", function() {
			$("#photo-album").photoAlbum('updateCounts');
			ref.resetRows($("#photo-album"), ".album-image", 5);
		});
		$("#photo-album").photoAlbum({
			size:10,
			itemSelector:".album-image"
		});
		$(".add-more-link").click(function() {
			ref.getAddToAlbumView(bb.utils.returnHashParam($(this)));
			return false;
		});
		$(".view-albums-link", this.$container).click(function() {
			ref.getAlbumsView();
			return false;
		});
		ref.bindAlbumCovers($("#album-sidebar"));
		$(".album-image").hover(function() {
			$(this).find(".image-action-link, .delete-image-link").show();
		}, function() {
			$(this).find(".image-action-link, .delete-image-link").hide();
		});
		ref.bindImages($("#photo-album"));

		$(".delete-album-link", ref.$container).click(function() {
			ref.deleteAlbum($(this), function() {
				ref.getAlbumsView();
			});
			return false;
		});

		$("#show-permissions-link").toggle(
			function() {
				$(".image-action-menu").show();
				$(".image-action-menu").addClass("lockOpen");
				$(".show-label", $(this)).hide();
				$(".hide-label", $(this)).show();
				return false;
			},
			function() {
				$(".image-action-menu").hide();
				$(".image-action-menu").removeClass("lockOpen");
				$(".show-label", $(this)).show();
				$(".hide-label", $(this)).hide();
				return false;
			}
		);


	});
};

picturesTab.prototype.getAddToAlbumView = function(guid) {
	var ref = this;
	this.$container.load(bb.urls.photos.addToAlbum, {albumGuid:guid, identifier:currentPersonaUser.guid}, function() {
		$("#album-sidebar").photoAlbum({
			size:6,
			itemSelector:".album-cover"
		});
		ref.bindAlbumCovers($("#album-sidebar"));
		$("#cancel-create button").click(function() {
			ref.getAlbumsView();
			return false;
		});
	});
};

picturesTab.prototype.getCreateAlbumView = function() {
	var ref = this;
	this.$container.load(bb.urls.photos.createAlbum, {identifier:currentPersonaUser.guid}, function() {
		$("#album-sidebar").photoAlbum({
			size:6,
			itemSelector:".album-cover"
		});
		ref.bindAlbumCovers($("#album-sidebar"));
		$("#cancel-create button").click(function() {
			ref.getAlbumsView();
			return false;
		});
		$("#create-album button").click(function() {
			var form = $("#create-album-form");
			$.post(form.attr("action"), "startPicture=&" + form.serialize(), function() {
				$("#create-album-step").hide();
				$("#upload-images-step").show();
			});
			return false;
		});
	});

};




