package com.api.muinda_kubika.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class defaultController {

    @GetMapping
    public ResponseEntity<Object> get(){
        try{
            System.out.println("Api em execucao!!!");
            return ResponseEntity.status(200).body("Api em execução!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
