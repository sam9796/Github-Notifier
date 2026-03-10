package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.DTO.GithubRepoDTO;


@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;
    
    @Value("${github.client.id}")
    private String clientId;
    
    private final RestTemplate  restTemplate;

    public TelegramService(RestTemplate restTemplate) {
    	this.restTemplate=restTemplate;
    }

    public void sendGithubButton(Long chatId) {

        String telegramUrl="https://api.telegram.org/bot" + botToken + "/sendMessage";

        String githubOauthUrl =
            "https://github.com/login/oauth/authorize" +
            "?client_id="+ clientId +
            "&scope=repo&state="+chatId;

        Map<String,Object> button = Map.of(
                "text","Connect GitHub",
                "url", githubOauthUrl
        );

        Map<String,Object> keyboard = Map.of(
                "inline_keyboard",
                List.of(List.of(button))
        );

        Map<String,Object> body = Map.of(
                "chat_id", chatId,
                "text", "Connect your GitHub account",
                "reply_markup", keyboard
        );

        restTemplate.postForObject(telegramUrl, body, String.class);
    }


    public void sendMessage(Long chatId, String text) {

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, Object> requestBody = Map.of(
                "chat_id", chatId,
                "text", text
        );

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, requestBody, String.class);
    }
    
    public void sendRepoPage(
            Long chatId,
            List<GithubRepoDTO> repos,
            int page) {
    	String telegramUrl="https://api.telegram.org/bot" + botToken + "/sendMessage";
        int pageSize = 10;

        int start = page * pageSize;
        int end = Math.min(start + pageSize, repos.size());

        List<GithubRepoDTO> pageRepos = repos.subList(start, end);

        List<List<Map<String,Object>>> keyboard = new ArrayList<>();

        for(GithubRepoDTO repo : pageRepos){

            Map<String,Object> button = Map.of(
                    "text", repo.getName(),
                    "callback_data", "repo|" + repo.getId()
            );

            keyboard.add(List.of(button));
        }

        List<Map<String,Object>> navRow = new ArrayList<>();

        if(page > 0){
            navRow.add(Map.of(
                    "text","⬅ Previous",
                    "callback_data","page|" + (page - 1)
            ));
        }

        if(end < repos.size()){
            navRow.add(Map.of(
                    "text","Next ➡",
                    "callback_data","page|" + (page + 1)
            ));
        }

        if(!navRow.isEmpty()){
            keyboard.add(navRow);
        }

        Map<String,Object> body = Map.of(
                "chat_id", chatId,
                "text","Select repository",
                "reply_markup", Map.of("inline_keyboard", keyboard)
        );

        restTemplate.postForObject(
                telegramUrl,
                body,
                String.class
        );
    }
}
