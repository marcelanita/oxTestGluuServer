package org.testClient.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.testClient.model.api.ApiRequest;
import org.testClient.model.api.ApiResponse;
import org.testClient.rest.RestApiClient;

import java.util.Collections;


public class OxClientApi {
    private static final Logger LOG = LoggerFactory.getLogger(OxClientApi.class);

    private final RestApiClient restApiClient;
    private final String basePath;
    @Autowired
    public OxClientApi(RestTemplate restTemplate) {
        this.restApiClient = new RestApiClient(restTemplate);
        this.basePath = "http://localhost:8080";
    }


    public ApiResponse doPost(String onBehalfOfToken, ApiRequest request) {
        return restApiClient.doPost(onBehalfOfToken, basePath + "/api/v1/test-post", request, ApiResponse.class);
    }

    public ApiResponse doGet(String onBehalfOfToken) {
        return restApiClient.doGet(onBehalfOfToken, basePath + "/api/v1/test-get",  ApiResponse.class,Collections.singletonList(MediaType.APPLICATION_JSON.toString()) ,null);
    }

}
