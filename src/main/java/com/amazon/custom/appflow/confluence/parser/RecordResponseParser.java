package com.amazon.custom.appflow.confluence.parser;

import com.amazon.custom.appflow.confluence.Blog;
import com.amazonaws.appflow.custom.connector.model.metadata.Entity;
import com.amazonaws.appflow.custom.connector.model.metadata.EntityDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.FieldDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableEntityDefinition;

/*
 * -
 *  * #%L
 * * Example Custom Connector.
 *  *
 * %%
 * Copyright (C) 2021 Amazon Web Services
 *  *
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 *
 */


import com.amazonaws.appflow.custom.connector.model.write.ImmutableWriteRecordResult;
import com.amazonaws.appflow.custom.connector.model.write.WriteRecordResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Sample implementation for parsing record API responses.
 */
public final class RecordResponseParser extends AbstractParser {
    private RecordResponseParser() {
    }
    private static final  ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson GSON = new Gson();
    private static final String TITLE_FIELD = "title";
    private static final String ID_FIELD = "id";
    private static final String BLOG_CONTENT = "blogContent";
    

    public static List<String>  parseQueryResponse(final String jsonString,List<String> fieldNames) {
        final JsonObject parentObject = GSON.fromJson(jsonString, JsonObject.class);
        
        Blog blog = new Blog();
        
        final List<String> records = new ArrayList<>();
        
        if (fieldNames.size()==0)
        	return records;
               
        if (Objects.nonNull(parentObject.get("results"))) {
            final JsonArray jsonArray = parentObject.get("results").getAsJsonArray();
            for ( JsonElement jsonElement : jsonArray) {
            	
            	if (fieldNames.contains(TITLE_FIELD))
            	{
	            	String blogTitle = jsonElement.getAsJsonObject().get(TITLE_FIELD).getAsString();
	            	blog.title = blogTitle;
            	}
            	if (fieldNames.contains(ID_FIELD))
            	{
	            	String blogId = jsonElement.getAsJsonObject().get("id").getAsString();
	            	blog.id = blogId;
            	}
            	if (fieldNames.contains(BLOG_CONTENT))
            	{
	            	try
	            	{
	            	 String blogContent = jsonElement.getAsJsonObject().get("body").getAsJsonObject().get("storage").getAsJsonObject().get("value").getAsString();
	            	 blog.blogContent = blogContent;
	            	}
	            	catch(Exception ex)
	            	{
	            		System.err.println(ex.getMessage());
	            	}
	            	try
	            	{
	                	records.add(objectMapper.writeValueAsString(blog));
	            	}
	            	catch(Exception ex)
	            	{
	            		System.err.println(ex.getMessage());
	            	}
            	}
        
            	
            }
        }

        return records;
    }

    public static WriteRecordResult parseWriteResponse(final String jsonString) {
        final JsonObject parentObject = GSON.fromJson(jsonString, JsonObject.class);
        return ImmutableWriteRecordResult.builder()
                .recordId(getStringValue(parentObject, "id"))
                .isSuccess(getBooleanValue(parentObject, "success"))
                .errorMessage(Objects.nonNull(parentObject.get("errors")) ?
                              parentObject.get("errors").getAsJsonArray().toString() : null)
                .build();
    }
}