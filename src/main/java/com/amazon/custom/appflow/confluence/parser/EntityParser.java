package com.amazon.custom.appflow.confluence.parser;

import com.amazonaws.appflow.custom.connector.model.metadata.Entity;
import com.amazonaws.appflow.custom.connector.model.metadata.EntityDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.FieldDataType;
import com.amazonaws.appflow.custom.connector.model.metadata.FieldDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableEntity;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableEntityDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableFieldDefinition;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableReadOperationProperty;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Sample implementation for parsing metadata API responses.
 */
public final class EntityParser extends AbstractParser {

    private EntityParser() {
    }

    private static final Gson GSON = new Gson();

    public static List<Entity> parseEntity(final String jsonString) {
        final JsonObject parentObject = GSON.fromJson(jsonString, JsonObject.class);
        final List<Entity> entityList = new ArrayList<>();
        if (Objects.nonNull(parentObject.get("sobjects"))) {
            final JsonArray jsonArray = parentObject.get("sobjects").getAsJsonArray();
            for (final JsonElement jsonElement : jsonArray) {
                entityList.add(buildEntity(jsonElement.getAsJsonObject()));
            }
        } else if (Objects.nonNull(parentObject.getAsJsonObject("objectDescribe"))) {
            entityList.add(buildEntity(parentObject.getAsJsonObject("objectDescribe")));
        }

        return entityList;
    }

    public static EntityDefinition parseEntityDefinition(final String jsonString) {
    	
    	System.out.println("parseEntityDefinition "+jsonString);
        final List<FieldDefinition> fieldDefinitions = new ArrayList<>();
        final JsonObject parentObject = GSON.fromJson(jsonString, JsonObject.class);
        
        String json = "{ \"name\": \"BlogContent\", \"label\":  \"BlogContent\" }";

        JsonObject jsonObject = JsonParser.parseString(json)
            .getAsJsonObject();
        
        final Entity entity = buildEntityForBlog(jsonObject);

        fieldDefinitions.add(buildFieldDefinition("title",null,"string",false));
    	
    	fieldDefinitions.add(buildFieldDefinition("id",null,"string",true));
    	
    	fieldDefinitions.add(buildFieldDefinition("blogContent",null,"string",true));
        

        return ImmutableEntityDefinition.builder().entity(entity).fields(fieldDefinitions).build();
    }

    private static Entity buildEntity(final JsonObject field) {
        final String name = getStringValue(field, "name");
        final boolean hasNestedEntities =false;
        final String description = getStringValue(field, "label");
        final Entity entity = ImmutableEntity.builder()
                .entityIdentifier(name)
                .label(description)
                .hasNestedEntities(hasNestedEntities)
                .description(description)
                .build();
        return entity;
    }
    private static Entity buildEntityForBlog(final JsonObject field) {
        final String name = getStringValue(field, "name");
        final boolean hasNestedEntities =false;
        final String description = getStringValue(field, "label");
        final Entity entity = ImmutableEntity.builder()
                .entityIdentifier(name)
                .label(description)
                .hasNestedEntities(hasNestedEntities)
                .description(description)
                .build();
        return entity;
    }
    private static FieldDefinition buildFieldDefinition(String title, String defaultValue,String type,boolean isPrimary) {
       
    	final FieldDataType dataType = convertDataType(type);
    	
        final FieldDefinition fieldDefinition = ImmutableFieldDefinition.builder()
                .fieldName(title)
                .dataType(dataType)
                .dataTypeLabel(type)
                .label(title)
                .description(title)
                .defaultValue(defaultValue)
                .isPrimaryKey(isPrimary)
                .readProperties(ImmutableReadOperationProperty.builder()
                        .isQueryable(true)
                        .isRetrievable(true)
                        .build())
                
                .build();
        return fieldDefinition;
    }


    private static FieldDataType convertDataType(final String type) {
        switch (type) {
            case "int":
                return FieldDataType.Integer;
            case "double":
                return FieldDataType.Double;
            case "long":
                return FieldDataType.Long;
            case "id":
            case "string":
            case "textarea":
                return FieldDataType.String;
            case "date":
                return FieldDataType.Date;
            case "datetime":
            case "time":
                return FieldDataType.DateTime;
            case "boolean":
                return FieldDataType.Boolean;
            default:
                return FieldDataType.Struct;
        }
    }
}