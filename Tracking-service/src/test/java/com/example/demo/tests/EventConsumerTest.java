package com.example.demo.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.example.demo.entity.TrackingEvent;
import com.example.demo.repository.TrackingRepository;
import com.example.demo.service.EventConsumer;

@ExtendWith(MockitoExtension.class)
public class EventConsumerTest {

	@Mock
    private TrackingRepository repo;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private EventConsumer consumer;

 
    @Test
    void testReceiveEvent() {

        // Given
        TrackingEvent event = new TrackingEvent();
        event.setDeliveryId(1L);
        event.setStatus("DELIVERED");
        event.setLocation("Delhi");

        Mockito.when(cacheManager.getCache("tracking"))
                .thenReturn(cache);

        // When
        consumer.receiveEvent(event);

        // Then

        //Capture saved event
        ArgumentCaptor<TrackingEvent> captor =
                ArgumentCaptor.forClass(TrackingEvent.class);

        Mockito.verify(repo).save(captor.capture());

        TrackingEvent saved = captor.getValue();

        //Verify fields
        Assertions.assertEquals(1L, saved.getDeliveryId());
        Assertions.assertEquals("DELIVERED", saved.getStatus());
        Assertions.assertEquals("Delhi", saved.getLocation());

        // Verify timestamp set
        Assertions.assertNotNull(saved.getTimestamp());

        // Verify cache eviction
        Mockito.verify(cache).evict(1L);
    }
}
