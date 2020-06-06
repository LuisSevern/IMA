package com.ima.dev.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.com.ima.dev.dto.Q2CustomerInfo;
import main.com.ima.dev.service.DBConnection;

public class DBConnectionTest {

	private int woPre;
	private long woNo;
	private String meterSN;
	private DBConnection dbconn1;

	@Before
	public void openConncection() {
		dbconn1 = new DBConnection(false);

	}

	@After
	public void closeConnection() {
		dbconn1.closeConnection();
	}

	@Test
	public void testReturnCustomerInfo() {
		woPre = 1;
		woNo = 805982010;
		Q2CustomerInfo customerInfo = dbconn1.returnCustomerInfo(woPre, woNo);
		assertNotNull(customerInfo);
		assertNotNull(customerInfo.getCustomerFirstName());
		assertEquals(customerInfo.getCustomerFirstName(), "Actea");
	}

	@Test
	public void countWoAndMeter() {
		woPre = 1;
		woNo = 105991001;
		meterSN = "02456789QYF";
		int contador = dbconn1.countWoAndMeter(woPre, woNo, meterSN);
		assertTrue(contador > 0);
	}
}
