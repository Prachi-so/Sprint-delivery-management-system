package com.lpu.admin_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.admin_service.dto.DashboardResponse;
import com.lpu.admin_service.dto.ReportResponse;

import com.lpu.admin_service.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
    private AdminService service;

    // DASHBOARD
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public DashboardResponse dashboard(
    		) {

//        if (!"ADMIN".equals(role)) {
//        	System.out.println("ROLE = " + role);
//            throw new RuntimeException("Access Denied");
//        }

        return service.getDashboard();
    }

    // STATUS REPORT
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/reports/status")
    public List<ReportResponse> statusReport(
            ) {

//        if (!"ADMIN".equals(role)) {
//            throw new RuntimeException("Access Denied");
//        }

        return service.getStatusReport();
    }

    // DAILY REPORT
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/reports/daily")
    public List<ReportResponse> dailyReport(
          ) {

//        if (!"ADMIN".equals(role)) {
//            throw new RuntimeException("Access Denied");
//        }

        return service.getDailyReport();
    }
}
