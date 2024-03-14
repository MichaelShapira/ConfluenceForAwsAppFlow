package com.amazon.custom.appflow.confluence;


import com.amazon.custom.appflow.confluence.handler.ConfluenceLambdaHandler;
import com.amazon.custom.appflow.confluence.utils.TestContext;
import com.amazonaws.appflow.custom.connector.lambda.handler.BaseLambdaConnectorHandler;
import com.amazonaws.appflow.custom.connector.model.credentials.AuthenticationType;
import com.amazonaws.appflow.custom.connector.model.credentials.ImmutableCredentials;
import com.amazonaws.appflow.custom.connector.model.credentials.ImmutableValidateCredentialsRequest;
import com.amazonaws.appflow.custom.connector.model.credentials.ValidateCredentialsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

class ValidateCredentialsRequestTest {
  @Test
  void testValidateCredentialsRequest() throws JsonProcessingException {
    // arrange
    final ObjectMapper objectMapper = new ObjectMapper();
    final BaseLambdaConnectorHandler requestHandler = new ConfluenceLambdaHandler();

    // act
    ValidateCredentialsRequest request = ImmutableValidateCredentialsRequest.builder()
      .credentials(
        ImmutableCredentials.builder().secretArn("your-secret-arn")
          .authenticationType(AuthenticationType.CustomAuth)
          .build())
      .build();

    requestHandler.handleRequest(
      new ByteArrayInputStream(objectMapper.writeValueAsBytes(request)),
      Mockito.mock(OutputStream.class),
      new TestContext()
    );
  }
}