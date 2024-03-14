package com.amazon.custom.appflow.confluence.handler;

import com.amazon.custom.appflow.confluence.SecretsManagerHelper;
import com.amazon.custom.appflow.confluence.parser.EntityParser;
import com.amazonaws.appflow.custom.connector.handlers.MetadataHandler;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ErrorDetails;
import com.amazonaws.appflow.custom.connector.model.metadata.DescribeEntityRequest;
import com.amazonaws.appflow.custom.connector.model.metadata.DescribeEntityResponse;
import com.amazonaws.appflow.custom.connector.model.metadata.Entity;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableDescribeEntityResponse;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableEntity;
import com.amazonaws.appflow.custom.connector.model.metadata.ImmutableListEntitiesResponse;
import com.amazonaws.appflow.custom.connector.model.metadata.ListEntitiesRequest;
import com.amazonaws.appflow.custom.connector.model.metadata.ListEntitiesResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfluenceMetadataHandler extends AbstractConfluenceHandler implements MetadataHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceMetadataHandler.class);

  
    @Override
    public ListEntitiesResponse listEntities(final ListEntitiesRequest request) {
    	
   	String additionalData = "Blog entity of your space";
    	
    	final Entity entity = ImmutableEntity.builder()
                .entityIdentifier("blogposts?body-format=storage")
                .label(additionalData)
                .hasNestedEntities(false)
                .description(additionalData)
                .build();
    	
    	final List<Entity> entityList = new ArrayList<>();
    	entityList.add(entity);
    	

            return ImmutableListEntitiesResponse.builder()
                    .isSuccess(true)
                    .entities(entityList)
                    .build();
     }
    

    @Override
    public DescribeEntityResponse describeEntity(final DescribeEntityRequest request) {
      
    	
    	 Map<String, String> secrets = SecretsManagerHelper.getSecret(request.connectorContext().credentials().secretArn());
	    	
 		 String url = secrets.getOrDefault("URL", null);
	     String userName = secrets.getOrDefault("UserName", null);
	     String token = secrets.getOrDefault("Token", null);
   
	    	
	    	
    	String entity = request.entityIdentifier();
    
    	HttpResponse<JsonNode> response;
		try {
			response = getConfluenceResponseForEntity(userName,token,url,entity);
			System.out.println(response.getBody());
			LOGGER.info(response.getBody().toString());
		} catch (UnirestException e) {
			LOGGER.error(e.getMessage());
	        ErrorDetails errorDetails =  ImmutableErrorDetails.builder().errorCode(ErrorCode.ServerError).errorMessage(e.getMessage()).build();
            return ImmutableDescribeEntityResponse.builder().isSuccess(false).errorDetails(errorDetails).build();
		}
    	
    
      
        return ImmutableDescribeEntityResponse.builder()
                    .isSuccess(true)
                    .entityDefinition(EntityParser.parseEntityDefinition(response.getBody().toString()))
                    .build();
        
    }
}