var MeBox = function(container, url) {

	this.$container = container;
	this.viewParams = {
		pageNum:0,
		pageSize:10,
		pageCount:1,
		offset:null
	};

	this.url = url;

	this.$btnPrevPage = null;
	this.$btnNextPage = null;
	this.init();
};

MeBox.prototype.init = function() {
	this.bindSocialActions();
	this.bindPagingButtons();
	this.bindPagingTextbox();
	this.setPagingParams();
	this.updatePagingView();
	bb.pageInit.bindMiniProfiles(this.$container);
};

MeBox.prototype.refresh = function(extraParams) {
	var ref = this;
	var params = this.getParams();
	if(extraParams !== undefined) {
		$.extend(params, extraParams);
	}
	this.$container.load(this.url, params, function() {
		ref.init();
	});
};

MeBox.prototype.getParams = function() {
	return {
		"startIndex": this.viewParams.offset,
		"maxResults": this.viewParams.pageSize
	};
};

MeBox.prototype.getPreviousPage = function() {
	if(this.viewParams.pageNum > 0) this.viewParams.pageNum --;
	this.viewParams.offset = (this.viewParams.pageNum - 1) *  this.viewParams.pageSize;
	this.refresh();
};

MeBox.prototype.getNextPage = function() {
	if(this.viewParams.pageNum < this.viewParams.pageCount) this.viewParams.pageNum++;
	this.viewParams.offset = (this.viewParams.pageNum - 1) * this.viewParams.pageSize;
	this.refresh();
};

MeBox.prototype.bindPagingTextbox = function() {
	var ref = this;

	this.$container.find(".paging-form").submit(function() {
		var input = $(this).find("*[name=pageNum]");

		var reqPage = input.val();

		if(isNaN(reqPage)) {
			$.bbDialog.alert("Please enter a number");
			input.val("").focus();
			return false;
		}

		if(reqPage > ref.viewParams.pageCount) {
			reqPage = ref.viewParams.pageCount;
		}

		if(reqPage < 1) {
			reqPage = 1;
		}
		input.val(reqPage);

		ref.viewParams.offset = (reqPage - 1) * ref.viewParams.pageSize;
		ref.refresh();

	    return false;
	});

};

MeBox.prototype.bindPagingButtons = function() {
	var ref = this;

	this.$btnPrevPage = this.$container.find(".paging-button.previous");
	this.$btnNextPage = this.$container.find(".paging-button.next");

	this.$btnPrevPage.click(function() {
		if(!$(this).is(".disabled")) ref.getPreviousPage();
		return false;
	});

	this.$btnNextPage.click(function() {
		if(!$(this).is(".disabled")) ref.getNextPage();
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

};

MeBox.prototype.setPagingParams = function() {
	var pageSize = parseInt(this.$container.find("#numResults").val());
	var offset = parseInt(this.$container.find("#startIndex").val());
	var total = parseInt(this.$container.find("#totalResults").val());
	this.viewParams.offset = offset;
	this.viewParams.pageSize = pageSize;
	this.viewParams.pageNum = Math.floor((offset) / pageSize) + 1;
	this.viewParams.pageCount = Math.max(Math.ceil(total / pageSize), 1);
};

MeBox.prototype.updatePagingView = function() {
	this.$container.find(".numResults").text(this.viewParams.pageSize);
	this.$container.find("input[name=pageNum]").val(this.viewParams.pageNum);
	this.$container.find(".total-pages").text(this.viewParams.pageCount);
	if(this.viewParams.pageNum == 1) {
		this.$btnPrevPage .addClass("disabled");
	} else {
		this.$btnPrevPage .removeClass("disabled");
	}
	if(this.viewParams.pageNum == this.viewParams.pageCount) {
		this.$btnNextPage .addClass("disabled");
	} else {
		this.$btnNextPage .removeClass("disabled");
	}
};


MeBox.prototype.bindSocialActions = function() {
	var self = this;

	// UN-WISH
	this.$container.find(".unwish-link").click(function() {
		var ref = $(this);
		var url = ref.attr("href");
		var username = self.getTargetUser(url);
		$.bbDialog.confirm(self.doUserNameSubstitution(bb.resources.wish.unsetMessage, username), function() {
			$.post(bb.urls.social.relationship.unwish, {identifier:username}, function(response) {
				//
			});
		});
		return false;
	});

	// UN-FRIEND
	this.$container.find(".unfriend-link").click(function() {
		var ref = $(this);
		var url = ref.attr("href");
		var username = self.getTargetUser(url);
		$.bbDialog.confirm(self.doUserNameSubstitution(bb.resources.friend.unsetMessage, username), function() {
			$.post(bb.urls.social.relationship.unsetFriend, {identifier:username}, function(response) {
				//
			});
		});
		return false;
	});

	// UN-FOLLOW
	this.$container.find(".unfollow-link").click(function() {
		var ref = $(this);
		var url = ref.attr("href");
		var username = self.getTargetUser(url);
		$.bbDialog.confirm(self.doUserNameSubstitution(bb.resources.follow.unsetMessage, username), function() {
			$.post(bb.urls.social.relationship.unsetFollow, {identifier:username}, function(response) {
				//
			});
		});
		return false;
	});

	// UN-VOUCH
	this.$container.find(".unvouch-link").click(function() {
		var ref = $(this);
		var url = ref.attr("href");
		var username = self.getTargetUser(url);
		$.bbDialog.confirm(self.doUserNameSubstitution(bb.resources.vouch.unsetMessage, username), function() {
			$.post(bb.urls.social.vouch.remove, {identifier:username}, function(response) {
				//
			});
		});
		return false;
	});
	                                                              
	// UN-VOUCH
	this.$container.find(".deletevouch-link").click(function() {
		var ref = $(this);
		var url = ref.attr("href");
		var username = self.getTargetUser(url);
		$.bbDialog.confirm(self.doUserNameSubstitution(bb.resources.vouch.deleteMessage, username), function() {
			$.post(bb.urls.social.vouch.delet3, {identifier:username}, function(response) {
				//
			});
		});
		return false;
	});

	// BLOCK
	this.$container.find(".block-link").click(function() {
		var ref = $(this);
		var url = ref.attr("href");
		var username = self.getTargetUser(url);
		$.bbDialog.confirm(self.doUserNameSubstitution(bb.resources.donkey.setMessage, username), function() {
			$.post(bb.urls.social.relationship.setBlock, {identifier:username}, function(response) {
				//
			});
		});
		return false;
	});

};

MeBox.prototype.getTargetUser = function(url) {
	return url.split("#").pop();
};

MeBox.prototype.doUserNameSubstitution = function(str, name) {
	return str.replace("{0}", name);
};
