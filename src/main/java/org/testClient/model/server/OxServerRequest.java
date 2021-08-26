package org.testClient.model.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OxServerRequest {

    private String state;


    public String getState() {
        return state;
    }

    public OxServerRequest setState(String state) {
        this.state = state;
        return this;
    }
}
