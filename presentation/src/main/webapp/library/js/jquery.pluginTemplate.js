/**
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 * Author:  Matthew Sweet
 * Last Modified Date: $DateTime: $
 *
 * This is a basic template for creating jQuery widget plug-ins based on a combination of various
 * design patterns I've discovered (and dictated by the jQuery widget construct itself).
 *
 * $.ui.PLUGIN_NAME.getter = ""; sets a method to act as a getter, so the method doesn't return the jQuery object itself
 *
 * $.ui.PLUGIN_NAME.defaults = {}; like the typical jQuery plugin options and are available in the class scope through this._getData("key")
 *                                 and this._setData("key", "value").
 *
 * Inside of the CLASSNAME definition, this.element references the original jQuery element(s).
 *
 *
 */

;(function($) {

	var CLASSNAME = {

		_init: function() {},

		//PRIVATE METHODS
		_privateMethod: function() {},

		//PUBLIC METHODS
		publicMethod: function() {}

	};

	$.widget("ui.PLUGIN_NAME", CLASSNAME);
	$.ui.PLUGIN_NAME.getter = "";
	$.ui.PLUGIN_NAME.defaults = {};

})(jQuery);		