package org.testClient.rest;

import org.apache.logging.log4j.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;

public class RestClient {
    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    private final RestTemplate restTemplate;
    private final RestClient.Authentication authentication;
    private final RestClient.RetryMechanism retryMechanism;
    private final MediaType contentType;

    public RestClient(RestTemplate restTemplate) {
        this(restTemplate, RestClient.noAuthentication() ,RestClient.noRetry());
    }
    public RestClient(RestTemplate restTemplate, String user, String password) {
        this(restTemplate, RestClient.basicAuthentication(user,password) ,RestClient.noRetry());
    }
    public RestClient(RestTemplate restTemplate, RestClient.Authentication authentication, RestClient.RetryMechanism retryMechanism) {
        this(restTemplate, authentication, retryMechanism, MediaType.APPLICATION_JSON_UTF8);
    }
    public RestClient(RestTemplate restTemplate, Authentication authentication, RetryMechanism retryMechanism, MediaType contentType) {
        this.restTemplate = restTemplate;
        this.authentication = authentication;
        this.retryMechanism = retryMechanism;
        this.contentType = contentType;
    }
    public <REQ, RES> RES send(HttpMethod method, URI uri, REQ body, Class<RES> responseClass) {
        return send(method, uri, body, Map.of(), responseClass);
    }
    public <REQ, RES> RES send(HttpMethod method, URI uri, REQ body, Map<String, String> requestHeaders, Class<RES> responseClass) {
        return this.send(method, uri, body, requestHeaders, responseClass, this.authentication);
    }
    public <REQ, RES> RES send(HttpMethod method, URI uri, REQ body, Map<String, String> requestHeaders, Class<RES> responseClass, RestClient.Authentication authentication) {
        return this.sendInternal(method, uri, body, this.restCall(responseClass), authentication, requestHeaders);
    }

    private <REQ, RES> RestCall<REQ, RES> restCall(Class<RES> responseClass) {
        return (method, uri, requestEntity) -> {
            return this.restTemplate.exchange(uri, method, requestEntity, responseClass);
        };
    }
    private <REQ, RES> RES sendInternal(HttpMethod method, URI uri, REQ body, RestClient.RestCall<REQ, RES> restCall, RestClient.Authentication authentication, Map<String, String> requestheaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(this.contentType);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON_UTF8));
        headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
        Objects.requireNonNull(headers);
        requestheaders.forEach(headers::add);
        HttpHeaders finalHttpHeaders = authentication.addAuthentication(headers);
        HttpEntity requestEntity = new HttpEntity(body, finalHttpHeaders);

        String requestBody;
        try {
            return (RES) restCall.executeRestCall(method, uri, requestEntity).getBody();
        } catch (RestClientResponseException var) {
            requestBody = JsonHelper.writeValueAsString(body);
            String responseBody = var.getResponseBodyAsString();
            LOG.error("Error while calling {} with request {} and response {}", new Object[]{uri, requestBody, responseBody, var});
            throw var;
        } catch (RuntimeException var1) {
            requestBody = JsonHelper.writeValueAsString(body);
            LOG.error("Error while calling {} with request {}", new Object[]{uri, requestBody, var1});
            throw var1;
        }
    }



    public interface Authentication {
        HttpHeaders addAuthentication(HttpHeaders httpHeaders);
    }

    private interface RestCall<REQ, RES> {
        HttpEntity<RES> executeRestCall(HttpMethod method, URI uri, HttpEntity<REQ> requestEntity);
    }
    public interface RetryMechanism {
        <Res> Res doWithRetry(URI uri, Supplier<Res> call);
    }

    public static RestClient.Authentication bearerToken(String token) {
        return (httpHeaders) -> {
            HttpHeaders newHttpHeaders = new HttpHeaders(httpHeaders);
            newHttpHeaders.add("Authorization", "Bearer " + token);
            return newHttpHeaders;
        };
    }
    public static RestClient.Authentication basicAuthentication(String username, String password) {
        return (httpHeaders) -> {
            HttpHeaders newHttpHeaders = new HttpHeaders(httpHeaders);
            String var10002 = username + ":" + password;
            newHttpHeaders.add("Authorization", "Basic " + Base64Util.encode(var10002));
            return newHttpHeaders;
        };
    }

    public static RestClient.Authentication noAuthentication() {
        return (httpHeaders) -> {
            return httpHeaders;
        };
    }

    public static RestClient.RetryMechanism noRetry() {
        return new RestClient.RetryMechanism() {
            public <Res> Res doWithRetry(URI uri, Supplier<Res> call) {
                return call.get();
            }
        };
    }
}
