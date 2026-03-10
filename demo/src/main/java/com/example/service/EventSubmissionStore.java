package com.example.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.DTO.DelayedEvents;
import com.example.DTO.InstantEvents;

@Service
public class EventSubmissionStore{
	private final EventStore eventStore;
	
	public EventSubmissionStore(EventStore eventStore) {
		this.eventStore=eventStore;
	}
	
	public String createInstantEvent(InstantEvents request) {
			String id=generateEventId();
			long now=System.currentTimeMillis();
			Event event=new Event();
			event.setId(id);
			event.setType(request.getType());
			event.setPayload(request.getPayload());
			event.setState(EventState.READY);
			event.setAttempts(0);
			event.setCreatedAt(now);
			event.setUpdatedAt(now);
			eventStore.save(event);
			eventStore.enqueueReady(id);
			return id;
	}
	
	public String createDelayedEvent(DelayedEvents request) {
		if(request.getExecuteAt()<=System.currentTimeMillis()) {
			throw new IllegalArgumentException("executeAt must be in the future");
			
		}
		String id =generateEventId();
		long now=System.currentTimeMillis();
		Event event=new Event();
		event.setId(id);
		event.setType(request.getType());
		event.setPayload(request.getPayload());
		event.setState(EventState.DELAYED);
		event.setAttempts(0);
		event.setCreatedAt(now);
		event.setUpdatedAt(now);
		
		eventStore.save(event);
		eventStore.enqueueDelayed(id,request.getExecuteAt());
		return id;
	}
	
	private String generateEventId() {
		return "evt-"+UUID.randomUUID();
	}
}