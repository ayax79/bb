package com.blackbox.presentation.extension;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

import java.util.Locale;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author A.J. Wright
 */
public class JSONArrayTypeConverter implements TypeConverter<JSONArray> {

    private static final Logger log = LoggerFactory.getLogger(JSONArrayTypeConverter.class);

    @Override
    public void setLocale(Locale locale) {
    }

    @Override
    public JSONArray convert(String input, Class<? extends JSONArray> targetType, Collection<ValidationError> errors) {
        try {
            return new JSONArray(input);
        }
        catch (JSONException e) {
            log.error(e.getMessage(), e);
            errors.add(new LocalizableError("validation.json.invalid"));
            return null;
        }
    }
}
