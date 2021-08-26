package org.testClient.rest;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RestUtils {

    public static URI url(String basePath, String... path) {
        return url(basePath, Arrays.asList(path), Map.of());
    }

    public static URI url(String basePath, String path, Map<String, String> queryParams) {
        return url(basePath, List.of(path), queryParams);
    }

    public static URI url(String basePath, List<String> path, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath);
        Iterator var4 = path.iterator();

        while(var4.hasNext()) {
            String pathSegment = (String)var4.next();
            builder.path("/" + pathSegment);
        }

        queryParams.forEach((key, value) -> {
            builder.queryParam(key, new Object[]{value});
        });
        return builder.build().encode().toUri();
    }
}
