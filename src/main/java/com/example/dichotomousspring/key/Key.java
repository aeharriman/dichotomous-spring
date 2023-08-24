package com.example.dichotomousspring.key;

import jakarta.persistence.*;

@Entity
public class Key {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(length = 8000)
    private String key;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
