package com.lpu.admin_service.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.lpu.admin_service.dto.DashboardResponse;
import com.lpu.admin_service.dto.DeliveryDto;
import com.lpu.admin_service.dto.ReportResponse;
import com.lpu.admin_service.feign.DeliveryClient;

@Service
public class AdminService {

	@Autowired
    private DeliveryClient deliveryClient;

    //DASHBOARD
	//@Cacheable(value = "dashboard")
    public DashboardResponse getDashboard() {

		List<DeliveryDto> deliveries = deliveryClient.getAllForAdmin();

	    long total = deliveries.size();

	    long delivered = deliveries.stream()
	            .filter(d -> "DELIVERED".equalsIgnoreCase(d.getStatus()))
	            .count();

	    long inTransit = deliveries.stream()
	            .filter(d -> "OUT_FOR_DELIVERY".equalsIgnoreCase(d.getStatus()))
	            .count();

	    long failed = deliveries.stream()
	            .filter(d -> "FAILED".equalsIgnoreCase(d.getStatus()))
	            .count();

	    DashboardResponse response = new DashboardResponse();
	    response.setTotalDeliveries(total);
	    response.setDelivered(delivered);
	    response.setInTransit(inTransit);
	    response.setFailed(failed);

	    return response;
    }

    // STATUS REPORT
    public List<ReportResponse> getStatusReport() {

        List<DeliveryDto> deliveries = deliveryClient.getAllForAdmin();

//        Map<String, Long> map = deliveries.stream()
//                .collect(Collectors.groupingBy(
//                        DeliveryDto::getStatus,
//                        Collectors.counting()
//                ));
        
        Map<String, Long> map = deliveries.stream()
        	    .collect(Collectors.groupingBy(
        	        d -> d.getStatus() != null ? d.getStatus() : "UNKNOWN",
        	        Collectors.counting()
        	    ));

        return map.entrySet()
                .stream()
                .map(e -> new ReportResponse(e.getKey(), e.getValue()))
                .toList();
    }

    // DAILY REPORT
    public List<ReportResponse> getDailyReport() {

        List<DeliveryDto> deliveries = deliveryClient.getAllForAdmin();

//        Map<String, Long> map = deliveries.stream()
//                .collect(Collectors.groupingBy(
//                        DeliveryDto::getCreatedDate,
//                        Collectors.counting()
//                ));
        
        Map<String, Long> map = deliveries.stream()
        	    .collect(Collectors.groupingBy(
        	        d -> d.getStatus() != null ? d.getStatus() : "UNKNOWN",
        	        Collectors.counting()
        	    ));

        return map.entrySet()
                .stream()
                .map(e -> new ReportResponse(e.getKey(), e.getValue()))
                .toList();
    }
}
