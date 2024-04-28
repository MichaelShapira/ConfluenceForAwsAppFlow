package com.amazon.custom.appflow.confluence.handler;

import com.amazon.custom.appflow.confluence.SecretsManagerHelper;
import com.amazon.custom.appflow.confluence.configuration.ConfluenceConnectorConfiguration;
import com.amazonaws.appflow.custom.connector.handlers.ConfigurationHandler;
import com.amazonaws.appflow.custom.connector.model.ErrorCode;
import com.amazonaws.appflow.custom.connector.model.ImmutableErrorDetails;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.ConnectorModes;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.DescribeConnectorConfigurationRequest;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.DescribeConnectorConfigurationResponse;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.ImmutableDescribeConnectorConfigurationResponse;
import com.amazonaws.appflow.custom.connector.model.credentials.ImmutableValidateCredentialsResponse;
import com.amazonaws.appflow.custom.connector.model.credentials.ValidateCredentialsRequest;
import com.amazonaws.appflow.custom.connector.model.credentials.ValidateCredentialsResponse;
import com.amazonaws.appflow.custom.connector.model.settings.ImmutableValidateConnectorRuntimeSettingsResponse;
import com.amazonaws.appflow.custom.connector.model.settings.ValidateConnectorRuntimeSettingsRequest;
import com.amazonaws.appflow.custom.connector.model.settings.ValidateConnectorRuntimeSettingsResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ConfluenceConfigurationHandler extends AbstractConfluenceHandler implements ConfigurationHandler {
	  private static final String CONNECTOR_OWNER = "michshap";
	  private static final String CONNECTOR_NAME = "ConfluenceBlogConnector";
	  private static final String CONNECTOR_VERSION = "1.0";
	  private static final Logger LOGGER = LoggerFactory.getLogger(ConfluenceConfigurationHandler.class);
	

	  /**
	   * Validates the user inputs corresponding to the connector settings for a given ConnectorRuntimeSettingScope.
	   *
	   * @param request - {@link ValidateConnectorRuntimeSettingsRequest}
	   * @return - {@link ValidateConnectorRuntimeSettingsResponse}
	   */
	  @Override
	  public ValidateConnectorRuntimeSettingsResponse validateConnectorRuntimeSettings(
	    final ValidateConnectorRuntimeSettingsRequest request
	  ) {
	    return ImmutableValidateConnectorRuntimeSettingsResponse.builder().isSuccess(true).build();
	  }

	  /**
	   * Validates the user provided credentials.
	   *
	   * @param request - {@link ValidateCredentialsRequest}
	   * @return - {@link ValidateCredentialsResponse}
	   */
	  @Override
	  public ValidateCredentialsResponse validateCredentials(final ValidateCredentialsRequest request) {
		  
		  
		   Map<String, String> secrets = SecretsManagerHelper.getSecret(request.credentials().secretArn());
	    	
		 
		String url = secrets.getOrDefault("URL", null);
	    	String userName = secrets.getOrDefault("UserName", null);
	    	String token = secrets.getOrDefault("Token", null);
	    	
	    	String entity = "blogposts?body-format=storage";
	    	
	    
	    	HttpResponse<JsonNode> response=null;
			try {
				response = getConfluenceResponseForEntity(userName,token,url,entity);
			} catch (UnirestException ex) {
				LOGGER.error(ex.getMessage());
			      return ImmutableValidateCredentialsResponse.builder()
			        .errorDetails(ImmutableErrorDetails.builder()
			          .errorCode(ErrorCode.InvalidCredentials)
			          .errorMessage(ex.getMessage()).build())
			        .isSuccess(false).build();
			}
		
	    return ImmutableValidateCredentialsResponse.builder().isSuccess(true).build();
	  }

	  /**
	   * Describes the Connector Configuration supported by the connector.
	   *
	   * @param request - {@link DescribeConnectorConfigurationRequest}
	   * @return {@link DescribeConnectorConfigurationResponse}
	   */
	  @Override
	  public DescribeConnectorConfigurationResponse describeConnectorConfiguration(
	    final DescribeConnectorConfigurationRequest request
	  ) {
		  
		  List<ConnectorModes> cm =   Arrays.asList(ConnectorModes.SOURCE);
		  
	    return ImmutableDescribeConnectorConfigurationResponse.builder()
	      .isSuccess(true)
	      .connectorOwner(CONNECTOR_OWNER)
	      .connectorName(CONNECTOR_NAME)
	      .connectorVersion(CONNECTOR_VERSION)
	      .connectorModes(cm)
	      .connectorRuntimeSetting(ConfluenceConnectorConfiguration.getConnectorRuntimeSettings())
	      .authenticationConfig(ConfluenceConnectorConfiguration.getAuthenticationConfig())
	      .supportedApiVersions(ConfluenceConnectorConfiguration.getSupportedApiVersions())
	      .build();
	  }
	}
