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
	public void openConnectionRollback()throws Exception{
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
		//assertEquals(customer.getCustomerFirstName(), "Crow´s Nest");
		
	}

}
