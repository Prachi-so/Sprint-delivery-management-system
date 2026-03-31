package com.lpu.auth_service.servicetest;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.auth_service.dto.DeliveryRequestDto;
import com.lpu.auth_service.dto.TrackingEventDto;
import com.lpu.auth_service.entity.Address;
import com.lpu.auth_service.entity.Delivery;
import com.lpu.auth_service.entity.DeliveryStatus;
import com.lpu.auth_service.repository.DeliveryRepository;
import com.lpu.auth_service.service.DeliveryService;
import com.lpu.auth_service.service.EventProducer;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {


    @Mock
    private DeliveryRepository repo;

    @Mock
    private EventProducer producer;

    @InjectMocks
    private DeliveryService deliveryService;

   
    @Test
    void testCreateDelivery() {

       
        DeliveryRequestDto req = new DeliveryRequestDto();
        req.setWeight(5.0);
        req.setType("BOX");

     
        Address sender = new Address();
        sender.setCity("Jaipur");

        Address receiver = new Address();
        receiver.setCity("Delhi");

        req.setSenderAddress(sender);
        req.setReceiverAddress(receiver);

        String userId = "rohit123@lpu.com";

        Delivery savedDelivery = new Delivery();
        savedDelivery.setId(1);
        savedDelivery.setUserId(userId);

        Mockito.when(repo.save(Mockito.any(Delivery.class)))
                .thenReturn(savedDelivery);

      
        Delivery result = deliveryService.createDelivery(req, userId);

       
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userId, result.getUserId());

       
        ArgumentCaptor<Delivery> captor = ArgumentCaptor.forClass(Delivery.class);
        Mockito.verify(repo).save(captor.capture());

        Delivery saved = captor.getValue();

        Assertions.assertEquals(50.0, saved.getPrice()); // 5 * 10
        Assertions.assertEquals(DeliveryStatus.BOOKED, saved.getStatus());
        Assertions.assertEquals("Jaipur", saved.getSenderAddress().getCity());
        Assertions.assertEquals("Delhi", saved.getReceiverAddress().getCity());
    }

  
    @Test
    void testGetUserDeliveries() {

        String userId = "rohit123@lpu.com";

        List<Delivery> deliveries = List.of(
                new Delivery(),
                new Delivery()
        );

        Mockito.when(repo.findByUserId(userId))
                .thenReturn(deliveries);

        List<Delivery> result =
                deliveryService.getUserDeliveries(userId);

        Assertions.assertEquals(2, result.size());

        Mockito.verify(repo).findByUserId(userId);
    }


    @Test
    void testUpdateStatus() {

        long id = 1;

        Delivery delivery = new Delivery();
        delivery.setId(id);
        delivery.setUserId("rohit123@lpu.com");
        delivery.setStatus(DeliveryStatus.BOOKED);

        Mockito.when(repo.findById(id))
                .thenReturn(Optional.of(delivery));

        Mockito.when(repo.save(Mockito.any(Delivery.class)))
                .thenReturn(delivery);

       
        Delivery result = deliveryService.updateStatus(
                id,
                DeliveryStatus.PICKED_UP
                
        );

       
        Assertions.assertNotNull(result);
        Assertions.assertEquals(DeliveryStatus.PICKED_UP, result.getStatus());

        Mockito.verify(repo).findById(id);
        Mockito.verify(repo).save(Mockito.any(Delivery.class));

        //Verify event sent
        Mockito.verify(producer).sendEvent(Mockito.any(TrackingEventDto.class));
    }

 
    @Test
    void testUpdateStatus_NotFound() {

        long id = 1;

        Mockito.when(repo.findById(id))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            deliveryService.updateStatus(id, DeliveryStatus.PICKED_UP);
        });
    }


    @Test
    void testGetAllDeliveries() {

        List<Delivery> list = List.of(
                new Delivery(),
                new Delivery(),
                new Delivery()
        );

        Mockito.when(repo.findAll()).thenReturn(list);

        List<Delivery> result = deliveryService.getAllDeliveries();

        Assertions.assertEquals(3, result.size());

        Mockito.verify(repo).findAll();
    }
}
