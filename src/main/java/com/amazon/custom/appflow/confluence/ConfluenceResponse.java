package com.amazon.custom.appflow.confluence;


import javax.annotation.Nullable;

/**
 * Class contains Salesforce response.
 */

public interface ConfluenceResponse {
    int statusCode();

    @Nullable
    String response();

    String errorReason();
}

