package com.example.dichotomousspring.key;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
//    List<Key> findAllByUserId(String userId);
}
