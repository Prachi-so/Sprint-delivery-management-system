package com.lpu.admin_service.dto;

public class DashboardResponse {

	 private long totalDeliveries;
	 private long delivered;
	 private long inTransit;
	 private long failed;
	 public long getTotalDeliveries() {
		 return totalDeliveries;
	 }
	 public void setTotalDeliveries(long totalDeliveries) {
		 this.totalDeliveries = totalDeliveries;
	 }
	 public long getDelivered() {
		 return delivered;
	 }
	 public void setDelivered(long delivered) {
		 this.delivered = delivered;
	 }
	 public long getInTransit() {
		 return inTransit;
	 }
	 public void setInTransit(long inTransit) {
		 this.inTransit = inTransit;
	 }
	 public long getFailed() {
		 return failed;
	 }
	 public void setFailed(long failed) {
		 this.failed = failed;
	 }
	 public DashboardResponse() {
		super();
	 }
	 
	 
}
