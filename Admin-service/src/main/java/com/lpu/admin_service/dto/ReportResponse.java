package com.lpu.admin_service.dto;

public class ReportResponse {

	private String key;
    private long value;

    public ReportResponse(String key, long value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public long getValue() { return value; }
    
}
