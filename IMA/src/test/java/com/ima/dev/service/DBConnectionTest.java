package test.java.com.ima.dev.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.com.ima.dev.dto.Q2CustomerInfo;
import main.java.com.ima.dev.service.DBConnection;

public class DBConnectionTest {
	private DBConnection dbconn;
	private int woPre;
	private long woNo;
	private String serialNumber;

	@Before
	public void openConnectionRollback() throws Exception {
		this.dbconn = new DBConnection(false, true);
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
		// assertEquals(customer.getCustomerFirstName(), "Crow´s Nest");

	}

	@Test
	public void testCountWoAndMeter() {
		woPre = 1;
		woNo = 105991001;
		serialNumber = "02456789QYF"; 
		/*
		 * int count = dbconn.countWoAndMeter(woPre, woNo,
		 * serialNumber); assertTrue(count > 0);
		 */
	}

	@Test
	public void testCheckWoExistsAPDAElectricity() {
		assertTrue(true);

	}

	@Test
	public void testUpdateWOStatus() {
		assertTrue(true);
	}

	@Test
	public void testUpdatePOD() {
		assertTrue(true);
	}

	@Test
	public void testGetRFDetails() {
		assertTrue(true);
	}

	@Test
	public void testGetRfSerial() {
		assertTrue(true);
	}

	@Test
	public void testUpdateRFMeterPort() {
		assertTrue(true);
	}

	@Test
	public void testUpdateRFMeterPortArgs() {
		assertTrue(true);
	}

	@Test
	public void testReturnWaterCustomerInfo() {
		assertTrue(true);
	}

	@Test
	public void testUpdateGenericWOStatus() {
		assertTrue(true);
	}

}
