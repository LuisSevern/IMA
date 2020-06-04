package main.com.ima.dev.dto;

public class Q2CustomerInfo {
	private String workOrderID;
	private String customerFirstName;
	private String customerLastName;
	private String premiseNumber;
	private String premiseHouseName;
	private String premiseStreet;
	private String premiseCity;
	private int installPoint;
	
	public String getWorkOrderID() {
		return workOrderID;
	}
	public void setWorkOrderID(String workOrderID) {
		this.workOrderID = workOrderID;
	}
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	public String getCustomerLastName() {
		return customerLastName;
	}
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	public String getPremiseNumber() {
		return premiseNumber;
	}
	public void setPremiseNumber(String premiseNumber) {
		this.premiseNumber = premiseNumber;
	}
	public String getPremiseHouseName() {
		return premiseHouseName;
	}
	public void setPremiseHouseName(String premiseHouseName) {
		this.premiseHouseName = premiseHouseName;
	}
	public String getPremiseStreet() {
		return premiseStreet;
	}
	public void setPremiseStreet(String premiseStreet) {
		this.premiseStreet = premiseStreet;
	}
	public String getPremiseCity() {
		return premiseCity;
	}
	public void setPremiseCity(String premiseCity) {
		this.premiseCity = premiseCity;
	}
	public int getInstallPoint() {
		return installPoint;
	}
	public void setInstallPoint(int installPoint) {
		this.installPoint = installPoint;
	}
	

}
