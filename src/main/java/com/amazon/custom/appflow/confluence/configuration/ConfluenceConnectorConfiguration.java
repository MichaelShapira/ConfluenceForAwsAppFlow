package com.amazon.custom.appflow.confluence.configuration;


import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.ConnectorModes;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.ImmutableOAuth2Defaults;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.OAuth2Defaults;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.OAuth2GrantType;
import com.amazonaws.appflow.custom.connector.model.settings.ConnectorRuntimeSetting;
import com.amazonaws.appflow.custom.connector.model.settings.ConnectorRuntimeSettingDataType;
import com.amazonaws.appflow.custom.connector.model.settings.ConnectorRuntimeSettingScope;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.AuthenticationConfig;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.ImmutableAuthenticationConfig;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.ImmutableCustomAuthConfig;
import com.amazonaws.appflow.custom.connector.model.settings.ImmutableConnectorRuntimeSetting;
import com.amazonaws.appflow.custom.connector.model.write.WriteOperationType;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.AuthParameter;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.CustomAuthConfig;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.ImmutableAuthParameter;


public class ConfluenceConnectorConfiguration {
	private ConfluenceConnectorConfiguration() {
    }
	
	

    public static List<ConnectorRuntimeSetting> getConnectorRuntimeSettings() {
    	ConnectorRuntimeSetting debug = ImmutableConnectorRuntimeSetting.builder()
                .key("debug")
                .dataType(ConnectorRuntimeSettingDataType.Boolean)
                .required(true)
                .label("Debug?")
                .description("Send debug messages to CloudWatch")
                .scope(ConnectorRuntimeSettingScope.CONNECTOR_PROFILE)
                .build();
    	return Arrays.asList(debug);
    	/*
    	 * ConnectorRuntimeSetting instanceUrl = ImmutableConnectorRuntimeSetting.builder()
                .key(ConnectorSettingKey.INSTANCE_URL)
                .dataType(ConnectorRuntimeSettingDataType.String)
                .required(true)
                .label("Confluence URL")
                .description("URL of the instance where user wants to run the operations. It looks like https://XXXXXXXXX.atlassian.net")
                .scope(ConnectorRuntimeSettingScope.CONNECTOR_PROFILE)
                .build();

        ConnectorRuntimeSetting userName = ImmutableConnectorRuntimeSetting.builder()
                .key(ConnectorSettingKey.USER_NAME)
                .dataType(ConnectorRuntimeSettingDataType.String)
                .required(true)
                .label("Confluence User Name")
                .description("Confluence User Name. You should see it inside your profile Avatar")
                .scope(ConnectorRuntimeSettingScope.CONNECTOR_PROFILE)
                .build();
        
        
        ConnectorRuntimeSetting token = ImmutableConnectorRuntimeSetting.builder()
                .key(ConnectorSettingKey.TOKEN)
                .dataType(ConnectorRuntimeSettingDataType.String)
                .required(true)
                .label("Confluence Access Token")
                .description("Confluence Access Token")
                .scope(ConnectorRuntimeSettingScope.CONNECTOR_PROFILE)
                .build();

        return Arrays.asList(instanceUrl, userName,token);
        */
    }

    public static List<ConnectorModes> getConnectorModes() {
        return Arrays.asList(ConnectorModes.SOURCE);
    }

    public static AuthenticationConfig getAuthenticationConfig() {

        ConfluenceAuthConfig config = new ConfluenceAuthConfig();
        return ImmutableAuthenticationConfig.builder()
            .isCustomAuthSupported(true)
            .addCustomAuthConfig(
                ImmutableCustomAuthConfig.builder()
                    .authenticationType(config.authenticationType())
                    .addAllAuthParameters(config.authParameters())
                    .build())
            .build();
      }
    
    /*
    public static AuthenticationConfig getAuthenticationConfig() {
        return ImmutableAuthenticationConfig.builder().isBasicAuthSupported(true).
                //.isOAuth2Supported(true)
               // .oAuth2Defaults(getOAuth2Defaults())
                .build();
    }*/
    

    private static OAuth2Defaults getOAuth2Defaults() {
        return ImmutableOAuth2Defaults.builder()
                .authURL(ImmutableList.of("https://login.salesforce.com/services/oauth2/authorize",
                        "https://test.salesforce.com/services/oauth2/authorize"))
                .tokenURL(ImmutableList.of("https://login.salesforce.com/services/oauth2/token",
                        "https://test.salesforce.com/services/oauth2/token"))
                .oAuthScopes(Arrays.asList("api", "refresh_token"))
                .addOAuth2GrantTypesSupported(OAuth2GrantType.AUTHORIZATION_CODE)
                .build();
    }

    public static List<String> getSupportedApiVersions() {
        return Collections.singletonList(SupportedConfluenceApiVersion.V2.getVersionNumber());
    }

    public static List<WriteOperationType> supportedWriteOperations() {
        return Arrays.asList(WriteOperationType.INSERT, WriteOperationType.UPDATE, WriteOperationType.UPSERT);
    }

}
