package com.api.freeloot.controller;

import com.api.freeloot.dto.GameView;
import com.api.freeloot.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;
    @GetMapping("all")
    public ResponseEntity<List<GameView>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getAll());
    }

}
