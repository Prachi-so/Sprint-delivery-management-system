package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.example.demo.config.RabbitConfig;
import com.example.demo.entity.TrackingEvent;
import com.example.demo.repository.TrackingRepository;

@Service
public class EventConsumer {

	@Autowired
    private TrackingRepository repo;
	
//	@Autowired
//	private CacheManager cacheManager;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receiveEvent(TrackingEvent event) {

       
        event.setTimestamp(LocalDateTime.now());
        
        TrackingEvent newEvent = new TrackingEvent();
        newEvent.setDeliveryId(event.getDeliveryId());
        newEvent.setUserId(event.getUserId());
        newEvent.setStatus(event.getStatus());
        newEvent.setLocation(event.getLocation());
        newEvent.setDescription(event.getDescription());
        newEvent.setTimestamp(LocalDateTime.now());
       

        repo.save(newEvent);

     
        
//        cacheManager.getCache("tracking")
//        .evict(event.getDeliveryId());
    }
}
