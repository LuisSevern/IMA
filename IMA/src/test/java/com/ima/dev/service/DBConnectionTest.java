package test.java.com.ima.dev.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.com.ima.dev.dto.Q2CustomerInfo;
import main.java.com.ima.dev.service.DBConnection;


public class DBConnectionTest {
	private DBConnection dbconn;
	private int woPre;
	private long woNo;
	private String serialNumber;
	
	
	@Before
	public void openConnectionRollback() {
		this.dbconn = new DBConnection(false);
	}
	
	@After
	public void closeConnection() {
		this.dbconn.closeConnection();
	}
	
	@Test
	public void testReturnCustomerInfo() {
		woPre = 1;
		woNo = 105991001;
		Q2CustomerInfo customer = dbconn.returnCustomerInfo(woPre, woNo);
		assertNotNull(customer);
		assertNotNull(customer.getCustomerFirstName());
		assertEquals(customer.getCustomerFirstName(), "Crow´s Nest");
	}

	@Test
	public void testCountWoAndMeter() {
		woPre = 1;
		woNo = 105991001;
		serialNumber = "02456789QYF";
		int count = dbconn.countWoAndMeter(woPre, woNo, serialNumber);
		assertTrue(count > 0);		
	}

	@Test
	public void testCheckWoExistsAPDAElectricity() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateWOStatus() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdatePOD() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRFDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRfSerial() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateRFMeterPort() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateRFMeterPortArgs() {
		fail("Not yet implemented");
	}

	@Test
	public void testReturnWaterCustomerInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateGenericWOStatus() {
		fail("Not yet implemented");
	}

}
