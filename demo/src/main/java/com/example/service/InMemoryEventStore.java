package com.example.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class InMemoryEventStore implements EventStore{
		private final Map<String,Event>store=new ConcurrentHashMap<>();
		
		@Override 
		public void save(Event event) {
			store.put(event.getId(),event);
		}
		
		@Override
		public Optional<Event>findById(String eventId){
			return Optional.ofNullable(store.get(eventId));
		}
		
		@Override 
		public void enqueueReady(String eventId) {
			
		}
		
		@Override
		public void enqueueDelayed(String eventId,long executeAt) {
			
		}
}