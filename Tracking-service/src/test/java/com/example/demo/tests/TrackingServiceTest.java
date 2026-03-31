package com.example.demo.tests;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.DeliveryProof;
import com.example.demo.entity.Document;
import com.example.demo.entity.TrackingEvent;
import com.example.demo.repository.DeliveryProofRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.TrackingRepository;
import com.example.demo.service.TrackingService;

@ExtendWith(MockitoExtension.class)
public class TrackingServiceTest {

	@Mock
    private TrackingRepository trackingRepo;

    @Mock
    private DocumentRepository documentRepo;

    @Mock
    private DeliveryProofRepository proofRepo;

    @InjectMocks
    private TrackingService trackingService;

   
    @Test
    void testCreateEvent() {

        TrackingEvent event = new TrackingEvent();
        event.setDeliveryId(1L);
        event.setStatus("SHIPPED");

        Mockito.when(trackingRepo.save(Mockito.any()))
                .thenReturn(event);

        TrackingEvent result = trackingService.createEvent(event);

        Assertions.assertNotNull(result);

        // capturing saved object
        ArgumentCaptor<TrackingEvent> captor =
                ArgumentCaptor.forClass(TrackingEvent.class);

        Mockito.verify(trackingRepo).save(captor.capture());

        TrackingEvent saved = captor.getValue();

        Assertions.assertEquals(1L, saved.getDeliveryId());
        Assertions.assertNotNull(saved.getTimestamp()); // 🔥 important
    }

  
    @Test
    void testGetEvents() {

        Long deliveryId = 1L;

        List<TrackingEvent> list = List.of(
                new TrackingEvent(),
                new TrackingEvent()
        );

        Mockito.when(trackingRepo.findByDeliveryId(deliveryId))
                .thenReturn(list);

        List<TrackingEvent> result =
                trackingService.getEvents(deliveryId);

        Assertions.assertEquals(2, result.size());

        Mockito.verify(trackingRepo).findByDeliveryId(deliveryId);
    }


    @Test
    void testSaveDocument() {

        Document doc = new Document();
        doc.setDeliveryId(1L);

        Mockito.when(documentRepo.save(Mockito.any()))
                .thenReturn(doc);

        Document result = trackingService.saveDocument(doc);

        Assertions.assertNotNull(result);

        ArgumentCaptor<Document> captor =
                ArgumentCaptor.forClass(Document.class);

        Mockito.verify(documentRepo).save(captor.capture());

        Document saved = captor.getValue();

        Assertions.assertEquals(1L, saved.getDeliveryId());
        Assertions.assertNotNull(saved.getUploadedAt()); // timestamp
    }

    // -----------------------------
    // ✅ 4. GET DOCUMENTS
    // -----------------------------
    @Test
    void testGetDocuments() {

        Long deliveryId = 1L;

        List<Document> docs = List.of(
                new Document(),
                new Document()
        );

        Mockito.when(documentRepo.findByDeliveryId(deliveryId))
                .thenReturn(docs);

        List<Document> result =
                trackingService.getDocuments(deliveryId);

        Assertions.assertEquals(2, result.size());

        Mockito.verify(documentRepo).findByDeliveryId(deliveryId);
    }

    // -----------------------------
    // ✅ 5. SAVE PROOF
    // -----------------------------
    @Test
    void testSaveProof() {

        DeliveryProof proof = new DeliveryProof();
        proof.setDeliveryId(1L);

        Mockito.when(proofRepo.save(Mockito.any()))
                .thenReturn(proof);

        DeliveryProof result = trackingService.saveProof(proof);

        Assertions.assertNotNull(result);

        ArgumentCaptor<DeliveryProof> captor =
                ArgumentCaptor.forClass(DeliveryProof.class);

        Mockito.verify(proofRepo).save(captor.capture());

        DeliveryProof saved = captor.getValue();

        Assertions.assertEquals(1L, saved.getDeliveryId());
        Assertions.assertNotNull(saved.getTimestamp());
    }

    // -----------------------------
    // ✅ 6. GET PROOFS
    // -----------------------------
    @Test
    void testGetProofs() {

        Long deliveryId = 1L;

        List<DeliveryProof> proofs = List.of(
                new DeliveryProof(),
                new DeliveryProof()
        );

        Mockito.when(proofRepo.findByDeliveryId(deliveryId))
                .thenReturn(proofs);

        List<DeliveryProof> result =
                trackingService.getProofs(deliveryId);

        Assertions.assertEquals(2, result.size());

        Mockito.verify(proofRepo).findByDeliveryId(deliveryId);
    }
}
