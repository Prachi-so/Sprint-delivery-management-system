package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DeliveryProof;

public interface DeliveryProofRepository extends JpaRepository<DeliveryProof,Long>{

	List<DeliveryProof> findByDeliveryId(Long deliveryId);
}
