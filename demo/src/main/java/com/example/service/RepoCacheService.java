package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.DTO.GithubRepoDTO;
import com.github.benmanes.caffeine.cache.Cache;

@Service
public class RepoCacheService {

    private final Cache<Long, List<GithubRepoDTO>> repoCache;

    public RepoCacheService(Cache<Long, List<GithubRepoDTO>> repoCache) {
        this.repoCache = repoCache;
    }

    public void putRepos(Long chatId, List<GithubRepoDTO> repos) {
        repoCache.put(chatId, repos);
    }

    public List<GithubRepoDTO> getRepos(Long chatId) {
        return repoCache.getIfPresent(chatId);
    }

    public void invalidate(Long chatId) {
        repoCache.invalidate(chatId);
    }

}