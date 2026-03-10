package com.example.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.DTO.GithubRepoDTO;

@Service
public class GithubService{
	
	private final RestTemplate restTemplate;

    public GithubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
	
	   public Map getGithubUser(String token) {

	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(token);

	        HttpEntity<Void> entity = new HttpEntity<>(headers);

	        ResponseEntity<Map> response =
	                restTemplate.exchange(
	                        "https://api.github.com/user",
	                        HttpMethod.GET,
	                        entity,
	                        Map.class
	                );

	        return response.getBody();
	    }
	   
	   public List<GithubRepoDTO> getUserRepos(String token) {

		    HttpHeaders headers = new HttpHeaders();
		    headers.setBearerAuth(token);

		    HttpEntity<Void> entity = new HttpEntity<>(headers);

		    ResponseEntity<List> response =
		            restTemplate.exchange(
		                    "https://api.github.com/user/repos",
		                    HttpMethod.GET,
		                    entity,
		                    List.class
		            );

		    List<Map<String,Object>> repos = response.getBody();

		    return repos.stream()
		            .map(repo -> new GithubRepoDTO(
		                    (Integer) repo.get("id"),
		                    (String) repo.get("name"),
		                    ((Map) repo.get("owner")).get("login").toString()
		            )).sorted(Comparator.comparing(GithubRepoDTO::getName))
		            .toList();
		}
	   
	   public Long createWebhook(
	            String token,
	            String owner,
	            String repo){

	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(token);
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        Map<String,Object> body = Map.of(
	                "name","web",
	                "active",true,
	                "events", List.of("push","pull_request", "issues",
	                        "issue_comment",
	                        "release"),
	                "config", Map.of(
	                        "url","https://tensely-immensurable-quinn.ngrok-free.dev/github/webhook",
	                        "content_type","json"
	                )
	        );

	        HttpEntity<Map<String,Object>> entity =
	                new HttpEntity<>(body, headers);

	        ResponseEntity<Map> response =
	                restTemplate.postForEntity(
	                        "https://api.github.com/repos/" + owner + "/" + repo + "/hooks",
	                        entity,
	                        Map.class
	                );

	        Map result = response.getBody();

	        return ((Number) result.get("id")).longValue();
	    }
}