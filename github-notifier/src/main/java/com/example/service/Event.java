package com.example.service;

import tools.jackson.databind.JsonNode;

public class Event{
	private String id;
	private String type;
	private JsonNode payload;
	private EventState state;
	private int attempts;
	private long createdAt;
	private long updatedAt;
	private String lastError;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public JsonNode getPayload() {
		return payload;
	}
	public void setPayload(JsonNode payload) {
		this.payload = payload;
	}
	public EventState getState() {
		return state;
	}
	public void setState(EventState state) {
		this.state = state;
	}
	public int getAttempts() {
		return attempts;
	}
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	public long getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getLastError() {
		return lastError;
	}
	public void setLastError(String lastError) {
		this.lastError = lastError;
	}
	
}