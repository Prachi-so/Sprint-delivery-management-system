package com.example.demo.tests;

import java.io.IOException;
import java.time.LocalDateTime;
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
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.controller.TrackingController;
import com.example.demo.entity.DeliveryProof;
import com.example.demo.entity.Document;
import com.example.demo.entity.TrackingEvent;
import com.example.demo.repository.DeliveryProofRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.service.FileService;
import com.example.demo.service.TrackingService;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

	    @Mock
	    private TrackingService service;

	    @Mock
	    private FileService fileService;

	    @Mock
	    private DocumentRepository documentRepo;

	    @Mock
	    private DeliveryProofRepository proofRepo;

	    @InjectMocks
	    private TrackingController controller;

	    // ✅ GET EVENTS (with field validation)
	    @Test
	    void testGetEvents() {

	        Long deliveryId = 1L;

	        TrackingEvent event = new TrackingEvent();
	        event.setDeliveryId(deliveryId);
	        event.setStatus("IN_TRANSIT");
	        event.setLocation("Delhi");
	        event.setTimestamp(LocalDateTime.now());

	        Mockito.when(service.getEvents(deliveryId))
	                .thenReturn(List.of(event));

	        List<TrackingEvent> result = controller.getEvents(deliveryId);

	        Assertions.assertEquals(1, result.size());
	        Assertions.assertEquals("Delhi", result.get(0).getLocation());

	        Mockito.verify(service).getEvents(deliveryId);
	    }

	    // ✅ UPLOAD DOCUMENT (with ArgumentCaptor 🔥)
	    @Test
	    void testUploadDocument() throws IOException {

	        MockMultipartFile file = new MockMultipartFile(
	                "file",
	                "invoice.pdf",
	                "application/pdf",
	                "dummy content".getBytes()
	        );

	        Long deliveryId = 1L;
	        String userId = "user123";
	        String docType = "INVOICE";

	        Mockito.when(fileService.saveFile(file))
	                .thenReturn("uploads/invoice.pdf");

	        Document savedDoc = new Document();
	        savedDoc.setId(1L);
	        savedDoc.setUserId(userId);

	        Mockito.when(service.saveDocument(Mockito.any(Document.class)))
	                .thenReturn(savedDoc);

	        Document result = controller.uploadDocument(file, deliveryId, userId, docType);

	        Assertions.assertNotNull(result);

	        // 🔥 Capture actual Document passed
	        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
	        Mockito.verify(service).saveDocument(captor.capture());

	        Document captured = captor.getValue();

	        Assertions.assertEquals("uploads/invoice.pdf", captured.getFileUrl());
	        Assertions.assertEquals("invoice.pdf", captured.getFileName());
	        Assertions.assertEquals("INVOICE", captured.getDocType());
	        Assertions.assertEquals(userId, captured.getUserId());
	    }

	    // ✅ GET DOCUMENT
	    @Test
	    void testGetDocument() {

	        Long id = 1L;

	        Document doc = new Document();
	        doc.setId(id);
	        doc.setFileUrl("uploads/test.pdf");
	        doc.setFileName("test.pdf");

	        Mockito.when(documentRepo.findById(id))
	                .thenReturn(Optional.of(doc));

	        ResponseEntity<Resource> response = controller.getDocument(id);

	        Assertions.assertEquals(200, response.getStatusCodeValue());
	        Assertions.assertNotNull(response.getBody());

	        // header validation 🔥
	        Assertions.assertTrue(
	                response.getHeaders()
	                        .getFirst("Content-Disposition")
	                        .contains("test.pdf")
	        );

	        Mockito.verify(documentRepo).findById(id);
	    }

	    // ❌ DOCUMENT NOT FOUND
	    @Test
	    void testGetDocument_NotFound() {

	        Mockito.when(documentRepo.findById(1L))
	                .thenReturn(Optional.empty());

	        Assertions.assertThrows(RuntimeException.class, () -> {
	            controller.getDocument(1L);
	        });
	    }

	    // ✅ UPLOAD PROOF (with ArgumentCaptor 🔥)
	    @Test
	    void testUploadProof() throws IOException {

	        MockMultipartFile file = new MockMultipartFile(
	                "file",
	                "proof.jpg",
	                "image/jpeg",
	                "dummy image".getBytes()
	        );

	        Long deliveryId = 1L;
	        String userId = "user123";
	        String proofType = "DELIVERED";

	        Mockito.when(fileService.saveFile(file))
	                .thenReturn("uploads/proof.jpg");

	        DeliveryProof proof = new DeliveryProof();
	        proof.setId(1L);
	        proof.setUserId(userId);

	        Mockito.when(service.saveProof(Mockito.any(DeliveryProof.class)))
	                .thenReturn(proof);

	        DeliveryProof result = controller.uploadProof(file, deliveryId, userId, proofType);

	        Assertions.assertNotNull(result);

	        // 🔥 Capture proof
	        ArgumentCaptor<DeliveryProof> captor = ArgumentCaptor.forClass(DeliveryProof.class);
	        Mockito.verify(service).saveProof(captor.capture());

	        DeliveryProof captured = captor.getValue();

	        Assertions.assertEquals("uploads/proof.jpg", captured.getImageUrl());
	        Assertions.assertEquals("DELIVERED", captured.getProofType());
	        Assertions.assertEquals(userId, captured.getUserId());
	    }

	    // ✅ GET PROOF
	    @Test
	    void testGetProof() {

	        Long id = 1L;

	        DeliveryProof proof = new DeliveryProof();
	        proof.setId(id);
	        proof.setImageUrl("uploads/proof.jpg");

	        Mockito.when(proofRepo.findById(id))
	                .thenReturn(Optional.of(proof));

	        ResponseEntity<Resource> response = controller.getProof(id);

	        Assertions.assertEquals(200, response.getStatusCodeValue());
	        Assertions.assertNotNull(response.getBody());

	        Mockito.verify(proofRepo).findById(id);
	    }

	    // ❌ PROOF NOT FOUND
	    @Test
	    void testGetProof_NotFound() {

	        Mockito.when(proofRepo.findById(1L))
	                .thenReturn(Optional.empty());

	        Assertions.assertThrows(RuntimeException.class, () -> {
	            controller.getProof(1L);
	        });
	    }
}
