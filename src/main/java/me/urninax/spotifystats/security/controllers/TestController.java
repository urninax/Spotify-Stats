package me.urninax.spotifystats.security.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController{

    @PostMapping("/hello")
    public ResponseEntity<?> testEndpoint(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
