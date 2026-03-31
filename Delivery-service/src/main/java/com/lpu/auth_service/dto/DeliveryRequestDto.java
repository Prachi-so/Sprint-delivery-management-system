package com.lpu.auth_service.dto;

import com.lpu.auth_service.entity.Address;

public class DeliveryRequestDto {

	private double weight;
    private String type;

    private Address senderAddress;
    private Address receiverAddress;
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Address getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(Address senderAddress) {
		this.senderAddress = senderAddress;
	}
	public Address getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(Address receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public DeliveryRequestDto() {
		super();
	}

    
}
