package com.amazon.custom.appflow.confluence.parser;

import com.google.gson.JsonObject;

public abstract class AbstractParser {
	  //Gets a String value from a Json Object and defaults to null with null values
    protected static String getStringValue(final JsonObject object, final String fieldName) {
        if (fieldName == null || object.get(fieldName) == null || object.get(fieldName).isJsonNull()) {
            return null;
        } else {
            return object.get(fieldName).getAsString();
        }
    }

    //Gets a Boolean value from a Json Object and defaults to false with null values
    // will default to true if fieldname is "true"
    protected static boolean getBooleanValue(final JsonObject object, final String fieldName) {
        if (fieldName == null) {
            return false;
        } else if ("true".equals(fieldName)) {
            return true;
        } else if (object.get(fieldName).isJsonNull()) {
            return false;
        } else {
            return object.get(fieldName).getAsBoolean();
        }
    }

}
