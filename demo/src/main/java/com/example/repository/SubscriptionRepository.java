package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Subscription;

public interface SubscriptionRepository
        extends JpaRepository<Subscription,Long>{

    boolean existsByChatIdAndRepoId(Long chatId, Long repoId);

    @Query("""
    		SELECT s.chatId
    		FROM Subscription s
    		JOIN GithubRepository r
    		ON s.repoId = r.id
    		WHERE r.githubRepoId = :repoId
    		""")
    		List<Long> findChatIdsByRepoGithubId(@Param("repoId") Long repoId);

}