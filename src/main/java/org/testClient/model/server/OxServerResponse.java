package org.testClient.model.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OxServerResponse {

    private String response;

    public String getResponse() {
        return response;
    }

    public OxServerResponse setResponse(String response) {
        this.response = response;
        return this;
    }
}
