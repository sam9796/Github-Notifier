package com.example.service;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.DTO.GithubEvent;
import com.example.repository.SubscriptionRepository;

@Service
public class KafkaConsumerService {

		 	
		 private final SubscriptionRepository subscriptionRepository;
		    private final TelegramService telegramService;
		   
		  public KafkaConsumerService(SubscriptionRepository subscriptionRepository,TelegramService telegramService) {
			  	this.subscriptionRepository=subscriptionRepository;
			  	this.telegramService=telegramService;
		  }

		    @KafkaListener(
		            topics = "github-events",
		            groupId = "github-notifier",
		            concurrency= "2"
		    )
		    public void consume(GithubEvent event){
		    	System.out.println("consume events");
		        List<Long> chatIds =
		                subscriptionRepository
		                .findChatIdsByRepoGithubId(event.getRepoId());

		        for(Long chatId : chatIds){

		            String message = formatMessage(event);

		            telegramService.sendMessage(chatId, message);
		        }
		    }
		   
		    private String formatMessage(GithubEvent event){

		        return """
		    Repository: %s
		    Event: %s
		    Branch: %s
		    Author: %s
		    Message: %s
		    """
		    .formatted(
		            event.getRepoName(),
		            event.getEventType(),
		            event.getBranch(),
		            event.getAuthor(),
		            event.getMessage()
		    );
		    }
	        
}