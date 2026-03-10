package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.GithubRepoDTO;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.GithubService;
import com.example.service.RepoCacheService;
import com.example.service.SubscriptionService;
import com.example.service.TelegramService;

@RestController
@RequestMapping("/webhook")
public class GithubNotifierBotController{
	private final TelegramService telegramService;
	private final GithubService githubService;
	private final UserRepository userRepository;
	private final RepoCacheService repoCacheService;
	private final SubscriptionService subscriptionService;

    public GithubNotifierBotController(TelegramService telegramService,GithubService githubService,UserRepository userRepository,RepoCacheService repoCacheService,SubscriptionService subscriptionService) {
        this.telegramService = telegramService;
        this.githubService= githubService;
        this.userRepository=userRepository;
        this.repoCacheService=repoCacheService;
        this.subscriptionService=subscriptionService;
    }

    @PostMapping
    public void handleUpdate(@RequestBody Map<String,Object> update) {
    	String text=null;
    	Long chatId=null;
    	 if(update.containsKey("message")) {

    	        Map message = (Map) update.get("message");

    	         text = (String) message.get("text");

    	        Map chat = (Map) message.get("chat");
    	         chatId = ((Number) chat.get("id")).longValue();

    	    }

    	    if(update.containsKey("callback_query")) {

    	        Map callback = (Map) update.get("callback_query");

    	        text = (String) callback.get("data");

    	        Map message = (Map) callback.get("message");

    	        Map chat = (Map) message.get("chat");

    	         chatId = ((Number) chat.get("id")).longValue();}
        if(chatId == null || text==null) return;
        	
        if("start".equalsIgnoreCase(text) || "hi".equalsIgnoreCase(text) || "hello".equalsIgnoreCase(text)) {
            telegramService.sendGithubButton(chatId);
        }
        if(text.toLowerCase().contains("list")) {
        	 User user = userRepository
                     .findByTelegramChatId(chatId)
                     .orElse(null);
        	 if(user==null) {
        		 telegramService.sendMessage(chatId,"Please authenticate your github account");
        	 }
        	 List<GithubRepoDTO> repos =
        		        repoCacheService.getRepos(chatId);

        		if(repos == null){

        		    repos = githubService.getUserRepos(user.getGithubAccessToken());

        		    repoCacheService.putRepos(chatId, repos);
        		}
        	
        	 this.telegramService.sendRepoPage(chatId, repos, 0);
        }
        if(text.startsWith("repo|")) {
        	 Long githubRepoId = Long.parseLong(text.split("\\|")[1]);
        	 
        	 List<GithubRepoDTO> repos =
        		        repoCacheService.getRepos(chatId);

        		if(repos == null){

        		    telegramService.sendMessage(
        		        chatId,
        		        "Repository list expired. Please fetch it again using /repos."
        		    );

        		    return;
        		}
        	
        		GithubRepoDTO repo=repos.stream().filter(r->(r.getId().longValue()==(githubRepoId))).findFirst().orElse(null);
        		
        		if(repo==null) {
        			   telegramService.sendMessage(
        				        chatId,
        				        "Repository not found. Please fetch repos again."
        				    );

        				    return;
        		}

        		User user = userRepository
                        .findByTelegramChatId(chatId)
                        .orElse(null);
           	 if(user==null) {
           		 telegramService.sendMessage(chatId,"Please authenticate your github account");
           	 }
           	 
           	 subscriptionService.subscribe(chatId, githubRepoId, repo.getOwner(), repo.getName(), user.getGithubAccessToken());

        	    telegramService.sendMessage(chatId, "Subscribed successfully!");
        }
        if(text.startsWith("page|")) {
        	String[] pageList=text.split("\\|");
        	Integer pageNo=Integer.parseInt(pageList[1]);
        	 List<GithubRepoDTO> repos =
     		        repoCacheService.getRepos(chatId);

     		if(repos == null){
     			 User user = userRepository
                         .findByTelegramChatId(chatId)
                         .orElse(null);
            	 if(user==null) {
            		 telegramService.sendMessage(chatId,"Please authenticate your github account");
            	 }
     		    repos = githubService.getUserRepos(user.getGithubAccessToken());

     		    repoCacheService.putRepos(chatId, repos);
     		}
        	this.telegramService.sendRepoPage(chatId,repos,pageNo);
        }
    }
}