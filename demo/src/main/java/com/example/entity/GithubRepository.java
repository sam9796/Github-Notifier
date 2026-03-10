package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "repositories")
public class GithubRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getGithubRepoId() {
		return githubRepoId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setGithubRepoId(Long githubRepoId) {
		this.githubRepoId = githubRepoId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getWebhookId() {
		return webhookId;
	}

	public void setWebhookId(Long webhookId) {
		this.webhookId = webhookId;
	}

	@Column(name="github_repo_id", unique = true)
    private Long githubRepoId;

    private String owner;

    private String name;

    @Column(name="webhook_id")
    private Long webhookId;

}