package com.example.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.DTO.GithubEvent;

@Service
public class KafkaProducerService {

	  private final KafkaTemplate<String, GithubEvent> kafkaTemplate;

	    public KafkaProducerService(KafkaTemplate<String, GithubEvent> kafkaTemplate) {
	        this.kafkaTemplate = kafkaTemplate;
	    }

	    public void publishEvent(GithubEvent event) {
	    		System.out.println("pushed events");
	        kafkaTemplate.send(
	                "github-events",
	                event.getRepoId().toString(),
	                event
	        );

	    }
}