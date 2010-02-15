;(function($) {
	var EventCalendar = {
		_init: function() {
			this._generateHTML();
			this._placeEvents();
		},
		_setDate: function(date) {
			this._setData("drawDate", date);
			this.element.empty();
			this._generateHTML();
			this._placeEvents();
		},
		_placeEvents: function() {
			var myEvents = this._getData("occasions");
			if(!myEvents) return;
			for(var i = 0; i < myEvents.length; i++) {
				var myEvent = myEvents[i];
				var eventDate = this._parseIso8601(myEvent.eventDate);
				var eventCompareDate = this._getCleanDate(eventDate);
				var daySelector = ".M" + eventDate.getMonth() + "D" + eventDate.getDate();
				var dayElement = this.element.find(daySelector);
				if(dayElement.length) {
					dayElement.addClass("eventDay");
					if(!dayElement.data("tips")) {
						dayElement.data("tips", new Array());
					}
					dayElement.data("tips").push(myEvent.description);
				}
			}
			$.each($(".eventDay", this.element), function() {
				var shit = $(this).data("tips").join("<br/>");
				$(this).qtip({
				   content: "<strong>Events on this day:</strong><br/>" + shit,
				   show: 'mouseover',
				   hide: 'mouseout',
					position: {
						corner: {
							target: 'topMiddle',
							tooltip: 'bottomMiddle'
						}
					},
					style: {
						padding: 8,
						background:'#d1feab',
						border: {
							width: 1,
							radius: 6,
							color: '#d1feab'
						},
						tip: 'bottomMiddle'
					}
				});
			});
		},
		_getCleanDate: function(date) {
			return new Date(date.getFullYear(), date.getMonth(), date.getDate());
		},
		_generateHTML: function() {
			var ref = this;
			var today = new Date();
			today = this._getCleanDate(today);
			var drawDate = this._getData("drawDate") || new Date(today);
			drawDate = this._getCleanDate(drawDate);
			this._setData("drawDate", drawDate);
			var drawYear = drawDate.getFullYear();
			var drawMonth = drawDate.getMonth();
			var showOtherMonths = false;
			var calendarTable = $("<table class='bb-event-calendar'/>");
			var calendarHeader = $("<thead />");
			var headerRow = $("<tr/>");
			var prevMonth = $("<a href='#' class='prev-month'>&#8592;" + this._getData("monthNames")._short[this._getPrevMonth(drawDate).getMonth()] + "</a>");
			var nextMonth = $("<a href='#' class='next-month'>" + this._getData("monthNames")._short[this._getNextMonth(drawDate).getMonth()] + "&#8594;</a>");
			prevMonth.click(function() {
				ref._setDate(ref._getPrevMonth(drawDate));
			});
			nextMonth.click(function() {
				ref._setDate(ref._getNextMonth(drawDate));
			});
			var calendarTitle = $("<div class='calendar-title'/>");
			calendarTitle.text(this._getData("monthNames")._long[drawMonth] + " " + drawYear);
			var headerCell = $("<th colspan='7'/>");
			var headerDiv = $("<div class='calendar-title-container'/>");
			headerDiv.append(prevMonth).append(nextMonth).append(calendarTitle);
			headerCell.append(headerDiv);
			headerRow.append(headerCell);
			calendarHeader.append(headerRow);
			var calendarBody = $("<tbody/>");
			var dayHeaderRow = $("<tr>");
			for(var dow = 0; dow < 7; dow++) {
				dayHeaderRow.append($("<td class='dayHeader'/>").text(this._getData("dayNames").initials[dow]));
			};
			calendarBody.append(dayHeaderRow);
			var daysInMonth = this._getDaysInMonth(drawYear, drawMonth);
			var leadDays = (this._getFirstDayOfMonth(drawYear, drawMonth) + 7) % 7;
			var numRows = Math.ceil((leadDays + daysInMonth) / 7); // calculate the number of rows to generate
			var printDate = new Date(drawYear, drawMonth, 1 - leadDays);
			for (var dRow = 0; dRow < numRows; dRow++) {
				var  weekRow =  $("<tr/>");
				for (var dow = 0; dow < 7; dow++) {
					var otherMonth = (printDate.getMonth() != drawMonth);
					var dayCell = $("<td />");
					dayCell.addClass((otherMonth)?"otherMonth":"currentMonth")
							.addClass("day")
							.addClass("ui-state-default")
							.addClass("M" + printDate.getMonth() + "D" + printDate.getDate())
							.addClass((printDate.getTime() == today.getTime())?"today":"");
					var contents = (otherMonth ? (showOtherMonths ? printDate.getDate() : '&#xa0;') : printDate.getDate());
					if(!otherMonth) {
						dayCell.bind("mouseover", function() {
							$(this).addClass('ui-state-highlight');
						});
						dayCell.bind("mouseout", function() {
							$(this).removeClass('ui-state-highlight');
						});
					}
					dayCell.append(contents);
					weekRow.append(dayCell);
					printDate.setDate(printDate.getDate() + 1);
				}
				calendarBody.append(weekRow);
			}
			calendarTable.append(calendarHeader);
			calendarTable.append(calendarBody);
			this.element.append(calendarTable);
		},
		_getDaysInMonth: function(year, month) {
			return 32 - new Date(year, month, 32).getDate();
		},
		_getFirstDayOfMonth: function(year, month) {
			return new Date(year, month, 1).getDay();
		},
		_getPrevMonth: function(indate) {
			var date = new Date(indate);
			var thisMonth = date.getMonth();
			date.setMonth(thisMonth-1);
			if(date.getMonth() != thisMonth-1 && (date.getMonth() != 11 || (thisMonth == 11 && date.getDate() == 1)))
			date.setDate(0);
			return date;
		},
		_getNextMonth: function(indate) {
			var date = new Date(indate);
			var thisMonth = date.getMonth();
			date.setMonth(thisMonth+1);
			if(date.getMonth() != thisMonth+1 && date.getMonth() != 0)
			date.setDate(0);
			return date;
		},
		_parseIso8601:function(iso8601) {
			var s = $.trim(iso8601);
			s = s.replace(/-/,"/").replace(/-/,"/");
			s = s.replace(/T/," ").replace(/Z/," UTC");
			s = s.replace(/([\+-]\d\d)\:?(\d\d)/," $1$2"); // -04:00 -> -0400
			return new Date(s);
		}
	};

	$.widget("ui.eventCalendar", EventCalendar);

	$.ui.eventCalendar.getter = "";

	$.ui.eventCalendar.defaults = {
		occasions:null,
		drawDate:null,
		today:new Date(),
		selected:0,
		spinner:null,
		dayNames: {
			initials: ['S', 'M', 'T', 'W', 'T', 'F', 'S']
		},
		monthNames: {
			_long: ['January','February','March','April','May','June','July','August','September','October','November','December'],
			_short: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
		}
	};
})(jQuery);