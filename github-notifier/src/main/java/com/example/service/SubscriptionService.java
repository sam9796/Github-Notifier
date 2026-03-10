package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.GithubRepository;
import com.example.entity.Subscription;
import com.example.repository.RepositoryRepository;
import com.example.repository.SubscriptionRepository;

import jakarta.transaction.Transactional;



@Service
public class SubscriptionService {

    private final RepositoryRepository repositoryRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final GithubService webhookService;
    
    public SubscriptionService(RepositoryRepository repositoryRepository,SubscriptionRepository subscriptionRepository,GithubService webhookService) {
    	this.repositoryRepository=repositoryRepository;
    	this.subscriptionRepository=subscriptionRepository;
    	this.webhookService=webhookService;
    }

    @Transactional
    public void subscribe(
            Long chatId,
            Long githubRepoId,
            String owner,
            String repo,
            String token){

        GithubRepository repository =
                repositoryRepository
                .findByGithubRepoId(githubRepoId)
                .orElseGet(()->{
                        Long webhookId = webhookService.createWebhook(token, owner, repo);

                        GithubRepository r = new GithubRepository();
                        r.setGithubRepoId(githubRepoId);
                        r.setOwner(owner);
                        r.setName(repo);
                        r.setWebhookId(webhookId);

                        return repositoryRepository.save(r);
                });

        if(!subscriptionRepository
                .existsByChatIdAndRepoId(chatId, repository.getId())){

            Subscription sub = new Subscription();
            sub.setChatId(chatId);
            sub.setRepoId(repository.getId());

            subscriptionRepository.save(sub);
        }
    }
}