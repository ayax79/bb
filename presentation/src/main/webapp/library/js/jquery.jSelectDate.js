/**
 * jQuery.jSelectDate Version 0.2
 * jQuery 
 *
 * Wathon Team
 * http://www.wathon.com
 * http://huacn.cnblogs.com
 * http://www.cnblogs.com/huacn/archive/2008/01/16/jquery_plugin_jSelectDate.html
 * 
 * created by:
 */
/*
 * *****************   Example   ***********************
 <script type="text/javascript">
 $("body").ready(function(){
 $("input.date").jSelectDate({
 css:"date",
 yearBeign: 1995,
 disabled : true
 });
 })
 </script>
 
 
 <body>
 <input type="text" id="txtName" class="date" value="2005-3-22" />
 <input type="text" id="txtDate2" class="date" value="1995-5-2" />
 </body>
 
 * ****************  End Example  **********************
 */
var jSelectDate = {

		
	/**
	 */
    settings : {
		css: "date",
		borderCss: "date",
        disabled: false,
        yearBegin: 1960,
        yearEnd: 2008,
		isShowLabel: true
	},
	
    
    
    /**
     */
    init: function(els){
    
        els.each(function(){
        
        
            var el = $(this);
            
            var elValue = el.val();
            elDate = new Date(elValue.split("-").join("/"));
            
            var nYear = elDate.getFullYear();
            var nMonth = jSelectDate.returnMonth(elDate.getMonth());
            var nDay = elDate.getDate();
            
            
            el.hide();
            
            var currentIdx = $("jSelectDateBorder").length + 1;
			
			var spanDate = document.createElement("span");
			spanDate.id = "spanDate" + currentIdx;
			spanDate.className = "jSelectDateBorder " + jSelectDate.settings.borderCss;
			spanDate.disabled = jSelectDate.settings.disabled;
			
            el.after(spanDate);
			
            var selYear = document.createElement("select");
            selYear.id = "selYear" + currentIdx
            selYear.className = jSelectDate.settings.css;
            selYear.disabled = jSelectDate.settings.disabled;
            
            for (var i = jSelectDate.settings.yearEnd; i >= jSelectDate.settings.yearBegin; i--) {
            
                var option = document.createElement("option");
                option.value = i;
                option.innerHTML = i;
                
                if (!isNaN(nYear)) {
                    if (i == nYear) {
                        option.selected = true;
                    }
                }
                
                selYear.appendChild(option);
                option = null;
                
            }            
           
			
            
            var selMonth = document.createElement("select");
            selMonth.id = "selMonth" + currentIdx
			selMonth.className = jSelectDate.settings.css;
            selMonth.disabled = jSelectDate.settings.disabled;
            for (var i = 1; i <= 12; i++) {
                var option = document.createElement("option");
                option.value = i;
                option.innerHTML = i;
                
                if (!isNaN(nMonth)) {
                    if (i == nMonth) {
                        option.selected = true;
                    }
                }
                
                selMonth.appendChild(option);
                option = null;
            }
            
            
            
            var selDay = document.createElement("select");
            selDay.id = "selDay" + currentIdx
			selDay.className = jSelectDate.settings.css;
            selDay.disabled = jSelectDate.settings.disabled;
            
			var maxDayNum = 30;
			if (nMonth == 2) {
				if (jSelectDate.isLeapYear(nYear)) {
					maxDayNum = 29;
				}
				else{
					maxDayNum = 28;
				}
			}
			else if (jSelectDate.isLargeMonth(nMonth)) {
					maxDayNum = 31;
			}
			
            for (var i = 1; i <= maxDayNum; i++) {
            
                var option = document.createElement("option");
                option.value = i;
                option.innerHTML = i;
                
                if (!isNaN(nDay)) {
                    if (i == nDay) {
                        option.selected = true;
                    }
                }
                
                selDay.appendChild(option);
                option = null;
            }
            $(spanDate).append(selMonth);
            $(selMonth).after(selDay);
            $(selDay).after(selYear);
			
			if (jSelectDate.settings.isShowLabel) {
				$(selMonth).before("   Month     ");
				$(selMonth).after("     Day     ");
				$(selDay).after("     Year    ");
			}else{
				$(selMonth).before(" ");
				$(selDay).before(" ");
			}
			
            var getDate = function(){
                var year = $(selYear).val();
                var month = $(selMonth).val();
                var day = $(selDay).val();
                el.val(year + "-" + month + "-" + day);
            }
			
			
            /**
             */
            $(selDay).change(function(){
                return getDate();
            });
            $(selMonth).change(function(){
				
				jSelectDate.progressDaySize(this,true);
				
                return getDate();
            });
            $(selYear).change(function(){
				jSelectDate.progressDaySize(this,false);
                return getDate();
            });
            
        })
        
        
    },
	

	isLeapYear : function(year){return (0==year%4&&((year%100!=0)||(year%400==0)));},

	/**
	 * @param {Object} monthNum
	 */
	isLargeMonth : function(monthNum){
		var largeArray = [true,false,true,false,true,false,true,true,false,true,false,true];
		return largeArray[monthNum - 1];
	},
    
    returnMonth: function(num){
        var arr = new Array("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        return arr[num];
    },
	
	/**
	 */
	createOption : function(value,text){
		var option = document.createElement("option");
        option.value = value;
        option.innerHTML = text;
		return option;			
	},
	
	/**
	 */
	progressDaySize: function(el,isMonth){
		if (isMonth == true) {
			var month = $(el).val();
			var year = $($("select", $(el).parent())[0]).val()
			var selDay = $($("select", $(el).parent())[2]);
			if (month == 2) {
			
				$("option:contains('31')", selDay).remove();
				$("option:contains('30')", selDay).remove();
				
				if (!jSelectDate.isLeapYear(year)) {
					$("option:contains('29')", selDay).remove();
				}
				else {
				
					if ($("option:contains('29')", selDay).length == 0) {
						selDay.append(jSelectDate.createOption(29, 29));
					}
				}
			}
			else 
				if (!jSelectDate.isLargeMonth(month)) {
				
					if ($("option:contains('30')", selDay).length == 0) {
						selDay.append(jSelectDate.createOption(30, 30));
					}
					
					$("option:contains('31')", selDay).remove();
				}
				else {
				
					if ($("option:contains('30')", selDay).length == 0) {
						selDay.append(jSelectDate.createOption(30, 30));
					}
					
					if ($("option:contains('31')", selDay).length == 0) {
						selDay.append(jSelectDate.createOption(31, 31));
					}
				}
		}
		else{
			var panelDate = $(el).parent();
			var year = $(el).val();
			var month = $($("select",panelDate)[1]).val()
			var selDay = $($("select",panelDate)[2]);
			if(month == 2){
				$("option:contains('31')",selDay).remove();
				$("option:contains('30')",selDay).remove();
				if(! jSelectDate.isLeapYear(year)){				
					$("option:contains('29')",selDay).remove();
				}
				else{
					
					if($("option:contains('29')",selDay).length == 0){
						selDay.append(jSelectDate.createOption(29, 29));
					}
				}
			}
			
		}
	}
    
}

jQuery.fn.jSelectDate = function(settings){    

	var getNowYear = function(){
        var date = new Date();
        return date.getFullYear();
    }
	
	jSelectDate.settings.yearEnd = getNowYear();
	
    $.extend(jSelectDate.settings, settings);


    jSelectDate.init($(this));
    
    return $(this);
    
}
