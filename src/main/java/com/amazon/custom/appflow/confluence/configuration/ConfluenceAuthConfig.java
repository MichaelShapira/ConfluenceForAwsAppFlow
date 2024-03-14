package com.amazon.custom.appflow.confluence.configuration;

import java.util.Arrays;
import java.util.List;

import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.AuthParameter;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.CustomAuthConfig;
import com.amazonaws.appflow.custom.connector.model.connectorconfiguration.auth.ImmutableAuthParameter;

public class ConfluenceAuthConfig implements CustomAuthConfig {


	public List<AuthParameter> authParameters() {
	    AuthParameter url = ImmutableAuthParameter.builder()
	      .key("URL")
	      .label("Confluence URL")
	      .description("Confluence URL without trailing /")
	      .required(true)
	      .sensitiveField(false)
	      .build();

	    AuthParameter userName = ImmutableAuthParameter.builder()
	      .key("UserName")
	      .label("User Name")
	      .description("Confluence User Name")
	      .required(true)
	      .sensitiveField(false)
	      .build();
	    AuthParameter token = ImmutableAuthParameter.builder()
	      .key("Token")
	      .label("Token")
	      .description("Confluence API Token")
	      .required(true)
	      .sensitiveField(false)
	      .build();

	   

	    return Arrays.asList(url, userName, token);
	}

	@Override
	public String authenticationType() {
		// TODO Auto-generated method stub
		return "API";
	}
}
