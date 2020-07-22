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
		assertTrue(false);

	}

	@Test
	public void testUpdateWOStatus() {
		assertTrue(false);
	}

	@Test
	public void testUpdatePOD() {
		assertTrue(false);
	}

	@Test
	public void testGetRFDetails() {
		assertTrue(false);
	}

	@Test
	public void testGetRfSerial() {
		assertTrue(false);
	}

	@Test
	public void testUpdateRFMeterPort() {
		assertTrue(false);
	}

	@Test
	public void testUpdateRFMeterPortArgs() {
		assertTrue(false);
	}

	@Test
	public void testReturnWaterCustomerInfo() {
		assertTrue(false);
	}

	@Test
	public void testUpdateGenericWOStatus() {
		assertTrue(false);
	}

}
