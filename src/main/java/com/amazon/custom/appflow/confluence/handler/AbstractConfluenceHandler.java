package com.amazon.custom.appflow.confluence.handler;


import com.amazon.custom.appflow.confluence.ConfluenceResponse;

import com.amazonaws.appflow.custom.connector.model.ConnectorContext;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ErrorDetails;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;
import com.amazonaws.appflow.custom.connector.model.credentials.BasicAuthCredentials;
import com.amazonaws.appflow.custom.connector.model.credentials.Credentials;
import com.amazonaws.appflow.custom.connector.util.CredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.commons.lang3.Range;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class AbstractConfluenceHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfluenceHandler.class);

    private static final Range<Integer> HTTP_STATUS_SUCCESS_CODE_RANGE = Range.between(200, 299);

    protected HttpResponse<JsonNode> getConfluenceResponseForEntity(final ConnectorContext connectorContext,String url,String entity) throws UnirestException {
        LOGGER.info("Fetching access token from Secret Arn.");
        BasicAuthCredentials credentials = CredentialsProvider.getBasicAuthCredentials(AWSSecretsManagerClientBuilder.defaultClient(),
                connectorContext.credentials().secretArn());
        
        HttpResponse<JsonNode> response = Unirest.get(url+"/")
				  .basicAuth(credentials.userName(),credentials.password())
				  .header("Accept", "application/json")
				  .asJson();
        
        
        return response;
    }
    protected HttpResponse<JsonNode> getConfluenceResponseForEntity(final Credentials creds,String url,String entity) throws UnirestException {
        LOGGER.info("Fetching access token from Secret Arn. "+creds.secretArn());
        BasicAuthCredentials credentials = CredentialsProvider.getBasicAuthCredentials(AWSSecretsManagerClientBuilder.defaultClient(),
        		creds.secretArn());
        

        HttpResponse<JsonNode> response = Unirest.get(url+"/")
				  .basicAuth(credentials.userName(),credentials.password())
				  .header("Accept", "application/json")
				  .asJson();
        
        
        return response;
    }
    
    protected HttpResponse<JsonNode> getConfluenceResponseForEntity(String userName,String password,String url,String entity) throws UnirestException {
        

        HttpResponse<JsonNode> response = Unirest.get(url+"/"+entity)
				  .basicAuth(userName,password)
				  .header("Accept", "application/json")
				  .asJson();
        
        
        return response;
    }
    
    protected ErrorDetails checkForErrorsInConfluenceResponse(final ConfluenceResponse response) {
        final int statusCode = response.statusCode();
        if (statusCode >= HTTP_STATUS_SUCCESS_CODE_RANGE.getMinimum() &&
            statusCode < HTTP_STATUS_SUCCESS_CODE_RANGE.getMaximum()) {
            return null;
        }

        ErrorCode errorCode = null;
        if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            errorCode = ErrorCode.InvalidCredentials;
        } else if (statusCode == HttpStatus.SC_BAD_REQUEST) {
            errorCode = ErrorCode.InvalidArgument;
        } else {
            errorCode = ErrorCode.ServerError;
        }
        String errorMessage =
                "Request failed with status code " + response.statusCode() + " error reason " + response.errorReason() +
                " and salesforce response is " + response.response();
        LOGGER.error(errorMessage);
        return ImmutableErrorDetails.builder().errorCode(errorCode).errorMessage(errorMessage).build();
    }




}
