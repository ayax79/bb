var RecaptchaOptions = {'theme' : 'clean' };

var SliderWizard = function(form) {
	this.setStep(0);
	this.totalSteps = 2;
	this.scrollTopOffset = 100;
	this.tweenLength = 300;
	this.form = form;
};

SliderWizard.prototype.setStep = function (step) {
	this.step = step;
	this.section = $(".reg-step-section:eq(" + this.step + ")");
};

SliderWizard.prototype.updateButtons = function () {
	if (this.step == 0) {
		$(".previous").hide();
		$(".next").show();
	} else if (this.step == this.totalSteps) {
		$(".next").hide();
		$(".previous").show();
	} else {
		$(".next").show();
		$(".previous").show();
	}
	if (this.step != this.totalSteps) {
		$(".step").hide();
	} else {
		$(".step").show();
	}
};

SliderWizard.prototype.updateNoAnimateMasHeight = function() {
	var mask = $(".reg-steps-mask");
	var ref = this;
	mask.css({"height":ref.section.height()});
};

SliderWizard.prototype.updateMaskHeight = function(doScroll) {
	var mask = $(".reg-steps-mask");
	var ref = this;
	mask.animate({
		height:ref.section.height(),
		duration: this.tweenLength,
		easing: 'easeOutQuad'
	}, function() {
		if (doScroll) {
			$('html, body').animate({
				scrollTop: $("#form-top").offset().top - ref.scrollTopOffset
			}, ref.tweenLength);
		}
	});
};

SliderWizard.prototype.updateStep = function (doScroll) {
	this.updateMaskHeight(doScroll);
	var ref = this;
	$(".reg-steps-container").animate({
		left:ref.step * -ref.section.outerWidth(true),
		duration: ref.tweenLength,
		easing: 'easeOutQuad'
	});
	this.updateButtons();
};

SliderWizard.prototype.init = function() {
	var ref = this;
	$(".next, .previous").click(function() {
		var $this = $(this);
		var newStep = ref.step;
        
		if ($this.is(".next")) {
            if (newStep == "1") {
				var validator = ref.form.validate();
				if(validator.element(".lookingForCheck") === false || validator.element("#explain") === false) {
					return false;
				}
            }
			newStep++;
			if (newStep > ref.totalSteps) newStep = this.totalSteps;
		} else if ($this.is(".previous")) {
			newStep--;
			if (newStep < 0) newStep = 0;
		}
		ref.setStep(newStep);
		ref.updateStep(true);
		return false;
	});
	this.updateStep();
};