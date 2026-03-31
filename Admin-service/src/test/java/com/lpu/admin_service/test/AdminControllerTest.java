package com.lpu.admin_service.test;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.admin_service.controller.AdminController;
import com.lpu.admin_service.dto.DashboardResponse;
import com.lpu.admin_service.dto.ReportResponse;
import com.lpu.admin_service.service.AdminService;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

	@Mock
    private AdminService service;

    @InjectMocks
    private AdminController controller;

    // ✅ DASHBOARD TEST (updated fields)
    @Test
    void testDashboard() {

        DashboardResponse dashboard = new DashboardResponse();

        // setting fields using setters
        dashboard.setTotalDeliveries(100);
        dashboard.setDelivered(60);
        dashboard.setInTransit(30);
        dashboard.setFailed(10);

        Mockito.when(service.getDashboard())
                .thenReturn(dashboard);

        DashboardResponse result = controller.dashboard();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(100, result.getTotalDeliveries());
        Assertions.assertEquals(60, result.getDelivered());
        Assertions.assertEquals(30, result.getInTransit());
        Assertions.assertEquals(10, result.getFailed());

        Mockito.verify(service).getDashboard();
    }

    // ✅ STATUS REPORT TEST (key-value based)
    @Test
    void testStatusReport() {

        ReportResponse r1 = new ReportResponse("DELIVERED", 20);
        ReportResponse r2 = new ReportResponse("IN_TRANSIT", 30);

        List<ReportResponse> list = List.of(r1, r2);

        Mockito.when(service.getStatusReport())
                .thenReturn(list);

        List<ReportResponse> result = controller.statusReport();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("DELIVERED", result.get(0).getKey());
        Assertions.assertEquals(20, result.get(0).getValue());

        Mockito.verify(service).getStatusReport();
    }

    // ✅ DAILY REPORT TEST (key = date)
    @Test
    void testDailyReport() {

        ReportResponse r1 = new ReportResponse("2026-03-27", 10);
        ReportResponse r2 = new ReportResponse("2026-03-26", 15);

        List<ReportResponse> list = List.of(r1, r2);

        Mockito.when(service.getDailyReport())
                .thenReturn(list);

        List<ReportResponse> result = controller.dailyReport();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("2026-03-27", result.get(0).getKey());
        Assertions.assertEquals(10, result.get(0).getValue());

        Mockito.verify(service).getDailyReport();
    }
}
