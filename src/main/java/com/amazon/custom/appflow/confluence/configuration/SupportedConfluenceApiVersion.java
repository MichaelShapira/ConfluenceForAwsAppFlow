package com.amazon.custom.appflow.confluence.configuration;

public enum SupportedConfluenceApiVersion {
	V2("v2");

    private final String versionNumber;

    SupportedConfluenceApiVersion(final String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionNumber() {
        return this.versionNumber;
    }
}
