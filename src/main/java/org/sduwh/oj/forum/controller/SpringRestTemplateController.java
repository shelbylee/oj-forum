package org.sduwh.oj.forum.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SpringRestTemplateController {

    @Autowired
    private RestTemplate restTemplate;
    /***********HTTP GET method*************/
    @GetMapping("/testGetApi")
    public String getJson(){
        String url="http://localhost:8000/account/sso";
        //String json =restTemplate.getForObject(url,Object.class);
        ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String json = results.getBody();
        return json;
    }

    /**********HTTP POST method**************/
    @PostMapping(value = "/testPost")
    public Object postJson(@RequestBody JsonObject param) {
        System.out.println(param.toString());
        param.addProperty("username", "a");
        param.addProperty("password", "123");
        return param;
    }

    @PostMapping(value = "/testPostApi")
    public Object testPost() {
        String url = "http://localhost:8000/api/login";
        JsonObject postData = new JsonObject();
        postData.addProperty("descp", "request for post");
        Gson json = restTemplate.postForEntity(url, postData, Gson.class).getBody();
        return json;
    }

}
