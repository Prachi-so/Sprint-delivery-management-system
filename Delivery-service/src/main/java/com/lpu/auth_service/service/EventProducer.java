package com.lpu.auth_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.auth_service.config.RabbitConfig;
import com.lpu.auth_service.dto.TrackingEventDto;

@Service
public class EventProducer {

	@Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEvent(TrackingEventDto event) {

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }
}
