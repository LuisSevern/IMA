package main.java.com.ima.dev.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;

//import java.io.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//import java.sql.ResultSet;

//import javax.sql.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.ima.dev.dto.Q1Premise;
import main.java.com.ima.dev.dto.Q2CustomerInfo;
import main.java.com.ima.dev.dto.Q3CustomerInfo;
import main.java.com.ima.dev.dto.Q5MiuMeterRelation;
import main.java.com.ima.dev.dto.Q6MWCustomerInfo;
import main.java.com.ima.dev.dto.Q7SwapModule;
import main.java.com.ima.dev.dto.Q9DeviceInfo;

public class DBConnection {

	private Connection connection = null;
	private ResultSet rs = null;
	private Statement st = null;
	private boolean autocommitOn;
	private LoggingSQLCommands sqllog = new LoggingSQLCommands();
	// connect to websphere datasource
	InitialContext ctx = null;
	DataSource dataSource = null;

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	final private String host = "localhost:3306";
	final private String user = "developer";
	final private String passwd = "developer";
	private static final Logger logger = LogManager.getLogger(DBConnection.class);

	private static String url;
	static {
		StringBuilder salida = new StringBuilder();
		
		
		try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("jdbc.properties")){
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			String url= System.getenv("IMA_DB_URL");
			if (url==null || url.isEmpty()) {
				url = prop.getProperty("db.url")!=null?prop.getProperty("db.url"):"localhost";
			}
			String user = prop.getProperty("db.user");
			String pass = prop.getProperty("db.password");
			salida.append("jdbc:mysql://[(host=").append(url).append(",port=3306,user=").append(user)
					.append(",password=").append(pass)
					.append(")]/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");

		} catch (IOException ex) {
			logger.error("Error al obtener cadena de conexión de bbdd.");
		}

