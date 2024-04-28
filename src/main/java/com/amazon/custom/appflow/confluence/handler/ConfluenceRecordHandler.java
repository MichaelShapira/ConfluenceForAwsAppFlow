package com.amazon.custom.appflow.confluence.handler;


import com.amazon.custom.appflow.confluence.SecretsManagerHelper;
import com.amazon.custom.appflow.confluence.parser.RecordResponseParser;
import com.amazonaws.appflow.custom.connector.handlers.RecordHandler;
import com.amazonaws.appflow.custom.connector.model.ConnectorContext;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ErrorDetails;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;
import com.amazonaws.appflow.custom.connector.model.query.ImmutableQueryDataResponse;
import com.amazonaws.appflow.custom.connector.model.query.QueryDataRequest;
import com.amazonaws.appflow.custom.connector.model.query.QueryDataResponse;
import com.amazonaws.appflow.custom.connector.model.retreive.RetrieveDataRequest;
import com.amazonaws.appflow.custom.connector.model.retreive.RetrieveDataResponse;
import com.amazonaws.appflow.custom.connector.model.write.WriteDataRequest;
import com.amazonaws.appflow.custom.connector.model.write.WriteDataResponse;



import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ConfluenceRecordHandler extends AbstractConfluenceHandler implements RecordHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceRecordHandler.class);



    @Override
    public RetrieveDataResponse retrieveData(final RetrieveDataRequest request) {
    	
    	System.out.println("retrieveData");
    	return null;
    }

    @Override
    public WriteDataResponse writeData(final WriteDataRequest request) {
		
    	System.out.println("writeData");
  	    return null;
    }

    @Override
    public QueryDataResponse queryData(final QueryDataRequest request) {
     
      System.out.println("queryData");
      Map<String, String> secrets = SecretsManagerHelper.getSecret(request.connectorContext().credentials().secretArn());
      String url = secrets.getOrDefault("URL", null);
      String userName = secrets.getOrDefault("UserName", null);
      String token = secrets.getOrDefault("Token", null);
  
  
      HttpResponse<JsonNode> response=null;
		try {
			response = getConfluenceResponseForEntity(userName,token,url,request.entityIdentifier());
			return ImmutableQueryDataResponse.builder()
                    .records(RecordResponseParser.parseQueryResponse(response.getBody().toString(),request.selectedFieldNames()))
                    .isSuccess(true)
                    .build();
		} catch (UnirestException ex) {
			LOGGER.error(ex.getMessage());
			ErrorDetails errorDetails =  ImmutableErrorDetails.builder().errorCode(ErrorCode.ServerError).errorMessage(ex.getMessage()).build();
            
			return ImmutableQueryDataResponse.builder().errorDetails(errorDetails).isSuccess(false).build();
		}
    	
    	
    	
    }
   
}
