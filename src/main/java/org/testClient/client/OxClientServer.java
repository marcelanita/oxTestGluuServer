package org.testClient.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.testClient.model.server.OxServerRequest;
import org.testClient.model.server.OxServerResponse;
import org.testClient.rest.RestClient;
import org.testClient.rest.RestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class OxClientServer  {

    private static final Logger LOG = LoggerFactory.getLogger(OxClientServer.class);

    private String serverUrl;
    private final RestClient restClient;
    private String username="admin";
    private String password="ick+Podbug6r";

    @Autowired
    public OxClientServer(RestTemplate restTemplate) {
        this.serverUrl = "https://mike-test.gluu.org";
        this.restClient = new RestClient(restTemplate,username,password);
    }



    public OxServerResponse registerUser(OxServerRequest oxRequest) {
        Map<String, String> queryParameters = new HashMap<>();
        try {
            String urlPath ="/identity/login.htm";
            return restClient.send(HttpMethod.GET, RestUtils.url(serverUrl, urlPath,queryParameters), oxRequest,
                     OxServerResponse.class);
        } catch (RuntimeException e) {
            throw e;
        }

    }

    public OxServerResponse token(OxServerRequest oxRequest) {
        Map<String, String> queryParameters = Map.of("user", "userName");
        try {
            return restClient.send(HttpMethod.GET, RestUtils.url(serverUrl, "/token",queryParameters), oxRequest,
                    Collections.emptyMap(), OxServerResponse.class);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
