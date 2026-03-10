package com.example.DTO;

import java.util.Map;

import com.sun.istack.NotNull;

import jakarta.validation.constraints.NotBlank;
import tools.jackson.databind.JsonNode;

public class DelayedEvents{
	@NotBlank
	private String type;
	
	@NotNull
	private JsonNode payload;
	
	@NotNull
	private Long executeAt;
	
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

	public Long getExecuteAt() {
		return executeAt;
	}

	public void setExecuteAt(Long executeAt) {
		this.executeAt = executeAt;
	}

	public String getDedupKey() {
		return dedupKey;
	}

	public void setDedupKey(String dedupKey) {
		this.dedupKey = dedupKey;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	private String dedupKey;
	
	private Map<String,String>metadata;
}