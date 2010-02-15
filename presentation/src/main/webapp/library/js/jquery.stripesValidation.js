/**
 *
 * Jquery addapter class so we can use built in Jquery validator.
 * NOTE: this is only skeleton, implementing only small part of possible validation,
 * Usage:
 * <s:field-metadata var="metaData">
 *   $(function(){$.fn.stripesValidation('${metaData.formId}', ${metaData});});
 * </s:field-metadata>
 * dependancies:
 * jquery.1.3.1
 * jquery.form
 * jquery.validate
 * Licence:
 * Same as jquery, thus:
 * Dual licensed under the MIT and GPL licenses.
 * http://docs.jquery.com/License
 */
(function($){

    /**
     * Processess Stripes meta data and converting it so jquery
     * validation plugin is used for client side validation
     * @param formId id of a form to be processed
     * @param metaData metadata that is produced by stripes tag
     */
    $.fn.stripesValidation = function(formId, metaData){
        var form = $('#' + formId);
        var idCount = 0;
        for(var fieldName in metaData){
            var field = form.find('[name=' + fieldName + ']');
            addValidation(field, metaData[fieldName]);
            form.validate({
				wrapper: "p class='error-wrap'",
				errorPlacement: function(error, element) {
					var pe;
					if(element.parent().is(".textinput37")) {
						error.insertBefore(element.parent());
					} else if(element.parent().is(".rb-content-r")) {
						error.insertAfter(element.parents("li").find("label:first"));
					} else {
						error.insertBefore(element);
					}
				},
				highlight: function( element, errorClass, validClass ) {
					$(element).addClass(errorClass).removeClass(validClass);
					form.trigger("highlight.validate");
				},
				unhighlight: function( element, errorClass, validClass ) {
					$(element).removeClass(errorClass).addClass(validClass);
					form.trigger("unhighlight.validate");
				}
			});
        }
        function addValidation(field, obj){
            for(var prop in obj){
                var val = obj[prop];
                switch(prop){
                    case 'label':
                        break;
                    case 'required':
                        if(val){ // add only if true
                            field.addClass(prop);
                        }
                        break;
                    case 'minlength': // should  already be there
                    case 'maxlength': // should already be there
                        field.attr(prop, val);
                        break;
                    case 'minvalue':
                        field.attr('min', val);
                        break;
                    case 'maxvalue':
                        field.attr('max', val);
                        break;
                    case 'mask':
                        setMask(field, val);
                        break;
                    case 'typeConverter':
                        setConverter(field, val);
                        break;
                    default:
                        debug('missing this:' + prop + ':' + val);
                }
            }
        }

        /**
         * Adds regular expression validation
         * @param field field reference
         * @param mask regular expression mask
         */
        function setMask(field, mask){
            idCount++;
            var methodRef = 'maskMethod' + idCount;
            field.addClass(methodRef);
            $.validator.addClassRules({
                methodRef: {
                    methodRef: true
                }
            });
            $.validator.addMethod(methodRef, function (value){
                return mask.test(value);
            }, 'Value is invalid');
        }

        /**
         * Add converter mappings
         * @param field  field reference
         * @param converter converter used by stripes for given field
         */
        function setConverter(field, converter){
            if(converter == 'EmailTypeConverter'){
                field.addClass('email');
            }
            else if(converter =='DateTypeConverter'){
                field.addClass('date');
                if($.fn.datepicker){field.datepicker();}
            }
            else{
                debug('missing converter mapping:' + converter);
            }
        }

        function debug(msg){
            if(window.console && window.console.log){
                window.console.log(msg);
            }
        }
    };
})(jQuery);
