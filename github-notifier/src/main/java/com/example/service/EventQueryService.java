package com.example.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
public class EventQueryService{
	private final EventStore eventStore;
	
	public EventQueryService(EventStore eventStore) {
		this.eventStore=eventStore;
	}
	
	public Event getEvent(String eventId) throws Exception {
		return eventStore.findById(eventId).orElseThrow(()->new NoSuchElementException("Event not found: "+eventId));
	}
	
}