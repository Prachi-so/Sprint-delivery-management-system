package com.lpu.admin_service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.lpu.admin_service.dto.DeliveryDto;

@FeignClient(name = "Delivery-service")
public interface DeliveryClient {

	  @GetMapping("/delivery/admin/all")
	   List<DeliveryDto> getAllForAdmin( );
}
