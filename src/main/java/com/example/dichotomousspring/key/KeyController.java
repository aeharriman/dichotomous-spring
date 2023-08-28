package com.example.dichotomousspring.key;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/keys")
public class KeyController {
    private final KeyService service;

    public KeyController(KeyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Key>> findAll() {
//        List<Key> keys = service.findAll();
        return ResponseEntity.ok().body(keys);
    }


}