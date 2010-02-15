var Mailbox = function(type, container) {

	this.folder = Mailbox.folders[type.toLowerCase().replace("tab", "")];
	this.$container = container;
	this.viewParams = {
		pageNum:0,
		pageSize:10,
		pageCount:1,
		offset:null,
		networkFilter:"ALL",
		statusFilter:"EITHER",
		sliderParams:{}
	};
	this.$btnPrevPage = null;
	this.$btnNextPage = null;
	this.init();
};

Mailbox.folders = {
	"inbox": "INBOX_FOLDER",
	"sent": "SENT_FOLDER",
	"archive": "ARCHIVED_FOLDER",
	"drafts": "DRAFTS_FOLDER"
};

Mailbox.filters = {
	"ALL": "ALL",
	"FRIENDS": "FRIENDS",
	"FOLLWERS": "FOLLWERS",
	"FOLLOWING": "FOLLOWING",
	"WISHED": "WISHED"
};

Mailbox.prototype.init = function() {
	
	this.bindActionsButtons();
	this.bindFilterButtons();
	this.bindMessageActions();
	bb.notifications.bindEvents($("#notifications-col"));
	this.bindPagingButtons();
	this.bindPagingTextbox();
	this.setPagingParams();
	this.updatePagingView();
	//this.saveState();
};

Mailbox.prototype.composeMessage = function() {
	var ref = this;
	var composer = bb.messaging.compose();
	composer.bind("messageSent", function() {
		ref.refresh();
	});
};

Mailbox.prototype.updateMessages = function(url, extraParams) {
	if(extraParams.messageGuid == "") {
		$.bbDialog.alert(bb.resources.messaging.requiredError);
		return false;
	}

	var ref = this;
	var params = this.getParams();
	if(extraParams !== undefined) {
		$.extend(params, extraParams);
	}
	this.$container.load(url, params, function() {
		ref.init();
	});
};

Mailbox.prototype.saveState = function() {
	//var hash = "#" + this.getStateHash();
	//var url = window.location.href.split("#")[0];
	//window.location.href = url + hash;
};

Mailbox.prototype.refresh = function(extraParams) {
	//this.saveState();
	var ref = this;
	var params = this.getParams();
	if(extraParams !== undefined) {
		$.extend(params, extraParams);
	}
	this.$container.load(bb.urls.mailbox.load, params, function() {
		ref.init();
	});
};

Mailbox.prototype.markUnread = function() {
	this.updateMessages(bb.urls.mailbox.markUnread, {messageGuid:this.getSelectedMessages().join()});
};

Mailbox.prototype.archiveMessages = function() {
	this.updateMessages(bb.urls.mailbox.archive, {messageGuid:this.getSelectedMessages().join()});
};

Mailbox.prototype.deleteMessage = function(guid) {
	this.updateMessages(bb.urls.mailbox.delet3, {messageGuid:guid});
};

Mailbox.prototype.deleteMessages = function() {
	this.updateMessages(bb.urls.mailbox.delet3, {messageGuid:this.getSelectedMessages().join()});
};

Mailbox.prototype.bindFilterButtons = function() {
	var ref = this;
	this.$container.find(".filterButton").bind("click", function() {
		ref.viewParams.networkFilter = bb.utils.returnHashParam($(this));
		ref.refresh();
		return false;
	});
	this.$container.find(".btnMessageStatus").bind("click", function() {
		ref.viewParams.statusFilter = bb.utils.returnHashParam($(this));
		ref.refresh();
		return false;
	});
};

Mailbox.prototype.getPreviousPage = function() {
	if(this.viewParams.pageNum > 0) this.viewParams.pageNum --;
	this.viewParams.offset = (this.viewParams.pageNum - 1) *  this.viewParams.pageSize;
	this.refresh();
};

Mailbox.prototype.getNextPage = function() {
	if(this.viewParams.pageNum < this.viewParams.pageCount) this.viewParams.pageNum++;
	this.viewParams.offset = (this.viewParams.pageNum - 1) * this.viewParams.pageSize;
	this.refresh();
};

