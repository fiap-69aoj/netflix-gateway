package com.netflix.gateway.gateway.service;

import com.netflix.gateway.gateway.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 31/10/2019 15:24
 */
@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public UserResponse findUser(final String username, final String password) {
        List<ServiceInstance> instances = discoveryClient.getInstances("NETFLIX-USER");
        String serviceUri = String.format("%s/", instances.get(0).getUri().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity entity = new HttpEntity(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUri)
                .queryParam("email", username)
                .queryParam("password", password);

        final ResponseEntity<UserResponse> exchange = restTemplate
                .exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, UserResponse.class);
        return exchange.getBody();
    }
}