		url = salida.toString();
	}

	public DBConnection() throws Exception {
		this(true);
	}

	/**
	 * Constructor para crear conexion especificando si se quiere autocommit o no.
	 * 
	 * @param autoCommitOn boolean
	 * @throws Exception 
	 */
	public DBConnection(boolean autoCommitOn) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

			// Setup the connection with the DB
			/*
			 * connect = DriverManager .getConnection("jdbc:mysql://" + host + "/world?" +
			 * "user=" + user + "&password=" + passwd );
			 */
			if (url == null || url.isEmpty()) {
				logger.error("DBConnection. Error when gettig connection url");
				throw new Exception("DBConnection. Error when gettig connection url.");
			}
			connection = DriverManager.getConnection(url);
			// "jdbc:mysql://[(host=localhost,port=3306,user=luis,password=luis)]/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
			connection.setAutoCommit(autoCommitOn);
			this.autocommitOn = autoCommitOn;
			logger.debug("DBConnection. Connected!");

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			logger.error("DBConnection. Error when connecting", e);
		}
	}

	// Accessing to DB through the Datasource defined in server
	/*
	 * public DBConnection(){
	 * 
	 * // Production try { String ds = "java:comp/env/jdbc/db"; InitialContext
	 * context = new InitialContext(); this.dataSource =
	 * (DataSource)context.lookup(ds); if (this.dataSource != null){ connection =
	 * this.dataSource.getConnection(); } } catch (NamingException e) {
	 * System.out.println("Exception in the context creation: 1"); } catch
	 * (SQLException e){ System.out.println("Exception in the context creation: 2");
	 * } } / // Error Control in AMM IE. End
	 * 
	 * public String testDataBase () { String value = ""; String serialNumberMeter =
	 * "09I9M5N2102800026"; String sql =
	 * "select neuronid from rmm.ce where serialnumber = '" + serialNumberMeter +
	 * "'";
	 * 
	 * try { st = connection.createStatement(); if (st == null){
	 * System.err.println("THERE IS NOT ST"); } else {
	 * System.err.println("THERE IS ST"); } rs = st.executeQuery(sql);
	 * System.err.println("AFTER TO PERFORMING THE QUERY"); while (rs.next()){ value
	 * = rs.getString("neuronid");
	 * System.out.println("The retrieved value has been: " + value); } rs.close();
	 * st.close(); } catch (SQLException e) { e.printStackTrace(); value = "Error";
	 * }
	 * 
	 * return value; }
	 * 
	 * // Error Control in AMM IE. Begin // Closing the DB connection /** Method to
	 * closing the connection
	 * 
	 * @throws SQLException
	 * 
	 */
	/**
	 * Cierra la conexi�n. Si el autocommit no est� activado, hace un rollback.
	 */
	public void closeConnection() {
		try {
			if (this.connection != null) {
				if (!this.autocommitOn) {
					this.connection.rollback();
				}
				this.connection.close();
				logger.debug("closeConnection. connection successfully closed!");
			}
		} catch (SQLException e) {
			logger.error("closeConnection. Error when closing", e);
		}
	}

	/**
	 * Method to retrieving a new DB connection
	 * 
	 * @throws SQLException
	 */
	public void getNewConnection() {
		try {
			connection = this.dataSource.getConnection();
			System.err.println("AMM IE: The connection has been getting without errors.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Q1 - get premise info
	public Q1Premise returnPremiseInfo(int woPre, long woNo, String meterSN) {
		Q1Premise premiseInfo = new Q1Premise();
		System.err.println("AMM IE: returnPremiseInfo(" + woPre + ", " + woNo + ", " + meterSN + "). Init");
		try {
			String sql = "select bat.ID AS ASSETID, bat.TYPENAME, bat.SERIAL, bad.STREET, bad.NUMBER, bad.HOUSENAME, bad.CITY, concat(DECODE(wo.REFERENCEPREFIX, 1, 'EXT',3,'DUP','PDA'), CHAR(wo.REFERENCENUMBER)) AS WORK_ORDER_ID, wo.LASTUPDATEDATE, bpe.FIRSTNAME, bpe.LASTNAME from BAM.ASSET bat, BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE bipl, BAM.ADDRESS bad, WOP.WORK_ORDER wo , BAM.PERSON bpe where bat.installpoint = bip.id and bip.installplace = bipl.id and bipl.address =  bad.id and bipl.contact = bpe.id and wo.placeid = bipl.placeid and wo.REFERENCEPREFIX = "
					+ woPre + " and wo.REFERENCENUMBER = " + woNo + " and bat.SERIAL = '" + meterSN
					+ "' and bip.NETWORK = 0";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				premiseInfo.setASSETID(rs.getString("ASSETID"));
				premiseInfo.setTYPENAME(rs.getString("TYPENAME"));
				premiseInfo.setSERIAL(rs.getString("SERIAL"));
				// premiseInfo.setID(rs.getString("ID"));
				premiseInfo.setSTREET(rs.getString("STREET"));
				premiseInfo.setNUMBER(rs.getString("NUMBER"));
				premiseInfo.setHOUSENAME(rs.getString("HOUSENAME"));
				premiseInfo.setCITY(rs.getString("CITY"));
				premiseInfo.setWORK_ORDER_ID(rs.getString("WORK_ORDER_ID"));
				premiseInfo.setLASTUPDATEDATE(rs.getString("LASTUPDATEDATE"));
				premiseInfo.setFIRSTNAME(rs.getString("FIRSTNAME"));
				premiseInfo.setLASTNAME(rs.getString("LASTNAME"));
			}
			st.close();
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		System.err.println("AMM IE: getWaterCustomerDetails(). End");
		return premiseInfo;
	}
	// Error Control in AMM IE. End

	// Q1 - Change install point
	public String changeInstallPoint(int installPoint1, int installPoint2, String serialNumber1, String workOrderRef1,
			String serialNumber2, String workOrderRef2) {
		// Q1SPResult spResult = new Q1SPResult();
		CallableStatement cs = null;
		try {
			System.err.println("AMM IE: changeInstallPoint(). Init");
			String sql = "CALL BAM.SWAPINSTALLPOINT(" + installPoint1 + "," + installPoint2 + "," + serialNumber1 + ","
					+ workOrderRef1 + "," + serialNumber2 + "," + workOrderRef2 + ")";
			cs = connection.prepareCall("CALL BAM.SWAPINSTALLPOINT(?,?,?,?,?,?)");
			// cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(1, installPoint1);
			cs.setInt(2, installPoint2);
			cs.setString(3, serialNumber1);
			cs.setInt(4, Integer.parseInt(workOrderRef1.substring(3).trim()));
			cs.setString(5, serialNumber2);
			cs.setInt(6, Integer.parseInt(workOrderRef2.substring(3).trim()));
			cs.executeUpdate();
			cs.close();
			System.err.println("AMM IE: changeInstallPoint(). End");
			sqllog.writeLog("test", sql);
			// spResult.setResult(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
			// spResult.setResult(false);
		}
		return "true";
	}

	// Q2 - get customer information
	public Q2CustomerInfo returnCustomerInfo(int woPre, long woNo) {

		logger.debug("returnCustomerInfo: Init->");
		Q2CustomerInfo customerInfo = new Q2CustomerInfo();
		try {
	/*		String sql = "select bip.ID, bad.STREET, bad.NUMBER, bad.HOUSENAME, bad.CITY, concat((CASE wo.REFERENCEPREFIX WHEN 1 THEN 'EXT' when 3 then 'DUP' ELSE 'PDA' END), CHAR(wo.REFERENCENUMBER)) AS WORK_ORDER_ID, bpe.FIRSTNAME, bpe.LASTNAME from BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE bipl, BAM.ADDRESS bad, WOP.WORK_ORDER wo , BAM.PERSON bpe where bip.installplace = bipl.id and bipl.address =  bad.id and bipl.contact = bpe.id and wo.placeid = bipl.placeid and wo.REFERENCEPREFIX = "
					+ woPre + " and wo.REFERENCENUMBER =" + woNo;
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			logger.debug("returnCustomerInfo. sql: [%s] - params: [%d %ld]", sql, woPre, woNo);
			while (rs.next()) {
				customerInfo.setCustomerFirstName(rs.getString("FIRSTNAME"));
				customerInfo.setCustomerLastName(rs.getString("LASTNAME"));
				customerInfo.setPremiseCity(rs.getString("CITY"));
				customerInfo.setPremiseHouseName(rs.getString("HOUSENAME"));
				customerInfo.setPremiseNumber(rs.getString("NUMBER"));
				customerInfo.setPremiseStreet(rs.getString("STREET"));
				customerInfo.setWorkOrderID(rs.getString("WORK_ORDER_ID"));
				customerInfo.setInstallPoint(rs.getInt("ID"));
			}
			logger.debug("returnCustomerInfo. Results: [%s] ", customerInfo.toString());
			st.close();
			rs.close();
			*/
			String sql = "select bad.STREET, bad.NUMBER, bad.HOUSENAME, bad.CITY, bpe.FIRSTNAME, bpe.LASTNAME from BAM.ADDRESS bad, BAM.PERSON bpe "
					;
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			logger.debug("returnCustomerInfo. sql: [%s] - params: [%d %ld]", sql, woPre, woNo);
			while (rs.next()) {
				customerInfo.setCustomerFirstName(rs.getString("FIRSTNAME"));
				customerInfo.setCustomerLastName(rs.getString("LASTNAME"));
				customerInfo.setPremiseCity(rs.getString("CITY"));
				customerInfo.setPremiseHouseName(rs.getString("HOUSENAME"));
				customerInfo.setPremiseNumber(rs.getString("NUMBER"));
				customerInfo.setPremiseStreet(rs.getString("STREET"));
			}
			logger.debug("returnCustomerInfo. Results: [%s] ", customerInfo.toString());
			st.close();
			rs.close();
		} catch (Exception e) {
			logger.error("returnCustomerInfo. Exception", e);
		}
		logger.debug("returnCustomerInfo: End <-");
		return customerInfo;
	}

	// Q2 - Get count of workorder and meter number
	public int countWoAndMeter(int woPre, long woNo, String meterSN) {
		logger.debug("countWoAndMeter. Init->");
		int result = 0;
		try {
			String sql = "select count(*) from WOP.WORK_ORDER wo, BAM.INSTALL_POINT ipoint, BAM.INSTALL_PLACE iplace, BAM.ASSET asset where wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo
					+ " and wo.PLACEID = iplace.PLACEID and ipoint.INSTALLPLACE = iplace.id and asset.INSTALLPOINT = ipoint.id and asset.SERIAL ='"
					+ meterSN + "'";
			st = connection.createStatement();
			logger.debug("countWoAndMeter. sql: [%s] - params: [%d %ld %s]", sql, woPre, woNo, meterSN);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				result = rs.getInt(1);
			}
			logger.debug("countWoAndMeter. Result: [%d] ", result);
			st.close();
			rs.close();

		} catch (Exception e) {
			logger.error("countWoAndMeter. Exception", e);
		}
		logger.debug("countWoAndMeter. End <-");
		return result;
	}

	// Q2 check if actual work order exists, is APDA and of type Electricity
	public int checkWoExistsAPDAElectricity(long woPre, long woNo) {
		System.err.println("AMM IE: checkWoExistsAPDAElectricity(" + woPre + ", " + woNo + "). Init");
		int result = 0;
		try {
//			String sql = "select ipoint.ID from WOP.WORK_ORDER wo, WOP.WO_DEVICE wodev, BAM.INSTALL_POINT ipoint, BAM.INSTALL_PLACE iplace where wo.REFERENCEPREFIX ="+woPre+" and wo.REFERENCENUMBER ="+woNo+" and (wo.STATUS = 4 or wo.STATUS = 2) and (wo.WORKORDERTYPE <> 1 and wo.WORKORDERTYPE <> 10) and wo.id = wodev.workorder and wodev.TARGETID = 'ENEL_CMEC_METER' and wodev.SERIAL is null and wo.PLACEID = iplace.PLACEID and ipoint.INSTALLPLACE = iplace.ID";
			String sql = "select ipoint.ID from WOP.WORK_ORDER wo, WOP.WO_DEVICE wodev, BAM.INSTALL_POINT ipoint, BAM.INSTALL_PLACE iplace where wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo
					+ " and (wo.STATUS = 4 or wo.STATUS = 2) and wo.WORKORDERTYPE in (1, 11, 21, 25) and wo.id = wodev.workorder and wodev.TARGETID = 'ENEL_CMEC_METER' and wodev.SERIAL is null and wo.PLACEID = iplace.PLACEID and ipoint.INSTALLPLACE = iplace.ID";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				result = rs.getInt(1);
			}
			st.close();
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		System.err.println("AMM IE: checkWoExistsAPDAElectricity(). End");
		return result;
	}

	// Q2 - update install point
	public boolean updateInstallPoint(String MeterSN, int iPoint) {
		boolean result = false;
		try {
			String sql = "update BAM.ASSET set INSTALLPOINT = " + iPoint + " where SERIAL = '" + MeterSN + "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// change workorder status
	// Q2 - Update Workorder status
	public boolean updateWOStatus(int woPre, int woNo) {
		logger.debug("updateWOStatus. Init ->");
		boolean result = false;
		try {
			String sql = "update WOP.WORK_ORDER set status = 8 where REFERENCEPREFIX =" + woPre
					+ " and REFERENCENUMBER = " + woNo;
			st = connection.createStatement();
			logger.debug("updateWOStatus. sql: [%s] - params: [%d, %ld]", sql, woPre, woNo);
			st.executeUpdate(sql);
			st.close();
			result = true;
		} catch (Exception e) {
			logger.error("updateWOStatus. Exception", e);
		}
		if (result) {
			logger.debug("updateWOStatus. Successfully Updated!");
		}
		logger.debug("updateWOStatus. End <-");
		return result;
	}

	// change point of delivery
	// Q2 - Update InstallPlace point of delivery
	public boolean updatePOD(int installpoint, String pod) {
		logger.debug("updatePOD. Init ->");
		boolean result = false;
		try {
			String sql = "update BAM.INSTALL_POINT set pointofdelivery ='" + pod + "' where id =" + installpoint;
			st = connection.createStatement();
			logger.debug("updatePOD. sql: [%s] - params: [%d, %s]", sql, pod, installpoint);
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
			if (result) {
				logger.debug("updatePOD. Successfully Updated!");
			}
		} catch (Exception e) {
			logger.error("updatePOD. Exception", e);
		}

		logger.debug("updatePOD. End <-");
		return result;
	}

	// Q3 Customer Info - PDA
	public Q3CustomerInfo getWaterCustomerDetailsPDA(int woPre, long woNo) {
		Q3CustomerInfo customerInfo = new Q3CustomerInfo();
		System.err.println("AMM IE: getWaterCustomerDetailsPDA(" + woPre + ", " + woNo + "). Init");
		try {
			// String sql = "select distinct serial1.SERIAL METERSERIAL, serial1.TYPENAME
			// METERTYPE, serial1.DEVICETYPE METERTYPEID, rf1.SERIAL RFSERIAL, rf1.TYPENAME
			// RFTYPE, rf1.DEVICETYPE RFTYPEID, bad.STREET, bad.NUMBER, bad.HOUSENAME,
			// bad.CITY, wo.PLACEID, wo.ID, concat(DECODE(wo.REFERENCEPREFIX, 1,
			// 'EXT',3,'DUP','PDA'), CHAR(wo.REFERENCENUMBER)) AS WORK_ORDER_ID,
			// bpe.FIRSTNAME, bpe.LASTNAME from BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE
			// bipl, BAM.ADDRESS bad, WOP.WORK_ORDER wo , BAM.PERSON bpe, WOP.WO_DEVICE
			// wodev, (select dev.SERIAL, devtype.TYPENAME, dev.DEVICETYPE from
			// WOP.WORK_ORDER wo, WOP.WO_DEVICE wodev, PCM.DEVICE dev, PCM.DEVICE_TYPE
			// devtype where wo.REFERENCEPREFIX ="+woPre+" and wo.REFERENCENUMBER ="+woNo+"
			// and wo.ID = wodev.workorder and wodev.TARGETID like 'WATER_METER%' and
			// wodev.SERIAL = dev.SERIAL and dev.DEVICETYPE = devtype.id) serial1, (select
			// dev.SERIAL, devtype.TYPENAME, dev.DEVICETYPE from WOP.WORK_ORDER wo,
			// WOP.WO_DEVICE wodev, PCM.DEVICE dev, PCM.DEVICE_TYPE devtype where
			// wo.REFERENCEPREFIX ="+woPre+" and wo.REFERENCENUMBER ="+woNo+" and wo.ID =
			// wodev.workorder and wodev.TARGETID = 'RF_UNIT' and wodev.SERIAL = dev.SERIAL
			// and dev.DEVICETYPE = devtype.id) rf1 where bip.installplace = bipl.id and
			// bipl.address = bad.id and bipl.contact = bpe.id and wo.placeid = bipl.placeid
			// and wo.status = 8 and wo.id = wodev.workorder and wo.REFERENCEPREFIX
			// ="+woPre+" and wo.REFERENCENUMBER ="+woNo;
			String sql = "select distinct serial1.SERIAL METERSERIAL, serial1.TYPENAME METERTYPE, serial1.DEVICETYPE METERTYPEID, rf1.SERIAL RFSERIAL, rf1.TYPENAME RFTYPE, rf1.DEVICETYPE RFTYPEID, rf1.devrelid, bad.STREET, bad.NUMBER, bad.HOUSENAME, bad.CITY, wo.PLACEID, wo.ID, concat(DECODE(wo.REFERENCEPREFIX, 1, 'EXT',3,'DUP','PDA'), CHAR(wo.REFERENCENUMBER)) AS WORK_ORDER_ID, bpe.FIRSTNAME, bpe.LASTNAME from BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE bipl, BAM.ADDRESS bad, WOP.WORK_ORDER wo , BAM.PERSON bpe, WOP.WO_DEVICE wodev, (select dev.SERIAL, devtype.TYPENAME, dev.DEVICETYPE from WOP.WORK_ORDER wo, WOP.WO_DEVICE wodev, PCM.DEVICE dev, PCM.DEVICE_TYPE devtype where wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo
					+ " and wo.ID = wodev.workorder and wodev.TARGETID like 'WATER_METER%' and wodev.SERIAL = dev.SERIAL and dev.DEVICETYPE = devtype.id) serial1, (select dev.SERIAL, devtype.TYPENAME, dev.DEVICETYPE, devrel.id as devrelid from WOP.WORK_ORDER wo, WOP.WO_DEVICE wodev, PCM.DEVICE dev, PCM.DEVICE_TYPE devtype, PCM.DEVICE_RELATION devrel where wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo
					+ " and wo.ID = wodev.workorder and wodev.TARGETID = 'RF_UNIT' and wodev.SERIAL = dev.SERIAL and dev.DEVICETYPE = devtype.id and devrel.source = dev.id) rf1 where bip.installplace = bipl.id and bipl.address =  bad.id and bipl.contact = bpe.id and wo.placeid = bipl.placeid and wo.status = 8 and wo.id = wodev.workorder and wo.REFERENCEPREFIX="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo;
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				customerInfo.setMeterSerial(rs.getString("METERSERIAL"));
				customerInfo.setMeterType(rs.getString("METERTYPE"));
				customerInfo.setRfSerial(rs.getString("RFSERIAL"));
				customerInfo.setRfType(rs.getString("RFTYPE"));
				customerInfo.setStreet(rs.getString("STREET"));
				customerInfo.setHouseNumber(rs.getString("NUMBER"));
				customerInfo.setHouseName(rs.getString("HOUSENAME"));
				customerInfo.setCity(rs.getString("CITY"));
				customerInfo.setWorkorderID(rs.getString("WORK_ORDER_ID"));
				customerInfo.setFirstName(rs.getString("FIRSTNAME"));
				customerInfo.setLastName(rs.getString("LASTNAME"));
				customerInfo.setPlaceID(rs.getString("PLACEID"));
				customerInfo.setMeterTypeID(rs.getInt("METERTYPEID"));
				customerInfo.setRfTypeID(rs.getInt("RFTYPEID"));
				customerInfo.setWoID(rs.getInt("ID"));
				customerInfo.setRelationshipID(rs.getInt("DEVRELID"));
			}
			st.close();
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		System.err.println("AMM IE: getWaterCustomerDetails(). End");
		return customerInfo;
	}

	// Q3 Customer Info - EXT DUP
	public Q3CustomerInfo getWaterCustomerDetails(int woPre, long woNo) {
		Q3CustomerInfo customerInfo = new Q3CustomerInfo();
		System.err.println("AMM IE: getWaterCustomerDetails(" + woPre + ", " + woNo + "). Init");
		try {
			// String sql = "select distinct serial1.SERIAL METERSERIAL, serial1.TYPENAME
			// METERTYPE, serial1.DEVICETYPE METERTYPEID, serial1.SERIAL2 RFSERIAL,
			// serial1.TYPENAME2 RFTYPE, serial1.DEVICETYPE2 RFTYPEID, bad.STREET,
			// bad.NUMBER, bad.HOUSENAME, bad.CITY, wo.PLACEID, wo.ID,
			// concat(DECODE(wo.REFERENCEPREFIX, 1, 'EXT',3,'DUP','PDA'),
			// CHAR(wo.REFERENCENUMBER)) AS WORK_ORDER_ID, bpe.FIRSTNAME, bpe.LASTNAME from
			// BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE bipl, BAM.ADDRESS bad,
			// WOP.WORK_ORDER wo , BAM.PERSON bpe, WOP.WO_DEVICE wodev, (select dev.SERIAL,
			// devtype.TYPENAME, dev.DEVICETYPE, dev2.SERIAL SERIAL2, devtype2.TYPENAME
			// TYPENAME2, dev2.DEVICETYPE DEVICETYPE2 from WOP.WORK_ORDER wo, WOP.WO_DEVICE
			// wodev, PCM.DEVICE dev, PCM.DEVICE_TYPE devtype, PCM.DEVICE dev2,
			// PCM.DEVICE_TYPE devtype2, WOP.WO_DEVICE wodev2 where wo.REFERENCEPREFIX
			// ="+woPre+" and wo.REFERENCENUMBER ="+woNo+" and wo.ID = wodev.workorder and
			// wodev.TARGETID like 'WATER_METER%' and wodev.SERIAL = dev.SERIAL and
			// dev.DEVICETYPE = devtype.id and wodev2.TARGETID = 'RF_UNIT' and wodev2.SERIAL
			// = dev2.SERIAL and dev2.DEVICETYPE = devtype2.id and wodev2.parent = wodev.id)
			// serial1 where bip.installplace = bipl.id and bipl.address = bad.id and
			// bipl.contact = bpe.id and wo.placeid = bipl.placeid and wo.status = 8 and
			// wo.id = wodev.workorder and wo.REFERENCEPREFIX ="+woPre+" and
			// wo.REFERENCENUMBER ="+woNo;
			String sql = "select distinct serial1.devrelid, serial1.SERIAL METERSERIAL, serial1.TYPENAME METERTYPE, serial1.DEVICETYPE METERTYPEID, serial1.SERIAL2 RFSERIAL, serial1.TYPENAME2 RFTYPE, serial1.DEVICETYPE2 RFTYPEID, bad.STREET, bad.NUMBER, bad.HOUSENAME, bad.CITY, wo.PLACEID, wo.ID, concat(DECODE(wo.REFERENCEPREFIX, 1, 'EXT',3,'DUP','PDA'), CHAR(wo.REFERENCENUMBER)) AS WORK_ORDER_ID, bpe.FIRSTNAME, bpe.LASTNAME from BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE bipl, BAM.ADDRESS bad, WOP.WORK_ORDER wo , BAM.PERSON bpe, WOP.WO_DEVICE wodev, (select devrel.id as devrelid, dev.SERIAL, devtype.TYPENAME, dev.DEVICETYPE, dev2.SERIAL SERIAL2, devtype2.TYPENAME TYPENAME2, dev2.DEVICETYPE DEVICETYPE2 from WOP.WORK_ORDER wo, WOP.WO_DEVICE wodev, PCM.DEVICE dev, PCM.DEVICE_RELATION devrel, PCM.DEVICE_TYPE devtype, PCM.DEVICE dev2, PCM.DEVICE_TYPE devtype2, WOP.WO_DEVICE wodev2 where wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo
					+ " and wo.ID = wodev.workorder and wodev.TARGETID like 'WATER_METER%' and wodev.SERIAL = dev.SERIAL and dev.DEVICETYPE = devtype.id and wodev2.TARGETID = 'RF_UNIT' and wodev2.SERIAL = dev2.SERIAL and dev2.DEVICETYPE = devtype2.id   and dev.WOREFERNUMBER = "
					+ woNo
					+ " and wodev2.parent = wodev.id and devrel.source = dev2.id) serial1 where bip.installplace = bipl.id and bipl.address =  bad.id and bipl.contact = bpe.id and wo.placeid = bipl.placeid and wo.status = 8 and wo.id = wodev.workorder and wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo;
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				customerInfo.setMeterSerial(rs.getString("METERSERIAL"));
				customerInfo.setMeterType(rs.getString("METERTYPE"));
				customerInfo.setRfSerial(rs.getString("RFSERIAL"));
				customerInfo.setRfType(rs.getString("RFTYPE"));
				customerInfo.setStreet(rs.getString("STREET"));
				customerInfo.setHouseNumber(rs.getString("NUMBER"));
				customerInfo.setHouseName(rs.getString("HOUSENAME"));
				customerInfo.setCity(rs.getString("CITY"));
				customerInfo.setWorkorderID(rs.getString("WORK_ORDER_ID"));
				customerInfo.setFirstName(rs.getString("FIRSTNAME"));
				customerInfo.setLastName(rs.getString("LASTNAME"));
				customerInfo.setPlaceID(rs.getString("PLACEID"));
				customerInfo.setMeterTypeID(rs.getInt("METERTYPEID"));
				customerInfo.setRfTypeID(rs.getInt("RFTYPEID"));
				customerInfo.setWoID(rs.getInt("ID"));
				customerInfo.setRelationshipID(rs.getInt("DEVRELID"));
			}
			st.close();
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		System.err.println("AMM IE: getWaterCustomerDetails(). End");
		return customerInfo;
	}

	// Q3 - getWaterMeterTypes
	public Map getWaterMeterTypes() {
		Map metHMap = new HashMap();
		try {
			String sql = "select ID, TYPENAME from PCM.DEVICE_TYPE where category = 1";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				metHMap.put(new Integer(rs.getInt("ID")), rs.getString("TYPENAME"));
			}
			st.close();
			rs.close();
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return metHMap;
	}

	// Q3 - getOndeoMiuTypes
	public Map getOndeoMiuTypes() {
		Map metHMap = new HashMap();
		try {
			String sql = "select ID, TYPENAME from PCM.DEVICE_TYPE where category = 8";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				metHMap.put(new Integer(rs.getInt("ID")), rs.getString("TYPENAME"));
			}
			st.close();
			rs.close();
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return metHMap;
	}

	// Q3 - Update PCM.DEVICE
	public boolean updateWaterDevice(int oldDeviceTypeID, int newDeviceTypeID, String deviceSerial, String placeID) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE set DEVICETYPE = " + newDeviceTypeID + " where SERIAL = '" + deviceSerial
					+ "' and DEVICETYPE = " + oldDeviceTypeID + " and PLACEID = '" + placeID + "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q3 - Get InstallPoint ID from InstallPlace
	public int getInstallPointFromInstallPlace(String iPlace) {
		int installPoint = 0;
		try {
			String sql = "select ip.id IPOINTID from BAM.INSTALL_POINT ip, BAM.INSTALL_PLACE iplace where iplace.id = ip.installplace and iplace.placeid = '"
					+ iPlace + "'";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				installPoint = rs.getInt("IPOINTID");
			}
			st.close();
			rs.close();
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return installPoint;
	}

	// Q3 - Update Water Asset
	public boolean updateWaterAsset(int InstallPoint, String newTypeName, String oldTypeName, String Serial) {
		boolean result = false;
		try {
			String sql = "update BAM.ASSET set TYPENAME = '" + newTypeName + "' where SERIAL = '" + Serial
					+ "' and TYPENAME = '" + oldTypeName + "' and INSTALLPOINT =" + InstallPoint;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q3 - Update Water Asset 2
	public boolean updateWaterAsset2(int InstallPoint, String newManufacturer, String Serial) {
		boolean result = false;
		try {
			String sql = "update BAM.ASSET set MANUFACTURER = '" + newManufacturer + "' where SERIAL = '" + Serial
					+ "' and INSTALLPOINT =" + InstallPoint;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q3 - Update Water Meter Type - WOP.WO_DEVICE (applies for all workorder
	// types)
	public boolean updateWaterWODevice(String TypeName, int woID) {
		boolean result = false;
		try {
			String sql = "update WOP.wo_device set typename = '" + TypeName + "' where workorder = " + woID
					+ " and targetid like '%WATER_METER%'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q3.1 - Update MIU Type - Device
	public boolean updateWaterMIUDevice(int oldDeviceTypeID, int newDeviceTypeID, String deviceSerial) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE set DEVICETYPE = " + newDeviceTypeID + " where SERIAL = '" + deviceSerial
					+ "' and DEVICETYPE = " + oldDeviceTypeID;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q3.1 - Update MIU Type - Asset
	public boolean updateWaterMIUAsset(String newTypeName, String oldTypeName, String Serial) {
		boolean result = false;
		try {
			String sql = "update BAM.ASSET set TYPENAME = '" + newTypeName + "' where SERIAL = '" + Serial
					+ "' and TYPENAME = '" + oldTypeName + "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q3.1 - Update MIU Type - WOP.WO_DEVICE - applies for MRWO & NSWO (EXT - DUP)
	public boolean updateWaterMIUWODevice(String TypeName, long woID) {
		boolean result = false;
		try {
			String sql = "update WOP.wo_device set typename = '" + TypeName
					+ "' where parent = (select wodev.parent from WOP.WO_DEVICE wodev, (SELECT ID from WOP.WO_DEVICE where workorder = "
					+ woID
					+ " and targetid like '%WATER_METER%') wodevmeter where wodev.parent = wodevmeter.ID) and targetid like '%RF_UNIT%'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

//		public int getNumOfMiuWaterMeterRelations (){
//			int associatedMeters = 0;
//			eeee
//			return associatedMeters;
//		}

	// Q3.1 - Update MIU Type - WOP.WO_DEVICE - applies only for MMWO (PDA)
	public boolean updateWaterMIUWODevicePDA(String TypeName, long woID) {
		boolean result = false;
		try {
			String sql = "update WOP.wo_device set typename = '" + TypeName + "' where workorder = " + woID
					+ " and targetid like '%RF_UNIT%'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.1 - Update Water Meter Device
	public boolean updateWaterMeterDeviceSN(String newSerial, String oldSerial, String PlaceID) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE set SERIAL = '" + newSerial + "' where serial = '" + oldSerial
					+ "' and placeid = '" + PlaceID + "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.1 - Update Water Meter Serial Asset
	public boolean updateWMSerialAsset(int installPoint, String newSerial, String oldSerial) {
		boolean result = false;
		try {
			String sql = "update BAM.ASSET set SERIAL = '" + newSerial + "' where SERIAL = '" + oldSerial
					+ "' and INSTALLPOINT =" + installPoint;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.1 - Update WOP.WO_DEVICE
	public boolean updateWoDevWM(int woID, String oldMeterSN, String newMeterSN) {
		boolean result = false;
		try {
			String sql = "update WOP.WO_DEVICE set SERIAL = '" + newMeterSN
					+ "' where TARGETID like 'WATER_METER%' and SERIAL = '" + oldMeterSN + "' and WORKORDER =" + woID;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.1 - Update Water Meter Serial - WOP.WO_DEVICE (applies for all workorder
	// types)
	public boolean updateWaterWODeviceSerial(String Serial, int woID) {
		boolean result = false;
		try {
			String sql = "update WOP.wo_device set serial = '" + Serial + "' where workorder = " + woID
					+ " and targetid like '%WATER_METER%'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.1 - Update Water Meter Serial - WOP.WO_DEVICE (applies for all workorder
	// types)
	public boolean updateWaterWODeviceSerialArgs(String Serial, int woID) {
		boolean result = false;
		try {
			String sql = "update WOP.OPN_ARG_VALUE set argvalue = '" + Serial
					+ "' where parentvalue = (select argval.id from WOP.WORK_ORDER wo, WOP.WO_DEV_OPERATION devop, WOP.OPN_ARG_VALUE argval where wo.id = "
					+ woID
					+ " and wo.id = devop.workorder and devop.operationname = 'PULSE_WATERMETER_CONNECT' and devop.id = argval.wodevoperation and argval.argname = 'PULSE_WATERMETER_CONNECT.INSTALLATION_OUT_DATA') and argname = 'PULSE_WATERMETER_CONNECT.INSTALLATION_OUT_DATA.CORRECTED_SERIAL_NUMBER'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.2 - Update MIU Serial - Device
	public boolean updateWaterMIUSerialDevice(String oldMIUSerial, String newMIUSerial, int deviceType) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE set SERIAL = '" + newMIUSerial + "' where SERIAL = '" + oldMIUSerial
					+ "' and DEVICETYPE = " + deviceType;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.1 - Update MIU Type - Asset
	public boolean updateWaterMIUSerialAsset(String oldMIUSerial, String newMIUSerial, String deviceTypeName) {
		boolean result = false;
		try {
			String sql = "update BAM.ASSET set SERIAL = '" + newMIUSerial + "' where SERIAL = '" + oldMIUSerial
					+ "' and TYPENAME = '" + deviceTypeName + "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.1 - Update WOP.WO_DEVICE
	public boolean updateWoDevMIU(long woID, String oldMIUSN, String newMIUSN) {
		boolean result = false;
		try {
			String sql = "update WOP.WO_DEVICE set SERIAL = '" + newMIUSN
					+ "' where TARGETID = 'RF_UNIT' and SERIAL = '" + oldMIUSN + "' and WORKORDER =" + woID;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4 - update RF meter in args
	public boolean updateRFMeterArgs(String value, long woid) {
		boolean result = false;
		try {
			String sql = "update WOP.OPN_ARG_VALUE set argvalue = '" + value
					+ "' where wodevoperation = (select devop.id from WOP.WORK_ORDER wo, WOP.WO_DEV_OPERATION devop where wo.id = "
					+ woid
					+ " and wo.id = devop.workorder and devop.operationname = 'PULSE_WATERMETER_CONNECT') and argname = 'PULSE_WATERMETER_CONNECT.ELSTER_UNIT_SERIAL'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.2 - Update MIU Type - WOP.WO_DEVICE - applies for MRWO & NSWO (EXT - DUP)
	public boolean updateWaterMIUSerialWODevice(String Serial, long woID) {
		boolean result = false;
		try {
			String sql = "update WOP.wo_device set serial = '" + Serial
					+ "' where parent = (select wodev.parent from WOP.WO_DEVICE wodev, (SELECT ID from WOP.WO_DEVICE where workorder = "
					+ woID
					+ " and targetid like '%WATER_METER%') wodevmeter where wodev.parent = wodevmeter.ID) and targetid like '%RF_UNIT%'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q4.2 - Update MIU Type - WOP.WO_DEVICE - applies only for MMWO (PDA)
	public boolean updateWaterMIUSerialWODevicePDA(String Serial, long woID) {
		boolean result = false;
		try {
			String sql = "update WOP.wo_device set serial = '" + Serial + "' where workorder = " + woID
					+ " and targetid like '%RF_UNIT%'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q5 - View Meters connected to RF
	public ArrayList getRFDetails(String rFSerial) {
		logger.debug("getRFDetails: Init ->");
		ArrayList aMiuMeterDetails = new ArrayList();
		try {
			String sql = "select dev.SERIAL MIUSERIAL, devtype.TYPENAME, devrel.ID, devrel.SOURCE, devrel.TARGET, dev2.SERIAL METERSERIAL, devreldt.ELEMENTVALUE, wo.id as woid from PCM.DEVICE dev, PCM.DEVICE_RELATION devrel, PCM.DEVICE dev2, PCM.DEVREL_DATA devreldt, PCM.DEVICE_TYPE devtype, WOP.WORK_ORDER wo where dev.serial ='"
					+ rFSerial
					+ "' and devrel.source = dev.id and devrel.target = dev2.id and devreldt.DEVRELATIONSHIP = devrel.ID and devreldt.ELEMENTNAME = 'portNumber' and dev.devicetype = devtype.id and dev2.woreferprefix = wo.referenceprefix and dev2.worefernumber = wo.referencenumber"
					+ " ORDER BY devreldt.ELEMENTVALUE"; // CQ 5171. Meters are not always ordered by port number. Data
															// from PCM.DEVREL_DATA needs to be ordered by the
															// ELEMENTVALUE field when getting the port numbers
			st = connection.createStatement();
			logger.debug("countWoAndMeter. sql: [%s] - params: [%s]", sql, rFSerial);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Q5MiuMeterRelation rfInfo = new Q5MiuMeterRelation();
				rfInfo.setMiuSerial(rs.getString("MIUSERIAL"));
				rfInfo.setId(rs.getInt("ID"));
				rfInfo.setSource(rs.getInt("SOURCE"));
				rfInfo.setTarget(rs.getInt("TARGET"));
				rfInfo.setMeterSerial(rs.getString("METERSERIAL"));
				rfInfo.setPort(rs.getInt("ELEMENTVALUE"));
				rfInfo.setMiuType(rs.getString("TYPENAME"));
				rfInfo.setWoid(rs.getInt("WOID"));
				aMiuMeterDetails.add(rfInfo);
			}
			logger.debug("countWoAndMeter. Results: [%d]", aMiuMeterDetails.size());
			st.close();
			rs.close();
		} catch (Exception e) {
			logger.error("getRFDetails. Exception", e);
		}
		logger.debug("getRFDetails: End <-");
		return aMiuMeterDetails;
	}

	// Q5 - View Meters connected to RF based on WOID
	public String getRfSerial(int devrelId) {
		String rfSerial = "";
		try {
			String sql = "select dev.SERIAL from PCM.DEVICE_RELATION devrel, PCM.DEVICE dev " + "where devrel.ID = "
					+ devrelId + " and dev.id = devrel.source";
			st = connection.createStatement();
			logger.debug("getRfSerial. sql: [%s] - params: [%d]", sql, devrelId);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				rfSerial = rs.getString("SERIAL");
			}
			logger.debug("getRfSerial. Results: [%s] ", !rfSerial.isEmpty() ? rfSerial : "");
			st.close();
			rs.close();
		} catch (Exception e) {
			logger.error("getRFSerial. Exception", e);
		}
		logger.debug("getRFSerial: End <-");
		return rfSerial;
	}

	// Q5 - View Meters connected to RF
	public boolean updateRFMeterPort(String value, int relationship) {
		logger.debug("updateRFMeterPort: Init ->");
		boolean result = false;
		try {
			String sql = "update PCM.DEVREL_DATA set ELEMENTVALUE = '" + value + "' where DEVRELATIONSHIP ="
					+ relationship + " and ELEMENTNAME = 'portNumber'";
			st = connection.createStatement();
			logger.debug("getRfSerial. sql: [%s] - params: [%s, %d]", sql, value, relationship);
			st.executeUpdate(sql);
			st.close();
			result = true;
		} catch (Exception e) {
			logger.error("updateRFMeterPort. Exception", e);
		}
		logger.debug("updateRFMeterPort. [%s] ", result ? "Successfully Updated!" : "No updates carried out");
		logger.debug("updateRFMeterPort: End <-");
		return result;
	}

	// Q5 - update RF meter port arguments
	public boolean updateRFMeterPortArgs(String value, long woid) {
		boolean result = false;
		try {
			String sql = "update WOP.OPN_ARG_VALUE set argvalue = '" + value
					+ "' where wodevoperation = (select devop.id from WOP.WORK_ORDER wo, WOP.WO_DEV_OPERATION devop where wo.id = "
					+ woid
					+ " and wo.id = devop.workorder and devop.operationname = 'PULSE_WATERMETER_CONNECT') and argname = 'PULSE_WATERMETER_CONNECT.CONNECTED_PORT_ID'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			logger.error("updateRFMeterPortArgs. Exception", e);
		}
		logger.debug("updateRFMeterPortArgs. [%s] ", result ? "Successfully Updated!" : "No updates carried out");
		logger.debug("updateRFMeterPortArgs: End <-");
		return result;
	}

	// Q6 check if actual work order exists, is APDA and of type Water
	public int checkWoExistsAPDAWater(int woPre, int woNo) {
		int result = 0;
		try {
			String sql = "select ipoint.ID from WOP.WORK_ORDER wo, WOP.WO_DEVICE wodev, BAM.INSTALL_POINT ipoint, BAM.INSTALL_PLACE iplace where wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo
					+ " and wo.STATUS = 4 and (wo.WORKORDERTYPE <> 1 and wo.WORKORDERTYPE <> 10) and wo.id = wodev.workorder and wodev.TARGETID like 'WATER_METER%' and wo.PLACEID = iplace.PLACEID and ipoint.INSTALLPLACE = iplace.ID";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				result = rs.getInt(1);
			}
			st.close();
			rs.close();
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q6 - customer info fixed
	public Q6MWCustomerInfo returnWaterCustomerInfo(int woPre, long woNo) {
		Q6MWCustomerInfo customerInfo = new Q6MWCustomerInfo();
		System.err.println("AMM IE: returnWaterCustomerInfo(" + woPre + ", " + woNo + "). Init");
		try {
			String sql = "select asset.id AID, bip.id IP, asset.serial METER, dev.DEVICETYPE, dev2.SERIAL MIU, bad.STREET, bad.NUMBER, bad.HOUSENAME, bad.CITY, concat((CASE wo.REFERENCEPREFIX WHEN 1 THEN 'EXT' when 3 then 'DUP' ELSE 'PDA' END), (wo.REFERENCENUMBER)) AS WORK_ORDER_ID, bipl.PLACEID, bpe.FIRSTNAME, bpe.LASTNAME from BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE bipl, BAM.ADDRESS bad, WOP.WORK_ORDER wo , BAM.PERSON bpe, BAM.ASSET asset, PCM.DEVICE dev LEFT OUTER JOIN PCM.DEVICE_RELATION devrel on dev.id = devrel.target and dev.id = devrel.target LEFT OUTER JOIN PCM.DEVICE dev2 on devrel.source = dev2.id where bip.installplace = bipl.id and bipl.address =  bad.id and bipl.contact = bpe.id and wo.placeid = bipl.placeid and bip.id = asset.installpoint and asset.serial = dev.serial and wo.REFERENCEPREFIX = dev.WOREFERPREFIX and wo.REFERENCENUMBER = dev.WOREFERNUMBER and wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo + " and bip.NETWORK = 2";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				customerInfo.setCustomerFirstName(rs.getString("FIRSTNAME"));
				customerInfo.setCustomerLastName(rs.getString("LASTNAME"));
				customerInfo.setPremiseCity(rs.getString("CITY"));
				customerInfo.setPremiseHouseName(rs.getString("HOUSENAME"));
				customerInfo.setPremiseNumber(rs.getString("NUMBER"));
				customerInfo.setPremiseStreet(rs.getString("STREET"));
				customerInfo.setWorkOrderID(rs.getString("WORK_ORDER_ID"));
				customerInfo.setInstallPoint(rs.getInt("IP"));
				customerInfo.setMeterSerial(rs.getString("METER"));
				customerInfo.setMiuSerial(rs.getString("MIU"));
				customerInfo.setAssetID(rs.getInt("AID"));
				customerInfo.setDeviceType(rs.getInt("DEVICETYPE"));
				customerInfo.setPlaceID(rs.getString("PLACEID"));
			}
			st.close();
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		System.err.println("AMM IE: returnWaterCustomerInfo(). End");
		return customerInfo;
	}

	/*
	 * //Q6 - Update PCM.DEVICE DEVICE SERIAL for swapped meter public boolean
	 * updateWaterDeviceSerial(String WMSerial, String placeID, int refPre, int
	 * refNo ) { boolean result = false; try { String sql =
	 * "update PCM.DEVICE set SERIAL = '"+WMSerial+"' where PLACEID = '"
	 * +placeID+"' and WOREFERPREFIX = "+refPre+" and WOREFERNUMBER ="+refNo; st =
	 * connection.createStatement(); st.executeUpdate(sql); st.close(); result =
	 * true; } catch (Exception e) { System.out.println("Exception is :" + e); }
	 * return result; }
	 * 
	 * //Q6 - Update PCM.DEVICE DEVICE TYPE for swapped meter public boolean
	 * updateWaterDeviceType(int dType, String placeID, int refPre, int refNo ) {
	 * boolean result = false; try { String sql =
	 * "update PCM.DEVICE set DEVICETYPE = "+dType+" where PLACEID = '"
	 * +placeID+"' and WOREFERPREFIX = "+refPre+" and WOREFERNUMBER ="+refNo; st =
	 * connection.createStatement(); st.executeUpdate(sql); st.close(); result =
	 * true; } catch (Exception e) { System.out.println("Exception is :" + e); }
	 * return result; }
	 */

	public boolean updateWaterDeviceSerial_1IP(String WMSerial, int deviceType, String placeID) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE set placeID = '" + placeID + "' where SERIAL = '" + WMSerial
					+ "' and DEVICETYPE =" + deviceType;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	public boolean updateWaterDeviceSerial_2PRE(String WMSerial, int deviceType, int woPrefix) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE set WOREFERPREFIX = " + woPrefix + " where SERIAL = '" + WMSerial
					+ "' and DEVICETYPE =" + deviceType;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	public boolean updateWaterDeviceSerial_3NUM(String WMSerial, int deviceType, int woNumber) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE set WOREFERNUMBER = " + woNumber + " where SERIAL = '" + WMSerial
					+ "' and DEVICETYPE =" + deviceType;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q7 - Water Meter and Module info by workorder
	public Q7SwapModule returnWaterMeterModule(int woPre, long woNo) {
		Q7SwapModule woInfo = new Q7SwapModule();
		System.err.println("AMM IE: returnWaterMeterModule(" + woPre + ", " + woNo + "). Init");
		try {
			String sql = "select concat(DECODE(wo.REFERENCEPREFIX, 1, 'EXT',3,'DUP','PDA'), CHAR(wo.REFERENCENUMBER)) AS WORK_ORDER_ID, dev.serial METER, dev2.serial MIU, devreldt.ELEMENTVALUE PORT, dev.id TARGET, dev2.id SOURCE, devrel.id DEVREL from WOP.WORK_ORDER wo, PCM.DEVICE dev, PCM.DEVICE_RELATION devrel, PCM.DEVICE dev2, PCM.DEVREL_DATA devreldt where wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER = " + woNo
					+ " and wo.PLACEID = dev.PLACEID and dev.id = devrel.TARGET and devrel.source = dev2.id and devrel.id = devreldt.devrelationship and devreldt.elementname = 'portNumber'";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				woInfo.setMetersn(rs.getString("METER"));
				woInfo.setMiuport(rs.getString("PORT"));
				woInfo.setMiusn(rs.getString("MIU"));
				woInfo.setWorkorder(rs.getString("WORK_ORDER_ID"));
				woInfo.setSource(rs.getInt("SOURCE"));
				woInfo.setTarget(rs.getInt("TARGET"));
				woInfo.setDevrelid(rs.getInt("DEVREL"));
			}
			st.close();
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		System.err.println("AMM IE: returnWaterMeterModule(). End");
		return woInfo;
	}

	// Q7 update target with source
	public boolean updateMIUMeterTarget(int target, int devrelid) {
		boolean result = false;
		try {
			String sql = "update PCM.DEVICE_RELATION set target = " + target + " where id = " + devrelid;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			result = true;
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	// Q8 get workorder information using Q6
	public Q6MWCustomerInfo returnWaterCustomerInfoUnexecuted(int woPre, long woNo) {
		Q6MWCustomerInfo customerInfo = new Q6MWCustomerInfo();
		try {
			String sql = "select bip.id IP, bad.STREET, bad.NUMBER, bad.HOUSENAME, bad.CITY, concat((CASE wo.REFERENCEPREFIX WHEN 1 THEN 'EXT' when 3 then 'DUP' ELSE 'PDA' END), (wo.REFERENCENUMBER)) AS WORK_ORDER_ID, bipl.PLACEID, bpe.FIRSTNAME, bpe.LASTNAME from BAM.INSTALL_POINT bip, BAM.INSTALL_PLACE bipl, BAM.ADDRESS bad, WOP.WORK_ORDER wo , BAM.PERSON bpe where bip.installplace = bipl.id and bipl.address =  bad.id and bipl.contact = bpe.id and wo.placeid = bipl.placeid and wo.REFERENCEPREFIX ="
					+ woPre + " and wo.REFERENCENUMBER =" + woNo
					+ " and bip.NETWORK = 2 and (wo.STATUS = 2 or wo.STATUS = 4)";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				customerInfo.setCustomerFirstName(rs.getString("FIRSTNAME"));
				customerInfo.setCustomerLastName(rs.getString("LASTNAME"));
				customerInfo.setPremiseCity(rs.getString("CITY"));
				customerInfo.setPremiseHouseName(rs.getString("HOUSENAME"));
				customerInfo.setPremiseNumber(rs.getString("NUMBER"));
				customerInfo.setPremiseStreet(rs.getString("STREET"));
				customerInfo.setWorkOrderID(rs.getString("WORK_ORDER_ID"));
				customerInfo.setInstallPoint(rs.getInt("IP"));
				customerInfo.setPlaceID(rs.getString("PLACEID"));
			}
			st.close();
			rs.close();
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return customerInfo;
	}

	// patch 3.0
	// Q* - Get count of existing meter with given Serial
	public int countExistingMeter(String meterSN) {

		int result = 0;
		try {
			String sql = "select count(*) from PCM.DEVICE where serial = '" + meterSN + "'";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				result = rs.getInt(1);
			}
			st.close();
			rs.close();
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	/**
	 * Method to retrieve the pcm.device PK of the device identified by SN and type
	 * 
	 * @param serialNumber String to the SN of the device
	 * @param deviceType   int to identify the type of the device
	 * @return long PK in pcm.device
	 */
	public long getDeviceId(String serialNumber, int deviceType) {
		long deviceId = -1;
		try {
			System.err.println("AMM IE: getDeviceId(). Init");
			String sql = "select id from pcm.device where serial = '" + serialNumber + "'" + "and devicetype = "
					+ deviceType;
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				deviceId = rs.getLong(1);
			}
			st.close();
			rs.close();
			System.err.println("AMM IE: Device with SN: " + serialNumber + " and type: " + deviceType
					+ " has the identifier: " + deviceId);
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: getDeviceId(). End");
		return deviceId;
	}

	/**
	 * Method to update the relation between a module and a meter. source <--
	 * correctId
	 * 
	 * @param correctId Correct module Identifier
	 * @param source    Wrong module identifier
	 */
	public void updateModuleMeterRelation(long correctId, long source) {
		long[] vMeterWronglyAssociated;
		try {
			System.err.println("AMM IE: updateModuleMeterRelation(). Init");
			vMeterWronglyAssociated = this.getMetersIdAssociatedToModule(source);
			for (int i = 0; i < vMeterWronglyAssociated.length; i++) {
				if (vMeterWronglyAssociated[i] != -1) {
					String sql = "update pcm.device_relation set source = " + correctId + " where source = " + source
							+ " and target = " + vMeterWronglyAssociated[i];
					st = connection.createStatement();
					int result = st.executeUpdate(sql);
					if (result >= 1) {
						System.err
								.println("AMM IE: Updating of the relation " + source + "/" + vMeterWronglyAssociated[i]
										+ " for this other one " + correctId + "/" + vMeterWronglyAssociated[i]);
					} else {
						System.err.println("AMM IE: Error while performing query");
					}
					st.close();
				}
			}
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: updateModuleMeterRelation(). End");
	}

	/**
	 * Method to delete a device identified by the couple serial number, type
	 * 
	 * @param serialNumber. Device Serial Number
	 * @param deviceType.   Device type
	 */
	public boolean deleteDevice(String serialNumber, int deviceType) {
		boolean valueToReturn = false;
		try {
			System.err.println("AMM IE: deleteDevice(). Init");
			String sql = "delete from pcm.device where serial =  '" + serialNumber + "' and devicetype = " + deviceType;
			st = connection.createStatement();
			int result = st.executeUpdate(sql);
			if (result == 1) {
				System.err.println("AMM IE: Deleting of the row identified as: " + serialNumber + "/" + deviceType);
			} else {
				System.err.println("AMM IE: Error while performing query");
			}
			st.close();
			valueToReturn = true;
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: deleteDevice(). End");
		return valueToReturn;
	}

	/**
	 * Method to retrieve all the meters associated to a module
	 * 
	 * @param idModule long Identifier of the module
	 * @return String[] Array with all the associated meter identifiers
	 */
	public long[] getMetersIdAssociatedToModule(long idModule) {
		long[] vMetersId = { -1, -1, -1, -1 }; // At most, a module will have 4 water meters associated
		int position = 0;
		try {
			System.err.println("AMM IE: getMetersIdAssociatedToModule(). Init");
			String sql = "select target from pcm.device_relation where source = " + idModule;
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				vMetersId[position] = rs.getLong(1);
				position++;
			}
			st.close();
			rs.close();
			System.err.println("AMM IE: Meters associted to module (" + idModule + ")");
			for (int i = 0, j = 1; i < vMetersId.length; i++, j++) {
				System.err.println("Meter " + j + ": " + vMetersId[i]);
			}
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: getMetersIdAssociatedToModule(). End");
		return vMetersId;
	}

	/**
	 * Method to update the data in the pcm.devrel_data table
	 * 
	 * @param vIdRelations String [][] Two dimensional matrix in which the first row
	 *                     stores the incorrect id relation, the second column
	 *                     stores the correct id relation
	 */
	public void updateWrongDataOfDevice(String[][] vIdRelations) {
		try {
			String sql = "";
			System.err.println("AMM IE: updateWrongDataOfDevice(). Init");
			for (int i = 0; i < vIdRelations[0].length; i++) {
				// [0, i] --> incorrect id | [1, i] --> correct
				sql = "update pcm.devrel_data set devrelationship = " + vIdRelations[1][i] + " where devrelationship = "
						+ vIdRelations[0][i];
				st = connection.createStatement();
				int result = st.executeUpdate(sql);
				if (result >= 1) {
					System.err.println("AMM IE: Updating the data of the relationship of the incorrect device: "
							+ vIdRelations[0][i] + " to the correct device: " + vIdRelations[0][i]);
				} else {
					System.err.println("AMM IE: There are not rows related with the module to update");
				}
				st.close();
			}
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: updateWrongDataOfDevice(). End");
	}

	public int getPortOfMeter(String serialMeter) {
		int portNumber = -1;
		try {
			System.err.println("AMM IE: getPortOfMeter(). Init");
			String sql = "select elementvalue from pcm.devrel_data where devrelationship "
					+ "= (select id from pcm.device_relation where target = (select id from pcm.device where serial = '"
					+ serialMeter + "'))" + "and elementname = 'portNumber'";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				portNumber = rs.getInt("elementvalue");
			}
			st.close();
			rs.close();
			System.err.println("AMM IE: Port associated to the meter " + serialMeter + " = " + portNumber);
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: getPortOfMeter(). End");
		return portNumber;
	}

	public String[] getMetersSerialsAssociatedFromModuleSerial(String moduleSerialNumber) {
		String[] vMetersId = { "-1", "-1", "-1", "-1" }; // At most, a module will have 4
															// water meters associated
		String[] vMetersIdAux = { "-1", "-1", "-1", "-1" };
		int position = 0;
		try {
			System.err.println("AMM IE: getMetersSerialsAssociatedFromModuleSerial(). Init");
			String sql1 = "select serial from pcm.device where id in ( ";
			String sql2 = "select target from pcm.device_relation where source = ";
			String sql3 = "(select id from pcm.device where serial = '" + moduleSerialNumber + "' and deviceType = "
					+ 16 + ")"; // It is a Tx4
			// module
			sql1 = sql1 + sql2 + sql3 + " )";
			st = connection.createStatement();
			rs = st.executeQuery(sql1);
			while (rs.next()) {
				vMetersId[position] = rs.getString(1);
				position++;
			}
			st.close();
			rs.close();

			int portNumber = -1;
			for (int i = 0; i < vMetersId.length && !vMetersId[i].equals("-1"); i++) {
				portNumber = this.getPortOfMeter(vMetersId[i]);
				vMetersIdAux[--portNumber] = vMetersId[i];
			}

			System.err.println("AMM IE: Meters associted to module (" + moduleSerialNumber + ")");
			for (int i = 0, j = 1; i < vMetersId.length; i++, j++) {
				System.err.println("Meter " + j + ": " + vMetersId[i]);
			}
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: getMetersSerialsAssociatedFromModuleSerial(). End");
		return vMetersIdAux;
	}

	public void deleteWaterMIUAsset(String miuSerial, String oldMiuTypeName) {
		try {
			System.err.println("AMM IE: deleteWaterMIUAsset(). Init");
			String sql = "delete from bam.asset where serial = '" + miuSerial + "' and typename = '" + oldMiuTypeName
					+ "'";
			st = connection.createStatement();
			int result = st.executeUpdate(sql);
			if (result == 1) {
				System.err.println("AMM IE: Deleting of the row identified as: " + miuSerial + "/" + oldMiuTypeName
						+ " in BAM.ASSET");
			} else {
				System.err.println("AMM IE: Error while performing query");
			}
			st.close();
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: deleteWaterMIUAsset(). End");
	}

	/**
	 * Updating of the install point in which is installed the electrical meter
	 * identified with the serial number supplied
	 * 
	 * @param installPoint Correct install point in which is installed the device
	 * @param meterSerial  Serial number that identify the electrical meter
	 * @return Boolean that indicates how has finished the operation
	 * 
	 */
	public boolean updateInstallPointAndElectricalMeterPartnership(int installPoint, String meterSerial) {
		boolean result = false;
		try {
			System.err.println("AMM IE: updateInstallPointAndElectricalMeterPartnership(). Init");
			String sql = "update BAM.INSTPOINT_EMETER set INSTALLPOINT = " + installPoint + " where SERIAL = '"
					+ meterSerial + "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			result = true;
			System.err.println("AMM IE: updateInstallPointAndElectricalMeterPartnership(). End");
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	/**
	 * Updating of the install point in which is installed the water meter
	 * identified with the serial number supplied
	 * 
	 * @param installPoint Correct install point in which is installed the device
	 * @param meterSerial  Serial number that identify the electrical meter
	 * @return Boolean that indicates how has finished the operation
	 */
	public boolean updateInstallPointAndWaterMeterPartnership(int installPoint, String meterSerial) {
		boolean result = false;
		try {
			System.err.println("AMM IE: updateInstallPointAndWaterMeterPartnership(). Init");
			String sql = "update BAM.INSTPOINT_WMETER set INSTALLPOINT = " + installPoint + " where SERIAL = '"
					+ meterSerial + "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			result = true;
			System.err.println("AMM IE: updateInstallPointAndWaterMeterPartnership(). End");
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	/**
	 * Updating of the typename in the BAM.INSTPOINT_WMETER table.
	 * 
	 * @param typename    new typename
	 * @param meterSerial meterSerial
	 * @return Boolean indicating how has finished the operation.
	 */
	public boolean updateInstallPointWaterTypename(String typename, String meterSerial) {
		boolean result = false;
		try {
			System.err.println("AMM IE: updateInstallPointWaterTypename(). Init");
			String sql = "update BAM.INSTPOINT_WMETER set TYPENAME = '" + typename + "' where SERIAL = '" + meterSerial
					+ "'";
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			result = true;
			System.err.println("AMM IE: updateInstallPointWaterTypename(). End");
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	/**
	 * Method to update the argument related with the install point of a meter
	 * (water or electricity) given a work order id.
	 * 
	 * @param installPoint Correct value of he install point
	 * @param workOrderId  Work order identifier.
	 * @return Boolean that indicates how has finished the operation
	 */
	public boolean updateIntallPointInArguments(int installPoint, String workOrderId) {
		boolean result = false;
		try {
			System.err.println("AMM IE: updateIntallPointInArguments(). Init");
			String sql = "update WOP.WO_ARG_VALUE set ARGVALUE = '" + installPoint
					+ "' where WOARGUMENT = 100 and workorder = " + workOrderId;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			result = true;
			System.err.println("AMM IE: updateIntallPointInArguments(). End");
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}

	/**
	 * Returns the Install point associated to an asset
	 * 
	 * @param assetId The asset identifier.
	 * @return the install point
	 */
	public int findInstallPointFromAssetId(int assetId) {
		int installPoint = -1;
		try {
			System.err.println("AMM IE: findInstallPointFromAssetId(). Init");
			String sql = "SELECT installpoint from bam.asset where id = " + assetId;
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				installPoint = rs.getInt("installpoint");
			}
			st.close();
			rs.close();
			System.err.println("AMM IE: The associated Install Point is: " + installPoint);
		} catch (Exception e) {
			System.err.println("AMM IE: An error has occurred while the operation was performed");
		}
		System.err.println("AMM IE: findInstallPointFromAssetId(). End");
		return installPoint;
	}

	public boolean updateGenericWOStatus(int woPre, int woNo, int status) {
		logger.debug("updateGenericWOStatus. Init ->");
		boolean result = false;
		try {
			String sql = "update WOP.WORK_ORDER set status = " + status + " where REFERENCEPREFIX =" + woPre
					+ " and REFERENCENUMBER = " + woNo;
			st = connection.createStatement();
			logger.debug("updateGenericWOStatus. sql: [%s] - params: [%d, %d]", sql, woNo, status);
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
			if (result) {
				logger.error("updateGenericWOStatus. Successfully Updated!");
			}
		} catch (Exception e) {
			logger.error("updateGenericWOStatus. Exception", e);
		}
		logger.debug("updateGenericWOStatus. End <-");
		return result;
	}

	// Q9 - device work order
	public Q9DeviceInfo deviceWorkOrderInfo(String serialNumber) {
		Q9DeviceInfo device = new Q9DeviceInfo();
		try {
//			String sql = "select device.placeid, dtype.typename, DECODE(device.status,1,'Installed',2,'Need Service',3,'Being Serviced',4,'Dismantled') as status, DECODE(device.woreferprefix,1,'EXT',2,'PDA',3,'DUP') as woreferprefix, device.worefernumber, VARCHAR_FORMAT(device.creationdate,'yyyy/MM/dd HH:mm:ss') as creationdate, VARCHAR_FORMAT(device.lastupdate,'yyyy/MM/dd HH:mm:ss') as lastupdate from PCM.DEVICE device, PCM.DEVICE_TYPE dtype where device.SERIAL ='"+serialNumber+"' and device.deviceType = dtype.id";
			String sql = "select device.placeid, dtype.typename, CASE device.status WHEN 1 THEN 'Installed' WHEN 2  THEN 'Need Service' WHEN 4 THEN 'Being Serviced' ELSE 'Dismantled' END as status, (CASE device.woreferprefix  WHEN 1 THEN 'EXT' when 3 then 'DUP' ELSE 'PDA' END) as woreferprefix, device.worefernumber, DATE_FORMAT(device.creationdate,'%Y-%m-%d %H.%i.%s')  as creationdate, DATE_FORMAT(device.lastupdate,'%Y-%m-%d %H.%i.%s')  as lastupdate from PCM.DEVICE device, PCM.DEVICE_TYPE dtype where device.SERIAL ='"
					+ serialNumber + "' and device.deviceType = dtype.id";
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				device.setSerial(serialNumber);
				device.setPlaceId(rs.getString("PLACEID"));
				device.setDeviceType(rs.getString("TYPENAME"));
				device.setStatus(rs.getString("STATUS"));
				device.setWoreferprefix(rs.getString("WOREFERPREFIX"));
				device.setWorefernumber(rs.getString("WOREFERNUMBER"));
				device.setCreationdate(rs.getString("CREATIONDATE"));
				device.setLastupdate(rs.getString("LASTUPDATE"));
			}
			st.close();
			rs.close();
			sqllog.writeLog("test", sql);
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}

		return device;
	}

	// Q4 Update meter serial (WATER)
	/**
	 * Updating of the install point in which is installed the water meter
	 * identified with the serial number supplied
	 * 
	 * @param installPoint   Correct install point in which is installed the device
	 * @param oldMeterSerial Current Serial number that identify the water meter
	 * @param newMeterSerial New Serial number for this meter
	 * @return Boolean that indicates how has finished the operation
	 */
	public boolean updateInstallPointWaterMeterBySerial(int installPoint, String oldMeterSerial,
			String newMeterSerial) {
		boolean result = false;
		try {
			System.err.println("AMM IE: updateInstallPointWaterMeterBySerial(). Init");
			String sql = "update BAM.INSTPOINT_WMETER set SERIAL = '" + newMeterSerial + "' where SERIAL = '"
					+ oldMeterSerial + "'" + " AND INSTALLPOINT = " + installPoint;
			st = connection.createStatement();
			st.executeUpdate(sql);
			st.close();
			sqllog.writeLog("test", sql);
			result = true;
			System.err.println("AMM IE: updateInstallPointWaterMeterBySerial(). End");
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}
		return result;
	}
}
