function applyStripesValidation(formId, fieldMetadata)
{
	// get the form
	var form = $('#'+formId);
	
	// add validation info to the form
	form.validation({invalidClass:'invalid'});
   	
   	var property;
   	
   	// add validation for each field after translating field properties
	for (property in fieldMetadata)
	{
		// translate the field properties we got from Stripes
		// to corresponding jquery.validation.js properties
		var validationProperties = translate(fieldMetadata[property]);
		
		// apply the validation properties to the field
		var field = form.find('[name='+property+']').validation(validationProperties);

		if (fieldMetadata[property].type == 'Date')
			field.addClass('datefield');
	}

	// This function translates the field properties from stripes into the input for jquery.validation
	function translate(properties)
	{
		var translated = {};
		var property;
		
		for (property in properties)
		{
			switch (property)
			{
				case 'label':
				case 'required':
				case 'minlength':
				case 'maxlength':
					// these properties are the same so just copy
					translated[property]=properties[property];
					break;
				case 'minvalue': translated['min']=properties[property];break;
				case 'maxvalue': translated['max']=properties[property];break;
				case 'mask': translated['regex']=properties[property];break;
				case 'type':
					switch (properties[property].toLowerCase())
					{
						case 'short':
						case 'int':
						case 'integer':
						case 'long':
							translated['int']=true;
							break;
						case 'float':
						case 'double':
						case 'bigdecimal':
							translated['float']=true;
							break;
						case 'date':
							translated['date']='m/d/yy';
							break;
					}
					break;
				case 'typeConverter':
					switch (properties[property])
					{
						case 'CreditCardTypeConverter':translated['creditCard']=true;break;
						case 'EmailTypeConverter':translated['email']=true;break;
					}
					break;
			}
		}
		
		return translated;
	}
}

// add the date picker control to fields with a class of datepicker
/*
$(function(){
	if (!$.datepicker)
		return;
	$.datepicker.setDefaults({showOn: 'both',
								dateFormat:'m/d/yy',
								buttonImageOnly: true,
								buttonImage: contextPath+'/images/jquery/datepicker.calendar.gif',
								buttonText: 'Calendar'});

	$('.datefield').datepicker();
});
*/