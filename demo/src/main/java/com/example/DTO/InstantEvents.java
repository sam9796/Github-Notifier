package com.example.DTO;

import java.util.Map;

import com.sun.istack.NotNull;

import jakarta.validation.constraints.NotBlank;
import tools.jackson.databind.JsonNode;

public class InstantEvents{
	@NotBlank
	private String type;
	
	@NotNull
	private JsonNode payload;
	
	private String dedupKey;
	
	private Map<String,String>metadata;

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
	
}