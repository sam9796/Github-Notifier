package com.example.service;

import java.util.Optional;

public interface EventStore{
	void save(Event event);
	Optional<Event>findById(String eventId);
	void enqueueReady(String eventId);
	void enqueueDelayed(String eventId,long executeAt);
}