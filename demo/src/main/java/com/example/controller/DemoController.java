package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.DelayedEvents;
import com.example.DTO.InstantEvents;
import com.example.service.EventQueryService;
import com.example.service.EventSubmissionStore;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
public class DemoController{
	private final EventSubmissionStore submissionService;
	private final EventQueryService queryService;
	
	public DemoController(EventSubmissionStore submissionService,EventQueryService queryService) {
		this.submissionService=submissionService;
		this.queryService=queryService;
	}
	
    @GetMapping("/{id}")
    public String ping() {
        return "pong";
    }
    
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody InstantEvents request) {
       return ResponseEntity.ok("Instant event meta data");
    }

    @PostMapping("/delayed")
    public ResponseEntity<String> create(@Valid @RequestBody DelayedEvents request) {
    	return ResponseEntity.ok("Delayed Event is created");
    }
}