package main.java.com.ima.dev.dto;

public class Q3CustomerInfo {
	private int woID;
	private String meterSerial;
	private String meterType;
	private String rfSerial;
	private String rfType;
	private String street;
	private String houseNumber;
	private String houseName;
	private String city;
	private String workorderID;
	private String firstName;
	private String lastName;
	private String placeID;
	private int meterTypeID;
	private int rfTypeID;
	private int relationshipID;
	private String[] waterMetersAssociated;
	
	public String getMeterSerial() {
		return meterSerial;
	}
	public void setMeterSerial(String meterSerial) {
		this.meterSerial = meterSerial;
	}
	public String getMeterType() {
		return meterType;
	}
	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}
	public String getRfSerial() {
		return rfSerial;
	}
	public void setRfSerial(String rfSerial) {
		this.rfSerial = rfSerial;
	}
	public String getRfType() {
		return rfType;
	}
	public void setRfType(String rfType) {
		this.rfType = rfType;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWorkorderID() {
		return workorderID;
	}
	public void setWorkorderID(String workorderID) {
		this.workorderID = workorderID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPlaceID() {
		return placeID;
	}
	public void setPlaceID(String placeID) {
		this.placeID = placeID;
	}
	public int getMeterTypeID() {
		return meterTypeID;
	}
	public void setMeterTypeID(int meterTypeID) {
		this.meterTypeID = meterTypeID;
	}
	public int getRfTypeID() {
		return rfTypeID;
	}
	public void setRfTypeID(int rfTypeID) {
		this.rfTypeID = rfTypeID;
	}
	public int getWoID() {
		return woID;
	}
	public void setWoID(int woID) {
		this.woID = woID;
	}
	public int getRelationshipID() {
		return relationshipID;
	}
	public void setRelationshipID(int relationshipID) {
		this.relationshipID = relationshipID;
	}
	public String[] getWaterMetersAssociated() {
		return waterMetersAssociated;
	}
	public void setWaterMetersAssociated(String[] waterMetersAssociated) {
		this.waterMetersAssociated = waterMetersAssociated;
	}
	
}
