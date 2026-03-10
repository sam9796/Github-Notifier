package com.example.DTO;

public class GithubEvent {

    private Long repoId;
    private String repoName;
    private String eventType;
    private String branch;
    private String author;
    private String message;

    public GithubEvent() {}

    public GithubEvent(Long repoId, String repoName,
                       String eventType, String branch,
                       String author, String message) {
        this.repoId = repoId;
        this.repoName = repoName;
        this.eventType = eventType;
        this.branch = branch;
        this.author = author;
        this.message = message;
    }

	public Long getRepoId() {
		return repoId;
	}

	public void setRepoId(Long repoId) {
		this.repoId = repoId;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
}