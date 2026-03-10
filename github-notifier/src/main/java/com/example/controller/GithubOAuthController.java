package com.example.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.DTO.GithubEvent;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.GithubService;
import com.example.service.KafkaProducerService;
import com.example.service.TelegramService;

@RestController
@RequestMapping("/github")
public class GithubOAuthController {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    private final TelegramService telegramService;
    private final UserRepository userRepository;
    private final GithubService githubService;
    private final KafkaProducerService kafkaProducerService;
    
    public GithubOAuthController(TelegramService telegramService, UserRepository userRepository, GithubService githubService,KafkaProducerService kafkaProducerService) {
        this.telegramService = telegramService;
		this.userRepository = userRepository;
		this.githubService = githubService;
		this.kafkaProducerService=kafkaProducerService;
    }

    @GetMapping("/callback")
    public String callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String,String> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code
        );

        HttpEntity<Map<String,String>> request =
                new HttpEntity<Map<String,String>>(body,headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        String accessToken =
                (String) response.getBody().get("access_token");

        Long chatId = Long.parseLong(state);

        telegramService.sendMessage( chatId,
        		"GitHub connected successfully!"
        );
        
        if (accessToken == null) {

            telegramService.sendMessage(
                    chatId,
                    "❌ GitHub authorization failed"
            );

            return "Authorization failed";
        }
        Map githubUser = githubService.getGithubUser(accessToken);

        saveGithubUser(chatId, accessToken, githubUser);

        return "GitHub account connected. Return to Telegram.";
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(
            @RequestBody Map<String,Object> payload,
            @RequestHeader("X-GitHub-Event") String eventType){

        GithubEvent event = parseEvent(payload, eventType);

        kafkaProducerService.publishEvent(event);

        return ResponseEntity.ok().build();
    }
    
    private GithubEvent parseEvent(Map<String,Object> payload, String eventType){

        Map repo = (Map) payload.get("repository");

        Long repoId = ((Number)repo.get("id")).longValue();

        String repoName = (String) repo.get("name");

        String branch = null;
        String author = null;
        String message = null;

        if("push".equals(eventType)){

            String ref = (String) payload.get("ref");
            branch = ref.replace("refs/heads/","");

            List commits = (List) payload.get("commits");

            if(!commits.isEmpty()){

                Map commit = (Map) commits.get(0);

                message = (String) commit.get("message");

                Map authorMap = (Map) commit.get("author");

                author = (String) authorMap.get("name");
            }
        }
        
        if("issues".equals(eventType)) {

            Map issue = (Map) payload.get("issue");

            String title = (String) issue.get("title");

            message=("Issue opened: " + title);
        }

        return new GithubEvent(
                repoId,
                repoName,
                eventType,
                branch,
                author,
                message
        );
    }
    
    public void saveGithubUser(
            Long chatId,
            String token,
            Map githubUser) {

        String username = (String) githubUser.get("login");
        Integer githubId = (Integer) githubUser.get("id");

        User user = userRepository
                .findByTelegramChatId(chatId)
                .orElse(new User());

        user.setTelegramChatId(chatId);
        user.setGithubAccessToken(token);
        user.setGithubUsername(username);
        user.setGithubUserId(githubId.longValue());

        userRepository.save(user);
    }

}




