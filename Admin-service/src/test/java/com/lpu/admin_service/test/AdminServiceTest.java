package com.lpu.admin_service.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.admin_service.dto.DashboardResponse;
import com.lpu.admin_service.dto.DeliveryDto;
import com.lpu.admin_service.dto.ReportResponse;
import com.lpu.admin_service.feign.DeliveryClient;
import com.lpu.admin_service.service.AdminService;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

	@Mock
    private DeliveryClient deliveryClient;

    @InjectMocks
    private AdminService adminService;

   
    @Test
    void testGetDashboard() {

        
        List<DeliveryDto> deliveries = List.of(
                createDelivery("DELIVERED"),
                createDelivery("OUT_FOR_DELIVERY"),
                createDelivery("FAILED"),
                createDelivery("DELIVERED")
        );

        Mockito.when(deliveryClient.getAllForAdmin())
                .thenReturn(deliveries);

       
        DashboardResponse result = adminService.getDashboard();

      
        Assertions.assertEquals(4, result.getTotalDeliveries());
        Assertions.assertEquals(2, result.getDelivered());
        Assertions.assertEquals(1, result.getInTransit());
        Assertions.assertEquals(1, result.getFailed());

        Mockito.verify(deliveryClient).getAllForAdmin();
    }

  
   
    @Test
    void testGetStatusReport() {

       
        List<DeliveryDto> deliveries = List.of(
                createDelivery("DELIVERED"),
                createDelivery("DELIVERED"),
                createDelivery("FAILED"),
                createDelivery(null) // test UNKNOWN case
        );

        Mockito.when(deliveryClient.getAllForAdmin())
                .thenReturn(deliveries);

        
        List<ReportResponse> result = adminService.getStatusReport();

       
        Assertions.assertEquals(3, result.size());

        Map<String, Long> map = result.stream()
                .collect(Collectors.toMap(
                        ReportResponse::getKey,
                        ReportResponse::getValue
                ));

        Assertions.assertEquals(2L, map.get("DELIVERED"));
        Assertions.assertEquals(1L, map.get("FAILED"));
        Assertions.assertEquals(1L, map.get("UNKNOWN"));
    }

  
    @Test
    void testGetDailyReport() {

     
        List<DeliveryDto> deliveries = List.of(
                createDelivery("DELIVERED"),
                createDelivery("FAILED"),
                createDelivery("FAILED")
        );

        Mockito.when(deliveryClient.getAllForAdmin())
                .thenReturn(deliveries);

       
        List<ReportResponse> result = adminService.getDailyReport();

      
        Assertions.assertEquals(2, result.size());

        Map<String, Long> map = result.stream()
                .collect(Collectors.toMap(
                        ReportResponse::getKey,
                        ReportResponse::getValue
                ));

        Assertions.assertEquals(1L, map.get("DELIVERED"));
        Assertions.assertEquals(2L, map.get("FAILED"));
    }

 
   
    @Test
    void testGetDashboard_Exception() {

        Mockito.when(deliveryClient.getAllForAdmin())
                .thenThrow(new RuntimeException("Service Down"));

        Assertions.assertThrows(RuntimeException.class, () -> {
            adminService.getDashboard();
        });
    }

  
    private DeliveryDto createDelivery(String status) {
        DeliveryDto d = new DeliveryDto();
        d.setStatus(status);
        return d;
    }
}
