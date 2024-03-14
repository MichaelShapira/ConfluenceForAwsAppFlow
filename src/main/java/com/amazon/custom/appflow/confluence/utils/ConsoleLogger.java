package com.amazon.custom.appflow.confluence.utils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Arrays;

public class ConsoleLogger implements LambdaLogger {

  @Override
  public void log(final String message) {
    System.out.println(message);
  }

  @Override
  public void log(final byte[] message) {
    System.out.println(Arrays.toString(message));
  }
}
