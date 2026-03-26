package com.example.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TrackingEvent implements Serializable{

	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long id;

	    private Long deliveryId;
	    private String userId;
	    private String status;
	    private String location;
	    private String description;
	    private LocalDateTime timestamp;
		
	    public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getDeliveryId() {
			return deliveryId;
		}
		public void setDeliveryId(Long deliveryId) {
			this.deliveryId = deliveryId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public LocalDateTime getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		public TrackingEvent() {
			super();
		}
	    
	    
	    
	    
	    
}
