package com.example.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long telegramChatId;

    private String githubUsername;

    private Long githubUserId;

    @Column(columnDefinition = "TEXT")
    private String githubAccessToken;

    public Long getTelegramChatId() {
		return telegramChatId;
	}

	public void setTelegramChatId(Long telegramChatId) {
		this.telegramChatId = telegramChatId;
	}

	public String getGithubUsername() {
		return githubUsername;
	}

	public void setGithubUsername(String githubUsername) {
		this.githubUsername = githubUsername;
	}

	public Long getGithubUserId() {
		return githubUserId;
	}

	public void setGithubUserId(Long githubUserId) {
		this.githubUserId = githubUserId;
	}

	public String getGithubAccessToken() {
		return githubAccessToken;
	}

	public void setGithubAccessToken(String githubAccessToken) {
		this.githubAccessToken = githubAccessToken;
	}

	private LocalDateTime createdAt = LocalDateTime.now();
}
