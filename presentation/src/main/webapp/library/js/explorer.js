var Explorer = function(container, url) {
    this.$container = container;
    this.url = url;
    this.viewHash = {};
    this.viewParams = {
		pageNum:0,
		pageSize:10,
		pageCount:1,
		offset:null
	};
	this.init();


};


Explorer.prototype.init = function() {
	//Bind events here
    this.bindPagingButtons();
	this.bindPagingTextbox();
	this.setPagingParams();
	this.updatePagingView();
    bb.pageInit.bindMiniProfiles($("#exp_results"));    
};


Explorer.prototype.getParams = function() {
	return {
		"explore.startIndex": this.viewParams.offset,
		"explore.maxResults": this.viewParams.pageSize
	};
};

Explorer.prototype.getPreviousPage = function() {
	if(this.viewParams.pageNum > 0) this.viewParams.pageNum --;
	this.viewParams.offset = (this.viewParams.pageNum - 1) *  this.viewParams.pageSize;
	this.refresh();
};

Explorer.prototype.getNextPage = function() {
	if(this.viewParams.pageNum < this.viewParams.pageCount) this.viewParams.pageNum++;
	this.viewParams.offset = (this.viewParams.pageNum - 1) * this.viewParams.pageSize;
	this.refresh();
};

Explorer.prototype.bindPagingTextbox = function() {
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

Explorer.prototype.bindPagingButtons = function() {
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

Explorer.prototype.setPagingParams = function() {
	var pageSize = parseInt(this.$container.find("#numResults").val());
    var offset = parseInt(this.$container.find("#startIndex").val());
	var total = parseInt(this.$container.find("#totalResults").val());

    this.viewParams.offset = offset;
    this.viewParams.pageSize = pageSize;
	this.viewParams.pageNum = Math.floor((offset) / pageSize) + 1;
	this.viewParams.pageCount = Math.max(Math.ceil(total / pageSize), 1);

};

Explorer.prototype.updatePagingView = function() {
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



Explorer.prototype.updateAddressHash = function(url) {
	var params = this.getViewState();
	var formAction = this.$searchForm.attr("action");
	$.address.value(formAction + "?" + params);
};

Explorer.prototype.updateFormView = function() {
   	var ref = this;
	this.$searchForm[0].reset();
	var params = bb.utils.getQueryParamArray($.address.queryString());
	$.each(params, function() {
		var field = $("*[name=" + this.name + "]", ref.$searchForm);
		if(field.is(":checkbox")) {
			$(":checkbox[value=" + (this.value) + "]", ref.$searchForm).attr("checked", "checked");
		} else if(field.is(":radio")) {
			$(":radio[value=" + (this.value) + "]", ref.$searchForm).attr("checked", "checked");
		} else {
			field.val(this.value);
		}
	});
};

Explorer.prototype.refresh = function() {
    var ref = this;
	var params = this.getParams();
    this.$container.load(this.url, params, function(arg) {
        ref.init();
	});
};

Explorer.prototype.getViewState = function() {
	return this.$container.find(".explorerForm").serialize();
};