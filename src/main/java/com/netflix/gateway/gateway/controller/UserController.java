package com.netflix.gateway.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 27/10/2019 11:09
 */
@RestController
@RequestMapping("/teste")
public class UserController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        List<ServiceInstance> instances = discoveryClient.getInstances("netflix-user");
        System.out.println(instances.get(0).getUri().toString());
        return new ResponseEntity<>("Teste", HttpStatus.OK);
    }

    @RequestMapping("/users")
    @ResponseBody
    public String getUsers() {
        return "{\"users\":[{\"name\":\"Lucas\", \"country\":\"Brazil\"}," +
                "{\"name\":\"Jackie\",\"country\":\"China\"}]}";
    }

}