Mailbox.prototype.bindPagingTextbox = function() {
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

Mailbox.prototype.bindPagingButtons = function() {
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

Mailbox.prototype.setPagingParams = function() {
	var pageSize = parseInt(this.$container.find("#numResults").val());
	var offset = parseInt(this.$container.find("#startIndex").val());
	var total = parseInt(this.$container.find("#totalResults").val());

	this.viewParams.offset = offset;
	this.viewParams.pageSize = pageSize;
	this.viewParams.pageNum = Math.floor((offset) / pageSize) + 1;
	this.viewParams.pageCount = Math.max(Math.ceil(total / pageSize), 1);

};

Mailbox.prototype.updatePagingView = function() {
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

Mailbox.prototype.bindActionsButtons = function() {
	var ref = this;
	this.$container.find(".btnMessageSelect").click(function() {
		if($(this).is(':checked')){
		ref.$container.find(".msg-select :checkbox").attr("checked", "checked");
		$('.btnMessageSelect').attr("title", "Deselect all");
		}
		else {
		ref.$container.find(".msg-select :checkbox").removeAttr("checked", "checked");
		$('.btnMessageSelect').attr("title", "Select all");
		}
	});
	
	this.$container.find(".btnMessageSelect").click(function() {
		if($(this).is(':checked')){
		ref.$container.find(".msg-select :checkbox").attr("checked", "checked");
		}
		else {
		ref.$container.find(".msg-select :checkbox").removeAttr("checked", "checked");
		}
	});

	this.$container.find(".btnCompose").click(function() {
		ref.composeMessage();
		return false;
	});

	this.$container.find(".btnMarkUnread").click(function() {
		if($(this).is(".disabled")) return false;
		ref.markUnread();
		return false;
	});

	this.$container.find(".btnReportAbuse").click(function() {
		//ref.reportAbuse();
		return false;
	});

	this.$container.find(".btnArchive").click(function() {
		if($(this).is(".disabled")) return false;
		ref.archiveMessages();
		return false;
	});

	this.$container.find(".btnDelete").click(function() {
		if($(this).is(".disabled")) return false;
		$.bbDialog.confirm(bb.resources.messaging.confirmDelete, function() {
			ref.deleteMessages();
		});
		return false;
	});
};

Mailbox.prototype.readMessage = function(guid) {
	ref = this;
	this.$container.load(bb.urls.mailbox.read, {parentGuid:guid}, function() {
		ref.bindMessageReadActions();
		ref.bindActionsButtons();
	});
};

Mailbox.prototype.bindMessageReadActions = function() {
	ref = this;
	this.$container.find(".btnSendMsg").click(function() {
		ref.postMessageReply();
		return false;
	});

	this.$container.find(".return-inbox").click(function() {
		ref.refresh();
		return false;
	});

	this.$container.find(".message-delete").click(function() {
		var $link = $(this);
		$.bbDialog.confirm(bb.resources.messaging.confirmDelete, function() {
			ref.deleteMessage(bb.utils.returnHashParam($link));
		});
		return false;
	});
};

Mailbox.prototype.postMessageReply = function() {
	ref = this;
	this.$container.load(bb.urls.mailbox.reply, $("#replyForm").serialize(), function(response) {
		ref.bindMessageReadActions();
		$("#replyForm textarea[name=body]").val("");
	});
};

Mailbox.prototype.bindMessageActions = function() {
	var ref = this;

	bb.pageInit.bindMiniProfiles(this.$container.find(".message-list"));
	this.$container.find(".read-message-link-div").click(function() {
		var link = $(this).find(".read-message-link");
		ref.readMessage(bb.utils.returnHashParam(link));
		return false;
	});
};

Mailbox.prototype.getParams = function() {
	return {
		"mailboxRequest.folder": this.folder,
		"mailboxRequest.filter": this.viewParams.networkFilter,
		"mailboxRequest.startIndex": this.viewParams.offset,
		"mailboxRequest.numResults": this.viewParams.pageSize,
		"mailboxRequest.readState": this.viewParams.statusFilter
	};
};

Mailbox.prototype.getStateHash = function() {
	var params =
	[
		"folder=" + this.folder,
		"filter=" + this.viewParams.networkFilter,
		"offset=" + this.viewParams.offset,
		"numResults=" + this.viewParams.pageSize,
		"status=" + this.viewParams.statusFilter
	];
	return params.join("&");
};

Mailbox.prototype.getSelectedMessages = function() {
	return $.map(this.$container.find(".message-list :checked"),function(el) {
		return $(el).val();
	});
};