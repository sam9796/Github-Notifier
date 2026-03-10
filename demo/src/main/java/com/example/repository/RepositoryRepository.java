package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.GithubRepository;

public interface RepositoryRepository
        extends JpaRepository<GithubRepository,Long>{

    Optional<GithubRepository> findByGithubRepoId(Long githubRepoId);

}