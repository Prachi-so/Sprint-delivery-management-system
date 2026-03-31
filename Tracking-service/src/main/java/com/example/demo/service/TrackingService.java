package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.DeliveryProof;
import com.example.demo.entity.Document;
import com.example.demo.entity.TrackingEvent;
import com.example.demo.repository.DeliveryProofRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.TrackingRepository;



@Service
public class TrackingService {

	    @Autowired
	    private TrackingRepository trackingRepo;

	    @Autowired
	    private DocumentRepository documentRepo;

	    @Autowired
	    private DeliveryProofRepository proofRepo;

	    // Tracking Event
	    public TrackingEvent createEvent(TrackingEvent event) {
	        event.setTimestamp(LocalDateTime.now());
	        return trackingRepo.save(event);
	    }

	   // @Cacheable(value = "tracking", key = "#deliveryId")
	    public List<TrackingEvent> getEvents(Long deliveryId) {
	        return trackingRepo.findByDeliveryId(deliveryId);
	    }

	    // Document
	    public Document saveDocument(Document doc) {
	        doc.setUploadedAt(LocalDateTime.now());
	        return documentRepo.save(doc);
	    }

	    public List<Document> getDocuments(Long deliveryId) {
	        return documentRepo.findByDeliveryId(deliveryId);
	    }

	    // Proof
	    public DeliveryProof saveProof(DeliveryProof proof) {
	        proof.setTimestamp(LocalDateTime.now());
	        return proofRepo.save(proof);
	    }

	    public List<DeliveryProof> getProofs(Long deliveryId) {
	        return proofRepo.findByDeliveryId(deliveryId);
	    }
}
