package com.amazon.custom.appflow.confluence.handler;

import com.amazonaws.appflow.custom.connector.lambda.handler.BaseLambdaConnectorHandler;

public class ConfluenceLambdaHandler extends BaseLambdaConnectorHandler 
{
	public ConfluenceLambdaHandler() {
	    super(
	      new ConfluenceMetadataHandler(),
	      new ConfluenceRecordHandler(),
	      new ConfluenceConfigurationHandler()
	    );
	  }

}
