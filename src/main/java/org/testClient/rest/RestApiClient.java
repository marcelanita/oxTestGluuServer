package org.testClient.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RestApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiClient.class);

    private final RestTemplate restTemplate;

    public RestApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <Res> Res doGet(String onBehalfOfToken, String uri, Class<Res> responseType, List<String> mediaTypes, Charset charset) {
        return this.doCall(uri, () -> {
            try {
                LOGGER.debug("{}::doGet()::uri={}", this.getClass().getName(), uri);
                HttpEntity<Object> requestEntity = this.buildReqHttpEntityForGet(onBehalfOfToken, mediaTypes, charset);
                ResponseEntity<Res> response = this.restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType, new Object[0]);
                return response.getBody();
            } catch (Exception var8) {
                LOGGER.debug("Exception in ClientBase on {}", uri);
                LOGGER.error("Handling REST exception while calling {} ", uri);
                return null;
            }
        });
    }

    public <Res, Req> Res doPost(String onBehalfOfToken, String uri, Req request, Class<Res> responseType) {
        return this.doCall(uri, () -> {
            try {
                LOGGER.debug("{}::doPost()::uri={}", this.getClass().getName(), uri);
                HttpEntity<Req> requestEntity = this.buildReqHttpEntityForJsonPostWithJsonBody(onBehalfOfToken, request);
                ResponseEntity<Res> result = this.restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseType, new Object[0]);
                return result.getBody();
            } catch (Exception var7) {
                LOGGER.debug("Exception in ClientBase on {}", uri);
                LOGGER.error("Handling REST exception while calling {} ", uri);
                return null;
            }
        });
    }



    private HttpEntity<Object> buildReqHttpEntityForGet(String onBehalfOfToken, List<String> mediaTypes, Charset charset) {
        HttpHeaders headers = new HttpHeaders();
        this.addCommonHeaders(onBehalfOfToken, headers, mediaTypes, charset);
        return new HttpEntity(headers);
    }

    private <Res> Res doCall(String uri, Supplier<Res> call) {
        return call.get();
    }


    private <Req> HttpEntity<Req> buildReqHttpEntityForJsonPostWithJsonBody(String onBehalfOfToken, Req request) {
        HttpHeaders headers = new HttpHeaders();
        this.addCommonPostHeaders(headers);
        this.addCommonHeaders(onBehalfOfToken, headers);
        return new HttpEntity(request, headers);
    }
    private void addCommonPostHeaders(HttpHeaders headers) {
        MediaType jsonUtf8 = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(jsonUtf8);
    }
    private void addCommonHeaders(String onBehalfOfToken, HttpHeaders headers) {
        this.addCommonHeaders(onBehalfOfToken, headers, Collections.singletonList("application/json"), StandardCharsets.UTF_8);
    }

    private void addCommonHeaders(String onBehalfOfToken, HttpHeaders headers, List<String> mediaTypes, Charset charset) {
        List<MediaType> acceptMediaTypes = (List)mediaTypes.stream().map(MediaType::valueOf).collect(Collectors.toList());
        headers.setAccept(acceptMediaTypes);
        if (charset != null) {
            headers.setAcceptCharset(Collections.singletonList(charset));
        }

        if (onBehalfOfToken != null) {
            headers.add("X-ONBEHALF-OF", onBehalfOfToken);
        }
    }


}
