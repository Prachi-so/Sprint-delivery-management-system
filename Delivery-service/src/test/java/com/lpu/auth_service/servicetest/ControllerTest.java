package com.lpu.auth_service.servicetest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.auth_service.controller.DeliveryController;
import com.lpu.auth_service.dto.DeliveryRequestDto;
import com.lpu.auth_service.entity.Address;
import com.lpu.auth_service.entity.Delivery;
import com.lpu.auth_service.entity.DeliveryStatus;
import com.lpu.auth_service.service.DeliveryService;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

	@Mock
    private DeliveryService service;

    @InjectMocks
    private DeliveryController controller;

    // ✅ CREATE DELIVERY (FULL DTO TEST)
    @Test
    void testCreateDelivery() {

        DeliveryRequestDto req = new DeliveryRequestDto();
        req.setWeight(5.0);
        req.setType("BOX");

        // Sender
        Address sender = new Address();
        sender.setName("Rohit");
        sender.setPhone("9999999999");
        sender.setCity("Jaipur");
        sender.setState("Rajasthan");
        sender.setPincode("302001");
        sender.setFullAddress("Vaishali Nagar");

        // Receiver
        Address receiver = new Address();
        receiver.setName("Amit");
        receiver.setPhone("8888888888");
        receiver.setCity("Delhi");
        receiver.setState("Delhi");
        receiver.setPincode("110001");
        receiver.setFullAddress("Connaught Place");

        req.setSenderAddress(sender);
        req.setReceiverAddress(receiver);

        String userId = "rohit123@lpu.com";

        Delivery delivery = new Delivery();
        delivery.setId(1);
        delivery.setUserId(userId);
        delivery.setSenderAddress(sender);
        delivery.setReceiverAddress(receiver);
        delivery.setStatus(DeliveryStatus.BOOKED);

        Mockito.when(service.createDelivery(req, userId))
                .thenReturn(delivery);

        Delivery result = controller.create(req, userId);

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userId, result.getUserId());
        Assertions.assertEquals("Jaipur", result.getSenderAddress().getCity());
        Assertions.assertEquals("Delhi", result.getReceiverAddress().getCity());

        Mockito.verify(service).createDelivery(req, userId);
    }
    

    // ✅ GET MY DELIVERIES
    @Test
    void testGetMyDeliveries() {

        String userId = "rohit123@lpu.com";

        List<Delivery> list = List.of(
                new Delivery(),
                new Delivery()
        );

        Mockito.when(service.getUserDeliveries(userId))
                .thenReturn(list);

        List<Delivery> result = controller.getMy(userId);

        Assertions.assertEquals(2, result.size());

        Mockito.verify(service).getUserDeliveries(userId);
    }

    // ✅ UPDATE STATUS
    @Test
    void testUpdateStatus() {

        long id = 1;

        Delivery delivery = new Delivery();
        delivery.setId(id);
        delivery.setStatus(DeliveryStatus.IN_TRANSIT);

        Mockito.when(service.updateStatus(id, DeliveryStatus.IN_TRANSIT))
                .thenReturn(delivery);

        Delivery result = controller.updateStatus(
                id,
                DeliveryStatus.IN_TRANSIT
                
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(DeliveryStatus.IN_TRANSIT, result.getStatus());

        Mockito.verify(service).updateStatus(id, DeliveryStatus.IN_TRANSIT);
    }

    // ✅ GET ALL DELIVERIES (ADMIN)
    @Test
    void testGetAllDeliveries() {

        List<Delivery> list = List.of(
                new Delivery(),
                new Delivery(),
                new Delivery()
        );

        Mockito.when(service.getAllDeliveries())
                .thenReturn(list);

        List<Delivery> result = controller.getAllForAdmin();

        Assertions.assertEquals(3, result.size());

        Mockito.verify(service).getAllDeliveries();
    }
}
